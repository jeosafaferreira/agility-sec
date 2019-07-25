package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author jeosafaferreira
 */
public class DisciplinaForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String type, sql, sql2, disc_id, cod;
    String[] courses = new String[100];
    String[] classes = new String[100];
    String[] teachers = new String[100];
    String[] books = new String[100];
    boolean modified;

    public DisciplinaForm() {
        initComponents();
        txtCar.setDocument(new AgilitySec.onlyNumbers());
        getData("new");
        type = "new";
        getRootPane().setDefaultButton(btnSalv);
    }

    public DisciplinaForm(java.awt.Frame parent, boolean modal, String cod) {
        super(parent, modal);
        initComponents();
        txtCar.setDocument(new AgilitySec.onlyNumbers());
        getData(cod);
        type = "upd";
        this.setTitle("EDITAR DISCIPLINA");
        getRootPane().setDefaultButton(btnSalv);
    }

    private void getData(String key) {
        con = ModuloConexao.conector();
        getCourses();
        rootPane.setDefaultButton(btnSalv);
        String course_id, book_id;

        if (!key.equals("new")) {
            try {
                pst = con.prepareStatement("SELECT * FROM disciplines WHERE cod='" + key + "'");
                rs = pst.executeQuery();
                rs.next();
                txtCod.setText(rs.getString("cod"));
                txtNom.setText(rs.getString("name"));
                txtCar.setText(rs.getString("workLoad"));
                disc_id = rs.getString("id");
                this.cod = rs.getString("cod");

                course_id = rs.getString("course_id");
                book_id = rs.getString("book_id");

                //CURSO
                pst = con.prepareStatement("SELECT name FROM courses WHERE id=" + course_id);
                rs = pst.executeQuery();
                rs.next();
                cboCur.setSelectedItem(rs.getString("name"));

                //LIVRO
                getBooks();
                cboLiv.setEnabled(true);
                pst = con.prepareStatement("SELECT title FROM books WHERE id=" + book_id);
                rs = pst.executeQuery();
                rs.next();
                cboLiv.setSelectedItem(rs.getString("title"));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0081\nDetalhes do erro:\n" + e);
                btnSalv.setEnabled(false);
            }
        }
    }

    private void getCourses() {
        int i = 0;
        //BUSCA Cursos
        sql = "SELECT id, name from courses";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboCur.addItem(rs.getString("name"));
                courses[i] = (rs.getString("id"));
                i++;
            }
            cboCur.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar cursos.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0082\nDetalhes do erro:\n" + e);
        }
    }

    private void getBooks() {
        sql = "SELECT id, title from books WHERE course_id=" + courses[cboCur.getSelectedIndex()];
        int i = 0;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboLiv.addItem(rs.getString("title"));
                books[i] = rs.getString("id");
                i++;
            }
            cboLiv.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar livros.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0083\nDetalhes do erro:\n" + e);
        }

    }

    private boolean check() {

        boolean r = false;
        if (txtCod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Código\".");
            txtCod.grabFocus();

        } else if (txtNom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Nome\".");
            txtNom.grabFocus();

        } else if (txtCar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Carga Horária\".");
            txtCar.grabFocus();

        } else if (cboCur.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Curso\".");
            cboCur.grabFocus();

        } else if (cboLiv.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Livro\".");
            cboLiv.grabFocus();
        } else {
            r = true;

            //Checa duplicidade do campo código
            if (!txtCod.getText().equals(this.cod)) {
                try {
                    if (AgilitySec.alreadyExists(con, "disciplines", "cod='" + txtCod.getText() + "'")) {
                        JOptionPane.showMessageDialog(this, "Este código já está em uso.\nPor favor, digite outro código para esta disciplina.");
                        r = false;
                    }

                } catch (Exception e) {
                    AgilitySec.showError(this, "#1157", e);
                }
            }

        }
        return r;
    }

    private void execQuery() {

        if (check()) {
            String txtErro1 = null;
            String txtErro2 = null;
            String txtSuc = null;

            if (type.equals("new")) {
                sql = "INSERT INTO disciplines (cod, name, workLoad, book_id, course_id, created_by) VALUES(?,?,?,?,?,?)";
                txtSuc = "Disciplina cadastrada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar esta disciplina.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0084";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar esta disciplina.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0085\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                sql = "UPDATE disciplines SET cod = ?, name = ?, workLoad = ?, book_id = ?, course_id = ?, modified_by=? WHERE id =" + disc_id;
                txtSuc = "Disciplina atualizada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar esta disciplina.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0086";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar esta disciplina.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0087\nDetalhes do erro:\n";
            }
            try {
                pst = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, txtCod.getText());
                pst.setString(2, txtNom.getText());
                pst.setString(3, txtCar.getText());
                pst.setString(4, books[cboLiv.getSelectedIndex()]);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        txtNom = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cboCur = new javax.swing.JComboBox<>();
        cboLiv = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnSalv = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVA DISCIPLINA");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DA DISCIPLINA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Código:");

        jLabel20.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(233, 2, 2));
        jLabel20.setText("*");

        jLabel21.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(233, 2, 2));
        jLabel21.setText("*");

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nome:");

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Carga Horária.:");

        jLabel22.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(233, 2, 2));
        jLabel22.setText("*");

        txtCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCar, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel22)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtCod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel21)
                        .addGap(342, 342, 342))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel22))
                .addGap(5, 5, 5)
                .addComponent(txtCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 553, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setOpaque(false);

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Curso:");

        jLabel17.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(233, 2, 2));
        jLabel17.setText("*");

        cboCur.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboCurPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        cboLiv.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Livro:");

        jLabel23.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(233, 2, 2));
        jLabel23.setText("*");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel23))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel17)))
                        .addGap(0, 472, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cboCur, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboLiv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel11))
                .addGap(6, 6, 6)
                .addComponent(cboLiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 158, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(315, 305, -1, -1));

        btnSalv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSalv.setText("Salvar");
        btnSalv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalv, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 305, 119, -1));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 570, 363));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtCod.getText().isEmpty() && txtNom.getText().isEmpty() && txtCar.getText().isEmpty() && cboCur.getSelectedIndex() == -1 && cboLiv.getSelectedIndex() == -1) {
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

    private void txtCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarActionPerformed

    }//GEN-LAST:event_txtCarActionPerformed

    private void cboCurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCurPopupMenuWillBecomeInvisible
        if (cboCur.getSelectedIndex() != -1) {
            cboLiv.removeAllItems();
            getBooks();
            cboLiv.setEnabled(true);
        }
    }//GEN-LAST:event_cboCurPopupMenuWillBecomeInvisible

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
            java.util.logging.Logger.getLogger(DisciplinaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DisciplinaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DisciplinaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DisciplinaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DisciplinaForm dialog = new DisciplinaForm();
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
    private javax.swing.JComboBox<String> cboLiv;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtCar;
    public javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtNom;
    // End of variables declaration//GEN-END:variables
}
