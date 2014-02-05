package br.com.danielferber.gittocc2.process;

import java.util.ArrayList;
import java.util.List;

/**
 * Waits until a process finishes execution, the run a list of listeners.
 *
 * @author Daniel Felix Ferber
 */
final class ProcessWaiter implements Runnable {

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
