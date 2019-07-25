package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.dal.WebServiceCep;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author jferreira
 */
public class AlunosForm extends javax.swing.JDialog {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String idFDialog = null;

    String stu_id = null;
    String stu_cod = null;
    String adress_id = null;
    String respFin_id = null;
    String respAcad_id = null;
    String respEmerg_id = null;
    String course_id = null;
    String class_id = null;
    String cRoom_id = null;
    DateFormat brData = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat sqlData = new SimpleDateFormat("yyyy-MM-dd");
    boolean modified;

    public AlunosForm(String codStu) {
        initComponents();
        con = ModuloConexao.conector();
        this.stu_cod = codStu;
        this.setTitle(this.getTitle() + stu_cod + ") - Agility®");
        listData();
        loadCourse();
    }

    private void listData() {
        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfDateTime = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

        String sql = "SELECT * FROM students WHERE cod ='" + this.stu_cod + "'";
        try {
            rs = con.prepareStatement(sql).executeQuery();
            rs.next();
            stu_id = rs.getString("id");
            adress_id = rs.getString("adress_id");
            respFin_id = rs.getString("respFin");
            respAcad_id = rs.getString("respAcad");
            respEmerg_id = rs.getString("respEmerg");
            course_id = rs.getString("course_id");
            class_id = rs.getString("class_id");

            txtAluNome.setText(rs.getString("name"));
            txtAluNasc.setText(dfDate.format(rs.getDate("datanasc")));
            txtMat.setText(rs.getString("cod"));
            txtAluCpf.setText(rs.getString("cpf"));
            txtAluRg.setText(rs.getString("rg"));
            txtAluDataRg.setText(dfDate.format(rs.getDate("dataRg")));
            txtAluOrgaoRg.setText(rs.getString("orgaoRg"));
            txtAluEmail.setText(rs.getString("email"));
            txtAluTel.setText(rs.getString("tel"));
            cboAluEstCivil.setSelectedItem(rs.getString("estCivil"));
            cboAluSexo.setSelectedItem(rs.getString("sexo"));
            txtAluExtraInfo.setText(rs.getString("extraInfo"));
            txtAluMed.setText(rs.getString("medicines"));
            txtAluBloodType.setText(rs.getString("bloodType"));
            txtAluMedObs.setText(rs.getString("medicalObs"));
            txtAluAlerg.setText(rs.getString("allergies"));

            //ENDEREÇO
            try {
                rs = con.prepareStatement("SELECT * FROM adresses WHERE id = " + adress_id).executeQuery();
                if (rs.next()) {
                    txtLogra.setText(rs.getString("logra"));
                    txtNum.setText(rs.getString("num"));
                    txtBairro.setText(rs.getString("bairro"));
                    txtCompl.setText(rs.getString("compl"));
                    cboUf.setSelectedItem(rs.getString("uf"));
                    txtMuni.setText(rs.getString("muni"));
                    txtRef.setText(rs.getString("refer"));
                    txtCep.setText(rs.getString("cep"));

                } else {
                    JOptionPane.showMessageDialog(null, "Não existe nenhum endereço cadastrado para este aluno !\nCódigo do Erro: #0019");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0020\nDetalhes do erro:\n" + e);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0011\nDetalhes do erro:\n" + e);
        }

    }

    private void loadCourse() {
        //Fiz esse método pra caso o usuário altere os dados da matrícula, if(AlunoMatricula.modified){loadCourse()}

        try {
            rs = con.prepareStatement("SELECT \n"
                    + "    CLA.cod,\n"
                    + "    CLA.name,\n"
                    + "    CLA.turno,\n"
                    + "    CRO.cod,\n"
                    + "    CRO.name,\n"
                    + "    COU.cod,\n"
                    + "    COU.name\n"
                    + "FROM\n"
                    + "    students STU\n"
                    + "        LEFT JOIN\n"
                    + "    classes CLA ON CLA.id = STU.class_id\n"
                    + "        LEFT JOIN\n"
                    + "    courses COU ON COU.id = STU.course_id\n"
                    + "        LEFT JOIN\n"
                    + "    classrooms CRO ON CRO.id = CLA.classroom_id\n"
                    + "WHERE\n"
                    + "    STU.cod = '" + this.stu_cod + "'").executeQuery();
            rs.next();
            txtMatCursoCod.setText(rs.getString("COU.cod"));
            txtMatCursoNome.setText(rs.getString("COU.name"));
            txtMatTurmaCod.setText(rs.getString("CLA.cod"));
            txtMatTurmaNome.setText(rs.getString("CLA.name"));
            txtMatTurmaTurno.setText(rs.getString("CLA.turno"));
            txtMatSalaCod.setText(rs.getString("CRO.cod"));
            txtMatSalaNome.setText(rs.getString("CRO.name"));

        } catch (Exception e) {
            AgilitySec.showError(this, "#1153", e);
        }

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

    private void cepInvalido() {
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
    }

    private boolean check() {
        boolean r = false;

        brData.setLenient(false);
        //Validação dos Campos
        if (txtAluNome.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Nome do aluno\".");
            txtAluNome.grabFocus();

        } else if (txtAluRg.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"RG\".");
            txtAluRg.grabFocus();

        } else if (txtAluOrgaoRg.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Orgão Emissor\".");
            txtAluOrgaoRg.grabFocus();

        } else if (txtAluTel.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Telefone\".");
            txtAluTel.grabFocus();

        } else if (cboAluEstCivil.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Estado Civil\".");
            cboAluEstCivil.grabFocus();

        } else if (cboAluSexo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Por favor, selecione uma opção no campo \"Sexo\".");
            cboAluSexo.grabFocus();

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
                brData.parse(txtAluNasc.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Nascimento\".");
                txtAluNasc.grabFocus();
                err = 1;
            }
            try {
                brData.parse(txtAluDataRg.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data de Expedição\".");
                txtAluDataRg.grabFocus();
                err = 1;
            }
            if (err == 0) {
                r = true;
            }
        }
        return r;
    }

    private void save() {
        try {
            pst = con.prepareStatement("UPDATE students STU LEFT JOIN adresses AD ON AD.id=STU.adress_id SET "
                    + "STU.name='" + txtAluNome.getText() + "', "
                    + "STU.dataNasc='" + sqlData.format(brData.parse(txtAluNasc.getText())) + "',"
                    + "STU.sexo='" + cboAluSexo.getSelectedItem() + "',"
                    + "STU.estCivil='" + cboAluEstCivil.getSelectedItem() + "', "
                    + "STU.cpf='" + txtAluCpf.getText() + "',"
                    + "STU.rg='" + txtAluRg.getText() + "',"
                    + "STU.dataRg='" + sqlData.format(brData.parse(txtAluDataRg.getText())) + "',"
                    + "STU.orgaoRg='" + txtAluOrgaoRg.getText() + "',"
                    + "STU.bloodType='" + txtAluBloodType.getText() + "',"
                    + "STU.medicines='" + txtAluMed.getText() + "',"
                    + "STU.allergies='" + txtAluAlerg.getText() + "',"
                    + "STU.medicalObs='" + txtAluMedObs.getText() + "',"
                    + "STU.tel='" + txtAluTel.getText() + "',"
                    + "STU.email='" + txtAluEmail.getText() + "',"
                    + "STU.extraInfo='" + txtAluExtraInfo.getText() + "',"
                    + "AD.cep='" + txtCep.getText() + "', "
                    + "AD.logra='" + txtLogra.getText() + "', "
                    + "AD.num='" + txtNum.getText() + "', "
                    + "AD.compl='" + txtCompl.getText() + "', "
                    + "AD.bairro='" + txtBairro.getText() + "', "
                    + "AD.muni='" + txtMuni.getText() + "', "
                    + "AD.refer='" + txtRef.getText() + "', "
                    + "AD.uf='" + cboUf.getSelectedItem() + "', "
                    + "AD.modified_by='" + Login.emp_id + "' "
                    + "WHERE STU.cod='" + this.stu_cod + "'");
            if (pst.executeUpdate() > 0) {
                this.modified = true;
                this.dispose();
                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#1152", e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAluNome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAluRg = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMat = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtAluOrgaoRg = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        cboAluSexo = new javax.swing.JComboBox<>();
        cboAluEstCivil = new javax.swing.JComboBox<>();
        txtAluNasc = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtAluNasc = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        txtAluDataRg = new javax.swing.JTextField();
        try{
            MaskFormatter data= new MaskFormatter("##/##/####");
            txtAluDataRg = new JFormattedTextField(data);
        }
        catch (Exception e){
        }
        txtAluCpf = new javax.swing.JTextField();
        try{
            MaskFormatter cpf= new MaskFormatter("###.###.###-##");
            txtAluCpf = new JFormattedTextField(cpf);
        }
        catch (Exception e){
        }
        jpContato = new javax.swing.JPanel();
        txtAluTel = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtAluEmail = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jpResp = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAluExtraInfo = new javax.swing.JTextPane();
        jpEndereco = new javax.swing.JPanel();
        txtLogra = new javax.swing.JTextField();
        txtNum = new javax.swing.JTextField();
        txtBairro = new javax.swing.JTextField();
        txtCompl = new javax.swing.JTextField();
        txtMuni = new javax.swing.JTextField();
        txtCep = new javax.swing.JTextField();
        try{
            MaskFormatter cep= new MaskFormatter("##.###-###");
            txtCep = new JFormattedTextField(cep);
        }
        catch (Exception e){
        }
        jLabel21 = new javax.swing.JLabel();
        txtRef = new javax.swing.JTextField();
        cboUf = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtMatCursoCod = new javax.swing.JTextField();
        txtMatCursoNome = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtMatTurmaCod = new javax.swing.JTextField();
        txtMatTurmaNome = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtMatSalaNome = new javax.swing.JTextField();
        txtMatTurmaTurno = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtMatSalaCod = new javax.swing.JTextField();
        btnEditAcad = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jpFichaMedica = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtAluMed = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtAluAlerg = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAluMedObs = new javax.swing.JTextArea();
        txtAluBloodType = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnSal = new javax.swing.JButton();
        btnCan = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("EDITAR ALUNO (#");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setOpaque(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Pessoais"));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Nome do Aluno:");

        jLabel5.setText("CPF:");

        txtAluRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluRgActionPerformed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel49.setText("RG:");

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Estado Civil:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Data de Nasc.:");

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Matrícula:");

        txtMat.setEditable(false);
        txtMat.setBackground(new java.awt.Color(204, 204, 204));

        jLabel51.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel51.setText("Orgão Emissor:");

        txtAluOrgaoRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluOrgaoRgActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Sexo:");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Data de Expedição:");

        jLabel34.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(233, 2, 2));
        jLabel34.setText("*");

        jLabel36.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(233, 2, 2));
        jLabel36.setText("*");

        jLabel37.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(233, 2, 2));
        jLabel37.setText("*");

        jLabel38.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(233, 2, 2));
        jLabel38.setText("*");

        jLabel42.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(233, 2, 2));
        jLabel42.setText("*");

        jLabel44.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(233, 2, 2));
        jLabel44.setText("*");

        cboAluSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));
        cboAluSexo.setSelectedIndex(-1);

        cboAluEstCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)" }));
        cboAluEstCivil.setSelectedIndex(-1);

        txtAluDataRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluDataRgActionPerformed(evt);
            }
        });

        txtAluCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluCpfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel34)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel37))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAluNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel38)
                                .addGap(67, 67, 67)
                                .addComponent(jLabel7)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel42))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboAluSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboAluEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(txtAluCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel49)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel36))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtAluRg, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtAluDataRg, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAluOrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel51)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel44)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8)
                            .addComponent(jLabel34)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboAluSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboAluEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAluNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel42))
                        .addGap(33, 33, 33)))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel49)
                                .addComponent(jLabel36))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAluRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAluCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel51)
                        .addComponent(jLabel44))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAluDataRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAluOrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContato.setBorder(javax.swing.BorderFactory.createTitledBorder("Contato"));
        jpContato.setOpaque(false);

        jLabel35.setText("E-mail:");

        jLabel32.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel32.setText("Telefone:");

        jLabel45.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(233, 2, 2));
        jLabel45.setText("*");

        javax.swing.GroupLayout jpContatoLayout = new javax.swing.GroupLayout(jpContato);
        jpContato.setLayout(jpContatoLayout);
        jpContatoLayout.setHorizontalGroup(
            jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpContatoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(txtAluEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpContatoLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45))
                    .addComponent(txtAluTel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpContatoLayout.setVerticalGroup(
            jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpContatoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel45))
                .addGap(0, 0, 0)
                .addComponent(txtAluTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel35)
                .addGap(0, 0, 0)
                .addComponent(txtAluEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpResp.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Adicionais"));
        jpResp.setOpaque(false);

        jScrollPane3.setViewportView(txtAluExtraInfo);

        javax.swing.GroupLayout jpRespLayout = new javax.swing.GroupLayout(jpResp);
        jpResp.setLayout(jpRespLayout);
        jpRespLayout.setHorizontalGroup(
            jpRespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jpRespLayout.setVerticalGroup(
            jpRespLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        jpEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder("ENDEREÇO"));
        jpEndereco.setOpaque(false);

        txtLogra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLograActionPerformed(evt);
            }
        });

        txtNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumActionPerformed(evt);
            }
        });

        txtBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBairroActionPerformed(evt);
            }
        });

        txtCompl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComplActionPerformed(evt);
            }
        });

        txtMuni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMuniActionPerformed(evt);
            }
        });

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

        jLabel21.setText("Referência:");

        cboUf.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" }));
        cboUf.setSelectedIndex(-1);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Logradouro:");

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Número:");

        jLabel11.setText("Complemento:");

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Bairro:");

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel14.setText("UF:");

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setText("Município:");

        jLabel46.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(233, 2, 2));
        jLabel46.setText("*");

        jLabel47.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(233, 2, 2));
        jLabel47.setText("*");

        jLabel48.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(233, 2, 2));
        jLabel48.setText("*");

        jLabel50.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(233, 2, 2));
        jLabel50.setText("*");

        jLabel52.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(233, 2, 2));
        jLabel52.setText("*");

        jLabel54.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(233, 2, 2));
        jLabel54.setText("*");

        jLabel16.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel16.setText("CEP:");

        javax.swing.GroupLayout jpEnderecoLayout = new javax.swing.GroupLayout(jpEndereco);
        jpEndereco.setLayout(jpEnderecoLayout);
        jpEnderecoLayout.setHorizontalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRef)
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtBairro, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtLogra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel48))
                                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel50))))
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel54))
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel47)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addGap(0, 7, Short.MAX_VALUE)
                                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel52))))
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpEnderecoLayout.createSequentialGroup()
                    .addGap(257, 257, 257)
                    .addComponent(jLabel46)
                    .addContainerGap(264, Short.MAX_VALUE)))
        );
        jpEnderecoLayout.setVerticalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel16)
                .addGap(6, 6, 6)
                .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jLabel47)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel50)
                    .addComponent(jLabel52)
                    .addComponent(jLabel54))
                .addGap(0, 0, 0)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(0, 0, 0)
                .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
            .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpEnderecoLayout.createSequentialGroup()
                    .addGap(87, 87, 87)
                    .addComponent(jLabel46)
                    .addContainerGap(88, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpContato, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpResp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jpContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpResp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Dados do Aluno", jPanel2);

        jPanel6.setOpaque(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Matrícula"));
        jPanel4.setOpaque(false);

        jLabel29.setText("Curso:");

        txtMatCursoCod.setEditable(false);
        txtMatCursoCod.setBackground(new java.awt.Color(234, 234, 234));
        txtMatCursoCod.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMatCursoCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatCursoCodActionPerformed(evt);
            }
        });

        txtMatCursoNome.setEditable(false);

        jLabel13.setText("Turma:");

        txtMatTurmaCod.setEditable(false);
        txtMatTurmaCod.setBackground(new java.awt.Color(234, 234, 234));

        txtMatTurmaNome.setEditable(false);

        jLabel24.setText("Turno:");

        txtMatSalaNome.setEditable(false);

        txtMatTurmaTurno.setEditable(false);

        jLabel25.setText("Sala:");

        txtMatSalaCod.setEditable(false);
        txtMatSalaCod.setBackground(new java.awt.Color(234, 234, 234));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtMatCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMatCursoNome, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel29)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMatTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMatSalaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMatSalaNome, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                    .addComponent(txtMatTurmaNome))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(txtMatTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel25))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatCursoNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatTurmaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatSalaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatSalaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        btnEditAcad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEditAcad.setText("Alterar Dados");
        btnEditAcad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditAcadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditAcad))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditAcad)
                .addContainerGap(193, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Informações Acadêmicas", jPanel6);

        jPanel8.setOpaque(false);

        jpFichaMedica.setBorder(javax.swing.BorderFactory.createTitledBorder("Ficha Médica"));
        jpFichaMedica.setOpaque(false);
        jpFichaMedica.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setText("Medicamento de uso contínuo:");
        jpFichaMedica.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));
        jpFichaMedica.add(txtAluMed, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 266, -1));

        jLabel40.setText("Alergias:");
        jpFichaMedica.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));
        jpFichaMedica.add(txtAluAlerg, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 266, -1));

        jLabel41.setText("Observações:");
        jpFichaMedica.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 195, -1, -1));

        txtAluMedObs.setColumns(20);
        txtAluMedObs.setRows(2);
        jScrollPane4.setViewportView(txtAluMedObs);

        jpFichaMedica.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 670, 50));
        jpFichaMedica.add(txtAluBloodType, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 120, -1));

        jLabel53.setText("Tipo Sanguíneo:");
        jpFichaMedica.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpFichaMedica, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpFichaMedica, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ficha Médica", jPanel8);

        jPanel12.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 7, 1011, 452));
        jPanel12.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 465, 1013, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        jPanel12.add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(776, 473, 119, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        jPanel12.add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(901, 473, -1, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        jPanel12.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 1040, 530));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMatCursoCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatCursoCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatCursoCodActionPerformed

    private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja salvar alterações?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            if (check()) {
                this.modified = true;
                save();
            }
        }
    }//GEN-LAST:event_btnSalActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (q == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnEditAcadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditAcadActionPerformed
        AlunoMatricula am = new AlunoMatricula(new JFrame(), Integer.parseInt(txtMat.getText()));
        am.setVisible(true);
        if (am.modified) {
            this.modified = true;
            loadCourse();
        }
    }//GEN-LAST:event_btnEditAcadActionPerformed

    private void txtCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCepActionPerformed

    }//GEN-LAST:event_txtCepActionPerformed

    private void txtCepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCepFocusLost
        if (!txtCep.getText().equals("  .   -   ")) {
            buscaCep();
        }
    }//GEN-LAST:event_txtCepFocusLost

    private void txtMuniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMuniActionPerformed

    }//GEN-LAST:event_txtMuniActionPerformed

    private void txtComplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComplActionPerformed

    }//GEN-LAST:event_txtComplActionPerformed

    private void txtBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBairroActionPerformed

    }//GEN-LAST:event_txtBairroActionPerformed

    private void txtNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumActionPerformed

    private void txtLograActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLograActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLograActionPerformed

    private void txtAluCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluCpfActionPerformed

    private void txtAluDataRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluDataRgActionPerformed

    }//GEN-LAST:event_txtAluDataRgActionPerformed

    private void txtAluOrgaoRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluOrgaoRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluOrgaoRgActionPerformed

    private void txtAluRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluRgActionPerformed

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
            java.util.logging.Logger.getLogger(AlunosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlunosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlunosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlunosForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AlunosForm dialog = new AlunosForm("");
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
    private javax.swing.JButton btnEditAcad;
    private javax.swing.JButton btnSal;
    private javax.swing.JComboBox<String> cboAluEstCivil;
    private javax.swing.JComboBox<String> cboAluSexo;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel jpContato;
    private javax.swing.JPanel jpEndereco;
    private javax.swing.JPanel jpFichaMedica;
    private javax.swing.JPanel jpResp;
    private javax.swing.JTextField txtAluAlerg;
    private javax.swing.JTextField txtAluBloodType;
    private javax.swing.JTextField txtAluCpf;
    private javax.swing.JTextField txtAluDataRg;
    private javax.swing.JTextField txtAluEmail;
    private javax.swing.JTextPane txtAluExtraInfo;
    private javax.swing.JTextField txtAluMed;
    private javax.swing.JTextArea txtAluMedObs;
    private javax.swing.JTextField txtAluNasc;
    private javax.swing.JTextField txtAluNome;
    private javax.swing.JTextField txtAluOrgaoRg;
    private javax.swing.JTextField txtAluRg;
    private javax.swing.JTextField txtAluTel;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCep;
    private javax.swing.JTextField txtCompl;
    private javax.swing.JTextField txtLogra;
    private javax.swing.JTextField txtMat;
    private javax.swing.JTextField txtMatCursoCod;
    private javax.swing.JTextField txtMatCursoNome;
    private javax.swing.JTextField txtMatSalaCod;
    private javax.swing.JTextField txtMatSalaNome;
    private javax.swing.JTextField txtMatTurmaCod;
    private javax.swing.JTextField txtMatTurmaNome;
    private javax.swing.JTextField txtMatTurmaTurno;
    private javax.swing.JTextField txtMuni;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtRef;
    // End of variables declaration//GEN-END:variables

}
