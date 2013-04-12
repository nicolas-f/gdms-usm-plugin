/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
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
