/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.git;

import br.com.danielferber.gittocc2.config.ConfigException;

/**
 *
 * @author Daniel Felix Ferber
 */
public class GitTasks {

    private final GitConfig config;
    private GitCommander commander;

    public GitTasks(GitConfig config) {
        this.config = config;
    }

    private GitCommander extractGitCommander() throws ConfigException {
        if (commander == null) {
            commander = new GitCommander(config);
        }
        return commander;
    }

    public class SetUpRepository implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.configSet("merge.defaultToUpstream", "true", GitCommander.ConfigScope.Default);
            commander.configSet("diff.renameLimit", "10000", GitCommander.ConfigScope.Default);
        }
    }

    public class Reset implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.reset(GitCommander.ResetMode.Hard);
        }
    }

    public class Clean implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.clean(true, true);
        }
    }

    public class Pull implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.fetch();
            commander.fastForward();
        }
    }

    public class Fetch implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.fetch();
        }
    }

    public class FastForward implements Runnable {

        private final GitCommander commander = extractGitCommander();

        @Override
        public void run() {
            commander.fastForward();
        }
    }
}
