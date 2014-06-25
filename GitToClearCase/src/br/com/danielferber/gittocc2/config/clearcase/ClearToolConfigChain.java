package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;

/**
 *
 * @author Daniel
 */
public class ClearToolConfigChain extends ClearToolConfigSourceImpl implements ClearToolConfigSource {
    final ClearToolConfigSource wrapped1;
    final ClearToolConfigSource wrapped2;

    public ClearToolConfigChain(final ClearToolConfigSource wrapped1, final ClearToolConfigSource wrapped2) {
        this.wrapped1 = wrapped1;
        this.wrapped2 = wrapped2;
    }

    @Override
    public String getActivityMessagePattern() {
        if (wrapped2.getActivityMessagePattern() != null) {
            return wrapped2.getActivityMessagePattern();
        }
        return wrapped1.getActivityMessagePattern();
    }

    @Override
    public File getClearToolExec() {
        if (wrapped2.getClearToolExec() != null) {
            return wrapped2.getClearToolExec();
        }
        return wrapped1.getClearToolExec();
    }

        @Override
		public File getCommitStampFileName() {
		    if (wrapped2.getCommitStampFileName() != null) {
		        return wrapped2.getCommitStampFileName();
		    }
		    return wrapped1.getCommitStampFileName();
		}

    @Override
    public File getCounterStampFileName() {
        if (wrapped2.getCounterStampFileName() != null) {
            return wrapped2.getCounterStampFileName();
        }
        return wrapped1.getCounterStampFileName();
    }

    @Override
    public Boolean getCreateActivity() {
        if (wrapped2.getCreateActivity() != null) {
            return wrapped2.getCreateActivity();
        }
        return wrapped1.getCreateActivity();
    }

    @Override
    public Long getOverriddenSyncCounter() {
        if (wrapped2.getOverriddenSyncCounter() != null) {
            return wrapped2.getOverriddenSyncCounter();
        }
        return wrapped1.getOverriddenSyncCounter();
    }

    @Override
    public String getOverriddenSyncFromCommit() {
        if (wrapped2.getOverriddenSyncFromCommit() != null) {
            return wrapped2.getOverriddenSyncFromCommit();
        }
        return wrapped1.getOverriddenSyncFromCommit();
    }

    @Override
   public Boolean getupdateVobViewDir() {
	if (wrapped2.getupdateVobViewDir() != null) {
	    return wrapped2.getupdateVobViewDir();
	}
	return wrapped1.getupdateVobViewDir();
   }

    @Override
    public File getVobViewDir() {
        if (wrapped2.getVobViewDir() != null) {
            return wrapped2.getVobViewDir();
        }
        return wrapped1.getVobViewDir();
    }

}
