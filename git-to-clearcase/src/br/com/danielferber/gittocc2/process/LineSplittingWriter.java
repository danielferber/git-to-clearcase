package br.com.danielferber.gittocc2.process;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Writer that identifies lines and allows taking an action upon each.
 *
 * @author Daniel Felix Ferber
 */
public abstract class LineSplittingWriter extends Writer {

    private final List<Exception> parseExceptions = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private int end = 0;

    public LineSplittingWriter() {
        super();
    }

    public List<Exception> getParseExceptions() {
        return parseExceptions;
    }

    @Override
    public void write(final char cbuf[], final int off, final int len) throws IOException {
        sb.append(cbuf, off, len);
        final int sblength = sb.length();
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
    
    protected void addParseException(Exception e)  {
        parseExceptions.add(e);
    }

    protected abstract void processLine(String line) throws IOException;

    protected void finished() {
        // ignore
    }
}
