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

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

/**
 *
 * @author Thomas Salliou
 */
public class SpinnerParameterPanel extends JPanel implements ActionListener{
    
    private Map<String, JSpinner> spinners;
    private Map<String, JRadioButton> selections;
    
    public SpinnerParameterPanel(Map<String, Double> parameters) {
        super (new SpringLayout());
        spinners = new HashMap<String, JSpinner>();
        selections = new HashMap<String, JRadioButton>();
        
        String[] labels = {"Year :",
            "Number of Turns :",
            "Buffer size :",
            "Amenities Weighting :",
            "Constructibility Weighting :",
            "Idealhousing Weighting :",
            "Gauss Deviation :",
            "Statistical model",
            "Schelling model",
            "Segregation Threshold :",
            "Segregation Tolerance :",
            "Household Memory :",
            "Moving Threshold :",
            "Immigrant Number :",
            "Threshold 1 :",
            "Threshold 2 :",
            "Threshold 3 :",
            "Threshold 4 :"
        };
        
        String[] tooltips = {"The starting year of the simulation.",
            "The number of turns the simulation is going to run.",
            "The size of the buffer used to determine neighbouring parcels.",
            "The weighting of amenities index used for dissatisfaction calculations.",
            "The weighting of constructibility index used for dissatisfaction calculations.",
            "The weighting of idealhousing coefficient used for dissatisfaction calculations.",
            "The relative deviation of the gaussian needed for moving in parcel selection.",
            "Selects the Statistical segregation model as a moving out model.",
            "Selects the Schelling segregation model as a moving out model.",
            "The part of neighbours too much rich and also too much poor, needed to decide if the household moves or not.",
            "The tolerance for determining if a neighbour is too much rich or too much poor.",
            "The size of the dissatisfaction memory of a household.",
            "If the total dissatisfaction exceeds this value, the household moves.",
            "The number of immigrants per turn.",
            "The first threshold of bati type.",
            "The second threshold of bati type.",
            "The third threshold of bati type.",
            "The forth threshold of bati type."
        };
        int spinnerNumber = labels.length;
        
        SpinnerModel yearModel = new SpinnerNumberModel((double) parameters.get("year"),1900,2500,1);
        JSpinner spinner = addLabeledSpinner(this, labels[0], tooltips[0], yearModel);
        spinners.put("year", spinner);
        
        SpinnerModel turnsModel = new SpinnerNumberModel((double) parameters.get("numberOfTurns"),1,1000,1);
        spinner = addLabeledSpinner(this, labels[1], tooltips[1], turnsModel);
        spinners.put("numberOfTurns", spinner);
        
        SpinnerModel bufferModel = new SpinnerNumberModel((double) parameters.get("bufferSize"),0.01,100.00,0.01);
        spinner = addLabeledSpinner(this, labels[2], tooltips[2], bufferModel);
        spinners.put("bufferSize", spinner);
        
        SpinnerModel weightingModel1 = new SpinnerNumberModel((double) parameters.get("amenitiesWeighting"),0.00,10.00,0.01);
        SpinnerModel weightingModel2 = new SpinnerNumberModel((double) parameters.get("constructibilityWeighting"),0.00,10.00,0.01);
        SpinnerModel weightingModel3 = new SpinnerNumberModel((double) parameters.get("idealhousingWeighting"),0.00,10.00,0.01);
        spinner = addLabeledSpinner(this, labels[3], tooltips[3], weightingModel1);
        spinners.put("amenitiesWeighting", spinner);
        spinner = addLabeledSpinner(this, labels[4], tooltips[4], weightingModel2);
        spinners.put("constructibilityWeighting", spinner);
        spinner = addLabeledSpinner(this, labels[5], tooltips[5], weightingModel3);
        spinners.put("idealhousingWeighting", spinner);
                
        SpinnerModel gaussModel = new SpinnerNumberModel((double) parameters.get("gaussDeviation"),0.01,1.00,0.01);
        spinner = addLabeledSpinner(this, labels[6], tooltips[6], gaussModel);
        spinners.put("gaussDeviation", spinner);
        
        ButtonGroup choices = new ButtonGroup();
        
        JRadioButton statisticalButton = new JRadioButton("Statistical Model", true);
        statisticalButton.setToolTipText(tooltips[7]);
        statisticalButton.addActionListener(this);
        statisticalButton.setActionCommand("statistical");
        this.add(statisticalButton);
        choices.add(statisticalButton);
        selections.put("statistical", statisticalButton);
        
        JRadioButton schellingButton = new JRadioButton("Schelling Model", false);
        schellingButton.setToolTipText(tooltips[8]);
        schellingButton.addActionListener(this);
        schellingButton.setActionCommand("schelling");
        this.add(schellingButton);
        choices.add(schellingButton);
        selections.put("schelling", schellingButton);
        
        SpinnerModel memoryModel = new SpinnerNumberModel((double) parameters.get("householdMemory"),1,50,1);
        spinner = addLabeledSpinner(this, labels[11], tooltips[11], memoryModel);
        spinners.put("householdMemory", spinner);
        
        SpinnerModel movingModel = new SpinnerNumberModel((double) parameters.get("movingThreshold"),0.01,200.0,0.01);
        spinner = addLabeledSpinner(this, labels[12], tooltips[12], movingModel);
        spinners.put("movingThreshold", spinner);
        
        SpinnerModel segThresholdModel = new SpinnerNumberModel((double) parameters.get("segregationThreshold"),0.01,1.00,0.01);
        spinner = addLabeledSpinner(this, labels[9], tooltips[9], segThresholdModel);
        spinner.setEnabled(false);
        spinners.put("segregationThreshold", spinner);
        
        SpinnerModel segToleranceModel = new SpinnerNumberModel((double) parameters.get("segregationTolerance"),0.01,1.00,0.01);
        spinner = addLabeledSpinner(this, labels[10], tooltips[10], segToleranceModel);
        spinner.setEnabled(false);
        spinners.put("segregationTolerance", spinner);
        
        SpinnerModel immigrantModel = new SpinnerNumberModel((double) parameters.get("immigrantNumber"),0,200000,1);
        spinner = addLabeledSpinner(this, labels[13], tooltips[13], immigrantModel);
        spinners.put("immigrantNumber", spinner);
        
        SpinnerModel thresholdModel_1 = new SpinnerNumberModel((double) parameters.get("threshold_1"),0,1,0.00001);
        spinner = addLabeledSpinner(this, labels[14], tooltips[14], thresholdModel_1);
        spinner.setEditor(new JSpinner.NumberEditor(spinner,"0.000000"));
        new JSpinner.NumberEditor(spinner,"0.000000");
        spinners.put("threshold_1", spinner);
        
        SpinnerModel thresholdModel_2 = new SpinnerNumberModel((double) parameters.get("threshold_2"),0,1,0.00001);
        spinner = addLabeledSpinner(this, labels[15], tooltips[15], thresholdModel_2);
        spinner.setEditor(new JSpinner.NumberEditor(spinner,"0.000000"));
        spinners.put("threshold_2", spinner);
        
        SpinnerModel thresholdModel_3 = new SpinnerNumberModel((double) parameters.get("threshold_3"),0,1,0.00001);
        spinner = addLabeledSpinner(this, labels[16], tooltips[16], thresholdModel_3);
        spinner.setEditor(new JSpinner.NumberEditor(spinner,"0.000000"));
        spinners.put("threshold_3", spinner);
        
        SpinnerModel thresholdModel_4 = new SpinnerNumberModel((double) parameters.get("threshold_4"),0,1,0.00001);
        spinner = addLabeledSpinner(this, labels[16], tooltips[16], thresholdModel_4);
        spinner.setEditor(new JSpinner.NumberEditor(spinner,"0.000000"));
        spinners.put("threshold_4", spinner);
        
        SpringUtilities.makeCompactGrid(this,spinnerNumber-1,2,10,10,6,10);
    }
    
    private static JSpinner addLabeledSpinner(Container c, String label, String tooltip, SpinnerModel model) {
        JLabel jl = new JLabel(label);
        c.add(jl);
        
        JSpinner spinner = new JSpinner(model);
        jl.setLabelFor(spinner);
        jl.setToolTipText(tooltip);
        c.add(spinner);
        
        return spinner;
    }
    
    public Map<String, JSpinner> getSpinners() {
        return spinners;
    }    
    
    public Map<String, JRadioButton> getSelections() {
        return selections;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("schelling")) {
            spinners.get("segregationThreshold").setEnabled(true);
            spinners.get("segregationTolerance").setEnabled(true);
            spinners.get("movingThreshold").setEnabled(false);
            spinners.get("householdMemory").setEnabled(false);
        }
        else {
            spinners.get("segregationThreshold").setEnabled(false);
            spinners.get("segregationTolerance").setEnabled(false);
            spinners.get("movingThreshold").setEnabled(true);
            spinners.get("householdMemory").setEnabled(true);
        }
    }
}
