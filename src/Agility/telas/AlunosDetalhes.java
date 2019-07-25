package Agility.telas;

import Agility.api.AgilityRel;
import static Agility.api.AgilityRel.openReport;
import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author jferreira
 */
public class AlunosDetalhes extends javax.swing.JDialog {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String idFDialog = null;

    String stu_id = null;
    String stu_cod = null;
    boolean stu_status = true;
    String adress_id = null;
    String respFin_id = null;
    String respAcad_id = null;
    String respEmerg_id = null;
    String course_id = null;
    String class_id = null;
    String cRoom_id = null;
    boolean modified;

    public AlunosDetalhes(JDialog parent, String codStu) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        this.stu_cod = codStu.replace("<html><strike>", "");
        this.setTitle(this.getTitle() + stu_cod + ") - Agility®");
        listData();
        loadResp();
    }

    private void listData() {
        DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfDateTime = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

        String sql = "SELECT * FROM students WHERE cod ='" + this.stu_cod + "'";
        try {
            rs = con.prepareStatement(sql).executeQuery();
            rs.next();
            //Verifica se o aluno está desligado
            if (rs.getString("status").equals("0")) {
                JOptionPane.showMessageDialog(this, "Este ALUNO ENCONTRA-SE DESLIGADO desta istituição.", "Atenção", JOptionPane.ERROR_MESSAGE + JOptionPane.OK_OPTION);
                btnDelAlu.setText("REATIVAR CADASTRO");
                btnDelAlu.setIcon(null);
                stu_status = false;
            }
            stu_id = rs.getString("id");
            adress_id = rs.getString("adress_id");
            respFin_id = rs.getString("respFin");
            respAcad_id = rs.getString("respAcad");
            respEmerg_id = rs.getString("respEmerg");
            course_id = rs.getString("course_id");
            class_id = rs.getString("class_id");

            txtNomeAlu.setText(rs.getString("name"));
            txtNasc.setText(dfDate.format(rs.getDate("datanasc")));
            txtMat.setText(rs.getString("cod"));
            txtCpf.setText(rs.getString("cpf"));
            txtRg.setText(rs.getString("rg"));
            txtDataRg.setText(dfDate.format(rs.getDate("dataRg")));
            txtCadBy.setText(rs.getString("created_by"));
            txtCadTime.setText(dfDateTime.format(rs.getTimestamp("created")) + "h");
            txtModBy.setText(rs.getString("modified_by"));
            txtModTime.setText((rs.getTimestamp("modified") != null) ? dfDateTime.format(rs.getTimestamp("modified")) + "h" : "");
            txtOrgaoRg.setText(rs.getString("orgaoRg"));
            txtEmail.setText(rs.getString("email"));
            txtTel.setText(rs.getString("tel"));
            txtEstCivil.setText(rs.getString("estCivil"));
            txtSexo.setText(rs.getString("sexo"));
            txtExtraInfo.setText(rs.getString("extraInfo"));
            txtMed.setText(rs.getString("medicines"));
            txtBloodType.setText(rs.getString("bloodType"));
            txtMedObs.setText(rs.getString("medicalObs"));
            txtAlerg.setText(rs.getString("allergies"));

            //ENDEREÇO
            try {
                rs = con.prepareStatement("SELECT * FROM adresses WHERE id = " + adress_id).executeQuery();
                if (rs.next()) {
                    txtLogra.setText(rs.getString("logra"));
                    txtNum.setText(rs.getString("num"));
                    txtBairro.setText(rs.getString("bairro"));
                    txtCompl.setText(rs.getString("compl"));
                    txtUf.setText(rs.getString("uf"));
                    txtMuni.setText(rs.getString("muni"));
                    txtRef.setText(rs.getString("refer"));
                    txtCep.setText(rs.getString("cep"));

                } else {
                    JOptionPane.showMessageDialog(null, "Não existe nenhum endereço cadastrado para este aluno !\nCódigo do Erro: #0019");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0020\nDetalhes do erro:\n" + e);
            }

            //CONTATO DE EMERGÊNCIA
            try {
                rs = con.prepareStatement("SELECT name,tel1 FROM responsibles WHERE id = " + respEmerg_id).executeQuery();
                if (rs.next()) {
                    lblEmeNome.setText(rs.getString("name"));
                    lblEmeCont.setText(rs.getString("tel1"));
                } else {
                    JOptionPane.showMessageDialog(null, "Não existe contato de emergência para este aluno!\nCódigo do Erro: #0017");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0018\nDetalhes do erro:\n" + e);
            }

            //CURSO
            try {
                rs = con.prepareStatement("SELECT cod, name, priceBase FROM courses WHERE id = " + course_id).executeQuery();
                if (rs.next()) {
                    txtCursoCod.setText(rs.getString("cod"));
                    txtCursoNome.setText(rs.getString("name"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0069\nDetalhes do erro:\n" + e);
            }

            //TURMA
            try {
                rs = con.prepareStatement("SELECT cod, name, turno, classroom_id FROM classes WHERE id = " + class_id).executeQuery();
                if (rs.next()) {
                    txtTurmaCod.setText(rs.getString("cod"));
                    txtTurmaNome.setText(rs.getString("name"));
                    txtTurmaTurno.setText(rs.getString("turno"));
                    cRoom_id = rs.getString("classroom_id");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0070\nDetalhes do erro:\n" + e);
            }

            //SALA
            try {
                rs = con.prepareStatement("SELECT cod, name FROM classrooms WHERE id = " + cRoom_id).executeQuery();
                if (rs.next()) {
                    txtMatSalaCod.setText(rs.getString("cod"));
                    txtMatSalaNome.setText(rs.getString("name"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0071\nDetalhes do erro:\n" + e);
            }

            //OCORRÊNCIAS
            try {
                DefaultTableModel dtmOco = (DefaultTableModel) tblOco.getModel();
                dtmOco.setNumRows(0);
                rs = con.prepareStatement("SELECT * FROM occurrences WHERE student_cod = " + txtMat.getText()).executeQuery();

                while (rs.next()) {
                    dtmOco.addRow(new String[]{rs.getString("id"), rs.getString("occurrence"), rs.getString("date")});
                }

                if (dtmOco.getRowCount() > 0) {
                    lblOco.setVisible(false);
                    btnDet.setVisible(true);
                } else {
                    lblOco.setVisible(true);
                    btnDet.setVisible(false);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0025\nDetalhes do erro:\n" + e);
            }

            //MENSALIDADES
            try {
                DefaultTableModel dtmMen = (DefaultTableModel) tblMen.getModel();
                dtmMen.setNumRows(0);
                Date now = new Date();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                rs = con.prepareStatement("SELECT * FROM tuitions WHERE student_cod = '" + txtMat.getText() + "' AND academic_year='" + Login.academic_year + "' ORDER BY venc ASC").executeQuery();
                while (rs.next()) {
                    dtmMen.addRow(new String[]{
                        rs.getString("id"),
                        rs.getString("refer"),
                        df.format(rs.getDate("venc")),
                        (rs.getString("valor_pago") == null ? (now.after(rs.getDate("venc")) ? "VENCIDO" : "EM ABERTO") : "PAGO"),
                        "R$ " + rs.getString("valor").replace(".", ",")
                    }
                    );
                }

                if (dtmMen.getRowCount() > 0) {
                    lblOco.setVisible(false);
                    btnDet.setVisible(true);
                } else {
                    lblOco.setVisible(true);
                    btnDet.setVisible(false);
                }
            } catch (Exception e) {
                AgilitySec.showError(this, "#0025", e);
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#0011", e);
        }

    }

    private void loadResp() {
        try {
            rs = con.prepareStatement("SELECT respFin.name, respAcad.name FROM students STU LEFT JOIN responsibles respFin ON STU.respFin=respFin.id LEFT JOIN responsibles respAcad ON STU.respAcad=respAcad.id WHERE STU.cod='" + this.stu_cod + "'").executeQuery();
            rs.next();
            txtRespFin.setText(rs.getString("respFin.name"));
            txtRespAcad.setText(rs.getString("respAcad.name"));
        } catch (Exception e) {
            AgilitySec.showError(this, "#1150", e);
        }
    }

    private void desativAluno() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        boolean valid = false;
        df.setLenient(false);
        String inputData = "";
        String msg = "Este aluno possui mensalidades";
        Set venc = new LinkedHashSet();
        Set pags = new LinkedHashSet();
        boolean r = true;
        try {
            //Busca mensalidades vencidas
            rs = con.prepareStatement("SELECT "
                    + "    refer\n"
                    + "FROM\n"
                    + "    tuitions\n"
                    + "WHERE\n"
                    + "    student_cod = '" + this.stu_cod + "'"
                    + "        AND valor_pago IS NULL"
                    + "        AND MONTH(venc) <= MONTH(CURDATE())").executeQuery();
            while (rs.next()) {
                venc.add(rs.getString(1));
            }

            //Busca mensalidades pagas adiantadas
            rs = con.prepareStatement("SELECT "
                    + "    refer\n"
                    + "FROM\n"
                    + "    tuitions\n"
                    + "WHERE\n"
                    + "    student_cod = '" + this.stu_cod + "'"
                    + "        AND valor_pago IS NOT NULL"
                    + "        AND MONTH(venc) > MONTH(CURDATE())").executeQuery();
            while (rs.next()) {
                pags.add(rs.getString(1));
            }

            //Ou tem vencidas, ou então tem pagas adiantadas, ou não tem nada.
            if (venc.size() > 0) {
                msg += " vencidas referente ao(s) mes(es):\n"
                        + "<html><b>" + venc.toString();
                msg += "\n\nSolicite a regularização ao responsável.";
                JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.PLAIN_MESSAGE);
            } else if (pags.size() > 0) {
                msg += " com pagamento adiantado listadas abaixo:\n"
                        + "<html><b>" + pags.toString();
                msg += "\n\nPor favor, efetue estorno das referidas mensalidades.";
                JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (Exception e) {
            AgilitySec.showError(this, "#1166", e);
            r = false;
        }

        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja desligar este aluno?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            while (!valid) {
                inputData = JOptionPane.showInputDialog(this, "Digite a data de desligamento:");
                try {
                    df.parse(inputData);
                    valid = true;
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data inválida.\nPor favor, digite uma data válida no formato \"dd/mm/aaaa\"");
                    valid = false;
                }
            }
            if (valid) {
                try {
                    //Atualiza o status = 0, desmatricula, seta data de desligamento
                    con.prepareStatement("UPDATE students "
                            + "SET "
                            + "    status = 0,"
                            + "    course_id = NULL,"
                            + "    class_id = NULL,"
                            + "    exitDate = '" + inputData + "'"
                            + "WHERE"
                            + "    cod = '" + this.stu_cod + "'").executeUpdate();

                    //Deleta mensalidades futuras que não estejam pagas. 
                    con.prepareStatement("DELETE FROM tuitions "
                            + "WHERE"
                            + "    student_cod = '" + this.stu_cod + "' "
                            + "    AND valor_pago IS NULL"
                            + "    AND month(venc) > month(CURDATE())").executeUpdate();

                } catch (Exception e) {
                    AgilitySec.showError(this, "#1165", e);
                    r = false;
                }
                //Ao entrar na tela de detalhes, avisar de aluno desligado. O botão de desligar, tem que tornar "reativar". Na tela Alunos, colocar risco no meio do nome do aluno

            }
            if (r) {
                JOptionPane.showMessageDialog(this, "Aluno desligado com sucesso.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                this.modified = true;
                this.dispose();
            }
        }
    }

    private void ativAluno() {
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja reativar este aluno?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (q == JOptionPane.YES_OPTION) {
            try {
                pst = con.prepareStatement("UPDATE students SET status=1 WHERE cod=" + stu_cod);
                if (pst.executeUpdate() > 0) {
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Cadastro ativado com sucesso!");
                }
            } catch (Exception e) {
                AgilitySec.showError(this, "#1167", e);
            }

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
        txtNomeAlu = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        txtRg = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDataRg = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        txtNasc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMat = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtOrgaoRg = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtRespFin = new javax.swing.JTextField();
        btnRespFin = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtRespAcad = new javax.swing.JTextField();
        btnRespAcad = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtCadBy = new javax.swing.JTextField();
        txtCadTime = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtModTime = new javax.swing.JTextField();
        txtModBy = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtEstCivil = new javax.swing.JTextField();
        txtSexo = new javax.swing.JTextField();
        jpEndereco = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txtCep = new javax.swing.JTextField();
        try{
            MaskFormatter cep= new MaskFormatter("##.###-###");
            txtCep = new JFormattedTextField(cep);
        }
        catch (Exception e){
        }
        txtLogra = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtNum = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        txtCompl = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        txtMuni = new javax.swing.JTextField();
        txtUf = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtRef = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jpContato = new javax.swing.JPanel();
        txtTel = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jpResp = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtExtraInfo = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jpOcorrencias = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOco = new javax.swing.JTable();
        lblOco = new javax.swing.JLabel();
        btnDet = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtCursoCod = new javax.swing.JTextField();
        txtCursoNome = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtTurmaCod = new javax.swing.JTextField();
        txtTurmaNome = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtTurmaTurno = new javax.swing.JTextField();
        txtMatSalaCod = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtMatSalaNome = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jpMensalidades = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMen = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jpFichaMedica = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtMed = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtAlerg = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lblEmeNome = new javax.swing.JLabel();
        lblEmeCont = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtMedObs = new javax.swing.JTextArea();
        txtBloodType = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnImpAluno = new javax.swing.JButton();
        btnImpRespFin = new javax.swing.JButton();
        btnImpQuaHor = new javax.swing.JButton();
        btnImpOco = new javax.swing.JButton();
        btnImpRespAcad = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnDelAlu = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnEfeMatri2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DADOS DO ALUNO (#");
        setResizable(false);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setOpaque(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("INFORMAÇÕES PESSOAIS"));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Nome do Aluno:");

        txtNomeAlu.setEditable(false);
        txtNomeAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeAluActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("CPF:");

        txtCpf.setEditable(false);
        txtCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfActionPerformed(evt);
            }
        });

        txtRg.setEditable(false);
        txtRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRgActionPerformed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel49.setText("RG:");

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Estado Civil:");

        txtDataRg.setEditable(false);
        txtDataRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataRgActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel50.setText("Data de Expedição:");

        txtNasc.setEditable(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Data de Nasc.:");

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Matrícula:");

        txtMat.setEditable(false);
        txtMat.setBackground(new java.awt.Color(204, 204, 204));

        jLabel51.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel51.setText("Orgão Emissor:");

        txtOrgaoRg.setEditable(false);
        txtOrgaoRg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrgaoRgActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Sexo:");

        txtRespFin.setEditable(false);
        txtRespFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRespFinActionPerformed(evt);
            }
        });

        btnRespFin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnRespFin.setToolTipText("Visualizar detalhes sobre responsável 1.");
        btnRespFin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnRespFin.setContentAreaFilled(false);
        btnRespFin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRespFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRespFinActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Responsável Finaceiro:");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Responsável Acadêmico:");

        txtRespAcad.setEditable(false);
        txtRespAcad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRespAcadActionPerformed(evt);
            }
        });

        btnRespAcad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnRespAcad.setToolTipText("Visualizar detalhes sobre o responsável 2.");
        btnRespAcad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnRespAcad.setContentAreaFilled(false);
        btnRespAcad.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRespAcad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRespAcadActionPerformed(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setOpaque(false);

        jLabel22.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel22.setText("Cadastrado por:");

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Cadastrado em:");

        txtCadBy.setEditable(false);

        txtCadTime.setEditable(false);
        txtCadTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCadTimeActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel23.setText("Modificado por:");

        txtModTime.setEditable(false);

        txtModBy.setEditable(false);
        txtModBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModByActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Modificado em:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtModBy)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtCadBy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel12))
                    .addComponent(jLabel9)
                    .addComponent(txtCadTime, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(txtModTime))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel12))
                .addGap(7, 7, 7)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtEstCivil.setEditable(false);

        txtSexo.setEditable(false);
        txtSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSexoActionPerformed(evt);
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
                            .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(174, 174, 174)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel49)
                                    .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDataRg, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtOrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtRespAcad, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnRespAcad, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNomeAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 36, Short.MAX_VALUE))
                            .addComponent(txtEstCivil))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeAlu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEstCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOrgaoRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDataRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel49))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel50)
                            .addGap(33, 33, 33))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtRespAcad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRespAcad, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder("ENDEREÇO"));
        jpEndereco.setOpaque(false);

        jLabel19.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel19.setText("CEP:");

        txtCep.setEditable(false);

        txtLogra.setEditable(false);

        jLabel44.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel44.setText("Número:");

        txtNum.setEditable(false);

        jLabel46.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel46.setText("Complemento:");

        txtCompl.setEditable(false);

        jLabel47.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel47.setText("Município:");

        txtMuni.setEditable(false);

        txtUf.setEditable(false);

        jLabel42.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel42.setText("UF:");

        txtBairro.setEditable(false);

        jLabel38.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel38.setText("Bairro:");

        jLabel48.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel48.setText("Referência:");

        txtRef.setEditable(false);
        txtRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRefActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel52.setText("Logradouro:");

        javax.swing.GroupLayout jpEnderecoLayout = new javax.swing.GroupLayout(jpEndereco);
        jpEndereco.setLayout(jpEnderecoLayout);
        jpEnderecoLayout.setHorizontalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48)
                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtRef, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpEnderecoLayout.createSequentialGroup()
                            .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEnderecoLayout.createSequentialGroup()
                                    .addComponent(jLabel38)
                                    .addGap(230, 230, 230))
                                .addGroup(jpEnderecoLayout.createSequentialGroup()
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtBairro, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLogra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel52))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jpEnderecoLayout.createSequentialGroup()
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel44)
                                        .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel46)
                                        .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jpEnderecoLayout.createSequentialGroup()
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel42)
                                        .addComponent(txtUf, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel47)
                                        .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpEnderecoLayout.setVerticalGroup(
            jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnderecoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel19)
                .addGap(0, 0, 0)
                .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jLabel46)
                    .addComponent(jLabel52))
                .addGap(4, 4, 4)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addGap(0, 0, 0)
                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addGap(0, 0, 0)
                        .addGroup(jpEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMuni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0)
                .addComponent(jLabel48)
                .addGap(0, 0, 0)
                .addComponent(txtRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpContato.setBorder(javax.swing.BorderFactory.createTitledBorder("CONTATO"));
        jpContato.setOpaque(false);

        txtTel.setEditable(false);

        jLabel35.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel35.setText("E-mail:");

        txtEmail.setEditable(false);

        jLabel32.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel32.setText("Telefone:");

        javax.swing.GroupLayout jpContatoLayout = new javax.swing.GroupLayout(jpContato);
        jpContato.setLayout(jpContatoLayout);
        jpContatoLayout.setHorizontalGroup(
            jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpContatoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel32)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpContatoLayout.setVerticalGroup(
            jpContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpContatoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel32)
                .addGap(0, 0, 0)
                .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel35)
                .addGap(0, 0, 0)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpResp.setBorder(javax.swing.BorderFactory.createTitledBorder("INFORMAÇÕES ADICIONAIS"));
        jpResp.setOpaque(false);

        txtExtraInfo.setEditable(false);
        jScrollPane3.setViewportView(txtExtraInfo);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jpEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jpResp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jpContato, javax.swing.GroupLayout.PREFERRED_SIZE, 258, Short.MAX_VALUE))))
                .addContainerGap())
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
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Dados do Aluno", jPanel2);

        jPanel6.setOpaque(false);

        jpOcorrencias.setBorder(javax.swing.BorderFactory.createTitledBorder("OCORRÊNCIAS"));
        jpOcorrencias.setOpaque(false);

        tblOco.setAutoCreateRowSorter(true);
        tblOco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Ocorrência", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOco.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblOco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblOcoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblOco);
        if (tblOco.getColumnModel().getColumnCount() > 0) {
            tblOco.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblOco.getColumnModel().getColumn(1).setPreferredWidth(455);
            tblOco.getColumnModel().getColumn(2).setPreferredWidth(124);
        }

        lblOco.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblOco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOco.setText("Nenhuma ocorrência registrada até o momento.");

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpOcorrenciasLayout = new javax.swing.GroupLayout(jpOcorrencias);
        jpOcorrencias.setLayout(jpOcorrenciasLayout);
        jpOcorrenciasLayout.setHorizontalGroup(
            jpOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpOcorrenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDet)
                .addContainerGap())
        );
        jpOcorrenciasLayout.setVerticalGroup(
            jpOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOcorrenciasLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDet, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOco))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("DADOS DE MATRÍCULA"));
        jPanel4.setOpaque(false);

        jLabel29.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel29.setText("Curso:");

        txtCursoCod.setEditable(false);
        txtCursoCod.setBackground(new java.awt.Color(234, 234, 234));
        txtCursoCod.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCursoCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCursoCodActionPerformed(evt);
            }
        });

        txtCursoNome.setEditable(false);

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel13.setText("Turma:");

        txtTurmaCod.setEditable(false);
        txtTurmaCod.setBackground(new java.awt.Color(234, 234, 234));

        txtTurmaNome.setEditable(false);

        jLabel24.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel24.setText("Turno:");

        txtTurmaTurno.setEditable(false);

        txtMatSalaCod.setEditable(false);
        txtMatSalaCod.setBackground(new java.awt.Color(234, 234, 234));

        jLabel25.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel25.setText("Sala:");

        txtMatSalaNome.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel25)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCursoNome, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(txtMatSalaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMatSalaNome))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(txtTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTurmaNome, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addComponent(txtTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCursoNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTurmaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatSalaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMatSalaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpOcorrencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpOcorrencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Informações Acadêmicas", jPanel6);

        jPanel7.setOpaque(false);

        jpMensalidades.setBorder(javax.swing.BorderFactory.createTitledBorder("MENSALIDADES"));
        jpMensalidades.setOpaque(false);

        tblMen.setAutoCreateRowSorter(true);
        tblMen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Mês de Ref.", "Vencimento", "Situação", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMen.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblMen);

        javax.swing.GroupLayout jpMensalidadesLayout = new javax.swing.GroupLayout(jpMensalidades);
        jpMensalidades.setLayout(jpMensalidadesLayout);
        jpMensalidadesLayout.setHorizontalGroup(
            jpMensalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
        );
        jpMensalidadesLayout.setVerticalGroup(
            jpMensalidadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMensalidadesLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpMensalidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpMensalidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(117, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Mensalidades", jPanel7);

        jPanel8.setOpaque(false);

        jpFichaMedica.setBorder(javax.swing.BorderFactory.createTitledBorder("FICHA MÉDICA"));
        jpFichaMedica.setOpaque(false);
        jpFichaMedica.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setText("Medicamento de uso contínuo:");
        jpFichaMedica.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        txtMed.setEditable(false);
        jpFichaMedica.add(txtMed, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 266, -1));

        jLabel40.setText("Alergias:");
        jpFichaMedica.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, -1, -1));

        txtAlerg.setEditable(false);
        jpFichaMedica.add(txtAlerg, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 266, -1));

        jLabel41.setText("Observações:");
        jpFichaMedica.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 195, -1, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Em caso de emergência, contatar:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), java.awt.Color.red)); // NOI18N
        jPanel3.setOpaque(false);

        jLabel43.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel43.setText("Nome:");

        jLabel45.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel45.setText("Contato:");

        lblEmeNome.setText("lblEmeNome");

        lblEmeCont.setText("lblEmeCont");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmeNome)
                    .addComponent(lblEmeCont))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(lblEmeNome))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(lblEmeCont))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jpFichaMedica.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 390, -1));

        txtMedObs.setEditable(false);
        txtMedObs.setColumns(20);
        txtMedObs.setRows(2);
        jScrollPane4.setViewportView(txtMedObs);

        jpFichaMedica.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 670, 50));

        txtBloodType.setEditable(false);
        jpFichaMedica.add(txtBloodType, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 120, -1));

        jLabel53.setText("Tipo Sanguíneo:");
        jpFichaMedica.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpFichaMedica, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpFichaMedica, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(282, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Ficha Médica", jPanel8);

        jPanel5.setOpaque(false);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("SELECIONE UMA OPÇÃO"));
        jPanel10.setOpaque(false);

        btnImpAluno.setText("<html><center>Dados do<br>Aluno");
        btnImpAluno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpAlunoActionPerformed(evt);
            }
        });

        btnImpRespFin.setText("<html><center>Dados do<br>Responsável Financeiro");
        btnImpRespFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpRespFinActionPerformed(evt);
            }
        });

        btnImpQuaHor.setText("Quadro de Horários");
        btnImpQuaHor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpQuaHorActionPerformed(evt);
            }
        });

        btnImpOco.setText("<html><center>Ocorrências");
        btnImpOco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpOcoActionPerformed(evt);
            }
        });

        btnImpRespAcad.setText("<html><center>Dados do<br>Responsável Acadêmico");
        btnImpRespAcad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImpRespAcadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnImpAluno, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImpRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImpRespAcad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(btnImpOco, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImpQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImpAluno, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImpRespFin, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImpRespAcad, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImpOco, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImpQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(241, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(351, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Imprimir", jPanel5);

        jPanel12.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 7, -1, -1));
        jPanel12.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 587, 1010, -1));

        btnDelAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDelAlu.setText("Desligar Aluno");
        btnDelAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelAluActionPerformed(evt);
            }
        });
        jPanel12.add(btnDelAlu, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 595, 200, 60));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492599271_Edit-Male-User.png"))); // NOI18N
        btnEdit.setText("Alterar Informações");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel12.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(614, 595, -1, -1));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel12.add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 595, 200, -1));

        btnEfeMatri2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492599197_Add-Male-User.png"))); // NOI18N
        btnEfeMatri2.setText("Novo Aluno");
        btnEfeMatri2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEfeMatri2ActionPerformed(evt);
            }
        });
        jPanel12.add(btnEfeMatri2, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 595, 200, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        jPanel12.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 3, 1020, 656));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAluActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeAluActionPerformed

    private void txtCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfActionPerformed

    private void txtRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRgActionPerformed

    private void btnRespAcadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRespAcadActionPerformed
        ResponsaveisDetalhes rD = new ResponsaveisDetalhes(respAcad_id);
        rD.setVisible(true);
        if (rD.modified) {
            loadResp();
        }
    }//GEN-LAST:event_btnRespAcadActionPerformed

    private void btnRespFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRespFinActionPerformed
        ResponsaveisDetalhes rD = new ResponsaveisDetalhes(respFin_id);
        rD.setVisible(true);
        if (rD.modified) {
            loadResp();
        }
    }//GEN-LAST:event_btnRespFinActionPerformed

    private void txtCursoCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCursoCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCursoCodActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnDelAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelAluActionPerformed
        if (stu_status) {
            desativAluno();
        } else {
            ativAluno();
        }
    }//GEN-LAST:event_btnDelAluActionPerformed

    private void txtDataRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataRgActionPerformed

    private void txtOrgaoRgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrgaoRgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrgaoRgActionPerformed

    private void txtCadTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCadTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCadTimeActionPerformed

    private void txtModByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModByActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModByActionPerformed

    private void txtRespFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRespFinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRespFinActionPerformed

    private void txtRespAcadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRespAcadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRespAcadActionPerformed

    private void txtRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRefActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblOco.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um ocorrência na tabela.");
        } else {
            new OcorrenciaDetalhes(this, tblOco.getValueAt(tblOco.getSelectedRow(), 0).toString()).setVisible(true);
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void tblOcoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblOcoKeyReleased
        System.out.println(tblMen.getColumnModel().getColumn(0).getWidth());
        System.out.println(tblMen.getColumnModel().getColumn(1).getWidth());
        System.out.println(tblMen.getColumnModel().getColumn(2).getWidth());
        System.out.println(tblMen.getColumnModel().getColumn(3).getWidth());
    }//GEN-LAST:event_tblOcoKeyReleased

    private void btnImpRespAcadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpRespAcadActionPerformed
        Map<String, Object> params = new HashMap<>();
        params.put("respId", this.respAcad_id);

        try {
            JasperPrint document = JasperFillManager.fillReport(AgilityRel.url + "Responsaveis/RespDados.jasper", params, con);
            openReport(document);
        } catch (Exception e) {
            AgilitySec.showErrorRel("#1148", e);

        }
    }//GEN-LAST:event_btnImpRespAcadActionPerformed

    private void btnImpOcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpOcoActionPerformed
        if (tblOco.getModel().getRowCount() > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("studentCod", txtMat.getText());

            try {
                JasperPrint document = JasperFillManager.fillReport(AgilityRel.url + "Ocorrencias/OcoByAlu.jasper", params, con);
                openReport(document);
            } catch (Exception e) {
                AgilitySec.showErrorRel("#1149", e);

            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum dado a ser exibido!");
        }
    }//GEN-LAST:event_btnImpOcoActionPerformed

    private void btnImpQuaHorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpQuaHorActionPerformed
        AgilityRel.openReport(AgilityRel.turQuaHor(this.class_id));
    }//GEN-LAST:event_btnImpQuaHorActionPerformed

    private void btnImpAlunoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpAlunoActionPerformed
        AgilityRel.aluDados(txtMat.getText());
    }//GEN-LAST:event_btnImpAlunoActionPerformed

    private void btnImpRespFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImpRespFinActionPerformed
        Map<String, Object> params = new HashMap<>();
        params.put("respId", this.respFin_id);

        try {
            JasperPrint document = JasperFillManager.fillReport(AgilityRel.url + "Responsaveis/RespDados.jasper", params, con);
            openReport(document);
        } catch (Exception e) {
            AgilitySec.showErrorRel("#1147", e);

        }
    }//GEN-LAST:event_btnImpRespFinActionPerformed

    private void txtSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSexoActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        AlunosForm af = new AlunosForm(txtMat.getText());
        af.setVisible(true);
        if (af.modified) {
            this.modified = true;
            listData();
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEfeMatri2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfeMatri2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEfeMatri2ActionPerformed

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
            java.util.logging.Logger.getLogger(AlunosDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlunosDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlunosDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlunosDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AlunosDetalhes dialog = new AlunosDetalhes(new JDialog(), "");
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
    private javax.swing.JButton btnDelAlu;
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEfeMatri2;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnImpAluno;
    private javax.swing.JButton btnImpOco;
    private javax.swing.JButton btnImpQuaHor;
    private javax.swing.JButton btnImpRespAcad;
    private javax.swing.JButton btnImpRespFin;
    private javax.swing.JButton btnRespAcad;
    private javax.swing.JButton btnRespFin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JPanel jpContato;
    private javax.swing.JPanel jpEndereco;
    private javax.swing.JPanel jpFichaMedica;
    private javax.swing.JPanel jpMensalidades;
    private javax.swing.JPanel jpOcorrencias;
    private javax.swing.JPanel jpResp;
    private javax.swing.JLabel lblEmeCont;
    private javax.swing.JLabel lblEmeNome;
    private javax.swing.JLabel lblOco;
    private javax.swing.JTable tblMen;
    private javax.swing.JTable tblOco;
    private javax.swing.JTextField txtAlerg;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtBloodType;
    private javax.swing.JTextField txtCadBy;
    private javax.swing.JTextField txtCadTime;
    private javax.swing.JTextField txtCep;
    private javax.swing.JTextField txtCompl;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtCursoCod;
    private javax.swing.JTextField txtCursoNome;
    private javax.swing.JTextField txtDataRg;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEstCivil;
    private javax.swing.JTextPane txtExtraInfo;
    private javax.swing.JTextField txtLogra;
    private javax.swing.JTextField txtMat;
    private javax.swing.JTextField txtMatSalaCod;
    private javax.swing.JTextField txtMatSalaNome;
    private javax.swing.JTextField txtMed;
    private javax.swing.JTextArea txtMedObs;
    private javax.swing.JTextField txtModBy;
    private javax.swing.JTextField txtModTime;
    private javax.swing.JTextField txtMuni;
    private javax.swing.JTextField txtNasc;
    private javax.swing.JTextField txtNomeAlu;
    private javax.swing.JTextField txtNum;
    private javax.swing.JTextField txtOrgaoRg;
    private javax.swing.JTextField txtRef;
    private javax.swing.JTextField txtRespAcad;
    private javax.swing.JTextField txtRespFin;
    private javax.swing.JTextField txtRg;
    private javax.swing.JTextField txtSexo;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtTurmaCod;
    private javax.swing.JTextField txtTurmaNome;
    private javax.swing.JTextField txtTurmaTurno;
    private javax.swing.JTextField txtUf;
    // End of variables declaration//GEN-END:variables

}
