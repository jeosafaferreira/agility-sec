package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jferreira
 */
public class AlunoMatricula extends javax.swing.JDialog {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String sql, sqlToDi, turnoIsSelec, courseName, className, turno;

    String cursoId[], cursoCod[], cursoValor[], classId[], classCod[], classTurno[], turmaId[], turmaCod[];
    DefaultTableModel dtmAlu;
    DefaultListModel modCurso = new DefaultListModel();
    boolean modified;

    public AlunoMatricula(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        tblAlunos.setVisible(false);
        lblTotAlu.setVisible(false);
        getCourses();
    }

    public AlunoMatricula(JFrame parent, int codStu) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        txtAluCod.setText(Integer.toString(codStu));
        tblAlunos.setVisible(false);
        lblTotAlu.setVisible(false);
        searchStu();
        getCourses();
    }

    private void getCourses() {
        try {
            pst = con.prepareStatement("SELECT id, cod, name, priceBase FROM courses");
            rs = pst.executeQuery();
            rs.last();
            cursoId = new String[rs.getRow()];
            cursoCod = new String[rs.getRow()];
            cursoValor = new String[rs.getRow()];

            rs.absolute(0);
            int i = 0;
            while (rs.next()) {
                cursoId[i] = rs.getString("id");
                cursoCod[i] = rs.getString("cod");
                cursoValor[i] = rs.getString("priceBase");
                cboCurso.addItem(rs.getString("name"));
                i++;
            }
            System.out.println(cursoCod);
            cboCurso.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao listar cursos.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0062\nDetalhes do erro:\n" + e);
            this.dispose();
        }

    }

    private void getClasses(String turno) {
        cboTurmaNome.removeAllItems();
        cboTurmaTurno.setEnabled(true);
        txtTurmaCod.setEnabled(true);
        btnPesqTurma.setEnabled(true);
        cboTurmaNome.setEnabled(true);
        try {
            if (turno.equals("")) {
                sql = "SELECT id, cod, name, turno, course_id FROM classes WHERE course_id=" + cursoId[cboCurso.getSelectedIndex()];
            } else {
                sql = "SELECT id, cod, name, turno, course_id FROM classes WHERE turno = '" + turno + "' and course_id=" + cursoId[cboCurso.getSelectedIndex()];
            }

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.last();
            classCod = new String[rs.getRow()];
            classId = new String[rs.getRow()];
            classTurno = new String[rs.getRow()];
            rs.absolute(0);
            int i = 0;
            while (rs.next()) {
                classTurno[i] = rs.getString("turno");
                classCod[i] = rs.getString("cod");
                classId[i] = rs.getString("id");
                cboTurmaNome.addItem(rs.getString("name"));
                i++;
            }
            cboTurmaNome.setSelectedIndex(-1);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao listar turmas.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0065\nDetalhes do erro:\n" + e);
            this.dispose();
        }

    }

    private void getStuClass() {
        //Lista estudantes da turma selecionada
        dtmAlu = (DefaultTableModel) tblAlunos.getModel();
        dtmAlu.setNumRows(0);

        tblAlunos.setVisible(true);
        try {
            //Pega capacidade da turma
            rs = con.prepareStatement("SELECT capacity FROM classes WHERE id='" + classId[cboTurmaNome.getSelectedIndex()] + "'").executeQuery();
            rs.next();
            lblCapacity.setText("/" + rs.getString(1));

            //Pega alunos da turma
            rs = con.prepareStatement(""
                    + "SELECT "
                    + "    cod, name"
                    + " FROM"
                    + "    students"
                    + " WHERE"
                    + "    class_id = " + classId[cboTurmaNome.getSelectedIndex()]
            ).executeQuery();
            while (rs.next()) {
                dtmAlu.addRow(new String[]{
                    rs.getString("cod"),
                    rs.getString("name")
                });
            }
            rs.last();
            lblTotAlu.setText(Integer.toString(rs.getRow()));
            lblTotAlu.setVisible(true);

        } catch (Exception e) {
            AgilitySec.showError(this, "#0066", e);
        }

    }

    private void searchStu() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        int row = 0;
        sql = "SELECT "
                + "    STU.cod,"
                + "    STU.name,"
                + "    DATE_FORMAT(STU.created, '%d/%m/%Y - %T'), "
                + "    DATE_FORMAT(STU.dataNasc, '%d/%m/%Y'), "
                + "    COU.name,"
                + "    CLA.name,"
                + "    CLA.turno "
                + "FROM"
                + "    students STU "
                + "        LEFT JOIN"
                + "    courses COU ON COU.id = STU.course_id"
                + "        LEFT JOIN"
                + "    classes CLA ON CLA.id = STU.class_id "
                + "WHERE STU.status=1 AND ";
        sqlToDi = "SELECT cod, name, DATE_FORMAT(dataNasc, '%d/%m/%Y'), DATE_FORMAT(created, '%d/%m/%Y - %T') FROM students WHERE status=1 AND ";

        if (txtAluNome.getText().isEmpty()) {
            //COMPLEMENTA CLAUSULA WHERE
            sql += "STU.cod LIKE '%" + txtAluCod.getText() + "%'";
            sqlToDi += "cod LIKE '%" + txtAluCod.getText() + "%'";
        } else {
            sql += "STU.name LIKE '%" + txtAluNome.getText() + "%' ";
            sqlToDi += "name LIKE '%" + txtAluNome.getText() + "%' ";
        }

        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Matrícula", "Nome do Aluno", "Data de Nasc.", "Cadastrado em:"};
                    int cols[] = {50, 250, 0, 0};
                    Dialog dAlu = new Dialog(this, true, sqlToDi, headers, cols);
                    dAlu.setVisible(true);
                    row = dAlu.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);
                    this.courseName = rs.getString("COU.name");
                    this.className = rs.getString("CLA.name");
                    this.turno = rs.getString("CLA.turno");

                    txtAluCod.setText(rs.getString("STU.cod"));
                    txtAluNome.setText(rs.getString("STU.name"));
                    txtAluCad.setText(rs.getString(3));
                    txtAluNasc.setText(rs.getString(4));

                    txtAluCod.setEnabled(false);
                    txtAluNome.setEnabled(false);

                    txtAluCod.setEditable(false);
                    txtAluNome.setEditable(false);
                    btnPesqAlu.setEnabled(false);
                    txtCursoCod.grabFocus();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                txtAluCod.grabFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao pesquisar aluno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0063\nDetalhes do erro:\n" + e);
        }
    }

    private void searchCourse() {
        int row = 0;

        sql = "SELECT id, cod, name FROM courses WHERE cod LIKE '%" + txtCursoCod.getText() + "%' ";
        sqlToDi = "SELECT cod, name FROM courses WHERE cod LIKE '%" + txtCursoCod.getText() + "%' ";
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Código", "Nome do Curso"};
                    int cols[] = {50, 0};
                    Dialog dAlu = new Dialog(this, true, sqlToDi, headers, cols);
                    dAlu.setVisible(true);
                    row = dAlu.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);
                    cboCurso.setSelectedItem(rs.getString("name"));
                    txtCursoCod.setText(cursoCod[cboCurso.getSelectedIndex()]);

                    txtCursoCod.setEnabled(false);
                    cboCurso.setEnabled(false);
                    btnPesqCurso.setEnabled(false);
                    getClasses("");
                    txtTurmaCod.grabFocus();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Curso não encontrado.");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao pesquisar curso.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0064\nDetalhes do erro:\n" + e);
        }
    }

    private void searchClass() {
        int row = 0;

        if (cboTurmaTurno.getSelectedIndex() == -1) {
            sql = "SELECT id, cod, name, turno FROM classes WHERE course_id=" + cursoId[cboCurso.getSelectedIndex()] + " and cod LIKE '%" + txtTurmaCod.getText() + "%' ";
            sqlToDi = "SELECT cod, name, turno FROM classes WHERE course_id=" + cursoId[cboCurso.getSelectedIndex()] + " and cod LIKE '%" + txtTurmaCod.getText() + "%' ";
        } else {
            sql = "SELECT id, cod, name, turno FROM classes WHERE turno ='" + cboTurmaTurno.getSelectedItem() + "' and course_id=" + cursoId[cboCurso.getSelectedIndex()] + " and cod LIKE '%" + txtTurmaCod.getText() + "%' ";
            sqlToDi = "SELECT cod, name, turno FROM classes WHERE turno ='" + cboTurmaTurno.getSelectedItem() + "' course_id=" + cursoId[cboCurso.getSelectedIndex()] + " and cod LIKE '%" + txtTurmaCod.getText() + "%' ";
        }
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Código", "Nome da Turma", "Turno"};
                    int cols[] = {0, 0, 0};
                    Dialog d = new Dialog(this, true, sqlToDi, headers, cols);
                    d.setVisible(true);
                    row = d.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);
                    cboTurmaNome.setSelectedItem(rs.getString("name"));
                    cboTurmaTurno.setSelectedItem(rs.getString("turno"));
                    txtTurmaCod.setText(rs.getString("cod"));
                    cboTurmaTurno.setEnabled(false);
                    txtTurmaCod.setEnabled(false);
                    cboTurmaNome.setEnabled(false);
                    btnPesqTurma.setEnabled(false);
                    getStuClass();
                    btnSave.grabFocus();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Turma não encontrada.");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao pesquisar turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0064\nDetalhes do erro:\n" + e);
        }
    }

    private void showDisponibleClasses() {
        String result = "";
        try {

            rs = con.prepareStatement(""
                    + "SELECT "
                    + "    CLA.cod, CLA.name, CLA.turno, COUNT(STU.cod) AS totStu, CLA.capacity"
                    + " FROM"
                    + "    classes CLA"
                    + "        LEFT JOIN"
                    + "    students STU ON STU.class_id = CLA.id"
                    + " WHERE"
                    + "    CLA.course_id = " + cursoId[cboCurso.getSelectedIndex()]
                    + " GROUP BY CLA.cod").executeQuery();
            while (rs.next()) {
                if (rs.getInt("CLA.capacity") > rs.getInt("totStu")) {
                    result += "<html><b>Turma: </b>" + rs.getString("CLA.cod") + " - " + rs.getString("CLA.name") + " \n"
                            + "<html><b>Turno: </b>" + rs.getString("CLA.turno") + "\n"
                            + "<html><b>Lotação: </b>" + rs.getString("totStu") + "/" + rs.getString("CLA.capacity") + "\n\n";

                }
            }
            if (result.equals("")) {
                result = "<html><b>Nenhuma turma disponível no momento.";
            }
            JOptionPane.showMessageDialog(this, "Desculpe. Esta turma está lotada!\n\nLista de turmas disponíveis para este curso:\n" + result);
        } catch (Exception e) {
            AgilitySec.showError(this, "#1155", e);
        }

    }

    private boolean check() {
        boolean r = false;
        if (txtAluCod.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione o(a) aluno(a) a ser matriculado(a).");
            txtAluCod.grabFocus();
        } else if (txtCursoCod.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um curso.");
            txtCursoCod.grabFocus();
        } else if (txtTurmaCod.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma turma.");
            txtTurmaCod.grabFocus();
        } else {
            int totStu = Integer.parseInt(lblTotAlu.getText());
            int limStu = Integer.parseInt(lblCapacity.getText().replace("/", ""));
            if (totStu < limStu) {
                r = true;
            } else {
                //Turma lotada. Vai mostrar na tela as que estão disponíveis.
                showDisponibleClasses();
            }
        }
        return r;
    }

    private String month(int i) {
        String month = null;
        switch (i) {
            case 1:
                month = "JANEIRO";
                break;
            case 2:
                month = "FEVEREIRO";
                break;
            case 3:
                month = "MARÇO";
                break;
            case 4:
                month = "ABRIL";
                break;
            case 5:
                month = "MAIO";
                break;
            case 6:
                month = "JUNHO";
                break;
            case 7:
                month = "JULHO";
                break;
            case 8:
                month = "AGOSTO";
                break;
            case 9:
                month = "SETEMBRO";
                break;
            case 10:
                month = "OUTUBRO";
                break;
            case 11:
                month = "NOVEMBRO";
                break;
            case 12:
                month = "DEZEMBRO";
                break;
        }
        return month;
    }

    private void save() {
        Set<String> oldTuitions = new LinkedHashSet<>();
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");
        boolean tuitions = false;

        try {

            //Matricula
            pst = con.prepareStatement("UPDATE students SET course_id = ?, class_id = ?, modified = NOW() WHERE cod=?");
            pst.setString(1, cursoId[cboCurso.getSelectedIndex()]);
            pst.setString(2, classId[cboTurmaNome.getSelectedIndex()]);
            pst.setString(3, txtAluCod.getText());
            if (pst.executeUpdate() > 0) {
                //MENSALIDADES ------------------------------------------------------------------------------
                //verifica se já existe
                rs = con.prepareStatement("SELECT id from tuitions WHERE student_cod='" + txtAluCod.getText() + "' AND academic_year='" + Login.academic_year + "'").executeQuery();
                while (rs.next()) {
                    oldTuitions.add(rs.getString("id"));
                }
                System.out.println(oldTuitions);
                if (oldTuitions.size() > 0) {
                    //Então existe, coloco na lista quais são os ID's das tuitions que tem daqui pra frente que ainda não foram pagas e faço UPDATE
                    oldTuitions.clear();

                    rs = con.prepareStatement("SELECT id from tuitions WHERE student_cod='" + txtAluCod.getText() + "' AND academic_year='" + Login.academic_year + "' AND venc > '" + dateSql.format(new Date()) + "' AND valor_pago IS NULL").executeQuery();
                    rs.last();
                    //Verifico se tem alguma mensalidade pra ser alterada (Daqui pra frente, que não esteja paga)
                    if (rs.getRow() > 0) {
                        rs.absolute(0);
                        while (rs.next()) {
                            oldTuitions.add(rs.getString("id"));
                        }
                        for (Object tuitions_id : oldTuitions.toArray()) {
                            pst = con.prepareStatement("UPDATE tuitions SET "
                                    + "valor='" + cursoValor[cboCurso.getSelectedIndex()] + "'"
                                    + ",modified='" + dateBr.format(new Date()) + "'"
                                    + ", modified_by='" + Login.emp_id + "'"
                                    + "WHERE id='" + tuitions_id + "'"
                            );
                            tuitions = pst.executeUpdate() > 0;
                        }
                    }
                } else {
                    //Então não existe, faço INSERT
                    rs = con.prepareStatement("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'agility' AND TABLE_NAME = 'tuitions'").executeQuery();
                    rs.next();

                    String pri_parcela = rs.getString(1);
                    if (pri_parcela == null) {
                        pri_parcela = "1";
                    }

                    for (int parc = 1; parc < 13; parc++) {
                        pst = con.prepareStatement("INSERT INTO tuitions SET "
                                + "student_cod='" + txtAluCod.getText() + "' "
                                + ",parcela='" + parc + "' "
                                + ",tot_parcela='12' "
                                + ",pri_parcela='" + pri_parcela + "' "
                                + ",valor='" + cursoValor[cboCurso.getSelectedIndex()] + "'"
                                + ",venc='" + Login.academic_year + "/" + parc + "/01'"
                                + ",refer='" + month(parc) + "'"
                                + ",academic_year='" + Login.academic_year + "' "
                                + ",created='" + dateBr.format(new Date()) + "' "
                                + ",created_by='" + Login.emp_id + "'");
                        tuitions = pst.executeUpdate() > 0;
                    }
                }

                if (tuitions) {
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Aluno(a) matriculado(a) com sucesso!");
                    this.modified = true;
                } else {
                    AgilitySec.showError(this, "#1068");
                }

            } else {
                AgilitySec.showError(this, "#1067");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao matricular este aluno(a).\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0067\nDetalhes do erro:\n" + e);
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
        btnPesqAlu = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtAluNasc = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAluCad = new javax.swing.JTextField();
        btnEraseAlu = new javax.swing.JButton();
        pnlCurso = new javax.swing.JLayeredPane();
        jLabel2 = new javax.swing.JLabel();
        txtCursoCod = new javax.swing.JTextField();
        btnPesqCurso = new javax.swing.JButton();
        btnEraseCur = new javax.swing.JButton();
        cboCurso = new javax.swing.JComboBox<>();
        pnlTurma = new javax.swing.JLayeredPane();
        jLabel4 = new javax.swing.JLabel();
        txtTurmaCod = new javax.swing.JTextField();
        cboTurmaNome = new javax.swing.JComboBox<>();
        btnPesqTurma = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTotAlu = new javax.swing.JLabel();
        btnEraseTur = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cboTurmaTurno = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblCapacity = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MATRICULAR ALUNO");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ALUNO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        pnlAluno.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
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
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nome:");

        btnPesqAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnPesqAlu.setText("...");
        btnPesqAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqAluActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Data de Nasc.:");

        txtAluNasc.setEditable(false);
        txtAluNasc.setEnabled(false);
        txtAluNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluNascActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)))
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(pnlAlunoLayout.createSequentialGroup()
                                .addComponent(txtAluCad, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        getContentPane().add(pnlAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, -1));

        pnlCurso.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CURSO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código:");

        txtCursoCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCursoCodActionPerformed(evt);
            }
        });
        txtCursoCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCursoCodKeyTyped(evt);
            }
        });

        btnPesqCurso.setText("...");
        btnPesqCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqCursoActionPerformed(evt);
            }
        });

        btnEraseCur.setText("Limpar Dados");
        btnEraseCur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEraseCurActionPerformed(evt);
            }
        });

        cboCurso.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboCursoPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        pnlCurso.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCurso.setLayer(txtCursoCod, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCurso.setLayer(btnPesqCurso, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCurso.setLayer(btnEraseCur, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlCurso.setLayer(cboCurso, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout pnlCursoLayout = new javax.swing.GroupLayout(pnlCurso);
        pnlCurso.setLayout(pnlCursoLayout);
        pnlCursoLayout.setHorizontalGroup(
            pnlCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCursoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pnlCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(pnlCursoLayout.createSequentialGroup()
                        .addComponent(txtCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPesqCurso)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEraseCur)
                .addContainerGap())
        );
        pnlCursoLayout.setVerticalGroup(
            pnlCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCursoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addGroup(pnlCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCursoCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesqCurso)
                    .addComponent(btnEraseCur)))
        );

        getContentPane().add(pnlCurso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 561, -1));

        pnlTurma.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "TURMA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Código:");

        txtTurmaCod.setEnabled(false);
        txtTurmaCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTurmaCodActionPerformed(evt);
            }
        });
        txtTurmaCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTurmaCodKeyReleased(evt);
            }
        });

        cboTurmaNome.setEnabled(false);
        cboTurmaNome.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurmaNomePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        btnPesqTurma.setText("...");
        btnPesqTurma.setEnabled(false);
        btnPesqTurma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqTurmaActionPerformed(evt);
            }
        });

        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAlunos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblAlunos);
        if (tblAlunos.getColumnModel().getColumnCount() > 0) {
            tblAlunos.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Alunos nesta turma:");

        jLabel7.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel7.setText("Lotação:");

        lblTotAlu.setText("00");

        btnEraseTur.setText("Limpar Dados");
        btnEraseTur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEraseTurActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Turno:");

        cboTurmaTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manhã", "Tarde", "Noite" }));
        cboTurmaTurno.setSelectedIndex(-1);
        cboTurmaTurno.setEnabled(false);
        cboTurmaTurno.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurmaTurnoPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                cboTurmaTurnoPopupMenuWillBecomeVisible(evt);
            }
        });

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSave.setText("Matricular");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        lblCapacity.setText("/00");

        pnlTurma.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(txtTurmaCod, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(cboTurmaNome, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(btnPesqTurma, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(lblTotAlu, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(btnEraseTur, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(cboTurmaTurno, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(btnCancel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(btnSave, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlTurma.setLayer(lblCapacity, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout pnlTurmaLayout = new javax.swing.GroupLayout(pnlTurma);
        pnlTurma.setLayout(pnlTurmaLayout);
        pnlTurmaLayout.setHorizontalGroup(
            pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTurmaLayout.createSequentialGroup()
                .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTurmaLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                            .addGroup(pnlTurmaLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotAlu)
                                .addGap(0, 0, 0)
                                .addComponent(lblCapacity)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlTurmaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTurmaLayout.createSequentialGroup()
                                .addComponent(txtTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPesqTurma)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboTurmaNome, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(pnlTurmaLayout.createSequentialGroup()
                                .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEraseTur))
                    .addGroup(pnlTurmaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTurmaLayout.setVerticalGroup(
            pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTurmaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTurmaTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTurmaLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnEraseTur))
                    .addGroup(pnlTurmaLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTurmaCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPesqTurma)
                            .addComponent(cboTurmaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(pnlTurmaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblTotAlu)
                    .addComponent(btnSave)
                    .addComponent(btnCancel)
                    .addComponent(lblCapacity))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        getContentPane().add(pnlTurma, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 266, 561, -1));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(-8, -5, 580, 620));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAluCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluCodKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchStu();
        } else {
            txtAluNome.setText(null);
        }
    }//GEN-LAST:event_txtAluCodKeyReleased

    private void txtAluNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluNomeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchStu();
        } else {
            txtAluCod.setText(null);
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

        txtAluCod.setEditable(true);
        txtAluNome.setEditable(true);
        txtAluCod.grabFocus();
        btnPesqAlu.setEnabled(true);
    }//GEN-LAST:event_btnEraseAluActionPerformed

    private void txtCursoCodKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursoCodKeyTyped
        if (!txtCursoCod.getText().equals("")) {
            cboCurso.setSelectedIndex(-1);
        }
    }//GEN-LAST:event_txtCursoCodKeyTyped

    private void btnPesqCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqCursoActionPerformed
        searchCourse();
    }//GEN-LAST:event_btnPesqCursoActionPerformed

    private void btnEraseCurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEraseCurActionPerformed
        txtCursoCod.setText(null);
        cboCurso.setSelectedIndex(-1);
        txtCursoCod.setEnabled(true);
        btnPesqCurso.setEnabled(true);
        cboCurso.setEnabled(true);
        btnEraseTurActionPerformed(null);
        cboTurmaNome.removeAllItems();
        cboTurmaTurno.setEnabled(false);
        txtTurmaCod.setEnabled(false);
        btnPesqTurma.setEnabled(false);
        cboTurmaNome.setEnabled(false);
    }//GEN-LAST:event_btnEraseCurActionPerformed

    private void cboCursoPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboCursoPopupMenuWillBecomeInvisible
        if (cboCurso.getSelectedIndex() != -1) {
            txtCursoCod.setText(cursoCod[cboCurso.getSelectedIndex()]);
            txtCursoCod.setEnabled(false);
            cboCurso.setEnabled(false);
            btnPesqCurso.setEnabled(false);
            getClasses("");
            txtTurmaCod.grabFocus();
        }
    }//GEN-LAST:event_cboCursoPopupMenuWillBecomeInvisible

    private void txtTurmaCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTurmaCodKeyReleased
        cboTurmaTurno.setSelectedItem(-1);
        cboTurmaNome.setSelectedItem(-1);
    }//GEN-LAST:event_txtTurmaCodKeyReleased

    private void cboTurmaNomePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurmaNomePopupMenuWillBecomeInvisible
        if (cboTurmaNome.getSelectedIndex() != -1) {
            txtTurmaCod.setText(classCod[cboTurmaNome.getSelectedIndex()]);
            cboTurmaTurno.setSelectedItem(classTurno[cboTurmaNome.getSelectedIndex()]);

            txtTurmaCod.setEnabled(false);
            cboTurmaTurno.setEnabled(false);
            cboTurmaNome.setEnabled(false);
            btnPesqTurma.setEnabled(false);
            btnSave.grabFocus();
            getStuClass();
        }
    }//GEN-LAST:event_cboTurmaNomePopupMenuWillBecomeInvisible

    private void btnPesqTurmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqTurmaActionPerformed

        if (cboCurso.getSelectedIndex() != -1) {
            searchClass();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um curso.");
            txtCursoCod.grabFocus();
        }
    }//GEN-LAST:event_btnPesqTurmaActionPerformed

    private void btnEraseTurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEraseTurActionPerformed
        if (cboCurso.getSelectedIndex() != -1) {
            getClasses("");
        }
        txtTurmaCod.setText(null);
        cboTurmaNome.setSelectedIndex(-1);
        cboTurmaTurno.setSelectedIndex(-1);
        tblAlunos.setVisible(false);
        lblTotAlu.setText("");

        txtTurmaCod.setEnabled(true);
        cboTurmaNome.setEnabled(true);
        btnPesqTurma.setEnabled(true);
    }//GEN-LAST:event_btnEraseTurActionPerformed

    private void cboTurmaTurnoPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurmaTurnoPopupMenuWillBecomeInvisible
        if (cboTurmaTurno.getSelectedIndex() != -1) {
            getClasses(cboTurmaTurno.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cboTurmaTurnoPopupMenuWillBecomeInvisible

    private void cboTurmaTurnoPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboTurmaTurnoPopupMenuWillBecomeVisible

    }//GEN-LAST:event_cboTurmaTurnoPopupMenuWillBecomeVisible

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (!txtAluCod.getText().isEmpty() || !txtCursoCod.getText().isEmpty() || !txtTurmaCod.getText().isEmpty()) {
            int exit = JOptionPane.showConfirmDialog(this, "Deseja realmente cancelar? Todos os dados serão perdidos.", "Atenção", JOptionPane.YES_NO_OPTION);
            if (exit == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (this.className != null) {
            String msg = "Este aluno(a) já está matriculado no seguinte curso:\n\n"
                    + "<html><b>Curso:</b> "
                    + "" + courseName + "\n"
                    + "<html><b>Turma:</b> "
                    + "" + className + "\n"
                    + "<html><b>Turno:</b> "
                    + "" + turno + "\n\n"
                    + "Deseja efetuar rematrícula?";

            int q = JOptionPane.showConfirmDialog(this, msg, "Atenção!", JOptionPane.YES_NO_OPTION);
            if (q == JOptionPane.YES_OPTION) {
                if (check()) {
                    save();
                }
            }
        } else {
            if (check()) {
                save();
            }
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtCursoCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCursoCodActionPerformed
        searchCourse();
    }//GEN-LAST:event_txtCursoCodActionPerformed

    private void txtTurmaCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTurmaCodActionPerformed
        if (cboCurso.getSelectedIndex() != -1) {
            searchClass();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um curso.");
            txtCursoCod.grabFocus();
        }
    }//GEN-LAST:event_txtTurmaCodActionPerformed

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
            java.util.logging.Logger.getLogger(AlunoMatricula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlunoMatricula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlunoMatricula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlunoMatricula.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AlunoMatricula dialog = new AlunoMatricula(new JFrame());
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEraseAlu;
    private javax.swing.JButton btnEraseCur;
    private javax.swing.JButton btnEraseTur;
    private javax.swing.JButton btnPesqAlu;
    private javax.swing.JButton btnPesqCurso;
    private javax.swing.JButton btnPesqTurma;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cboCurso;
    private javax.swing.JComboBox<String> cboTurmaNome;
    private javax.swing.JComboBox<String> cboTurmaTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCapacity;
    private javax.swing.JLabel lblTotAlu;
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JLayeredPane pnlCurso;
    private javax.swing.JLayeredPane pnlTurma;
    private javax.swing.JTable tblAlunos;
    private javax.swing.JTextField txtAluCad;
    private javax.swing.JTextField txtAluCod;
    private javax.swing.JTextField txtAluNasc;
    private javax.swing.JTextField txtAluNome;
    private javax.swing.JTextField txtCursoCod;
    private javax.swing.JTextField txtTurmaCod;
    // End of variables declaration//GEN-END:variables

}
