package Agility.telas;

import Agility.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author jeosafaferreira
 */
public class LivroForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String[] courses;
    String book_id, type;
    int new_id;
    boolean modified;

    public LivroForm() {
        initComponents();
        listData("new");
        type = "new";
        txtTit.grabFocus();
        getRootPane().setDefaultButton(btnSalv);
    }

    public LivroForm(java.awt.Frame parent, boolean modal, String cod) {
        super(parent, modal);
        initComponents();
        listData(cod);
        type = "upd";
        getRootPane().setDefaultButton(btnSalv);
    }

    private void listData(String key) {
        lblCur.setVisible(false);
        con = ModuloConexao.conector();
        try {

            rs = con.prepareStatement("SELECT MAX(id) as id FROM books").executeQuery();
            rs.next();
            new_id = rs.getInt("id") + 1;
            txtCod.setText(Integer.toString(new_id));

            rs = con.prepareStatement("SELECT id, name FROM courses").executeQuery();
            rs.last();
            courses = new String[rs.getRow()];
            rs.absolute(0);
            int i = 0;
            while (rs.next()) {
                courses[i] = rs.getString("id");
                cboCur.addItem(rs.getString("name"));
                i++;
            }
            cboCur.setSelectedIndex(-1);

            if (!key.equals("new")) {
                rs = con.prepareStatement("SELECT * FROM books WHERE id='" + key + "'").executeQuery();
                rs.next();
                book_id = rs.getString("id");
                txtTit.setText(rs.getString("title"));
                txtCod.setText(rs.getString("id"));
                txtEdi.setText(rs.getString("publisher"));
                txtAut.setText(rs.getString("author"));
                txtIsb.setText(rs.getString("isbn"));

                rs = con.prepareStatement("SELECT * FROM courses WHERE id=" + rs.getString("course_id")).executeQuery();
                rs.next();
                cboCur.setSelectedItem(rs.getString("name"));

                //VERIFICO SE ESTÁ SENDO USADO. SE SIM, NÃO DEIXO ALTERAR O CURSO.
                rs = con.prepareStatement("SELECT name FROM disciplines WHERE book_id=" + book_id).executeQuery();
                if (rs.next()) {
                    cboCur.setEnabled(false);
                    lblCur.setVisible(true);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao consultar banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0088\nDetalhes do erro:\n" + e);
        }

    }

    /**
     * RETORNA: INT 0: Se falhar; INT 1: Se passar;
     */
    private int check() {
        int r = 0;
        if (txtCod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Código\".");
            txtCod.grabFocus();
        } else if (txtTit.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Título\".");
            txtTit.grabFocus();
        } else if (txtAut.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Autor(es)\".");
            txtAut.grabFocus();
        } else if (txtEdi.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Editora\".");
            txtEdi.grabFocus();
        } else if (cboCur.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Curso\".");
            cboCur.grabFocus();
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
            String sql = null;

            if (type.equals("new")) {
                sql = "INSERT INTO books (title, author, publisher, isbn, course_id, created_by) VALUES(?,?,?,?,?,?)";
                txtSuc = "Livro cadastrado com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar este livro.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0089";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar este livro.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0090\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn = ?, course_id = ?, modified_by=? WHERE id =" + book_id;
                txtSuc = "Os dados do livro foram atualizados com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar este livro.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0091";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar este livro.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0092\nDetalhes do erro:\n";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtTit.getText());
                pst.setString(2, txtAut.getText());
                pst.setString(3, txtEdi.getText());
                pst.setString(4, txtIsb.getText());
                pst.setString(5, courses[cboCur.getSelectedIndex()]);
                pst.setString(6, Login.emp_id);
                if (pst.executeUpdate() == 0) {
                    JOptionPane.showMessageDialog(this, txtErro1);
                } else {
                    this.modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, txtSuc);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, txtErro2 + e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        txtAut = new javax.swing.JTextField();
        txtEdi = new javax.swing.JTextField();
        txtIsb = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cboCur = new javax.swing.JComboBox<>();
        lblCur = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnSalv = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVO LIVRO");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Título:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Autor(es):");

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Editora:");

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("ISBN:");

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        txtCod.setEditable(false);
        txtCod.setBackground(new java.awt.Color(233, 233, 233));

        jLabel18.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(233, 2, 2));
        jLabel18.setText("*");

        jLabel19.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(233, 2, 2));
        jLabel19.setText("*");

        jLabel20.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(233, 2, 2));
        jLabel20.setText("*");

        jLabel21.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(233, 2, 2));
        jLabel21.setText("*");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEdi, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel7)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel21)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtIsb, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
                    .addComponent(txtAut)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel20)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel18))
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtTit))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIsb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setOpaque(false);

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Curso:");

        jLabel17.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(233, 2, 2));
        jLabel17.setText("*");

        lblCur.setForeground(new java.awt.Color(255, 0, 51));
        lblCur.setText("Não é possível alterar o curso porque este livro já está sendo utilizado.");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cboCur, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel17))
                            .addComponent(lblCur))
                        .addGap(0, 157, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCur)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 216, 630, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 322, -1, -1));

        btnSalv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSalv.setText("Salvar");
        btnSalv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalv, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 322, 119, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 660, 390));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtTit.getText().isEmpty() && txtAut.getText().isEmpty() && txtEdi.getText().isEmpty() && txtIsb.getText().isEmpty() && cboCur.getSelectedIndex() == -1) {
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
            java.util.logging.Logger.getLogger(LivroForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LivroForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LivroForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LivroForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LivroForm dialog = new LivroForm();
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
    private javax.swing.JLabel background;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnSalv;
    private javax.swing.JComboBox<String> cboCur;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblCur;
    private javax.swing.JTextField txtAut;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtEdi;
    private javax.swing.JTextField txtIsb;
    private javax.swing.JTextField txtTit;
    // End of variables declaration//GEN-END:variables
}
