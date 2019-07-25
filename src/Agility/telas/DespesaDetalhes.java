package Agility.telas;

import Agility.dal.ModuloConexao;
import static Agility.api.AgilitySec.df;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JDialog;

/**
 *
 * @author jeosafaferreira
 */
public class DespesaDetalhes extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home;
    DefaultTableModel dtm;
    String fin_id, status, pri_parcela;
    DateFormat dataBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");
    GregorianCalendar cal = new GregorianCalendar();
    Date hoje;
    boolean parc, modified;

    public DespesaDetalhes(JDialog parent) {
        super(parent);
        initComponents();
        JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados desta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1015");
        this.dispose();
    }

    public DespesaDetalhes(JDialog parent, String id, String type) {
        super(parent);
        initComponents();
        status = type;
        if (status.equals("payed")) {
            btnPay.setText("Pagamento");
            btnEdit.setEnabled(false);
            this.setTitle("DETALHAMENTO DE CONTA PAGA");
        }
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblPar.getModel();
        fin_id = id;
        cal.set(Calendar.HOUR, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.MILLISECOND, 00);
        hoje = cal.getTime();
        listData();
    }

    private void listData() {

        try {
            rs = con.prepareStatement("SELECT "
                    + "     * "
                    + " FROM"
                    + "     finances FIN "
                    + "         LEFT JOIN "
                    + "     acc_plan B ON FIN.acc_plan_id = B.id"
                    + "         LEFT JOIN "
                    + "     employees EMP_CRE ON FIN.created_by=EMP_CRE.id "
                    + "         LEFT JOIN "
                    + "     employees EMP_MOD ON FIN.modified_by=EMP_MOD.id"
                    + " WHERE FIN.id='" + fin_id + "'"
            ).executeQuery();
            if (rs.next()) {
                pri_parcela = rs.getString("FIN.pri_parcela"); //Uso essa variável na função delConta()
                txtCod.setText(rs.getString("FIN.id"));
                txtPlan.setText(rs.getString("B.title"));
                txtTit.setText(rs.getString("FIN.titulo"));
                txtObs.setText(rs.getString("FIN.obs"));
                txtCadBy.setText(rs.getString("EMP_CRE.name"));
                txtCadTime.setText(df(rs.getTimestamp("created")));
                txtModBy.setText(rs.getString("EMP_MOD.name"));
                txtModTime.setText(df(rs.getTimestamp("modified")));

                if (rs.getString("FIN.tot_parcela").equals("1")) {
                    //CONTA À VISTA
                    parc = false;

                    txtVlr.setText("R$ " + rs.getString("FIN.valor"));
                    txtVenc.setText(dataBr.format(dateSql.parse(rs.getString("FIN.venc"))));

                    cbVis.setSelected(true);
                    cbVis.setEnabled(true);
                    txtVlr.setEnabled(true);
                    txtVenc.setEnabled(true);

                } else {
                    //CONTA PARCELADA
                    parc = true;
                    txtPar.setText(rs.getString("FIN.parcela") + "/" + rs.getString("FIN.tot_parcela"));
                    txtParVenc.setText(dataBr.format(dateSql.parse(rs.getString("FIN.venc"))));
                    txtParVlr.setText("R$ " + rs.getString("FIN.valor").replace(".", ","));

                    cbParc.setSelected(true);
                    tblPar.setEnabled(true);
                    cbParc.setEnabled(true);
                    txtPar.setEnabled(true);
                    txtParVenc.setEnabled(true);
                    txtParVlr.setEnabled(true);

                    //BUSCA TODAS AS PARCELAS
                    rs = con.prepareStatement("SELECT id,parcela,venc,valor,pri_parcela, valor_pago FROM finances WHERE pri_parcela=" + rs.getString("FIN.pri_parcela")).executeQuery();

                    dtm.setNumRows(0); //Zerando tabela
                    while (rs.next()) {
                        String status;
                        Date venc = dateSql.parse(rs.getString("venc"));
                        if (rs.getString("valor_pago") == null) {
                            status = "<html><font color='#CCCC00'><b>PENDENTE</b></font></html>";
                            if (venc.equals(hoje)) {
                                status = "<html><font color='red'><b>VENCE HOJE</b></font></html>";
                            } else if (venc.before(hoje)) {
                                status = "<html><font color='red'><b>VENCIDO</b></font></html>";
                            }
                        } else {
                            status = "<html><font color='#00cc00'><b>PAGO</b></font></html>";
                        }
                        dtm.addRow(new String[]{rs.getString("parcela"), dataBr.format(dateSql.parse(rs.getString("venc"))), "R$ " + rs.getString("valor").replace(".", ","), status});
                    }

                    //SELECIONANDO LINHA CORRESPONDENTE  NA TABELA
                    for (int i = 0; i < dtm.getRowCount(); i++) {
                        if (dtm.getValueAt(i, 1).equals(txtParVenc.getText())) {
                            tblPar.setRowSelectionInterval(i, i);
                        }
                    }

                    //VALOR TOTAL:
                    rs.first();
                    rs = con.prepareStatement("SELECT SUM(valor) AS total FROM finances WHERE pri_parcela=" + rs.getString("pri_parcela")).executeQuery();
                    rs.next();
                    dtm.addRow(new String[]{null, null, null, null});
                    dtm.addRow(new String[]{null, "<html><b><font color='red'>VALOR TOTAL:</font></html>", "<html><b>R$ " + rs.getString("total").replace(".", ",") + "</b></html>", null});

                }
            } else {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1017");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1016\nDetalhes do erro:\n" + e);
        }
    }

    private void delConta(String param) {
        switch (param) {
            case "vist":
                try {
                    con.prepareStatement("DELETE FROM finances WHERE id='" + txtCod.getText() + "'").executeUpdate();
                    modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Conta deletada com sucesso!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao deletar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1028\nDetalhes do erro:\n" + e);
                }
                break;
            case "parc":
                try {
                    con.prepareStatement("DELETE FROM finances WHERE pri_parcela='" + pri_parcela + "'").executeUpdate();
                    modified = true;
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Conta deletada com sucesso!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao deletar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1029\nDetalhes do erro:\n" + e);
                }
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPlan = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtCadBy = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtModBy = new javax.swing.JTextField();
        txtModTime = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtCadTime = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cbParc = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        txtParVlr = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPar = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtPar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtParVenc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbVis = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        txtVlr = new javax.swing.JTextField();
        txtVenc = new javax.swing.JTextField();
        btnEdit = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        btnPay = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DETALHAMENTO DE CONTAS À PAGAR");
        setAlwaysOnTop(true);
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Título/Doc:");

        txtTit.setEditable(false);

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Observações:");

        txtObs.setEditable(false);
        txtObs.setColumns(20);
        txtObs.setLineWrap(true);
        txtObs.setRows(3);
        txtObs.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtObs);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Código:");

        txtCod.setEditable(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Plano de Contas:");

        txtPlan.setEditable(false);

        jLabel14.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel14.setText("Cadastrado por:");

        txtCadBy.setEditable(false);
        txtCadBy.setForeground(new java.awt.Color(51, 51, 51));

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setText("Modificado por:");

        txtModBy.setEditable(false);
        txtModBy.setForeground(new java.awt.Color(51, 51, 51));

        txtModTime.setEditable(false);
        txtModTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel16.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel16.setText("Modificado em:");

        txtCadTime.setEditable(false);
        txtCadTime.setForeground(new java.awt.Color(51, 51, 51));

        jLabel17.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel17.setText("Cadastrado em:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(txtTit)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtCod, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtPlan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14)
                            .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15)
                            .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCadTime)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel17)
                                    .addGap(41, 41, 41)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel16)
                                .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPlan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCadBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtModBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setOpaque(false);

        cbParc.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cbParc.setText("PARCELADO");
        cbParc.setEnabled(false);
        cbParc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbParcActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Valor:");

        txtParVlr.setEditable(false);
        txtParVlr.setEnabled(false);

        tblPar.setAutoCreateRowSorter(true);
        tblPar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parcela", "Vencimento", "Valor", "Situação"
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
        tblPar.setEnabled(false);
        tblPar.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPar.getTableHeader().setReorderingAllowed(false);
        tblPar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblParMouseDragged(evt);
            }
        });
        tblPar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblParMousePressed(evt);
            }
        });
        tblPar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblParKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblPar);

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Parcela:");

        txtPar.setEditable(false);
        txtPar.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel12.setText("Vencimento:");

        txtParVenc.setEditable(false);
        txtParVenc.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("QUADRO DE PARCELAS:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addComponent(jScrollPane2)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbParc)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel8))
                                    .addComponent(txtPar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtParVenc, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(txtParVlr, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator5))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbParc)
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParVlr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, 510));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Valor Total:");

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("Vencimento:");

        cbVis.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cbVis.setText("À VISTA");
        cbVis.setEnabled(false);
        cbVis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVisActionPerformed(evt);
            }
        });

        txtVlr.setEditable(false);
        txtVlr.setEnabled(false);

        txtVenc.setEditable(false);
        txtVenc.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbVis)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtVlr, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbVis)
                .addGap(5, 5, 5)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVlr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, -1));

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/edit - 48x48.png"))); // NOI18N
        btnEdit.setText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        getContentPane().add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 540, 197, -1));

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnDel.setText("Deletar");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });
        getContentPane().add(btnDel, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 540, 198, 60));

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1492770700_018.png"))); // NOI18N
        btnExit.setText("Fechar Janela");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 540, 198, -1));
        getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 854, -1));

        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money - 48x48.png"))); // NOI18N
        btnPay.setText("Pagar");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        getContentPane().add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 197, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 895, 612));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        DespesaForm df = new DespesaForm(home, fin_id);
        df.setVisible(true);
        if (df.modified) {
            if (df.updMode == 1 || df.updMode == 3) {
                //NESSE CASO, DELETOU O ANTIGO, QUANDO SAIR DA TELA DF, QUE VOLTAR PRA DETALHES, VAI DAR ERRO PQ ESSE REGISTRO NÃO EXISTE MAIS
                this.dispose();
                modified = true;
            } else {
                listData();
                modified = true;
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        if (parc) {
            int w = JOptionPane.showConfirmDialog(this, "Você deseja realmente deletar todas as parcelas desta conta?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (w == JOptionPane.YES_OPTION) {
                delConta("parc");
            }
        } else {
            int w = JOptionPane.showConfirmDialog(this, "Você deseja realmente deletar esta conta?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (w == JOptionPane.YES_OPTION) {
                delConta("vist");
            }
        }
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        DespesaPagar dp = new DespesaPagar(home, fin_id);
        dp.setVisible(true);
        if (dp.modified == 1) {
            listData();
            modified = true; //PEGO ESSA VARIÁVEL NO FRAME "DespesasAbertas"
        } else if (dp.modified == 2) {
            this.dispose();
            modified = true;
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void cbVisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVisActionPerformed
        cbVis.setSelected(true);
    }//GEN-LAST:event_cbVisActionPerformed

    private void cbParcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbParcActionPerformed
        cbParc.setSelected(true);
    }//GEN-LAST:event_cbParcActionPerformed

    private void tblParMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblParMousePressed
        //Seleciona linha correspondente a conta na tabela
        for (int i = 0; i < dtm.getRowCount(); i++) {
            if (dtm.getValueAt(i, 1).equals(txtParVenc.getText())) {
                tblPar.setRowSelectionInterval(i, i);
            }
        }
    }//GEN-LAST:event_tblParMousePressed

    private void tblParMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblParMouseDragged
        //Seleciona linha correspondente a conta na tabela
        for (int i = 0; i < dtm.getRowCount(); i++) {
            if (dtm.getValueAt(i, 1).equals(txtParVenc.getText())) {
                tblPar.setRowSelectionInterval(i, i);
            }
        }
    }//GEN-LAST:event_tblParMouseDragged

    private void tblParKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblParKeyReleased
        evt.consume();
    }//GEN-LAST:event_tblParKeyReleased

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
            java.util.logging.Logger.getLogger(DespesaDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DespesaDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DespesaDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DespesaDetalhes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DespesaDetalhes dialog = new DespesaDetalhes(new javax.swing.JDialog());
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
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnPay;
    private javax.swing.JCheckBox cbParc;
    private javax.swing.JCheckBox cbVis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable tblPar;
    private javax.swing.JTextField txtCadBy;
    private javax.swing.JTextField txtCadTime;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtModBy;
    private javax.swing.JTextField txtModTime;
    private javax.swing.JTextArea txtObs;
    private javax.swing.JTextField txtPar;
    private javax.swing.JTextField txtParVenc;
    private javax.swing.JTextField txtParVlr;
    private javax.swing.JTextField txtPlan;
    private javax.swing.JTextField txtTit;
    private javax.swing.JTextField txtVenc;
    private javax.swing.JTextField txtVlr;
    // End of variables declaration//GEN-END:variables

}
