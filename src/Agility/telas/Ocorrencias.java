package Agility.telas;

import Agility.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jferreira
 */
public class Ocorrencias extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home;
    DefaultTableModel dtm;

    public Ocorrencias(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblOco.getModel();
        listData();
    }

    public void listData() {
        dtm.setNumRows(0);
        try {
            rs = con.prepareStatement("SELECT STU.name,STU.status, OCU.* FROM occurrences OCU LEFT JOIN students STU ON OCU.student_cod = STU.cod").executeQuery();
            while (rs.next()) {
                dtm.addRow(new String[]{
                    rs.getString("OCU.id"),
                    rs.getString("OCU.occurrence"),
                    rs.getString("STU.status").equals("0")? "<html><strike>"+rs.getString("STU.name"):rs.getString("STU.name"),
                    rs.getString("OCU.date")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar ocorrências.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0083\nDetalhes do erro:\n" + e);
            this.dispose();
        }
    }

    private void filter() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtm);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(4);
        RowFilter<Object, Object> rf;

        tblOco.setRowSorter(tr);

        if (txtCod.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));
        }

        if (txtOco.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 1));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtOco.getText(), 1));
        }
        if (txtAlu.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 2));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtAlu.getText(), 2));
        }
        rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);
        if (tblOco.getRowCount() > 0) {
            tblOco.setRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCod = new javax.swing.JTextField();
        btnLimp = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtOco = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAlu = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOco = new javax.swing.JTable();
        btnDet = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OCORRÊNCIAS - Agility®");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });
        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
        });

        btnLimp.setText("Limpar");
        btnLimp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ocorrência:");

        txtOco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOcoActionPerformed(evt);
            }
        });
        txtOco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOcoKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Aluno:");

        txtAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluActionPerformed(evt);
            }
        });
        txtAlu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 5, Short.MAX_VALUE)
                        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtOco, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimp, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimp)
                    .addComponent(txtOco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAlu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 760, -1));

        tblOco.setAutoCreateRowSorter(true);
        tblOco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Ocorrência", "Aluno", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOco.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblOco.getTableHeader().setReorderingAllowed(false);
        tblOco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOcoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOco);
        if (tblOco.getColumnModel().getColumnCount() > 0) {
            tblOco.getColumnModel().getColumn(0).setPreferredWidth(93);
            tblOco.getColumnModel().getColumn(1).setPreferredWidth(267);
            tblOco.getColumnModel().getColumn(2).setPreferredWidth(278);
            tblOco.getColumnModel().getColumn(3).setPreferredWidth(119);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 763, 152));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 250, 200, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar Ocorrência");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 250, -1, -1));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Nova Ocorrência");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 173, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 790, 310));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filter();

    }//GEN-LAST:event_txtCodKeyReleased

    private void btnLimpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpActionPerformed
        txtCod.setText("");
        txtOco.setText("");
        txtAlu.setText("");
        filter();
    }//GEN-LAST:event_btnLimpActionPerformed

    private void txtOcoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOcoKeyReleased

        filter();

    }//GEN-LAST:event_txtOcoKeyReleased

    private void txtAluKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluKeyReleased
        filter();
    }//GEN-LAST:event_txtAluKeyReleased

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblOco.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um ocorrência na tabela.");
            txtCod.grabFocus();
        } else {
            OcorrenciaDetalhes ocoD = new OcorrenciaDetalhes(this, (String) tblOco.getValueAt(tblOco.getSelectedRow(), 0));
            ocoD.setVisible(true);
            if (ocoD.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tblOco.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Antes de clicar neste botão, selecione uma ocorrência na tabela.");
            txtCod.grabFocus();
        } else {
            OcorrenciaForm ocoF = new OcorrenciaForm(home, (String) tblOco.getValueAt(tblOco.getSelectedRow(), 0));
            ocoF.setVisible(true);
            if (ocoF.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        OcorrenciaForm ocoF = new OcorrenciaForm(home);
        ocoF.setVisible(true);
        if (ocoF.modified) {
            listData();

        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);

    }//GEN-LAST:event_txtCodActionPerformed

    private void txtOcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOcoActionPerformed
        btnDetActionPerformed(null);

    }//GEN-LAST:event_txtOcoActionPerformed

    private void txtAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluActionPerformed
        btnDetActionPerformed(null);

    }//GEN-LAST:event_txtAluActionPerformed

    private void tblOcoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOcoMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblOcoMouseClicked

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
            java.util.logging.Logger.getLogger(Ocorrencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ocorrencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ocorrencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ocorrencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Ocorrencias dialog = new Ocorrencias(new JFrame());
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
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLimp;
    private javax.swing.JButton btnNew;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblOco;
    private javax.swing.JTextField txtAlu;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtOco;
    // End of variables declaration//GEN-END:variables

}
