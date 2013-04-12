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
package org.gdms.usm.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.driver.Driver;
import org.gdms.driver.DriverException;
import org.gdms.driver.FileDriver;
import org.gdms.driver.driverManager.DriverManager;
import org.gdms.source.FileDriverFilter;
import org.gdms.source.SourceManager;
import org.gdms.usm.BufferBuildTypeCalculator;
import org.gdms.usm.GaussParcelSelector;
import org.gdms.usm.SchellingDecisionMaker;
import org.gdms.usm.StatisticalDecisionMaker;
import org.gdms.usm.Step;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;
import org.orbisgis.view.background.BackgroundManager;

/**
 *
 * @author Thomas Salliou
 */
public class LaunchFrame extends JFrame implements ActionListener {

    private String modelChoice;
    private JFileChooser dataFc;
    private JTextField dataPath;
    private JFileChooser outputFc;
    private JTextField outputPath;
    private String configPath;
    private JButton launchButton;
    private JButton modifyButton;
    private JButton cancelButton;
    private JCheckBox thresholdButton;
    
    public LaunchFrame(String configPath, String choice) throws DataSourceCreationException, DriverException {
        super("Urban Sprawl Model - Launch");
        modelChoice = choice;
        this.configPath = configPath;
        
        //Check panel
        ReadParameterPanel rpp = new ReadParameterPanel(configPath, choice);
        add(rpp, BorderLayout.NORTH);
        
        //Path selecting panel
        JPanel browsePanel = new JPanel(new BorderLayout());
        
        //Data file chooser
        dataFc = new JFileChooser();
        DataManager dm = Services.getService(DataManager.class);
        SourceManager sourceManager = dm.getSourceManager();
        DriverManager driverManager = sourceManager.getDriverManager();
        Driver[] filtered = driverManager.getDrivers(new FileDriverFilter());
        for (int i = 0; i < filtered.length; i++) {
                FileDriver fileDriver = (FileDriver) filtered[i];
                String[] extensions = fileDriver.getFileExtensions();
                dataFc.addChoosableFileFilter(new FormatFilter(extensions,fileDriver.getTypeDescription()));
        }
        dataPath = new JTextField("Please choose initial data", 35);
        dataPath.setEditable(false);
        JButton dataBrowseButton = new JButton("Browse...");
        dataBrowseButton.setActionCommand("dataBrowse");
        dataBrowseButton.addActionListener(this);
        JPanel dataBrowseBar = new JPanel();
        dataBrowseBar.add(dataPath); 
        dataBrowseBar.add(dataBrowseButton);
        
        //Output directory chooser
        outputFc = new JFileChooser();
        outputFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        outputPath = new JTextField("Please choose output data destination directory", 35);
        outputPath.setEditable(false);
        JButton outputBrowseButton = new JButton("Browse...");
        outputBrowseButton.setActionCommand("outputBrowse");
        outputBrowseButton.addActionListener(this);
        JPanel outputBrowseBar = new JPanel();
        outputBrowseBar.add(outputPath);
        outputBrowseBar.add(outputBrowseButton);

        browsePanel.add(dataBrowseBar, BorderLayout.NORTH);
        browsePanel.add(outputBrowseBar, BorderLayout.SOUTH);
        add(browsePanel, BorderLayout.CENTER);
        
        //Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        //Add a modify thresholds panel in the button panel
        thresholdButton = new JCheckBox();
        JLabel thresholdText = new JLabel("Modify thresholds during simulation");
        JPanel thresholdPanel = new JPanel(new BorderLayout());
        thresholdPanel.add(thresholdText, BorderLayout.WEST);
        thresholdPanel.add(thresholdButton, BorderLayout.EAST);
        buttonPanel.add(thresholdPanel, BorderLayout.NORTH);
        
        launchButton = new JButton("Launch");
        launchButton.addActionListener(this);
        launchButton.setActionCommand("launch");
        buttonPanel.add(launchButton, BorderLayout.WEST);
        modifyButton = new JButton("Modify");
        modifyButton.addActionListener(this);
        modifyButton.setActionCommand("modify");
        buttonPanel.add(modifyButton, BorderLayout.CENTER);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");
        buttonPanel.add(cancelButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("dataBrowse")) {
            int val = dataFc.showDialog(this, "Choose data");
            if (val == JFileChooser.APPROVE_OPTION) {
                dataPath.setText(dataFc.getSelectedFile().getAbsolutePath());
            }
        }
        else if(e.getActionCommand().equals("outputBrowse")) {
            int val = outputFc.showDialog(this, "Choose destination");
            if (val == JFileChooser.APPROVE_OPTION) {
                outputPath.setText(outputFc.getSelectedFile().getAbsolutePath());
            }
        }
        else if(e.getActionCommand().equals("launch")) {
            DataManager dma = Services.getService(DataManager.class);
            DataSourceFactory dsf = dma.getDataSourceFactory();
            BufferBuildTypeCalculator bbtc = new BufferBuildTypeCalculator();
            GaussParcelSelector gps = new GaussParcelSelector();
            Step s;
            if(modelChoice.equals("schelling")) {
                SchellingDecisionMaker dm = new SchellingDecisionMaker();
                s = new Step(2000, dataPath.getText(), configPath, outputPath.getText(), bbtc, dm, gps, dsf);
            }
            else {
                StatisticalDecisionMaker dm = new StatisticalDecisionMaker();
                s = new Step(2000, dataPath.getText(), configPath, outputPath.getText(), bbtc, dm, gps, dsf);
            }
            new ProgressFrame(s, thresholdButton.isSelected());
            launchButton.setEnabled(false);
            modifyButton.setEnabled(false);
            cancelButton.setEnabled(false);
            BackgroundManager bm = Services.getService(BackgroundManager.class);
            bm.backgroundOperation(new ExecuteSimulation(s));
            
        }
        else if(e.getActionCommand().equals("modify")) {
            try {
                new ModifyFrame(configPath, modelChoice);
            } catch (DataSourceCreationException ex) {
                Logger.getLogger(LaunchFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DriverException ex) {
                Logger.getLogger(LaunchFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            dispose();
        }
        else {
            new ConfigFrame();
            dispose();
        }
    }
}
