package Agility.telas;

import Agility.api.AgilitySec;
import Agility.dal.ModuloConexao;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author jeosafaferreira
 */
public class DespesaPagar extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String fop_id[];
    String finance_id, finance_venc, vlrTot, type;
    Date today = new Date();
    DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");
    BigDecimal finance_vlr = new BigDecimal("0.00");
    BigDecimal juros = new BigDecimal("0.00");
    BigDecimal multa = new BigDecimal("0.00");
    BigDecimal desc = new BigDecimal("0.00");
    long daysDelay;
    int modified;

    public DespesaPagar(java.awt.Frame parent, String cod) {
        super(parent);
        initComponents();
        finance_id = cod;
        con = ModuloConexao.conector();
        txtDes.setDocument(new onlyNumbers());
        txtJur.setDocument(new onlyNumbers());
        txtMul.setDocument(new onlyNumbers());
        txtPDes.setDocument(new onlyNumbers());
        txtPJur.setDocument(new onlyNumbers());
        txtPMul.setDocument(new onlyNumbers());
        try {
            rs = con.prepareStatement("SELECT valor_pago FROM finances WHERE id='" + cod + "' AND valor_pago IS NOT NULL").executeQuery();
            if (rs.next()) {
                type = "payed";
            } else {
                type = "notPayed";
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1023\nDetalhes do erro:\n" + e);
        }
        getData();
    }

    private void getData() {
        try {
            //Pega formas de pagamento
            rs = con.prepareStatement("SELECT id, name FROM form_of_pay WHERE type='P'").executeQuery();
            rs.last();
            fop_id = new String[rs.getRow()];
            rs.absolute(0);

            int i = 0;
            while (rs.next()) {
                cboFor.addItem(rs.getString("name"));
                fop_id[i] = rs.getString("id");
                i++;
            }

            cboFor.setSelectedIndex(-1);

            //Pega dados da conta
            rs = con.prepareStatement("SELECT valor, venc FROM finances WHERE id='" + finance_id + "'").executeQuery();

            rs.next();
            txtVlr.setText("R$ " + rs.getString("valor").replace(".", ","));
            txtVenc.setText(dateBr.format(rs.getDate("venc")));
            finance_vlr = new BigDecimal(rs.getString("valor"));
            finance_venc = rs.getString("venc");

            //CONTA PAGA
            if (type.equals("payed")) {
                try {
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN form_of_pay B ON A.form_of_pay_id = B.id WHERE A.id='" + finance_id + "'").executeQuery();
                    rs.next();
                    cboFor.setSelectedItem(rs.getString("B.name"));
                    dcPag.setDate(rs.getDate("data_pag"));
                    txtPMul.setText(rs.getString("multa"));
                    txtPJur.setText(rs.getString("juros"));
                    txtPDes.setText(rs.getString("desc"));
                    txtVlr.setText("R$ " + rs.getString("valor_pago").replace(".", ","));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1024\nDetalhes do erro:\n" + e);
                }
                juros = new BigDecimal(txtPJur.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);
                multa = new BigDecimal(txtPMul.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);
                desc = new BigDecimal(txtPDes.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);

                txtJur.setText(juros.toString().replace(".", ","));
                txtMul.setText(multa.toString().replace(".", ","));
                txtDes.setText(desc.toString().replace(".", ","));

                jLabel10.setText("Valor Pago:");

                btnPay.setText("Estonar");
                btnCan.setText("Fechar");

                cboFor.setEnabled(false);
                dcPag.setEnabled(false);
                txtPDes.setEditable(false);
                txtPJur.setEditable(false);
                txtPMul.setEditable(false);
                btnHoj.setEnabled(false);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao buscar dados.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1007\nDetalhes do erro:\n" + e);
        }

    }

    public class onlyNumbers extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str.replaceAll("[^0-9,.]", ""), a);
        }
    }

    private void calcDays() {
        if (dcPag.getDate() != null) {
            try {
                dateBr.setLenient(false);
                daysDelay = dcPag.getDate().getTime() - dateSql.parse(finance_venc).getTime();
                daysDelay = TimeUnit.DAYS.convert(daysDelay, TimeUnit.MILLISECONDS);

                if (daysDelay < 1) {
                    txtAtr.setForeground(new java.awt.Color(0, 204, 0));
                    txtAtr.setText("0");

                    txtPJur.setEnabled(false);
                    txtPMul.setEnabled(false);
                    txtJur.setEnabled(false);
                    txtMul.setEnabled(false);

                    txtPDes.grabFocus();
                } else {
                    txtAtr.setForeground(new java.awt.Color(255, 0, 0));
                    txtAtr.setText(Long.toString(daysDelay));

                    txtPJur.setEnabled(true);
                    txtPMul.setEnabled(true);
                    txtJur.setEnabled(true);
                    txtMul.setEnabled(true);

                    txtPJur.grabFocus();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Data do Pagamento\" corretamente." + e);
                dcPag.grabFocus();
            }
        }
    }

    /**
     * RETORNA: INT 0: Se falhar no check; INT 1: Se passar no check;
     */
    private int check() {
        int r = 0;
        if (txtPMul.getText().equals("")) {
            txtPMul.setText("0.00");
        }
        if (txtPJur.getText().equals("")) {
            txtPJur.setText("0.00");
        }
        if (txtPDes.getText().equals("")) {
            txtPDes.setText("0.00");
        }
        if (cboFor.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione a forma de pagamento.");
            cboFor.grabFocus();
        } else if (dcPag.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha o campo \"Data do Pagamento\".");
            dcPag.grabFocus();
        } else {
            r = 1;
        }

        return r;
    }

    /**
     *
     * @return 1, se houve alteração no db; 0 se não houve alteração.
     */
    private void execQuery() {

        int r = 0;
        String txtSuc, sql;

        if (check() == 1) {

            sql = "UPDATE finances SET valor_pago=?, data_pag=?, multa=?, juros=?, `desc`=?, form_of_pay_id=?, modified_by=? WHERE id='" + finance_id + "'";
            txtSuc = "Conta atualizada com sucesso!";

            try {
                if (type.equals("payed")) {
                    int q = JOptionPane.showConfirmDialog(this, "Deseja realmente efetuar estorno?", "Atenção", JOptionPane.YES_NO_OPTION);
                    if (q == JOptionPane.YES_OPTION) {
                        //ESTORNAR
                        pst = con.prepareStatement(sql);
                        pst.setString(1, null);
                        pst.setString(2, null);
                        pst.setString(3, null);
                        pst.setString(4, null);
                        pst.setString(5, null);
                        pst.setString(6, null);
                        pst.setString(7, Login.emp_id);
                        if (pst.executeUpdate() == 0) {
                            AgilitySec.showError(this, "#1020");
                        } else {
                            this.dispose();
                            JOptionPane.showMessageDialog(null, txtSuc);
                            modified = 2; //Com o código 2, executa dispose() no DespesaDetalhes
                        }
                    }
                } else {
                    //PAGAR
                    pst = con.prepareStatement(sql);
                    pst.setString(1, txtVlr.getText().replace("R$ ", "").replace(",", "."));
                    pst.setString(2, dateSql.format(dcPag.getDate()));
                    pst.setString(3, txtPMul.getText().replace(",", "."));
                    pst.setString(4, txtPJur.getText().replace(",", "."));
                    pst.setString(5, txtPDes.getText().replace(",", "."));
                    pst.setString(6, fop_id[cboFor.getSelectedIndex()]);
                    pst.setString(7, Login.emp_id);
                    if (pst.executeUpdate() == 0) {
                        AgilitySec.showError(this, "#1019");
                    } else {
                        this.dispose();
                        JOptionPane.showMessageDialog(this, txtSuc);
                        modified = 1;
                    }
                }
            } catch (Exception e) {
                AgilitySec.showError(this, "#1019", e);

            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboFor = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        dcPag = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        txtAtr = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPJur = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPMul = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPDes = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtJur = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtMul = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDes = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtVlr = new javax.swing.JTextField();
        btnHoj = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtVenc = new javax.swing.JTextField();
        btnCan = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BAIXAR CONTA");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Forma de Pagamento:");

        cboFor.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        cboFor.setForeground(new java.awt.Color(51, 51, 51));
        cboFor.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboForPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Data do Pagamento:");

        dcPag.setPreferredSize(new java.awt.Dimension(50, 27));
        dcPag.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dcPagPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Dias em atraso:");

        txtAtr.setEditable(false);
        txtAtr.setBackground(new java.awt.Color(204, 204, 204));
        txtAtr.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtAtr.setForeground(new java.awt.Color(255, 0, 0));

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("(%)");

        txtPJur.setBackground(new java.awt.Color(255, 255, 204));
        txtPJur.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtPJur.setForeground(new java.awt.Color(51, 51, 51));
        txtPJur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPJur.setEnabled(false);
        txtPJur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPJurKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("(%)");

        txtPMul.setBackground(new java.awt.Color(255, 255, 204));
        txtPMul.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtPMul.setForeground(new java.awt.Color(51, 51, 51));
        txtPMul.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPMul.setEnabled(false);
        txtPMul.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPMulKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("(%)");

        txtPDes.setBackground(new java.awt.Color(255, 255, 204));
        txtPDes.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtPDes.setForeground(new java.awt.Color(51, 51, 51));
        txtPDes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPDes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPDesKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Juros diário (R$):");

        txtJur.setEditable(false);
        txtJur.setBackground(new java.awt.Color(204, 204, 204));
        txtJur.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtJur.setForeground(new java.awt.Color(51, 51, 51));
        txtJur.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Multa (R$):");

        txtMul.setEditable(false);
        txtMul.setBackground(new java.awt.Color(204, 204, 204));
        txtMul.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtMul.setForeground(new java.awt.Color(51, 51, 51));
        txtMul.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Desconto (R$):");

        txtDes.setEditable(false);
        txtDes.setBackground(new java.awt.Color(204, 204, 204));
        txtDes.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtDes.setForeground(new java.awt.Color(51, 51, 51));

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Valor Total:");

        txtVlr.setEditable(false);
        txtVlr.setBackground(new java.awt.Color(255, 255, 204));
        txtVlr.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N

        btnHoj.setText("Hoje");
        btnHoj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHojActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel13.setText("Vencimento:");

        txtVenc.setEditable(false);
        txtVenc.setBackground(new java.awt.Color(204, 204, 204));
        txtVenc.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtVenc.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboFor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtAtr, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPJur, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPMul, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtPDes, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGap(42, 42, 42)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(jLabel6)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMul, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(txtDes, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtJur, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addComponent(jLabel10)
                            .addComponent(txtVlr)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcPag, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHoj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboFor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHoj))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAtr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPJur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPMul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(6, 6, 6)
                .addComponent(txtVlr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, -1, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 480, -1, -1));

        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money.png"))); // NOI18N
        btnPay.setText("Baixar");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        getContentPane().add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 119, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-8, -5, 300, 540));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        if (type.equals("new")) {
            if (cboFor.getSelectedIndex() == -1 || txtAtr.getText().equals("")) {
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

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        execQuery();
    }//GEN-LAST:event_btnPayActionPerformed

    private void btnHojActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHojActionPerformed
        dcPag.setDate(today);
        txtPJur.grabFocus();
        calcDays();

    }//GEN-LAST:event_btnHojActionPerformed

    private void txtPJurKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPJurKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPMul.grabFocus();
        }
        juros = new BigDecimal(txtPJur.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);

        txtJur.setText(juros.toString().replace(".", ","));

        txtVlr.setText("R$ " + finance_vlr.add(multa).add(juros.multiply(new BigDecimal(daysDelay))).subtract(desc).setScale(2, RoundingMode.HALF_DOWN).toString().replace(".", ","));
    }//GEN-LAST:event_txtPJurKeyReleased

    private void txtPMulKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPMulKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPDes.grabFocus();
        }
        multa = new BigDecimal(txtPMul.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);
        txtMul.setText(multa.toString().replace(".", ","));

        txtVlr.setText("R$ " + finance_vlr.add(multa).add(juros.multiply(new BigDecimal(daysDelay))).subtract(desc).setScale(2, RoundingMode.HALF_DOWN).toString().replace(".", ","));

    }//GEN-LAST:event_txtPMulKeyReleased

    private void txtPDesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPDesKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnPay.grabFocus();
        }
        desc = new BigDecimal(txtPDes.getText().replace(",", ".")).multiply(finance_vlr).divide(new BigDecimal("100"), 4, RoundingMode.HALF_DOWN);
        txtDes.setText(desc.toString().replace(".", ","));

        txtVlr.setText("R$ " + finance_vlr.add(multa).add(juros.multiply(new BigDecimal(daysDelay))).subtract(desc).setScale(2, RoundingMode.HALF_DOWN).toString().replace(".", ","));
    }//GEN-LAST:event_txtPDesKeyReleased

    private void cboForPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboForPopupMenuWillBecomeInvisible
        cboFor.transferFocus();
    }//GEN-LAST:event_cboForPopupMenuWillBecomeInvisible

    private void dcPagPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dcPagPropertyChange
        calcDays();
    }//GEN-LAST:event_dcPagPropertyChange

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
            java.util.logging.Logger.getLogger(DespesaPagar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DespesaPagar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DespesaPagar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DespesaPagar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DespesaPagar dialog = new DespesaPagar(new javax.swing.JFrame(), "");
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
    private javax.swing.JButton btnHoj;
    private javax.swing.JButton btnPay;
    private javax.swing.JComboBox<String> cboFor;
    private com.toedter.calendar.JDateChooser dcPag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtAtr;
    private javax.swing.JTextField txtDes;
    private javax.swing.JTextField txtJur;
    private javax.swing.JTextField txtMul;
    private javax.swing.JTextField txtPDes;
    private javax.swing.JTextField txtPJur;
    private javax.swing.JTextField txtPMul;
    private javax.swing.JTextField txtVenc;
    private javax.swing.JTextField txtVlr;
    // End of variables declaration//GEN-END:variables

}
