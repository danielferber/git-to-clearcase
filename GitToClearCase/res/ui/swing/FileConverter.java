/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.danielferber.gittocc2.ui.swing;

import java.io.File;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Daniel
 */
public class FileConverter extends Converter<File, String> {

    @Override
    public String convertForward(File arg) {
        return arg.getPath();
    }

    @Override
    public File convertReverse(String arg) {
        return new File(arg);
    }

}