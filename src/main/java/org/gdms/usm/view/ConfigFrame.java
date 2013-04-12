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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Thomas Salliou
 */
public class ConfigFrame extends JFrame implements ActionListener {
    
    public ConfigFrame() {
        
        super("Urban Sprawl Model - Configuration");
        JPanel chooserPanel = new JPanel(new BorderLayout(20, 20));
        
        //Labels
        JLabel welcome = new JLabel("<html>Welcome to the Urban Sprawl Model simulation plugin.<br />You'll need a config file to run it :</html>");
        JLabel create = new JLabel("Create a new config file");
        JLabel or = new JLabel("OR",JLabel.CENTER);
        JLabel choose = new JLabel("Choose an existing one");
        
        //Buttons
        JButton createButton = new JButton("Create");
        createButton.setActionCommand("create");
        createButton.addActionListener(this);
        JButton chooseButton = new JButton("Choose");
        chooseButton.setActionCommand("choose");
        chooseButton.addActionListener(this);
        JButton cancelButton = new JButton("Return to OrbisGIS");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        
        //Layout
        JPanel createPart = new JPanel(new BorderLayout(10,10));
        createPart.add(create, BorderLayout.NORTH);
        createPart.add(createButton, BorderLayout.SOUTH);
        JPanel choosePart = new JPanel(new BorderLayout(10,10));
        choosePart.add(choose, BorderLayout.NORTH);
        choosePart.add(chooseButton, BorderLayout.SOUTH);
        chooserPanel.add(choosePart, BorderLayout.EAST);
        chooserPanel.add(or, BorderLayout.CENTER);
        chooserPanel.add(createPart, BorderLayout.WEST);
        chooserPanel.add(welcome,BorderLayout.NORTH);
        chooserPanel.add(cancelButton, BorderLayout.SOUTH);
        chooserPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(chooserPanel);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("create".equals(e.getActionCommand())) {
            new CreateFrame();
            dispose();
        }
        else if ("choose".equals(e.getActionCommand())) {
            new ChooseFrame();
            dispose();
        }
        else {
            dispose();
        }
    }
}
