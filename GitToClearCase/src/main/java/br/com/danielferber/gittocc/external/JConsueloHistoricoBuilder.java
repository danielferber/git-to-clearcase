/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc.external;

import java.util.concurrent.Callable;

/**
 *
 * @author a7hs
 */
public class JConsueloHistoricoBuilder implements Callable<History> {
    private String content;
    private String titulo;
    
    public JConsueloHistoricoBuilder(String content, String titulo) {
        this.content = content;
        this.titulo = titulo;
        if (content == null) {
            throw new IllegalArgumentException("O conteúdo para gerar Histórico é inválido!");
        }
    }
    
    @Override
    public History call() throws Exception {
        History history = new History(titulo);
        String[] array = content.split("\n");
        for (String s: array) {
            HistoryEnum he = HistoryEnum.getHistoryEnum(s);
            switch (he) {
                case CORRECAO:
                    history.addCorrecao(s); break;
                case MELHORIA:
                    history.addMelhoria(s); break;
                case MELHORIA_TECNICA:
                    history.addMelhoriaTecnica(s); break;
                case NOVA_FUNCIONALIDADE:
                    history.addNovaFuncionalidade(s); break;
            }
        }
        return history;
    }
    
    
}
