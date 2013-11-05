/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.relatorio;

import br.com.danielferber.slf4jtoys.slf4j.profiler.internal.EventData;
import br.com.danielferber.slf4jtoys.slf4j.profiler.internal.EventReader;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.net.www.MeteredStream;

/**
 *
 * @author X7WS
 */
public class Main {

    
    
    public static void main(String ...argv) {
        Relatorio relatorio = new Relatorio();
        
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(argv[0]));
            String line;
            EventReader reader = new EventReader();
            MeterData data = new MeterData();
            while ((line = r.readLine()) != null) {
                if (data.read(line, 'M')) {
                    Map<String, String> context = data.getContext();
                    String category = data.getEventCategory();
                    if (category.startsWith("ClearToolProcess.")) {
                        boolean ok = context.get("exitValue").equals("0");
                        if (category.endsWith(".mkactivity")) {
                            if (ok) {
                                relatorio.mkactivity.ok.adicionar(data);
                            } else {
                                relatorio.mkactivity.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".checkoutDir")) {
                            if (ok) {
                                relatorio.checkoutDir.ok.adicionar(data);
                            } else {
                                relatorio.checkoutDir.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".checkoutFile")) {
                            if (ok) {
                                relatorio.checkoutFile.ok.adicionar(data);
                            } else {
                                relatorio.checkoutFile.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".checkinDir")) {
                            if (ok) {
                                relatorio.checkinDir.ok.adicionar(data);
                            } else {
                                relatorio.checkinDir.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".checkinFile")) {
                            if (ok) {
                                relatorio.checkinFile.ok.adicionar(data);
                            } else {
                                relatorio.checkinFile.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".makeDir")) {
                            if (ok) {
                                relatorio.makeDir.ok.adicionar(data);
                            } else {
                                relatorio.makeDir.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".makeFile")) {
                            if (ok) {
                                relatorio.makeFile.ok.adicionar(data);
                            } else {
                                relatorio.makeFile.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".moveFile")) {
                            if (ok) {
                                relatorio.moveFile.ok.adicionar(data);
                            } else {
                                relatorio.moveFile.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".rmnameFile")) {
                            if (ok) {
                                relatorio.rmnameFile.ok.adicionar(data);
                            } else {
                                relatorio.rmnameFile.bad.adicionar(data);
                            }
                        } else if (category.endsWith(".rmnameDir")) {
                            if (ok) {
                                relatorio.rmnameDir.ok.adicionar(data);
                            } else {
                                relatorio.rmnameDir.bad.adicionar(data);
                            }
                        } else {
                            System.out.println(category);
                        }
                    }
                }                
            }

            System.out.println("                   Atividade     ok [s] T ok[m] ign [s] T ign[m]");
            print("mkactivity", relatorio.mkactivity);
            print("makeDir", relatorio.makeDir);
            print("makeFile", relatorio.makeFile);
            print("checkoutDir", relatorio.checkoutDir);
            print("checkoutFile", relatorio.checkoutFile);
            print("checkinDir", relatorio.checkinDir);
            print("checkinFile", relatorio.checkinFile);
            print("moveFile", relatorio.moveFile);
            print("rmnameDir", relatorio.rmnameDir);
            print("rmnameFile", relatorio.rmnameFile);
            print("total", relatorio);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void print(String titulo, Relatorio.Medida medida) {
        System.out.format("%30s %7.1f %8d %7.1f %8d\n", titulo, medida.ok.getMedia()/1000000000.0, medida.ok.getTotal()/60000000000L, medida.bad.getMedia()/1000000000.0, medida.bad.getTotal()/60000000000L);
    }

    private static void print(String titulo, Relatorio relatorio) {
        System.out.format("%30s           %8.1f         %8.1f\n", titulo, 
                (relatorio.mkactivity.ok.getTotal()+
                relatorio.makeDir.ok.getTotal()+
            relatorio.makeFile.ok.getTotal()+
            relatorio.checkoutDir.ok.getTotal()+
            relatorio.checkoutFile.ok.getTotal()+
            relatorio.checkinDir.ok.getTotal()+
            relatorio.checkinFile.ok.getTotal()+
            relatorio.moveFile.ok.getTotal()+
            relatorio.rmnameDir.ok.getTotal()+
            relatorio.rmnameFile.ok.getTotal())/60000000000.0,
                (relatorio.mkactivity.bad.getTotal()+
                relatorio.makeDir.bad.getTotal()+
            relatorio.makeFile.bad.getTotal()+
            relatorio.checkoutDir.bad.getTotal()+
            relatorio.checkoutFile.bad.getTotal()+
            relatorio.checkinDir.bad.getTotal()+
            relatorio.checkinFile.bad.getTotal()+
            relatorio.moveFile.bad.getTotal()+
            relatorio.rmnameDir.bad.getTotal()+
            relatorio.rmnameFile.bad.getTotal())/60000000000.0);
    }
    
}
