package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jferreira
 */
public class NotaInserir extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String[] codStudentsInThisClass;
    String course_id, class_id, discipline_id, bim, type;
    DefaultTableModel dtm;

    public NotaInserir(JDialog parent) {
        super(parent);
        initComponents();
        AgilitySec.showError(this, "#0003");
    }

    public NotaInserir(JDialog parent, String course_id, String class_id, String discipline_id, String bim, String type) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblAlunos.getModel();
        this.course_id = course_id;
        this.class_id = class_id;
        this.discipline_id = discipline_id;
        this.bim = bim;
        this.type = type;

        listData();
    }

    private void listData() {
        //SETA TEXTO DA lblPeriod
        switch (bim) {
            case "bim1":
                lblPer.setText("1º Bimestre");
                break;
            case "bim2":
                lblPer.setText("2º Bimestre");
                break;
            case "bim3":
                lblPer.setText("3º Bimestre");
                break;
            case "bim4":
                lblPer.setText("4º Bimestre");
                break;
            case "rec":
                lblPer.setText("Recuperação");
                break;
        }

        try {
            //BUSCANDO CURSO
            rs = con.prepareStatement("SELECT name FROM courses WHERE id='" + course_id + "'").executeQuery();
            rs.next();
            lblCur.setText(rs.getString("name"));

            //BUSCANDO TURMA
            rs = con.prepareStatement("SELECT cod, name, turno FROM classes WHERE id='" + class_id + "'").executeQuery();
            rs.next();
            lblTurma.setText(rs.getString("cod") + " - " + rs.getString("name"));
            lblTurno.setText(rs.getString("turno"));

            //BUSCANDO DISCIPLINA
            rs = con.prepareStatement("SELECT name FROM disciplines WHERE id='" + discipline_id + "'").executeQuery();
            rs.next();
            lblDisc.setText(rs.getString("name"));

            //BUSCANDO ALUNOS
            rs = con.prepareStatement("SELECT id, cod, name FROM students WHERE course_id='" + course_id + "' AND class_id='" + class_id + "'").executeQuery();
            rs.last();
            codStudentsInThisClass = new String[rs.getRow()];
            rs.absolute(0);
            int i = 0;
            while (rs.next()) {
                codStudentsInThisClass[i] = rs.getString("cod");

                dtm.addRow(new String[]{
                    rs.getString("cod"),
                    rs.getString("name"),
                    "",
                    "",
                    "",
                    ""});
                i++;
            }
            //BUSCANDO NOTAS JÁ CADASTRADAS
            if (type.equals("upd")) {
                try {

                    int k = 0;
                    for (String cod : codStudentsInThisClass) {
                        rs = con.prepareStatement("SELECT " + bim + "_n1, " + bim + "_n2, misses_" + bim + " FROM boletins WHERE "
                                + "academic_year='" + Login.academic_year + "' "
                                + "AND course_id='" + course_id + "' "
                                + "AND class_id='" + class_id + "' "
                                + "AND discipline_id='" + discipline_id + "' "
                                + "AND student_cod='" + cod + "'").executeQuery();
                        rs.next();
                        dtm.setValueAt(rs.getString(bim + "_n1"), k, 2);
                        dtm.setValueAt(rs.getString(bim + "_n2"), k, 3);
                        dtm.setValueAt(rs.getString("misses_" + bim), k, 4);

                        k++;
                    }
                } catch (SQLException e) {
                    this.dispose();
                    AgilitySec.showError(this, "#0010", e);
                }

            }

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#0004", ex);
        }

    }

    private boolean check() {
        boolean r = true;
        int rows = tblAlunos.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String n1 = dtm.getValueAt(i, 2).toString().replace(",", ".");
            String n2 = dtm.getValueAt(i, 3).toString().replace(",", ".");
            String misses = dtm.getValueAt(i, 4).toString();

            if (!n1.equals("")) {
                if (Double.parseDouble(n1) < 0 || Double.parseDouble(n1) > 10) { //Se a nota for maior que dez ou menor que zero dá erro.
                    r = false;
                }
            } else {
                r = false; //Se tiver campo em branco, dá erro
            }

            if (!n2.equals("")) {
                if (Double.parseDouble(n2) < 0 || Double.parseDouble(n2) > 10) { //Se a nota for maior que dez ou menor que zero dá erro.
                    r = false;
                }
            } else {
                r = false; //Se tiver campo em branco, dá erro
            }
            if (!misses.equals("")) {
                try {
                    Integer.parseInt(misses); //Int não aceita número quebrado.
                } catch (Exception e) {
                    r = false;
                }

            } else {
                r = false; //Se tiver campo em branco, dá erro
            }
        }
        if (!r) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar.\n Verifique as etapas abaixo e tente novamente:\n"
                    + "1.Insira todas as notas e faltas de todos os alunos desta turma.\n"
                    + "2.Verifique se os dados inseridos estão corretos.", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
        }
        return r;
    }

    private void save() {
        int rows = tblAlunos.getRowCount();
        String sql;

        try {
            if (AgilitySec.alreadyExists(con, "boletins",
                    "academic_year='" + Login.academic_year + "' "
                    + "AND course_id='" + course_id + "' "
                    + "AND class_id='" + class_id + "' "
                    + "AND discipline_id='" + discipline_id + "' "
                    + "AND bim1_n1 IS NOT NULL")) {

                //APENAS UPDATE!
                sql = "UPDATE boletins SET " + bim + "_n1=?, " + bim + "_n2=?, misses_" + bim + "=?, modified_by=? WHERE student_cod=? AND course_id=? AND class_id=? AND discipline_id=? AND academic_year=?";
                pst = con.prepareStatement(sql);
                for (int i = 0; i < rows; i++) {
                    pst.setString(1, tblAlunos.getValueAt(i, 2).toString().replace(",", "."));
                    pst.setString(2, tblAlunos.getValueAt(i, 3).toString().replace(",", "."));
                    pst.setString(3, tblAlunos.getValueAt(i, 4).toString());
                    pst.setString(4, Login.emp_id);
                    pst.setString(5, tblAlunos.getValueAt(i, 0).toString());
                    pst.setString(6, course_id);
                    pst.setString(7, class_id);
                    pst.setString(8, discipline_id);
                    pst.setString(9, Login.academic_year);
                    pst.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!", "", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

            } else {
                //CRIA NOVA LINHA
                sql = "INSERT INTO boletins SET student_cod=?, course_id=?, class_id=?, discipline_id=?, bim1_n1=?, bim1_n2=?, misses_" + bim + "=?, created_by=?, academic_year=?";
                pst = con.prepareStatement(sql);
                for (int i = 0; i < rows; i++) {
                    pst.setString(1, tblAlunos.getValueAt(i, 0).toString());
                    pst.setString(2, course_id);
                    pst.setString(3, class_id);
                    pst.setString(4, discipline_id);
                    pst.setString(5, tblAlunos.getValueAt(i, 2).toString().replace(",", "."));
                    pst.setString(6, tblAlunos.getValueAt(i, 3).toString().replace(",", "."));
                    pst.setString(7, tblAlunos.getValueAt(i, 4).toString());
                    pst.setString(8, Login.emp_id);
                    pst.setString(9, Login.academic_year);
                    pst.executeUpdate();

                }
                JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!", "", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } catch (SQLException ex) {
            this.dispose();
            AgilitySec.showError(this, "#0009", ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblCur = new javax.swing.JLabel();
        lblTurno = new javax.swing.JLabel();
        lblPer = new javax.swing.JLabel();
        lblTurma = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblDisc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();
        btnCanc = new javax.swing.JButton();
        btnSalv = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRO DE NOTAS E FALTAS");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setBorder(null);

        jLabel1.setFont(new java.awt.Font("Lucida Sans", 1, 21)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cadastrar Notas e Faltas");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Curso:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Turma:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Turno:");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Período:");

        lblCur.setText("lblCur");

        lblTurno.setText("lblTurno");

        lblPer.setText("lblPer");

        lblTurma.setText("lblTurNome");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Disciplina:");

        lblDisc.setText("lblDisc");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPer))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(79, 79, 79)
                                        .addComponent(lblCur))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblDisc)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTurno)
                                    .addComponent(lblTurma)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(jLabel1)))
                .addGap(0, 262, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblCur)
                    .addComponent(jLabel3)
                    .addComponent(lblTurma))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblTurno)
                    .addComponent(jLabel6)
                    .addComponent(lblDisc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblPer))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        tblAlunos.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nome do Aluno", "Nota 1", "Nota 2", "Faltas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlunos.setRowHeight(20);
        tblAlunos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        tblAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAlunosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAlunos);
        if (tblAlunos.getColumnModel().getColumnCount() > 0) {
            tblAlunos.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblAlunos.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblAlunos.getColumnModel().getColumn(2).setPreferredWidth(30);
            tblAlunos.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblAlunos.getColumnModel().getColumn(4).setPreferredWidth(30);
        }

        btnCanc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCanc.setText("Cancelar");
        btnCanc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancActionPerformed(evt);
            }
        });

        btnSalv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSalv.setText("Salvar Dados");
        btnSalv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCanc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnSalv)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCanc)
                    .addComponent(btnSalv))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Todas as alterações serão perdidas.\nDeseja realmente cancelar?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCancActionPerformed

    private void btnSalvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvActionPerformed
        if (check()) {
            save();
        }
    }//GEN-LAST:event_btnSalvActionPerformed

    private void tblAlunosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAlunosMouseClicked
    }//GEN-LAST:event_tblAlunosMouseClicked

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
            java.util.logging.Logger.getLogger(NotaInserir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotaInserir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotaInserir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotaInserir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NotaInserir dialog = new NotaInserir(new JDialog());
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
    private javax.swing.JButton btnCanc;
    private javax.swing.JButton btnSalv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCur;
    private javax.swing.JLabel lblDisc;
    private javax.swing.JLabel lblPer;
    private javax.swing.JLabel lblTurma;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JTable tblAlunos;
    // End of variables declaration//GEN-END:variables
}
