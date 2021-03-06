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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.schema.DefaultMetadata;
import org.gdms.data.schema.Metadata;
import org.gdms.data.types.TypeFactory;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.driver.DriverException;
import org.gdms.driver.gdms.GdmsWriter;
import org.orbisgis.core.Services;

/**
 *
 * @author Thomas Salliou
 */
public class CreateFrame extends JFrame implements ActionListener {

    private SpinnerParameterPanel spp;
    private JFileChooser fc;
    private JTextField path;
    
    public CreateFrame() {

        super("Urban Sprawl Model - Create");
        Map<String, Double> defaultParameters = new HashMap<String, Double>();
        defaultParameters.put("year", 2000.0);
        defaultParameters.put("numberOfTurns", 1.0);
        defaultParameters.put("bufferSize", 10.0);
        defaultParameters.put("amenitiesWeighting", 1.0);
        defaultParameters.put("constructibilityWeighting", 1.0);
        defaultParameters.put("idealhousingWeighting", 1.0);
        defaultParameters.put("gaussDeviation", 0.10);
        defaultParameters.put("householdMemory", 5.0);
        defaultParameters.put("movingThreshold", 20.0);
        defaultParameters.put("segregationThreshold", 0.8);
        defaultParameters.put("segregationTolerance", 0.3);
        defaultParameters.put("immigrantNumber", 5000.0);
        defaultParameters.put("threshold_1", 0.);
        defaultParameters.put("threshold_2", 0.000155);
        defaultParameters.put("threshold_3", 0.001000);
        defaultParameters.put("threshold_4", 0.001466);
        spp = new SpinnerParameterPanel(defaultParameters);
        
        //Spinner panel
        add(spp, BorderLayout.NORTH);

        //File chooser
        fc = new JFileChooser();
        fc.setFileFilter(new GdmsFileFilter());

        //Path text field
        path = new JTextField("Please select a target file", 25);
        path.setEditable(false);

        //Button labels
        JButton create = new JButton("Create");
        JButton cancel = new JButton("Cancel");
        JButton browse = new JButton("Browse...");

        //Button actions
        create.setActionCommand("create");
        cancel.setActionCommand("cancel");
        browse.setActionCommand("browse");
        create.addActionListener(this);
        cancel.addActionListener(this);
        browse.addActionListener(this);

        //Button tooltips
        create.setToolTipText("Creates a config file with the given parameters.");
        cancel.setToolTipText("Cancels and returns to the selection panel.");
        browse.setToolTipText("Select a target location, existing or not.");

        //Browse path panel
        JPanel browsePanel = new JPanel();
        browsePanel.add(path);
        browsePanel.add(browse);

        //Button panel, flow layout is efficient here
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(create);
        buttonPanel.add(cancel);

        add(buttonPanel, BorderLayout.SOUTH);
        add(browsePanel, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("create".equals(e.getActionCommand())) {
            if (fc.getSelectedFile() == null || !fc.getSelectedFile().getAbsolutePath().endsWith(".gdms")) {
                JOptionPane.showMessageDialog(this, "Please select or create a valid GDMS file.", "Invalid file", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    File globals = fc.getSelectedFile().getAbsoluteFile();

                    //Table creation
                    GdmsWriter globalsGW = new GdmsWriter(globals);
                    String[] fieldNames1 = {"bufferSize",
                        "amenitiesWeighting",
                        "constructibilityWeighting",
                        "idealhousingWeighting",
                        "gaussDeviation",
                        "segregationThreshold",
                        "segregationTolerance",
                        "householdMemory",
                        "movingThreshold",
                        "immigrantNumber",
                        "numberOfTurns",
                        "year",
                        "threshold_1",
                        "threshold_2",
                        "threshold_3",
                        "threshold_4"};
                    org.gdms.data.types.Type integ = TypeFactory.createType(64);
                    org.gdms.data.types.Type doubl = TypeFactory.createType(16);
                    org.gdms.data.types.Type[] fieldTypes1 = {doubl, doubl, doubl, doubl, doubl, doubl, doubl, integ, doubl, integ, integ, integ, doubl, doubl, doubl, doubl};
                    Metadata m1 = new DefaultMetadata(fieldTypes1, fieldNames1);
                    globalsGW.writeMetadata(0, m1);

                    //Table filling
                    Map<String, JSpinner> sp = spp.getSpinners();
                    Double hM = (Double) sp.get("householdMemory").getValue();
                    Integer hMi = hM.intValue();
                    Double iN = (Double) sp.get("immigrantNumber").getValue();
                    Integer iNi = iN.intValue();
                    Double nOT = (Double) sp.get("numberOfTurns").getValue();
                    Integer nOTi = nOT.intValue();
                    Double y = (Double) sp.get("year").getValue();
                    Integer yi = y.intValue();
                    globalsGW.addValues(new Value[]{ValueFactory.createValue((Double) sp.get("bufferSize").getValue()),
                                ValueFactory.createValue((Double) sp.get("amenitiesWeighting").getValue()),
                                ValueFactory.createValue((Double) sp.get("constructibilityWeighting").getValue()),
                                ValueFactory.createValue((Double) sp.get("idealhousingWeighting").getValue()),
                                ValueFactory.createValue((Double) sp.get("gaussDeviation").getValue()),
                                ValueFactory.createValue((Double) sp.get("segregationThreshold").getValue()),
                                ValueFactory.createValue((Double) sp.get("segregationTolerance").getValue()),
                                ValueFactory.createValue(hMi),
                                ValueFactory.createValue((Double) sp.get("movingThreshold").getValue()),
                                ValueFactory.createValue(iNi),
                                ValueFactory.createValue(nOTi),
                                ValueFactory.createValue(yi),
                                ValueFactory.createValue((Double) sp.get("threshold_1").getValue()),
                                ValueFactory.createValue((Double) sp.get("threshold_2").getValue()),
                                ValueFactory.createValue((Double) sp.get("threshold_3").getValue()),
                                ValueFactory.createValue((Double) sp.get("threshold_4").getValue())
                            });

                    //Table closing
                    globalsGW.writeRowIndexes();
                    globalsGW.writeExtent();
                    globalsGW.writeWritenRowCount();
                    globalsGW.close();

                    try {
                        if (spp.getSelections().get("statistical").isSelected()) {
                            new LaunchFrame(fc.getSelectedFile().getAbsolutePath(), "statistical");
                        } else if (spp.getSelections().get("schelling").isSelected()) {
                            new LaunchFrame(fc.getSelectedFile().getAbsolutePath(), "schelling");
                        }
                    } catch (DriverException ex) {
                        Services.getErrorManager().error("Driver Exception", ex);
                        JOptionPane.showMessageDialog(this, "Some driver error has occurred.", "Driver Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    } catch (DataSourceCreationException ex) {
                        Services.getErrorManager().error("DataSourceCreation Exception", ex);
                        JOptionPane.showMessageDialog(this, "Some DataSource creation error has occurred.", "DataSource Creation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    dispose();
                } catch (IOException ex) {
                    Services.getErrorManager().error("I/O Exception", ex);
                    JOptionPane.showMessageDialog(this, "Some I/O error has occurred.", "I/O Error", JOptionPane.WARNING_MESSAGE);
                    return;
                } catch (DriverException ex) {
                    Services.getErrorManager().error("Driver Exception", ex);
                    JOptionPane.showMessageDialog(this, "Some driver error has occurred.", "Driver Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

            }
        } else if ("browse".equals(e.getActionCommand())) {
            int value = fc.showSaveDialog(this);
            if (value == JFileChooser.APPROVE_OPTION) {
                if (!fc.getSelectedFile().getAbsolutePath().endsWith(".gdms")) {
                    fc.setSelectedFile(new File(fc.getSelectedFile().getAbsolutePath() + ".gdms"));
                }
                path.setText(fc.getSelectedFile().getAbsolutePath());
            }
        } else {
            new ConfigFrame();
            dispose();
        }

    }
}
