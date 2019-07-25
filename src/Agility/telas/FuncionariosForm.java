package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.dal.WebServiceCep;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author jferreira
 */
public class FuncionariosForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    boolean modified;
    String type, id, addr_id, occ_id = "", emp_id;
    Set<String> occs_id = new LinkedHashSet();

    public FuncionariosForm() {
        initComponents();
        con = ModuloConexao.conector();
        this.type = "new";
        txtRg.setDocument(new AgilitySec.onlyNumbers());
        txtCtpsNum.setDocument(new AgilitySec.onlyNumbers());
        txtCtpsSer.setDocument(new AgilitySec.onlyNumbers());
        txtNis.setDocument(new AgilitySec.onlyNumbers());
        txtNum.setDocument(new AgilitySec.onlyNumbers());
        txtCurso1Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso2Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso3Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso4Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso5Ano.setDocument(new AgilitySec.onlyNumbers());
        txtTel1.setDocument(new AgilitySec.onlyNumbers());
        txtTel2.setDocument(new AgilitySec.onlyNumbers());

        btnDel.setVisible(false);
        listData();
        txtNome.grabFocus();
        getRootPane().setDefaultButton(btnCad);

    }

    public FuncionariosForm(String id) {
        initComponents();
        con = ModuloConexao.conector();
        this.id = id;
        this.type = "upd";
        this.setTitle("EDITAR INFORMAÇÕES");
        getRootPane().setDefaultButton(btnCad);
        txtNome.grabFocus();
        txtRg.setDocument(new AgilitySec.onlyNumbers());
        txtCtpsNum.setDocument(new AgilitySec.onlyNumbers());
        txtCtpsSer.setDocument(new AgilitySec.onlyNumbers());
        txtNis.setDocument(new AgilitySec.onlyNumbers());
        txtNum.setDocument(new AgilitySec.onlyNumbers());
        txtCurso1Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso2Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso3Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso4Ano.setDocument(new AgilitySec.onlyNumbers());
        txtCurso5Ano.setDocument(new AgilitySec.onlyNumbers());
        txtTel1.setDocument(new AgilitySec.onlyNumbers());
        txtTel2.setDocument(new AgilitySec.onlyNumbers());
        listData();

    }

    private void listData() {
        String occ_id;
        try {
            rs = con.prepareStatement("SELECT id, title FROM occupations WHERE status=1").executeQuery();
            while (rs.next()) {
                occs_id.add(rs.getString("id"));
                cboCargo.addItem(rs.getString("title"));
            }
            cboCargo.setSelectedIndex(-1);

            if (this.type.equals("upd")) {
                //CARREGA DADOS
                rs = con.prepareStatement("SELECT * FROM employees WHERE id='" + this.id + "'").executeQuery();
                rs.next();
                this.addr_id = rs.getString("adress_id");
                this.occ_id = rs.getString("occupation_id");

                txtNome.setText(rs.getString("name"));

                txtDataNasc.setText(rs.getString("dataNasc"));
                cboSexo.setSelectedItem(rs.getString("sexo"));
                txtCpf.setText(rs.getString("cpf"));
                txtRg.setText(rs.getString("rg"));
                txtRgData.setText(rs.getString("dataRg"));
                txtRgOrgao.setText(rs.getString("orgaoRg"));
                txtTel1.setText(rs.getString("tel1"));
                txtTel2.setText(rs.getString("tel2"));
                cboEstCivil.setSelectedItem(rs.getString("estCivil"));
                txtNis.setText(rs.getString("nis"));
                txtCtpsNum.setText(rs.getString("ctps").replaceAll("[^0-9]", ""));
                txtCtpsSer.setText(rs.getString("ctps_serie"));
                cboCtpsUf.setSelectedItem(rs.getString("ctps_uf"));
                txtEmail.setText(rs.getString("email"));
                cboFormaCont.setSelectedItem(rs.getString("form_contrato"));
                txtDataAdmi.setText(rs.getString("date_admission"));

                //PRIMEIRO EMPREGO
                if (rs.getString("first_job").equals("SIM")) {
                    rbSim.setSelected(true);
                } else {
                    rbNao.setSelected(true);
                }

                //FORMAÇÃO
                rs = con.prepareStatement("SELECT * FROM employees_education WHERE employee_id='" + this.id + "'").executeQuery();
                rs.last();
                int qtd = rs.getRow();
                rs.absolute(1);
                txtCurso1.setText(rs.getString("course"));
                txtCurso1Inst.setText(rs.getString("instituition"));
                txtCurso1Ano.setText(rs.getString("concYear"));

                if (qtd > 1) {
                    rs.next();
                    txtCurso2.setText(rs.getString("course"));
                    txtCurso2Inst.setText(rs.getString("instituition"));
                    txtCurso2Ano.setText(rs.getString("concYear"));
                }
                if (qtd > 2) {
                    rs.next();
                    txtCurso3.setText(rs.getString("course"));
                    txtCurso3Inst.setText(rs.getString("instituition"));
                    txtCurso3Ano.setText(rs.getString("concYear"));
                }
                if (qtd > 3) {
                    rs.next();
                    txtCurso4.setText(rs.getString("course"));
                    txtCurso4Inst.setText(rs.getString("instituition"));
                    txtCurso4Ano.setText(rs.getString("concYear"));
                }
                if (qtd > 4) {
                    rs.next();
                    txtCurso5.setText(rs.getString("course"));
                    txtCurso5Inst.setText(rs.getString("instituition"));
                    txtCurso5Ano.setText(rs.getString("concYear"));
                }
                //ENDEREÇO
                rs = con.prepareStatement("SELECT * FROM adresses WHERE id='" + this.addr_id + "'").executeQuery();
                rs.next();
                txtCep.setText(rs.getString("cep"));
                txtLogra.setText(rs.getString("logra"));
                txtNum.setText(rs.getString("num"));
                txtCompl.setText(rs.getString("compl"));
                txtBairro.setText(rs.getString("bairro"));
                txtMuni.setText(rs.getString("muni"));
                txtRef.setText(rs.getString("refer"));
                cboUf.setSelectedItem(rs.getString("uf"));

                //CARGO
                rs = con.prepareStatement("SELECT title FROM occupations WHERE id='" + this.occ_id + "'").executeQuery();
                rs.next();
                cboCargo.setSelectedItem(rs.getString("title"));

            }

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1055", ex);
        }
    }

    private String formatTel(JTextField field) {
        String tel = field.getText();
        String r = "";
        switch (tel.length()) {
            case 8:
                r = "(85) " + tel.substring(0, 4) + " - " + tel.substring(4, 8);
                break;
            case 9:
                r = "(85) " + tel.substring(0, 5) + " - " + tel.substring(5, 9);
                break;
            case 10:
                r = "(" + tel.substring(0, 2) + ") " + tel.substring(2, 6) + " - " + tel.substring(6, 10);
                break;
            case 11:
                r = "(" + tel.substring(0, 2) + ") " + tel.substring(2, 7) + " - " + tel.substring(7, 11);
                break;
        }
        return r;
    }

    private boolean check() {
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
        boolean r = false;
        if (txtNome.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtNome, "Nome Completo");
        } else if (txtDataNasc.getText().equals("  /  /    ")) {
            AgilitySec.checkMessageError(this, txtDataNasc, "Data de Nascimento");
        } else if (cboEstCivil.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboEstCivil, "Estado Civil");
        } else if (cboSexo.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboSexo, "Sexo");
        } else if (txtTel1.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtTel1, "Telefone 1");
        } else if (txtTel1.getText().length() < 8) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha corretamente o campo \"Telefone 1\".");
            txtTel1.grabFocus();
        } else if (!txtTel2.getText().equals("") && txtTel2.getText().length() < 8) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha corretamente o campo \"Telefone 2\".");
            txtTel2.grabFocus();
        } else if (!rbSim.isSelected() && !rbNao.isSelected()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção no campo \"Primeiro Emprego\".");
        } else if (cboCargo.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboCargo, "Cargo");
        } else if (txtDataAdmi.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtDataAdmi, "Data de Admissão");
        } else if (cboFormaCont.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboFormaCont, "Forma de Contrato");
        } else if (txtRg.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtRg, "RG");
        } else if (txtRgData.getText().equals("  /  /    ")) {
            AgilitySec.checkMessageError(this, txtRgData, "Data de Expedição (RG)");
        } else if (txtRgOrgao.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtRgOrgao, "Orgão Expedidor (RG)");
        } else if (txtCpf.getText().equals("   .   .   -  ")) {
            AgilitySec.checkMessageError(this, txtCpf, "CPF");
        } else if (txtCtpsNum.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtCtpsNum, "CTPS (Número)");
        } else if (txtCtpsSer.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtCtpsSer, "CTPS (Série)");
        } else if (cboCtpsUf.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboCtpsUf, "CTPS (UF)");
        } else if (txtLogra.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtLogra, "Logradouro");
        } else if (txtNum.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtNum, "Número (Endereço)");
        } else if (txtBairro.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtBairro, "Bairro");
        } else if (cboUf.getSelectedIndex() == -1) {
            AgilitySec.checkMessageError(this, cboUf, "UF (Endereço)");
        } else if (txtMuni.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtMuni, "Município");
        } else if (txtCurso1.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtCurso1, "Curso");
        } else if (txtCurso1Inst.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtCurso1Inst, "Instituição de Ensino");
        } else if (txtCurso1Ano.getText().equals("")) {
            AgilitySec.checkMessageError(this, txtCurso1Ano, "Ano de Conclusão");
        } else {
            r = true;
        }
        //Cursos
        boolean cur2 = true, cur3 = true, cur4 = true, cur5 = true;
        if (!txtCurso2.getText().equals("")) {
            //VALIDA CURSO 2

            if (txtCurso2Inst.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso2Inst, "Instituição de Ensino (Curso 2)");
                cur2 = false;
            } else if (txtCurso2Ano.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso2Ano, "Ano de Conclusão (Curso 2)");
                cur2 = false;
            }
        }
        if (!txtCurso3.getText().equals("")) {
            //VALIDA CURSO 3

            if (txtCurso3Inst.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso3Inst, "Instituição de Ensino (Curso 3)");
                cur3 = false;
            } else if (txtCurso3Ano.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso3Ano, "Ano de Conclusão (Curso 3)");
                cur3 = false;
            }
        }
        if (!txtCurso4.getText().equals("")) {
            //VALIDA CURSO 4

            if (txtCurso4Inst.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso4Inst, "Instituição de Ensino (Curso 4)");
                cur4 = false;
            } else if (txtCurso4Ano.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso4Ano, "Ano de Conclusão (Curso 4)");
                cur4 = false;
            }
        }
        if (!txtCurso5.getText().equals("")) {
            //VALIDA CURSO 5

            if (txtCurso5Inst.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso5Inst, "Instituição de Ensino (Curso 5)");
                cur5 = false;
            } else if (txtCurso5Ano.getText().equals("")) {
                AgilitySec.checkMessageError(this, txtCurso5Ano, "Ano de Conclusão (Curso 5)");
                cur5 = false;
            }
        }

        if (cur2 && cur3 && cur4 && cur5 && r) {
            r = true;
            dateBr.setLenient(false);
            //VALIDA DATA DE NASC
            try {
                dateBr.parse(txtDataNasc.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Nascimento\".");
                txtDataNasc.grabFocus();
                r = false;
            }
            //VALIDA DATA DE EXPEDIÇÃO
            try {
                dateBr.parse(txtRgData.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Expedição\".");
                txtRgData.grabFocus();
                r = false;
            }
            //VALIDA DATA DE ADMISSÃO
            try {
                dateBr.parse(txtDataAdmi.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Admissão\".");
                txtDataAdmi.grabFocus();
                r = false;
            }
        }

        return r;
    }

    private void save() {
        String sqlEmp, sqlAddr, msg, education, first_job;
        Date now = new Date();
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        education = txtCurso1.getText() + "," + txtCurso1Inst.getText() + "," + txtCurso1Ano.getText() + ";";
        first_job = (rbSim.isSelected()) ? "SIM" : "NÃO";

        if (!txtCurso2.getText().equals("")) {
            education += txtCurso2.getText() + "," + txtCurso2Inst.getText() + "," + txtCurso2Ano.getText() + ";";
        }
        if (!txtCurso3.getText().equals("")) {
            education += txtCurso3.getText() + "," + txtCurso3Inst.getText() + "," + txtCurso3Ano.getText() + ";";
        }
        if (!txtCurso4.getText().equals("")) {
            education += txtCurso4.getText() + "," + txtCurso4Inst.getText() + "," + txtCurso4Ano.getText() + ";";
        }
        if (!txtCurso5.getText().equals("")) {
            education += txtCurso5.getText() + "," + txtCurso5Inst.getText() + "," + txtCurso5Ano.getText() + ";";
        }

        if (this.type.equals("new")) {
            msg = "Dados cadastrados com sucesso!";

            //SALVA ENDEREÇO
            sqlAddr = "INSERT INTO adresses SET cep=?, logra=?, num=?, compl=?, bairro=?, muni=?, refer=?, uf=?, created_by=?";

            try {
                pst = con.prepareStatement(sqlAddr, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, txtCep.getText());
                pst.setString(2, txtLogra.getText());
                pst.setString(3, txtNum.getText());
                pst.setString(4, txtCompl.getText());
                pst.setString(5, txtBairro.getText());
                pst.setString(6, txtMuni.getText());
                pst.setString(7, txtRef.getText());
                pst.setString(8, cboUf.getSelectedItem().toString());
                pst.setString(9, Login.emp_id);
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    addr_id = rs.getString(1);
                }
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1056", ex);
                this.dispose();
            }

            //SALVA FUNCIONÁRIO
            sqlEmp = "INSERT INTO employees SET "
                    + "name='" + txtNome.getText() + "',"
                    + "dataNasc='" + txtDataNasc.getText() + "',"
                    + "sexo='" + cboSexo.getSelectedItem() + "',"
                    + "cpf='" + txtCpf.getText() + "',"
                    + "rg='" + txtRg.getText() + "',"
                    + "dataRg='" + txtRgData.getText() + "',"
                    + "orgaoRg='" + txtRgOrgao.getText() + "',"
                    + "tel1='" + formatTel(txtTel1) + "',"
                    + "tel2='" + formatTel(txtTel2) + "',"
                    + "estCivil='" + cboEstCivil.getSelectedItem() + "',"
                    + "nis='" + txtNis.getText() + "',"
                    + "ctps='" + txtCtpsNum.getText() + "',"
                    + "ctps_serie='" + txtCtpsSer.getText() + "',"
                    + "ctps_uf='" + cboCtpsUf.getSelectedItem() + "',"
                    + "email='" + txtEmail.getText() + "',"
                    + "first_job='" + first_job + "',"
                    + "form_contrato='" + cboFormaCont.getSelectedItem() + "',"
                    + "date_admission='" + txtDataAdmi.getText() + "',"
                    + "adress_id='" + addr_id + "',"
                    + "occupation_id='" + occs_id.toArray()[cboCargo.getSelectedIndex()] + "',"
                    + "created='" + dateBr.format(now) + "',"
                    + "created_by='" + Login.emp_id + "'";
            try {
                pst = con.prepareStatement(sqlEmp, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                emp_id = rs.getString(1);
            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1058", ex);
            }

            //SALVA FORMAÇÃO
            String[] eduSql;
            eduSql = education.split(";");
            boolean r = true;
            for (String data : eduSql) {
                try {
                    con.prepareStatement("INSERT INTO employees_education SET "
                            + "course='" + data.split(",")[0] + "', "
                            + "instituition='" + data.split(",")[1] + "', "
                            + "concYear='" + data.split(",")[2] + "', "
                            + "employee_id='" + emp_id + "'").executeUpdate();

                } catch (SQLException e) {
                    r = false;
                    AgilitySec.showError(this, "1143", e);
                }
            }
            if (r) {
                this.modified = true;
                this.dispose();
                JOptionPane.showMessageDialog(this, msg);
            }
        } else {
            msg = "Dados atualizados com sucesso!";
            //ATUALIZA ENDEREÇO
            sqlAddr = "UPDATE adresses SET cep=?, logra=?, num=?, compl=?, bairro=?, muni=?, refer=?, uf=?, modified_by=? WHERE id='" + addr_id + "'";
            try {
                pst = con.prepareStatement(sqlAddr, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, txtCep.getText());
                pst.setString(2, txtLogra.getText());
                pst.setString(3, txtNum.getText());
                pst.setString(4, txtCompl.getText());
                pst.setString(5, txtBairro.getText());
                pst.setString(6, txtMuni.getText());
                pst.setString(7, txtRef.getText());
                pst.setString(8, cboUf.getSelectedItem().toString());
                pst.setString(9, Login.emp_id);
                pst.executeUpdate();
                System.out.println("Salvou endereço");

            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1057", ex);
                this.dispose();
            }
            //ATUALIZA FUNCIONÁRIO
            sqlEmp = "UPDATE employees SET "
                    + "name='" + txtNome.getText() + "',"
                    + "dataNasc='" + txtDataNasc.getText() + "',"
                    + "sexo='" + cboSexo.getSelectedItem() + "',"
                    + "cpf='" + txtCpf.getText() + "',"
                    + "rg='" + txtRg.getText() + "',"
                    + "dataRg='" + txtRgData.getText() + "',"
                    + "orgaoRg='" + txtRgOrgao.getText() + "',"
                    + "tel1='" + formatTel(txtTel1) + "',"
                    + "tel2='" + formatTel(txtTel2) + "',"
                    + "estCivil='" + cboEstCivil.getSelectedItem() + "',"
                    + "nis='" + txtNis.getText() + "',"
                    + "ctps='" + txtCtpsNum.getText() + "',"
                    + "ctps_serie='" + txtCtpsSer.getText() + "',"
                    + "ctps_uf='" + cboCtpsUf.getSelectedItem() + "',"
                    + "email='" + txtEmail.getText() + "',"
                    + "first_job='" + first_job + "',"
                    + "form_contrato='" + cboFormaCont.getSelectedItem() + "',"
                    + "date_admission='" + txtDataAdmi.getText() + "',"
                    + "adress_id='" + addr_id + "',"
                    + "occupation_id='" + occs_id.toArray()[cboCargo.getSelectedIndex()] + "',"
                    + "modified='" + dateBr.format(now) + "',"
                    + "modified_by='" + Login.emp_id + "' "
                    + "WHERE id='" + this.id + "'";
            try {
                con.prepareStatement(sqlEmp).executeUpdate();
                System.out.println("atualizou funcionário");

            } catch (SQLException ex) {
                AgilitySec.showError(this, "#1142", ex);
            }

            //ATUALIZA FORMAÇÃO
            String[] eduSql;
            eduSql = education.split(";");
            boolean r = true;
            //deleta formação antiga
            try {
                con.prepareStatement("DELETE FROM employees_education WHERE employee_id='" + this.id + "'").executeUpdate();

            } catch (SQLException e) {
                AgilitySec.showError(this, "#1144", e);
            }
            //adiciona formação nova
            for (String data : eduSql) {
                try {
                    pst = con.prepareStatement("INSERT INTO employees_education SET "
                            + "course='" + data.split(",")[0] + "', "
                            + "instituition='" + data.split(",")[1] + "', "
                            + "concYear='" + data.split(",")[2] + "', "
                            + "employee_id='" + this.id + "'");
                    pst.executeUpdate();
                    System.out.println(pst);
                    System.out.println("atualizou formação");

                } catch (SQLException e) {
                    r = false;
                    AgilitySec.showError(this, "1143", e);
                }
            }
            if (r) {
                this.modified = true;
                this.dispose();
                JOptionPane.showMessageDialog(this, msg);
            }
        }
    }

    private void cepInvalido() {
        txtCep.setValue(null);
        if (!txtLogra.isEnabled()) {
            txtBairro.setText(null);
            txtBairro.setEnabled(true);
            txtLogra.setText(null);
            txtLogra.setEnabled(true);
            txtMuni.setText(null);
            txtMuni.setEnabled(true);
            cboUf.setSelectedItem("CE");
            cboUf.setEnabled(true);
        }
        txtLogra.grabFocus();
    }

    private void buscaCep() {
        if (!txtCep.getText().equals("")) {
            WebServiceCep cep = WebServiceCep.searchCep(txtCep.getText());
            if (cep.wasSuccessful()) {
                txtBairro.setText(cep.getBairro());
                txtBairro.setEnabled(false);
                txtLogra.setText(cep.getLogradouroFull());
                txtLogra.setEnabled(false);
                txtMuni.setText(cep.getCidade());
                txtMuni.setEnabled(false);
                cboUf.setSelectedItem(cep.getUf());
                cboUf.setEnabled(false);
                txtNum.grabFocus();
            } else {
                JOptionPane.showMessageDialog(null, "CEP inválido.");
                cepInvalido();
            }
        } else {
            cepInvalido();
        }
    }

    private String inputData() {
        String dateResi = JOptionPane.showInputDialog(this, "Digite a data de demissão");
        return dateResi;
    }

    private void desativ() {
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dSql = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        boolean valid = false;
        df.setLenient(false);
        String inputData = "";

        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja desligar este funcionário?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            while (!valid) {
                inputData = inputData();
                try {
                    df.parse(inputData);
                    valid = true;
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data inválida.\nPor favor, digite uma data válida no formato \"xx/xx/xxxx\"");
                    valid = false;
                }
            }
            if (valid) {
                try {
                    pst = con.prepareStatement("UPDATE employees SET date_resignation='" + inputData + "', modified='" + dSql.format(now) + "', modified_by='" + Login.emp_id + "', status=0 WHERE id='" + this.id + "'");
                    if (pst.executeUpdate() > 0) {
                        this.modified = true;
                        this.dispose();
                        JOptionPane.showMessageDialog(this, "Funcionário desligado com sucesso.");
                    }
                } catch (SQLException ex) {
                    AgilitySec.showError(this, "#1063", ex);
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTel1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTel2 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cboEstCivil = new javax.swing.JComboBox<>();
        cboSexo = new javax.swing.JComboBox<>();
        rbSim = new javax.swing.JRadioButton();
        rbNao = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        cboCargo = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        cboFormaCont = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        txtRg = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtRgOrgao = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txtCtpsNum = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtCtpsSer = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtNis = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtLogra = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtRef = new javax.swing.JTextField();
        cboUf = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtNum = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtCompl = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtMuni = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        cboCtpsUf = new javax.swing.JComboBox<>();
        txtDataNasc = new javax.swing.JFormattedTextField();
        txtRgData = new javax.swing.JFormattedTextField();
        txtCpf = new javax.swing.JFormattedTextField();
        loadCep = new javax.swing.JButton();
        btnLimpCep = new javax.swing.JButton();
        txtCep = new javax.swing.JFormattedTextField();
        jLabel70 = new javax.swing.JLabel();
        btnHoje = new javax.swing.JButton();
        txtDataAdmi = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtCurso1 = new javax.swing.JTextField();
        txtCurso1Inst = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtCurso2 = new javax.swing.JTextField();
        txtCurso2Inst = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtCurso3 = new javax.swing.JTextField();
        txtCurso3Inst = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtCurso4Inst = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCurso4 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCurso4Ano = new javax.swing.JTextField();
        txtCurso1Ano = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCurso2Ano = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtCurso3Ano = new javax.swing.JTextField();
        txtCurso5 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        txtCurso5Inst = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtCurso5Ano = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        txtCan = new javax.swing.JButton();
        btnCad = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVO FUNCIONÁRIO");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setToolTipText("");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Nome Completo:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Data de Nascimento:");

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Estado Civil:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Sexo:");

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Telefone 1:");

        jLabel28.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel28.setText("Telefone 2:");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setText("Email:");

        jLabel16.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel16.setText("Primeiro emprego?");

        cboEstCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        cboEstCivil.setSelectedIndex(-1);

        cboSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        cboSexo.setSelectedIndex(-1);

        buttonGroup1.add(rbSim);
        rbSim.setText("SIM");

        buttonGroup1.add(rbNao);
        rbNao.setText("NÃO");

        jLabel27.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel27.setText("Cargo:");

        cboCargo.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                cboCargoPopupMenuWillBecomeVisible(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel25.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel25.setText("Forma de Contrato:");

        cboFormaCont.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLT - Carteira Assinada", "Estágio", "Jovem Aprendiz", "Contratação Temporária", "Terceirizado" }));
        cboFormaCont.setSelectedIndex(-1);

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("RG:");

        jLabel29.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel29.setText("Data de Expedição:");

        jLabel30.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel30.setText("Orgão Emissor:");

        jLabel31.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel31.setText("CPF:");

        jLabel32.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel32.setText("CTPS (Número):");

        jLabel33.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel33.setText("CTPS (Série):");

        jLabel34.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel34.setText("CTPS (UF):");

        jLabel35.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel35.setText("NIS:");

        jLabel36.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel36.setText("Data de Admissão:");

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel26.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel26.setText("<html><u>CEP:</u>");
        jLabel26.setToolTipText("");

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel14.setText("Logradouro:");

        jLabel37.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(233, 2, 2));
        jLabel37.setText("*");

        jLabel38.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel38.setText("Bairro:");

        jLabel39.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(233, 2, 2));
        jLabel39.setText("*");

        jLabel40.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel40.setText("Referência:");

        cboUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        cboUf.setSelectedIndex(-1);

        jLabel41.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel41.setText("UF:");

        jLabel42.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(233, 2, 2));
        jLabel42.setText("*");

        jLabel43.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel43.setText("Número:");

        jLabel44.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(233, 2, 2));
        jLabel44.setText("*");

        jLabel45.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel45.setText("Complemento:");

        jLabel46.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel46.setText("Município:");

        jLabel47.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(233, 2, 2));
        jLabel47.setText("*");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1491566401_windows.png"))); // NOI18N

        jLabel51.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(233, 2, 2));
        jLabel51.setText("*");

        jLabel52.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(233, 2, 2));
        jLabel52.setText("*");

        jLabel53.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(233, 2, 2));
        jLabel53.setText("*");

        jLabel54.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(233, 2, 2));
        jLabel54.setText("*");

        jLabel55.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(233, 2, 2));
        jLabel55.setText("*");

        jLabel56.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(233, 2, 2));
        jLabel56.setText("*");

        jLabel57.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(233, 2, 2));
        jLabel57.setText("*");

        jLabel58.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(233, 2, 2));
        jLabel58.setText("*");

        jLabel60.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(233, 2, 2));
        jLabel60.setText("*");

        jLabel61.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(233, 2, 2));
        jLabel61.setText("*");

        jLabel62.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(233, 2, 2));
        jLabel62.setText("*");

        jLabel63.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(233, 2, 2));
        jLabel63.setText("*");

        jLabel64.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(233, 2, 2));
        jLabel64.setText("*");

        jLabel65.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(233, 2, 2));
        jLabel65.setText("*");

        jLabel66.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(233, 2, 2));
        jLabel66.setText("*");

        jLabel59.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(233, 2, 2));
        jLabel59.setText("*");

        cboCtpsUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        cboCtpsUf.setSelectedIndex(-1);

        try {
            txtDataNasc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtRgData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtRgData.setText("1 /  /    ");

        try {
            txtCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        loadCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/load.png"))); // NOI18N
        loadCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCepActionPerformed(evt);
            }
        });

        btnLimpCep.setText("Limpar");
        btnLimpCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpCepActionPerformed(evt);
            }
        });

        try {
            txtCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCepFocusLost(evt);
            }
        });
        txtCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCepActionPerformed(evt);
            }
        });

        jLabel70.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(233, 2, 2));
        jLabel70.setText("*Caso haja dúvidas, deixe em branco.");

        btnHoje.setText("Hoje");
        btnHoje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHojeActionPerformed(evt);
            }
        });

        try {
            txtDataAdmi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataAdmi.setText("1 /  /    ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2))
                        .addGap(16, 16, 16))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtRef, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel38)
                                                    .addGap(2, 2, 2)
                                                    .addComponent(jLabel39)
                                                    .addGap(219, 219, 219))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(txtBairro, javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                            .addComponent(jLabel14)
                                                            .addGap(2, 2, 2)
                                                            .addComponent(jLabel37))
                                                        .addComponent(txtLogra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                            .addComponent(jLabel43)
                                                            .addGap(2, 2, 2)
                                                            .addComponent(jLabel44))
                                                        .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel45)
                                                        .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                            .addComponent(jLabel41)
                                                            .addGap(2, 2, 2)
                                                            .addComponent(jLabel42)))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                            .addComponent(jLabel46)
                                                            .addGap(2, 2, 2)
                                                            .addComponent(jLabel47))
                                                        .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(loadCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(btnLimpCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(147, 147, 147))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel52)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtNome)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel51))
                                            .addComponent(txtTel1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel28)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel36)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel65))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(btnHoje, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(txtTel2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel27)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel64)
                                                .addGap(294, 294, 294))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(cboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtDataAdmi, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(74, 74, 74)))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel25)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel66))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel15)
                                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel16)
                                                        .addGap(2, 2, 2)
                                                        .addComponent(jLabel59))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(rbSim)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(rbNao))))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addGap(2, 2, 2)
                                                        .addComponent(jLabel53))
                                                    .addComponent(txtDataNasc))
                                                .addGap(4, 4, 4)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(8, 8, 8)
                                                        .addComponent(jLabel10)
                                                        .addGap(2, 2, 2)
                                                        .addComponent(jLabel55))
                                                    .addComponent(cboEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addGap(2, 2, 2)
                                                        .addComponent(jLabel54))
                                                    .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(84, 84, 84))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cboFormaCont, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCtpsNum, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel32)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel61)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCtpsSer, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel33)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel62)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel34)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel63))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(cboCtpsUf, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtNis, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel56)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel29)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel57))
                                            .addComponent(txtRgData, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtRgOrgao, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel30)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel58)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel31)
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel60))
                                            .addComponent(jLabel35)
                                            .addComponent(txtCpf)))
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel70))
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel52))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDataNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel53)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel3)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addGap(34, 34, 34)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel51)
                                    .addComponent(jLabel28))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rbSim)
                                    .addComponent(rbNao)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel59))
                        .addGap(33, 33, 33)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel27)
                                .addComponent(jLabel64))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cboCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDataAdmi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(13, 13, 13))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel36)
                                .addComponent(jLabel65))
                            .addGap(45, 45, 45)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel66))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboFormaCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHoje))
                        .addGap(14, 14, 14)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel58))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtRgOrgao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel56))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRgData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel57))
                                .addGap(33, 33, 33))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(jLabel60))
                        .addGap(33, 33, 33)))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(jLabel35))
                            .addComponent(jLabel63, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel61))
                        .addGap(7, 7, 7)
                        .addComponent(txtCtpsNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(jLabel62))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCtpsSer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboCtpsUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel70)
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnLimpCep)
                                .addComponent(loadCep)))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37)
                            .addComponent(jLabel44)
                            .addComponent(jLabel43)
                            .addComponent(jLabel45))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLogra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel42))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel38)
                                    .addComponent(jLabel39))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel46)
                                    .addComponent(jLabel47))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4))
                .addGap(10, 10, 10)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados Pessoais", jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setOpaque(false);

        jLabel17.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel17.setText("Curso:");

        jLabel18.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel18.setText("Instituição de Ensino:");

        jLabel19.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel19.setText("Curso:");

        jLabel20.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel20.setText("Instituição de Ensino:");

        jLabel21.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel21.setText("Curso:");

        jLabel22.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel22.setText("Instituição de Ensino:");

        jLabel23.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel23.setText("Instituição de Ensino:");

        jLabel24.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel24.setText("Curso:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Ano de Conclusão:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Ano de Conclusão:");

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("Ano de Conclusão:");

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Ano de Conclusão:");

        jLabel48.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel48.setText("Curso:");

        jLabel49.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel49.setText("Instituição de Ensino:");

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel13.setText("Ano de Conclusão:");

        jLabel67.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(233, 2, 2));
        jLabel67.setText("*");

        jLabel68.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(233, 2, 2));
        jLabel68.setText("*");

        jLabel69.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(233, 2, 2));
        jLabel69.setText("*");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21)
                            .addComponent(jLabel24)
                            .addComponent(jLabel48))
                        .addGap(278, 278, 278)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23)
                            .addComponent(jLabel49)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCurso4)
                            .addComponent(txtCurso3)
                            .addComponent(txtCurso2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel67))
                            .addComponent(txtCurso1)
                            .addComponent(txtCurso5, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel68))
                                    .addComponent(txtCurso1Inst, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                                    .addComponent(txtCurso2Inst))
                                .addComponent(txtCurso3Inst, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCurso4Inst, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCurso5Inst, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel69))
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(txtCurso2Ano, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addComponent(txtCurso3Ano)
                    .addComponent(txtCurso4Ano)
                    .addComponent(txtCurso5Ano)
                    .addComponent(txtCurso1Ano))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel67))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCurso1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel68)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel69))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCurso1Inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCurso1Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCurso2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCurso2Inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurso2Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCurso3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCurso3Inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCurso3Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jLabel5))
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCurso4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCurso4Inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCurso4Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(jLabel13))
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCurso5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCurso5Inst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCurso5Ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(279, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Formação", jPanel4);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 940, 610));

        txtCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        txtCan.setText("Cancelar");
        txtCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCanActionPerformed(evt);
            }
        });
        getContentPane().add(txtCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 630, 150, -1));

        btnCad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnCad.setText("Cadastrar");
        btnCad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadActionPerformed(evt);
            }
        });
        getContentPane().add(btnCad, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 630, 150, -1));

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDel.setText("Desligar Funcionário");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 200, -1));

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 960, 690));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadActionPerformed
        if (check()) {
            save();
        }
    }//GEN-LAST:event_btnCadActionPerformed

    private void txtCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_txtCanActionPerformed

    private void cboCargoPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCargoPopupMenuWillBecomeVisible

    }//GEN-LAST:event_cboCargoPopupMenuWillBecomeVisible

    private void loadCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCepActionPerformed
        if (!txtCep.getText().equals("  .   -   ")) {
            buscaCep();
        }
    }//GEN-LAST:event_loadCepActionPerformed

    private void btnLimpCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpCepActionPerformed
        txtCep.setValue(null);
        if (!txtLogra.isEnabled()) {
            txtBairro.setText(null);
            txtBairro.setEnabled(true);
            txtLogra.setText(null);
            txtLogra.setEnabled(true);
            txtMuni.setText(null);
            txtMuni.setEnabled(true);
            cboUf.setSelectedItem("CE");
            cboUf.setEnabled(true);
        }
        txtLogra.grabFocus();
    }//GEN-LAST:event_btnLimpCepActionPerformed

    private void txtCepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCepFocusLost
        buscaCep();
    }//GEN-LAST:event_txtCepFocusLost

    private void txtCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepActionPerformed
        buscaCep();
    }//GEN-LAST:event_txtCepActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        desativ();
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnHojeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojeActionPerformed
        txtDataAdmi.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }//GEN-LAST:event_btnHojeActionPerformed

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
            java.util.logging.Logger.getLogger(FuncionariosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FuncionariosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FuncionariosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FuncionariosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FuncionariosForm dialog = new FuncionariosForm();
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
    private javax.swing.JButton btnCad;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnHoje;
    private javax.swing.JButton btnLimpCep;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboCargo;
    private javax.swing.JComboBox<String> cboCtpsUf;
    private javax.swing.JComboBox<String> cboEstCivil;
    private javax.swing.JComboBox<String> cboFormaCont;
    private javax.swing.JComboBox<String> cboSexo;
    private javax.swing.JComboBox<String> cboUf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadCep;
    private javax.swing.JRadioButton rbNao;
    private javax.swing.JRadioButton rbSim;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JButton txtCan;
    private javax.swing.JFormattedTextField txtCep;
    private javax.swing.JTextField txtCompl;
    private javax.swing.JFormattedTextField txtCpf;
    private javax.swing.JTextField txtCtpsNum;
    private javax.swing.JTextField txtCtpsSer;
    private javax.swing.JTextField txtCurso1;
    private javax.swing.JTextField txtCurso1Ano;
    private javax.swing.JTextField txtCurso1Inst;
    private javax.swing.JTextField txtCurso2;
    private javax.swing.JTextField txtCurso2Ano;
    private javax.swing.JTextField txtCurso2Inst;
    private javax.swing.JTextField txtCurso3;
    private javax.swing.JTextField txtCurso3Ano;
    private javax.swing.JTextField txtCurso3Inst;
    private javax.swing.JTextField txtCurso4;
    private javax.swing.JTextField txtCurso4Ano;
    private javax.swing.JTextField txtCurso4Inst;
    private javax.swing.JTextField txtCurso5;
    private javax.swing.JTextField txtCurso5Ano;
    private javax.swing.JTextField txtCurso5Inst;
    private javax.swing.JFormattedTextField txtDataAdmi;
    private javax.swing.JFormattedTextField txtDataNasc;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtLogra;
    private javax.swing.JTextField txtMuni;
    private javax.swing.JTextField txtNis;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtRef;
    private javax.swing.JTextField txtRg;
    private javax.swing.JFormattedTextField txtRgData;
    private javax.swing.JTextField txtRgOrgao;
    private javax.swing.JTextField txtTel1;
    private javax.swing.JTextField txtTel2;
    // End of variables declaration//GEN-END:variables

}
