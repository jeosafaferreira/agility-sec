/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author jferreira
 */
public class QuadroHorarioEdit extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String course_id, disc_id, teacher_id, lblDisc, lblProf;
    List disc_ids = new ArrayList();
    List teacher_ids = new ArrayList();
    boolean modified;

    public QuadroHorarioEdit(JDialog parent, String course_id, String discipline_id, String teacher_id) {
        super(parent);
        initComponents();

        con = ModuloConexao.conector();
        this.course_id = course_id;
        this.disc_id = discipline_id;
        this.teacher_id = teacher_id;

        try {
            //Lista disciplinas e seleciona a antiga.
            rs = con.prepareStatement("SELECT id, cod, name FROM disciplines WHERE course_id=" + course_id).executeQuery();
            while (rs.next()) {
                disc_ids.add(rs.getString("id"));
                cboDisc.addItem(rs.getString("cod") + " - " + rs.getString("name"));
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1089", ex);
        }

        listTea();

        //Se já tinha dados antigos, ele deixa selecionado nos comboBox, se não, deixa os cbo -1
        if (discipline_id != null) {
            cboDisc.setSelectedIndex(disc_ids.indexOf(discipline_id));
        } else {
            cboDisc.setSelectedIndex(-1);
        }

        if (teacher_id != null) {
            cboProf.setSelectedIndex(teacher_ids.indexOf(teacher_id));
        } else {
            cboProf.setSelectedIndex(-1);
        }

        getRootPane().setDefaultButton(btnOk);
    }

    private void listTea() {
        cboProf.removeAllItems();
        teacher_ids.clear();

        try {
            rs = con.prepareStatement("SELECT E.id, E.name FROM teacher_discipline TD LEFT JOIN employees E ON TD.employee_id=E.id WHERE TD.discipline_id=" + disc_ids.toArray()[cboDisc.getSelectedIndex()] + " GROUP BY E.name").executeQuery();

            while (rs.next()) {
                teacher_ids.add(rs.getString("E.id"));
                cboProf.addItem(rs.getString("E.name"));
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1088", ex);
        }
    }

    private boolean check() {
        boolean r = false;
        if (cboDisc.getSelectedIndex() < 0) {
            AgilitySec.checkMessageError(this, cboDisc, "Disciplina");
        } else if (cboProf.getSelectedIndex() < 0) {
            AgilitySec.checkMessageError(this, cboDisc, "Professor");
        } else {
            r = true;
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboDisc = new javax.swing.JComboBox<>();
        cboProf = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EDITAR HORÁRIO");
        setAlwaysOnTop(true);
        setModal(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Disciplina:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Professor:");

        cboDisc.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboDiscPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboProf, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel4))
                            .addComponent(cboDisc, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 156, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboProf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 148, -1, -1));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Salvar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 148, 119, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 410, 210));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (check()) {
            lblDisc = cboDisc.getSelectedItem().toString();
            lblProf = cboProf.getSelectedItem().toString();
            this.disc_id = (String) disc_ids.toArray()[cboDisc.getSelectedIndex()];
            this.teacher_id = (String) teacher_ids.toArray()[cboProf.getSelectedIndex()];
            this.modified = true;
            this.dispose();
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void cboDiscPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboDiscPopupMenuWillBecomeInvisible
        listTea();
    }//GEN-LAST:event_cboDiscPopupMenuWillBecomeInvisible

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuadroHorarioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuadroHorarioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuadroHorarioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuadroHorarioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuadroHorarioEdit dialog = new QuadroHorarioEdit(new JDialog(), "", "", "");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox<String> cboDisc;
    private javax.swing.JComboBox<String> cboProf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
