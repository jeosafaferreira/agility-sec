package Agility.telas;

import Agility.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

public class Dialog extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultTableModel dtm;

    public Dialog(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno ao efetuar pesquisa.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0061");
    }

    public Dialog(JDialog parent, boolean modal, String sql, String headers[], int widths[]) {
        super(parent, modal);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblResult.getModel();
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            tblResult.setModel(DbUtils.resultSetToTableModel(rs));

            int cols = headers.length;

            for (int i = 0; i < cols; i++) {
                tblResult.getColumnModel().getColumn(i).setHeaderValue(headers[i]);
                tblResult.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0012\nDetalhes do erro:\n" + e);
        }

    }

    public int idFDialog() {
        return tblResult.getSelectedRow() + 1;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblResult = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resultado da Busca");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        tblResult.setAutoCreateRowSorter(true);
        tblResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResult.getTableHeader().setReorderingAllowed(false);
        tblResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblResultMouseClicked(evt);
            }
        });
        tblResult.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblResultKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblResult);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tblResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblResultMouseClicked
        idFDialog();
        this.dispose();

    }//GEN-LAST:event_tblResultMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void tblResultKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblResultKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            this.dispose();
        }
    }//GEN-LAST:event_tblResultKeyPressed

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
            java.util.logging.Logger.getLogger(Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dialog dialog = new Dialog(new JDialog(), true);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblResult;
    // End of variables declaration//GEN-END:variables
}
