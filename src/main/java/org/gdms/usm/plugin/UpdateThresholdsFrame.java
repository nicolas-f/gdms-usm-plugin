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
 * Copyright (C) 2011-1012 IRSTV (FR CNRS 2488)
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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import org.gdms.usm.Manager;

/**
 *
 * @author Belgarion
 */
public class UpdateThresholdsFrame extends javax.swing.JFrame implements ActionListener {

    
    UpdateThresholdsPanel utp;
    private final ProgressFrame progressFrame;
    /**
     * Creates new form UpdateThresholdFrame
     */
    public UpdateThresholdsFrame(Manager m,ProgressFrame pf) {
        super("Update Thresholds");
        progressFrame = pf;
        utp = new UpdateThresholdsPanel(m);
        add(utp,BorderLayout.NORTH);
        
        //Buttons
        JButton actualiseButton = new JButton("actualise");
        actualiseButton.setActionCommand("actualise");
        actualiseButton.addActionListener(this);
        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);

        
                //Layout
        JPanel actionPart = new JPanel();
        actionPart.add(actualiseButton);
        actionPart.add(okButton);
        add(actionPart, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("actualise")) {
            utp.actualise();
            return;
        }
        if(ae.getActionCommand().equals("OK")) {
            utp.save();
            this.dispose();
            this.progressFrame.setThresholdsUpOnDate(true);
            return;
        }
    }

    
}