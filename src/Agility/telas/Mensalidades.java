/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jferreira
 */
public class Mensalidades extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel dtmAlu, dtmPag, dtmPen;
    Set aluId = new LinkedHashSet();

    public Mensalidades(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtmAlu = (DefaultTableModel) tblAlu.getModel();
        dtmPag = (DefaultTableModel) tblPag.getModel();
        dtmPen = (DefaultTableModel) tblPen.getModel();
        listData();
    }

    private void listData() {
        try {
            //BUSCA ALUNOS
            rs = con.prepareStatement("SELECT cod, name FROM students WHERE course_id IS NOT NULL").executeQuery();

            while (rs.next()) {
                dtmAlu.addRow(new String[]{
                    rs.getString("cod"),
                    rs.getString("name")
                });
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1066", ex);
        }

    }

    private void filter() {
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(dtmAlu);
        List<RowFilter<Object, Object>> filters = new ArrayList<>(4);
        RowFilter<Object, Object> rf;

        tblAlu.setRowSorter(tr);

        if (txtCod.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtCod.getText(), 0));
        }

        if (txtAlu.getText().equals("")) {
            filters.add(RowFilter.regexFilter("", 0));
        } else {
            filters.add(RowFilter.regexFilter("(?i)" + txtAlu.getText(), 1));
        }

        rf = RowFilter.andFilter(filters);
        tr.setRowFilter(rf);

    }

    private void listTuitions() {
        dtmPag.setNumRows(0);
        dtmPen.setNumRows(0);
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");

        String mat = (String) tblAlu.getValueAt(tblAlu.getSelectedRow(), 0);
        try {
            //Mensalidades Pendentes
            rs = con.prepareStatement("SELECT id,valor,venc,academic_year FROM tuitions WHERE student_cod='" + mat + "' AND valor_pago IS NULL AND academic_year=" + Login.academic_year).executeQuery();
            System.out.println("Pen:" + rs.getStatement());
            while (rs.next()) {
                dtmPen.addRow(new String[]{
                    rs.getString("id"),
                    "R$ " + rs.getString("valor").replace(".", ","),
                    dateBr.format(rs.getDate("venc")),
                    rs.getString("academic_year")
                });
            }

            //Mensalidades Pagas
            rs = con.prepareStatement("SELECT id, valor, valor_pago, venc, data_baixa FROM tuitions WHERE student_cod='" + mat + "' AND valor_pago IS NOT NULL AND academic_year=" + Login.academic_year).executeQuery();
            System.out.println("Pg:" + rs.getStatement());
            while (rs.next()) {
                dtmPag.addRow(new String[]{
                    rs.getString("id"),
                    "R$ " + rs.getString("valor").replace(".", ","),
                    "R$ " + rs.getString("valor_pago").replace(".", ","),
                    dateBr.format(rs.getDate("venc")),
                    dateBr.format(rs.getDate("data_baixa"))
                });
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1069", ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtAlu = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlu = new javax.swing.JTable();
        txtCod = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Header = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPag = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPen = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        tblAlu.setAutoCreateRowSorter(true);
        tblAlu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nome do Aluno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAlu.getTableHeader().setReorderingAllowed(false);
        tblAlu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAluMouseClicked(evt);
            }
        });
        tblAlu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAluKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblAluKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblAlu);
        if (tblAlu.getColumnModel().getColumnCount() > 0) {
            tblAlu.getColumnModel().getColumn(0).setPreferredWidth(92);
            tblAlu.getColumnModel().getColumn(1).setPreferredWidth(320);
        }

        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(txtAlu))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("CONTROLE DE MENSALIDADES");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, -1, -1));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("X");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 10, -1, -1));
        jPanel2.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1180, 148));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("FATURAS PENDENTES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("FATURAS PAGAS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(310, 310, 310))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        jButton1.setText("Fechar Janela");

        tblPag.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Valor Base", "Valor Pago", "Vencimento", "Pago em:"
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
        tblPag.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPag.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblPag);

        tblPen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Valor", "Vencimento", "Ano Letivo"
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
        tblPen.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPen.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblPen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void txtAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluActionPerformed

    private void tblAluKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAluKeyReleased
        System.out.println(tblAlu.getColumnModel().getColumn(0).getWidth());
        System.out.println(tblAlu.getColumnModel().getColumn(1).getWidth());
    }//GEN-LAST:event_tblAluKeyReleased

    private void txtAluKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluKeyReleased
        filter();
    }//GEN-LAST:event_txtAluKeyReleased

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filter();
    }//GEN-LAST:event_txtCodKeyReleased

    private void tblAluMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAluMouseClicked
        if (tblAlu.getSelectedRow() > -1) {
            listTuitions();
        }
    }//GEN-LAST:event_tblAluMouseClicked

    private void tblAluKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAluKeyPressed
        evt.consume();

    }//GEN-LAST:event_tblAluKeyPressed

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
            java.util.logging.Logger.getLogger(Mensalidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mensalidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mensalidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mensalidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Mensalidades dialog = new Mensalidades(new JFrame());
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
    private javax.swing.JLabel Header;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable tblAlu;
    private javax.swing.JTable tblPag;
    private javax.swing.JTable tblPen;
    private javax.swing.JTextField txtAlu;
    private javax.swing.JTextField txtCod;
    // End of variables declaration//GEN-END:variables

}
