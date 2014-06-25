/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config.clearcase;

import java.io.File;


abstract class ClearToolConfigSourceImpl implements ClearToolConfigSource {

    @Override
    public File getCommitStampAbsoluteFile() {
        return new File(getVobViewDir(), getCommitStampFileName().getPath());
    }

    @Override
    public File getCounterStampAbsoluteFile() {
        return new File(getVobViewDir(), getCounterStampFileName().getPath());
    }

}
