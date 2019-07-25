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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jferreira
 */
public class Turmas extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home; //Usado pra abrir JDialog
    TurmaDetalhes turmasDetalhes;
    DefaultTableModel dtm;

    public Turmas(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblTurmas.getModel();
        listData();
    }

    public void listData() {
        dtm.setNumRows(0);
        try {
            pst = con.prepareStatement(""
                    + "SELECT "
                    + "    CLA.*, COU.name, CLR.name, COUNT(STU.cod)"
                    + " FROM"
                    + "    classes CLA"
                    + "        LEFT JOIN"
                    + "    students STU ON STU.class_id = CLA.id"
                    + "        LEFT JOIN"
                    + "    courses COU ON COU.id = CLA.course_id"
                    + "        LEFT JOIN"
                    + "    classrooms CLR ON CLR.id = CLA.classroom_id"
                    + " WHERE"
                    + "    CLA.cod IS NOT NULL"
                    + " GROUP BY CLA.id"
            );

            rs = pst.executeQuery();
            while (rs.next()) {
                dtm.addRow(new String[]{
                    rs.getString("CLA.cod"),
                    rs.getString("CLA.name"),
                    rs.getString("COU.name"),
                    rs.getString("CLA.turno"),
                    rs.getString("CLR.name"),
                    rs.getString("COUNT(STU.cod)") + "/" + rs.getString("CLA.capacity")
                });
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#0043", e);
        }

    }

    private void filter() {

        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtm);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(4);
        RowFilter<Object, Object> rf;

        tblTurmas.setRowSorter(tr);

        if (txtCod.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));
        }

        if (txtDesc.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 1));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtDesc.getText(), 1));
        }

        if (txtCurso.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 2));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtCurso.getText(), 2));
        }
        if (cboTurno.getSelectedIndex() == 3) {
            filters.add(RowFilter.regexFilter("", 3));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + cboTurno.getSelectedItem(), 3));
        }
        if (txtSala.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 3));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtSala.getText(), 4));
        }
        rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);
        if (tblTurmas.getRowCount() > 0) {
            tblTurmas.setRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDesc = new javax.swing.JTextField();
        txtSala = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtCurso = new javax.swing.JTextField();
        btnLimpar = new javax.swing.JButton();
        cboTurno = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTurmas = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDet = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("TURMAS - Agility®");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentShown(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sala:");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Turno:");

        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });
        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Desc. da Turma:");

        txtDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescActionPerformed(evt);
            }
        });
        txtDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDescKeyReleased(evt);
            }
        });

        txtSala.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalaKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Curso:");

        txtCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCursoActionPerformed(evt);
            }
        });
        txtCurso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCursoKeyReleased(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        cboTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manhã", "Tarde", "Noite", "<html><b>MOSTRAR TODOS" }));
        cboTurno.setSelectedIndex(3);
        cboTurno.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurnoPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtCurso, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cboTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtSala, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 5, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpar)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 830, -1));

        tblTurmas.setAutoCreateRowSorter(true);
        tblTurmas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Curso", "Turno", "Sala", "Lotação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTurmas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTurmas.getTableHeader().setReorderingAllowed(false);
        tblTurmas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTurmasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTurmas);
        if (tblTurmas.getColumnModel().getColumnCount() > 0) {
            tblTurmas.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblTurmas.getColumnModel().getColumn(1).setPreferredWidth(135);
            tblTurmas.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblTurmas.getColumnModel().getColumn(3).setPreferredWidth(163);
            tblTurmas.getColumnModel().getColumn(4).setPreferredWidth(163);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 830, 250));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Criar Turma");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 160, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar Turma");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 160, -1));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 350, 200, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 855, 410));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filter();
    }//GEN-LAST:event_txtCodKeyReleased

    private void txtDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtDescActionPerformed

    private void txtDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescKeyReleased
        filter();
    }//GEN-LAST:event_txtDescKeyReleased

    private void txtSalaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalaKeyReleased
        filter();
    }//GEN-LAST:event_txtSalaKeyReleased

    private void txtCursoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursoKeyReleased
        filter();
    }//GEN-LAST:event_txtCursoKeyReleased

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtCod.setText("");
        txtCurso.setText("");
        txtDesc.setText("");
        txtSala.setText("");
        cboTurno.setSelectedIndex(3);
        filter();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void jPanel2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentShown

    }//GEN-LAST:event_jPanel2ComponentShown

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        TurmaForm tn = new TurmaForm(home, false);
        tn.setVisible(true);
        listData();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tblTurmas.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(home, "Por favor, selecione uma turma na tabela.");
            txtCod.grabFocus();
        } else {
            TurmaForm tn = new TurmaForm(home, false, (String) tblTurmas.getValueAt(tblTurmas.getSelectedRow(), 0));
            tn.setVisible(true);
            if (tn.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblTurmas.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione uma turma na tabela!");
            txtCod.grabFocus();
        } else {
            turmasDetalhes = new TurmaDetalhes(tblTurmas.getValueAt(tblTurmas.getSelectedRow(), 0).toString());
            turmasDetalhes.setVisible(true);
            if (turmasDetalhes.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCursoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCursoActionPerformed

    private void cboTurnoPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurnoPopupMenuWillBecomeInvisible
        filter();
    }//GEN-LAST:event_cboTurnoPopupMenuWillBecomeInvisible

    private void tblTurmasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTurmasMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblTurmasMouseClicked

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
            java.util.logging.Logger.getLogger(Turmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Turmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Turmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Turmas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Turmas dialog = new Turmas(new JFrame());
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
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox<String> cboTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTurmas;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCurso;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtSala;
    // End of variables declaration//GEN-END:variables

}
