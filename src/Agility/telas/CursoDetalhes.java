package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jferreira
 */
public class CursoDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String cod, sql = null;
    Home home;
    boolean modified;
    DefaultTableModel dtmCla, dtmMat, dtmBooks;

    public CursoDetalhes(String cod) {
        initComponents();
        this.cod = cod;
        con = ModuloConexao.conector();

        dtmCla = (DefaultTableModel) tblTurmas.getModel();
        dtmMat = (DefaultTableModel) tblMatriz.getModel();
        dtmBooks = (DefaultTableModel) tblLivros.getModel();
        listData();

    }

    private void listData() {
        dtmBooks.setNumRows(0);
        dtmCla.setNumRows(0);
        dtmMat.setNumRows(0);

        int course_id = 0;
        int class_id = 0;
        int disc_id = 0;

        //DADOS DO CURSO
        sql = "SELECT * FROM courses WHERE cod='" + this.cod + "'";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                course_id = rs.getInt("id");
                txtCod.setText(rs.getString("cod"));
                txtNome.setText(rs.getString("name"));
                txtMen.setText("R$ " + rs.getString("priceBase").replace(".", ","));
                sldFreq.setValue(rs.getInt("freq"));
            } else {
                JOptionPane.showMessageDialog(null, "Desculpe, não foi possível obter dados deste curso.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0029");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0028\nDetalhes do erro:\n" + e);
        }

        //TURMAS RELACIONADAS
        sql = "SELECT * FROM classes C LEFT JOIN classrooms CR ON C.classroom_id=CR.id WHERE course_id='" + course_id + "'";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                dtmCla.addRow(new String[]{
                    rs.getString("C.cod"),
                    rs.getString("C.name"),
                    rs.getString("C.turno"),
                    rs.getString("CR.name")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0031\nDetalhes do erro:\n" + e);
        }

        //MATRIZ CURRICULAR
        sql = "SELECT cod, name, workLoad FROM disciplines WHERE course_id=" + course_id;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                dtmMat.addRow(new String[]{
                    rs.getString("cod"),
                    rs.getString("name"),
                    rs.getString("workLoad")

                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0033\nDetalhes do erro:\n" + e);
        }

        //LIVROS
        sql = "SELECT * FROM books WHERE course_id=" + course_id;
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                while (rs.next()) {
                    dtmBooks.addRow(new String[]{
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author")
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0035\nDetalhes do erro:\n" + e);
        }
    }

    private void delCurso() {
        int qtd = tblTurmas.getModel().getRowCount();
        if (qtd > 0) {
            JOptionPane.showMessageDialog(null, "<html>Há <b>" + qtd + "</b> turmas neste curso. Não é possível excluí-lo do sistema.", "Atenção!", JOptionPane.ERROR_MESSAGE);

        } else {
            try {
                pst = con.prepareStatement("UPDATE courses SET status=0, modified_by='" + Login.emp_id + "' WHERE cod='" + this.cod+"'");
                if (pst.executeUpdate() > 0) {
                    this.modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Curso deletado com sucesso.");
                }
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1065", ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDesktop = new javax.swing.JTabbedPane();
        pnlTurmas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTurmas = new javax.swing.JTable();
        pnlMatriz = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMatriz = new javax.swing.JTable();
        pnlLivros = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblLivros = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        sldFreq = new javax.swing.JSlider();
        btnEdit = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes do Curso");
        setModal(true);
        setResizable(false);

        pnlDesktop.setBorder(null);

        pnlTurmas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblTurmas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Turma", "Turno", "Sala"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, true
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
        tblTurmas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblTurmasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblTurmas);

        javax.swing.GroupLayout pnlTurmasLayout = new javax.swing.GroupLayout(pnlTurmas);
        pnlTurmas.setLayout(pnlTurmasLayout);
        pnlTurmasLayout.setHorizontalGroup(
            pnlTurmasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );
        pnlTurmasLayout.setVerticalGroup(
            pnlTurmasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        pnlDesktop.addTab("Turmas", pnlTurmas);

        pnlMatriz.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblMatriz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Disciplina", "Carga Horária"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMatriz.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblMatriz.getTableHeader().setReorderingAllowed(false);
        tblMatriz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblMatrizKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblMatriz);

        javax.swing.GroupLayout pnlMatrizLayout = new javax.swing.GroupLayout(pnlMatriz);
        pnlMatriz.setLayout(pnlMatrizLayout);
        pnlMatrizLayout.setHorizontalGroup(
            pnlMatrizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );
        pnlMatrizLayout.setVerticalGroup(
            pnlMatrizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        pnlDesktop.addTab("Matriz Curricular", pnlMatriz);

        pnlLivros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblLivros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título", "Autor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLivros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblLivros.getTableHeader().setReorderingAllowed(false);
        tblLivros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblLivrosKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblLivros);

        javax.swing.GroupLayout pnlLivrosLayout = new javax.swing.GroupLayout(pnlLivros);
        pnlLivros.setLayout(pnlLivrosLayout);
        pnlLivrosLayout.setHorizontalGroup(
            pnlLivrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );
        pnlLivrosLayout.setVerticalGroup(
            pnlLivrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );

        pnlDesktop.addTab("Livros", pnlLivros);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código:");

        txtCod.setEditable(false);

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Mensalidade Base:");

        txtMen.setEditable(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nome:");

        txtNome.setEditable(false);
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Porcentagem de frequência mínima:");

        sldFreq.setMajorTickSpacing(10);
        sldFreq.setMinorTickSpacing(5);
        sldFreq.setPaintLabels(true);
        sldFreq.setPaintTicks(true);
        sldFreq.setSnapToTicks(true);
        sldFreq.setValue(75);
        sldFreq.setBorder(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtCod, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(txtMen))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldFreq, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldFreq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar Curso");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDel.setText("Deletar Curso");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(pnlDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(pnlDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExit)))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblTurmasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTurmasKeyReleased
        System.out.println(tblTurmas.getColumnModel().getColumn(0).getWidth());
        System.out.println(tblTurmas.getColumnModel().getColumn(1).getWidth());
        System.out.println(tblTurmas.getColumnModel().getColumn(2).getWidth());
        System.out.println(tblTurmas.getColumnModel().getColumn(3).getWidth());
    }//GEN-LAST:event_tblTurmasKeyReleased

    private void tblMatrizKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblMatrizKeyReleased
        System.out.println(tblMatriz.getColumnModel().getColumn(0).getWidth());
        System.out.println(tblMatriz.getColumnModel().getColumn(1).getWidth());
        System.out.println(tblMatriz.getColumnModel().getColumn(2).getWidth());
    }//GEN-LAST:event_tblMatrizKeyReleased

    private void tblLivrosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLivrosKeyReleased
        System.out.println(tblLivros.getColumnModel().getColumn(0).getWidth());
        System.out.println(tblLivros.getColumnModel().getColumn(1).getWidth());
        System.out.println(tblLivros.getColumnModel().getColumn(2).getWidth());
    }//GEN-LAST:event_tblLivrosKeyReleased

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        CursoForm cursoF = new CursoForm(txtCod.getText());
        cursoF.setVisible(true);
        if (cursoF.modified) {
            listData();
            this.modified = true;
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        delCurso();
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

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
            java.util.logging.Logger.getLogger(CursoDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CursoDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CursoDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CursoDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CursoDetalhes dialog = new CursoDetalhes("");
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
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane pnlDesktop;
    private javax.swing.JPanel pnlLivros;
    private javax.swing.JPanel pnlMatriz;
    private javax.swing.JPanel pnlTurmas;
    private javax.swing.JSlider sldFreq;
    private javax.swing.JTable tblLivros;
    private javax.swing.JTable tblMatriz;
    private javax.swing.JTable tblTurmas;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtMen;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
