package br.com.danielferber.gittocc2.process;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Repeats the encoded text output stream of a Process to interested Readers and
 * Writers.
 *
 * @author Daniel Felix Ferber
 */
class ProcessOutputRepeater implements Runnable {

    /**
     * Reader that wraps process outputstream and handles encoding.
     */
    Reader reader;
    /**
     * Thread that redirects process output to interested Readers and Writers.
     */
    Thread thread;
    /**
     * Interested writers that receive process output.
     */
    final List<Writer> writers = new ArrayList<>();
    /**
     * Exception raised while redirecting process output.
     */
    Exception exception = null;

    public ProcessOutputRepeater() {
        super();
    }

    public void start(final InputStream inputStream) {
        reader = new InputStreamReader(inputStream);
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void waitFor() throws InterruptedException {
        thread.join();
    }

    public Exception getException() {
        return exception;
    }

    /**
     * Registers an interested Writer to receive the process output.
     *
     * @param writer The interested Writer.
     * @return the ProcessOutputConsumer itself following the Builder pattern.
     */
    public ProcessOutputRepeater with(final Writer writer) {
        writers.add(writer);
        return this;
    }

    /**
     * Creates a Reader that reproduces the process output.
     *
     * @return the Reader that reproduces the process output.
     */
    public Reader split() throws IOException {
        final PipedReader pr = new PipedReader();
        final PipedWriter pw = new PipedWriter(pr);
        with(pw);
        return pr;
    }

    @Override
    public void run() {
        final int maxlen = 1024;
        final char[] buffer = new char[maxlen];
        try {
            int len;
            while ((len = reader.read(buffer, 0, maxlen)) != -1) {
                if (len == 0) {
                    Thread.yield();
                }
                for (final Writer writer : writers) {
                    writer.write(buffer, 0, len);
                    writer.flush();
                }
            }
        } catch (final EOFException e) {
            // empty, ignore
        } catch (final Exception e) {
            this.exception = e;
            try {
                reader.close();
            } catch (IOException ee) {
                // ignore
            }
        }
    }
}
