/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.schema.DefaultMetadata;
import org.gdms.data.schema.Metadata;
import org.gdms.data.types.Type;
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
public class ModifyFrame extends JFrame implements ActionListener {

    private String configPath;
    private SpinnerParameterPanel spp;
    private String oldChoice;
    
    public ModifyFrame(String configPath, String choice) throws DataSourceCreationException, DriverException {
        super("Urban Sprawl Model - Modify");
        this.configPath = configPath;
        this.oldChoice = choice;

        //parameters reading
        Map<String, Double> parameters = new HashMap<String, Double>();
        DataSourceFactory dsf = new DataSourceFactory();
        File configFile = new File(configPath);
        DataSource configSource = dsf.getDataSource(configFile);
        configSource.open();
        parameters.put("year", configSource.getFieldValue(0, configSource.getFieldIndexByName("year")).getAsDouble());
        parameters.put("numberOfTurns", configSource.getFieldValue(0, configSource.getFieldIndexByName("numberOfTurns")).getAsDouble());
        parameters.put("bufferSize", configSource.getFieldValue(0, configSource.getFieldIndexByName("bufferSize")).getAsDouble());
        parameters.put("amenitiesWeighting", configSource.getFieldValue(0, configSource.getFieldIndexByName("amenitiesWeighting")).getAsDouble());
        parameters.put("constructibilityWeighting", configSource.getFieldValue(0, configSource.getFieldIndexByName("constructibilityWeighting")).getAsDouble());
        parameters.put("idealhousingWeighting", configSource.getFieldValue(0, configSource.getFieldIndexByName("idealhousingWeighting")).getAsDouble());
        parameters.put("gaussDeviation", configSource.getFieldValue(0, configSource.getFieldIndexByName("gaussDeviation")).getAsDouble());
        parameters.put("segregationThreshold", configSource.getFieldValue(0, configSource.getFieldIndexByName("segregationThreshold")).getAsDouble());
        parameters.put("segregationTolerance", configSource.getFieldValue(0, configSource.getFieldIndexByName("segregationTolerance")).getAsDouble());
        parameters.put("householdMemory", configSource.getFieldValue(0, configSource.getFieldIndexByName("householdMemory")).getAsDouble());
        parameters.put("movingThreshold", configSource.getFieldValue(0, configSource.getFieldIndexByName("movingThreshold")).getAsDouble());
        parameters.put("immigrantNumber", configSource.getFieldValue(0, configSource.getFieldIndexByName("immigrantNumber")).getAsDouble());
        configSource.close();

        spp = new SpinnerParameterPanel(parameters);
        add(spp, BorderLayout.NORTH);

        //buttons
        JPanel buttonPanel = new JPanel();
        JButton modify = new JButton("Modify");
        JButton cancel = new JButton("Cancel");
        modify.setActionCommand("modify");
        cancel.setActionCommand("cancel");
        modify.addActionListener(this);
        cancel.addActionListener(this);
        buttonPanel.add(modify);
        buttonPanel.add(cancel);

        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("modify".equals(e.getActionCommand())) {
            try {
                File globals = new File(configPath);

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
                    "year"};
                Type integ = TypeFactory.createType(64);
                Type doubl = TypeFactory.createType(16);
                Type[] fieldTypes1 = {doubl, doubl, doubl, doubl, doubl, doubl, doubl, integ, doubl, integ, integ, integ};
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
                            ValueFactory.createValue(yi)
                        });

                //Table closing
                globalsGW.writeRowIndexes();
                globalsGW.writeExtent();
                globalsGW.writeWritenRowCount();
                globalsGW.close();

                try {
                    if (spp.getSelections().get("statistical").isSelected()) {
                        new LaunchFrame(configPath, "statistical");
                    } else if (spp.getSelections().get("schelling").isSelected()) {
                        new LaunchFrame(configPath, "schelling");
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

        } else {
            try {
                new LaunchFrame(configPath, oldChoice);
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
        }
    }
}
