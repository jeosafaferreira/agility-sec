package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author jeosafaferreira
 */
public class ResponsaveisDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    boolean modified;
    String responsible_id;
    DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateTimeSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DefaultTableModel dtmAlu, dtmMen;

    public ResponsaveisDetalhes() {
        initComponents();
        AgilitySec.showError(this, "#1043");

    }

    public ResponsaveisDetalhes(String cod) {
        initComponents();
        con = ModuloConexao.conector();
        responsible_id = cod;
        dtmAlu = (DefaultTableModel) tblAlunos.getModel();
        dtmMen = (DefaultTableModel) tblMen.getModel();

        getData();
    }

    private void getData() {
        try {
            pst = con.prepareStatement("SELECT * FROM responsibles WHERE id='" + responsible_id + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtCod.setText(rs.getString("id"));
                txtCon1.setText(rs.getString("tel1"));
                txtCon2.setText(rs.getString("tel2"));
                txtCpf.setText(rs.getString("cpf"));
                txtNas.setText(dateBr.format(dateSql.parse(rs.getString("dataNasc"))));
                txtNom.setText(rs.getString("name"));
                txtRg.setText(rs.getString("rg"));
                txtRgEmi.setText(dateBr.format(dateSql.parse(rs.getString("dataRg"))));
                txtRgOrg.setText(rs.getString("orgaoRg"));
                txtEstCiv.setText(rs.getString("estCivil"));
                txtSex.setText(rs.getString("sexo"));
                txtCadTime.setText(dateBr.format(dateTimeSql.parse(rs.getString("created"))));
                if (rs.getString("modified") != null) {
                    txtModTime.setText(dateBr.format(dateTimeSql.parse(rs.getString("modified"))));
                } else {
                    txtModTime.setText("--");
                }
            } else {
                AgilitySec.showError(this, "#1047");
            }

            //POPULANDO TABELA
            dtmAlu.setNumRows(0);
            pst = con.prepareStatement("SELECT stu.cod,stu.name, stu.status, class.name,course.name FROM students stu LEFT JOIN classes class ON class.id = stu.class_id LEFT JOIN courses course ON course.id = stu.course_id WHERE stu.respFin='" + responsible_id + "' OR stu.respAcad='" + responsible_id + "'");
            rs = pst.executeQuery();
            while (rs.next()) {
                dtmAlu.addRow(new String[]{
                    rs.getString("STU.status").equals("0") ? "<html><strike>" + rs.getString("STU.cod") : rs.getString("STU.cod"),
                    rs.getString("STU.status").equals("0") ? "<html><strike>" + rs.getString("STU.name") : rs.getString("STU.name"),
                    rs.getString("course.name"),
                    rs.getString("class.name")
                });
            }
            //MENSALIDADES
            java.util.Date now = new java.util.Date();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            rs = con.prepareStatement("SELECT "
                    + "    STU.name, TUI.id, TUI.venc, TUI.valor, TUI.valor_pago "
                    + "FROM "
                    + "    students STU"
                    + "        LEFT JOIN"
                    + "    tuitions TUI ON STU.cod = TUI.student_cod "
                    + "WHERE"
                    + "    (STU.respAcad = '" + this.responsible_id + "' OR STU.respFin = '" + this.responsible_id + "')").executeQuery();
            while (rs.next()) {
                dtmMen.addRow(new String[]{
                    rs.getString("TUI.id"),
                    rs.getString("STU.name"),
                    "R$ " + rs.getString("TUI.valor").replace(".", ","),
                    df.format(rs.getDate("venc")),
                    (rs.getString("valor_pago") == null ? (now.after(rs.getDate("venc")) ? "VENCIDO" : "EM ABERTO") : "PAGO")
                }
                );
            }

        } catch (Exception e) {
            AgilitySec.showError(this, "#1044", e);
        }
    }

    private void delResp() {
        int q = JOptionPane.showConfirmDialog(this, "Deseja realmente deletar este responsável?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (q == JOptionPane.YES_OPTION) {
            if (tblAlunos.getModel().getRowCount() > 0) {
                JOptionPane.showMessageDialog(this, "Não é permitido deletar. \n<html>Ele(a) é responsável por <b>" + tblAlunos.getModel().getRowCount() + "</b> aluno(s) nesta instituição.</html>");
            } else {
                try {
                    pst = con.prepareStatement("UPDATE responsibles SET status='0' WHERE id='" + responsible_id + "'");
                    if (pst.executeUpdate() > 0) {
                        JOptionPane.showMessageDialog(this, "Responsável deletado com sucesso.");
                        modified = true;
                        this.dispose();
                    } else {
                        AgilitySec.showError(this, "#1046");
                    }
                } catch (Exception e) {
                    AgilitySec.showError(this, "#1045", e);
                }

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
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        txtRgOrg = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtEstCiv = new javax.swing.JTextField();
        txtSex = new javax.swing.JTextField();
        txtCod = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCadTime = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtModTime = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCadBy = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtModBy = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMen = new javax.swing.JTable();
        btnEdit1 = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DETALHES DO RESPONSÁVEL");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpResp1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpResp1.setOpaque(false);

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Nome do Responsável:");

        txtNom.setEditable(false);
        txtNom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNomFocusLost(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel32.setText("CPF:");

        jLabel33.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel33.setText("RG:");

        txtRg.setEditable(false);

        txtCpf.setEditable(false);

        jLabel34.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel34.setText("Telefone 1:");

        txtCon1.setEditable(false);

        jLabel35.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel35.setText("Telefone 2:");

        txtCon2.setEditable(false);

        jLabel36.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel36.setText("Data de Nascimento:");

        txtNas.setEditable(false);

        txtRgEmi.setEditable(false);

        jLabel37.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel37.setText("Data de Emissão:");

        jLabel38.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel38.setText("Estado Civil:");

        jLabel39.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel39.setText("Sexo:");

        txtRgOrg.setEditable(false);

        jLabel40.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel40.setText("Orgão Emissor:");

        txtEstCiv.setEditable(false);

        txtSex.setEditable(false);

        txtCod.setEditable(false);
        txtCod.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        txtCod.setForeground(new java.awt.Color(51, 51, 51));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Cadastrado em:");

        txtCadTime.setEditable(false);
        txtCadTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Modificado em:");

        txtModTime.setEditable(false);
        txtModTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Cadastrado por:");

        txtCadBy.setEditable(false);
        txtCadBy.setForeground(new java.awt.Color(51, 51, 51));

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Modificado por:");

        txtModBy.setEditable(false);
        txtModBy.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jpResp1Layout = new javax.swing.GroupLayout(jpResp1);
        jpResp1.setLayout(jpResp1Layout);
        jpResp1Layout.setHorizontalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCon1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCpf))
                        .addGap(6, 6, 6)
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCon2, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(txtRg)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpResp1Layout.createSequentialGroup()
                                .addComponent(txtRgEmi)
                                .addGap(166, 166, 166))))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel1)
                                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNas)
                                            .addGroup(jpResp1Layout.createSequentialGroup()
                                                .addComponent(jLabel36)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4)
                                            .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(txtModBy))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jpResp1Layout.createSequentialGroup()
                                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jpResp1Layout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addGap(90, 90, 90))
                                    .addComponent(txtEstCiv))
                                .addGap(332, 332, 332)))
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtRgOrg)
                                .addComponent(txtSex)
                                .addComponent(txtCadTime)
                                .addGroup(jpResp1Layout.createSequentialGroup()
                                    .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel40)
                                        .addComponent(jLabel39)
                                        .addComponent(jLabel2))
                                    .addGap(41, 41, 41)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jpResp1Layout.setVerticalGroup(
            jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpResp1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel36)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel37)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRgEmi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRgOrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCon1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCon2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpResp1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEstCiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpResp1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        getContentPane().add(jpResp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 710, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblAlunos.setAutoCreateRowSorter(true);
        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nome Completo", "Curso", "Turma"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblAlunos);
        if (tblAlunos.getColumnModel().getColumnCount() > 0) {
            tblAlunos.getColumnModel().getColumn(0).setPreferredWidth(25);
            tblAlunos.getColumnModel().getColumn(1).setPreferredWidth(247);
            tblAlunos.getColumnModel().getColumn(2).setPreferredWidth(157);
            tblAlunos.getColumnModel().getColumn(3).setPreferredWidth(48);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Alunos Relacionados", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblMen.setAutoCreateRowSorter(true);
        tblMen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód. Mensalidade", "Aluno Relacionado", "Valor", "Vencimento", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblMen);
        if (tblMen.getColumnModel().getColumnCount() > 0) {
            tblMen.getColumnModel().getColumn(0).setPreferredWidth(126);
            tblMen.getColumnModel().getColumn(1).setPreferredWidth(265);
            tblMen.getColumnModel().getColumn(2).setPreferredWidth(103);
            tblMen.getColumnModel().getColumn(3).setPreferredWidth(96);
            tblMen.getColumnModel().getColumn(4).setPreferredWidth(95);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Mensalidades", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 328, -1, -1));

        btnEdit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492599271_Edit-Male-User.png"))); // NOI18N
        btnEdit1.setText("Editar Resp.");
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 478, 229, -1));

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDel.setText("Deletar");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 478, 231, 60));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(487, 478, 229, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 740, 560));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNomFocusLost

    }//GEN-LAST:event_txtNomFocusLost

    private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdit1ActionPerformed
        ResponsaveisForm rF = new ResponsaveisForm(responsible_id);
        rF.setVisible(true);
        if (rF.modified) {
            modified = true;
            getData();
        }
    }//GEN-LAST:event_btnEdit1ActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        delResp();
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
            java.util.logging.Logger.getLogger(ResponsaveisDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResponsaveisDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ResponsaveisDetalhes dialog = new ResponsaveisDetalhes();
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
    private javax.swing.JButton btnEdit1;
    private javax.swing.JButton btnExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jpResp1;
    private javax.swing.JTable tblAlunos;
    private javax.swing.JTable tblMen;
    private javax.swing.JTextField txtCadBy;
    private javax.swing.JTextField txtCadTime;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCon1;
    private javax.swing.JTextField txtCon2;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtEstCiv;
    private javax.swing.JTextField txtModBy;
    private javax.swing.JTextField txtModTime;
    private javax.swing.JTextField txtNas;
    private javax.swing.JTextField txtNom;
    private javax.swing.JTextField txtRg;
    private javax.swing.JTextField txtRgEmi;
    private javax.swing.JTextField txtRgOrg;
    private javax.swing.JTextField txtSex;
    // End of variables declaration//GEN-END:variables
}
