package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilityRel;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jeosafaferreira
 */
public class TurmaDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home;
    String classCod, classId;
    boolean modified;
    DefaultTableModel dtmAlu, dtmProf;

    public TurmaDetalhes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados desta turma.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0060");
        this.dispose();
    }

    public TurmaDetalhes(String cod) {
        initComponents();
        con = ModuloConexao.conector();
        this.classCod = cod;
        try {
            rs = con.prepareStatement("SELECT id FROM classes WHERE cod='" + cod + "'").executeQuery();
            rs.next();
            this.classId = rs.getString("id");
        } catch (Exception e) {
            AgilitySec.showError(this, "#1154", e);
        }
        dtmAlu = (DefaultTableModel) tblAlunos.getModel();
        dtmProf = (DefaultTableModel) tblProf.getModel();
        listData();

    }

    private void listData() {
        DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        dtmAlu.setNumRows(0);
        dtmProf.setNumRows(0);
        //BUSCA TURMA
        try {
            pst = con.prepareStatement("SELECT * FROM classes WHERE cod='" + this.classCod + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                String class_id = rs.getString("id");
                String course_id = rs.getString("course_id");
                String cRoom_id = rs.getString("classroom_id");

                txtCod.setText(rs.getString("cod"));
                txtCreated.setText(dateBr.format(dateSql.parse(rs.getString("created"))));
                txtCreatedBy.setText(rs.getString("created_by"));
                txtModified.setText((rs.getString("modified") != null) ? dateBr.format(dateSql.parse(rs.getString("modified"))) : "");
                txtModifiedBy.setText(rs.getString("modified_by"));
                txtTurno.setText(rs.getString("turno"));
                txtTurma.setText(rs.getString("name"));
                txtCapacity.setText(rs.getString("capacity"));

                //BUSCA CURSO
                pst = con.prepareStatement("SELECT name FROM courses WHERE id=" + course_id);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtCurso.setText(rs.getString("name"));
                } else {
                    JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0053");
                }
                //BUSCA SALA
                pst = con.prepareStatement("SELECT name FROM classrooms WHERE id=" + cRoom_id);
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtSala.setText(rs.getString("name"));
                } else {
                    JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0054");
                }
                //LISTA ALUNOS
                pst = con.prepareStatement("SELECT cod, name, type FROM students WHERE status=1 AND class_id=" + class_id);
                rs = pst.executeQuery();
                while (rs.next()) {
                    dtmAlu.addRow(new String[]{
                        rs.getString("cod"),
                        rs.getString("name"),
                        rs.getString("type")
                    });
                }
                lblTotAlu.setText(Integer.toString(dtmAlu.getRowCount()));

                //HORÁRIO Escolar
                //IMPLEMENTAR!!!!!!
                //PROFESSORES
                rs = con.prepareStatement("SELECT "
                        + "    EMP.id, EMP.name, EMP.tel1, EMP.tel2 "
                        + "FROM"
                        + "    schedules SCH"
                        + "        LEFT JOIN"
                        + "    employees EMP ON EMP.id = SCH.employee_id "
                        + "WHERE"
                        + "    SCH.class_id = " + this.classId + " "
                        + "GROUP BY EMP.id").executeQuery();
                while (rs.next()) {
                    dtmProf.addRow(new String[]{
                        rs.getString("EMP.id"),
                        rs.getString("EMP.name"),
                        rs.getString("EMP.tel1"),
                        rs.getString("EMP.tel2")
                    });
                }
            } else {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0055");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0056\nDetalhes do erro:\n" + e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCurso = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTurma = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTurno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtSala = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCapacity = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlAlunosMat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlunos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        lblTotAlu = new javax.swing.JLabel();
        pnlProf = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProf = new javax.swing.JTable();
        pnlHorario = new javax.swing.JPanel();
        btnQuaHor = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtCreated = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtModified = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCreatedBy = new javax.swing.JTextField();
        txtModifiedBy = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes da Turma");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DA TURMA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        txtCod.setEditable(false);
        txtCod.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Curso:");

        txtCurso.setEditable(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Turma:");

        txtTurma.setEditable(false);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Turno:");

        txtTurno.setEditable(false);

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Sala:");

        txtSala.setEditable(false);

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("Limite de Alunos:");

        txtCapacity.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtTurno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(txtCod, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(92, 92, 92))
                            .addComponent(txtSala, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txtCapacity)))
                    .addComponent(jLabel3)
                    .addComponent(txtCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtTurma, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 649, -1));

        pnlAlunosMat.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matrícula", "Nome do Aluno", "Procedência"
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
        tblAlunos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAlunos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblAlunos);
        if (tblAlunos.getColumnModel().getColumnCount() > 0) {
            tblAlunos.getColumnModel().getColumn(0).setPreferredWidth(10);
            tblAlunos.getColumnModel().getColumn(1).setPreferredWidth(280);
            tblAlunos.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        jLabel6.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel6.setText("Total de alunos:");

        lblTotAlu.setText("lblTotAlu");

        javax.swing.GroupLayout pnlAlunosMatLayout = new javax.swing.GroupLayout(pnlAlunosMat);
        pnlAlunosMat.setLayout(pnlAlunosMatLayout);
        pnlAlunosMatLayout.setHorizontalGroup(
            pnlAlunosMatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
            .addGroup(pnlAlunosMatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotAlu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAlunosMatLayout.setVerticalGroup(
            pnlAlunosMatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunosMatLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunosMatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblTotAlu))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Alunos Matriculados", pnlAlunosMat);

        pnlProf.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblProf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome Completo", "Telefone 1", "Telefone 2"
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
        tblProf.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblProf.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblProf);
        if (tblProf.getColumnModel().getColumnCount() > 0) {
            tblProf.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblProf.getColumnModel().getColumn(1).setPreferredWidth(290);
            tblProf.getColumnModel().getColumn(2).setPreferredWidth(140);
            tblProf.getColumnModel().getColumn(3).setPreferredWidth(139);
        }

        javax.swing.GroupLayout pnlProfLayout = new javax.swing.GroupLayout(pnlProf);
        pnlProf.setLayout(pnlProfLayout);
        pnlProfLayout.setHorizontalGroup(
            pnlProfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );
        pnlProfLayout.setVerticalGroup(
            pnlProfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Professores", pnlProf);

        pnlHorario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnQuaHor.setText("Abrir Quadro de Horários");
        btnQuaHor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuaHorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHorarioLayout = new javax.swing.GroupLayout(pnlHorario);
        pnlHorario.setLayout(pnlHorarioLayout);
        pnlHorarioLayout.setHorizontalGroup(
            pnlHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHorarioLayout.createSequentialGroup()
                .addContainerGap(229, Short.MAX_VALUE)
                .addComponent(btnQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(217, 217, 217))
        );
        pnlHorarioLayout.setVerticalGroup(
            pnlHorarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHorarioLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(btnQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Quadro de Horários", pnlHorario);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 323, -1, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/edit - 48x48.png"))); // NOI18N
        btnEdit.setText("Editar Turma");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 557, 197, -1));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 557, 198, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Criado em:");

        txtCreated.setEditable(false);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Modificado em:");

        txtModified.setEditable(false);

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Criado por:");

        txtCreatedBy.setEditable(false);

        txtModifiedBy.setEditable(false);

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Modificado por:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtCreated, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtCreatedBy, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtModified, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtModifiedBy))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCreated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCreatedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModified, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModifiedBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 183, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 670, 630));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        TurmaForm tn = new TurmaForm(home, rootPaneCheckingEnabled, classCod);
        tn.setVisible(true);
        if (tn.modified) {
            this.classCod = tn.txtCod.getText();
            listData();
            this.modified = true;
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnQuaHorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuaHorActionPerformed
        AgilityRel.openReport("Quadro de horários - Agility®", AgilityRel.turQuaHor(this.classId));
    }//GEN-LAST:event_btnQuaHorActionPerformed

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
            java.util.logging.Logger.getLogger(TurmaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TurmaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TurmaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TurmaDetalhes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TurmaDetalhes dialog = new TurmaDetalhes(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnQuaHor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblTotAlu;
    private javax.swing.JPanel pnlAlunosMat;
    private javax.swing.JPanel pnlHorario;
    private javax.swing.JPanel pnlProf;
    private javax.swing.JTable tblAlunos;
    private javax.swing.JTable tblProf;
    private javax.swing.JTextField txtCapacity;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtCreated;
    private javax.swing.JTextField txtCreatedBy;
    private javax.swing.JTextField txtCurso;
    private javax.swing.JTextField txtModified;
    private javax.swing.JTextField txtModifiedBy;
    private javax.swing.JTextField txtSala;
    private javax.swing.JTextField txtTurma;
    private javax.swing.JTextField txtTurno;
    // End of variables declaration//GEN-END:variables
}
