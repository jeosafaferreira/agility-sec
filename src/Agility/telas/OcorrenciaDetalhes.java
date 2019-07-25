package Agility.telas;

import Agility.dal.ModuloConexao;
import static Agility.api.AgilitySec.df;
import java.sql.*;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author jeosafaferreira
 */
public class OcorrenciaDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String oco_id, stu_cod;
    Home home;
    boolean modified;

    public OcorrenciaDetalhes(JDialog parent) {
        super(parent);
        initComponents();
        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados desta ocorrência.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0099");
    }

    public OcorrenciaDetalhes(JDialog parent, String id) {
        super(parent);
        initComponents();
        oco_id = id;
        con = ModuloConexao.conector();
        listData();
    }

    private void listData() {
        //Busca dados atuais
        try {
            pst = con.prepareStatement("SELECT "
                    + "    OCO.*, EMP_CRE.name, EMP_MOD.name"
                    + " FROM "
                    + "     occurrences OCO "
                    + "         LEFT JOIN "
                    + "     employees EMP_CRE ON OCO.created_by=EMP_CRE.id "
                    + "         LEFT JOIN "
                    + "     employees EMP_MOD ON OCO.modified_by=EMP_MOD.id "
                    + " WHERE OCO.id='" + oco_id + "'");

            rs = pst.executeQuery();
            System.out.println(rs.toString());
            if (rs.next()) {
                stu_cod = rs.getString("student_cod");

                txtOcoCod.setText(rs.getString("id"));
                txtOcoDat.setText(rs.getString("date"));
                txtOcoDet.setText(rs.getString("details"));
                txtOcoHor.setText(rs.getString("time"));
                txtOcoOco.setText(rs.getString("occurrence"));
                txtOcoSen.setText(rs.getString("sentence"));
                txtCadBy.setText(rs.getString("EMP_CRE.name"));
                txtCadTime.setText(df(rs.getTimestamp("created")));
                txtModBy.setText(rs.getString("EMP_MOD.name"));
                txtModTime.setText(df(rs.getTimestamp("modified")));

                //ALUNO
                pst = con.prepareStatement("SELECT cod, name, dataNasc, created FROM students WHERE cod='" + stu_cod + "'");
                System.out.println(pst);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtAluCod.setText(rs.getString("cod"));
                    txtAluNome.setText(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0105\nDetalhes do erro:\n" + e);
        }
    }

    private void delOco() {
        int q = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar esta ocorrência?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (q == JOptionPane.YES_OPTION) {
            try {
                con.prepareStatement("DELETE FROM occurrences WHERE id=" + oco_id).executeUpdate();
                JOptionPane.showMessageDialog(this, "Ocorrência excluída com sucesso.");
                modified = true;
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0106\nDetalhes do erro:\n" + e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAluno = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAluCod = new javax.swing.JTextField();
        txtAluNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pnlAluno5 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtOcoDat = new javax.swing.JTextField();
        txtOcoHor = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtOcoOco = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOcoDet = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        txtOcoSen = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtOcoCod = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCadBy = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtModBy = new javax.swing.JTextField();
        txtModTime = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCadTime = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnEdit = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DETALHES DA OCORRÊNCIA");
        setModal(true);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder("Aluno"));
        pnlAluno.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Matrícula:");

        txtAluCod.setEditable(false);
        txtAluCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluCodKeyReleased(evt);
            }
        });

        txtAluNome.setEditable(false);
        txtAluNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluNomeKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Nome:");

        javax.swing.GroupLayout pnlAlunoLayout = new javax.swing.GroupLayout(pnlAluno);
        pnlAluno.setLayout(pnlAlunoLayout);
        pnlAlunoLayout.setHorizontalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtAluCod, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 401, Short.MAX_VALUE))
                    .addComponent(txtAluNome))
                .addContainerGap())
        );
        pnlAlunoLayout.setVerticalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAluCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 620, -1));

        pnlAluno5.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Ocorrência"));
        pnlAluno5.setOpaque(false);

        jLabel23.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel23.setText("Data:");

        txtOcoDat.setEditable(false);

        txtOcoHor.setEditable(false);

        jLabel25.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel25.setText("Horário:");

        jLabel26.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel26.setText("Ocorrência:");

        txtOcoOco.setEditable(false);

        jLabel27.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel27.setText("Datalhes da Ocorrência:");

        txtOcoDet.setEditable(false);
        txtOcoDet.setColumns(20);
        txtOcoDet.setLineWrap(true);
        txtOcoDet.setRows(3);
        txtOcoDet.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtOcoDet);

        jLabel28.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel28.setText("Sentença:");

        txtOcoSen.setEditable(false);

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Código:");

        txtOcoCod.setEditable(false);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Cadastrado por:");

        txtCadBy.setEditable(false);
        txtCadBy.setForeground(new java.awt.Color(51, 51, 51));

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Modificado por:");

        txtModBy.setEditable(false);
        txtModBy.setForeground(new java.awt.Color(51, 51, 51));

        txtModTime.setEditable(false);
        txtModTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Modificado em:");

        txtCadTime.setEditable(false);
        txtCadTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Cadastrado em:");

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout pnlAluno5Layout = new javax.swing.GroupLayout(pnlAluno5);
        pnlAluno5.setLayout(pnlAluno5Layout);
        pnlAluno5Layout.setHorizontalGroup(
            pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAluno5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                    .addComponent(txtOcoOco)
                    .addComponent(txtOcoSen)
                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel23)
                            .addGroup(pnlAluno5Layout.createSequentialGroup()
                                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtOcoCod, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                    .addComponent(txtOcoDat, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(txtOcoHor, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(39, 39, 39)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCadTime)
                                .addGroup(pnlAluno5Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(41, 41, 41)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel26))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlAluno5Layout.setVerticalGroup(
            pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAluno5Layout.createSequentialGroup()
                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOcoCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25))
                        .addGap(4, 4, 4)
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOcoDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtOcoHor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6))
                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAluno5Layout.createSequentialGroup()
                                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlAluno5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlAluno5Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOcoOco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addGap(6, 6, 6)
                .addComponent(txtOcoSen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        getContentPane().add(pnlAluno5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 620, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar Ocorrência");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 470, 197, 60));

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDel.setText("Deletar Ocorrência");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 470, 198, 60));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 470, 198, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 650, 550));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAluCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluCodKeyReleased

    }//GEN-LAST:event_txtAluCodKeyReleased

    private void txtAluNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluNomeKeyReleased

    }//GEN-LAST:event_txtAluNomeKeyReleased

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        OcorrenciaForm ocoF = new OcorrenciaForm(home, txtOcoCod.getText());
        ocoF.setVisible(true);
        if (ocoF.modified) {
            this.modified = true;
            listData();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        delOco();
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

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
            java.util.logging.Logger.getLogger(OcorrenciaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OcorrenciaDetalhes dialog = new OcorrenciaDetalhes(new JDialog());
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
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JPanel pnlAluno5;
    private javax.swing.JTextField txtAluCod;
    private javax.swing.JTextField txtAluNome;
    private javax.swing.JTextField txtCadBy;
    private javax.swing.JTextField txtCadTime;
    private javax.swing.JTextField txtModBy;
    private javax.swing.JTextField txtModTime;
    private javax.swing.JTextField txtOcoCod;
    private javax.swing.JTextField txtOcoDat;
    private javax.swing.JTextArea txtOcoDet;
    private javax.swing.JTextField txtOcoHor;
    private javax.swing.JTextField txtOcoOco;
    private javax.swing.JTextField txtOcoSen;
    // End of variables declaration//GEN-END:variables

}
