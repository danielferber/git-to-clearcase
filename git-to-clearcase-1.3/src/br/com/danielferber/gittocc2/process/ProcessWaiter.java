package br.com.danielferber.gittocc2.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Waits until a process finishes execution, the run a list of listeners.
 *
 * @author Daniel Felix Ferber
 */
class ProcessWaiter implements Runnable {

    Process process;
    Thread thread;
    final List<Runnable> listeners = new ArrayList<>();

    public ProcessWaiter() {
        super();
    }

    public void start(final Process process) {
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
    public ProcessWaiter with(final Runnable runnable) {
        listeners.add(runnable);
        return this;
    }

    @Override
    public void run() {
        while (true) {
            try {
                process.exitValue();
                break;
            } catch (final IllegalThreadStateException e) {
                try {
                    process.waitFor();
                } catch (final InterruptedException ee) {
                    // ignore
                }
            }
        }
        for (final Runnable runnable : listeners) {
            runnable.run();
        }
    }
}
