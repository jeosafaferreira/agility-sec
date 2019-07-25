/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agility.telas;

import Agility.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author jeosafaferreira
 */
public class CursoForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String sql, type, course_cod;
    boolean modified;

    public CursoForm() {
        initComponents();
        con = ModuloConexao.conector();
        type = "new";
        rootPane.setDefaultButton(btnSalv);
        txtCod.grabFocus();
    }

    public CursoForm(String cod) {
        initComponents();
        this.setTitle("EDITAR CURSO");
        con = ModuloConexao.conector();
        course_cod = cod;
        getData();
        rootPane.setDefaultButton(btnSalv);
        type = "upd";
        txtNom.grabFocus();
        txtCod.setEditable(false);
        txtCod.setBackground(new java.awt.Color(204, 204, 204));

    }

    public void ValidaNumero(JTextField text) {
        long valor;
        if (text.getText().length() != 0) {
            try {
                valor = Long.parseLong(text.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Esse Campo só aceita números", "Informação", JOptionPane.INFORMATION_MESSAGE);
                text.grabFocus();
            }
        }
    }

    private void getData() {
        //BUSCA Curso
        sql = "SELECT cod,name,freq,priceBase FROM courses WHERE cod='" + course_cod + "'";
        try {
            rs = con.prepareStatement(sql).executeQuery();
            if (rs.next()) {
                txtCod.setText(rs.getString(1));
                txtNom.setText(rs.getString(2));
                txtFre.setText(rs.getString(3));
                txtPre.setText(rs.getString(4));
            } else {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0076");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0075\nDetalhes do erro:\n" + e);
        }

    }

    /**
     * RETORNA: INT 0: Se falhar no check; INT 1: Se passar no check;
     */
    private int check() {
        double valor;
        int r = 0;
        if (txtCod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Código\".");
            txtCod.grabFocus();

        } else if (txtNom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Nome do Curso\".");
            txtNom.grabFocus();

        } else if (txtFre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Freq. obrigatória (%)\".");
            txtFre.grabFocus();
        } else if (txtPre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Preço base\".");
            txtPre.grabFocus();
        } else {
            try {
                valor = Double.parseDouble(txtFre.getText().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "O campo \"Freq. obrigatória (%)\" aceita apenas números.");
                txtFre.grabFocus();
                r = 2;
            }
            try {
                valor = Double.parseDouble(txtPre.getText().replace(",", "."));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "O campo \"Preço base\" aceita apenas números.");
                txtPre.grabFocus();
                r = 2;
            }
            if (r != 2) {
                r = 1;
            }
        }
        return r;
    }

    private void execQuery() {
        if (check() == 1) {
            String txtErro1 = null;
            String txtErro2 = null;
            String txtSuc = null;

            txtErro1 = "Desculpe, ocorreu um erro ao salvar este curso.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0073";
            txtErro2 = "Desculpe, ocorreu um erro ao salvar este curso.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0074\nDetalhes do erro:\n";

            if (type.equals("new")) {
                sql = "INSERT INTO courses (cod, name, priceBase, freq, created_by) VALUES(?,?,?,?,?)";
                txtSuc = "Curso cadastrado com sucesso!";

            } else if (type.equals("upd")) {
                sql = "UPDATE courses SET cod = ?, name = ?, priceBase = ?, freq = ?, modified_by = ? WHERE cod ='" + course_cod + "'";
                txtSuc = "Curso atualizado com sucesso!";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtCod.getText());
                pst.setString(2, txtNom.getText());
                pst.setString(3, txtPre.getText().replaceAll(",", "."));
                pst.setString(4, txtFre.getText().replaceAll(",", "."));
                pst.setString(5, Login.emp_id);
                if (pst.executeUpdate() == 0) {
                    JOptionPane.showMessageDialog(null, txtErro1);
                } else {
                    this.modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(null, txtSuc);
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
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFre = new javax.swing.JTextField();
        btnCan = new javax.swing.JButton();
        btnSalv = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVO CURSO");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DO CURSO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código:");

        jLabel13.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(233, 2, 2));
        jLabel13.setText("*");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nome:");

        jLabel14.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(233, 2, 2));
        jLabel14.setText("*");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Preço base: (R$)");

        jLabel15.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(233, 2, 2));
        jLabel15.setText("*");

        txtPre.setColumns(8);

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Freq. mínima (%):");

        jLabel16.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(233, 2, 2));
        jLabel16.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel13))
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel14))
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel15))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtPre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                        .addComponent(txtFre, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel13))
                .addGap(4, 4, 4)
                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel14))
                .addGap(7, 7, 7)
                .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 286, -1, -1));

        btnSalv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSalv.setText("Salvar");
        btnSalv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalv, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 286, 119, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 450, 340));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtCod.getText().equals("") || txtNom.getText().equals("") || txtFre.getText().equals("") || txtPre.getText().equals("")) {
                this.dispose();
            } else {
                int q = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
                if (q == JOptionPane.YES_OPTION) {
                    this.dispose();
                }
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnSalvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvActionPerformed
        execQuery();
    }//GEN-LAST:event_btnSalvActionPerformed

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
            java.util.logging.Logger.getLogger(CursoForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CursoForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CursoForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CursoForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CursoForm dialog = new CursoForm();
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
    private javax.swing.JButton btnSalv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtFre;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtPre;
    // End of variables declaration//GEN-END:variables
}
