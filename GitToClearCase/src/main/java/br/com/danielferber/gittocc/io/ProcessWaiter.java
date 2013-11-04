package br.com.danielferber.gittocc.io;

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
class ProcessWaiter implements Runnable {

    Process process;
    Thread thread;
    final List<Runnable> listeners = new ArrayList<Runnable>();

    public ProcessWaiter() {
        super();
    }

    public void start(Process process) {
        this.process = process;
        thread = new Thread(this);
//        thread.setDaemon(true);
        thread.start();
    }

    public void waitFor() throws InterruptedException {
        thread.join();
    }

    /**
     * Registers a listener called when process finishes.
     *
     */
    public ProcessWaiter with(Runnable runnable) {
        listeners.add(runnable);
        return this;
    }

    @Override
    public void run() {
        while (true) {
            try {
                process.exitValue();
                break;
            } catch (IllegalThreadStateException e) {
                try {
                    process.waitFor();
                } catch (InterruptedException ee) {
                    // ignore
                }
            }
        }
        for (Runnable runnable : listeners) {
            runnable.run();
        }
    }
}
