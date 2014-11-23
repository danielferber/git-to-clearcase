/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2;

import br.com.danielferber.gittocc2.task.config.SyncByCompareConfiguration;
import java.io.File;

/**
 *
 * @author Daniel Felix Ferber
 */
class SyncByCompareTask extends SyncStrategyTask {

    private final File compareRoot;

    public SyncByCompareTask(SyncByCompareConfiguration configuration) {
        super("SyncByCompare", "Synchronize by directory comparison", configuration);
        compareRoot = configuration.getCompareRoot();
    }

    @Override
    protected Void meteredCall() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TreeDiff diffTaskResult() throws Exception {
        TreeDiff diff = new CompareTreeDiffTask(gitConfig.getRepositoryDir(), cleartoolConfig.getVobViewDir(), compareRoot).call();
        return diff;
    }

}
