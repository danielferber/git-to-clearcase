package br.com.danielferber.gittocc2.io.process;

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

    Reader reader;
    Thread thread;
    final List<Writer> writers = new ArrayList<>();

    public ProcessOutputRepeater() {
        super();
    }

    public void start(InputStream inputStream) {
        reader = new InputStreamReader(inputStream);
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void waitFor() throws InterruptedException {
        thread.join();
    }
    
    /**
     * Registers an interested Writer to receive the process output.
     *
     * @param writer The interested Writer.
     * @return the ProcessOutputConsumer itself following the Builder pattern.
     */
    public ProcessOutputRepeater with(Writer writer) {
        writers.add(writer);
        return this;
    }

    /**
     * Creates a Reader that reproduces the process output.
     *
     * @return the Reader that reproduces the process output.
     */
    public Reader split() throws IOException {
        PipedReader pr = new PipedReader();
        PipedWriter pw = new PipedWriter(pr);
        with(pw);
        return pr;
    }

    @Override
    public void run() {
        final int maxlen = 1024;
        char[] buffer = new char[maxlen];
        try {
            int len;
            while ((len = reader.read(buffer, 0, maxlen)) != -1) {
                if (len == 0) {
                    Thread.yield();
                }
                for (Writer writer : writers) {
                    writer.write(buffer, 0, len);
                    writer.flush();
                }
            };
        } catch (EOFException e) {
            // empty, ignore
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
