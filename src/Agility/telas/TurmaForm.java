package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author jeosafaferreira
 */
public class TurmaForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String sql, type, class_id;
    Map<String, String> cRooms = new HashMap<>();
    Map<String, String> courses = new HashMap<>();
    boolean modified;
    String classBeforeUp, swiftBeforeUp;

    public TurmaForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        txtCapacity.setDocument(new AgilitySec.onlyNumbers());
        con = ModuloConexao.conector();
        getRootPane().setDefaultButton(btnSalv);
        getData("new");
        type = "new";
    }

    public TurmaForm(java.awt.Frame parent, boolean modal, String cod) {
        super(parent, modal);
        initComponents();
        txtCapacity.setDocument(new AgilitySec.onlyNumbers());
        this.setTitle("EDITAR TURMA");
        con = ModuloConexao.conector();
        getRootPane().setDefaultButton(btnSalv);
        getData(cod);
        type = "upd";

    }

    private void getData(String key) {
        String course_id = null;
        String cRoom_id = null;

        int i = 0;

        //BUSCA Salas
        sql = "SELECT id, name from classrooms";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboSala.addItem(rs.getString("name"));
                cRooms.put(rs.getString("name"), rs.getString("id"));
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar salas.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0046\nDetalhes do erro:\n" + e);
        }

        i = 0; //RESETO A VARIÁVEL (NÃO DELETAR!)

        //BUSCA Cursos
        sql = "SELECT id, name from courses";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboCurso.addItem(rs.getString("name"));
                courses.put(rs.getString("name"), rs.getString("id"));
                i++;
            }
            cboSala.setSelectedIndex(-1);
            cboCurso.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar cursos.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0047\nDetalhes do erro:\n" + e);
        }

        //UPDATE
        if (!key.equals("new")) {
            //Busca dados atuais
            try {
                pst = con.prepareStatement("SELECT * FROM classes WHERE cod='" + key + "'");
                rs = pst.executeQuery();
                if (rs.next()) {
                    class_id = rs.getString("id");
                    course_id = rs.getString("course_id");
                    cRoom_id = rs.getString("classroom_id");

                    txtCod.setText(rs.getString("cod"));
                    txtNome.setText(rs.getString("name"));
                    txtCapacity.setText(rs.getString("capacity"));
                    cboTurno.setSelectedItem(rs.getString("turno"));

                    swiftBeforeUp = rs.getString("turno");

                    //NOME DO CURSO
                    pst = con.prepareStatement("SELECT name FROM courses WHERE id=" + course_id);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        cboCurso.setSelectedItem(rs.getString("name"));
                    }
                    //SALA
                    pst = con.prepareStatement("SELECT name FROM classrooms WHERE id=" + cRoom_id);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        cboSala.setSelectedItem(rs.getString("name"));
                    }

                    classBeforeUp = rs.getString("name");

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0057\nDetalhes do erro:\n" + e);
            }

        }
    }

    private boolean cRoomOrTurnoChanged() {
        boolean r = true;
        if (cboSala.getSelectedItem().equals(classBeforeUp) && cboTurno.getSelectedItem().equals(swiftBeforeUp)) {
            r = false;
        }
        return r;
    }

    private boolean check() {
        boolean r = false;
        if (txtCod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Código\".");
            txtCod.grabFocus();

        } else if (txtNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Nome da Turma\".");
            txtNome.grabFocus();

        } else if (cboCurso.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Curso\".");
            cboCurso.grabFocus();

        } else if (cboSala.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Sala\".");
            cboSala.grabFocus();

        } else if (cboTurno.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Turno\".");
            cboTurno.grabFocus();
        } else {
            r = true;
            //Verifica se escreveu número no campo capacidade
            try {
                Integer.parseInt(txtCapacity.getText());
            } catch (NumberFormatException e) {
                AgilitySec.checkMessageError(this, txtCapacity, "Limite de Alunos");
                r = false;
            }

            //Verifico se alterou a sala ou turno. Porque se não, vai dizer que a sala não está disponível, sendo que tá sendo usado por ela mesma.
            if (cRoomOrTurnoChanged()) {
                //Verifica disponibilidade da Sala
                try {
                    rs = con.prepareStatement(""
                            + "SELECT "
                            + "    CLA.cod, CLA.name, COU.cod, COU.name"
                            + " FROM"
                            + "    classes CLA"
                            + "        LEFT JOIN"
                            + "    courses COU ON COU.id = CLA.course_id"
                            + " WHERE"
                            + "    classroom_id = " + cRooms.get((String) cboSala.getSelectedItem()) + " AND turno = '" + cboTurno.getSelectedItem() + "'"
                    ).executeQuery();

                    if (rs.next()) {
                        String course = rs.getString("COU.cod") + " - " + rs.getString("COU.name");
                        String classe = rs.getString("CLA.cod") + " - " + rs.getString("CLA.name");
                        String clrDisp = "";
                        //Sala ocupada. Verifico quais que estão disponíveis
                        rs = con.prepareStatement(""
                                + "SELECT "
                                + "    CLR.name"
                                + " FROM"
                                + "    classrooms CLR"
                                + " WHERE"
                                + "    CLR.id NOT IN (SELECT "
                                + "            CLA.classroom_id"
                                + "        FROM"
                                + "            classes CLA"
                                + "        WHERE"
                                + "            turno = 'Manhã')"
                        ).executeQuery();
                        while (rs.next()) {
                            clrDisp += rs.getString("CLR.name") + "\n";
                        }
                        JOptionPane.showMessageDialog(this, "Esta sala não está disponível no turno selecionado.\n\n"
                                + "Está sendo usada por:\n"
                                + "<html><b>Curso: </b>" + course + "\n"
                                + "<html><b>Turma: </b>" + classe + "\n\n"
                                + "<html><b>Salas disponíveis no turno selecionado: \n"
                                + clrDisp
                        );
                        r = false;

                    }
                } catch (Exception e) {
                    AgilitySec.showError(this, "#1156", e);
                    r = false;
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
                sql = "INSERT INTO classes (cod, name, turno, course_id, classroom_id, capacity, created_by) VALUES(?,?,?,?,?,?,?)";
                txtSuc = "Turma cadastrada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar esta turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0044";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar esta turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0045\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                sql = "UPDATE classes SET cod = ?, name = ?, turno = ?, course_id = ?, classroom_id = ?, capacity=?, modified_by=? WHERE id =" + class_id;
                txtSuc = "Turma atualizada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar esta turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0058";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar esta turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0059\nDetalhes do erro:\n";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtCod.getText());
                pst.setString(2, txtNome.getText());
                pst.setString(3, cboTurno.getSelectedItem().toString());
                pst.setString(4, courses.get(cboCurso.getSelectedItem()));
                pst.setString(5, cRooms.get(cboSala.getSelectedItem()));
                pst.setString(6, txtCapacity.getText());
                pst.setString(7, Login.emp_id);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cboTurno = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboSala = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboCurso = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtCapacity = new javax.swing.JTextField();
        btnCan = new javax.swing.JButton();
        btnSalv = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVA TURMA");
        setAlwaysOnTop(true);
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DA TURMA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Nome da Turma:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Turno:");

        cboTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manhã", "Tarde", "Noite" }));
        cboTurno.setSelectedIndex(-1);
        cboTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTurnoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Sala:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Curso:");

        jLabel13.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(233, 2, 2));
        jLabel13.setText("*");

        jLabel14.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(233, 2, 2));
        jLabel14.setText("*");

        jLabel15.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(233, 2, 2));
        jLabel15.setText("*");

        jLabel16.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(233, 2, 2));
        jLabel16.setText("*");

        jLabel17.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(233, 2, 2));
        jLabel17.setText("*");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Limite de Alunos:");

        jLabel18.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(233, 2, 2));
        jLabel18.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboCurso, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel17))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCod)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel13))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel15))
                                    .addComponent(cboTurno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel18))
                                    .addComponent(txtCapacity))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel16))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel14))
                                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboSala, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, -1, -1));

        btnSalv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSalv.setText("Salvar");
        btnSalv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalv, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 119, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 480, 350));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cboTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTurnoActionPerformed

    }//GEN-LAST:event_cboTurnoActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtCod.getText().isEmpty() && txtNome.getText().isEmpty() && cboCurso.getSelectedIndex() == -1 && cboSala.getSelectedIndex() == -1 && cboTurno.getSelectedIndex() == -1) {
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

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

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
            java.util.logging.Logger.getLogger(TurmaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TurmaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TurmaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TurmaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TurmaForm dialog = new TurmaForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cboCurso;
    private javax.swing.JComboBox<String> cboSala;
    private javax.swing.JComboBox<String> cboTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtCapacity;
    public javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
