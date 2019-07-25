package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class ProfessoresXDisciplinasDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    DefaultListModel dlmDiscAll = new DefaultListModel();
    DefaultListModel dlmDiscProf = new DefaultListModel();

    public ProfessoresXDisciplinasDetalhes() {
        initComponents();
        con = ModuloConexao.conector();
        AgilitySec.showError(this, "#1072");
    }

    public ProfessoresXDisciplinasDetalhes(String id, String name) {
        initComponents();
        con = ModuloConexao.conector();
        txtCod.setText(id);
        txtNome.setText(name);
        listDiscAll.setModel(dlmDiscAll);
        listDiscProf.setModel(dlmDiscProf);
        listData();
    }

    private void listData() {
        //BUSCA TODAS AS DISCIPLINAS
        try {
            rs = con.prepareStatement("SELECT id, cod, name FROM disciplines WHERE status=1 ORDER BY cod ASC").executeQuery();
            while (rs.next()) {
                dlmDiscAll.addElement(rs.getString("cod") + "- " + rs.getString("name"));
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1073", ex);
        }

        //BUSCA DISCIPLINAS DESTE PROF
        try {
            String disc;
            rs = con.prepareStatement("SELECT TD.*, D.cod, D.name FROM teacher_discipline TD LEFT JOIN disciplines D ON TD.discipline_id=D.id WHERE TD.employee_id='" + txtCod.getText() + "' ORDER BY D.cod ASC").executeQuery();
            while (rs.next()) {
                disc = rs.getString("D.cod") + "- " + rs.getString("D.name");
                dlmDiscProf.addElement(disc);
                if (dlmDiscAll.contains(disc)) {
                    dlmDiscAll.removeElement(disc);
                }
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1074", ex);
        }
    }

    private void add(String value) {
        dlmDiscProf.addElement(value);
        dlmDiscAll.removeElement(value);
    }

    private void addAll() {
        int qtd = dlmDiscAll.getSize();
        for (int i = 0; i < qtd; i++) {
            dlmDiscProf.addElement(dlmDiscAll.elementAt(i));
        }
        dlmDiscAll.clear();
    }

    private void remove(String value) {
        if (!value.isEmpty()) {
            dlmDiscProf.removeElement(value);
            dlmDiscAll.addElement(value);
        }
    }

    private void remAll() {
        int qtd = dlmDiscProf.getSize();
        for (int i = 0; i < qtd; i++) {
            dlmDiscAll.addElement(dlmDiscProf.elementAt(i));
        }
        dlmDiscProf.clear();
    }

    private boolean check() {
        boolean r = false;
        if (dlmDiscProf.getSize() < 1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione a(s) disciplina(s) associadas a este professor(a).");
        } else {
            r = true;
        }
        return r;

    }

    private void save() {
        int pos;
        String cod;
        boolean execute = false;
        //DELETA OS DADOS DO db (Limpeza)
        try {
            con.prepareStatement("DELETE FROM teacher_discipline WHERE employee_id=" + txtCod.getText()).executeUpdate();
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1076", ex);
        }

        try {
            for (int i = 0; i < dlmDiscProf.getSize(); i++) {
                pos = dlmDiscProf.get(i).toString().indexOf("-");
                cod = dlmDiscProf.get(i).toString().substring(0, pos);
                rs = con.prepareStatement("SELECT id FROM disciplines WHERE cod='" + cod + "'").executeQuery();
                rs.next();
                pst = con.prepareStatement("INSERT INTO teacher_discipline SET "
                        + "employee_id='" + txtCod.getText() + "'"
                        + ",discipline_id='" + rs.getString("id") + "'"
                        + ",created_by='" + Login.emp_id + "'"
                );
                execute = pst.executeUpdate() > 0;
            }
            if (execute) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1075", ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listDiscProf = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDiscAll = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        btnAddAll1 = new javax.swing.JButton();
        btnAddDisc = new javax.swing.JButton();
        btnRemDisc = new javax.swing.JButton();
        btnRemAll = new javax.swing.JButton();
        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PROFESSOR X DISCIPLINAS");
        setAlwaysOnTop(true);
        setModal(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Nome:");

        txtCod.setEditable(false);

        txtNome.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 325, Short.MAX_VALUE))
                    .addComponent(txtNome))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 488, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);

        listDiscProf.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listDiscProf);

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Disciplinas Disponíveis:");

        listDiscAll.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listDiscAll);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Disciplinas deste Professor:");

        btnAddAll1.setText(">>");
        btnAddAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAll1ActionPerformed(evt);
            }
        });

        btnAddDisc.setText(">");
        btnAddDisc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDiscActionPerformed(evt);
            }
        });

        btnRemDisc.setText("<");
        btnRemDisc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemDiscActionPerformed(evt);
            }
        });

        btnRemAll.setText("<<");
        btnRemAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRemAll, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddAll1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddDisc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemDisc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(btnAddAll1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddDisc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemDisc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemAll)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 86, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 294, -1, -1));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Salvar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 294, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 510, 350));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAll1ActionPerformed
        addAll();
    }//GEN-LAST:event_btnAddAll1ActionPerformed

    private void btnAddDiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDiscActionPerformed
        if (listDiscAll.getSelectedIndex() > -1) {
            add(listDiscAll.getSelectedValue());
        }
    }//GEN-LAST:event_btnAddDiscActionPerformed

    private void btnRemDiscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemDiscActionPerformed
        if (listDiscProf.getSelectedIndex() > -1) {
            remove(listDiscProf.getSelectedValue());
        }
    }//GEN-LAST:event_btnRemDiscActionPerformed

    private void btnRemAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemAllActionPerformed
        remAll();
    }//GEN-LAST:event_btnRemAllActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        this.dispose();

    }//GEN-LAST:event_btnCanActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (check()) {
            save();
        }
    }//GEN-LAST:event_btnOkActionPerformed

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
            java.util.logging.Logger.getLogger(ProfessoresXDisciplinasDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProfessoresXDisciplinasDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProfessoresXDisciplinasDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProfessoresXDisciplinasDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProfessoresXDisciplinasDetalhes dialog = new ProfessoresXDisciplinasDetalhes();
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
    private javax.swing.JButton btnAddAll1;
    private javax.swing.JButton btnAddDisc;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRemAll;
    private javax.swing.JButton btnRemDisc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> listDiscAll;
    private javax.swing.JList<String> listDiscProf;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
