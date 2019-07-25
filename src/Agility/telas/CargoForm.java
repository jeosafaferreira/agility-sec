package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.text.PlainDocument;

/**
 *
 * @author jferreira
 */
public class CargoForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    boolean modified;
    String type, occ_id, occ_title;

    public CargoForm() {
        initComponents();
        con = ModuloConexao.conector();
        type = "new";
        txtSal.setDocument(new AgilitySec.onlyNumbersNPoints());
        getRootPane().setDefaultButton(btnSal);
    }

    public CargoForm(String id) {
        initComponents();
        con = ModuloConexao.conector();
        type = "upd";
        this.occ_id = id;
        this.setTitle("ALTERAR CARGO");
        txtSal.setDocument(new AgilitySec.onlyNumbersNPoints());
        listData();
        getRootPane().setDefaultButton(btnSal);
    }

    private void listData() {
        try {
            rs = con.prepareStatement("SELECT * FROM occupations WHERE id='" + this.occ_id + "'").executeQuery();
            if (rs.next()) {
                this.occ_title = rs.getString("title");
                txtCar.setText(this.occ_title);
                txtSal.setText(rs.getString("salary_base").replace(".", ","));

            } else {
                AgilitySec.showError(this, "#1039");
            }
        } catch (SQLException e) {
            AgilitySec.showError(this, "#1050", e);
        }
    }

    private boolean check() {
        boolean r = false;
        if (!txtCar.getText().equals("")) {
            if (!txtSal.getText().equals("")) {
                r = true;
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Salário Base\".");
                txtSal.grabFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Cargo\".");
            txtCar.grabFocus();
        }
        return r;
    }

    private void execQuery() {
        String sql, msg;
        Date now = new Date();
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

        if (this.type.equals("upd")) {
            sql = "UPDATE occupations SET title=?, salary_base=?, modified=?, modified_by=? WHERE id='" + this.occ_id + "'";
            msg = "Dados atualizados com sucesso!";
        } else {
            sql = "INSERT INTO occupations SET title=?, salary_base=?, created=?, created_by=?";
            msg = "Dados cadastrados com sucesso!";
        }
        try {
            if (AgilitySec.alreadyExists(con, "occupations", "title!='" + this.occ_title + "' AND title='" + txtCar.getText() + "'")) {
                JOptionPane.showMessageDialog(this, "Já existe um cargo com este nome!");
                txtCar.grabFocus();
            } else {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtCar.getText());
                pst.setString(2, txtSal.getText());
                pst.setString(3, dateBr.format(now));
                pst.setString(4, Login.emp_id);
                if (pst.executeUpdate() > 0) {
                    this.modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, msg);
                }
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#0051", ex);
        }
    }

    private boolean confirmEdit() {
        boolean r = false;
        try {
            rs = con.prepareStatement("SELECT id FROM employees WHERE occupation_id='" + this.occ_id + "'").executeQuery();
            if (rs.next()) {
                rs.last();
                int qq = JOptionPane.showConfirmDialog(this, "<html>Esta alteração irá afetar <b> " + rs.getRow() + " funcionários.</b> Deseja continuar?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (qq == JOptionPane.YES_OPTION) {
                    r = true;
                }
            } else {
                r = true;
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1064", ex);
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCar = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSal = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnSal = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setTitle("CADASTRAR CARGO");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cargo:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Salário base (R$):");

        jLabel79.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(233, 2, 2));
        jLabel79.setText("*");

        jLabel80.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(233, 2, 2));
        jLabel80.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel79))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel80))
                    .addComponent(txtCar, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(txtSal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel80))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(199, 150, -1, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 150, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 330, 210));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (this.type.equals("upd")) {
            this.dispose();
        } else if (!txtCar.getText().equals("") || !txtSal.getText().equals("")) {
            int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (q == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
        if (check()) {
            if (confirmEdit()) {
                execQuery();
            } else {
                this.dispose();
            }
        }
    }//GEN-LAST:event_btnSalActionPerformed

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
            java.util.logging.Logger.getLogger(CargoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CargoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CargoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CargoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CargoForm dialog = new CargoForm();
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
    private javax.swing.JButton btnSal;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCar;
    private javax.swing.JTextField txtSal;
    // End of variables declaration//GEN-END:variables

}
