/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc.cc;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author X7WS
 */
public class ClearToolCommander {

    final ClearToolProcessBuilder pb;

    public ClearToolCommander(ClearToolProcessBuilder pb) {
        this.pb = pb;
    }

    public void checkout(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            pb.reset("checkout").command("checkout").preserveTime().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                pb.argument(iterator.next().getPath());
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
        }
    }

    public void checkin(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            pb.reset("checkin").command("checkin").preserveTime().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                pb.argument(iterator.next().getPath());
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
        }
    }

    public void remove(Collection<File> filesOrDirs) throws IOException {
        Iterator<File> iterator = filesOrDirs.iterator();
        while (iterator.hasNext()) {
            pb.reset("rmname").command("rmname").force().noComment();
            for (int i = 0; i < 10 && iterator.hasNext(); i++) {
                pb.argument(iterator.next().getPath());
            }
            ClearToolProcess p = pb.create();
            p.waitFor();
        }
    }

    public void moveFile(File source, File target) throws IOException {
        pb.reset("move").command("mv").noComment().argument(source.getPath()).argument(target.getPath()).create().waitFor();
    }
}
