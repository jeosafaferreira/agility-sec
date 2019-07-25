package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class MensalidadeAlunoSelec extends javax.swing.JDialog {

    static Connection con = ModuloConexao.conector();
    static PreparedStatement pst;
    static ResultSet rs;
    String call;

    public MensalidadeAlunoSelec(JFrame parent) {
        super(parent);
        initComponents();
    }

    private void searchStu() {
        int row = 0;
        String sql, sqlToDi;
        if (txtAluNome.getText().isEmpty()) {
            sql = "SELECT id, cod, name, dataNasc, class_id, created FROM students WHERE cod LIKE '%" + txtAluCod.getText() + "%'";
            sqlToDi = "SELECT cod, name, dataNasc, created FROM students WHERE cod LIKE '%" + txtAluCod.getText() + "%'";
        } else {
            sql = "SELECT id, cod, name, dataNasc, class_id, created FROM students WHERE name LIKE '%" + txtAluNome.getText() + "%' ";
            sqlToDi = "SELECT cod, name, dataNasc, created FROM students WHERE name LIKE '%" + txtAluNome.getText() + "%' ";
        }
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Matrícula", "Nome do Aluno", "Data de Nasc.", "Cadastrado em:"};
                    int cols[] = {50, 250, 0, 0};
                    Dialog dAlu = new Dialog(new JDialog(), true, sqlToDi, headers, cols);
                    dAlu.setVisible(true);
                    row = dAlu.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);

                    txtAluCod.setText(rs.getString("cod"));
                    txtAluNome.setText(rs.getString("name"));
                    txtAluCad.setText(rs.getString("created"));
                    txtAluNasc.setText(rs.getString("datanasc"));

                    txtAluCod.setEnabled(false);
                    txtAluNome.setEnabled(false);

                    btnOkActionPerformed(null);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                txtAluCod.grabFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao pesquisar aluno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0063\nDetalhes do erro:\n" + e);
        }
    }

    private boolean check() {
        boolean r = false;
        if (!txtAluNasc.getText().isEmpty()) {
            r = true;
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno!");
        }
        return r;
    }

    public static void payLastTuition(String sCod) {
        String tui_id;
        try {
            rs = con.prepareStatement("SELECT id FROM tuitions WHERE student_cod='" + sCod + "' AND valor_pago IS NULL ORDER BY venc ASC").executeQuery();
            if (rs.next()) {
                tui_id = rs.getString("id");
                new MensalidadePagar(new JDialog(), tui_id).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Todas as mensalidades deste aluno já estão quitadas!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            AgilitySec.showError(null, "#1094", ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        pnlAluno = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAluCod = new javax.swing.JTextField();
        txtAluNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnPesqAlu = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtAluNasc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAluCad = new javax.swing.JTextField();
        btnEraseAlu = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selecionar Aluno");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, -1, -1));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Confirmar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 170, -1, -1));

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DO ALUNO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        pnlAluno.setForeground(new java.awt.Color(255, 255, 255));
        pnlAluno.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Matrícula:");

        txtAluCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluCodKeyReleased(evt);
            }
        });

        txtAluNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluNomeKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Nome:");

        btnPesqAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnPesqAlu.setText("...");
        btnPesqAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqAluActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Data de Nasc.:");

        txtAluNasc.setEditable(false);
        txtAluNasc.setEnabled(false);
        txtAluNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluNascActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Cadastrado em:");

        txtAluCad.setEditable(false);
        txtAluCad.setEnabled(false);

        btnEraseAlu.setText("Limpar dados");
        btnEraseAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEraseAluActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAlunoLayout = new javax.swing.GroupLayout(pnlAluno);
        pnlAluno.setLayout(pnlAlunoLayout);
        pnlAlunoLayout.setHorizontalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAluNasc, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtAluCod, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(6, 6, 6))
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)))
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                                .addComponent(txtAluCad, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEraseAlu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesqAlu)))
                .addContainerGap())
        );
        pnlAlunoLayout.setVerticalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(0, 0, 0)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAluCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesqAlu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAluNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEraseAlu))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 580, 230));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAluCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluCodKeyReleased
        txtAluNome.setText(null);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchStu();
        }
    }//GEN-LAST:event_txtAluCodKeyReleased

    private void txtAluNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluNomeKeyReleased
        txtAluCod.setText(null);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchStu();
        }
    }//GEN-LAST:event_txtAluNomeKeyReleased

    private void btnPesqAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqAluActionPerformed
        searchStu();
    }//GEN-LAST:event_btnPesqAluActionPerformed

    private void txtAluNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluNascActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluNascActionPerformed

    private void btnEraseAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEraseAluActionPerformed
        txtAluCod.setText(null);
        txtAluNome.setText(null);
        txtAluCad.setText(null);
        txtAluNasc.setText(null);
        txtAluCod.setEnabled(true);
        txtAluNome.setEnabled(true);

        txtAluCod.grabFocus();
        btnPesqAlu.setEnabled(true);
    }//GEN-LAST:event_btnEraseAluActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (check()) {
            this.dispose();
            payLastTuition(txtAluCod.getText());
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCanActionPerformed

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
            java.util.logging.Logger.getLogger(MensalidadeAlunoSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MensalidadeAlunoSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MensalidadeAlunoSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MensalidadeAlunoSelec.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MensalidadeAlunoSelec dialog = new MensalidadeAlunoSelec(new JFrame());
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
    private javax.swing.JButton btnEraseAlu;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnPesqAlu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JTextField txtAluCad;
    private javax.swing.JTextField txtAluCod;
    private javax.swing.JTextField txtAluNasc;
    private javax.swing.JTextField txtAluNome;
    // End of variables declaration//GEN-END:variables

}
