package com.scraping.vagas.Scraping.services;

import com.scraping.vagas.Scraping.dto.ScrapBody;
import com.scraping.vagas.Scraping.exception.ScrapingException;
import com.scraping.vagas.Scraping.model.*;
import com.scraping.vagas.Scraping.repositories.ObjetoInteresseRespository;
import com.scraping.vagas.Scraping.repositories.SiteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ScrapingService {

    private final SiteRepository siteRepository;
    private final ObjetoInteresseRespository objetoInteresseRespository;

    /**
     * Retorna número total de páginas a partir do seletor de navegação do site.
     * Lança ScrapingException se não for possível acessar a página principal.
     */
    public int getTotalPages(SiteModel site) {
        try {
            Document docPages = Jsoup.connect(site.getUrlBase())
                    .timeout(10_000)
                    .get();

            Elements pages = docPages.select(site.getNavPageCssSelector()+" "+site.getPageCssItemCssSelector());
            log.info("Quantidade de cards de página "+pages.size());
            // percorre do fim para o começo buscando o maior número (última página)
            List<Element> pagesList = new ArrayList<>(pages);
            Collections.reverse(pagesList);

            int totalPages = 0;
            for (Element page : pagesList) {
                String n = page.text();
                if (n.matches("\\d+")) {
                    totalPages = Integer.parseInt(n);
                    break; // pegamos a primeira ocorrência após reverso = maior página
                }
            }
            log.info("Foram encontradas "+ totalPages +" páginas");
            return totalPages == 0 ? 1 : totalPages;
        } catch (Exception e) {
            log.error("Erro ao recuperar total de páginas do site {}: {}", site.getUrlBase(), e.getMessage(), e);
            throw new ScrapingException("Erro ao recuperar total de páginas: " + e.getMessage(), e);
        }
    }


    public RelatorioExecucaoModel extrair(CamposObjetoModel campo, Element card, SiteModel site, Boolean text) {
        String r;
        String atributo = campo.getAtributo() == null ? "" : campo.getAtributo();
        //if(Objects.equals(campo.getCampo(), "descricao")) continue;
        if (!atributo.isEmpty()) {
            r = card.select(campo.getSeletor_css()).attr(atributo).trim();
        } else {
            if (text)
                r = card.select(campo.getSeletor_css()).text().trim();
            else
                r = card.select(campo.getSeletor_css()).html();
        }
        RelatorioExecucaoModel rem = new RelatorioExecucaoModel();
        rem.setSite(site);
        rem.setCamposObjeto(campo);
        rem.setResultado(r);

        return rem;
    }


    public List<ObjetoInteresseModel> scrapeAllPages(SiteModel site) {

        int totalPages = getTotalPages(site);
        return scrapeAllPages(site, 1, totalPages);

    }

    /**
     * Realiza scraping de todas as páginas e salva os objetos resultantes.
     * A operação é transacional: ou todos os objetos gravam, ou rollback.
     */
    @Transactional
    public List<ObjetoInteresseModel> scrapeAllPages(SiteModel site, int paginaInicial, int paginaFinal) {
        Set<CamposObjetoModel> campos = site.getCamposObjetos();


        log.info("foram encontrados: "+campos.size());

        // logo após carregar o site
        //log.info("DEBUG: entrou em scrapeAllPages - site_id={}", scrapBody.site_id());


        //System.out.println("O objeto tem "+campos.size()+" campos");
        List<ObjetoInteresseModel> objetos = new ArrayList<>();


        for (int page = paginaInicial; page <= paginaFinal; page++) {
            String url = buildPageUrl(site, page);
            log.info("🔎 Scraping página {}: {}", page, url);
            Document doc;
            try {
                doc = Jsoup.connect(url).timeout(10_000).get();
            } catch (Exception e) {
                // decide-se aqui: logar e continuar ou abortar. Estou logando e pulando esta página.
                log.warn("Falha ao acessar a página {} (url={}): {} — pulando página", page, url, e.getMessage());
                continue;
            }

            Elements cards = doc.select(site.getCardCssSelector());
            System.out.println("Foram encontrados "+ cards.size()+" cards");
            if (cards == null || cards.isEmpty()) {
                log.debug("Nenhum card encontrado na página {} (url={})", page, url);
            }
            System.out.println("O objeto tem "+campos.size()+" campos");
            for (Element card : cards) {
                List<RelatorioExecucaoModel> cardResult = new ArrayList<>();
                //Set<Camp> conteudoObjetoModels = new HashSet<>();
                if (campos == null || campos.isEmpty()) {
                    log.warn("Site id={} não tem campos definidos (getCamposObjetos vazio)", site.getId());
                    continue;
                }
                //ConteudoObjetoModel conteudoObjetoModel = new ConteudoObjetoModel();
                for (var campo : campos) {
                    RelatorioExecucaoModel rem =  extrair(campo, card, site, true);

                    if (rem.getResultado() != null && !rem.getResultado().isEmpty())
                        cardResult.add(rem);

                    if(!campo.getCamposPaginaObjeto().isEmpty()) {
                        log.info("Descrição existe");
                        try {
                            Document desc = Jsoup.connect(rem.getResultado()).timeout(10_000).get();
                            for (var c : campo.getCamposPaginaObjeto()) {
                                RelatorioExecucaoModel rem2 = extrair(c, desc, site, false);
                                if (rem2.getResultado() != null && !rem2.getResultado().isEmpty()) {
                                    cardResult.add(rem2);
                                } else {
                                    continue;
                                }
                                if (rem2.getResultado().length() > 50) {
                                    log.info("Descrição: " + rem2.getResultado().substring(0, 50));
                                } else {
                                    log.info("Descrição: " + rem2.getResultado());
                                }
                            }
                        } catch (Exception e) {
                            // decide-se aqui: logar e continuar ou abortar. Estou logando e pulando esta página.
                            log.warn("Falha ao acessar a página {} (url={}): {} — pulando página", page, rem.getResultado(), e.getMessage());

                        }
                    }
                }

                ObjetoInteresseModel objetoInteresseModel = new ObjetoInteresseModel();
                for (var c : cardResult) {
                    c.setObjetoInteresse(objetoInteresseModel);
                }
                objetoInteresseModel.setRelatoriosExecucao(new HashSet<>(cardResult));
                objetoInteresseModel.setSite(site);



                // exemplo simples de hash para detectar duplicados: concatena os resultados
                String hash = generateHashFromCard(cardResult);
                objetoInteresseModel.setHash(hash);

                objetos.add(objetoInteresseModel);
            }
        }

        if (objetos.isEmpty()) {
            log.info("Nenhum objeto coletado para o site id={}", site.getId());
            return Collections.emptyList();
        }

        objetos = getNovosObjetos(objetos);

        try {
            List<ObjetoInteresseModel> saved = objetoInteresseRespository.saveAll(objetos);
            log.info("Persistidos {} novos objetos para o site id={}", saved.size(), site.getId());
            return saved;
        } catch (Exception e) {
            log.error("Erro ao salvar objetos no banco: {}", e.getMessage(), e);
            throw new ScrapingException("Erro ao persistir objetos: " + e.getMessage(), e);
        }
    }


    private List<ObjetoInteresseModel> getNovosObjetos(List<ObjetoInteresseModel> objetos) {
        // Busca de uma vez todos os que já existem no banco

        List<String> hashesNovos = objetos.stream()
                .map(ObjetoInteresseModel::getHash)
                .toList();

        Set<String> hashesExistentes = objetoInteresseRespository.findAllHashesExistentes(hashesNovos);

        // Filtra só os novos
        List<ObjetoInteresseModel> novos = objetos.stream()
                .filter(obj -> !hashesExistentes.contains(obj.getHash()))
                .toList();

        // Agora salva só os realmente novos
        return novos;
    }


    /** Constrói a URL da página substituindo o número da página conforme seu padrão atual. */
    private String buildPageUrl(SiteModel site, int pageNumber) {
        // se a urlPage for "page=1" por exemplo, troca apenas o "1"
        if (site.getUrlPage() != null && !site.getUrlPage().isEmpty()) {
            return site.getUrlBase().replace(site.getUrlPage(), site.getUrlPage().replace("1", String.valueOf(pageNumber)));
        } else {
            // fallback: appendar param, caso o site não tenha urlPage configurado
            return site.getUrlBase() + "?page=" + pageNumber;
        }
    }

    /** Gera um hash SHA-256 a partir dos resultados dos relatórios do card. */
    private String generateHashFromCard(List<RelatorioExecucaoModel> cardResult) {
        try {
            String joined = cardResult.stream()
                    .sorted(Comparator.comparing(r -> r.getCamposObjeto().getCampo())) // garante ordem fixa
                    .map(r -> r.getCamposObjeto().getCampo() + "=" + Optional.ofNullable(r.getResultado()).orElse(""))
                    .collect(Collectors.joining("||"));

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(joined.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.warn("Falha ao gerar hash do card: {}", e.getMessage());
            return "";
        }
    }

}
