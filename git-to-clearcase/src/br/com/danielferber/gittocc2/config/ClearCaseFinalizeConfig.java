/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.danielferber.gittocc2.config;

import java.io.PrintStream;

/**
 *
 * @author Daniel Felix Ferber
 */
public interface ClearCaseFinalizeConfig {

    /**
     * @return if true, check for pending, forgotten checkouts
     */
    Boolean getValidateExistingCheckout();

    public static void printConfig(PrintStream ps, ClearCaseFinalizeConfig config) {
        ps.println(" * Find forgotten checkouts: " + config.getValidateExistingCheckout());
    }

    static void validate(final ClearCaseFinalizeConfig wrapped) {
        if (wrapped.getValidateExistingCheckout() == null) {
            throw new ConfigException("Check forgotten checkouts: missing value.");
        }

    }
}
