/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm.plugin;

import org.orbisgis.core.ui.pluginSystem.AbstractPlugIn;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.workbench.Names;

/**
 *
 * @author Thomas Salliou
 */
public class ResultsPlugIn extends AbstractPlugIn {

    @Override
    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(this,
                new String[]{Names.FILE, "GDMS-USM"},
                "Results", false,
                null, null, null, context);
    }

    @Override
    public boolean execute(PlugInContext context) throws Exception {
        new ResultsFrame();
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
