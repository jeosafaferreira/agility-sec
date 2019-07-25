/* Desenvolvedor: Jeosafá Ferreira
 * Data de início: 17/04/2017
 *
 *
 */
package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilityRel;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 *
 * @author root
 */
public class Home extends javax.swing.JFrame {

    WizardNovoAluno nAluno;
    AlunoMatricula alumat;
    Connection con;
    ResultSet rs;

    public Home() {
        //Teste
        initComponents();
        con = ModuloConexao.conector();
        try {
            //BUSCA ANO LETIVO
            rs = con.prepareStatement("SELECT academic_year FROM school_data").executeQuery();
            rs.next();
            lblAlert2.setVisible(false);
            lblAlert3.setVisible(false);
            lblAlert4.setVisible(false);
        } catch (SQLException e) {
            AgilitySec.showError(this, "#1048", e);
        }
    }

    private void showExpiredAcc() {
        Date hoje = Calendar.getInstance().getTime();
        Calendar prox = Calendar.getInstance();
        prox.set(Calendar.DAY_OF_MONTH, prox.get(Calendar.DAY_OF_MONTH) + 3);
        String msg = "";

        DateFormat sqlData = new SimpleDateFormat("yyyy/MM/dd");
        try {
            //Verifica conta vencida
            rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc < '" + sqlData.format(hoje) + "' AND valor_pago IS null ORDER BY venc ASC").executeQuery();
            if (rs.next()) {
                rs.last();
                lblAlert1.setText("Atenção!");
                lblAlert2.setText("<html><font color=\"red\"><li><b> Você possui " + rs.getRow() + " conta(s) vencidas.");
                lblAlert2.setVisible(true);

                msg += "Você possui " + rs.getRow() + " conta(s) vencidas.\n";
            }

            //Verifica conta próxima a se vencer
            rs = con.prepareStatement("SELECT * FROM finances A LEFT JOIN acc_plan B ON A.acc_plan_id = B.id WHERE venc >= '" + sqlData.format(hoje) + "' AND venc <='" + sqlData.format(prox.getTime()) + "' AND valor_pago IS null ORDER BY venc ASC").executeQuery();
            System.out.println(rs.getStatement());
            if (rs.next()) {
                rs.last();
                lblAlert1.setText("Atenção!");
                lblAlert3.setText("<html><font color=\"red\"><li><b> Você possui " + rs.getRow() + " conta(s) próxima(s) a vencer.");
                lblAlert3.setVisible(true);

                msg += "Você possui " + rs.getRow() + " conta(s) próxima(s) a vencer.";
            }

        } catch (Exception e) {
            AgilitySec.showError(this, "#1157", e);
        }
        if (!msg.equals("")) {
            JOptionPane.showMessageDialog(this, "<html><b>Atenção!\n" + msg, "Atenção", JOptionPane.ERROR_MESSAGE);
            new DespesasAbertas(this).setVisible(true);
        }
    }

