package Agility.telas.relatorios;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author jferreira
 */
public class DisSelec extends javax.swing.JDialog {

    Connection con;
    ResultSet rs, rsCou;
    String teacher_id, course_id, class_id, discipline_id, period, bim, type;
    Set cboCourseId = new LinkedHashSet();
    Set cboClassId = new LinkedHashSet();
    Set cboDiscId = new LinkedHashSet();
    public boolean modified;

    public DisSelec() {
        initComponents();
        con = ModuloConexao.conector();
        listCourses();
    }

    private void listCourses() {
        cboCur.removeAllItems();
        cboTur.setEnabled(false);
        cboDisc.setEnabled(false);

        try {
            rs = con.prepareStatement("SELECT C.* FROM courses C").executeQuery();
            while (rs.next()) {
                cboCur.addItem(rs.getString("C.name"));
                cboCourseId.add(rs.getString("C.id"));
            }
            cboCur.setSelectedIndex(-1);
            if (cboCur.getItemCount() == 1) {
                cboCur.setSelectedIndex(0);
                listClasses();
            }

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1118", ex);
        }
    }

    private void listClasses() {
        this.course_id = (String) cboCourseId.toArray()[cboCur.getSelectedIndex()];
        cboTur.removeAllItems();
        cboTur.setEnabled(true);
        cboDisc.setEnabled(false);
        try {
            rs = con.prepareStatement("SELECT CLA.id, CLA.name FROM classes CLA WHERE CLA.course_id='" + this.course_id + "'").executeQuery();
            while (rs.next()) {
                cboClassId.add(rs.getString("CLA.id"));
                cboTur.addItem(rs.getString("CLA.name"));
            }
            cboTur.setSelectedIndex(-1);
            if (cboTur.getItemCount() == 1) {
                cboTur.setSelectedIndex(0);
                listDisciplines();
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1119", ex);
        }
    }

    private void listDisciplines() {
        this.class_id = (String) cboClassId.toArray()[cboTur.getSelectedIndex()];
        cboDisc.removeAllItems();
        cboDisc.setEnabled(true);
        try {
            rs = con.prepareStatement("SELECT D.id, D.name FROM disciplines D WHERE D.course_id='" + this.course_id + "'").executeQuery();

            while (rs.next()) {
                cboDisc.addItem(rs.getString("D.name"));
                cboDiscId.add(rs.getString("D.id"));
            }
            cboDisc.setSelectedIndex(-1);
            if (cboDisc.getItemCount() == 1) {
                cboDisc.setSelectedIndex(0);
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1120", ex);
        }
    }

    private boolean check() {
        boolean r = false;

        if (cboCur.getSelectedIndex() != -1) {
            if (cboTur.getSelectedIndex() != -1) {
                r = true;
            } else {
                AgilitySec.checkMessageError(this, cboTur, "Turma");
            }
        } else {
            AgilitySec.checkMessageError(this, cboCur, "Curso");
        }
        return r;
    }

    public String getId() {
        return cboDiscId.toArray()[cboDisc.getSelectedIndex()].toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboCur = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboTur = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboDisc = new javax.swing.JComboBox<>();
        btnCan = new javax.swing.JButton();
        btnCon = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SELECIONAR DISCIPLINA");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Curso:");

        cboCur.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboCurPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Turma:");

        cboTur.setEnabled(false);
        cboTur.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Disciplina:");

        cboDisc.setEnabled(false);
        cboDisc.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboDiscPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cboTur, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboDisc, javax.swing.GroupLayout.Alignment.LEADING, 0, 150, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });

        btnCon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnCon.setText("Confirmar");
        btnCon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCan, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnCon))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCan)
                    .addComponent(btnCon))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConActionPerformed
        if (check()) {
            this.modified = true;
            this.dispose();
        }
    }//GEN-LAST:event_btnConActionPerformed

    private void cboCurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCurPopupMenuWillBecomeInvisible
        if (cboCur.getSelectedIndex() != -1) {
            listClasses();
        }
    }//GEN-LAST:event_cboCurPopupMenuWillBecomeInvisible

    private void cboTurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurPopupMenuWillBecomeInvisible
        if (cboTur.getSelectedIndex() != -1) {
            listDisciplines();
        }
    }//GEN-LAST:event_cboTurPopupMenuWillBecomeInvisible

    private void cboDiscPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboDiscPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_cboDiscPopupMenuWillBecomeInvisible

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(DisSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DisSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DisSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DisSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DisSelec dialog = new DisSelec();
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
    private javax.swing.JButton btnCon;
    private javax.swing.JComboBox<String> cboCur;
    private javax.swing.JComboBox<String> cboDisc;
    private javax.swing.JComboBox<String> cboTur;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
