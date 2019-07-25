package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class NotaSelec extends javax.swing.JDialog {

    Connection con;
    ResultSet rs, rsCou;
    String emp_id, course_id, class_id, discipline_id, period, bim, type;
    Set cboCourseId = new LinkedHashSet();
    Set cboClassId = new LinkedHashSet();
    Set cboDiscId = new LinkedHashSet();

    public NotaSelec(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        emp_id = Login.emp_id;
        cboAnoLet.addItem(Login.academic_year);
        listCourses();
    }

    private void listCourses() {
        cboCur.removeAllItems();
        cboCla.setEnabled(false);
        cboDisc.setEnabled(false);
        cboPer.setEnabled(false);

        try {
            rs = con.prepareStatement("SELECT "
                    + "    id, name "
                    + "FROM "
                    + "    courses").executeQuery();
            while (rs.next()) {
                cboCur.addItem(rs.getString("name"));
                cboCourseId.add(rs.getString("id"));
            }
            cboCur.setSelectedIndex(-1);
            if (cboCur.getItemCount() == 1) {
                cboCur.setSelectedIndex(0);
                listClasses();
            }

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#0018", ex);
        }
    }

    private void listClasses() {
        this.course_id = (String) cboCourseId.toArray()[cboCur.getSelectedIndex()];
        cboCla.removeAllItems();
        cboCla.setEnabled(true);
        cboDisc.setEnabled(false);
        cboPer.setEnabled(false);
        try {
            rs = con.prepareStatement("SELECT "
                    + "    id, cod, name "
                    + "FROM "
                    + "    classes "
                    + "WHERE "
                    + "    course_id = '" + course_id + "' ").executeQuery();
            while (rs.next()) {
                cboClassId.add(rs.getString("id"));
                cboCla.addItem(rs.getString("name") + " (" + rs.getString("cod") + ")");
            }
            cboCla.setSelectedIndex(-1);
            if (cboCla.getItemCount() == 1) {
                cboCla.setSelectedIndex(0);
                listDisciplines();
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#0002", ex);
        }
    }

    private void listDisciplines() {
        this.class_id = (String) cboClassId.toArray()[cboCla.getSelectedIndex()];
        cboDisc.removeAllItems();
        cboDisc.setEnabled(true);
        cboPer.setEnabled(false);
        try {
            rs = con.prepareStatement("SELECT  "
                    + "    id, name "
                    + "FROM "
                    + "    disciplines "
                    + "WHERE "
                    + "    course_id = '" + course_id + "' ").executeQuery();

            while (rs.next()) {
                cboDisc.addItem(rs.getString("name"));
                cboDiscId.add(rs.getString("id"));
            }
            cboDisc.setSelectedIndex(-1);
            if (cboDisc.getItemCount() == 1) {
                cboDisc.setSelectedIndex(0);
                listPeriods();
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#0006", ex);
        }
    }

    private void listPeriods() {
        this.discipline_id = (String) cboDiscId.toArray()[cboDisc.getSelectedIndex()];
        cboPer.setEnabled(true);
    }

    private boolean check() {
        boolean r = false;

        switch (cboPer.getSelectedItem().toString()) {
            case "1º Bimestre":
                this.bim = "bim1";
                break;
            case "2º Bimestre":
                this.bim = "bim2";
                break;
            case "3º Bimestre":
                this.bim = "bim3";
                break;
            case "4º Bimestre":
                this.bim = "bim4";
                break;
            case "Recuperação":
                this.bim = "rec";
                break;
        }

        if (cboCur.getSelectedIndex() != -1) {
            if (cboCla.getSelectedIndex() != -1) {
                if (cboPer.getSelectedIndex() != -1) {
                    r = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, selecione um período.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione uma turma.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um curso.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
        }

        if (r) {
            //VERIFICA SE JÁ TEM DADOS DESSA TURMA NESSA DISCIPLINA
            try {
                if (AgilitySec.alreadyExists(con, "boletins",
                        "academic_year='" + Login.academic_year + "' "
                        + "AND course_id='" + course_id + "' "
                        + "AND class_id='" + class_id + "' "
                        + "AND discipline_id='" + discipline_id + "' "
                        + "AND " + bim + "_n1 IS NOT NULL")) {

                    r = true;
                    this.type = "upd";

                } else {
                    r = false;
                    //VERIFICA SE AS NOTAS DO BIMESTRE ANTERIOR FORAM CADASTRADAS.
                    this.type = "new";
                    switch (bim) {
                        case "bim1":
                            r = true;
                            break;
                        case "bim2":
                            if (AgilitySec.alreadyExists(con, "boletins",
                                    "academic_year='" + Login.academic_year + "' "
                                    + "AND course_id='" + course_id + "' "
                                    + "AND class_id='" + class_id + "' "
                                    + "AND discipline_id='" + discipline_id + "' "
                                    + "AND bim1_n1 IS NOT NULL")) {
                                r = true;
                            } else {
                                JOptionPane.showMessageDialog(this, "Desculpe, não é possível prosseguir.\nPor favor, certifique-se de que as notas do bimestre anterior ao escolhido foram cadastradas.", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
                                r = false;
                            }
                            break;
                        case "bim3":
                            if (AgilitySec.alreadyExists(con, "boletins",
                                    "academic_year='" + Login.academic_year + "' "
                                    + "AND course_id='" + course_id + "' "
                                    + "AND class_id='" + class_id + "' "
                                    + "AND discipline_id='" + discipline_id + "' "
                                    + "AND bim2_n1 IS NOT NULL")) {
                                r = true;
                            } else {
                                JOptionPane.showMessageDialog(this, "Desculpe, não é possível prosseguir.\nPor favor, certifique-se de que as notas do bimestre anterior ao escolhido foram cadastradas.", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
                            }
                            break;
                        case "bim4":
                            if (AgilitySec.alreadyExists(con, "boletins",
                                    "academic_year='" + Login.academic_year + "' "
                                    + "AND course_id='" + course_id + "' "
                                    + "AND class_id='" + class_id + "' "
                                    + "AND discipline_id='" + discipline_id + "' "
                                    + "AND bim3_n1 IS NOT NULL")) {
                                r = true;
                            } else {
                                JOptionPane.showMessageDialog(this, "Desculpe, não é possível prosseguir.\nPor favor, certifique-se de que as notas do bimestre anterior ao escolhido foram cadastradas.", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
                                r = false;
                            }
                            break;
                        case "rec":
                            if (AgilitySec.alreadyExists(con, "boletins",
                                    "academic_year='" + Login.academic_year + "' "
                                    + "AND course_id='" + course_id + "' "
                                    + "AND class_id='" + class_id + "' "
                                    + "AND discipline_id='" + discipline_id + "' "
                                    + "AND bim4_n1 IS NOT NULL")) {
                                r = true;
                            } else {
                                JOptionPane.showMessageDialog(this, "Desculpe, não é possível prosseguir.\nPor favor, certifique-se de que as notas do bimestre anterior ao escolhido foram cadastradas.", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
                                r = false;
                            }
                            break;
                    }

                }
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#0008", ex);
            }
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboCur = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboCla = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cboPer = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboDisc = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboAnoLet = new javax.swing.JComboBox<>();
        btnCan = new javax.swing.JButton();
        btnCon = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SELECIONAR TURMA");
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

        cboCla.setEnabled(false);
        cboCla.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboClaPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Período:");

        cboPer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "Recuperação" }));
        cboPer.setSelectedIndex(-1);
        cboPer.setEnabled(false);

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

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Ano Letivo:");

        cboAnoLet.setEnabled(false);
        cboAnoLet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAnoLetActionPerformed(evt);
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
                    .addComponent(jLabel3)
                    .addComponent(cboPer, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cboCla, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboDisc, javax.swing.GroupLayout.Alignment.LEADING, 0, 150, Short.MAX_VALUE))
                    .addComponent(cboAnoLet, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboAnoLet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboDisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(0, 20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnConActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConActionPerformed
        if (check()) {
            NotaInserir ni = new NotaInserir(this, course_id, class_id, discipline_id, this.bim, this.type);
            this.dispose();
            ni.show();
        }
    }//GEN-LAST:event_btnConActionPerformed

    private void cboCurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCurPopupMenuWillBecomeInvisible
        if (cboCur.getSelectedIndex() != -1) {
            listClasses();
        }
    }//GEN-LAST:event_cboCurPopupMenuWillBecomeInvisible

    private void cboClaPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboClaPopupMenuWillBecomeInvisible
        if (cboCla.getSelectedIndex() != -1) {
            listDisciplines();
        }
    }//GEN-LAST:event_cboClaPopupMenuWillBecomeInvisible

    private void cboDiscPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboDiscPopupMenuWillBecomeInvisible
        if (cboDisc.getSelectedIndex() != -1) {
            listPeriods();
        }
    }//GEN-LAST:event_cboDiscPopupMenuWillBecomeInvisible

    private void cboAnoLetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAnoLetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAnoLetActionPerformed

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
            java.util.logging.Logger.getLogger(NotaSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotaSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotaSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotaSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NotaSelec dialog = new NotaSelec(new JFrame());
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
    private javax.swing.JComboBox<String> cboAnoLet;
    private javax.swing.JComboBox<String> cboCla;
    private javax.swing.JComboBox<String> cboCur;
    private javax.swing.JComboBox<String> cboDisc;
    public javax.swing.JComboBox<String> cboPer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
