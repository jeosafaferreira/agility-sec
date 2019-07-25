package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.dal.WebServiceCep;
import Agility.api.AgilitySec;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import org.json.JSONObject;

/**
 *
 * @author jferreira
 */
public class WizardNovoAluno extends javax.swing.JDialog {

    int card = 1;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    List<String> responsibles = new ArrayList<>();
    DateFormat vData = new SimpleDateFormat("dd/MM/yyyy");
    int buscaRespFin, buscaRespAcad = 0;
    String idRespFin, idRespAcad = null;
    boolean modified = false;

    public WizardNovoAluno(JFrame parent) {
        super(parent);
        initComponents();
        btnBack.setVisible(false);
        con = ModuloConexao.conector();
    }

    private void next() {
        card++;
        CardLayout cl = (CardLayout) (parentPanel.getLayout());
        cl.next(parentPanel);
        if (card == 4) {
            ImageIcon finish = new ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"));
            btnNext.setText("Concluir");
            btnNext.setIcon(finish);
        }
    }

    private void back() {
        card--;
        CardLayout cl = (CardLayout) (parentPanel.getLayout());
        cl.previous(parentPanel);
        if (card != 4) {
            ImageIcon normal = new ImageIcon(getClass().getResource("/Agility/icones/1498991450_f-right_256.png"));
            btnNext.setText("Avançar");
            btnNext.setIcon(normal);
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
        WebServiceCep cep = WebServiceCep.searchCep(txtCep.getText());
        if (!txtCep.getText().equals("  .   -   ")) {
            if (!cep.hasException()) {
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
                JOptionPane.showMessageDialog(null, "Erro ao buscar CEP.");
                btnLimpCepActionPerformed(null);
            }
        }
    }

    private void buscaAluno() {
        String sql = "SELECT cod, name FROM students WHERE name = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, txtNomeAlu.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                int q = JOptionPane.showConfirmDialog(null, "Este aluno já possui cadastro nesta instituição. Deseja visualizar?", "Atenção!", JOptionPane.YES_NO_OPTION);
                if (q == JOptionPane.YES_OPTION) {
                    AlunosDetalhes a = new AlunosDetalhes(this, rs.getString("cod"));
                    a.setVisible(true);
                    this.dispose();
                } else {
                    txtNomeAlu.setText(null);
                    txtNomeAlu.grabFocus();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0072\nDetalhes do erro:\n" + e);
            this.dispose();
        }
    }

    private void buscaResp(String resp) {
        String sql = "SELECT * FROM responsibles WHERE name=?";
        try {
            pst = con.prepareStatement(sql);
            if (resp.equals("Fin")) {
                pst.setString(1, resp1Nome.getText());
            } else {
                pst.setString(1, resp2Nome.getText());
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                SimpleDateFormat maskDate = new SimpleDateFormat("dd/MM/yyyy");
                int q = JOptionPane.showConfirmDialog(parentPanel, "Esse responsável já possui cadastro. Deseja carregar dados?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (q == JOptionPane.YES_OPTION) {
                    if (resp.equals("Fin")) {
                        buscaRespFin = 1;
                        idRespFin = rs.getString("id");
                        resp1Nasc.setText(maskDate.format(rs.getDate("dataNasc")));
                        resp1Sexo.setSelectedItem(rs.getString("sexo"));
                        resp1Cpf.setText(rs.getString("cpf"));
                        resp1Rg.setText(rs.getString("rg"));
                        resp1DataRg.setText(maskDate.format(rs.getDate("dataRg")));
                        resp1OrgaoRg.setText(rs.getString("orgaoRg"));
                        resp1Tel1.setText(rs.getString("tel1"));
                        resp1Tel2.setText(rs.getString("tel2"));
                        resp1EstCivil.setSelectedItem(rs.getString("estCivil"));

                        resp1Nome.setEnabled(false);
                        resp1Nasc.setEnabled(false);
                        resp1Sexo.setEnabled(false);
                        resp1Cpf.setEnabled(false);
                        resp1Rg.setEnabled(false);
                        resp1DataRg.setEnabled(false);
                        resp1OrgaoRg.setEnabled(false);
                        resp1Tel1.setEnabled(false);
                        resp1Tel2.setEnabled(false);
                        resp1EstCivil.setEnabled(false);

                        resp2Nome.grabFocus();

                    } else {
                        buscaRespAcad = 1;
                        idRespAcad = rs.getString("id");
                        resp2Nasc.setText(maskDate.format(rs.getDate("dataNasc")));
                        resp2Sexo.setSelectedItem(rs.getString("sexo"));
                        resp2Cpf.setText(rs.getString("cpf"));
                        resp2Rg.setText(rs.getString("rg"));
                        resp2DataRg.setText(maskDate.format(rs.getDate("dataRg")));
                        resp2OrgaoRg.setText(rs.getString("orgaoRg"));;
                        resp2Tel1.setText(rs.getString("tel1"));;
                        resp2Tel2.setText(rs.getString("tel2"));;
                        resp2EstCivil.setSelectedItem(rs.getString("estCivil"));

                        resp2Nome.setEnabled(false);
                        resp2Nasc.setEnabled(false);
                        resp2Sexo.setEnabled(false);
                        resp2Cpf.setEnabled(false);
                        resp2Rg.setEnabled(false);
                        resp2DataRg.setEnabled(false);
                        resp2OrgaoRg.setEnabled(false);
                        resp2Tel1.setEnabled(false);
                        resp2Tel2.setEnabled(false);
                        resp2EstCivil.setEnabled(false);

                        btnNext.grabFocus();

                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro. (COD: #0010)\nContate o suporte técnico em (85) 98942-9672\nDetalhes do erro:" + e);
        }
    }

    private void card1() {
        next();
    }

    private void card2() {
        vData.setLenient(false);
        //Validação dos Campos
        if (txtNomeAlu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Nome do aluno\".");
            txtNomeAlu.grabFocus();

        } else if (txtRg.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"RG\".");
            txtRg.grabFocus();

        } else if (txtOrgaoRg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Orgão Emissor\".");
            txtOrgaoRg.grabFocus();

        } else if (txtTel.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Telefone\".");
            txtTel.grabFocus();

        } else if (cboEstCivil.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Estado Civil\".");
            cboEstCivil.grabFocus();

        } else if (cboSexo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Sexo\".");
            cboSexo.grabFocus();

        } else if (txtLogra.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Logradouro\".");
            txtLogra.grabFocus();

        } else if (txtNum.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Número\".");
            txtNum.grabFocus();

        } else if (txtBairro.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Bairro\".");
            txtBairro.grabFocus();

        } else if (cboUf.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"UF\".");
            cboUf.grabFocus();

        } else if (txtMuni.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Município\".");
            txtMuni.grabFocus();
        } else {
            int err = 0;
            try {
                vData.parse(txtNasc.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Nascimento\".");
                txtNasc.grabFocus();
                err = 1;
            }
            try {
                vData.parse(txtDataRg.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Expedição\".");
                txtDataRg.grabFocus();
                err = 1;
            }

            //Procura o cep conrespondente:
            if (txtCep.getText().equals("  .   -   ")) {
                String uf, muni, logra;
                uf = cboUf.getSelectedItem() + "";
                muni = txtMuni.getText();
                logra = txtLogra.getText();
                try {
                    JSONObject json = AgilitySec.readJsonFromUrl("https://viacep.com.br/ws/" + uf + "/" + muni + "/" + logra + "/json/");
                    txtCep.setText(json.get("cep").toString());
                    System.out.println("cep");

                } catch (Exception e) {
                    AgilitySec.showError(this, "#1158", e);
                    err = 1;
                }
            }

            if (err == 0) {
                next();
            }
        }
        //Validação dos Campos -----
    }

    private void card3() {
        vData.setLenient(false);
        //RESPONSÁVEL FINANCEIRO
        if (resp1Nome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o nome do responsável financeiro.");
            resp1Nome.grabFocus();
        } else if (resp1Sexo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Sexo\". (Responsável Financeiro)");
            resp1Sexo.grabFocus();

        } else if (resp1Cpf.getText().equals("   .   .   -  ")) { //Otimizar essa validação de cpf.
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"CPF\". (Responsável Financeiro)");
            resp1Cpf.grabFocus();

        } else if (resp1Rg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"RG\". (Responsável Financeiro)");
            resp1Rg.grabFocus();

        } else if (resp1OrgaoRg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Orgão Emissor\".");
            resp1OrgaoRg.grabFocus();

        } else if (resp1Tel1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Telefone 1\". (Responsável Financeiro)");
            resp1Tel1.grabFocus();

        } else if (resp1EstCivil.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Estado Civil\". (Responsável Financeiro)");
            resp1EstCivil.grabFocus();

        } else if (resp2Nome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o nome do responsável acadêmico.");
            resp2Nome.grabFocus();

        } else if (resp2Sexo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Sexo\". (Responsável Acadêmico)");
            resp2Sexo.grabFocus();
        } else if (resp2Cpf.getText().equals("   .   .   -  ")) { //Otimizar essa validação de cpf.
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"CPF\". (Responsável Acadêmico)");
            resp2Cpf.grabFocus();
        } else if (resp2Rg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"RG\". (Responsável Acadêmico)");
            resp2Rg.grabFocus();

        } else if (resp2OrgaoRg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Orgão Emissor\".");
            resp2OrgaoRg.grabFocus();

        } else if (resp2Tel1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Telefone 1\". (Responsável Acadêmico)");
            resp2Tel1.grabFocus();

        } else if (resp2EstCivil.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Estado Civil\". (Responsável Acadêmico)");
            resp2EstCivil.grabFocus();
        } else {
            int i = 0; //i == 0(Valor inicial); i == 1(Teve erro); i == 2(Tudo ok)
            while (i == 0) {
                try {
                    vData.parse(resp1Nasc.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Nascimento\". (Responsável Financeiro)");
                    resp1Nasc.grabFocus();
                    i = 1;
                    break;
                }
                try {
                    vData.parse(resp1DataRg.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Emissão\". (Responsável Financeiro)");
                    resp1DataRg.grabFocus();
                    i = 1;
                    break;
                }
                try {
                    vData.parse(resp2Nasc.getText());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Nascimento\". (Responsável Acadêmico)");
                    resp2Nasc.grabFocus();
                    i = 1;
                    break;
                }
                try {
                    vData.parse(resp2DataRg.getText());
                    i = 2;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Emissão\". (Responsável Acadêmico)");
                    resp2DataRg.grabFocus();
                    i = 1;
                    break;
                }

                if (i == 2) {
                    responsibles.removeAll(responsibles);
                    responsibles.add(resp1Nome.getText());
                    if (!cboDupli.isSelected()) {
                        responsibles.add(resp2Nome.getText());
                    }
                    DefaultComboBoxModel dcbom = new DefaultComboBoxModel(responsibles.toArray());
                    cboEmergCont.setModel(dcbom);
                    next();
                }
            }
        }
    }

    private void card4() {
        if (cboEmergCont.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, informe quem deverá ser contatado em caso de emergência.");
        } else {
            finish();
        }
    }

    private void finish() {
        int erroUpd = 0;
        int newCodStu = 0;
        int idStu = 0;
        String idAddr = null;
        String idPeopEmerg;

        //Pega id do novo aluno
        String sqlStu = "SELECT max(id) as id FROM students ";
        try {
            pst = con.prepareStatement(sqlStu);
            rs = pst.executeQuery();
            if (rs.next()) {
                idStu = rs.getInt(1) + 1;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao salvar este aluno. (COD: #0009)\nContate o suporte técnico em (85) 98942-9672\nDetalhes do erro:" + e);
        }

        //Salva endereços
        String sqlAddr = "INSERT INTO adresses(cep, logra, num, compl, bairro, muni, refer, uf, created_by) VALUES (?,?,?,?,?,?,?,?,?)";
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
            if (pst.executeUpdate() <= 0) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao salvar este aluno. (COD: #0004)\nContate o suporte técnico em (85) 98942-9672");
                erroUpd = 1; //Ocorreu erro
            }
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                idAddr = rs.getString(1);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao salvar este aluno. (COD:#0005)\nDetalhes: " + e + "\nContate o suporte técnico em (85) 98942-9672");
            erroUpd = 1;
        }

        //Salva responsáveis
        String sqlPeop = "INSERT INTO responsibles(name, dataNasc, sexo, cpf, rg, dataRg, orgaoRg, tel1, tel2, estCivil, created_by) VALUES (?,DATE_FORMAT(STR_TO_DATE(?, '%d/%m/%Y'), '%Y-%m-%d'),?,?,?,DATE_FORMAT(STR_TO_DATE(?, '%d/%m/%Y'), '%Y-%m-%d'),?,?,?,?,?)";
        try {
            if (buscaRespFin == 0) { //Se estiver igual a 0, é porque o responsável ainda não possui cadastro.
                pst = con.prepareStatement(sqlPeop, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, resp1Nome.getText());
                pst.setString(2, resp1Nasc.getText());
                pst.setString(3, resp1Sexo.getSelectedItem().toString());
                pst.setString(4, resp1Cpf.getText());
                pst.setString(5, resp1Rg.getText());
                pst.setString(6, resp1DataRg.getText());
                pst.setString(7, resp1OrgaoRg.getText());
                pst.setString(8, resp1Tel1.getText());
                pst.setString(9, resp1Tel2.getText());
                pst.setString(10, resp1EstCivil.getSelectedItem().toString());
                pst.setString(11, Login.emp_id);

                if (pst.executeUpdate() <= 0) {
                    JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro neste cadastro. (COD: #0006)\nContate o suporte técnico em (85) 98942-9672");
                    erroUpd = 1;
                }
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    idRespFin = rs.getString(1);
                    System.out.println("idRespFin: " + idRespFin);
                }
            }
            if (buscaRespAcad == 0) {
                if (!cboDupli.isSelected()) {
                    pst = con.prepareStatement(sqlPeop, PreparedStatement.RETURN_GENERATED_KEYS);
                    pst.setString(1, resp2Nome.getText());
                    pst.setString(2, resp2Nasc.getText());
                    pst.setString(3, resp2Sexo.getSelectedItem().toString());
                    pst.setString(4, resp2Cpf.getText());
                    pst.setString(5, resp2Rg.getText());
                    pst.setString(6, resp2DataRg.getText());
                    pst.setString(7, resp2OrgaoRg.getText());
                    pst.setString(8, resp2Tel1.getText());
                    pst.setString(9, resp2Tel2.getText());
                    pst.setString(10, resp2EstCivil.getSelectedItem().toString());
                    pst.setString(11, Login.emp_id);
                    if (pst.executeUpdate() <= 0) {
                        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro neste cadastro. (COD: #0007)\nContate o suporte técnico em (85) 98942-9672");
                        erroUpd = 1;
                    }
                    rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        idRespAcad = rs.getString(1);
                        System.out.println("idRespAcad: " + idRespAcad);
                    }
                } else {
                    idRespAcad = idRespFin;
                }
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#0008", e);
            erroUpd = 1;
        }

        //Salva aluno
        //Pega emp_id do Resp Emerg.
        if (cboEmergCont.getSelectedIndex() == 0) {
            idPeopEmerg = idRespFin;
            System.out.println("SelectedIndex == 1; idPeopEmerg: " + idRespFin);
        } else {
            idPeopEmerg = idRespAcad;
            System.out.println("SelectedIndex ! 1; idPeopEmerg: " + idRespAcad);
        }
        String sqlGetCod = "SELECT cod FROM students WHERE cod LIKE '%" + Calendar.getInstance().get(Calendar.YEAR) + "%'";
        sqlStu = "INSERT INTO students SET "
                + "cod=?, "
                + "name=?, "
                + "datanasc=DATE_FORMAT(STR_TO_DATE(?, '%d/%m/%Y'), '%Y-%m-%d'), "
                + "cpf=?,"
                + "rg=?,"
                + "orgaoRg=?,"
                + "dataRg=DATE_FORMAT(STR_TO_DATE(?, '%d/%m/%Y'), '%Y-%m-%d'), "
                + "email=?,"
                + "estCivil=?,"
                + "sexo=?,"
                + "tel=?,"
                + "bloodType=?,"
                + "medicines=?,"
                + "allergies=?,"
                + "medicalObs=?,"
                + "extraInfo=?,"
                + "adress_id=?,"
                + "respFin=?,"
                + "respAcad=?,"
                + "respEmerg=?,"
                + "created_by=?";
        try {
            rs = con.prepareStatement(sqlGetCod).executeQuery();
            if (rs.next()) {
                rs.last();
                newCodStu = rs.getInt("cod") + 1;
            } else {
                newCodStu = Calendar.getInstance().get(Calendar.YEAR) * 1000 + 1;
            }
            pst = con.prepareStatement(sqlStu);
            pst.setInt(1, newCodStu);
            pst.setString(2, txtNomeAlu.getText());
            pst.setString(3, txtNasc.getText());
            if (cboRespAlu.isSelected()) {
                pst.setString(4, resp1Cpf.getText());
                //Se o cbo estiver selecionado, pode ser que não tenham inserido cpf no card 2, mas obrigatoriamente tem que inserir no card 3.
            } else {
                pst.setString(4, txtCpf.getText());
            }
            pst.setString(5, txtRg.getText());
            pst.setString(6, txtOrgaoRg.getText());
            pst.setString(7, txtDataRg.getText());
            pst.setString(8, txtEmail.getText());
            pst.setString(9, cboEstCivil.getSelectedItem().toString());
            pst.setString(10, cboSexo.getSelectedItem().toString());
            pst.setString(11, txtTel.getText());
            if (cboBlood.getSelectedIndex() == -1) {
                pst.setString(12, null);
            } else {
                pst.setString(12, cboBlood.getSelectedItem().toString());
            }

            pst.setString(13, txtMed.getText());
            pst.setString(14, txtAllerg.getText());
            pst.setString(15, txtObsMed.getText());
            pst.setString(16, txtExtra.getText());
            pst.setString(17, idAddr);
            pst.setString(18, idRespFin);
            pst.setString(19, idRespAcad);
            pst.setString(20, idPeopEmerg);
            pst.setString(21, Login.emp_id);

            if (pst.executeUpdate() <= 0) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao salvar este aluno. (COD: #0002)\nContate o suporte técnico em (85) 98942-9672");
                erroUpd = 1; //Ocorreu erro
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao salvar este aluno. (COD:#0003)\nDetalhes: " + e + "\nContate o suporte técnico em (85) 98942-9672");
            erroUpd = 1;
        }

        if (erroUpd == 0) {

            int q = JOptionPane.showConfirmDialog(null, "Dados inseridos com sucesso! Deseja matricular este aluno?", "Atenção!", JOptionPane.YES_NO_OPTION);
            this.modified = true;
            if (q == JOptionPane.YES_OPTION) {
                AlunoMatricula alumat = new AlunoMatricula(new Home(), newCodStu);
                alumat.setVisible(true);
                this.dispose();
            } else {
                this.dispose();
            }
        }
    }

    private void sair() {
        if (card >= 2) {
            int exit = JOptionPane.showConfirmDialog(null, "Deseja realmente cancelar este cadastro?\nTodos os dados serão perdidos.", "Atenção", JOptionPane.YES_NO_OPTION);
            if (exit == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        parentPanel = new javax.swing.JPanel();
        jpCard1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jpCard2 = new javax.swing.JPanel();
        jpInfoPess = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNomeAlu = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtNasc = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtNasc = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        try{
            MaskFormatter cpf= new MaskFormatter("###.###.###-##");
            txtCpf = new JFormattedTextField(cpf);
        }
        catch (Exception e){
        }
        jLabel8 = new javax.swing.JLabel();
        cboEstCivil = new javax.swing.JComboBox<>();
        cboSexo = new javax.swing.JComboBox<>();
        jLabel49 = new javax.swing.JLabel();
        txtRg = new javax.swing.JTextField();
        txtOrgaoRg = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtDataRg = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtDataRg = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtTel = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jpEndereco = new javax.swing.JPanel();
        txtLogra = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtNum = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtCompl = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtMuni = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtRef = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cboUf = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        txtCep = new javax.swing.JFormattedTextField();
        jLabel55 = new javax.swing.JLabel();
        btnLimpCep = new javax.swing.JButton();
        loadCep = new javax.swing.JButton();
        jpCard3 = new javax.swing.JPanel();
        jpResp1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        resp1Nome = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        resp1Rg = new javax.swing.JTextField();
        resp1Cpf = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        resp1Tel1 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        resp1Tel2 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        resp1Nasc = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            resp1Nasc = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        resp1DataRg = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            resp1DataRg = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        jLabel37 = new javax.swing.JLabel();
        resp1EstCivil = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        resp1Sexo = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        resp1OrgaoRg = new javax.swing.JTextField();
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
        cboRespAlu = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        btnLimpFin = new javax.swing.JButton();
        jpResp2 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        resp2Nome = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        resp2Rg = new javax.swing.JTextField();
        resp2Cpf = new javax.swing.JTextField();
        try{    MaskFormatter cpf= new MaskFormatter("###.###.###-##");    resp2Cpf = new JFormattedTextField(cpf); }    catch (Exception e){ }
        jLabel45 = new javax.swing.JLabel();
        resp2Tel1 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        resp2Tel2 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        resp2Nasc = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            resp2Nasc = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        resp2DataRg = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            resp2DataRg = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        jLabel48 = new javax.swing.JLabel();
        resp2EstCivil = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        resp2Sexo = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        resp2OrgaoRg = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        cboDupli = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        btnLimpAcad = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jpCard4 = new javax.swing.JPanel();
        jpFichaMedica = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        txtMed = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtAllerg = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        cboEmergCont = new javax.swing.JComboBox<>();
        jLabel89 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObsMed = new javax.swing.JTextArea();
        jLabel69 = new javax.swing.JLabel();
        cboBlood = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtExtra = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR NOVO ALUNO");
        setModal(true);
        setResizable(false);

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1498991351_f-left_256.png"))); // NOI18N
        btnBack.setText("Voltar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1498991450_f-right_256.png"))); // NOI18N
        btnNext.setText("Avançar");
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        parentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        parentPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                parentPanelComponentShown(evt);
            }
        });
        parentPanel.setLayout(new java.awt.CardLayout());

        jpCard1.setBorder(null);
        jpCard1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jpCard1ComponentShown(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(99, 135, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setForeground(new java.awt.Color(53, 88, 248));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setForeground(new java.awt.Color(254, 254, 254));

        jLabel3.setFont(new java.awt.Font("Cantarell", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(1, 1, 1));
        jLabel3.setText("BEM-VINDO AO ASSISTENTE DE CADASTRO");

        jLabel93.setText("Em caso de dúvidas, pressione F1 para abrir a ajuda, ou contate nosso suporte.");

        jLabel92.setText("Campos em negrito, destacados com asterísco(*), são de preenchimento obrigatório.");

        jLabel91.setText("Preencha os dados sempre com atenção a identificação dos campos!");

        jLabel90.setText("Este assistente irá auxiliá-lo(a) a efetuar o cadastro completo de um(a) novo(a) aluno(a).");

        jLabel94.setText("Para continuar, clique no botão \"Avançar\".");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel94)
                            .addComponent(jLabel90)
                            .addComponent(jLabel93)
                            .addComponent(jLabel91)
                            .addComponent(jLabel92))
                        .addGap(45, 45, 45))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(70, 70, 70))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel3)
                .addGap(70, 70, 70)
                .addComponent(jLabel90)
                .addGap(8, 8, 8)
                .addComponent(jLabel92)
                .addGap(8, 8, 8)
                .addComponent(jLabel91)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
                .addComponent(jLabel94)
                .addGap(73, 73, 73))
        );

        javax.swing.GroupLayout jpCard1Layout = new javax.swing.GroupLayout(jpCard1);
        jpCard1.setLayout(jpCard1Layout);
        jpCard1Layout.setHorizontalGroup(
            jpCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCard1Layout.setVerticalGroup(
            jpCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        parentPanel.add(jpCard1, "card5");

        jpCard2.setBackground(new java.awt.Color(254, 254, 254));
        jpCard2.setBorder(null);
        jpCard2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jpCard2ComponentShown(evt);
            }
        });

        jpInfoPess.setBackground(new java.awt.Color(254, 254, 254));
        jpInfoPess.setBorder(javax.swing.BorderFactory.createTitledBorder("INFORMAÇÕES PESSOAIS"));

        jLabel1.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel1.setText("Nome do Aluno:");

        txtNomeAlu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomeAluFocusLost(evt);
            }
        });
        txtNomeAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeAluActionPerformed(evt);
            }
        });

        jLabel6.setText("Email:");

        jLabel7.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel7.setText("Estado Civil:");

        jLabel2.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel2.setText("Data de Nascimento:");

        jLabel5.setText("CPF:");

        txtCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel8.setText("Sexo:");

        cboEstCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        cboEstCivil.setSelectedIndex(-1);

        cboSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        cboSexo.setSelectedIndex(-1);

        jLabel49.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel49.setText("RG:");

        txtRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRgActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel51.setText("Data de Expedição:");

        txtDataRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataRgActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(233, 2, 2));
        jLabel10.setText("*");

        jLabel11.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(233, 2, 2));
        jLabel11.setText("*");

        jLabel13.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(233, 2, 2));
        jLabel13.setText("*");

        jLabel23.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(233, 2, 2));
        jLabel23.setText("*");

        jLabel24.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(233, 2, 2));
        jLabel24.setText("*");

        jLabel25.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(233, 2, 2));
        jLabel25.setText("*");

        jLabel50.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel50.setText("Orgão Emissor:");

        jLabel22.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(233, 2, 2));
        jLabel22.setText("*");

        jLabel96.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel96.setText("Telefone:");

        jLabel95.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(233, 2, 2));
        jLabel95.setText("*");

        javax.swing.GroupLayout jpInfoPessLayout = new javax.swing.GroupLayout(jpInfoPess);
        jpInfoPess.setLayout(jpInfoPessLayout);
        jpInfoPessLayout.setHorizontalGroup(
            jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInfoPessLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel13))
                            .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNasc)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel10))
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel25))
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(txtDataRg)
                                .addGap(2, 2, 2))))
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel11))
                    .addComponent(txtNomeAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel96)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel95)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel23))
                            .addComponent(cboEstCivil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel24))
                                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                                        .addComponent(jLabel50)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel22)))
                                .addGap(0, 22, Short.MAX_VALUE))))
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtOrgaoRg)))
                .addContainerGap())
        );
        jpInfoPessLayout.setVerticalGroup(
            jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInfoPessLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNomeAlu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDataRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtOrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel25))))
                                .addGap(2, 2, 2))
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel49)
                                    .addComponent(jLabel13))
                                .addGap(33, 33, 33)))
                        .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jLabel23))
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel24))
                                    .addGroup(jpInfoPessLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jpInfoPessLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel96)
                                    .addComponent(jLabel95))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpInfoPessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cboSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpEndereco.setBackground(new java.awt.Color(254, 254, 254));
        jpEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder("ENDEREÇO"));

        txtLogra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLograActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel15.setText("Número:");

        txtNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel16.setText("Bairro:");

        txtBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBairroActionPerformed(evt);
            }
        });

        jLabel17.setText("Complemento:");

        txtCompl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComplActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel18.setText("Município:");

        jLabel19.setFont(new java.awt.Font("Cantarell", 1, 14)); // NOI18N
        jLabel19.setText("<html><u>CEP:</u>");
        jLabel19.setToolTipText("");

        txtMuni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMuniActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel20.setText("UF:");

        jLabel21.setText("Referência:");

        jLabel27.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(233, 2, 2));
        jLabel27.setText("*");

        jLabel28.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(233, 2, 2));
        jLabel28.setText("*");

        jLabel29.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(233, 2, 2));
        jLabel29.setText("*");

        jLabel30.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(233, 2, 2));
        jLabel30.setText("*");

        jLabel31.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(233, 2, 2));
        jLabel31.setText("*");

        cboUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        cboUf.setSelectedIndex(-1);

        jLabel41.setFont(new java.awt.Font("DejaVu Sans", 0, 10)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(233, 2, 2));
        jLabel41.setText("*Caso haja dúvidas, deixe em branco.");

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

        jLabel55.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel55.setText("Logradouro:");
        jLabel55.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnLimpCep.setText("Limpar");
        btnLimpCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpCepActionPerformed(evt);
            }
        });

        loadCep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/load.png"))); // NOI18N
        loadCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCepActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpEnderecoLayout = new javax.swing.GroupLayout(jpEndereco);
        jpEndereco.setLayout(jpEnderecoLayout);
        jpEnderecoLayout.setHorizontalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28)
                                .addGap(219, 219, 219))
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtBairro, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpEnderecoLayout.createSequentialGroup()
                                            .addComponent(jLabel55)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel29))
                                        .addComponent(txtLogra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                                        .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(loadCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(btnLimpCep, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel27))
                            .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel30)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel17)
                                    .addGap(64, 64, 64))
                                .addGroup(jpEnderecoLayout.createSequentialGroup()
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jpEnderecoLayout.createSequentialGroup()
                                            .addGap(74, 74, 74)
                                            .addComponent(jLabel31))
                                        .addComponent(jLabel18)
                                        .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addContainerGap()))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                                .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRef)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jpEnderecoLayout.setVerticalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(1, 1, 1)
                        .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnLimpCep)
                        .addComponent(loadCep)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel31)
                            .addComponent(jLabel20)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpCard2Layout = new javax.swing.GroupLayout(jpCard2);
        jpCard2.setLayout(jpCard2Layout);
        jpCard2Layout.setHorizontalGroup(
            jpCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpInfoPess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpCard2Layout.createSequentialGroup()
                        .addComponent(jpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpCard2Layout.setVerticalGroup(
            jpCard2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpInfoPess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        parentPanel.add(jpCard2, "card2");

        jpCard3.setBackground(new java.awt.Color(254, 254, 254));
        jpCard3.setBorder(null);
        jpCard3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jpCard3ComponentShown(evt);
            }
        });

        jpResp1.setBackground(new java.awt.Color(254, 254, 254));
        jpResp1.setBorder(javax.swing.BorderFactory.createTitledBorder("RESPONSÁVEL FINANCEIRO"));

        jLabel12.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel12.setText("Nome do Responsável:");

        resp1Nome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1NomeFocusLost(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel32.setText("CPF:");

        jLabel33.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel33.setText("RG:");

        resp1Rg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1RgFocusLost(evt);
            }
        });
        resp1Rg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resp1RgActionPerformed(evt);
            }
        });

        try{    MaskFormatter cpf= new MaskFormatter("###.###.###-##");    resp1Cpf = new JFormattedTextField(cpf); }    catch (Exception e){ }
        resp1Cpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1CpfFocusLost(evt);
            }
        });
        resp1Cpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resp1CpfActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel34.setText("Telefone 1:");

        resp1Tel1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1Tel1FocusLost(evt);
            }
        });

        jLabel35.setText("Telefone 2:");

        resp1Tel2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1Tel2FocusLost(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel36.setText("Data de Nascimento:");

        resp1Nasc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1NascFocusLost(evt);
            }
        });

        resp1DataRg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1DataRgFocusLost(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel37.setText("Data de Emissão:");

        resp1EstCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casado(a)", "Solteiro(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        resp1EstCivil.setSelectedIndex(-1);
        resp1EstCivil.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1EstCivilFocusLost(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel38.setText("Estado Civil:");

        resp1Sexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        resp1Sexo.setSelectedIndex(-1);
        resp1Sexo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1SexoFocusLost(evt);
            }
        });
        resp1Sexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resp1SexoActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel39.setText("Sexo:");

        resp1OrgaoRg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp1OrgaoRgFocusLost(evt);
            }
        });
        resp1OrgaoRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resp1OrgaoRgActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
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

        cboRespAlu.setText("O responsável é o próprio aluno");
        cboRespAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRespAluActionPerformed(evt);
            }
        });

        btnLimpFin.setText("Limpar Dados");
        btnLimpFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpFinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpResp1Layout = new javax.swing.GroupLayout(jpResp1);
        jpResp1.setLayout(jpResp1Layout);
        jpResp1Layout.setHorizontalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createSequentialGroup()
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
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel70))
                                            .addComponent(resp1Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(resp1Nasc)))
                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(resp1Tel1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                                .addComponent(jLabel32)
                                                .addGap(1, 1, 1)
                                                .addComponent(jLabel79))
                                            .addComponent(resp1Cpf))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                                .addComponent(jLabel33)
                                                .addGap(1, 1, 1)
                                                .addComponent(jLabel73))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp1Layout.createSequentialGroup()
                                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel35)
                                                    .addComponent(resp1Rg, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                                        .addComponent(jLabel38)
                                                        .addGap(1, 1, 1)
                                                        .addComponent(jLabel77))
                                                    .addComponent(resp1DataRg)))
                                            .addGroup(jpResp1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(resp1Tel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resp1EstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                                    .addComponent(resp1OrgaoRg)
                                    .addComponent(resp1Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(cboRespAlu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpFin))))
        );
        jpResp1Layout.setVerticalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboRespAlu)
                    .addComponent(btnLimpFin))
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel36)
                    .addComponent(jLabel39)
                    .addComponent(jLabel70)
                    .addComponent(jLabel71)
                    .addComponent(jLabel72))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resp1Nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1Nasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(resp1Cpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1Rg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1DataRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1OrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38)
                    .addComponent(jLabel76)
                    .addComponent(jLabel77))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resp1Tel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1Tel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp1EstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpResp2.setBackground(new java.awt.Color(254, 254, 254));
        jpResp2.setBorder(javax.swing.BorderFactory.createTitledBorder("RESPONSÁVEL ACADÊMICO"));

        jLabel42.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel42.setText("Nome do Responsável:");

        resp2Nome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp2NomeFocusLost(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel43.setText("CPF:");

        jLabel44.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel44.setText("RG:");

        resp2Cpf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                resp2CpfFocusLost(evt);
            }
        });
        resp2Cpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resp2CpfKeyPressed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel45.setText("Telefone 1:");

        resp2Tel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resp2Tel1ActionPerformed(evt);
            }
        });

        jLabel46.setText("Telefone 2:");

        jLabel47.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel47.setText("Data de Nascimento:");

        jLabel48.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel48.setText("Data de Emissão:");

        resp2EstCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Casado(a)", "Solteiro(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        resp2EstCivil.setSelectedIndex(-1);

        jLabel52.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel52.setText("Estado Civil:");

        resp2Sexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        resp2Sexo.setSelectedIndex(-1);

        jLabel53.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel53.setText("Sexo:");

        jLabel54.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel54.setText("Orgão Emissor:");

        cboDupli.setText("Copiar dados do responsável acima");
        cboDupli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDupliActionPerformed(evt);
            }
        });

        jLabel80.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(233, 2, 2));
        jLabel80.setText("*");

        jLabel81.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(233, 2, 2));
        jLabel81.setText("*");

        jLabel82.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(233, 2, 2));
        jLabel82.setText("*");

        jLabel83.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(233, 2, 2));
        jLabel83.setText("*");

        jLabel84.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(233, 2, 2));
        jLabel84.setText("*");

        jLabel85.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(233, 2, 2));
        jLabel85.setText("*");

        jLabel86.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(233, 2, 2));
        jLabel86.setText("*");

        jLabel88.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(233, 2, 2));
        jLabel88.setText("*");

        btnLimpAcad.setText("Limpar Dados");
        btnLimpAcad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpAcadActionPerformed(evt);
            }
        });

        jLabel97.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(233, 2, 2));
        jLabel97.setText("*");

        javax.swing.GroupLayout jpResp2Layout = new javax.swing.GroupLayout(jpResp2);
        jpResp2.setLayout(jpResp2Layout);
        jpResp2Layout.setHorizontalGroup(
            jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpResp2Layout.createSequentialGroup()
                        .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel84))
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(resp2Tel1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpResp2Layout.createSequentialGroup()
                                        .addComponent(jLabel43)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel97))
                                    .addComponent(resp2Cpf))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpResp2Layout.createSequentialGroup()
                                        .addComponent(jLabel44)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel81))
                                    .addGroup(jpResp2Layout.createSequentialGroup()
                                        .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(resp2Tel2, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(resp2Rg))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jpResp2Layout.createSequentialGroup()
                                                .addComponent(jLabel52)
                                                .addGap(1, 1, 1)
                                                .addComponent(jLabel88))
                                            .addComponent(resp2DataRg, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                            .addComponent(resp2EstCivil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addGap(6, 6, 6)
                        .addComponent(resp2OrgaoRg)
                        .addContainerGap())
                    .addGroup(jpResp2Layout.createSequentialGroup()
                        .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addComponent(resp2Nome, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(resp2Nasc, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpResp2Layout.createSequentialGroup()
                                        .addComponent(jLabel48)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel82))))
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel80)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel47)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel85))
                            .addComponent(cboDupli)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpResp2Layout.createSequentialGroup()
                                        .addComponent(jLabel53)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel83))
                                    .addGroup(jpResp2Layout.createSequentialGroup()
                                        .addComponent(jLabel54)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel86)))
                                .addGap(0, 44, Short.MAX_VALUE))
                            .addGroup(jpResp2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpResp2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btnLimpAcad))
                                    .addComponent(resp2Sexo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
        );
        jpResp2Layout.setVerticalGroup(
            jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpResp2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboDupli)
                    .addComponent(btnLimpAcad))
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel47)
                    .addComponent(jLabel53)
                    .addComponent(jLabel80)
                    .addComponent(jLabel83)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resp2Nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2Nasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2Sexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44)
                    .addComponent(jLabel48)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81)
                    .addComponent(jLabel82)
                    .addComponent(jLabel86)
                    .addComponent(jLabel97))
                .addGap(6, 6, 6)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resp2Cpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2Rg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2DataRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2OrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jLabel46)
                    .addComponent(jLabel52)
                    .addComponent(jLabel84)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(resp2Tel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2Tel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resp2EstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jpCard3Layout = new javax.swing.GroupLayout(jpCard3);
        jpCard3.setLayout(jpCard3Layout);
        jpCard3Layout.setHorizontalGroup(
            jpCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpResp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpResp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jpCard3Layout.setVerticalGroup(
            jpCard3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpResp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jpResp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        parentPanel.add(jpCard3, "card3");

        jpCard4.setBackground(new java.awt.Color(254, 254, 254));
        jpCard4.setBorder(null);
        jpCard4.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jpCard4ComponentShown(evt);
            }
        });

        jpFichaMedica.setBackground(new java.awt.Color(254, 254, 254));
        jpFichaMedica.setBorder(javax.swing.BorderFactory.createTitledBorder("INFORMAÇÕES MÉDICAS"));

        jLabel65.setText("Medicamento de uso contínuo:");

        jLabel66.setText("Alergias:");

        jLabel67.setText("Observações:");

        jPanel12.setBackground(new java.awt.Color(245, 245, 245));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Em caso de emergência, contatar:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), java.awt.Color.red)); // NOI18N

        jLabel68.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel68.setText("Nome:");

        jLabel89.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(233, 2, 2));
        jLabel89.setText("*");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel89)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(cboEmergCont, 0, 333, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(jLabel89))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEmergCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        txtObsMed.setColumns(20);
        txtObsMed.setRows(2);
        txtObsMed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObsMedKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtObsMed);

        jLabel69.setText("Tipo Sanguíneo:");

        cboBlood.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+" }));
        cboBlood.setSelectedIndex(-1);

        javax.swing.GroupLayout jpFichaMedicaLayout = new javax.swing.GroupLayout(jpFichaMedica);
        jpFichaMedica.setLayout(jpFichaMedicaLayout);
        jpFichaMedicaLayout.setHorizontalGroup(
            jpFichaMedicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFichaMedicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpFichaMedicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67)
                    .addComponent(jLabel69)
                    .addComponent(cboBlood, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpFichaMedicaLayout.createSequentialGroup()
                        .addGroup(jpFichaMedicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel65)
                            .addComponent(txtMed, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66)
                            .addComponent(txtAllerg, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpFichaMedicaLayout.setVerticalGroup(
            jpFichaMedicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpFichaMedicaLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel69)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboBlood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpFichaMedicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpFichaMedicaLayout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addGap(6, 6, 6)
                        .addComponent(txtMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel66)
                        .addGap(6, 6, 6)
                        .addComponent(txtAllerg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel67)
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(254, 254, 254));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("INFORMAÇÕES ADICIONAIS"));

        txtExtra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExtraKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtExtra);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpCard4Layout = new javax.swing.GroupLayout(jpCard4);
        jpCard4.setLayout(jpCard4Layout);
        jpCard4Layout.setHorizontalGroup(
            jpCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpFichaMedica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpCard4Layout.setVerticalGroup(
            jpCard4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpCard4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jpFichaMedica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        parentPanel.add(jpCard4, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(parentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(parentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnNext)
                    .addComponent(btnCancel))
                .addGap(12, 12, 12))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        back();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        switch (card) {
            case 1:
                card1();
                break;
            case 2:
                card2();
                break;
            case 3:
                card3();
                break;
            case 4:
                card4();
                break;
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        sair();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void jpCard1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jpCard1ComponentShown
        btnBack.setVisible(false);
    }//GEN-LAST:event_jpCard1ComponentShown

    private void txtNomeAluFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomeAluFocusLost
        buscaAluno();
    }//GEN-LAST:event_txtNomeAluFocusLost

    private void txtNomeAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAluActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeAluActionPerformed

    private void txtCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfActionPerformed

    private void txtRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRgActionPerformed

    private void txtDataRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataRgActionPerformed

    }//GEN-LAST:event_txtDataRgActionPerformed

    private void txtLograActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLograActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLograActionPerformed

    private void txtNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumActionPerformed

    private void txtBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBairroActionPerformed

    }//GEN-LAST:event_txtBairroActionPerformed

    private void txtComplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComplActionPerformed

    }//GEN-LAST:event_txtComplActionPerformed

    private void txtMuniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMuniActionPerformed

    }//GEN-LAST:event_txtMuniActionPerformed

    private void jpCard2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jpCard2ComponentShown
        txtNomeAlu.grabFocus();
        btnBack.setVisible(true);
    }//GEN-LAST:event_jpCard2ComponentShown

    private void resp1NomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1NomeFocusLost
        buscaResp("Fin");
        if (cboDupli.isSelected()) {
            resp2Nome.setText(resp1Nome.getText());
        }
    }//GEN-LAST:event_resp1NomeFocusLost

    private void resp1RgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1RgFocusLost
        if (cboDupli.isSelected()) {
            resp2Rg.setText(resp1Rg.getText());
        }
    }//GEN-LAST:event_resp1RgFocusLost

    private void resp1RgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resp1RgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resp1RgActionPerformed

    private void resp1CpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1CpfFocusLost
        if (cboDupli.isSelected()) {
            resp2Cpf.setText(resp1Cpf.getText());
        }
    }//GEN-LAST:event_resp1CpfFocusLost

    private void resp1CpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resp1CpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resp1CpfActionPerformed

    private void resp1Tel1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1Tel1FocusLost
        if (cboDupli.isSelected()) {
            resp2Tel1.setText(resp1Tel1.getText());
        }
    }//GEN-LAST:event_resp1Tel1FocusLost

    private void resp1Tel2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1Tel2FocusLost
        if (cboDupli.isSelected()) {
            resp2Tel2.setText(resp1Tel2.getText());
        }
    }//GEN-LAST:event_resp1Tel2FocusLost

    private void resp1NascFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1NascFocusLost
        if (cboDupli.isSelected()) {
            resp2Nasc.setText(resp1Nasc.getText());
        }
    }//GEN-LAST:event_resp1NascFocusLost

    private void resp1DataRgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1DataRgFocusLost
        if (cboDupli.isSelected()) {
            resp2DataRg.setText(resp1DataRg.getText());
        }
    }//GEN-LAST:event_resp1DataRgFocusLost

    private void resp1EstCivilFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1EstCivilFocusLost
        if (cboDupli.isSelected()) {
            resp2EstCivil.setSelectedItem(resp1EstCivil.getSelectedItem());
        }
    }//GEN-LAST:event_resp1EstCivilFocusLost

    private void resp1SexoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1SexoFocusLost
        if (cboDupli.isSelected()) {
            resp2Sexo.setSelectedItem(resp2Sexo.getSelectedItem());
        }
    }//GEN-LAST:event_resp1SexoFocusLost

    private void resp1SexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resp1SexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resp1SexoActionPerformed

    private void resp1OrgaoRgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp1OrgaoRgFocusLost
        if (cboDupli.isSelected()) {
            resp2OrgaoRg.setText(resp1OrgaoRg.getText());
        }
    }//GEN-LAST:event_resp1OrgaoRgFocusLost

    private void resp1OrgaoRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resp1OrgaoRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resp1OrgaoRgActionPerformed

    private void cboRespAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRespAluActionPerformed
        if (cboRespAlu.isSelected()) {
            resp1Nome.setText(txtNomeAlu.getText());
            resp1Cpf.setText(txtCpf.getText());
            resp1Tel1.setText(txtTel.getText());
            resp1DataRg.setText(txtDataRg.getText());
            resp1EstCivil.setSelectedItem(cboEstCivil.getSelectedItem());
            resp1Nasc.setText(txtNasc.getText());
            resp1OrgaoRg.setText(txtOrgaoRg.getText());
            resp1Rg.setText(txtRg.getText());
            resp1Sexo.setSelectedItem(cboSexo.getSelectedItem());

            if (!resp1Cpf.getText().equals("   .   .   -  ")) {
                resp1Cpf.setEditable(false);
                resp1Cpf.setEnabled(false);
            }
            resp1Nome.setEditable(false);
            resp1Tel1.setEditable(false);
            resp1Tel2.setEditable(false);
            resp1DataRg.setEditable(false);
            resp1EstCivil.setEditable(false);
            resp1Nasc.setEditable(false);
            resp1OrgaoRg.setEditable(false);
            resp1Rg.setEditable(false);
            resp1Sexo.setEditable(false);

            resp1Nome.setEnabled(false);
            resp1Tel1.setEnabled(false);
            resp1Tel2.setEnabled(false);
            resp1DataRg.setEnabled(false);
            resp1EstCivil.setEnabled(false);
            resp1Nasc.setEnabled(false);
            resp1OrgaoRg.setEnabled(false);
            resp1Rg.setEnabled(false);
            resp1Sexo.setEnabled(false);
        } else {
            resp1Nome.setText(null);
            resp1Cpf.setText(null);
            resp1Tel1.setText(null);
            resp1DataRg.setText(null);
            resp1EstCivil.setSelectedItem(null);
            resp1Nasc.setText(null);
            resp1OrgaoRg.setText(null);
            resp1Rg.setText(null);
            resp1Sexo.setSelectedItem(null);

            resp1Nome.setEditable(true);
            resp1Tel2.setEditable(true);
            resp1Cpf.setEditable(true);
            resp1DataRg.setEditable(true);
            resp1Tel1.setEditable(true);
            resp1Nasc.setEditable(true);
            resp1OrgaoRg.setEditable(true);
            resp1Rg.setEditable(true);

            resp1Nome.setEnabled(true);
            resp1Tel2.setEnabled(true);
            resp1Cpf.setEnabled(true);
            resp1DataRg.setEnabled(true);
            resp1EstCivil.setEnabled(true);
            resp1Tel1.setEnabled(true);
            resp1Nasc.setEnabled(true);
            resp1OrgaoRg.setEnabled(true);
            resp1Rg.setEnabled(true);
            resp1Sexo.setEnabled(true);

            if (cboDupli.isSelected()) {
                cboDupli.setSelected(false);
                cboDupliActionPerformed(evt);

            }
        }
    }//GEN-LAST:event_cboRespAluActionPerformed

    private void btnLimpFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpFinActionPerformed
        buscaRespFin = 0;
        cboRespAlu.setSelected(false);
        resp1Nome.setText("");
        resp1Nasc.setText("");
        resp1Sexo.setSelectedItem(-1);
        resp1Cpf.setText("");
        resp1Rg.setText("");
        resp1DataRg.setText("");
        resp1OrgaoRg.setText("");
        resp1Tel1.setText("");
        resp1Tel2.setText("");
        resp1EstCivil.setSelectedItem(-1);

        resp1Nome.setEditable(true);
        resp1Tel2.setEditable(true);
        resp1Cpf.setEditable(true);
        resp1DataRg.setEditable(true);
        resp1Tel1.setEditable(true);
        resp1Nasc.setEditable(true);
        resp1OrgaoRg.setEditable(true);
        resp1Rg.setEditable(true);

        resp1Nome.setEnabled(true);
        resp1Tel2.setEnabled(true);
        resp1Cpf.setEnabled(true);
        resp1DataRg.setEnabled(true);
        resp1EstCivil.setEnabled(true);
        resp1Tel1.setEnabled(true);
        resp1Nasc.setEnabled(true);
        resp1OrgaoRg.setEnabled(true);
        resp1Rg.setEnabled(true);
        resp1Sexo.setEnabled(true);

        if (cboDupli.isSelected()) {
            btnLimpAcadActionPerformed(null);
            cboDupli.setSelected(false);
        }

        resp1Nome.grabFocus();
    }//GEN-LAST:event_btnLimpFinActionPerformed

    private void resp2NomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp2NomeFocusLost
        buscaResp("Acad");
    }//GEN-LAST:event_resp2NomeFocusLost

    private void resp2CpfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resp2CpfFocusLost

    }//GEN-LAST:event_resp2CpfFocusLost

    private void resp2CpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resp2CpfKeyPressed

    }//GEN-LAST:event_resp2CpfKeyPressed

    private void resp2Tel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resp2Tel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resp2Tel1ActionPerformed

    private void cboDupliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDupliActionPerformed
        if (cboDupli.isSelected()) {
            resp2Nome.setText(resp1Nome.getText());
            resp2Tel2.setText(resp1Tel2.getText());
            resp2Cpf.setText(resp1Cpf.getText());
            resp2DataRg.setText(resp1DataRg.getText());
            resp2EstCivil.setSelectedItem(resp1EstCivil.getSelectedItem());
            resp2Tel1.setText(resp1Tel1.getText());
            resp2Nasc.setText(resp1Nasc.getText());
            resp2OrgaoRg.setText(resp1OrgaoRg.getText());
            resp2Rg.setText(resp1Rg.getText());
            resp2Sexo.setSelectedItem(resp1Sexo.getSelectedItem());

            resp2Nome.setEditable(false);
            resp2Tel2.setEditable(false);
            resp2Cpf.setEditable(false);
            resp2DataRg.setEditable(false);
            resp2EstCivil.setEditable(false);
            resp2Tel1.setEditable(false);
            resp2Nasc.setEditable(false);
            resp2OrgaoRg.setEditable(false);

            resp2Rg.setEditable(false);
            resp2Sexo.setEditable(false);

            resp2Nome.setEnabled(false);
            resp2Tel2.setEnabled(false);
            resp2Cpf.setEnabled(false);
            resp2DataRg.setEnabled(false);
            resp2EstCivil.setEnabled(false);
            resp2Tel1.setEnabled(false);
            resp2Nasc.setEnabled(false);
            resp2OrgaoRg.setEnabled(false);
            resp2Rg.setEnabled(false);
            resp2Sexo.setEnabled(false);
        } else {
            resp2Nome.setText(null);
            resp2Tel2.setText(null);
            resp2Cpf.setText(null);
            resp2DataRg.setText(null);
            resp2EstCivil.setSelectedItem(null);
            resp2Tel1.setText(null);
            resp2Nasc.setText(null);
            resp2OrgaoRg.setText(null);
            resp2Rg.setText(null);
            resp2Sexo.setSelectedItem(null);

            resp2Nome.setEditable(true);
            resp2Tel2.setEditable(true);
            resp2Cpf.setEditable(true);
            resp2DataRg.setEditable(true);
            resp2Tel1.setEditable(true);
            resp2Nasc.setEditable(true);
            resp2OrgaoRg.setEditable(true);
            resp2Rg.setEditable(true);

            resp2Nome.setEnabled(true);
            resp2Tel2.setEnabled(true);
            resp2Cpf.setEnabled(true);
            resp2DataRg.setEnabled(true);
            resp2EstCivil.setEnabled(true);
            resp2Tel1.setEnabled(true);
            resp2Nasc.setEnabled(true);
            resp2OrgaoRg.setEnabled(true);
            resp2Rg.setEnabled(true);
            resp2Sexo.setEnabled(true);
        }
    }//GEN-LAST:event_cboDupliActionPerformed

    private void btnLimpAcadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpAcadActionPerformed
        buscaRespAcad = 0;
        cboDupli.setSelected(false);
        resp2Nome.setText("");
        resp2Nasc.setText("");
        resp2Sexo.setSelectedItem(-1);
        resp2Cpf.setText("");
        resp2Rg.setText("");
        resp2DataRg.setText("");
        resp2OrgaoRg.setText("");
        resp2Tel1.setText("");
        resp2Tel2.setText("");
        resp2EstCivil.setSelectedItem(-1);

        resp2Nome.setEditable(true);
        resp2Tel2.setEditable(true);
        resp2Cpf.setEditable(true);
        resp2DataRg.setEditable(true);
        resp2Tel1.setEditable(true);
        resp2Nasc.setEditable(true);
        resp2OrgaoRg.setEditable(true);
        resp2Rg.setEditable(true);

        resp2Nome.setEnabled(true);
        resp2Tel2.setEnabled(true);
        resp2Cpf.setEnabled(true);
        resp2DataRg.setEnabled(true);
        resp2EstCivil.setEnabled(true);
        resp2Tel1.setEnabled(true);
        resp2Nasc.setEnabled(true);
        resp2OrgaoRg.setEnabled(true);
        resp2Rg.setEnabled(true);
        resp2Sexo.setEnabled(true);

        resp2Nome.grabFocus();
    }//GEN-LAST:event_btnLimpAcadActionPerformed

    private void jpCard3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jpCard3ComponentShown
        resp1Nome.grabFocus();
    }//GEN-LAST:event_jpCard3ComponentShown

    private void jpCard4ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jpCard4ComponentShown

    }//GEN-LAST:event_jpCard4ComponentShown

    private void parentPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_parentPanelComponentShown

    }//GEN-LAST:event_parentPanelComponentShown

    private void loadCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCepActionPerformed
        if (!txtCep.getText().equals("  .   -   ")) {
            buscaCep();
        }
    }//GEN-LAST:event_loadCepActionPerformed

    private void txtCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepActionPerformed
        buscaCep();
    }//GEN-LAST:event_txtCepActionPerformed

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

    private void txtObsMedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObsMedKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            if (evt.isShiftDown()) {
                txtObsMed.transferFocusBackward();
            } else {
                txtObsMed.transferFocus();
            }
        }    }//GEN-LAST:event_txtObsMedKeyPressed

    private void txtExtraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExtraKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            if (evt.isShiftDown()) {
                txtExtra.transferFocusBackward();
            } else {
                txtExtra.transferFocus();
            }
        }
    }//GEN-LAST:event_txtExtraKeyPressed

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
            java.util.logging.Logger.getLogger(WizardNovoAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WizardNovoAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WizardNovoAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WizardNovoAluno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                WizardNovoAluno dialog = new WizardNovoAluno(new JFrame());
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
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLimpAcad;
    private javax.swing.JButton btnLimpCep;
    private javax.swing.JButton btnLimpFin;
    private javax.swing.JButton btnNext;
    private javax.swing.JComboBox<String> cboBlood;
    private javax.swing.JCheckBox cboDupli;
    private javax.swing.JComboBox<String> cboEmergCont;
    private javax.swing.JComboBox<String> cboEstCivil;
    private javax.swing.JCheckBox cboRespAlu;
    private javax.swing.JComboBox<String> cboSexo;
    private javax.swing.JComboBox<String> cboUf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel jpCard1;
    private javax.swing.JPanel jpCard2;
    private javax.swing.JPanel jpCard3;
    private javax.swing.JPanel jpCard4;
    private javax.swing.JPanel jpEndereco;
    private javax.swing.JPanel jpFichaMedica;
    private javax.swing.JPanel jpInfoPess;
    private javax.swing.JPanel jpResp1;
    private javax.swing.JPanel jpResp2;
    private javax.swing.JButton loadCep;
    private javax.swing.JPanel parentPanel;
    private javax.swing.JTextField resp1Cpf;
    private javax.swing.JTextField resp1DataRg;
    private javax.swing.JComboBox<String> resp1EstCivil;
    private javax.swing.JTextField resp1Nasc;
    private javax.swing.JTextField resp1Nome;
    private javax.swing.JTextField resp1OrgaoRg;
    private javax.swing.JTextField resp1Rg;
    private javax.swing.JComboBox<String> resp1Sexo;
    private javax.swing.JTextField resp1Tel1;
    private javax.swing.JTextField resp1Tel2;
    private javax.swing.JTextField resp2Cpf;
    private javax.swing.JTextField resp2DataRg;
    private javax.swing.JComboBox<String> resp2EstCivil;
    private javax.swing.JTextField resp2Nasc;
    private javax.swing.JTextField resp2Nome;
    private javax.swing.JTextField resp2OrgaoRg;
    private javax.swing.JTextField resp2Rg;
    private javax.swing.JComboBox<String> resp2Sexo;
    private javax.swing.JTextField resp2Tel1;
    private javax.swing.JTextField resp2Tel2;
    private javax.swing.JTextField txtAllerg;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JFormattedTextField txtCep;
    private javax.swing.JTextField txtCompl;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtDataRg;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextPane txtExtra;
    private javax.swing.JTextField txtLogra;
    private javax.swing.JTextField txtMed;
    private javax.swing.JTextField txtMuni;
    private javax.swing.JTextField txtNasc;
    private javax.swing.JTextField txtNomeAlu;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextArea txtObsMed;
    private javax.swing.JTextField txtOrgaoRg;
    private javax.swing.JTextField txtRef;
    private javax.swing.JTextField txtRg;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables

}
