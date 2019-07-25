package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
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
public class PlanoContas extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel dtm;
    Home home;

    public PlanoContas(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblPlan.getModel();
        listData();
    }

    private void listData() {
        dtm.setNumRows(0);
        try {
            rs = con.prepareStatement("SELECT * FROM acc_plan").executeQuery();
            while (rs.next()) {
                dtm.addRow(new String[]{rs.getString("id"), rs.getString("title"), rs.getString("type")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar plano de contas.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1009\nDetalhes do erro:\n" + e);
            this.dispose();
        }
    }

    private void filter(String field) {
        dtm = (DefaultTableModel) tblPlan.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtm);
        tblPlan.setRowSorter(tr);
        tr.setSortable(0, true);
        String key;
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>(4);
        RowFilter<Object, Object> rf;
        switch (field) {
            case "cod":

                filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));

                //"MULTI-FILTRO"
                if (!txtTit.getText().equals("")) {
                    filters.add(RowFilter.regexFilter("(?i)" + txtTit.getText(), 1));
                }
                if (cboTip.getSelectedItem() != "Todos") {
                    filters.add(RowFilter.regexFilter("(?i)" + cboTip.getSelectedItem(), 2));
                } else {
                    filters.add(RowFilter.regexFilter("", 2));
                }
                rf = RowFilter.andFilter(filters);
                tr.setRowFilter(rf);
                break;

            case "tit":
                filters.add(RowFilter.regexFilter("(?i)" + txtTit.getText(), 1));

                if (!txtCod.getText().equals("")) {
                    filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));
                }
                if (cboTip.getSelectedItem() != "Todos") {
                    filters.add(RowFilter.regexFilter("(?i)" + cboTip.getSelectedItem(), 2));
                } else {
                    filters.add(RowFilter.regexFilter("", 2));
                }
                rf = RowFilter.andFilter(filters);
                tr.setRowFilter(rf);

                break;
            case "tip":
                if (cboTip.getSelectedItem() == "Todos") {
                    filters.add(RowFilter.regexFilter("", 2));
                } else {
                    filters.add(RowFilter.regexFilter("(?i)" + cboTip.getSelectedItem(), 2));
                }

                if (!txtCod.getText().equals("")) {
                    filters.add(RowFilter.regexFilter("(?i)" + txtTit.getText(), 0));
                }
                if (!txtTit.getText().equals("")) {
                    filters.add(RowFilter.regexFilter("(?i)" + txtTit.getText(), 1));
                }

                rf = RowFilter.andFilter(filters);
                tr.setRowFilter(rf);
                break;
        }
        if (tblPlan.getRowCount() > 0) {
            tblPlan.setRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboTip = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPlan = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDet = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PLANO DE CONTAS - Agility®");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel2ComponentShown(evt);
            }
        });

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

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Título:");

        txtTit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitActionPerformed(evt);
            }
        });
        txtTit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTitKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Tipo:");

        cboTip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pagamentos", "Recebimentos" }));
        cboTip.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTipPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setText("Limpar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cboTip, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 554, -1));

        tblPlan.setAutoCreateRowSorter(true);
        tblPlan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPlan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPlan.getTableHeader().setReorderingAllowed(false);
        tblPlan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPlanMouseClicked(evt);
            }
        });
        tblPlan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPlanKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblPlan);
        if (tblPlan.getColumnModel().getColumnCount() > 0) {
            tblPlan.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblPlan.getColumnModel().getColumn(1).setPreferredWidth(244);
            tblPlan.getColumnModel().getColumn(2).setPreferredWidth(244);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 554, 179));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Cadastrar");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 180, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 270, 181, -1));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(383, 270, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 580, 330));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filter("cod");

    }//GEN-LAST:event_txtCodKeyReleased

    private void txtTitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyReleased
        filter("tit");
    }//GEN-LAST:event_txtTitKeyReleased

    private void cboTipPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTipPopupMenuWillBecomeInvisible
        if (cboTip.getSelectedIndex() > -1) {
            filter("tip");
        }
    }//GEN-LAST:event_cboTipPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txtCod.setText("");
        txtTit.setText("");
        cboTip.setSelectedIndex(0);

        txtCod.grabFocus();
        filter("cod");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel2ComponentShown

    }//GEN-LAST:event_jPanel2ComponentShown

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        PlanoContasForm pcf = new PlanoContasForm(home);
        pcf.setVisible(true);
        if (pcf.modified) {
            listData();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tblPlan.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Antes de clicar neste botão, selecione um item na tabela.");
            txtCod.grabFocus();
        } else {
            PlanoContasForm pcF = new PlanoContasForm((String) tblPlan.getValueAt(tblPlan.getSelectedRow(), 0));
            pcF.setVisible(true);
            if (pcF.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblPlan.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um item na tabela.");
            txtCod.grabFocus();
        } else {
            PlanoContaDetalhes pcD = new PlanoContaDetalhes(tblPlan.getValueAt(tblPlan.getSelectedRow(), 0).toString());
            pcD.setVisible(true);
            if (pcD.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtTitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtTitActionPerformed

    private void tblPlanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPlanKeyReleased
        AgilitySec.getColumnsWidth(tblPlan);
    }//GEN-LAST:event_tblPlanKeyReleased

    private void tblPlanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPlanMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblPlanMouseClicked

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
            java.util.logging.Logger.getLogger(PlanoContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlanoContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlanoContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlanoContas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PlanoContas dialog = new PlanoContas(new JFrame());
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
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox<String> cboTip;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPlan;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtTit;
    // End of variables declaration//GEN-END:variables

}
