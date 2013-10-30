/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.git;

import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.Meter;
import br.com.danielferber.slf4jtoys.slf4j.profiler.meter.MeterFactory;
import java.io.File;
import java.util.concurrent.Callable;

/**
 *
 * @author X7WS
 */
public class GitHistoryBuilder implements Callable<GitHistory> {

    final GitCommander gitCommander;
    final String fromCommit;
    final File repositoryDir;

    public GitHistoryBuilder(GitCommander gitCommander, File repositoryDir, String fromCommit) {
        this.gitCommander = gitCommander;
        this.fromCommit = fromCommit;
        this.repositoryDir = repositoryDir;
    }

    @Override
    public GitHistory call() throws Exception {
        Meter m = MeterFactory.getMeter(GitHistoryBuilder.class).m("Obter alterações do Git.").ctx("fromCommit", fromCommit).start();
        Meter m2 = null;
        try {
//            m2 = m.sub("fetch").m("Download dos commits do repositório remoto para repositório local.").start();
//            gitCommander.fetch();
//            m2.ok();
//
//            m2 = m.sub("fastForward").m("Sincronizar view ClearCase com repositório local.").start();
//            gitCommander.fastForward();
//            m2.ok();

            m2 = m.sub("currentCommit").m("Determinar commit do repositório local.").start();
            String gitCommit = gitCommander.getCurrentCommit();
            m2.ok("currentCommit", gitCommit);

            m2 = m.sub("report").m("Gerar relatório de commits.").ctx("fromCommit", fromCommit).ctx("currentCommit", gitCommit).start();
            String report = gitCommander.commitMessagesReport(fromCommit, gitCommit, "%an (%ad):%n%s%n");
            m2.ok();

            GitHistory history = new GitHistory(repositoryDir, fromCommit, gitCommit, report);

            m2 = m.sub("changeSet").m("Enumerar operações de arquivos.").ctx("fromCommit", fromCommit).ctx("currentCommit", gitCommit).start();
            gitCommander.changeSet(fromCommit, gitCommit, history.filesAdded, history.filesDeleted, history.filesModified,
                    history.filesMovedFrom, history.filesMovedTo, history.filesCopiedFrom, history.filesCopiedTo);
            m2.ok();

            m.ok();
            return history;
        } catch (Exception e) {
            if (m2 != null) {
                m2.fail(e);
            }
            m.fail(e);
            throw e;
        }
    }
}
