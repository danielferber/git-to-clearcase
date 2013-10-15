package br.com.danielferber.gittocc.process;

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
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Repeats the encoded text output stream of a Process to interested Readers and Writers.
 *
 * @author Daniel Felix Ferber
 */
public class ProcessOutputRepeater extends Thread {

    final Reader reader;
    final List<Writer> writers = new ArrayList<Writer>();

    public ProcessOutputRepeater(InputStream is) {
        super("ProcessOutputConsumer");
        this.reader = new InputStreamReader(is);
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
