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
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.driver.DriverException;

/**
 *
 * @author Thomas Salliou
 */
public class ReadParameterPanel extends JPanel {
    
    public ReadParameterPanel(String configPath, String choice) throws DataSourceCreationException, DriverException {
        super(new SpringLayout());
        
        String[] labels = {"Year :",
            "Number of Turns :",
            "Buffer size :",
            "Amenities Weighting :",
            "Constructibility Weighting :",
            "Idealhousing Weighting :",
            "Gauss Deviation :",
            "Moving out model :",
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
            "The moving out model chosen for the simulation.",
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
        int lineNumber = labels.length;
        
        DataSourceFactory dsf = new DataSourceFactory();
        File configFile = new File(configPath);
        DataSource configSource = dsf.getDataSource(configFile);
        configSource.open();
        
        addLabeledField(this, labels[0], tooltips[0], configSource.getFieldValue(0, configSource.getFieldIndexByName("year")).toString());
        addLabeledField(this, labels[1], tooltips[1], configSource.getFieldValue(0, configSource.getFieldIndexByName("numberOfTurns")).toString());
        addLabeledField(this, labels[2], tooltips[2], configSource.getFieldValue(0, configSource.getFieldIndexByName("bufferSize")).toString());
        addLabeledField(this, labels[3], tooltips[3], configSource.getFieldValue(0, configSource.getFieldIndexByName("amenitiesWeighting")).toString());
        addLabeledField(this, labels[4], tooltips[4], configSource.getFieldValue(0, configSource.getFieldIndexByName("constructibilityWeighting")).toString());
        addLabeledField(this, labels[5], tooltips[5], configSource.getFieldValue(0, configSource.getFieldIndexByName("idealhousingWeighting")).toString());
        addLabeledField(this, labels[6], tooltips[6], configSource.getFieldValue(0, configSource.getFieldIndexByName("gaussDeviation")).toString());
        addLabeledField(this, labels[7], tooltips[7], choice);
        if (choice.equals("schelling")) {
            addLabeledField(this, labels[8], tooltips[8], configSource.getFieldValue(0, configSource.getFieldIndexByName("segregationThreshold")).toString());
            addLabeledField(this, labels[9], tooltips[9], configSource.getFieldValue(0, configSource.getFieldIndexByName("segregationTolerance")).toString());
        }
        else if(choice.equals("statistical")) {
            addLabeledField(this, labels[10], tooltips[10], configSource.getFieldValue(0, configSource.getFieldIndexByName("householdMemory")).toString());
            addLabeledField(this, labels[11], tooltips[11], configSource.getFieldValue(0, configSource.getFieldIndexByName("movingThreshold")).toString());
        }
        addLabeledField(this, labels[12], tooltips[12], configSource.getFieldValue(0, configSource.getFieldIndexByName("immigrantNumber")).toString());
        addLabeledField(this, labels[13], tooltips[13], configSource.getFieldValue(0, configSource.getFieldIndexByName("threshold_1")).toString());
        addLabeledField(this, labels[14], tooltips[14], configSource.getFieldValue(0, configSource.getFieldIndexByName("threshold_2")).toString());
        addLabeledField(this, labels[15], tooltips[15], configSource.getFieldValue(0, configSource.getFieldIndexByName("threshold_3")).toString());
        addLabeledField(this, labels[16], tooltips[16], configSource.getFieldValue(0, configSource.getFieldIndexByName("threshold_4")).toString());
        
        configSource.close();
        
        SpringUtilities.makeCompactGrid(this,lineNumber-2,2,10,10,6,10);
    }
    
    private static JTextField addLabeledField(Container c, String label, String tooltip, String text) {
        JLabel jl = new JLabel(label);
        c.add(jl);
        
        JTextField tf = new JTextField(text, 10);
        tf.setEditable(false);
        jl.setLabelFor(tf);
        jl.setToolTipText(tooltip);
        c.add(tf);
        
        return tf;
    }
}
