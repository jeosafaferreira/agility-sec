package Agility.telas;

import Agility.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author jeosafaferreira
 */
public class OcorrenciaForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String type, sql, sqlToDi, oco_id, stu_cod;
    DefaultTableModel dtm;
    Home home;
    boolean modified;
    DateFormat vData = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat vHora = new SimpleDateFormat("HH:mm");

    public OcorrenciaForm(java.awt.Frame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        type = "new";
        dtm = (DefaultTableModel) tblOco.getModel();
        getData("new");
    }

    public OcorrenciaForm(java.awt.Frame parent, String id) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        type = "upd";
        dtm = (DefaultTableModel) tblOco.getModel();
        getData(id);
        oco_id = id;

        this.setTitle("EDITAR OCORRÊNCIA");
        jLabel10.setText("Outras ocorrências:");
        btnEraseAlu.setVisible(false);
        btnPesqAlu.setVisible(false);
        btnDet.setVisible(false);
    }

    private void getData(String id) {
        if (!id.equals("new")) {
            txtOcoDat.setEnabled(true);
            txtOcoDet.setEnabled(true);
            txtOcoHor.setEnabled(true);
            txtOcoOco.setEnabled(true);
            txtOcoSen.setEnabled(true);
            txtAluCod.setEnabled(false);
            txtAluNome.setEnabled(false);
            btnPesqAlu.setEnabled(false);
            btnEraseAlu.setEnabled(false);

            //Busca dados atuais
            try {
                //DADOS DA OCORRÊNCIA
                pst = con.prepareStatement("SELECT * FROM occurrences WHERE id='" + id + "'");
                rs = pst.executeQuery();
                if (rs.next()) {
                    stu_cod = rs.getString("student_cod");

                    txtOcoDat.setText(rs.getString("date"));
                    txtOcoDet.setText(rs.getString("details"));
                    txtOcoHor.setText(rs.getString("time"));
                    txtOcoOco.setText(rs.getString("occurrence"));
                    txtOcoSen.setText(rs.getString("sentence"));
                }

                //ALUNO
                pst = con.prepareStatement("SELECT cod, name, dataNasc, created FROM students WHERE cod='" + stu_cod + "'");
                rs = pst.executeQuery();
                if (rs.next()) {
                    txtAluCod.setText(rs.getString("cod"));
                    txtAluNome.setText(rs.getString("name"));
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar aluno relacionado a esta ocorrência.\nCódigo do erro: #108.");
                }

                //OCORRÊNCIAS ANTERIORES
                rs = con.prepareStatement("SELECT * FROM occurrences WHERE student_cod='" + txtAluCod.getText() + "' and id != '" + id + "'").executeQuery();
                int i = 0;
                dtm.setNumRows(0);
                while (rs.next()) {
                    dtm.addRow(new Object[]{null, null, null});
                    tblOco.setValueAt(rs.getString("id"), i, 0);
                    tblOco.setValueAt(rs.getString("occurrence"), i, 1);
                    tblOco.setValueAt(rs.getString("date"), i, 2);
                    i++;
                    btnDet.setEnabled(true);
                }
                rs.last();
                jLabel12.setText(Integer.toString(rs.getRow()));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0104\nDetalhes do erro:\n" + e);
                this.dispose();
            }
        }
    }

    private void searchStu() {
        int row = 0;

        if (txtAluNome.getText().isEmpty()) {
            sql = "SELECT cod, name, dataNasc, class_id, created FROM students WHERE status=1 cod LIKE '%" + txtAluCod.getText() + "%'";
            sqlToDi = "SELECT cod, name, dataNasc, created FROM students WHERE status=1 AND cod LIKE '%" + txtAluCod.getText() + "%'";
        } else {
            sql = "SELECT cod, name, dataNasc, class_id, created FROM students WHERE status=1 AND name LIKE '%" + txtAluNome.getText() + "%' ";
            sqlToDi = "SELECT cod, name, dataNasc, created FROM students WHERE status=1 AND name LIKE '%" + txtAluNome.getText() + "%' ";
        }
        try {
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                if (rs.getRow() > 1) {
                    String headers[] = {"Matrícula", "Nome do Aluno", "Data de Nasc.", "Cadastrado em:"};
                    int cols[] = {50, 250, 0, 0};
                    Dialog dAlu = new Dialog(new JDialog(), true, sqlToDi, headers, cols);
                    dAlu.setVisible(true);
                    row = dAlu.idFDialog();
                } else {
                    row = 1;
                }
                if (row > 0) {
                    rs.absolute(row);

                    txtAluCod.setText(rs.getString("cod"));
                    txtAluNome.setText(rs.getString("name"));

                    txtAluCod.setEnabled(false);
                    txtAluNome.setEnabled(false);
                    btnPesqAlu.setEnabled(false);

                    //OCORRÊNCIAS ANTERIORES
                    try {
                        rs = con.prepareStatement("SELECT * FROM occurrences WHERE student_cod='" + txtAluCod.getText() + "'").executeQuery();
                        int i = 0;
                        dtm.setNumRows(0);
                        while (rs.next()) {
                            dtm.addRow(new Object[]{null, null, null});
                            tblOco.setValueAt(rs.getString("id"), i, 0);
                            tblOco.setValueAt(rs.getString("occurrence"), i, 1);
                            tblOco.setValueAt(rs.getString("date"), i, 2);
                            i++;
                            btnDet.setEnabled(true);
                        }
                        rs.last();
                        jLabel12.setText(Integer.toString(rs.getRow()));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar ocorrências.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0107\nDetalhes do erro:\n" + e);
                        this.dispose();
                    }

                    txtOcoDat.setEnabled(true);
                    txtOcoDet.setEnabled(true);
                    txtOcoHor.setEnabled(true);
                    txtOcoOco.setEnabled(true);
                    txtOcoSen.setEnabled(true);
                    txtOcoDat.grabFocus();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
                txtAluCod.grabFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno ao pesquisar aluno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0063\nDetalhes do erro:\n" + e);
        }
    }

    /**
     * RETORNA: INT 0: Se falhar no check; INT 1: Se passar no check;
     */
    private int check() {
        int r = 0;
        if (txtAluCod.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um aluno.");
            txtAluCod.grabFocus();

        } else if (txtOcoDat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Data\".");
            txtOcoDat.grabFocus();

        } else if (txtOcoSen.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Sentença\".");
            txtOcoSen.grabFocus();

        } else if (txtOcoHor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Horário\".");
            txtOcoHor.grabFocus();

        } else if (txtOcoOco.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Ocorrência\".");
            txtOcoOco.grabFocus();
        } else {
            r = 1;
            //VALIDA HORA
            try {
                vHora.setLenient(false);
                vHora.parse(txtOcoHor.getText().replaceAll("h", ""));
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Hora\".");
                txtOcoHor.setText("");
                txtOcoHor.grabFocus();
                r = 0;
            }
            //VALIDA DATA
            try {
                vData.setLenient(false);
                vData.parse(txtOcoDat.getText());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Por favor, preencha corretamente o campo \"Data\".");
                txtOcoDat.setText("");
                txtOcoDat.grabFocus();
                r = 0;
            }
        }
        return r;
    }

    private void execQuery() {
        if (check() == 1) {
            String txtErro1 = null;
            String txtErro2 = null;
            String txtSuc = null;

            if (type.equals("new")) {
                sql = "INSERT INTO occurrences (occurrence, details, sentence, student_cod, date, time, created_by) VALUES(?,?,?,?,?,?,?)";
                txtSuc = "Ocorrência cadastrada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar esta ocorrência.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0100";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar esta ocorrência.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0101\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                sql = "UPDATE occurrences SET occurrence=?, details=?, sentence=?, student_cod=?, date=?, time=?, modified_by=? WHERE id=" + oco_id;
                txtSuc = "Ocorrência atualizada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar esta ocorrência.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0102";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar esta ocorrência.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #0103\nDetalhes do erro:\n";
            }
            try {
                pst = con.prepareStatement(sql);
                pst.setString(1, txtOcoOco.getText());
                pst.setString(2, txtOcoDet.getText());
                pst.setString(3, txtOcoSen.getText());
                pst.setString(4, txtAluCod.getText());
                pst.setString(5, txtOcoDat.getText());
                pst.setString(6, txtOcoHor.getText());
                pst.setString(7, Login.emp_id);
                if (pst.executeUpdate() == 0) {
                    JOptionPane.showMessageDialog(this, txtErro1);
                } else {
                    modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, txtSuc);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, txtErro2 + e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAluno1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtOcoDat = new javax.swing.JTextField();
        try{    MaskFormatter data= new MaskFormatter("##/##/####");    txtOcoDat = new JFormattedTextField(data); }    catch (Exception e){ }
        txtOcoHor = new javax.swing.JTextField();
        try{
            MaskFormatter hora = new MaskFormatter("##:##h");
            txtOcoHor = new JFormattedTextField(hora);
        }catch (Exception e){
        }
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtOcoOco = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOcoDet = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        txtOcoSen = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        btnHoje = new javax.swing.JButton();
        btnCan = new javax.swing.JButton();
        btnSal = new javax.swing.JButton();
        pnlAluno = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtAluCod = new javax.swing.JTextField();
        txtAluNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnPesqAlu = new javax.swing.JButton();
        btnEraseAlu = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOco = new javax.swing.JTable();
        btnDet = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NOVA OCORRÊNCIA");
        setAlwaysOnTop(true);
        setModal(true);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAluno1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Ocorrência"));
        pnlAluno1.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Data:");

        txtOcoDat.setEnabled(false);
        txtOcoDat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOcoDatKeyTyped(evt);
            }
        });

        txtOcoHor.setEnabled(false);
        txtOcoHor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOcoHorKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Horário:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Ocorrência:");

        txtOcoOco.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Detalhes da Ocorrência:");

        txtOcoDet.setColumns(20);
        txtOcoDet.setLineWrap(true);
        txtOcoDet.setRows(3);
        txtOcoDet.setWrapStyleWord(true);
        txtOcoDet.setEnabled(false);
        txtOcoDet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOcoDetKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtOcoDet);

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Sentença:");

        txtOcoSen.setEnabled(false);

        jLabel20.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(233, 2, 2));
        jLabel20.setText("*");

        jLabel21.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(233, 2, 2));
        jLabel21.setText("*");

        jLabel22.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(233, 2, 2));
        jLabel22.setText("*");

        jLabel24.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(233, 2, 2));
        jLabel24.setText("*");

        btnHoje.setText("Hoje");
        btnHoje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHojeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAluno1Layout = new javax.swing.GroupLayout(pnlAluno1);
        pnlAluno1.setLayout(pnlAluno1Layout);
        pnlAluno1Layout.setHorizontalGroup(
            pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAluno1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                    .addGroup(pnlAluno1Layout.createSequentialGroup()
                        .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAluno1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel20))
                            .addComponent(jLabel7))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtOcoOco)
                    .addGroup(pnlAluno1Layout.createSequentialGroup()
                        .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlAluno1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel24))
                            .addGroup(pnlAluno1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel22))
                            .addGroup(pnlAluno1Layout.createSequentialGroup()
                                .addComponent(txtOcoDat, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(btnHoje, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlAluno1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel21))
                                    .addComponent(txtOcoHor, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtOcoSen)))
        );
        pnlAluno1Layout.setVerticalGroup(
            pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAluno1Layout.createSequentialGroup()
                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOcoDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOcoHor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHoje))
                .addGap(2, 2, 2)
                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOcoOco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAluno1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtOcoSen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(pnlAluno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 285, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(518, 586, 127, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 586, 111, -1));

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder("Aluno"));
        pnlAluno.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
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
        jLabel3.setText("Nome:");

        btnPesqAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492598245_icon-112-search-plus.png"))); // NOI18N
        btnPesqAlu.setText("...");
        btnPesqAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqAluActionPerformed(evt);
            }
        });

        btnEraseAlu.setText("Limpar dados");
        btnEraseAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEraseAluActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Ocorrências Anteriores:");

        tblOco.setAutoCreateRowSorter(true);
        tblOco.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COD", "Ocorrência", "Data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOco.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblOco.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblOco);

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel11.setText("Total:");

        jLabel12.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("0");

        javax.swing.GroupLayout pnlAlunoLayout = new javax.swing.GroupLayout(pnlAluno);
        pnlAluno.setLayout(pnlAlunoLayout);
        pnlAlunoLayout.setHorizontalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAlunoLayout.createSequentialGroup()
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txtAluCod, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(pnlAlunoLayout.createSequentialGroup()
                                .addComponent(txtAluNome)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPesqAlu)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEraseAlu))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDet, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(btnPesqAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnEraseAlu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0))
        );

        getContentPane().add(pnlAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 639, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 680, 650));

        setSize(new java.awt.Dimension(661, 666));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOcoDatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOcoDatKeyTyped
        String t = txtOcoDat.getText();
        int l = t.replaceAll(" ", "").length();
        if (l > 8) {
            txtOcoDat.transferFocus();
        }
    }//GEN-LAST:event_txtOcoDatKeyTyped

    private void txtOcoHorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOcoHorKeyTyped
        String t = txtOcoHor.getText();
        int l = t.replaceAll(" ", "").length();

        if (l > 4) {
            txtOcoHor.transferFocus();
        }
    }//GEN-LAST:event_txtOcoHorKeyTyped

    private void txtOcoDetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOcoDetKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            if (evt.isShiftDown()) {
                txtOcoDet.transferFocusBackward();
            } else {
                txtOcoDet.transferFocus();
            }
        }
    }//GEN-LAST:event_txtOcoDetKeyPressed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtAluCod.getText().isEmpty() && txtOcoDet.getText().isEmpty() && txtOcoOco.getText().isEmpty() && txtOcoSen.getText().isEmpty()) {
                this.dispose();
            } else {
                int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
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

    private void txtAluCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluCodKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesqAluActionPerformed(null);
        } else {
            txtAluNome.setText(null);
        }
    }//GEN-LAST:event_txtAluCodKeyReleased

    private void txtAluNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluNomeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPesqAluActionPerformed(null);
        } else {
            txtAluCod.setText(null);
        }
    }//GEN-LAST:event_txtAluNomeKeyReleased

    private void btnPesqAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqAluActionPerformed
        searchStu();
    }//GEN-LAST:event_btnPesqAluActionPerformed

    private void btnEraseAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEraseAluActionPerformed
        dtm.setNumRows(0);
        txtAluCod.setText(null);
        txtAluNome.setText(null);

        txtAluCod.setEnabled(true);
        txtAluNome.setEnabled(true);

        txtAluCod.grabFocus();
        btnPesqAlu.setEnabled(true);
    }//GEN-LAST:event_btnEraseAluActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblOco.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione um ocorrência na tabela.");
        } else {
            OcorrenciaDetalhes ocoD = new OcorrenciaDetalhes(this, (String) tblOco.getValueAt(tblOco.getSelectedRow(), 0));
            ocoD.setVisible(true);
            if (ocoD.modified) {
                searchStu();
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void btnHojeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojeActionPerformed
        txtOcoDat.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
    }//GEN-LAST:event_btnHojeActionPerformed

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
            java.util.logging.Logger.getLogger(OcorrenciaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OcorrenciaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OcorrenciaForm dialog = new OcorrenciaForm(new javax.swing.JFrame());
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
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEraseAlu;
    private javax.swing.JButton btnHoje;
    private javax.swing.JButton btnPesqAlu;
    private javax.swing.JButton btnSal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JPanel pnlAluno1;
    private javax.swing.JTable tblOco;
    private javax.swing.JTextField txtAluCod;
    private javax.swing.JTextField txtAluNome;
    private javax.swing.JTextField txtOcoDat;
    private javax.swing.JTextArea txtOcoDet;
    private javax.swing.JTextField txtOcoHor;
    private javax.swing.JTextField txtOcoOco;
    private javax.swing.JTextField txtOcoSen;
    // End of variables declaration//GEN-END:variables
}
