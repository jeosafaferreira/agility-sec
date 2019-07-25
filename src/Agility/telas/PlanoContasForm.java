package Agility.telas;

import Agility.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author jeosafaferreira
 */
public class PlanoContasForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String type, acc_plan_id, sql, oldTit, oldType;
    boolean modified;

    public PlanoContasForm(java.awt.Frame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        type = "new";
        getRootPane().setDefaultButton(btnSal);

    }

    public PlanoContasForm(String cod) {
        initComponents();
        con = ModuloConexao.conector();
        type = "upd";
        acc_plan_id = cod;
        getData();
        oldTit = txtTit.getText();
        oldType = cboTip.getSelectedItem().toString();
        this.setTitle("Alterar Plano de Contas (#" + cod + ")");
        getRootPane().setDefaultButton(btnSal);
    }

    private void getData() {
        try {
            rs = con.prepareStatement("SELECT * FROM acc_plan WHERE id='" + acc_plan_id + "'").executeQuery();
            rs.next();
            txtTit.setText(rs.getString("title"));
            txtObs.setText(rs.getString("obs"));
            cboTip.setSelectedItem(rs.getString("type"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1032\nDetalhes do erro:\n" + e);
        }
    }

    /**
     *
     * @return true se tiver duplicado.
     */
    private boolean isDupli() {
        boolean r = false;
        try {
            rs = con.prepareStatement("SELECT id FROM acc_plan WHERE type='" + cboTip.getSelectedItem() + "' AND title='" + txtTit.getText() + "'").executeQuery();
            if (rs.next()) {
                r = true;
            } else {
                r = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1036\nDetalhes do erro:\n" + e);
        }
        return r;
    }

    private int check() {
        int r = 0;
        if (txtTit.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Título/Desc.\".");
            txtTit.grabFocus();

        } else if (cboTip.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção no campo \"Tipo\".");
            cboTip.grabFocus();
        } else if (type.equals("upd")) {
            //Verifica se os campos foram alterados. Se tiverem sido alterados, verifica se há duplicidade no db. 
            //Só deixa salvar(r=1) se não tiver duplicidade.
            if (!txtTit.getText().equals(oldTit) || !cboTip.getSelectedItem().equals(oldType)) {
                if (isDupli()) {
                    JOptionPane.showMessageDialog(this, "Não foi possível salvar.\n<html>Já existe um plano de contas do tipo \"<b>" + cboTip.getSelectedItem() + "</b>\" com este nome!");
                    txtTit.grabFocus();
                } else {
                    r = 1;
                }
            } else {
                r = 1;
            }
        } else if (isDupli()) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar.\n<html>Já existe um plano de contas do tipo \"<b>" + cboTip.getSelectedItem() + "</b>\" com este nome!");
            txtTit.grabFocus();
        } else {
            r = 1;
        }
        return r;
    }

    private void execQuery() {
        if (check() == 1) {
            String txtErro1 = null;
            String txtErro2 = null;
            String txtSuc = null;

            if (type.equals("new")) {
                sql = "INSERT INTO acc_plan (title, obs, type, created_by) VALUES(?,?,?,?)";
                txtSuc = "Plano de Contas cadastrado com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1010";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1011\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                sql = "UPDATE acc_plan SET title=?, obs=?, type=?, modified_by=? WHERE id=" + acc_plan_id;
                txtSuc = "Plano de Contas atualizado com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1012";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1033\nDetalhes do erro:\n";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtTit.getText());
                pst.setString(2, txtObs.getText());
                pst.setString(3, cboTip.getSelectedItem().toString());
                pst.setString(4, Login.emp_id);
                if (pst.executeUpdate() == 0) {
                    JOptionPane.showMessageDialog(null, txtErro1);
                } else {
                    this.dispose();
                    JOptionPane.showMessageDialog(null, txtSuc);
                    modified = true;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, txtErro2 + e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        jLabel74 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        cboTip = new javax.swing.JComboBox<>();
        btnCan = new javax.swing.JButton();
        btnSal = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NOVO PLANO DE CONTAS");
        setAlwaysOnTop(true);
        setIconImage(null);
        setIconImages(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Título/Desc.:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Tipo:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Observações:");

        txtObs.setColumns(20);
        txtObs.setLineWrap(true);
        txtObs.setRows(5);
        txtObs.setWrapStyleWord(true);
        txtObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtObs);

        jLabel74.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(233, 2, 2));
        jLabel74.setText("*");

        jLabel76.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(233, 2, 2));
        jLabel76.setText("*");

        cboTip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pagamentos", "Recebimentos" }));
        cboTip.setSelectedIndex(-1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTit)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel74))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel76))
                            .addComponent(jLabel6)
                            .addComponent(cboTip, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel76))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 288, 127, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 288, 111, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 330, 350));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtTit.getText().isEmpty() && txtObs.getText().isEmpty()) {
                this.dispose();
            } else {
                int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar? Todos os dados deste plano de contas serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
                if (q == JOptionPane.YES_OPTION) {
                    this.dispose();
                }
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
        if (type.equals("upd")) {
            int q = JOptionPane.showConfirmDialog(this, "Deseja realmente salvar as alterações?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (q == JOptionPane.YES_OPTION) {
                execQuery();
            }
        } else {
            execQuery();
        }
    }//GEN-LAST:event_btnSalActionPerformed

    private void txtObsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObsKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            if (evt.isShiftDown()) {
                txtObs.transferFocusBackward();
            } else {
                txtObs.transferFocus();
            }
        }
    }//GEN-LAST:event_txtObsKeyPressed

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
            java.util.logging.Logger.getLogger(PlanoContasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlanoContasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlanoContasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlanoContasForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PlanoContasForm dialog = new PlanoContasForm(new javax.swing.JFrame());
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
    public javax.swing.JComboBox<String> cboTip;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtObs;
    public javax.swing.JTextField txtTit;
    // End of variables declaration//GEN-END:variables

}
