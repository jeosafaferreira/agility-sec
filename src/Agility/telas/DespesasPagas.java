package Agility.telas;

import Agility.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author jferreira
 */
public class DespesasPagas extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home;
    DefaultTableModel dtm;
    DateFormat sqlData = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat vData = new SimpleDateFormat("dd/MM/yyyy");
    Calendar cal = new GregorianCalendar();
    java.util.Date hoje = new java.util.Date();
    int mesAtual = cal.get(Calendar.MONTH) + 1; //NÃO ALTERAR! O +1 é porque nessa classe, janeiro = 0
    int anoAtual = cal.get(Calendar.YEAR);
    int[] mesFiltro = new int[4];
    int[] anoFiltro = new int[4];

    public DespesasPagas(JFrame parent) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        dtm = (DefaultTableModel) tblContas.getModel();
        listData();
    }

    private void listData() {
        try {
            rs = con.prepareStatement("SELECT title FROM acc_plan").executeQuery();
            while (rs.next()) {
                cboPla.addItem(rs.getString("title"));
            }
            cboPla.addItem("<html><b>MOSTRAR TODAS</b></html>");
            cboPla.setSelectedItem("<html><b>MOSTRAR TODAS</b></html>");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar plano de contas.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1021\nDetalhes do erro:\n" + ex);
        }

        cboVenc.addItem(mounths(mesAtual - 4));
        cboVenc.addItem(mounths(mesAtual - 3));
        cboVenc.addItem(mounths(mesAtual - 2));
        cboVenc.addItem(mounths(mesAtual - 1));
        cboVenc.addItem(mounths(mesAtual));
        cboVenc.addItem("-------------------------------------");
        cboVenc.addItem("<html><b>ESTE ANO</b></html>");
        cboVenc.addItem("<html><b>MOSTRAR TODAS</b></html>");

        cboVenc.setSelectedIndex(4);

        for (int i = 0; i < 4; i++) {
            mesFiltro[i] = mesAtual - 1 - i;
            anoFiltro[i] = anoAtual;
            if (mesFiltro[i] > 12) {
                mesFiltro[i] = mesFiltro[i] - 12;
                anoFiltro[i] = anoAtual + 1;
            }
        }
        System.out.println(Arrays.toString(mesFiltro));

        filterBy("venc");

    }

    private String mounths(int m) {
        String mounth = "";
        int prevYear = cal.get(Calendar.YEAR) - 1;
        switch (m) {
            case 1:
                mounth = "Janeiro";
                break;
            case 2:
                mounth = "Fevereiro";
                break;
            case 3:
                mounth = "Março";
                break;
            case 4:
                mounth = "Abril";
                break;
            case 5:
                mounth = "Maio";
                break;
            case 6:
                mounth = "Junho";
                break;
            case 7:
                mounth = "Julho";
                break;
            case 8:
                mounth = "Agosto";
                break;
            case 9:
                mounth = "Setembro";
                break;
            case 10:
                mounth = "Outubro";
                break;
            case 11:
                mounth = "Novembro";
                break;
            case 12:
                mounth = "Dezembro";
                break;
            case 0:
                mounth = "Dezembro/" + prevYear;
                break;
            case -1:
                mounth = "Novembro/" + prevYear;
                break;
            case -2:
                mounth = "Outubro/" + prevYear;
                break;
            case -3:
                mounth = "Setembro/" + prevYear;
                break;
        }
        return mounth;
    }

    /**
     *
     * <u>Códigos:</u><br>
     * <b>101</b> pra todos desse ano;<br>
     * <b>102</b> para mostrar tudo;<br>
     *
     */
    private void filterBy(int mes, int ano) {
        dtm.setNumRows(0);
        Date d_venc;
        Date d_paga;
        try {
            switch (mes) {
                case 101: //Mostrar todas desse ano
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE data_pag BETWEEN '" + anoAtual + "-01-01' AND '" + anoAtual + "-12-31' AND valor_pago IS NOT null").executeQuery();
                    //System.out.println("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc BETWEEN '" + anoAtual + "-01-01' AND '" + anoAtual + "12-31' AND valor_pago IS NOT null");
                    break;
                case 102: //Mostrar tudo
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE valor_pago IS NOT null").executeQuery();
                    break;
                default:
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE data_pag BETWEEN '" + ano + "-" + mes + "-01' AND '" + ano + "-" + mes + "-31' AND valor_pago IS NOT null").executeQuery();
                    System.out.println("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE data_pag BETWEEN '" + ano + "-" + mes + "-01' AND '" + ano + "-" + mes + "-31' AND valor_pago IS NOT null");
                    break;
            }
            //POPULA TABELA
            while (rs.next()) {
                dtm.addRow(new Object[]{
                    rs.getString("A.id"),
                    rs.getString("A.titulo"),
                    rs.getString("A.parcela") + "/" + rs.getString("A.tot_parcela"),
                    rs.getString("A.valor_pago"),
                    rs.getString("B.title"),
                    vData.format(sqlData.parse(rs.getString("A.venc"))),
                    vData.format(sqlData.parse(rs.getString("A.data_pag")))
                });
            }
            //Colorindo data de pagamento
            for (int i = 0; i < dtm.getRowCount(); i++) {
                d_venc = vData.parse(tblContas.getValueAt(i, 5).toString());
                d_paga = vData.parse(tblContas.getValueAt(i, 6).toString());
                if (d_paga.after(d_venc)) {
                    //(VERMELHO)
                    tblContas.setValueAt("<html><b><font color='#ff3333'>" + tblContas.getValueAt(i, 6), i, 6);
                } else {
                    //(VERDE)
                    tblContas.setValueAt("<html><b><font color='#00cc00'>" + tblContas.getValueAt(i, 6), i, 6);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar contas à pagar.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1022\nDetalhes do erro:\n" + e);
            this.dispose();
        }

    }

    private void filterBy(String field) {
        DefaultTableModel tm;
        tm = (DefaultTableModel) tblContas.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tm);
        tblContas.setRowSorter(tr);
        String key;
        switch (field) {
            case "cod":
                key = txtCod.getText();
                tr.setRowFilter(RowFilter.regexFilter("(?i)" + key, 0)); //(?i) é pra tornar case-insesitivy
                break;

            case "titulo":
                key = txtTit.getText();
                tr.setRowFilter(RowFilter.regexFilter("(?i)" + key, 1));
                break;

            case "venc":
                key = Integer.toString(cboVenc.getSelectedIndex());
                switch (key) {
                    case "0":
                        filterBy(mesFiltro[3], anoFiltro[3]);
                        break;
                    case "1":
                        filterBy(mesFiltro[2], anoFiltro[2]);
                        break;
                    case "2":
                        filterBy(mesFiltro[1], anoFiltro[1]);
                        break;
                    case "3":
                        filterBy(mesFiltro[0], anoFiltro[0]);
                        break;
                    case "4":
                        filterBy(mesAtual, anoAtual);
                        break;
                    //Pulei um por causa do "JSeparator" improvisado
                    case "6":
                        filterBy(101, 101);
                        break;
                    case "7":
                        filterBy(102, 102);
                        break;
                }
                break;
            case "plan":
                key = cboPla.getSelectedItem().toString();
                System.out.println(key);
                if (key.equals("<html><b>MOSTRAR TODAS</b></html>")) {
                    tr.setRowFilter(null);
                } else {
                    tr.setRowFilter(RowFilter.regexFilter("(?i)" + key, 2));
                }

                break;
        }
        if (tblContas.getRowCount() > 0) {
            tblContas.setRowSelectionInterval(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtCod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTit = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cboVenc = new javax.swing.JComboBox<>();
        cboPla = new javax.swing.JComboBox<>();
        btnLim = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblContas = new javax.swing.JTable();
        btnDet = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        btnPaids1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DESPESAS PAGAS - Agility®");
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N
        jPanel2.setOpaque(false);

        txtCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodActionPerformed(evt);
            }
        });
        txtCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Código:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Título:");

        txtTit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTitActionPerformed(evt);
            }
        });
        txtTit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTitKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pago em:");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Plano de Contas:");

        cboVenc.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboVencPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        cboPla.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cboPlaPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        btnLim.setText("Limpar Filtros");
        btnLim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(cboVenc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cboPla, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLim)
                        .addGap(0, 402, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboVenc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboPla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLim))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1210, -1));

        tblContas.setAutoCreateRowSorter(true);
        tblContas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título", "Parcela", "Valor Pago", "Plano de Contas", "Vencimento", "Pago em:"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblContas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblContas.getTableHeader().setReorderingAllowed(false);
        tblContas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblContasMouseClicked(evt);
            }
        });
        tblContas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblContasKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblContas);
        if (tblContas.getColumnModel().getColumnCount() > 0) {
            tblContas.getColumnModel().getColumn(0).setPreferredWidth(85);
            tblContas.getColumnModel().getColumn(1).setPreferredWidth(422);
            tblContas.getColumnModel().getColumn(2).setPreferredWidth(136);
            tblContas.getColumnModel().getColumn(3).setPreferredWidth(109);
            tblContas.getColumnModel().getColumn(4).setPreferredWidth(199);
            tblContas.getColumnModel().getColumn(5).setPreferredWidth(128);
            tblContas.getColumnModel().getColumn(6).setPreferredWidth(128);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1213, 250));

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });
        getContentPane().add(btnDet, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 340, 200, -1));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Cadastrar Conta");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        getContentPane().add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 173, -1));

        btnPaids1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money.png"))); // NOI18N
        btnPaids1.setText("Pagamento");
        btnPaids1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaids1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnPaids1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 340, 173, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 1240, 400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
        filterBy("cod");
    }//GEN-LAST:event_txtCodKeyReleased

    private void txtTitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTitKeyReleased
        filterBy("titulo");
    }//GEN-LAST:event_txtTitKeyReleased

    private void cboVencPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboVencPopupMenuWillBecomeInvisible
        if (cboVenc.getSelectedIndex() > -1) {
            filterBy("venc");
            if (cboPla.getSelectedIndex() > -1) {
                filterBy("plan");
            }
        }
    }//GEN-LAST:event_cboVencPopupMenuWillBecomeInvisible

    private void cboPlaPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboPlaPopupMenuWillBecomeInvisible
        if (cboPla.getSelectedIndex() > -1) {
            filterBy("plan");
        }
    }//GEN-LAST:event_cboPlaPopupMenuWillBecomeInvisible

    private void btnLimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimActionPerformed
        txtCod.setText("");
        txtTit.setText("");
        cboVenc.setSelectedItem("<html><b>MOSTRAR TODAS</b></html>");
        cboPla.setSelectedItem("<html><b>MOSTRAR TODAS</b></html>");
        filterBy("venc");
    }//GEN-LAST:event_btnLimActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (tblContas.getSelectedRow() < 0 || tblContas.getValueAt(tblContas.getSelectedRow(), 0) == null) {
            JOptionPane.showMessageDialog(null, "Antes de clicar neste botão, selecione uma conta na tabela!");
            txtCod.grabFocus();
        } else {
            DespesaDetalhes DD = new DespesaDetalhes(this, tblContas.getValueAt(tblContas.getSelectedRow(), 0).toString(), "payed");
            DD.setVisible(true);
            if (DD.modified) {
                listData();
            }

        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        DespesaForm DF = new DespesaForm(home);
        DF.setVisible(true);
        if (DF.modified) {
            listData();
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnPaids1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaids1ActionPerformed
        if (tblContas.getSelectedRow() < 0 || tblContas.getValueAt(tblContas.getSelectedRow(), 0) == null) {
            JOptionPane.showMessageDialog(home, "Por favor, selecione uma conta na tabela.");
        } else {
            DespesaPagar dp = new DespesaPagar(home, tblContas.getValueAt(tblContas.getSelectedRow(), 0).toString());
            dp.setVisible(true);
            if (dp.modified > 0) {
                listData();
            }
        }
    }//GEN-LAST:event_btnPaids1ActionPerformed

    private void tblContasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblContasKeyReleased
    }//GEN-LAST:event_tblContasKeyReleased

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtTitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtTitActionPerformed

    private void tblContasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContasMouseClicked
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblContasMouseClicked

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
            java.util.logging.Logger.getLogger(DespesasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DespesasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DespesasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DespesasPagas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DespesasPagas dialog = new DespesasPagas(new JFrame());
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
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPaids1;
    private javax.swing.JComboBox<String> cboPla;
    private javax.swing.JComboBox<String> cboVenc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblContas;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtTit;
    // End of variables declaration//GEN-END:variables

}
