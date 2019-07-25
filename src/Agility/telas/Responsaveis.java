package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class Responsaveis extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel dtm;

    public Responsaveis(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblRes.getModel();
        listData();
    }

    private void listData() {
        dtm.setNumRows(0);
        try {
            rs = con.prepareStatement("SELECT id, name, sexo, tel1, tel2 FROM responsibles WHERE status='1'").executeQuery();
            while (rs.next()) {
                dtm.addRow(new String[]{
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("sexo"),
                    rs.getString("tel1"),
                    rs.getString("tel2")
                });
            }
        } catch (SQLException e) {
            AgilitySec.showError(this, "#1037", e);
        }
    }

    private void filter() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtm);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(4);
        RowFilter<Object, Object> rf;

        tblRes.setRowSorter(tr);

        if (txtCod.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));
        }

        if (txtNom.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 1));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtNom.getText(), 1));
        }

        if (cboSex.getSelectedIndex() == 2) {
            filters.add(RowFilter.regexFilter("", 3));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + cboSex.getSelectedItem(), 3));
        }
        rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);
        if (tblRes.getRowCount() > 0) {
            tblRes.setRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        txtNom = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cboSex = new javax.swing.JComboBox<>();
        btnLim = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRes = new javax.swing.JTable();
        btnDet = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RESPONSÁVEIS - Agility®");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Código:");

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

        txtNom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomActionPerformed(evt);
            }
        });
        txtNom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nome:");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sexo:");

        cboSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino", "<html><b>Mostrar Todos" }));
        cboSex.setSelectedIndex(2);
        cboSex.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboSexPopupMenuWillBecomeInvisible(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboSex, 0, 151, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLim))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(157, 157, 157))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLim)))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 930, -1));

        tblRes.setAutoCreateRowSorter(true);
        tblRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COD", "Nome Completo", "Sexo", "Contato 1", "Contato 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblRes.getTableHeader().setReorderingAllowed(false);
        tblRes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblResMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRes);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 933, 236));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 320, -1, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, 181, -1));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Cadastrar");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 180, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 960, 380));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filter();
    }//GEN-LAST:event_txtCodKeyReleased

    private void txtNomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomKeyReleased
        filter();
    }//GEN-LAST:event_txtNomKeyReleased

    private void cboSexPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboSexPopupMenuWillBecomeInvisible
        filter();
    }//GEN-LAST:event_cboSexPopupMenuWillBecomeInvisible

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        txtCod.setText("");
        txtNom.setText("");
        cboSex.setSelectedIndex(2);

        txtNom.grabFocus();

        filter();
    }//GEN-LAST:event_btnLimActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblRes.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um item na tabela.");
            txtCod.grabFocus();
        } else {
            ResponsaveisDetalhes rD = new ResponsaveisDetalhes(tblRes.getValueAt(tblRes.getSelectedRow(), 0).toString());
            rD.setVisible(true);
            if (rD.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (tblRes.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Antes de clicar neste botão, selecione um item na tabela.");
            txtCod.grabFocus();
        } else {
            ResponsaveisForm rF = new ResponsaveisForm((String) tblRes.getValueAt(tblRes.getSelectedRow(), 0));
            rF.setVisible(true);
            if (rF.modified) {
                listData();
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        ResponsaveisForm rF = new ResponsaveisForm();
        rF.setVisible(true);
        if (rF.modified) {
            listData();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtNomActionPerformed

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtCodActionPerformed

    private void tblResMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblResMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblResMouseClicked

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
            java.util.logging.Logger.getLogger(Responsaveis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Responsaveis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Responsaveis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Responsaveis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Responsaveis dialog = new Responsaveis(new JFrame());
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
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNew;
    private javax.swing.JComboBox<String> cboSex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblRes;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtNom;
    // End of variables declaration//GEN-END:variables

}