    private void exit() {
        int exit = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair do sistema?", "Atenção!", JOptionPane.YES_NO_OPTION);
        if (exit == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void waiting() {
        JOptionPane.showMessageDialog(null, "Esta funcionalidade ainda está em desenvolvimento!\nEm caso de dúvidas, contate o suporte Agility®.", "Atenção!", INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblAlert1 = new javax.swing.JLabel();
        lblAlert3 = new javax.swing.JLabel();
        lblAlert2 = new javax.swing.JLabel();
        lblAlert4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsr = new javax.swing.JLabel();
        txtEscola = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnRelAlu = new javax.swing.JButton();
        btnAlu = new javax.swing.JButton();
        btnResp = new javax.swing.JButton();
        btnRelResp = new javax.swing.JButton();
        btnRelFun = new javax.swing.JButton();
        btnOco = new javax.swing.JButton();
        btnRelRespInad = new javax.swing.JButton();
        btnRelContasPagar = new javax.swing.JButton();
        btnQuaHor = new javax.swing.JButton();
        btnRecebPag = new javax.swing.JButton();
        btnTurmas = new javax.swing.JButton();
        btnCursos = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        finMenBaixadas1 = new javax.swing.JMenuItem();
        finMenAberto1 = new javax.swing.JMenuItem();
        finMenVencidas1 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        aluDados = new javax.swing.JMenuItem();
        aluLista = new javax.swing.JMenuItem();
        aluBaixoRend = new javax.swing.JMenuItem();
        aluFaltosos = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        respDados = new javax.swing.JMenuItem();
        respLista = new javax.swing.JMenuItem();
        respInad = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        ocoByAlu = new javax.swing.JMenuItem();
        ocoByTurma = new javax.swing.JMenuItem();
        ocoByCurso = new javax.swing.JMenuItem();
        ocoTodas = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        curDados = new javax.swing.JMenuItem();
        curMatriz = new javax.swing.JMenuItem();
        curLista = new javax.swing.JMenuItem();
        jMenuItem54 = new javax.swing.JMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        turDados = new javax.swing.JMenuItem();
        turLista = new javax.swing.JMenuItem();
        turQuaHor = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        disDados = new javax.swing.JMenuItem();
        disDisxProf = new javax.swing.JMenuItem();
        jMenu18 = new javax.swing.JMenu();
        funDados = new javax.swing.JMenuItem();
        funAtivos = new javax.swing.JMenuItem();
        funInativos = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        livDados = new javax.swing.JMenuItem();
        livLista = new javax.swing.JMenuItem();
        jMenu20 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem64 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        jMenu21 = new javax.swing.JMenu();
        jMenuItem71 = new javax.swing.JMenuItem();
        jMenu17 = new javax.swing.JMenu();
        finMenBaixadas = new javax.swing.JMenuItem();
        finMenAberto = new javax.swing.JMenuItem();
        finMenVencidas = new javax.swing.JMenuItem();
        finConPagar = new javax.swing.JMenuItem();
        finConBaixadas = new javax.swing.JMenuItem();
        finConVencidas = new javax.swing.JMenuItem();
        finFluxoMensal = new javax.swing.JMenuItem();
        finFluxoAnual = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        profLista = new javax.swing.JMenuItem();
        proProfxDisc = new javax.swing.JMenuItem();
        jMenu19 = new javax.swing.JMenu();
        outListaDeNotas = new javax.swing.JMenuItem();
        outListaDeChamada = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem79 = new javax.swing.JMenuItem();
        jMenuItem78 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem76 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Agility® - Gestão Escolar");
        setFocusCycleRoot(false);
        setName("FrmPrinc"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "QUADRO DE AVISOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel6.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Agility® 2018");

        lblAlert1.setForeground(new java.awt.Color(255, 255, 255));
        lblAlert1.setText("Nenhuma informação a ser exibida.");

        lblAlert3.setForeground(new java.awt.Color(255, 255, 255));
        lblAlert3.setText("lblAlert3");

        lblAlert2.setForeground(new java.awt.Color(255, 255, 255));
        lblAlert2.setText("lblAlert2");

        lblAlert4.setForeground(new java.awt.Color(255, 255, 255));
        lblAlert4.setText("lblAlert4");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Usuário:");

        txtUsr.setForeground(new java.awt.Color(255, 255, 255));
        txtUsr.setText("txtUsr");

        txtEscola.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        txtEscola.setForeground(new java.awt.Color(255, 255, 255));
        txtEscola.setText("txtEscola");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblAlert1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 984, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEscola, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtUsr, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblAlert3)
                            .addComponent(lblAlert4)
                            .addComponent(lblAlert2))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAlert1)
                    .addComponent(jLabel2))
                .addGap(8, 8, 8)
                .addComponent(lblAlert2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAlert3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAlert4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                .addComponent(txtEscola)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUsr))
                .addContainerGap())
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1350, 380));

        jPanel1.setOpaque(false);

        btnRelAlu.setBackground(new java.awt.Color(212, 212, 212));
        btnRelAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/2.png"))); // NOI18N
        btnRelAlu.setText("Dados do Aluno");
        btnRelAlu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelAlu.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRelAlu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelAluActionPerformed(evt);
            }
        });

        btnAlu.setBackground(new java.awt.Color(212, 212, 212));
        btnAlu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/student_icon.png"))); // NOI18N
        btnAlu.setText("Alunos");
        btnAlu.setSelected(true);
        btnAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAluActionPerformed(evt);
            }
        });

        btnResp.setBackground(new java.awt.Color(212, 212, 212));
        btnResp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/responsibles_icon.png"))); // NOI18N
        btnResp.setText("Responsáveis");
        btnResp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRespActionPerformed(evt);
            }
        });

        btnRelResp.setBackground(new java.awt.Color(212, 212, 212));
        btnRelResp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/2.png"))); // NOI18N
        btnRelResp.setText("Dados do Responsável");
        btnRelResp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelResp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRelResp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelResp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelRespActionPerformed(evt);
            }
        });

        btnRelFun.setBackground(new java.awt.Color(212, 212, 212));
        btnRelFun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/2.png"))); // NOI18N
        btnRelFun.setText("<html>Dados do Funcionário");
        btnRelFun.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelFun.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRelFun.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelFun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelFunActionPerformed(evt);
            }
        });

        btnOco.setBackground(new java.awt.Color(212, 212, 212));
        btnOco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/occurrences.png"))); // NOI18N
        btnOco.setText("Ocorrências");
        btnOco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOcoActionPerformed(evt);
            }
        });

        btnRelRespInad.setBackground(new java.awt.Color(212, 212, 212));
        btnRelRespInad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/2.png"))); // NOI18N
        btnRelRespInad.setText("Responsáveis Inadimplentes");
        btnRelRespInad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelRespInad.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRelRespInad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelRespInad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelRespInadActionPerformed(evt);
            }
        });

        btnRelContasPagar.setBackground(new java.awt.Color(212, 212, 212));
        btnRelContasPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/2.png"))); // NOI18N
        btnRelContasPagar.setText("Contas à Pagar");
        btnRelContasPagar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRelContasPagar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnRelContasPagar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRelContasPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelContasPagarActionPerformed(evt);
            }
        });

        btnQuaHor.setBackground(new java.awt.Color(212, 212, 212));
        btnQuaHor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/to_pay.png"))); // NOI18N
        btnQuaHor.setText("Notas e Faltas");
        btnQuaHor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuaHorActionPerformed(evt);
            }
        });

        btnRecebPag.setBackground(new java.awt.Color(212, 212, 212));
        btnRecebPag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/money - 48x48.png"))); // NOI18N
        btnRecebPag.setText("Receber Pagamento");
        btnRecebPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecebPagActionPerformed(evt);
            }
        });

        btnTurmas.setBackground(new java.awt.Color(212, 212, 212));
        btnTurmas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/classes_icon.png"))); // NOI18N
        btnTurmas.setText("Turmas");
        btnTurmas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTurmasActionPerformed(evt);
            }
        });

        btnCursos.setBackground(new java.awt.Color(212, 212, 212));
        btnCursos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_14_2058813.png"))); // NOI18N
        btnCursos.setText("Cursos");
        btnCursos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCursosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRelAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTurmas, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnResp, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOco, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRelResp, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnRelFun, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnRelRespInad, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnRelContasPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(btnRecebPag, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnOco, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnResp, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnTurmas, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnQuaHor, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRelResp, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRelFun, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRelRespInad, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRelContasPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRecebPag, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(btnRelAlu, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 1350, 270));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/back3.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 670));

        jMenuBar1.setPreferredSize(new java.awt.Dimension(597, 30));

        jMenu1.setText("Cadastros");

        jMenuItem20.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem20.setText("Novo Aluno");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem20);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem4.setText("Efetuar Matrícula");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Alunos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem23.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem23.setText("Responsáveis");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem23);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem5.setText("Cursos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText("Turmas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem6.setText("Disciplinas");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem21.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem21.setText("Livros");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem21);

        jMenuItem22.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem22.setText("Salas de Aula");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem22);

        jMenuItem24.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem24.setText("Ocorrências");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem24);

        jMenuItem36.setText("Quadro de Horários");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem36);

        jMenuItem8.setText("Notas e Faltas");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Financeiro");

        jMenuItem26.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem26.setText("Contas em Aberto");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem26);

        jMenuItem27.setText("Contas Pagas");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuItem25.setText("Plano de Contas");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem25);

        jMenuBar1.add(jMenu5);

        jMenu7.setText("Mensalidades");

        finMenBaixadas1.setText("Mensalidades Baixadas");
        finMenBaixadas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenBaixadas1ActionPerformed(evt);
            }
        });
        jMenu7.add(finMenBaixadas1);

        finMenAberto1.setText("Mensalidades em Aberto");
        finMenAberto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenAberto1ActionPerformed(evt);
            }
        });
        jMenu7.add(finMenAberto1);

        finMenVencidas1.setText("Mensalidades Vencidas");
        finMenVencidas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenVencidas1ActionPerformed(evt);
            }
        });
        jMenu7.add(finMenVencidas1);

        jMenuItem33.setText("Receber Pagamento");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem33);

        jMenuBar1.add(jMenu7);

        jMenu4.setText("Relatórios");

        jMenu8.setText("Alunos");

        aluDados.setText("Dados do Aluno");
        aluDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aluDadosActionPerformed(evt);
            }
        });
        jMenu8.add(aluDados);

        aluLista.setText("Lista de Alunos Matriculados");
        aluLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aluListaActionPerformed(evt);
            }
        });
        jMenu8.add(aluLista);

        aluBaixoRend.setText("Alunos com Baixo Rendimento Escolar");
        aluBaixoRend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aluBaixoRendActionPerformed(evt);
            }
        });
        jMenu8.add(aluBaixoRend);

        aluFaltosos.setText("Alunos Faltosos");
        aluFaltosos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aluFaltososActionPerformed(evt);
            }
        });
        jMenu8.add(aluFaltosos);

        jMenu4.add(jMenu8);

        jMenu9.setText("Responsáveis");

        respDados.setText("Dados do Responsável");
        respDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                respDadosActionPerformed(evt);
            }
        });
        jMenu9.add(respDados);

        respLista.setText("Lista de Responsáveis");
        respLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                respListaActionPerformed(evt);
            }
        });
        jMenu9.add(respLista);

        respInad.setText("Responsáveis Inadimplentes");
        respInad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                respInadActionPerformed(evt);
            }
        });
        jMenu9.add(respInad);

        jMenu4.add(jMenu9);

        jMenu14.setText("Ocorrências");

        ocoByAlu.setText("Ocorrências por Aluno");
        ocoByAlu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocoByAluActionPerformed(evt);
            }
        });
        jMenu14.add(ocoByAlu);

        ocoByTurma.setText("Ocorrências por Turma");
        ocoByTurma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocoByTurmaActionPerformed(evt);
            }
        });
        jMenu14.add(ocoByTurma);

        ocoByCurso.setText("Ocorrências por Curso");
        ocoByCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocoByCursoActionPerformed(evt);
            }
        });
        jMenu14.add(ocoByCurso);

        ocoTodas.setText("Todas as Ocorrências");
        ocoTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocoTodasActionPerformed(evt);
            }
        });
        jMenu14.add(ocoTodas);

        jMenu4.add(jMenu14);

        jMenu10.setText("Cursos");

        curDados.setText("Dados do Curso");
        curDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curDadosActionPerformed(evt);
            }
        });
        jMenu10.add(curDados);

        curMatriz.setText("Matriz Curricular");
        curMatriz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curMatrizActionPerformed(evt);
            }
        });
        jMenu10.add(curMatriz);

        curLista.setText("Lista de Cursos Disponíveis");
        curLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                curListaActionPerformed(evt);
            }
        });
        jMenu10.add(curLista);

        jMenuItem54.setText("Rendimento de determinado curso*");
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem54);

        jMenuItem55.setText("Rendimentos de todos os cursos*");
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem55);

        jMenu4.add(jMenu10);

        jMenu11.setText("Turmas");

        turDados.setText("Dados da Turma");
        turDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turDadosActionPerformed(evt);
            }
        });
        jMenu11.add(turDados);

        turLista.setText("Lista de Turmas*");
        turLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turListaActionPerformed(evt);
            }
        });
        jMenu11.add(turLista);

        turQuaHor.setText("Quadro de Horários");
        turQuaHor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                turQuaHorActionPerformed(evt);
            }
        });
        jMenu11.add(turQuaHor);

        jMenuItem47.setText("Rendimento*");
        jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem47ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem47);

        jMenu4.add(jMenu11);

        jMenu12.setText("Disciplinas");

        disDados.setText("Dados da Disciplina");
        disDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disDadosActionPerformed(evt);
            }
        });
        jMenu12.add(disDados);

        disDisxProf.setText("Disciplina(s) X Professore(s)*");
        jMenu12.add(disDisxProf);

        jMenu4.add(jMenu12);

        jMenu18.setText("Funcionários");

        funDados.setText("Dados do Funcionário");
        funDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                funDadosActionPerformed(evt);
            }
        });
        jMenu18.add(funDados);

        funAtivos.setText("Funcionários Ativos");
        funAtivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                funAtivosActionPerformed(evt);
            }
        });
        jMenu18.add(funAtivos);

        funInativos.setText("Funcionários Inativos");
        funInativos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                funInativosActionPerformed(evt);
            }
        });
        jMenu18.add(funInativos);

        jMenu4.add(jMenu18);

        jMenu13.setText("Livros");

        livDados.setText("Dados do Livro");
        livDados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                livDadosActionPerformed(evt);
            }
        });
        jMenu13.add(livDados);

        livLista.setText("Lista de Livros");
        livLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                livListaActionPerformed(evt);
            }
        });
        jMenu13.add(livLista);

        jMenu4.add(jMenu13);

        jMenu20.setText("Boletins");

        jMenuItem14.setText("Boletim Bimestral*");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem14);

        jMenuItem64.setText("Boletim Final");
        jMenuItem64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem64ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem64);

        jMenu4.add(jMenu20);

        jMenu16.setText("Financeiro");

        jMenu21.setText("Boletos");

        jMenuItem71.setText("Imprimir Boleto(s)*");
        jMenu21.add(jMenuItem71);

        jMenu16.add(jMenu21);

        jMenu17.setText("Mensalidades");

        finMenBaixadas.setText("Mensalidades Baixadas");
        finMenBaixadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenBaixadasActionPerformed(evt);
            }
        });
        jMenu17.add(finMenBaixadas);

        finMenAberto.setText("Mensalidades em Aberto");
        finMenAberto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenAbertoActionPerformed(evt);
            }
        });
        jMenu17.add(finMenAberto);

        finMenVencidas.setText("Mensalidades Vencidas");
        finMenVencidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finMenVencidasActionPerformed(evt);
            }
        });
        jMenu17.add(finMenVencidas);

        jMenu16.add(jMenu17);

        finConPagar.setText("Contas em Aberto");
        finConPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finConPagarActionPerformed(evt);
            }
        });
        jMenu16.add(finConPagar);

        finConBaixadas.setText("Contas Baixadas");
        finConBaixadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finConBaixadasActionPerformed(evt);
            }
        });
        jMenu16.add(finConBaixadas);

        finConVencidas.setText("Contas Vencidas");
        finConVencidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finConVencidasActionPerformed(evt);
            }
        });
        jMenu16.add(finConVencidas);

        finFluxoMensal.setText("Fluxo de Caixa Mensal");
        finFluxoMensal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finFluxoMensalActionPerformed(evt);
            }
        });
        jMenu16.add(finFluxoMensal);

        finFluxoAnual.setText("Fluxo de Caixa Anual");
        finFluxoAnual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finFluxoAnualActionPerformed(evt);
            }
        });
        jMenu16.add(finFluxoAnual);

        jMenu4.add(jMenu16);

        jMenu15.setText("Professores");

        jMenuItem9.setText("Quadro de Horários");
        jMenu15.add(jMenuItem9);

        profLista.setText("Lista de Professores");
        profLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profListaActionPerformed(evt);
            }
        });
        jMenu15.add(profLista);

        proProfxDisc.setText("Professore(s) x Disciplina(s)*");
        jMenu15.add(proProfxDisc);

        jMenu4.add(jMenu15);

        jMenu19.setText("Outros");

        outListaDeNotas.setText("Lista de Notas*");
        outListaDeNotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outListaDeNotasActionPerformed(evt);
            }
        });
        jMenu19.add(outListaDeNotas);

        outListaDeChamada.setText("Lista de Chamada*");
        outListaDeChamada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outListaDeChamadaActionPerformed(evt);
            }
        });
        jMenu19.add(outListaDeChamada);

        jMenuItem15.setText("Histórico Escolar*");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem15);

        jMenuItem79.setText("Quadro de Vagas*");
        jMenuItem79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem79ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem79);

        jMenuItem78.setText("Ficha de Pré-Matrícula*");
        jMenuItem78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem78ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem78);

        jMenuItem12.setText("Aniversariantes do Mês*");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem12);

        jMenuItem76.setText("Declaração de Matrícula*");
        jMenuItem76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem76ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem76);

        jMenu4.add(jMenu19);

        jMenuBar1.add(jMenu4);

        jMenu6.setText("Recursos Humanos");

        jMenuItem30.setText("Cargos");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem30);

        jMenuItem29.setText("Funcionários");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem29);

        jMenuItem34.setText("Professores X Disciplinas");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem34);

        jMenuItem35.setText("Disciplinas X Professores");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem35);

        jMenuBar1.add(jMenu6);

        jMenu2.setText("Outros");

        jMenuItem31.setText("Quadro de Vagas");
        jMenu2.add(jMenuItem31);

        jMenuItem28.setText("Atualizar Ano Letivo");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem28);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem7.setText("Sair");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(1376, 727));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new Alunos(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        exit();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exit();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new Turmas(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new Cursos(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new Disciplinas(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void aluListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aluListaActionPerformed
        AgilityRel.aluLista();
    }//GEN-LAST:event_aluListaActionPerformed

    private void respDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_respDadosActionPerformed
        AgilityRel.respDados();
    }//GEN-LAST:event_respDadosActionPerformed

    private void turDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turDadosActionPerformed
        AgilityRel.turDados();
    }//GEN-LAST:event_turDadosActionPerformed

    private void curListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curListaActionPerformed
        AgilityRel.curLista();
    }//GEN-LAST:event_curListaActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        waiting();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void finMenAbertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenAbertoActionPerformed
        AgilityRel.finMenAberto();
    }//GEN-LAST:event_finMenAbertoActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed

    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        waiting();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void ocoByTurmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocoByTurmaActionPerformed
        AgilityRel.ocoByTurma();
    }//GEN-LAST:event_ocoByTurmaActionPerformed

    private void ocoByCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocoByCursoActionPerformed
        AgilityRel.ocoByCurso();
    }//GEN-LAST:event_ocoByCursoActionPerformed

    private void ocoByAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocoByAluActionPerformed
        AgilityRel.ocoByAlu();
    }//GEN-LAST:event_ocoByAluActionPerformed

    private void funAtivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_funAtivosActionPerformed
        AgilityRel.funAtivos();
    }//GEN-LAST:event_funAtivosActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        showExpiredAcc();
    }//GEN-LAST:event_formComponentShown

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        AlunoMatricula aluMat = new AlunoMatricula(this);
        aluMat.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        new Salas(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        new Livros(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        new Ocorrencias(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        new DespesasAbertas(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        new PlanoContas(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        new DespesasPagas(this).setVisible(true);

    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        new Responsaveis(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "<html>Deseja realmente atualizar todo o sistema para <b>2018</b>?", "Atenção!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Sua solicitação foi enviada para o suporte técnico!\nAguarde a confirmação da atualização.\n"
                    + "(Implementar uma função pra me enviar um aviso, pra eu analisar se o sistema está pronto pra atualização, e outra função que retorne pro diretor dizendo que está tudo OK.");
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        Cargos F = new Cargos(this);
        F.setVisible(true);
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        new Funcionarios(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        ProfessoresXDisciplinas pd = new ProfessoresXDisciplinas(this);
        pd.setVisible(true);

    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        DisciplinasXProfessores dp = new DisciplinasXProfessores(this);
        dp.setVisible(true);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        new QuadroHorarioSelec(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        new MensalidadePagarCod(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void respListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_respListaActionPerformed
        AgilityRel.respLista();
    }//GEN-LAST:event_respListaActionPerformed

    private void respInadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_respInadActionPerformed
        AgilityRel.respInad();
    }//GEN-LAST:event_respInadActionPerformed

    private void curDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curDadosActionPerformed
        AgilityRel.curDados();
    }//GEN-LAST:event_curDadosActionPerformed

    private void curMatrizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_curMatrizActionPerformed
        AgilityRel.curMatriz();
    }//GEN-LAST:event_curMatrizActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void ocoTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocoTodasActionPerformed
        AgilityRel.ocoTodas();
    }//GEN-LAST:event_ocoTodasActionPerformed

    private void finMenVencidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenVencidasActionPerformed
        AgilityRel.finMenVencidas();
    }//GEN-LAST:event_finMenVencidasActionPerformed

    private void funInativosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_funInativosActionPerformed
        AgilityRel.funInativos();
    }//GEN-LAST:event_funInativosActionPerformed

    private void jMenuItem76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem76ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem76ActionPerformed

    private void outListaDeChamadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outListaDeChamadaActionPerformed
        AgilityRel.outListaDeChamada();
    }//GEN-LAST:event_outListaDeChamadaActionPerformed

    private void jMenuItem78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem78ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem78ActionPerformed

    private void jMenuItem79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem79ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem79ActionPerformed

    private void finMenBaixadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenBaixadasActionPerformed
        AgilityRel.finMenBaixadas();
    }//GEN-LAST:event_finMenBaixadasActionPerformed

    private void aluDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aluDadosActionPerformed
        AgilityRel.aluDados();
    }//GEN-LAST:event_aluDadosActionPerformed

    private void turQuaHorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turQuaHorActionPerformed
        AgilityRel.openReport(AgilityRel.turQuaHor(""));
    }//GEN-LAST:event_turQuaHorActionPerformed

    private void turListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_turListaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_turListaActionPerformed

    private void funDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_funDadosActionPerformed
        AgilityRel.funDados();
    }//GEN-LAST:event_funDadosActionPerformed

    private void disDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disDadosActionPerformed
        AgilityRel.disDados();
    }//GEN-LAST:event_disDadosActionPerformed

    private void jMenuItem64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem64ActionPerformed
        AgilityRel.bolFin();
    }//GEN-LAST:event_jMenuItem64ActionPerformed

    private void finConPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finConPagarActionPerformed
        AgilityRel.finConPagar();
    }//GEN-LAST:event_finConPagarActionPerformed

    private void finConBaixadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finConBaixadasActionPerformed
        AgilityRel.finConBaixadas();
    }//GEN-LAST:event_finConBaixadasActionPerformed

    private void finConVencidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finConVencidasActionPerformed
        AgilityRel.finConVencidas();
    }//GEN-LAST:event_finConVencidasActionPerformed

    private void finFluxoMensalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finFluxoMensalActionPerformed
        AgilityRel.finFluxoMensal();
    }//GEN-LAST:event_finFluxoMensalActionPerformed

    private void finFluxoAnualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finFluxoAnualActionPerformed
        AgilityRel.finFluxoAnual();
    }//GEN-LAST:event_finFluxoAnualActionPerformed

    private void profListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profListaActionPerformed
        AgilityRel.profLista();
    }//GEN-LAST:event_profListaActionPerformed

    private void livDadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_livDadosActionPerformed
        AgilityRel.livDados();
    }//GEN-LAST:event_livDadosActionPerformed

    private void livListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_livListaActionPerformed
        AgilityRel.livLista();
    }//GEN-LAST:event_livListaActionPerformed

    private void btnRespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRespActionPerformed
        new Responsaveis(this).setVisible(true);
    }//GEN-LAST:event_btnRespActionPerformed

    private void btnRelRespActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelRespActionPerformed
        AgilityRel.respDados();
    }//GEN-LAST:event_btnRelRespActionPerformed

    private void btnAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAluActionPerformed
        new Alunos(this).setVisible(true);
    }//GEN-LAST:event_btnAluActionPerformed

    private void btnTurmasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTurmasActionPerformed
        new Turmas(this).setVisible(true);
    }//GEN-LAST:event_btnTurmasActionPerformed

    private void btnOcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOcoActionPerformed
        new Ocorrencias(this).setVisible(true);
    }//GEN-LAST:event_btnOcoActionPerformed

    private void btnQuaHorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuaHorActionPerformed
        new NotaSelec(this).setVisible(true);
    }//GEN-LAST:event_btnQuaHorActionPerformed

    private void btnRelAluActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelAluActionPerformed
        AgilityRel.aluDados();
    }//GEN-LAST:event_btnRelAluActionPerformed

    private void btnRelFunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelFunActionPerformed
        AgilityRel.funDados();
    }//GEN-LAST:event_btnRelFunActionPerformed

    private void btnRelRespInadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelRespInadActionPerformed
        AgilityRel.respInad();
    }//GEN-LAST:event_btnRelRespInadActionPerformed

    private void btnRelContasPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelContasPagarActionPerformed
        AgilityRel.finConPagar();
    }//GEN-LAST:event_btnRelContasPagarActionPerformed

    private void btnRecebPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecebPagActionPerformed
        new MensalidadePagarCod(this).setVisible(true);
    }//GEN-LAST:event_btnRecebPagActionPerformed

    private void btnCursosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCursosActionPerformed
        new Cursos(this).setVisible(true);
    }//GEN-LAST:event_btnCursosActionPerformed

    private void finMenVencidas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenVencidas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_finMenVencidas1ActionPerformed

    private void finMenBaixadas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenBaixadas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_finMenBaixadas1ActionPerformed

    private void finMenAberto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finMenAberto1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_finMenAberto1ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new NotaSelec(this).setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void outListaDeNotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outListaDeNotasActionPerformed
        AgilityRel.outListaDeNotas();
    }//GEN-LAST:event_outListaDeNotasActionPerformed

    private void aluBaixoRendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aluBaixoRendActionPerformed
        AgilityRel.aluBaixoRend();
    }//GEN-LAST:event_aluBaixoRendActionPerformed

    private void aluFaltososActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aluFaltososActionPerformed
        AgilityRel.aluFaltosos();
    }//GEN-LAST:event_aluFaltososActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        nAluno = new WizardNovoAluno(this);
        nAluno.setVisible(true);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aluBaixoRend;
    private javax.swing.JMenuItem aluDados;
    private javax.swing.JMenuItem aluFaltosos;
    private javax.swing.JMenuItem aluLista;
    private javax.swing.JButton btnAlu;
    private javax.swing.JButton btnCursos;
    private javax.swing.JButton btnOco;
    private javax.swing.JButton btnQuaHor;
    private javax.swing.JButton btnRecebPag;
    private javax.swing.JButton btnRelAlu;
    private javax.swing.JButton btnRelContasPagar;
    private javax.swing.JButton btnRelFun;
    private javax.swing.JButton btnRelResp;
    private javax.swing.JButton btnRelRespInad;
    private javax.swing.JButton btnResp;
    private javax.swing.JButton btnTurmas;
    private javax.swing.JMenuItem curDados;
    private javax.swing.JMenuItem curLista;
    private javax.swing.JMenuItem curMatriz;
    private javax.swing.JMenuItem disDados;
    private javax.swing.JMenuItem disDisxProf;
    private javax.swing.JMenuItem finConBaixadas;
    private javax.swing.JMenuItem finConPagar;
    private javax.swing.JMenuItem finConVencidas;
    private javax.swing.JMenuItem finFluxoAnual;
    private javax.swing.JMenuItem finFluxoMensal;
    private javax.swing.JMenuItem finMenAberto;
    private javax.swing.JMenuItem finMenAberto1;
    private javax.swing.JMenuItem finMenBaixadas;
    private javax.swing.JMenuItem finMenBaixadas1;
    private javax.swing.JMenuItem finMenVencidas;
    private javax.swing.JMenuItem finMenVencidas1;
    private javax.swing.JMenuItem funAtivos;
    private javax.swing.JMenuItem funDados;
    private javax.swing.JMenuItem funInativos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu19;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem64;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem71;
    private javax.swing.JMenuItem jMenuItem76;
    private javax.swing.JMenuItem jMenuItem78;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblAlert1;
    private javax.swing.JLabel lblAlert2;
    private javax.swing.JLabel lblAlert3;
    private javax.swing.JLabel lblAlert4;
    private javax.swing.JMenuItem livDados;
    private javax.swing.JMenuItem livLista;
    private javax.swing.JMenuItem ocoByAlu;
    private javax.swing.JMenuItem ocoByCurso;
    private javax.swing.JMenuItem ocoByTurma;
    private javax.swing.JMenuItem ocoTodas;
    private javax.swing.JMenuItem outListaDeChamada;
    private javax.swing.JMenuItem outListaDeNotas;
    private javax.swing.JMenuItem proProfxDisc;
    private javax.swing.JMenuItem profLista;
    private javax.swing.JMenuItem respDados;
    private javax.swing.JMenuItem respInad;
    private javax.swing.JMenuItem respLista;
    private javax.swing.JMenuItem turDados;
    private javax.swing.JMenuItem turLista;
    private javax.swing.JMenuItem turQuaHor;
    public javax.swing.JLabel txtEscola;
    public javax.swing.JLabel txtUsr;
    // End of variables declaration//GEN-END:variables
}
