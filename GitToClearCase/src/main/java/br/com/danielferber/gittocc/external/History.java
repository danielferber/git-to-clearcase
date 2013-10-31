/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc.external;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author a7hs
 */
@SuppressWarnings("FieldMayBeFinal")
public class History {
    private String titulo;
    private Date dataGeracao;
    private List<String> novasFuncionalidadesList;
    private List<String> melhoriasList;
    private List<String> melhoriasTecnicasList;
    private List<String> correcoesList;

    public History(String titulo) {
        this.titulo = titulo;
        novasFuncionalidadesList = new LinkedList<String>();
        melhoriasList = new LinkedList<String>();
        melhoriasTecnicasList = new LinkedList<String>();
        correcoesList = new LinkedList<String>();
        dataGeracao = new Date();
    }

    
    public void addNovaFuncionalidade(String s) {
        novasFuncionalidadesList.add(s);
    }

    public void addMelhoria(String s) {
        melhoriasList.add(s);
    }

    public void addMelhoriaTecnica(String s) {
        melhoriasTecnicasList.add(s);
    }

    public void addCorrecao(String s) {
        correcoesList.add(s);
    }

    public Date getDataGeracao() {
        return dataGeracao;
    }
    
    public String getDataGeracaoAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataGeracao);
    }

    public List<String> getNovasFuncionalidadesList() {
        return novasFuncionalidadesList;
    }

    public List<String> getMelhoriasList() {
        return melhoriasList;
    }

    public List<String> getMelhoriasTecnicasList() {
        return melhoriasTecnicasList;
    }

    public List<String> getCorrecoesList() {
        return correcoesList;
    }

    public String getTitulo() {
        return titulo;
    }
    
}
