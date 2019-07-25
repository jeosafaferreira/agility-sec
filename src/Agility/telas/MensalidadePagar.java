package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class MensalidadePagar extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String tCod, tRefer, sCod, sName, couCod, couName, claCod, claName;
    BigDecimal tValor = new BigDecimal("0.00");
    BigDecimal tValorTot = new BigDecimal("0.00");
    BigDecimal tMulta = new BigDecimal("0.00");
    BigDecimal tJuros = new BigDecimal("0.00");
    BigDecimal refJurosP = new BigDecimal("0.00033333");
    BigDecimal refMultaP = new BigDecimal("0.02");
    Date tVenc;
    Date now = new Date();
    DateFormat dateBr = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateSql = new SimpleDateFormat("yyyy-MM-dd");

    public MensalidadePagar(JDialog parent, String cod) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        this.tCod = cod;
        this.setTitle("PAGAR MENSALIDADE (#" + cod + ")");
        getRootPane().setDefaultButton(btnOk);
        getData();
        btnOk.grabFocus();
    }

    private void getData() {
        long daysDelay = 0;

        try {
            rs = con.prepareStatement("SELECT T.id, T.valor, T.venc, T.refer, S.cod,S.name,COU.cod,COU.name,CLA.cod,CLA.name FROM tuitions T LEFT JOIN students S ON T.student_cod=S.cod LEFT JOIN courses COU ON S.course_id=COU.id LEFT JOIN classes CLA ON S.class_id=CLA.id WHERE T.id = '" + this.tCod + "'").executeQuery();
            rs.next();
            this.sCod = rs.getString("S.cod");
            this.sName = rs.getString("S.name");
            this.couCod = rs.getString("COU.cod");
            this.couName = rs.getString("COU.name");
            this.claCod = rs.getString("CLA.cod");
            this.claName = rs.getString("CLA.name");
            this.tVenc = rs.getDate("T.venc");
            this.tRefer = rs.getString("T.refer");
            this.tValor = rs.getBigDecimal("T.valor");
            this.tMulta = refMultaP.multiply(tValor).setScale(2, RoundingMode.HALF_DOWN);

            //DADOS DO ALUNO
            txtAluCod.setText(sCod);
            txtAluNome.setText(sName);
            txtAluTur.setText(claCod + " - " + claName);
            txtAluCur.setText(couCod + " - " + couName);

            //MENSALIDADE - VALOR - VENC - MES
            txtTuiVen.setText(dateBr.format(tVenc));
            txtTuiMes.setText(tRefer);
            txtTuiValorBase.setText("R$ " + tValor.toString().replace(".", ","));

            //MENSALIDADE - DIAS DE ATRASO
            dateBr.setLenient(false);
            daysDelay = now.getTime() - tVenc.getTime();
            daysDelay = TimeUnit.DAYS.convert(daysDelay, TimeUnit.MILLISECONDS);

            if (daysDelay < 1) {
                txtTuiAtr.setForeground(new java.awt.Color(0, 204, 0));
                txtTuiAtr.setText("0");
            } else {
                txtTuiAtr.setForeground(new java.awt.Color(255, 0, 0));
                txtTuiAtr.setText(Long.toString(daysDelay));
            }

            //MENSALIDADE - JUROS, MULTA E CORREÇÃO DE VALOR
            tJuros = refJurosP.multiply(tValor).multiply(new BigDecimal(daysDelay)).setScale(2, RoundingMode.CEILING);

            if (daysDelay > 1) {
                txtTuiMul.setText("R$ " + tMulta.toString().replace(".", ","));
                txtTuiJur.setText("R$ " + tJuros.toString().replace(".", ","));
                tValorTot = tValor.add(tJuros).add(tMulta).setScale(2, RoundingMode.CEILING);
            }else{
                //NESSE CASO, NÃO HÁ JUROS.
                txtTuiMul.setText("R$ 0,00");
                txtTuiJur.setText("R$ 0,00");
                tValorTot = tValor;
            }

            //MENSALIDADE - VLR A RECEBER
            txtTuiTotal.setText("R$ " + tValorTot.toString().replace(".", ","));

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1095", ex);
        }
    }

    private void showRef() {
        String msg = "<html><b>Valor do Documento: \n"
                + "R$ " + this.tValor.toString().replace(".", ",") + "\n\n"
                + "<html><b>Multa por atraso: \n"
                + "2%. \n"
                + "(R$ " + this.tMulta.toString().replace(".", ",") + ")\n\n"
                + "<html><b>Juros moratórios: \n"
                + "1% ao mês.\n"
                + "(R$ " + this.refJurosP.multiply(tValor).setScale(4, RoundingMode.HALF_DOWN).toString().replace(".", ",") + " x Dias de atraso)\n\n";

        JOptionPane.showMessageDialog(this, msg, "Valores de Referência", JOptionPane.PLAIN_MESSAGE);
    }

    private void save() {
        try {
            pst = con.prepareStatement("UPDATE tuitions SET "
                    + "valor_pago='" + tValorTot + "'"
                    + ", data_baixa='" + dateSql.format(now) + "'"
                    + ", multa='" + tMulta + "'"
                    + ", juros='" + tJuros + "'"
                    + ", desco=0.00"
                    + ", form_of_pay_id='4'" //DINHEIRO
                    + ", modified_by='" + Login.emp_id + "'"
                    + "WHERE id='" + this.tCod + "'"
            );
            System.out.println(pst);
            if (pst.executeUpdate() > 0) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1096", ex);
        }

    }

    private void calcTroco() {
        String input = JOptionPane.showInputDialog(this, "Valor recebido:");
        BigDecimal vlrRec, troco;

        if (input != null) {
            try {
                Double.parseDouble(input.replace(",", "."));
                vlrRec = new BigDecimal(input.replace(",", "."));
                troco = vlrRec.subtract(tValorTot);

                if (vlrRec.compareTo(tValorTot) < 0) {
                    JOptionPane.showMessageDialog(this, "O valor recebido é insuficiente!");
                    calcTroco();
                } else {
                    JOptionPane.showMessageDialog(this, "<html><b>Troco: </b>R$ " + troco.toString().replace(".", ","));
                }

            } catch (Exception e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(this, "Valor inválido. Tente novamente!");
                calcTroco();
            }
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
        jLabel5 = new javax.swing.JLabel();
        txtAluTur = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAluCur = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTuiVen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTuiAtr = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTuiJur = new javax.swing.JTextField();
        txtTuiMul = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtTuiTotal = new javax.swing.JTextField();
        btnCalcT = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTuiMes = new javax.swing.JTextField();
        txtTuiValorBase = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnRef = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PAGAR MENSALIDADE (#cod)");
        setAlwaysOnTop(true);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAluno.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DO ALUNO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        pnlAluno.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("Matrícula:");

        txtAluCod.setEditable(false);
        txtAluCod.setBackground(new java.awt.Color(204, 204, 204));
        txtAluCod.setText("2017003");
        txtAluCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluCodKeyReleased(evt);
            }
        });

        txtAluNome.setEditable(false);
        txtAluNome.setBackground(new java.awt.Color(204, 204, 204));
        txtAluNome.setText("Francisco das Chagas Braga Barbosa Júnior");
        txtAluNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAluNomeKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("Nome:");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Turma:");

        txtAluTur.setEditable(false);
        txtAluTur.setBackground(new java.awt.Color(204, 204, 204));
        txtAluTur.setText("EM1A - Turma A");
        txtAluTur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAluTurActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel9.setText("Curso:");

        txtAluCur.setEditable(false);
        txtAluCur.setBackground(new java.awt.Color(204, 204, 204));
        txtAluCur.setText("EM1 - 1º  ANO DO ENSINO MEDIO");

        javax.swing.GroupLayout pnlAlunoLayout = new javax.swing.GroupLayout(pnlAluno);
        pnlAluno.setLayout(pnlAlunoLayout);
        pnlAlunoLayout.setHorizontalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAlunoLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtAluCod, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(6, 6, 6))
                    .addGroup(pnlAlunoLayout.createSequentialGroup()
                        .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtAluTur, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(txtAluNome, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(txtAluCur))
                .addGap(6, 6, 6))
        );
        pnlAlunoLayout.setVerticalGroup(
            pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlunoLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(9, 9, 9)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAluCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAlunoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAluTur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAluCur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        getContentPane().add(pnlAluno, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "DADOS DO PAGAMENTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("Vencimento:");

        txtTuiVen.setEditable(false);
        txtTuiVen.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiVen.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtTuiVen.setText("10/12/2018");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("Dias em atraso:");

        txtTuiAtr.setEditable(false);
        txtTuiAtr.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiAtr.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtTuiAtr.setForeground(new java.awt.Color(255, 0, 0));
        txtTuiAtr.setText("12");

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel6.setText("Juros:");

        txtTuiJur.setEditable(false);
        txtTuiJur.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiJur.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtTuiJur.setText("R$ 1,45");
        txtTuiJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuiJurActionPerformed(evt);
            }
        });

        txtTuiMul.setEditable(false);
        txtTuiMul.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiMul.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtTuiMul.setText("R$ 2,00");
        txtTuiMul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuiMulActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Multa:");

        jLabel11.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel11.setText("VALOR À RECEBER:");

        txtTuiTotal.setEditable(false);
        txtTuiTotal.setBackground(new java.awt.Color(255, 255, 204));
        txtTuiTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtTuiTotal.setText("R$103,45");

        btnCalcT.setText("Calcular Troco");
        btnCalcT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcTActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Mês de Referência:");

        txtTuiMes.setEditable(false);
        txtTuiMes.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiMes.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtTuiMes.setText("JANEIRO");
        txtTuiMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuiMesActionPerformed(evt);
            }
        });

        txtTuiValorBase.setEditable(false);
        txtTuiValorBase.setBackground(new java.awt.Color(204, 204, 204));
        txtTuiValorBase.setFont(new java.awt.Font("DejaVu Sans", 0, 14)); // NOI18N
        txtTuiValorBase.setText("R$ 99,99");

        jLabel10.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel10.setText("Valor Base:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTuiTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(txtTuiValorBase, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCalcT))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtTuiJur, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(txtTuiMul, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtTuiVen, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTuiMes, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtTuiAtr, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTuiVen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTuiAtr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTuiMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTuiMul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTuiJur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTuiValorBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTuiTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalcT))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 174, 490, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 395, 145, -1));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Confirmar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 395, 145, -1));

        btnRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money.png"))); // NOI18N
        btnRef.setText("Valores de Ref.");
        btnRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefActionPerformed(evt);
            }
        });
        getContentPane().add(btnRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 395, 188, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -5, 510, 450));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtAluCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluCodKeyReleased

    }//GEN-LAST:event_txtAluCodKeyReleased

    private void txtAluNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAluNomeKeyReleased

    }//GEN-LAST:event_txtAluNomeKeyReleased

    private void txtAluTurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAluTurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAluTurActionPerformed

    private void txtTuiJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuiJurActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTuiJurActionPerformed

    private void txtTuiMulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuiMulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTuiMulActionPerformed

    private void btnRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefActionPerformed
        showRef();
    }//GEN-LAST:event_btnRefActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja cancelar este pagamento?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnCalcTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcTActionPerformed
        calcTroco();

    }//GEN-LAST:event_btnCalcTActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        save();
    }//GEN-LAST:event_btnOkActionPerformed

    private void txtTuiMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuiMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTuiMesActionPerformed

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
            java.util.logging.Logger.getLogger(MensalidadePagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MensalidadePagar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MensalidadePagar dialog = new MensalidadePagar(new JDialog(), "");
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
    private javax.swing.JButton btnCalcT;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRef;
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
    private javax.swing.JPanel pnlAluno;
    private javax.swing.JTextField txtAluCod;
    private javax.swing.JTextField txtAluCur;
    private javax.swing.JTextField txtAluNome;
    private javax.swing.JTextField txtAluTur;
    private javax.swing.JTextField txtTuiAtr;
    private javax.swing.JTextField txtTuiJur;
    private javax.swing.JTextField txtTuiMes;
    private javax.swing.JTextField txtTuiMul;
    private javax.swing.JTextField txtTuiTotal;
    private javax.swing.JTextField txtTuiValorBase;
    private javax.swing.JTextField txtTuiVen;
    // End of variables declaration//GEN-END:variables

}
