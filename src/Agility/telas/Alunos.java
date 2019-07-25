package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jferreira
 */
public class Alunos extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel dtm;

    public Alunos(Frame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblAlunos.getModel();
        listData();
    }

    public void listData() {
        dtm.setNumRows(0);
        try {
            rs = con.prepareStatement("SELECT \n"
                    + "    STU.cod, STU.name, STU.status, COU.name, CLA.name, CLA.turno\n"
                    + "FROM\n"
                    + "    students STU\n"
                    + "        LEFT JOIN\n"
                    + "    courses COU ON COU.id = STU.course_id\n"
                    + "        LEFT JOIN\n"
                    + "    classes CLA ON CLA.id = STU.class_id\n"
                    + "ORDER BY STU.name ASC").executeQuery();
            while (rs.next()) {
                dtm.addRow(new String[]{
                    rs.getString("STU.status").equals("0")? "<html><strike>"+rs.getString("STU.cod"):rs.getString("STU.cod"),
                    rs.getString("STU.status").equals("0") ? "<html><strike>" + rs.getString("STU.name") : rs.getString("STU.name"),
                    rs.getString("COU.name"),
                    rs.getString("CLA.name"),
                    rs.getString("CLA.turno")
                });
            }

            //FILTRO
            rs = con.prepareStatement("SELECT name FROM courses").executeQuery();
            while (rs.next()) {
                cboCur.addItem(rs.getString("name"));
            }
            cboCur.addItem("<html><b>MOSTRAR TUDO");
            cboCur.setSelectedItem("<html><b>MOSTRAR TUDO");

            cboTur.addItem("Manhã");
            cboTur.addItem("Tarde");
            cboTur.addItem("Noite");
            cboTur.addItem("<html><b>MOSTRAR TUDO");
            cboTur.setSelectedItem("<html><b>MOSTRAR TUDO");

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1062", ex);
        }
    }

    private void filter() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtm);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(4);
        RowFilter<Object, Object> rf;

        tblAlunos.setRowSorter(tr);

        if (txtMat.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtMat.getText(), 0));
        }

        if (txtNome.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 1));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtNome.getText(), 1));
        }

        if (cboCur.getSelectedItem() == "<html><b>MOSTRAR TUDO") {
            filters.add(RowFilter.regexFilter("", 2));
        } else {
            filters.add(RowFilter.regexFilter(String.format("^%s$", Pattern.quote((String) cboCur.getSelectedItem())), 2));
        }

        if (cboTur.getSelectedItem() == "<html><b>MOSTRAR TUDO") {
            filters.add(RowFilter.regexFilter("", 4));
        } else {
            filters.add(RowFilter.regexFilter(String.format("^%s$", Pattern.quote((String) cboTur.getSelectedItem())), 4));
        }

        rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);

        if (tblAlunos.getRowCount() > 0) {
            tblAlunos.addRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMat = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cboCur = new javax.swing.JComboBox<>();
        btnLim = new javax.swing.JButton();
        cboTur = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDet = new javax.swing.JButton();
        Background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ALUNOS - Agility®");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Matrícula:");

        txtMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMatKeyReleased(evt);
            }
        });

        txtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nome:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Curso:");

        cboCur.setPreferredSize(new java.awt.Dimension(136, 25));
        cboCur.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboCurPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        btnLim.setText("Limpar");
        btnLim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimActionPerformed(evt);
            }
        });

        cboTur.setPreferredSize(new java.awt.Dimension(136, 25));
        cboTur.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Turno:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboTur, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLim, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addGap(157, 157, 157))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(32, 32, 32))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLim)
                            .addComponent(cboTur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 1040, -1));

        tblAlunos.setAutoCreateRowSorter(true);
        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nome Completo", "Curso", "Turma", "Turno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlunos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        tblAlunos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAlunosMouseClicked(evt);
            }
        });
        tblAlunos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblAlunosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblAlunos);
        if (tblAlunos.getColumnModel().getColumnCount() > 0) {
            tblAlunos.getColumnModel().getColumn(0).setPreferredWidth(77);
            tblAlunos.getColumnModel().getColumn(1).setPreferredWidth(396);
            tblAlunos.getColumnModel().getColumn(2).setPreferredWidth(169);
            tblAlunos.getColumnModel().getColumn(3).setPreferredWidth(135);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 90, 1043, 236));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Cadastrar");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 180, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 181, -1));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 330, -1, -1));

        Background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1055, 390));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMatKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDetActionPerformed(null);
        } else {
            filter();
        }
    }//GEN-LAST:event_txtMatKeyReleased

    private void txtNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnDetActionPerformed(null);
        } else {
            filter();
        }
    }//GEN-LAST:event_txtNomeKeyReleased

    private void cboCurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCurPopupMenuWillBecomeInvisible
        filter();
    }//GEN-LAST:event_cboCurPopupMenuWillBecomeInvisible

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        txtMat.setText("");
        txtNome.setText("");
        cboCur.setSelectedIndex(0);
        cboTur.setSelectedIndex(0);
        txtNome.grabFocus();

        filter();
    }//GEN-LAST:event_btnLimActionPerformed

    private void tblAlunosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAlunosKeyReleased

    }//GEN-LAST:event_tblAlunosKeyReleased

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        WizardNovoAluno wna = new WizardNovoAluno(new Home());
        wna.setVisible(true);
        if (wna.modified) {
            listData();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tblAlunos.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Antes de clicar neste botão, selecione um aluno na tabela.");
        } else {
            AlunosForm af = new AlunosForm((String) tblAlunos.getValueAt(tblAlunos.getSelectedRow(), 0));
            af.setVisible(true);
            if (af.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblAlunos.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um aluno na tabela.");
            txtMat.grabFocus();
        } else {
            AlunosDetalhes adn = new AlunosDetalhes(this, (tblAlunos.getValueAt(tblAlunos.getSelectedRow(), 0).toString()));
            adn.setVisible(true);
            if (adn.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void cboTurPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurPopupMenuWillBecomeInvisible
        filter();
    }//GEN-LAST:event_cboTurPopupMenuWillBecomeInvisible

    private void tblAlunosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAlunosMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblAlunosMouseClicked

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
            java.util.logging.Logger.getLogger(Alunos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Alunos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Alunos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Alunos.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                Alunos dialog = new Alunos(new JFrame());
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
    private javax.swing.JLabel Background;
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox<String> cboCur;
    private javax.swing.JComboBox<String> cboTur;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAlunos;
    private javax.swing.JTextField txtMat;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
