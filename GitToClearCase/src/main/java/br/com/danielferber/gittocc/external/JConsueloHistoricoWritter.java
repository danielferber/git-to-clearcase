
package br.com.danielferber.gittocc.external;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Callable;
import static br.com.danielferber.gittocc.external.HistoryPreviewBuilder.CHAR_ENCODING;

/**
 *
 * @author a7hs
 */
public class JConsueloHistoricoWritter implements Callable<Boolean>{
    private final File arquivoHistoricoPath;
    private final String conteudo;
    
    private final File arquivoMarcacaoHistoricoPath;
    private final String marcacaoHistorico;
    
    private final File versionPath;
    private final Long version;
    
    private final File releasePath;
    private final Long release;

    public JConsueloHistoricoWritter(File arquivoHistoricoPath, String conteudo, File arquivoMarcacaoHistoricoPath, String marcacaoHistorico, File versionPath, Long version, File releasePath, Long release) {
        this.arquivoHistoricoPath = arquivoHistoricoPath;
        this.conteudo = conteudo;
        this.arquivoMarcacaoHistoricoPath = arquivoMarcacaoHistoricoPath;
        this.marcacaoHistorico = marcacaoHistorico;
        this.versionPath = versionPath;
        this.version = version;
        this.releasePath = releasePath;
        this.release = release;
    }
    
    public Boolean call() {
        return   doFile(arquivoHistoricoPath, conteudo)
              && doFile(arquivoMarcacaoHistoricoPath, marcacaoHistorico)
              && doFile(versionPath, version.toString())
              && doFile(releasePath, release.toString());
    }
    
    private boolean doFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, CHAR_ENCODING);
            osw.write(content);
            osw.flush();
            osw.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
