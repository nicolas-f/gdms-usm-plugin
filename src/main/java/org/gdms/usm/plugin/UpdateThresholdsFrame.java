/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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