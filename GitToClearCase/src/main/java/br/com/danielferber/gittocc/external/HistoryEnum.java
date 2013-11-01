/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc.external;

/**
 *
 * @author a7hs
 */
public enum HistoryEnum {
    NOVA_FUNCIONALIDADE("[F]"),
    MELHORIA("[M]"),
    CORRECAO("[C]"),
    MELHORIA_TECNICA("[T]"),
    INVALIDO("");
    
    private final String pattern;

    private HistoryEnum(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
    
    public static HistoryEnum getHistoryEnum(String value) {
        for (HistoryEnum he: values()) {
            if (value.toUpperCase().startsWith(he.getPattern())) {
                return he;
            }
        }
        return INVALIDO;
    }
    
}
