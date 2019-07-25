package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author jeosafaferreira
 */
public class ResponsaveisForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String type, responsible_id, sql;
    boolean modified;
    DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");

    public ResponsaveisForm() {
        initComponents();
        con = ModuloConexao.conector();
        type = "new";
        txtRg.setDocument(new AgilitySec.onlyNumbers());
    }

    public ResponsaveisForm(String id) {
        initComponents();
        con = ModuloConexao.conector();
        type = "upd";
        responsible_id = id;
        this.setTitle("EDITAR RESPONSÁVEL (#" + id + ")");
        txtRg.setDocument(new AgilitySec.onlyNumbers());
        getData();
    }

    private void getData() {
        try {
            rs = con.prepareStatement("SELECT * FROM responsibles WHERE id='" + responsible_id + "'").executeQuery();
            if (rs.next()) {
                txtCon1.setText(rs.getString("tel1"));
                txtCon2.setText(rs.getString("tel2"));
                txtCpf.setText(rs.getString("cpf"));
                txtNas.setText(dateBr.format(dateSql.parse(rs.getString("dataNasc"))));
                txtNom.setText(rs.getString("name"));
                txtRg.setText(rs.getString("rg"));
                txtRgEmi.setText(dateBr.format(dateSql.parse(rs.getString("dataRg"))));
                txtRgOrg.setText(rs.getString("orgaoRg"));
                cboEstCiv.setSelectedItem(rs.getString("estCivil"));
                cboSex.setSelectedItem(rs.getString("sexo"));
            } else {
                AgilitySec.showError(this, "#1039");
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#1038", e);
        }

    }

    /**
     *
     * @return true, se estiver tudo Ok.
     */
    private boolean check() {
        boolean r = false;
        if (txtCon1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Telefone 1.\".");
            txtCon1.grabFocus();
        } else if (txtCpf.getText().equals("   .   .   -  ")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo  \"CPF\".");
            txtCpf.grabFocus();
        } else if (txtNas.getText().equals("  /  /    ")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo  \"Data de Nascimento\".");
            txtNas.grabFocus();
        } else if (txtNom.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Nome do Responsável\".");
            txtNom.grabFocus();
        } else if (txtRg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"RG\".");
            txtRg.grabFocus();
        } else if (txtRgEmi.getText().equals("  /  /    ")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Data de Emissão\".");
            txtRgEmi.grabFocus();
        } else if (txtRgOrg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Orgão Emissor\".");
            txtRgOrg.grabFocus();
        } else if (cboEstCiv.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção no campo \"Estado Civil\".");
            cboEstCiv.grabFocus();
        } else if (cboSex.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção no campo \"Sexo\".");
            cboSex.grabFocus();
        } else {
            dateBr.setLenient(false);
            r = true;

            try {
                Integer.parseInt(txtRg.getText());
            } catch (NumberFormatException e) {
                r = false;
                AgilitySec.checkMessageError(this, txtRg, "RG");
            }

            try {
                dateBr.parse(txtNas.getText());
            } catch (ParseException ex) {
                r = false;
                JOptionPane.showMessageDialog(this, "Por favor, preencha corretamente o campo \"Data de Nascimento\".");
                txtNas.grabFocus();
            }

            try {
                dateBr.parse(txtRgEmi.getText());
            } catch (ParseException ex) {
                r = false;
                JOptionPane.showMessageDialog(this, "Por favor, preencha corretamente o campo \"Data de Emissão\".");
                txtRgEmi.grabFocus();
            }
        }
        return r;
    }

    private void execQuery() {
        if (check()) {
            String txtSuc = null;

            if (type.equals("new")) {
                sql = "INSERT INTO responsibles (name, dataNasc, sexo, cpf, rg, dataRg, orgaoRg, tel1, tel2, estCivil, created_by) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                txtSuc = "Responsável cadastrado com sucesso!";

            } else if (type.equals("upd")) {
                sql = "UPDATE responsibles SET name=?, dataNasc=?, sexo=?, cpf=?, rg=?, dataRg=?, orgaoRg=?, tel1=?, tel2=?, estCivil=?, modified_by=? WHERE id=" + responsible_id;
                txtSuc = "Responsável atualizado com sucesso!";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtNom.getText());
                pst.setString(2, dateSql.format(dateBr.parse(txtNas.getText())));
                pst.setString(3, cboSex.getSelectedItem().toString());
                pst.setString(4, txtCpf.getText());
                pst.setString(5, txtRg.getText());
                pst.setString(6, dateSql.format(dateBr.parse(txtRgEmi.getText())));
                pst.setString(7, txtRgOrg.getText());
                pst.setString(8, txtCon1.getText());
                pst.setString(9, txtCon2.getText());
                pst.setString(10, cboEstCiv.getSelectedItem().toString());
                pst.setString(11, Login.emp_id);
                if (pst.executeUpdate() == 0) {
                    AgilitySec.showError(this, "#1041");
                } else {
                    this.dispose();
                    JOptionPane.showMessageDialog(null, txtSuc);
                    modified = true;
                }
            } catch (Exception e) {
                AgilitySec.showError(this, "#1042", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpResp1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtNom = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtRg = new javax.swing.JTextField();
        txtCpf = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtCon1 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtCon2 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtNas = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtNas = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        txtRgEmi = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtRgEmi = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        jLabel37 = new javax.swing.JLabel();
        cboEstCiv = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        cboSex = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        txtRgOrg = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnSal = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR RESPONSÁVEL");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpResp1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpResp1.setOpaque(false);

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Nome do Responsável:");

        txtNom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomFocusLost(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel32.setText("CPF:");

        jLabel33.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel33.setText("RG:");

        try{    MaskFormatter cpf= new MaskFormatter("###.###.###-##");    txtCpf = new JFormattedTextField(cpf); }    catch (Exception e){ }

        jLabel34.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel34.setText("Telefone 1:");

        jLabel35.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel35.setText("Telefone 2:");

        jLabel36.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel36.setText("Data de Nascimento:");

        jLabel37.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel37.setText("Data de Emissão:");

        cboEstCiv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casado(a)", "Solteiro(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        cboEstCiv.setSelectedIndex(-1);

        jLabel38.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel38.setText("Estado Civil:");

        cboSex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        cboSex.setSelectedIndex(-1);

        jLabel39.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel39.setText("Sexo:");

        jLabel40.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel40.setText("Orgão Emissor:");

        jLabel70.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(233, 2, 2));
        jLabel70.setText("*");

        jLabel71.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(233, 2, 2));
        jLabel71.setText("*");

        jLabel72.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(233, 2, 2));
        jLabel72.setText("*");

        jLabel73.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(233, 2, 2));
        jLabel73.setText("*");

        jLabel74.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(233, 2, 2));
        jLabel74.setText("*");

        jLabel75.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(233, 2, 2));
        jLabel75.setText("*");

        jLabel76.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(233, 2, 2));
        jLabel76.setText("*");

        jLabel77.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(233, 2, 2));
        jLabel77.setText("*");

        jLabel79.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(233, 2, 2));
        jLabel79.setText("*");

        javax.swing.GroupLayout jpResp1Layout = new javax.swing.GroupLayout(jpResp1);
        jpResp1.setLayout(jpResp1Layout);
        jpResp1Layout.setHorizontalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel76)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel70))
                            .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel71))
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel74))
                            .addComponent(txtNas, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCon1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel79))
                            .addComponent(txtCpf))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel73))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35)
                                    .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel77))
                                    .addComponent(txtRgEmi)))
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtCon2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboEstCiv, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel75))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel72))
                    .addComponent(txtRgOrg)
                    .addComponent(cboSex, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jpResp1Layout.setVerticalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel36)
                    .addComponent(jLabel39)
                    .addComponent(jLabel70)
                    .addComponent(jLabel71)
                    .addComponent(jLabel72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel37)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73)
                    .addComponent(jLabel74)
                    .addComponent(jLabel75)
                    .addComponent(jLabel79))
                .addGap(6, 6, 6)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRgEmi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRgOrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38)
                    .addComponent(jLabel76)
                    .addComponent(jLabel77))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCon2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEstCiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        getContentPane().add(jpResp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 214, 127, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(599, 214, 111, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 730, 270));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtCon1.getText().isEmpty()
                    && txtCon2.getText().isEmpty()
                    && txtCpf.getText().equals("   .   .   -  ")
                    && txtNas.getText().equals("  /  /    ")
                    && txtNom.getText().isEmpty()
                    && txtRg.getText().isEmpty()
                    && txtRgEmi.getText().equals("  /  /    ")
                    && txtRgOrg.getText().isEmpty()
                    && cboEstCiv.getSelectedIndex() == -1
                    && cboSex.getSelectedIndex() == -1) {
                this.dispose();
            } else {
                int q = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja cancelar? Todos os dados deste responsável serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
                if (q == JOptionPane.YES_OPTION) {
                    this.dispose();
                }
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
        if (type.equals("upd")) {
            int q = JOptionPane.showConfirmDialog(this, "Deseja realmente salvar as alterações?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (q == JOptionPane.YES_OPTION) {
                execQuery();
            }
        } else {
            execQuery();
        }
    }//GEN-LAST:event_btnSalActionPerformed

    private void txtNomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomFocusLost
        if (type.equals("new")) {
            try {
                if (AgilitySec.alreadyExists(con, "responsibles", new String[]{"name"}, new String[]{txtNom.getText()})) {
                    int q = JOptionPane.showConfirmDialog(this, "Já existe um responsável com este nome! Deseja carregar dados?", "Atenção", JOptionPane.YES_NO_OPTION);
                    if (q == JOptionPane.YES_OPTION) {
                        try {
                            rs = con.prepareStatement("SELECT id from responsibles WHERE name='" + txtNom.getText() + "'").executeQuery();
                            rs.next();
                            ResponsaveisDetalhes rD = new ResponsaveisDetalhes(rs.getString("id"));
                            rD.setVisible(true);
                        } catch (Exception e) {
                            AgilitySec.showError(this, "#1040", e);
                        }
                        this.dispose();
                    } else {
                        txtNom.setText("");
                        txtNom.grabFocus();
                    }
                }
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1049", ex);
            }
        }
    }//GEN-LAST:event_txtNomFocusLost

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
            java.util.logging.Logger.getLogger(ResponsaveisForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ResponsaveisForm dialog = new ResponsaveisForm();
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
    private javax.swing.JButton btnSal;
    private javax.swing.JComboBox<String> cboEstCiv;
    private javax.swing.JComboBox<String> cboSex;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JPanel jpResp1;
    private javax.swing.JTextField txtCon1;
    private javax.swing.JTextField txtCon2;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtNas;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtRg;
    private javax.swing.JTextField txtRgEmi;
    private javax.swing.JTextField txtRgOrg;
    // End of variables declaration//GEN-END:variables

}
