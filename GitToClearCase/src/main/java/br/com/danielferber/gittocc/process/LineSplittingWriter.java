package br.com.danielferber.gittocc.process;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer that identifies lines and allows taking an action upon each.
 *
 * @author Daniel Felix Ferber
 */
public abstract class LineSplittingWriter extends Writer {

    StringBuilder sb = new StringBuilder();
    int end = 0;

    public LineSplittingWriter() {
        super();
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        sb.append(cbuf, off, len);
        int sblength = sb.length();
        int start = 0;
        int end = sb.indexOf("\n", start);
        while (end != -1) {
            processLine(sb.subSequence(start, end).toString());
            start = end + 1;
            end = sb.indexOf("\n", start);
        }
        sb = new StringBuilder(sb.subSequence(start, sblength));
    }

    @Override
    public void close() throws IOException {
        finished();
    }

    @Override
    public void flush() throws IOException {
        // ignore
    }

    protected abstract void processLine(String line);

    protected void finished() {
        // ignore
    }
}
