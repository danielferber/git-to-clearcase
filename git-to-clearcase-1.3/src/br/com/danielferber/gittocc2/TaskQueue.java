package br.com.danielferber.gittocc2;

import br.com.danielferber.slf4jtoys.slf4j.logger.LoggerFactory;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;

/**
 *
 * @author Daniel Felix Ferber
 */
public class TaskQueue implements Runnable {

    public final Logger logger = LoggerFactory.getLogger("queue");
    private final Meter meter = MeterFactory.getMeter(logger);

    private static class TaskEntry implements Comparable<TaskEntry> {

        final int priority;
        final String name;
        final Runnable runnable;

        public TaskEntry(int priority, String name, Runnable runnable) {
            this.priority = priority;
            this.name = name;
            this.runnable = runnable;
        }

        @Override
        public int compareTo(TaskEntry o) {
            return this.priority - o.priority;
        }
    }

    private final Set<TaskEntry> taskQueue = new TreeSet<>();

    public void add(int priority, String name, Runnable runnable) {
        taskQueue.add(new TaskEntry(priority, name, runnable));
    }

    public void add(int priority, Runnable runnable) {
        final String name = runnable.getClass().getName();
        int pos = name.lastIndexOf('.');
        if (pos < 0) {
            pos = 0;
        } else {
            pos++;
        }
        taskQueue.add(new TaskEntry(priority, name.substring(pos).toLowerCase(), runnable));
    }

    @Override
    public void run() {
        for (TaskEntry task : taskQueue) {
            meter.sub(task.name).run(task.runnable);
        }
    }
}
