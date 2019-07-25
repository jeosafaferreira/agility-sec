package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.joda.time.LocalDate;

/**
 *
 * @author jferreira
 */
public class DespesasAbertas extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Home home;
    DefaultTableModel dtmAbe, dtmVen;
    DateFormat sqlData = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat vData = new SimpleDateFormat("dd/MM/yyyy");
    Calendar cal = new GregorianCalendar();
    Date hoje = new Date();
    int mesAtual = cal.get(Calendar.MONTH) + 1; //NÃO ALTERAR! O +1 é porque nessa classe, janeiro = 0
    int anoAtual = cal.get(Calendar.YEAR);
    int[] mesFiltro = new int[4];
    int[] anoFiltro = new int[4];
    boolean s = false;

    public DespesasAbertas(JFrame parent) {
        super(parent);
        initComponents();

        con = ModuloConexao.conector();
        dtmAbe = (DefaultTableModel) tblConAbe.getModel();
        dtmVen = (DefaultTableModel) tblConVenc.getModel();

        if (!contasVencidas()) {
            pnlContasVencidas.setVisible(false);
            this.setSize(850, 380);
        }

        fillFilters();
    }

    private boolean contasVencidas() {
        dtmVen.setNumRows(0);
        boolean r = false;
        //BUSCA CONTAS VENCIDAS
        try {

            rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc < '" + sqlData.format(hoje) + "' AND valor_pago IS null ORDER BY venc ASC").executeQuery();
            while (rs.next()) {
                dtmVen.addRow(new String[]{
                    "" + rs.getString("A.id"),
                    "<html><font color='red'><b>" + rs.getString("A.titulo") + "</font></html>",
                    "<html><font color='red'><b>" + rs.getString("A.parcela") + "/" + rs.getString("tot_parcela") + "</font></html>",
                    "<html><font color='red'><b>R$ " + rs.getString("A.valor").replace(".", ",") + "</font></html>",
                    "<html><font color='red'><b>" + vData.format(sqlData.parse(rs.getString("A.venc"))) + "</font></html>",
                    "<html><font color='red'><b>" + rs.getString("B.title") + "</font></html>",
                    "<html><font color='red'><b>" + rs.getString("A.obs") + "</font></html>"
                });
            }
            if (dtmVen.getRowCount() > 0) {
                r = true;
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#1151", e);
        }
        return r;
    }

    private void fillFilters() {
        //POPULA FILTROS
        cboVenc.addItem(mounths(mesAtual));
        cboVenc.addItem(mounths(mesAtual + 1));
        cboVenc.addItem(mounths(mesAtual + 2));
        cboVenc.addItem(mounths(mesAtual + 3));
        cboVenc.addItem(mounths(mesAtual + 4));
        cboVenc.addItem("-------------------------------------");
        cboVenc.addItem("<html><b>ESTE ANO</b></html>");
        cboVenc.addItem("<html><b>MOSTRAR TODAS</b></html>");
        cboVenc.setSelectedItem("<html><b>MOSTRAR TODAS</b></html>");
        for (int i = 0; i < 4; i++) {
            mesFiltro[i] = mesAtual + 1 + i;
            anoFiltro[i] = anoAtual;
            if (mesFiltro[i] > 12) {
                mesFiltro[i] = mesFiltro[i] - 12;
                anoFiltro[i] = anoAtual + 1;
            }
        }

        try {
            rs = con.prepareStatement("SELECT title FROM acc_plan").executeQuery();
            while (rs.next()) {
                cboPla.addItem(rs.getString("title"));
            }
            cboPla.addItem("<html><b>MOSTRAR TODAS</b></html>");
            cboPla.setSelectedItem("<html><b>MOSTRAR TODAS</b></html>");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao listar plano de contas.\nContate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br\nCódigo do Erro: #1013\nDetalhes do erro:\n" + ex);
        }

        filterBy("venc");
    }

    private String mounths(int m) {
        String mounth = "";
        int nextYear = cal.get(Calendar.YEAR) + 1;
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
            case 13:
                mounth = "Janeiro/" + nextYear;
                break;
            case 14:
                mounth = "Fevereiro/" + nextYear;
                break;
            case 15:
                mounth = "Março/" + nextYear;
                break;
            case 16:
                mounth = "Abril/" + nextYear;
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
        System.out.println(hoje);
        dtmAbe.setNumRows(0);
        try {
            switch (mes) {
                case 101: //Mostrar todas desse ano
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc BETWEEN '" + anoAtual + "-01-01' AND '" + anoAtual + "-12-31' AND valor_pago IS null ORDER BY venc ASC").executeQuery();
                    break;
                case 102: //Mostrar tudo
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE valor_pago IS null AND venc >= '" + sqlData.format(hoje) + "' ORDER BY venc ASC").executeQuery();
                    break;
                default:
                    rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc BETWEEN '" + ano + "-" + mes + "-01' AND '" + ano + "-" + mes + "-31' AND valor_pago IS null ORDER BY venc ASC").executeQuery();
                    break;
            }
            while (rs.next()) {
                LocalDate venc = LocalDate.fromDateFields(rs.getDate("A.venc"));
                LocalDate hoje = LocalDate.now();
                dtmAbe.addRow(new Object[]{
                    rs.getString("A.id"),
                    rs.getString("A.titulo"),
                    rs.getString("A.parcela") + "/" + rs.getString("A.tot_parcela"),
                    "R$ " + rs.getString("A.valor").replace(".", ","),
                    hoje.isEqual(venc) ? "<html><font color='red'><b>" + venc.toString("dd/MM/YYYY") : venc.toString("dd/MM/YYYY"),
                    rs.getString("B.title"),
                    rs.getString("A.obs")});
            }
        } catch (Exception e) {
            AgilitySec.showError(this, "#1004", e);
            this.dispose();
        }

    }

    private void filterBy(String field) {
        DefaultTableModel tm;
        tm = (DefaultTableModel) tblConAbe.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tm);
        tblConAbe.setRowSorter(tr);
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
                        filterBy(mesAtual, anoAtual);
                        break;
                    case "1":
                        filterBy(mesFiltro[0], anoFiltro[0]);
                        break;
                    case "2":
                        filterBy(mesFiltro[1], anoFiltro[1]);
                        break;
                    case "3":
                        filterBy(mesFiltro[2], anoFiltro[2]);
                        break;
                    case "4":
                        filterBy(mesFiltro[3], anoFiltro[3]);
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
                if (key.equals("<html><b>MOSTRAR TODAS</b></html>")) {
                    tr.setRowFilter(null);
                } else {
                    tr.setRowFilter(RowFilter.regexFilter("(?i)" + key, 2));
                }

                break;
        }
        if (tblConAbe.getRowCount() > 0) {
            tblConAbe.setRowSelectionInterval(0, 0);
        }
    }

    private String getCodSelected() {
        String cod = null;
        if (tblConAbe.getSelectedRow() > -1) {
            cod = (String) tblConAbe.getValueAt(tblConAbe.getSelectedRow(), 0);
        } else if (tblConVenc.getSelectedRow() > -1) {
            cod = (String) tblConVenc.getValueAt(tblConVenc.getSelectedRow(), 0);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma conta.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            txtCod.grabFocus();
        }

        return cod;
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
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblConAbe = new javax.swing.JTable();
        pnlContasVencidas = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConVenc = new javax.swing.JTable();
        btnNew = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();
        btnDet = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DESPESAS ABERTAS - Agility®");
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
        jLabel1.setText("Vencimento:");

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
                        .addGap(0, 12, Short.MAX_VALUE))))
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

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 820, -1));

        jPanel1.setOpaque(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setOpaque(false);

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("CONTAS EM ABERTO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblConAbe.setAutoCreateRowSorter(true);
        tblConAbe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título", "Parcela", "Valor", "Vencimento", "Plano de Contas", "Observações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
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
        tblConAbe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblConAbe.getTableHeader().setReorderingAllowed(false);
        tblConAbe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConAbeMouseClicked(evt);
            }
        });
        tblConAbe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblConAbeKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblConAbe);
        if (tblConAbe.getColumnModel().getColumnCount() > 0) {
            tblConAbe.getColumnModel().getColumn(0).setPreferredWidth(76);
            tblConAbe.getColumnModel().getColumn(1).setPreferredWidth(245);
            tblConAbe.getColumnModel().getColumn(2).setPreferredWidth(62);
            tblConAbe.getColumnModel().getColumn(3).setPreferredWidth(94);
            tblConAbe.getColumnModel().getColumn(4).setPreferredWidth(85);
            tblConAbe.getColumnModel().getColumn(5).setPreferredWidth(124);
            tblConAbe.getColumnModel().getColumn(6).setPreferredWidth(124);
        }

        pnlContasVencidas.setOpaque(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel4.setOpaque(false);

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("CONTAS VENCIDAS");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblConVenc.setAutoCreateRowSorter(true);
        tblConVenc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Título", "Parcela", "Valor", "Vencimento", "Plano de Contas", "Observações"
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
        tblConVenc.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblConVenc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblConVencMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblConVenc);
        if (tblConVenc.getColumnModel().getColumnCount() > 0) {
            tblConVenc.getColumnModel().getColumn(0).setPreferredWidth(76);
            tblConVenc.getColumnModel().getColumn(1).setPreferredWidth(245);
            tblConVenc.getColumnModel().getColumn(2).setPreferredWidth(62);
            tblConVenc.getColumnModel().getColumn(3).setPreferredWidth(94);
            tblConVenc.getColumnModel().getColumn(4).setPreferredWidth(85);
            tblConVenc.getColumnModel().getColumn(5).setPreferredWidth(124);
            tblConVenc.getColumnModel().getColumn(6).setPreferredWidth(124);
        }

        javax.swing.GroupLayout pnlContasVencidasLayout = new javax.swing.GroupLayout(pnlContasVencidas);
        pnlContasVencidas.setLayout(pnlContasVencidasLayout);
        pnlContasVencidasLayout.setHorizontalGroup(
            pnlContasVencidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContasVencidasLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlContasVencidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(0, 0, 0))
        );
        pnlContasVencidasLayout.setVerticalGroup(
            pnlContasVencidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContasVencidasLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1494236453_plus.png"))); // NOI18N
        btnNew.setText("Cadastrar Conta");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money.png"))); // NOI18N
        btnPay.setText("Baixar Conta");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnDet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/1493192895_eye-open.png"))); // NOI18N
        btnDet.setText("Visualizar detalhes");
        btnDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_icon-136-document-edit_314724.png"))); // NOI18N
        btnEdit.setText("Editar Conta");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87)
                        .addComponent(btnPay, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(btnDet, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
                        .addComponent(pnlContasVencidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlContasVencidas, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNew)
                    .addComponent(btnEdit)
                    .addComponent(btnPay)
                    .addComponent(btnDet))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 820, 400));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 850, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodKeyReleased
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

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        DespesaForm DF = new DespesaForm(home);
        DF.setVisible(true);
        if (DF.modified) {
            contasVencidas();
            if (cboVenc.getSelectedIndex() > -1) {
                filterBy("venc");
                if (cboPla.getSelectedIndex() > -1) {
                    filterBy("plan");
                }
            }
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        if (getCodSelected() != null) {
            DespesaPagar dp = new DespesaPagar(home, getCodSelected());
            dp.setVisible(true);
            if (dp.modified > 0) {
                contasVencidas();
                if (cboVenc.getSelectedIndex() > -1) {
                    filterBy("venc");
                    if (cboPla.getSelectedIndex() > -1) {
                        filterBy("plan");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void btnDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetActionPerformed
        if (getCodSelected() != null) {
            DespesaDetalhes DD = new DespesaDetalhes(this, getCodSelected(), "notPayed");
            DD.setVisible(true);
            if (DD.modified) {
                contasVencidas();
                if (cboVenc.getSelectedIndex() > -1) {
                    filterBy("venc");
                    if (cboPla.getSelectedIndex() > -1) {
                        filterBy("plan");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnDetActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (getCodSelected() != null) {
            DespesaForm DF = new DespesaForm(home, getCodSelected());
            DF.setVisible(true);
            if (DF.modified) {
                contasVencidas();
                if (cboVenc.getSelectedIndex() > -1) {
                    filterBy("venc");
                    if (cboPla.getSelectedIndex() > -1) {
                        filterBy("plan");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void tblConVencMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConVencMouseClicked
        tblConAbe.clearSelection();
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblConVencMouseClicked

    private void tblConAbeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblConAbeMouseClicked
        tblConVenc.clearSelection();
        if (evt.getClickCount() == 2) {
            btnDetActionPerformed(null);
        }
    }//GEN-LAST:event_tblConAbeMouseClicked

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtTitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTitActionPerformed
        btnDetActionPerformed(null);
    }//GEN-LAST:event_txtTitActionPerformed

    private void tblConAbeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblConAbeKeyReleased
        AgilitySec.getColumnsWidth(tblConAbe);
    }//GEN-LAST:event_tblConAbeKeyReleased

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
            java.util.logging.Logger.getLogger(DespesasAbertas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DespesasAbertas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DespesasAbertas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DespesasAbertas.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DespesasAbertas dialog = new DespesasAbertas(new JFrame());
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
    private javax.swing.JLabel background;
    private javax.swing.JButton btnDet;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnLim;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPay;
    private javax.swing.JComboBox<String> cboPla;
    private javax.swing.JComboBox<String> cboVenc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlContasVencidas;
    private javax.swing.JTable tblConAbe;
    private javax.swing.JTable tblConVenc;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtTit;
    // End of variables declaration//GEN-END:variables

}
