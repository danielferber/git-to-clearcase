
package br.com.danielferber.gittocc.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 *
 * @author a7hs
 */
public class JConsueloHistoricoMerge implements Callable<String>{
    private static final String HR_1 = "<hr />";
    private static final String HR_2 = "<hr/>";
    
    private final File arquivoHistorico;
    private final String novoConteudo;
    
    public JConsueloHistoricoMerge(File arquivoHistorico, String novoConteudo) {
        this.arquivoHistorico = arquivoHistorico;
        this.novoConteudo = novoConteudo;
    }

    public String call() throws FileNotFoundException, IOException {
        return getPrefix().concat(getSufix());
    }
    
    private String getPrefix() throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(arquivoHistorico);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader buffer = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line=buffer.readLine())!=null) {
            sb.append(line).append("\n");
            if ("<hr />".equals(trimToEmpty(line)) || "<hr/>".equals(trimToEmpty(line))) {
                sb.append("\n");
                break;
            }
        }
        buffer.close();
        isr.close();
        fis.close();
        return sb.toString();
    }
    
    private String getSufix() {
        int posicao = getPosicao();
        return novoConteudo.substring(posicao, novoConteudo.length());
    }
    
    private int getPosicao() {
        if (novoConteudo.indexOf(HR_1)>0) {
            return novoConteudo.indexOf(HR_1) + HR_1.length();
        }
        return novoConteudo.indexOf(HR_2) + HR_2.length();
    }
    
}
