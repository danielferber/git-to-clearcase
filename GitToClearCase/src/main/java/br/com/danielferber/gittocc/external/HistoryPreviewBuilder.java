/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 *
 * @author a7hs
 */
public class HistoryPreviewBuilder implements Callable<String> {
    
    private final History history;
    private final File arquivoHistorico;

    public HistoryPreviewBuilder(History history, File arquivoHistorico) {
        this.history = history;
        this.arquivoHistorico = arquivoHistorico;
    }

    @Override
    public String call() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>")
            .append(getHeader())
            .append(getNovasFuncionalidades())
            .append(getMelhorias())
            .append(getMelhoriasTecnicas())
            .append(getCorrecoes())
          .append("</div>");
        String htmlContentPreview = getHtmlContent(sb.toString());
        return htmlContentPreview;
    }

    private String getHeader() {
        if ("".equals(trimToEmpty(history.getTitulo()))) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>")
          .append(trimToEmpty(history.getTitulo()))
          .append("</h2>");
        return sb.toString();
    }

    private String getNovasFuncionalidades() {
        if (history.getNovasFuncionalidadesList().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        processList(history.getNovasFuncionalidadesList(), sb, "Novas Funcionalidades");
        return sb.toString();
    }

    private String getMelhorias() {
        if (history.getMelhoriasList().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        processList(history.getMelhoriasList(), sb, "Melhorias");
        return sb.toString();
    }

    private String getMelhoriasTecnicas() {
        if (history.getMelhoriasTecnicasList().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        processList(history.getMelhoriasTecnicasList(), sb, "Melhorias Técnicas");
        return sb.toString();
    }

    private String getCorrecoes() {
        if (history.getCorrecoesList().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        processList(history.getCorrecoesList(), sb, "Correções");
        return sb.toString();
    }
    
    private void processList(List<String> list, StringBuilder sb, String title) {
        //Adiciona titulo da lista
        sb.append("<h3>")
          .append(title)
          .append("</h3>");
        //Adiciona conteudo da lista
        sb.append("<ul>");
        for (String s: list) {
            sb.append("<li>")
              .append(s)
              .append("</li>");
        }
        sb.append("</ul>");
    }

    private String getHtmlContent(String newContent) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(arquivoHistorico);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader buffer = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        boolean verifyNewContent = true;
        String line;
        while ((line=buffer.readLine())!=null) {
            sb.append(line).append("\n");
            if (verifyNewContent) {
                if ("<hr />".equals(trimToEmpty(line)) || "<hr/>".equals(trimToEmpty(line))) {
                    sb.append(newContent).append("\n");
                    verifyNewContent = false;
                }
            }
        }
        buffer.close();
        isr.close();
        fis.close();
        return sb.toString();
    }
    
}
