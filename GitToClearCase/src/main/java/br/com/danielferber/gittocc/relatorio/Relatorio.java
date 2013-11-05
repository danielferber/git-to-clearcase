/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.relatorio;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterData;

/**
 *
 * @author X7WS
 */
public class Relatorio {

    public static class Estatistica {

        long contador;
        long soma;
        long maior = Long.MIN_VALUE;
        long menor = Long.MAX_VALUE;

        public double getMedia() {
            return (double) soma / (double) contador;
        }

        public long getContador() {
            return contador;
        }

        void adicionar(MeterData data) {
            long numero = data.getExecutionTime();
            contador++;
            soma += numero;
            if (maior < numero) {
                maior = numero;
            }
            if (menor > numero) {
                menor = numero;
            }
        }
        
        public long getTotal() {
            return soma;
        }
    }

    public static class Medida {

        public final Estatistica ok = new Estatistica();
        public final Estatistica bad = new Estatistica();
    }
    public final Medida mkactivity = new Medida();
    public final Medida checkoutDir = new Medida();
    public final Medida checkoutFile = new Medida();
    public final Medida checkinDir = new Medida();
    public final Medida checkinFile = new Medida();
    public final Medida rmnameFile = new Medida();
    public final Medida rmnameDir = new Medida();
    public final Medida moveFile = new Medida();
    public final Medida makeDir = new Medida();
    public final Medida makeFile = new Medida();
}
