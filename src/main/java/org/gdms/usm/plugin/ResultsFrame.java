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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceDefinition;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.NoSuchTableException;
import org.gdms.data.DataSourceFactory;
import org.gdms.driver.DriverException;
import org.gdms.driver.gdms.GdmsWriter;
import org.gdms.sql.engine.ParseException;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;

/**
 *
 * @author Thomas Salliou
 */
public class ResultsFrame extends JFrame implements ActionListener {

    JCheckBox[] checkboxes;
    JButton createViewButton;
    JButton cancelButton;
    JButton selectAllButton;
    JButton deselectAllButton;
    
    public ResultsFrame() {
        super("Urban Sprawn Model - View Results");
        setLayout(new BorderLayout(10, 30));
        
        JLabel label = new JLabel("Select the steps you want to view in Geocatalog.", SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);
        
        //Get the number of steps in database
        DataManager dm = Services.getService(DataManager.class);
        DataSourceFactory dsf = dm.getDataSourceFactory();
        int totalSteps = 0;
        try {
            DataSource ds = dsf.getDataSource("Step");
            ds.open();
            totalSteps = (int) ds.getRowCount();
            ds.close();
        } catch (NoSuchTableException ex) {
            Services.getOutputManager().print("ERROR : NoSuchTableException, the table Step does not exist in Geocatalog.");
            return;
        } catch (DataSourceCreationException ex) {
            Services.getOutputManager().print("ERROR : DataSourceCreationException.");
            return;
        } catch (DriverException ex) {
            Services.getOutputManager().print("ERROR : DriverException.");
            return;
        }
        
        checkboxes = new JCheckBox[totalSteps];
        
        //Step checkboxes panel
        int rows = 0;
        if (totalSteps%5 == 0) {
            rows = totalSteps/5;
        }
        else {
            rows = (totalSteps/5) + 1;
        }
        JPanel checkboxesPanel = new JPanel(new GridLayout(rows, 5, 5, 5));
        for(int i = 0; i < totalSteps; i++) {
            int j = i + 1;
            JCheckBox stepCheckbox = new JCheckBox("Step "+j, true);
            checkboxesPanel.add(stepCheckbox);
            checkboxes[i] = stepCheckbox;
        }
        add(checkboxesPanel, BorderLayout.CENTER);
        
        //Button Panel
        JPanel buttonPanel = new JPanel(new BorderLayout(30,0));
        JPanel mainPanel = new JPanel();
        createViewButton = new JButton("Create View");
        createViewButton.addActionListener(this);
        createViewButton.setActionCommand("createView");
        mainPanel.add(createViewButton);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");
        mainPanel.add(cancelButton);
        
        buttonPanel.add(mainPanel, BorderLayout.WEST);
        
        JPanel controlPanel = new JPanel();
        selectAllButton = new JButton("Select all");
        selectAllButton.addActionListener(this);
        selectAllButton.setActionCommand("selectAll");
        controlPanel.add(selectAllButton);
        
        deselectAllButton = new JButton("Select none");
        deselectAllButton.addActionListener(this);
        deselectAllButton.setActionCommand("selectNone");
        controlPanel.add(deselectAllButton);
        
        buttonPanel.add(controlPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
                
        //Frame display
        pack();
        getRootPane().setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("selectAll")) {
            for (JCheckBox box : checkboxes) {
                box.setSelected(true);
            }
        }
        else if(e.getActionCommand().equals("selectNone")) {
            for (JCheckBox box : checkboxes) {
                box.setSelected(false);
            }
        }
        else if(e.getActionCommand().equals("createView")) {
            DataManager dm = Services.getService(DataManager.class);
            DataSourceFactory dsf = dm.getDataSourceFactory();
            for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                    int j = i + 1;
                    try {
                        dsf.executeSQL("CREATE VIEW Stepview"+j+" AS SELECT * FROM Plot a, PlotState b WHERE a.plotID=b.plotID AND b.stepNumber="+j+";");
                    } catch (ParseException ex) {
                        Logger.getLogger(ResultsFrame.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (DriverException ex) {
                        Logger.getLogger(ResultsFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else {
            dispose();
        }
    }
}
