/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc;

import br.com.danielferber.gittocc.git.GitCommander;
import br.com.danielferber.gittocc.git.GitHistory;
import br.com.danielferber.gittocc.git.GitHistoryBuilder;
import br.com.danielferber.gittocc.git.GitProcessBuilder;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import sun.nio.cs.HistoricallyNamedCharset;

/**
 *
 * @author X7WS
 */
public class TarefaSincronizacao implements Callable<Void> {
    final ExecutorService executor;
    final File gitDir;
    final File gitExecutable;
    final File ccDir;
    final File ccExecutable;

    public TarefaSincronizacao(ExecutorService executor, File gitDir, File gitExecutable, File ccDir, File ccExecutable) {
        this.gitDir = gitDir;
        this.gitExecutable = gitExecutable;
        this.ccDir = ccDir;
        this.ccExecutable = ccExecutable;
        this.executor = executor;
    }

    @Override
    public Void call() throws Exception {
        final GitProcessBuilder gitProcessBuilder = new GitProcessBuilder(gitDir, gitExecutable);
        final GitCommander gitCommander = new GitCommander(gitProcessBuilder);
//        ClearToolDriver ctDriver = new ClearToolDriver(ccDir, ctExecutable);
//            String ccCommit = ctDriver.readCommitFromFile();

        final GitHistoryBuilder historyBuilder = new GitHistoryBuilder(gitCommander, "58c7698e496e0c09b0de9d87ce8cab27d3507b46");
        final Future<GitHistory> historyBuilderResult = executor.submit(historyBuilder);
        final GitHistory gitHistory = historyBuilderResult.get();
        

//            ClearToolActivity activity = new ClearToolActivity(ctDriver, log);
//            activity.run();
        return null;
    }
}
