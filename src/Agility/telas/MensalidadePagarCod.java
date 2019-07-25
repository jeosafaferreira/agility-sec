/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class MensalidadePagarCod extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String idTuiUsu, idTuiSql, idLastTui, stuCod;

    public MensalidadePagarCod(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        getRootPane().setDefaultButton(btnOk);
        txtCod.grabFocus();
    }

    private void searchTui() {
        this.idTuiUsu = txtCod.getText();

        if (idTuiUsu.equals("")) {
            this.dispose();
            new MensalidadeAlunoSelec(new Home()).setVisible(true);
        } else {
            try {
                rs = con.prepareStatement("SELECT id, student_cod, valor_pago FROM tuitions WHERE id='" + idTuiUsu + "'").executeQuery();
                if (rs.next()) {
                    this.dispose();

                    this.idTuiSql = rs.getString(1);
                    this.stuCod = rs.getString("student_cod");

                    //verifica se já está pago
                    if (rs.getString("valor_pago") == null) {
                        //verifica se tem anterior
                        rs = con.prepareStatement("SELECT id, MIN(venc) FROM tuitions WHERE student_cod='" + this.stuCod + "' AND valor_pago IS NULL").executeQuery();
                        rs.next();
                        if (idTuiSql.equals(rs.getString("id"))) {
                            new MensalidadePagar(this, idTuiSql).setVisible(true);
                        } else {

                            int q = JOptionPane.showConfirmDialog(this, "Atenção! Existe mensalidade anterior em aberto!\nDeseja abri-la?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                            if (q == JOptionPane.YES_OPTION) {
                                MensalidadeAlunoSelec.payLastTuition(stuCod);
                            }
                        }
                    } else {
                        int q = JOptionPane.showConfirmDialog(this, "Esta mensalidade já está quitada!\nDeseja abrir a próxima mensalidade a se vencer?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                        if (q == JOptionPane.YES_OPTION) {
                            MensalidadeAlunoSelec.payLastTuition(stuCod);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Código inválido!");
                    txtCod.setText("");
                }
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1093", ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnPes = new javax.swing.JButton();
        btnCan = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Código");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Informe o código da mensalidade:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
        });
        getContentPane().add(txtCod, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 27, 302, -1));

        btnOk.setText("Confirmar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 60, -1, -1));

        btnPes.setText("Pesquisar");
        btnPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesActionPerformed(evt);
            }
        });
        getContentPane().add(btnPes, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(196, 60, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 330, 110));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        searchTui();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesActionPerformed
        new MensalidadeAlunoSelec(new Home()).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnPesActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }//GEN-LAST:event_txtCodKeyReleased

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
            java.util.logging.Logger.getLogger(MensalidadePagarCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagarCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagarCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagarCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MensalidadePagarCod dialog = new MensalidadePagarCod(new JFrame());
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
    private javax.swing.JButton btnPes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtCod;
    // End of variables declaration//GEN-END:variables

}
