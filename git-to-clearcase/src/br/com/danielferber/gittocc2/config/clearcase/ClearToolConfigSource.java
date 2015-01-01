package br.com.danielferber.gittocc2.config.clearcase;

import br.com.danielferber.gittocc2.config.ClearCaseActivityConfig;
import br.com.danielferber.gittocc2.config.ClearCaseFinalizeConfig;
import br.com.danielferber.gittocc2.config.ClearCasePrepareConfig;
import br.com.danielferber.gittocc2.config.ClearCaseVobConfig;
import br.com.danielferber.gittocc2.config.ClearToolConfig;
import br.com.danielferber.gittocc2.config.SynchronizationConfig;
import java.io.PrintStream;

/**
 *
 * @author Daniel
 */
public interface ClearToolConfigSource extends ClearCaseFinalizeConfig, ClearCasePrepareConfig, ClearCaseVobConfig, ClearToolConfig, ClearCaseActivityConfig, SynchronizationConfig {
    final class Utils {
    }
}
