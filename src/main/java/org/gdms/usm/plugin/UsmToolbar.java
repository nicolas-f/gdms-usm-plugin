/**
 *
 * Gdms-USM is a library dedicated to multi-agent simulation for modeling urban sprawl.
 * It is based on the GDMS library. It uses the OrbisGIS renderer to display results.
 *
 * This version is developed at French IRSTV Institute and at LIENSs UMR 7266 laboratory
 * (http://lienss.univ-larochelle.fr/) as part of the VegDUD project, funded by the
 * French Agence Nationale de la Recherche (ANR) under contract ANR-09-VILL-0007.
 *
 * Gdms-USM is distributed under GPL 3 license. It is maintained by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2011-2012 IRSTV (FR CNRS 2488)
 *
 * Gdms-USM is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms-USM is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms-USM. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://trac.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */

package org.gdms.usm.plugin;

import org.gdms.usm.view.ConfigFrame;
import org.gdms.usm.view.ResultsFrame;
import org.orbisgis.view.components.actions.DefaultAction;
import org.orbisgis.view.main.frames.ext.MainWindow;
import org.orbisgis.view.main.frames.ext.ToolBarAction;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * USM toolbar
 * @author Nicolas Fortin
 */
public class UsmToolbar implements ToolBarAction {
    public static final String MENU_LAUNCH = "usm-menu-launch";
    public static final String MENU_RESULTS = "usm-menu-results";
    public static final String MENU_GROUP_USM = "MENU_GROUP_USM";
    private ConfigFrame configFrame;
    private ResultsFrame resultsFrame;

    @Override
    public List<Action> createActions(MainWindow mainWindow) {
        List<Action> actions = new ArrayList<Action>();
        DefaultAction launchAction = new DefaultAction(MENU_LAUNCH,"Launch USM",
                new ImageIcon(UsmToolbar.class.getResource("house.png")),
                EventHandler.create(ActionListener.class,this,"onLaunchUsm")).setLogicalGroup(MENU_GROUP_USM);
        DefaultAction resultsAction = new DefaultAction(MENU_RESULTS,"USM Results",
                new ImageIcon(UsmToolbar.class.getResource("charts.png")),
                EventHandler.create(ActionListener.class,this,"onResultsUsm")).setLogicalGroup(MENU_GROUP_USM);
        actions.add(launchAction);
        actions.add(resultsAction);
        return actions;
    }

    /**
     * User click on launch toolbar button
     */
    public void onLaunchUsm() {
        if(configFrame==null) {
            configFrame = new ConfigFrame();
        } else {
            configFrame.setVisible(true);
        }
    }

    /**
     * User click on results toolbar button
     */
    public void onResultsUsm() {
        if(resultsFrame==null) {
            resultsFrame = new ResultsFrame();
        } else {
            resultsFrame.setVisible(true);
        }
    }
    @Override
    public void disposeActions(MainWindow mainWindow, List<Action> actions) {
        if(configFrame!=null) {
            configFrame.dispose();
            configFrame = null;
        }
        if(resultsFrame!=null) {
            resultsFrame.dispose();
            resultsFrame = null;
        }
    }
}
