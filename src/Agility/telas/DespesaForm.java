package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author jeosafaferreira
 */
public class DespesaForm extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String type, fin_id, sql, acc_plan_id[], pri_parcela;
    boolean modified, parcelado;
    DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateTimeSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DefaultTableModel dtm;
    Home home;
    int updMode = 0;

    public DespesaForm(java.awt.Frame parent) {
        super(parent);
        initComponents();
        formatInputs();
        con = ModuloConexao.conector();
        type = "new";
        dtm = (DefaultTableModel) tblPar.getModel();
        getData("new");
        cboPlan.setSelectedIndex(-1);
    }

    public DespesaForm(java.awt.Frame parent, String id) {
        super(parent);
        initComponents();
        formatInputs();
        con = ModuloConexao.conector();
        type = "upd";
        dtm = (DefaultTableModel) tblPar.getModel();
        getData(id);
        fin_id = id;
        this.setTitle("ALTERAR CONTA À PAGAR - (#" + fin_id + ")");
    }

    private void formatInputs() {
        txtVlr.setDocument(new AgilitySec.onlyNumbersNPoints());
        txtQtdPar.setDocument(new AgilitySec.onlyNumbers());
        txtVlrPar.setDocument(new AgilitySec.onlyNumbersNPoints());
    }

    private void getData(String id) {
        txtPlanCod.setFocusTraversalKeysEnabled(false); //Isso é para poder capturar quando a tecla tab for pressionada (Pra manipular o foco)
        txtTit.setFocusTraversalKeysEnabled(false);

        //Buscando plano de contas
        try {
            rs = con.prepareStatement("SELECT * FROM acc_plan WHERE type='Pagamentos'").executeQuery();
            rs.last();
            acc_plan_id = new String[rs.getRow()];
            Arrays.fill(acc_plan_id, null); //Zerando array
            cboPlan.removeAllItems();
            rs.absolute(0);
            int i = 0;
            while (rs.next()) {
                cboPlan.addItem(rs.getString("title"));
                acc_plan_id[i] = rs.getString("id");
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1001\nDetalhes do erro:\n" + e);
            this.dispose();
        }

        //Buscando dados cadastrados
        if (!id.equals("new")) {
            try {
                rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE A.id='" + id + "'").executeQuery();
                if (rs.next()) {
                    txtPlanCod.setText(rs.getString("B.id"));
                    cboPlan.setSelectedItem(rs.getString("B.title"));
                    txtTit.setText(rs.getString("A.titulo"));
                    txtObs.setText(rs.getString("A.obs"));

                    if (rs.getString("A.tot_parcela").equals("1")) {
                        //CONTA À VISTA
                        parcelado = false;
                        txtVlr.setText("R$ " + rs.getString("A.valor"));
                        txtVenc.setText(dateBr.format(dateSql.parse(rs.getString("A.venc"))));

                        cbVis.setSelected(true);
                        cbVis.setEnabled(true);
                        txtVlr.setEnabled(true);
                        txtVenc.setEnabled(true);

                    } else {
                        //BUSCA TODAS AS PARCELAS
                        rs = con.prepareStatement("SELECT id,parcela,tot_parcela,venc,valor FROM finances WHERE id=" + rs.getString("A.pri_parcela")).executeQuery();
                        rs.next();

                        parcelado = true;
                        pri_parcela = rs.getString("id");
                        txtQtdPar.setText(rs.getString("tot_parcela"));
                        txtVlrPar.setText(rs.getString("valor"));

                        cbParc.setSelected(true);
                        cbParc.setEnabled(true);
                        tblPar.setEnabled(true);
                        txtVencPar.setEnabled(true);
                        txtQtdPar.setEnabled(true);
                        txtVlrPar.setEnabled(true);
                        btnLim.setEnabled(true);
                        btnGerar.setEnabled(true);

                        rs = con.prepareStatement("SELECT id,parcela,venc,valor,pri_parcela, valor_pago FROM finances WHERE pri_parcela=" + rs.getString("id")).executeQuery();

                        dtm.setNumRows(0); //Zerando tabela
                        while (rs.next()) {
                            dtm.addRow(new String[]{rs.getString("parcela"), dateBr.format(dateSql.parse(rs.getString("venc"))), "R$ " + rs.getString("valor").replace(".", ",")});
                        }

                        rs.first();
                        txtVencPar.setText(dateBr.format(dateSql.parse(rs.getString("venc"))));

                        //VALOR TOTAL:
                        rs = con.prepareStatement("SELECT SUM(valor) AS total FROM finances WHERE pri_parcela=" + rs.getString("pri_parcela")).executeQuery();
                        rs.next();
                        dtm.addRow(new String[]{null, null, null, null});
                        dtm.addRow(new String[]{null, "<html><b><font color='red'>VALOR TOTAL:</font></html>", "<html><b>R$ " + rs.getString("total").replace(".", ",") + "</b></html>", null});

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao consultar ao banco de dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1017");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1025\nDetalhes do erro:\n" + e);
                this.dispose();
            }
        }
    }

    /**
     * RETORNA: INT 0: Se falhar no check; INT 1: Se passar no check;
     */
    private int check() {
        int r = 1;
        if (txtPlanCod.getText().isEmpty()) {
            r = 0;
            JOptionPane.showMessageDialog(null, "Por favor, selecione um plano de contas.");
            txtPlanCod.grabFocus();

        } else if (txtTit.getText().isEmpty()) {
            r = 0;
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Título/Doc\".");
            txtTit.grabFocus();

        } else {
            if (cbVis.isSelected()) {
                if (txtVlr.getText().isEmpty()) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Valor Total\".");
                    txtVlr.grabFocus();
                } else if (txtVenc.getText().equals("  /  /    ")) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Vencimento\".");
                    txtVenc.grabFocus();
                }
            } else if (cbParc.isSelected()) {
                if (tblPar.getRowCount() < 1) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, se esta conta for parcelada, preencha corretamente todos os campos relacionados ao parcelamento e clique no botão \"Gerar\".\nSe for à vista, marque a opção \"Á vista\" no painel inferior esquerdo.");
                    txtVlrPar.grabFocus();
                } else if (txtVencPar.getText().equals("  /  /    ")) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Vencimento da 1º Parcela\".");
                    txtVencPar.grabFocus();
                } else if (txtQtdPar.getText().isEmpty()) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Qtd. de Parcelas\".");
                    txtQtdPar.grabFocus();
                } else if (txtVlrPar.getText().isEmpty()) {
                    r = 0;
                    JOptionPane.showMessageDialog(null, "Por favor, preencha o campo \"Valor da Parcela\".");
                    txtVlrPar.grabFocus();
                }
            } else {
                r = 0;
                JOptionPane.showMessageDialog(null, "Por favor, informe se o pagamento é parcelado ou à vista.");
            }
            if (r > 0) {
                //VALIDA DATA
                String msg = null;
                try {
                    dateBr.setLenient(false);
                    if (cbVis.isSelected()) {
                        dateBr.parse(txtVenc.getText());
                        msg = "Por favor, preencha corretamente o campo \"Vencimento\".";
                    } else {
                        dateBr.parse(txtVencPar.getText());
                        msg = "Por favor, preencha corretamente o campo \"Venc. da 1º Parcela\".";
                    }
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, msg);
                    txtVenc.setText("");
                    txtVenc.grabFocus();
                    r = 0;
                }
            }
        }
        return r;
    }

    private void execQuery() {
        int ret = 0;
        int new_id = 0;

        if (check() == 1) {
            String txtErro1 = null;
            String txtErro2 = null;
            String txtSuc = null;

            if (type.equals("new")) {
                sql = "INSERT INTO finances (titulo, parcela, tot_parcela, valor, venc, obs, acc_plan_id, pri_parcela, created_by) VALUES(?,?,?,?,?,?,?,?,?)";
                txtSuc = "Conta cadastrada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao salvar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1002";
                txtErro2 = "Desculpe, ocorreu um erro ao salvar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1003\nDetalhes do erro:\n";

            } else if (type.equals("upd")) {
                if (parcelado) {
                    if (cbVis.isSelected()) {
                        //CONTA QUE ERA PARCELADA, AGORA É À VISTA.
                        updMode = 1;
                        try {
                            con.prepareStatement("DELETE FROM finances WHERE pri_parcela='" + pri_parcela + "'").executeUpdate();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao atualizar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1026\nDetalhes do erro:\n" + e);
                        }
                        sql = "INSERT INTO finances (titulo, parcela, tot_parcela, valor, venc, obs, acc_plan_id, pri_parcela, created_by) VALUES(?,?,?,?,?,?,?,?,?)";
                    } else {
                        //CONTA QUE ERA PARCELADA E CONTINUA PARCELADA.
                        updMode = 2;
                        sql = "UPDATE finances SET titulo=?, valor=?, venc=?, obs=?, acc_plan_id=?, modified_by=? WHERE pri_parcela='" + pri_parcela + "' AND parcela = ?";
                    }
                } else if (cbParc.isSelected()) {
                    //CONTA QUE ERA À VISTA E AGORA É PARCELADA.
                    updMode = 3;
                    try {
                        con.prepareStatement("DELETE FROM finances WHERE id='" + fin_id + "'").executeUpdate();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro ao atualizar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1027\nDetalhes do erro:\n" + e);
                    }
                    sql = "INSERT INTO finances (titulo, parcela, tot_parcela, valor, venc, obs, acc_plan_id, pri_parcela, created_by) VALUES(?,?,?,?,?,?,?,?,?)";
                } else {
                    //CONTA QUE ERA À VISTA E CONTINUA À VISTA.
                    updMode = 4;
                    sql = "UPDATE finances SET titulo=?, valor=?, venc=?, obs=?, acc_plan_id=?, modified_by=? WHERE id='" + fin_id + "'";
                }
                txtSuc = "Conta atualizada com sucesso!";
                txtErro1 = "Desculpe, ocorreu um erro ao atualizar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1004";
                txtErro2 = "Desculpe, ocorreu um erro ao atualizar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1005\nDetalhes do erro:\n";
            }

            try {
                pst = con.prepareStatement(sql);
                //PEGANDO O VALOR ATUAL DO AUTO_INCREMENT
                rs = con.prepareStatement("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'agility' AND TABLE_NAME = 'finances'").executeQuery();
                rs.next();
                new_id = rs.getInt("AUTO_INCREMENT");
                if (type.equals("new")) {
                    if (cbParc.isSelected()) {
                        //CONTA PARCELADA
                        int rows = Integer.parseInt(txtQtdPar.getText());
                        if (rows > 0) { //VERIFICA SE A TABELA DE PARCELAMENTO POSSUI DADOS
                            for (int i = 0; i < rows; i++) {
                                pst.setString(1, txtTit.getText());
                                pst.setString(2, tblPar.getValueAt(i, 0).toString());
                                pst.setString(3, Integer.toString(rows));
                                pst.setDouble(4, Double.parseDouble(tblPar.getValueAt(i, 2).toString().replace("R$ ", "").replace(",", ".")));
                                pst.setString(5, dateSql.format(dateBr.parse(tblPar.getValueAt(i, 1).toString())));
                                pst.setString(6, txtObs.getText());
                                pst.setString(7, acc_plan_id[cboPlan.getSelectedIndex()]);
                                pst.setString(8, Integer.toString(new_id));
                                pst.setString(9, Login.emp_id);
                                ret = pst.executeUpdate();
                            }
                        }
                    } else if (cbVis.isSelected()) {
                        //CONTA À VISTA
                        pst.setString(1, txtTit.getText());
                        pst.setString(2, "1");
                        pst.setString(3, "1");
                        pst.setDouble(4, Double.parseDouble(txtVlr.getText().replace(",", ".").replace("R$", "")));
                        pst.setString(5, dateSql.format(dateBr.parse(txtVenc.getText())));
                        pst.setString(6, txtObs.getText());
                        pst.setString(7, acc_plan_id[cboPlan.getSelectedIndex()]);
                        pst.setString(8, Integer.toString(new_id));
                        pst.setString(9, Login.emp_id);
                        ret = pst.executeUpdate();
                    }
                } else if (type.equals("upd")) {
                    switch (updMode) {
                        case 1:
                            //CONTA QUE ERA PARCELADA, AGORA É À VISTA.
                            pst.setString(1, txtTit.getText());
                            pst.setString(2, "1");
                            pst.setString(3, "1");
                            pst.setDouble(4, Double.parseDouble(txtVlr.getText().replace(",", ".").replace("R$", "")));
                            pst.setString(5, dateSql.format(dateBr.parse(txtVenc.getText())));
                            pst.setString(6, txtObs.getText());
                            pst.setString(7, acc_plan_id[cboPlan.getSelectedIndex()]);
                            pst.setString(8, Integer.toString(new_id));
                            pst.setString(9, Login.emp_id);
                            ret = pst.executeUpdate();
                            break;
                        case 2:
                            //CONTA QUE ERA PARCELADA E CONTINUA PARCELADA.
                            int rows = Integer.parseInt(txtQtdPar.getText());
                            for (int i = 0; i < rows; i++) {
                                pst.setString(1, txtTit.getText());
                                pst.setDouble(2, Double.parseDouble(tblPar.getValueAt(i, 2).toString().replace("R$ ", "").replace(",", ".")));
                                pst.setString(3, dateSql.format(dateBr.parse(tblPar.getValueAt(i, 1).toString())));
                                pst.setString(4, txtObs.getText());
                                pst.setString(5, acc_plan_id[cboPlan.getSelectedIndex()]);
                                pst.setString(6, Login.emp_id);
                                pst.setString(7, tblPar.getValueAt(i, 0).toString());
                                ret = pst.executeUpdate();
                            }
                            break;
                        case 3:
                            //CONTA QUE ERA À VISTA E AGORA É PARCELADA.
                            rows = Integer.parseInt(txtQtdPar.getText());
                            for (int i = 0; i < rows; i++) {
                                pst.setString(1, txtTit.getText());
                                pst.setString(2, tblPar.getValueAt(i, 0).toString());
                                pst.setString(3, Integer.toString(rows));
                                pst.setDouble(4, Double.parseDouble(tblPar.getValueAt(i, 2).toString().replace("R$ ", "").replace(",", ".")));
                                pst.setString(5, dateSql.format(dateBr.parse(tblPar.getValueAt(i, 1).toString())));
                                pst.setString(6, txtObs.getText());
                                pst.setString(7, acc_plan_id[cboPlan.getSelectedIndex()]);
                                pst.setString(8, Integer.toString(new_id));
                                pst.setString(9, Login.emp_id);
                                ret = pst.executeUpdate();
                            }
                            break;
                        case 4:
                            //CONTA QUE ERA À VISTA E CONTINUA À VISTA.
                            pst.setString(1, txtTit.getText());
                            pst.setDouble(2, Double.parseDouble(txtVlr.getText().replace(",", ".").replace("R$", "")));
                            pst.setString(3, dateSql.format(dateBr.parse(txtVenc.getText())));
                            pst.setString(4, txtObs.getText());
                            pst.setString(5, acc_plan_id[cboPlan.getSelectedIndex()]);
                            pst.setString(6, Login.emp_id);
                            ret = pst.executeUpdate();
                            break;
                    }
                }
                if (ret == 0) {
                    JOptionPane.showMessageDialog(this, txtErro1);
                } else {
                    JOptionPane.showMessageDialog(this, txtSuc);
                    modified = true;
                    this.dispose();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, txtErro2 + e);
            }
        }
    }

    private void geraParc() throws ParseException {
        dtm.setNumRows(0); //Limpando Tabela

        double qtdPar = 0;
        double vlrPar;
        DecimalFormat df = new DecimalFormat("0.##");
        Calendar cal = Calendar.getInstance();

        //INICIO CHECK
        if (txtVencPar.getText().equals("  /  /    ")) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Vencimento da 1º Parcela\".");
            txtVencPar.grabFocus();
        } else if (txtQtdPar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Qtd. de Parcelas\".");
            txtQtdPar.grabFocus();
        } else if (txtVlrPar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Valor da Parcela\".");
            txtVlrPar.grabFocus();
        } else {
            //valida vencimento
            boolean vencOk = true;
            try {
                dateBr.setLenient(false);
                dateBr.parse(txtVencPar.getText());
                cal.setTime(dateBr.parse(txtVencPar.getText()));
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //Pra que o primeiro venc seja a data que foi informada o txtVenc
            } catch (ParseException e) {
                vencOk = false;
                txtVencPar.setText("");
                txtVencPar.grabFocus();
                JOptionPane.showMessageDialog(this, "Por favor, preencha corretamente o campo \"Venc. da 1º Parcela\".");
            }
            //FIM CHECK

            if (vencOk) {
                try {
                    qtdPar = Integer.parseInt(txtQtdPar.getText());

                } catch (Exception e) {
                    AgilitySec.checkMessageError(this, txtQtdPar, "Qtd. de Parcelas");
                }
                int re = 0;
                if (qtdPar > 100) {
                    int q = JOptionPane.showConfirmDialog(this, "<html>Tem certeza que deseja gerar <b>" + txtQtdPar.getText() + "</b> prestações?", "Atenção!", JOptionPane.YES_NO_OPTION);
                    if (q == JOptionPane.YES_OPTION) {
                        re = 1;
                    }
                } else {
                    re = 1;
                }
                if (re == 1) {
                    vlrPar = Double.parseDouble(txtVlrPar.getText().replace(",", "."));
                    for (int k = 0; k < qtdPar; k++) {
                        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
                        dtm.addRow(new Object[]{k + 1, dateBr.format(cal.getTime()), "R$ " + txtVlrPar.getText().replace(".", ",")});
                    }
                    dtm.addRow(new Object[]{null, null, null});
                    dtm.addRow(new String[]{null, "<html><b><font color='red'>VALOR TOTAL:</font></html>", ("<html><b>R$ " + (vlrPar * qtdPar)).replace(".", ",")});
                }
            }
        }
    }

    private boolean getAccPlan() {
        boolean ret = false;
        if (txtPlanCod.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um plano de contas.");
        } else {
            try {
                rs = con.prepareStatement("SELECT * FROM acc_plan WHERE id='" + txtPlanCod.getText() + "'").executeQuery();
                if (rs.next()) {
                    cboPlan.setSelectedItem(rs.getString("title"));
                    txtTit.grabFocus();
                    ret = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Plano de Contas não encontrado.");
                    txtPlanCod.setText("");
                    txtPlanCod.grabFocus();
                    cboPlan.setSelectedIndex(-1);
                    ret = false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao cadastrar esta conta.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1030\nDetalhes do erro:\n" + e);
            }
        }

        return ret;
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
        jLabel74 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cbParc = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        txtVlrPar = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPar = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtQtdPar = new javax.swing.JTextField();
        btnLim = new javax.swing.JButton();
        btnGerar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtVencPar = new javax.swing.JTextField();
        try{ MaskFormatter data = new MaskFormatter("##/##/####"); txtVencPar = new JFormattedTextField(data);  txtVencPar.setEnabled(false); }catch (Exception e){ }
        btnCan = new javax.swing.JButton();
        btnSal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        cbVis = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        txtVlr = new javax.swing.JTextField();
        txtVenc = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        btnAddPlan = new javax.swing.JButton();
        cboPlan = new javax.swing.JComboBox<>();
        txtPlanCod = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRAR CONTA À PAGAR");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Título/Doc:");

        txtTit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTitKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Observações:");

        txtObs.setColumns(20);
        txtObs.setLineWrap(true);
        txtObs.setRows(5);
        txtObs.setWrapStyleWord(true);
        txtObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObsKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtObs);

        jLabel74.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(233, 2, 2));
        jLabel74.setText("*");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addComponent(txtTit)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel74))
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 98, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setOpaque(false);

        cbParc.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cbParc.setText("Parcelado");
        cbParc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbParcActionPerformed(evt);
            }
        });

        jLabel7.setText("Valor da Parcela:");

        txtVlrPar.setEnabled(false);

        tblPar.setAutoCreateRowSorter(true);
        tblPar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parcela", "Vencimento", "Valor"
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
        tblPar.setEnabled(false);
        tblPar.setRowSelectionAllowed(false);
        tblPar.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblPar);

        jLabel8.setText("Qtd. de Parcelas:");

        txtQtdPar.setEnabled(false);

        btnLim.setText("Limpar");
        btnLim.setEnabled(false);
        btnLim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimActionPerformed(evt);
            }
        });

        btnGerar.setText("Gerar");
        btnGerar.setEnabled(false);
        btnGerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarActionPerformed(evt);
            }
        });

        jLabel12.setText("Venc. da 1º Parcela:");

        txtVencPar.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(cbParc)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtQtdPar, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtVlrPar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnGerar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnLim))))
                            .addComponent(jLabel12)
                            .addComponent(txtVencPar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 65, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbParc)
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVencPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVlrPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLim)
                            .addComponent(btnGerar)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtQtdPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(357, 6, -1, 430));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(607, 442, 127, -1));

        btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnSal.setText("Salvar");
        btnSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalActionPerformed(evt);
            }
        });
        getContentPane().add(btnSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 442, 111, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel3.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Valor Total (R$):");

        jLabel77.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(233, 2, 2));
        jLabel77.setText("*");

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("Vencimento:");

        jLabel78.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(233, 2, 2));
        jLabel78.setText("*");

        cbVis.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cbVis.setText("Á vista");
        cbVis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVisActionPerformed(evt);
            }
        });

        txtVlr.setEnabled(false);

        txtVenc.setEnabled(false);
        try{
            MaskFormatter data = new MaskFormatter("##/##/####");
            txtVenc = new JFormattedTextField(data);
            txtVenc.setEnabled(false);
        }catch (Exception e){ }

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
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel77))
                            .addComponent(txtVlr, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel78))
                            .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbVis)
                .addGap(5, 5, 5)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel77)
                    .addComponent(jLabel11)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVlr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 316, 345, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setOpaque(false);

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel15.setText("Código:");

        jLabel79.setFont(new java.awt.Font("Cantarell", 0, 15)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(233, 2, 2));
        jLabel79.setText("*");

        jLabel16.setText("Plano de Contas:");

        btnAddPlan.setText("+");
        btnAddPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPlanActionPerformed(evt);
            }
        });

        cboPlan.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        cboPlan.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboPlanPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        txtPlanCod.setBackground(new java.awt.Color(204, 255, 204));
        txtPlanCod.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtPlanCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPlanCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPlanCodKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel79)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel16)
                        .addGap(0, 116, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtPlanCod, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboPlan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddPlan)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel79)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlanCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPlan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPlan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 345, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 870, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPlanActionPerformed
        PlanoContasForm pcf = new PlanoContasForm(home);
        pcf.cboTip.setSelectedIndex(0);
        pcf.cboTip.setEnabled(false);
        pcf.setVisible(true);
        pcf.setModalityType(ModalityType.APPLICATION_MODAL);
        if (pcf.modified) {
            getData("new");
            cboPlan.setSelectedItem(pcf.txtTit.getText());
            txtPlanCod.setText(acc_plan_id[cboPlan.getSelectedIndex()]);
        }
    }//GEN-LAST:event_btnAddPlanActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (txtPlanCod.getText().isEmpty() && txtTit.getText().isEmpty() && txtVlr.getText().isEmpty() && txtVenc.getText().equals("  /  /    ") && txtObs.getText().isEmpty() && !cbParc.isSelected()) {
                this.dispose();
            } else {
                int q = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja cancelar? Todos os dados serão perdidos.", "Atenção!", JOptionPane.YES_NO_OPTION);
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

    private void cbParcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbParcActionPerformed
        if (cbParc.isSelected()) {
            if (cbVis.isSelected()) {
                cbVis.setSelected(false);
                txtVlr.setText("");
                txtVenc.setText("");
                txtVlr.setEnabled(false);
                txtVenc.setEnabled(false);
            }

            txtVencPar.setEnabled(true);
            txtQtdPar.setEnabled(true);
            txtVlrPar.setEnabled(true);
            btnGerar.setEnabled(true);
            btnLim.setEnabled(true);
            tblPar.setEnabled(true);
            tblPar.setEnabled(true);
            txtVencPar.grabFocus();
        } else {
            txtVencPar.setEnabled(false);
            txtQtdPar.setEnabled(false);
            txtVlrPar.setEnabled(false);
            btnGerar.setEnabled(false);
            btnLim.setEnabled(false);
            tblPar.setEnabled(false);
            tblPar.setEnabled(false);
        }
    }//GEN-LAST:event_cbParcActionPerformed

    private void btnGerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarActionPerformed
        try {
            geraParc();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Desculpe, ocorreu um erro interno.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1006\nDetalhes do erro:\n" + ex);
        }


    }//GEN-LAST:event_btnGerarActionPerformed

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        txtVencPar.setText("");
        txtQtdPar.setText("");
        txtVlrPar.setText("");
        dtm.setNumRows(0);
    }//GEN-LAST:event_btnLimActionPerformed

    private void cboPlanPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboPlanPopupMenuWillBecomeInvisible
        if (cboPlan.getSelectedIndex() != -1) {
            txtPlanCod.setText(acc_plan_id[cboPlan.getSelectedIndex()]);
            txtTit.grabFocus();
        }
    }//GEN-LAST:event_cboPlanPopupMenuWillBecomeInvisible

    private void txtPlanCodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlanCodKeyPressed
        txtPlanCod.setFocusTraversalKeysEnabled(false);
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                getAccPlan();
                break;
            case KeyEvent.VK_TAB:
                getAccPlan();
                break;
        }
    }//GEN-LAST:event_txtPlanCodKeyPressed

    private void cbVisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVisActionPerformed
        if (cbVis.isSelected()) {
            if (cbParc.isSelected()) {
                btnLimActionPerformed(null);
                cbParc.setSelected(false);
                txtVencPar.setEnabled(false);
                txtQtdPar.setEnabled(false);
                txtVlrPar.setEnabled(false);
                btnGerar.setEnabled(false);
                btnLim.setEnabled(false);
                tblPar.setEnabled(false);
                tblPar.setEnabled(false);

            }
            txtVlr.setEnabled(true);
            txtVenc.setEnabled(true);
            txtVlr.grabFocus();
        } else {
            txtVlr.setText("");
            txtVenc.setText("");
            txtVlr.setEnabled(false);
            txtVenc.setEnabled(false);
        }
    }//GEN-LAST:event_cbVisActionPerformed

    private void txtObsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObsKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            if (evt.isShiftDown()) {
                txtObs.transferFocusBackward();
            } else {
                txtObs.transferFocus();
            }
        }
    }//GEN-LAST:event_txtObsKeyPressed

    private void txtTitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            if (evt.isShiftDown()) {
                txtPlanCod.grabFocus();
            } else {
                txtTit.transferFocus();
            }
        }
    }//GEN-LAST:event_txtTitKeyPressed

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
            java.util.logging.Logger.getLogger(DespesaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DespesaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DespesaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DespesaForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DespesaForm dialog = new DespesaForm(new javax.swing.JFrame());
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
    private javax.swing.JButton btnAddPlan;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnGerar;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnSal;
    private javax.swing.JCheckBox cbParc;
    private javax.swing.JCheckBox cbVis;
    private javax.swing.JComboBox<String> cboPlan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tblPar;
    private javax.swing.JTextArea txtObs;
    private javax.swing.JTextField txtPlanCod;
    private javax.swing.JTextField txtQtdPar;
    private javax.swing.JTextField txtTit;
    private javax.swing.JTextField txtVenc;
    private javax.swing.JTextField txtVencPar;
    private javax.swing.JTextField txtVlr;
    private javax.swing.JTextField txtVlrPar;
    // End of variables declaration//GEN-END:variables

}
