/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agility.telas.relatorios;

import Agility.telas.*;
import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class ResSelec extends javax.swing.JDialog {

    static Connection con;
    static PreparedStatement pst;
    static ResultSet rs;
    public boolean modified;

    public ResSelec() {
        initComponents();
        con = ModuloConexao.conector();
    }

    private void search() {
        int row = 0;
        String sql;
        if (txtNome.getText().isEmpty()) {
            sql = "SELECT id, name, dataNasc, created FROM responsibles WHERE id LIKE '%" + txtId.getText() + "%'";
        } else {
            sql = "SELECT id, name, dataNasc, created FROM responsibles WHERE name LIKE '%" + txtNome.getText() + "%' ";
        }
        try {
            rs = con.prepareStatement(sql).executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Código", "Nome Completo", "Data de Nasc.", "Cadastrado em"};
                    int cols[] = {50, 250, 0, 0};

                    Dialog dAlu = new Dialog(this, true, sql, headers, cols);
                    dAlu.setVisible(true);
                    row = dAlu.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);

                    txtId.setText(rs.getString("id"));
                    txtNome.setText(rs.getString("name"));
                    txtDataCad.setText(rs.getString("created"));
                    txtDataNasc.setText(rs.getString("dataNasc"));

                    btnOkActionPerformed(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Responsável não encontrado.");
                txtId.grabFocus();
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#1104", e);
        }
    }

    private boolean check() {
        boolean r = false;
        if (!txtDataNasc.getText().isEmpty()) {
            r = true;
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um responsável!");
        }
        return r;
    }

    public String getId() {
        return txtId.getText();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        pnlAluno = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnPesqAlu = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtDataNasc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDataCad = new javax.swing.JTextField();
        btnEraseAlu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selecionar Responsável");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Confirmar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder("Responsável"));

        jLabel1.setText("Código:");

        txtId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdKeyReleased(evt);
            }
        });

        txtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeKeyReleased(evt);
            }
        });

        jLabel3.setText("Nome:");

        btnPesqAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnPesqAlu.setText("...");
        btnPesqAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqAluActionPerformed(evt);
            }
        });

        jLabel5.setText("Data de Nasc.:");

        txtDataNasc.setEditable(false);
        txtDataNasc.setEnabled(false);
        txtDataNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataNascActionPerformed(evt);
            }
        });

        jLabel9.setText("Cadastrado em:");

        txtDataCad.setEditable(false);
        txtDataCad.setEnabled(false);

        btnEraseAlu.setText("Limpar dados");
        btnEraseAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEraseAluActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAlunoLayout = new javax.swing.GroupLayout(pnlAluno);
        pnlAluno.setLayout(pnlAlunoLayout);
        pnlAlunoLayout.setHorizontalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDataNasc, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(6, 6, 6))
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)))
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                                .addComponent(txtDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEraseAlu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesqAlu)))
                .addContainerGap())
        );
        pnlAlunoLayout.setVerticalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(0, 0, 0)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesqAlu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDataNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEraseAlu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOk))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlAluno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlAluno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOk)
                    .addComponent(btnCan))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdKeyReleased
        txtNome.setText(null);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            search();
        }
    }//GEN-LAST:event_txtIdKeyReleased

    private void txtNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyReleased
        txtId.setText(null);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            search();
        }
    }//GEN-LAST:event_txtNomeKeyReleased

    private void btnPesqAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqAluActionPerformed
        search();
    }//GEN-LAST:event_btnPesqAluActionPerformed

    private void txtDataNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataNascActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataNascActionPerformed

    private void btnEraseAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEraseAluActionPerformed
        txtId.setText(null);
        txtNome.setText(null);
        txtDataCad.setText(null);
        txtDataNasc.setText(null);

        txtId.grabFocus();
    }//GEN-LAST:event_btnEraseAluActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (check()) {
            this.modified = true;
            this.dispose();
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

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
            java.util.logging.Logger.getLogger(ResSelec.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResSelec.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResSelec.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResSelec.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ResSelec dialog = new ResSelec();
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
    private javax.swing.JButton btnEraseAlu;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPesqAlu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JTextField txtDataCad;
    private javax.swing.JTextField txtDataNasc;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
