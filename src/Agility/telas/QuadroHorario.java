package Agility.telas;

import Agility.dal.ModuloConexao;
import Agility.api.AgilitySec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jferreira
 */
public class QuadroHorario extends javax.swing.JDialog {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String course_id, class_id;
    QuadroHorarioEdit qForm;
    String[] allIdProf = new String[15];
    String[] allIdDisc = new String[15];
    String[] allCodDisc = new String[15];
    String[] allNameProf = new String[15];
    String[] allNameDisc = new String[15];

    /* 
    MAPA allIdDisc e allIdProf:
    [00] [03] [06] [09] [12]
    [01] [04] [07] [10] [13]
    [02] [05] [08] [11] [14]
     */
    public QuadroHorario(JFrame parent, String course_id, String class_id) {
        super(parent);
        initComponents();
        con = ModuloConexao.conector();
        this.class_id = class_id;
        this.course_id = course_id;

        listData();
    }

    private void listData() {
        //HEADER
        try {
            //CURSO
            rs = con.prepareStatement("SELECT name FROM courses WHERE id=" + this.course_id).executeQuery();
            rs.next();
            lblCur.setText(rs.getString("name"));

            //TURMA, TURNO 
            rs = con.prepareStatement("SELECT cod, name, turno FROM classes WHERE id=" + this.class_id).executeQuery();
            rs.next();
            lblTurma.setText(rs.getString("cod") + " - " + rs.getString("name"));
            lblTurno.setText(rs.getString("turno"));

            //HORÁRIOS
            if (rs.getString("turno").equals("Manhã")) {
                lblT1.setText("07:30h ~ 08:30h");
                lblT2.setText("08:30h ~ 09:30h");
                lblIntervalo.setText("09:30h ~ 10:00h");
                lblT3.setText("10:00h ~ 11:00h");

            } else if (rs.getString("turno").equals("Tarde")) {
                lblT1.setText("13:30h ~ 14:30h");
                lblT2.setText("14:30h ~ 15:30h");
                lblIntervalo.setText("15:30h ~ 16:00h");
                lblT3.setText("16:00h ~ 17:00h");
            }

        } catch (SQLException e) {
            AgilitySec.showError(this, "#1092", e);
        }

        try {

            rs = con.prepareStatement("SELECT S.*,E.name,D.name,D.cod FROM schedules S LEFT JOIN employees E ON E.id=S.employee_id LEFT JOIN disciplines D ON D.id=S.disc_id WHERE S.class_id=" + this.class_id + " ORDER BY S.weekday ASC, S.time ASC").executeQuery();

            System.out.println(rs.getStatement());

            int i = 0;
            //Trocando o valor null por vazio, pra não aparecer "null - null" na tela
            Arrays.fill(allCodDisc, " ");
            Arrays.fill(allNameDisc, " ");
            Arrays.fill(allNameProf, " ");
            while (rs.next()) {

                allIdProf[i] = rs.getString("S.employee_id");
                allIdDisc[i] = rs.getString("S.disc_id");

                allNameDisc[i] = rs.getString("D.name");

                allNameProf[i] = rs.getString("E.name");

                allCodDisc[i] = rs.getString("D.cod");
                i++;
            }
            lblW2T1Disc.setText(allCodDisc[0] + " - " + allNameDisc[0]);
            lblW2T2Disc.setText(allCodDisc[1] + " - " + allNameDisc[1]);
            lblW2T3Disc.setText(allCodDisc[2] + " - " + allNameDisc[2]);
            lblW3T1Disc.setText(allCodDisc[3] + " - " + allNameDisc[3]);
            lblW3T2Disc.setText(allCodDisc[4] + " - " + allNameDisc[4]);
            lblW3T3Disc.setText(allCodDisc[5] + " - " + allNameDisc[5]);
            lblW4T1Disc.setText(allCodDisc[6] + " - " + allNameDisc[6]);
            lblW4T2Disc.setText(allCodDisc[7] + " - " + allNameDisc[7]);
            lblW4T3Disc.setText(allCodDisc[8] + " - " + allNameDisc[8]);
            lblW5T1Disc.setText(allCodDisc[9] + " - " + allNameDisc[9]);
            lblW5T2Disc.setText(allCodDisc[10] + " - " + allNameDisc[10]);
            lblW5T3Disc.setText(allCodDisc[11] + " - " + allNameDisc[11]);
            lblW6T1Disc.setText(allCodDisc[12] + " - " + allNameDisc[12]);
            lblW6T2Disc.setText(allCodDisc[13] + " - " + allNameDisc[13]);
            lblW6T3Disc.setText(allCodDisc[14] + " - " + allNameDisc[14]);

            lblW2T1Prof.setText(allNameProf[0]);
            lblW2T2Prof.setText(allNameProf[1]);
            lblW2T3Prof.setText(allNameProf[2]);
            lblW3T1Prof.setText(allNameProf[3]);
            lblW3T2Prof.setText(allNameProf[4]);
            lblW3T3Prof.setText(allNameProf[5]);
            lblW4T1Prof.setText(allNameProf[6]);
            lblW4T2Prof.setText(allNameProf[7]);
            lblW4T3Prof.setText(allNameProf[8]);
            lblW5T1Prof.setText(allNameProf[9]);
            lblW5T2Prof.setText(allNameProf[10]);
            lblW5T3Prof.setText(allNameProf[11]);
            lblW6T1Prof.setText(allNameProf[12]);
            lblW6T2Prof.setText(allNameProf[13]);
            lblW6T3Prof.setText(allNameProf[14]);

        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1089", ex);
        }
    }

    private void save() {
        String created = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
        boolean update = false;
        try {
            //LIMPA OS DADOS DESSA TURMA
            con.prepareStatement("DELETE FROM schedules WHERE class_id=" + this.class_id).executeUpdate();

            //SÃO 15 POSIÇÕES, ENTÃO:
            int i = 0;
            for (int w = 2; w < 7; w++) {
                for (int t = 1; t < 4; t++) {
                    pst = con.prepareStatement("INSERT INTO schedules SET "
                            + "weekday=" + w
                            + ", time=" + t
                            + ", class_id=" + this.class_id
                            + ", disc_id=" + allIdDisc[i]
                            + ", employee_id=" + allIdProf[i]
                            + ", created='" + created + "'"
                            + ", created_by=" + Login.emp_id
                    );
                    i++;
                    System.out.println(pst);
                    update = pst.executeUpdate() > 0;
                }
            }
            if (update) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            }
        } catch (SQLException ex) {
            AgilitySec.showError(this, "#1090", ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        lblW2T1Disc = new javax.swing.JLabel();
        lblW2T1Prof = new javax.swing.JLabel();
        btnW2T1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lblW3T1Prof = new javax.swing.JLabel();
        lblW3T1Disc = new javax.swing.JLabel();
        btnW3T1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        lblW4T1Disc = new javax.swing.JLabel();
        lblW4T1Prof = new javax.swing.JLabel();
        btnW4T1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lblW5T1Disc = new javax.swing.JLabel();
        lblW5T1Prof = new javax.swing.JLabel();
        btnW5T1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        lblW6T1Disc = new javax.swing.JLabel();
        lblW6T1Prof = new javax.swing.JLabel();
        btnW6T1 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        lblW3T2Disc = new javax.swing.JLabel();
        lblW3T2Prof = new javax.swing.JLabel();
        btnW3T2 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        lblW2T2Disc = new javax.swing.JLabel();
        lblW2T2Prof = new javax.swing.JLabel();
        btnW2T2 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        lblW4T2Disc = new javax.swing.JLabel();
        lblW4T2Prof = new javax.swing.JLabel();
        btnW4T2 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        lblW5T2Disc = new javax.swing.JLabel();
        lblW5T2Prof = new javax.swing.JLabel();
        btnW5T2 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        lblW6T2Disc = new javax.swing.JLabel();
        lblW6T2Prof = new javax.swing.JLabel();
        btnW6T2 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        lblW5T3Disc = new javax.swing.JLabel();
        lblW5T3Prof = new javax.swing.JLabel();
        btnW5T3 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        lblW3T3Disc = new javax.swing.JLabel();
        lblW3T3Prof = new javax.swing.JLabel();
        btnW3T3 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        lblW4T3Disc = new javax.swing.JLabel();
        lblW4T3Prof = new javax.swing.JLabel();
        btnW4T3 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        lblW6T3Disc = new javax.swing.JLabel();
        lblW6T3Prof = new javax.swing.JLabel();
        btnW6T3 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        lblW2T3Disc = new javax.swing.JLabel();
        lblW2T3Prof = new javax.swing.JLabel();
        btnW2T3 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblT1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblT3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblT2 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lblIntervalo = new javax.swing.JLabel();
        btnCan = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblCur = new javax.swing.JLabel();
        lblTurno = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTurma = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        btnChange = new javax.swing.JButton();
        BackGround = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quadro de Horários");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("SEGUNDA-FEIRA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1)
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 199, 195, -1));

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW2T1Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T1Disc.setText("lblW2T1Disc");

        lblW2T1Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T1Prof.setText("lblW2T1Prof");

        btnW2T1.setText("Editar");
        btnW2T1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW2T1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(lblW2T1Prof, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(lblW2T1Disc, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnW2T1)
                .addGap(64, 64, 64))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblW2T1Disc)
                .addGap(6, 6, 6)
                .addComponent(lblW2T1Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW2T1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 234, 195, -1));

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setText("TERÇA-FEIRA");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel6)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addContainerGap())
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 199, 200, -1));

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW3T1Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T1Prof.setText("lblW3T1Prof");

        lblW3T1Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T1Disc.setText("lblW3T1Disc");

        btnW3T1.setText("Editar");
        btnW3T1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW3T1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnW3T1)
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblW3T1Prof, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblW3T1Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW3T1Disc)
                .addGap(6, 6, 6)
                .addComponent(lblW3T1Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW3T1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 234, -1, -1));

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW4T1Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T1Disc.setText("lblW4T1Disc");

        lblW4T1Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T1Prof.setText("lblW4T1Prof");

        btnW4T1.setText("Editar");
        btnW4T1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW4T1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblW4T1Prof, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW4T1Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnW4T1)
                .addGap(64, 64, 64))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW4T1Disc)
                .addGap(6, 6, 6)
                .addComponent(lblW4T1Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW4T1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 234, 195, -1));

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setText("QUARTA-FEIRA");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel7)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 199, 195, -1));

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setText("QUINTA-FEIRA");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel8)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(737, 199, 198, -1));

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW5T1Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T1Disc.setText("lblW5T1Disc");

        lblW5T1Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T1Prof.setText("lblW5T1Prof");

        btnW5T1.setText("Editar");
        btnW5T1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW5T1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblW5T1Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblW5T1Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW5T1)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW5T1Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW5T1Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW5T1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(737, 234, 198, -1));

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW6T1Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T1Disc.setText("lblW6T1Disc");

        lblW6T1Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T1Prof.setText("lblW6T1Prof");

        btnW6T1.setText("Editar");
        btnW6T1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW6T1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnW6T1)
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblW6T1Prof, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblW6T1Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW6T1Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW6T1Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW6T1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(941, 234, -1, -1));

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("SEXTA-FEIRA");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel9)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(941, 199, 200, -1));

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW3T2Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T2Disc.setText("lblW3T2Disc");

        lblW3T2Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T2Prof.setText("lblW3T2Prof");

        btnW3T2.setText("Editar");
        btnW3T2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW3T2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW3T2Prof, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW3T2Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW3T2)
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW3T2Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW3T2Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW3T2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 323, 200, -1));

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW2T2Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T2Disc.setText("lblW2T2Disc");

        lblW2T2Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T2Prof.setText("lblW2T2Prof");

        btnW2T2.setText("Editar");
        btnW2T2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW2T2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(btnW2T2)
                .addGap(64, 64, 64))
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW2T2Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(lblW2T2Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblW2T2Disc)
                .addGap(6, 6, 6)
                .addComponent(lblW2T2Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW2T2))
        );

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 323, -1, 83));

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW4T2Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T2Disc.setText("lblW4T2Disc");

        lblW4T2Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T2Prof.setText("lblW4T2Prof");

        btnW4T2.setText("Editar");
        btnW4T2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW4T2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW4T2)
                .addGap(64, 64, 64))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW4T2Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW4T2Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW4T2Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW4T2Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnW4T2)
                .addContainerGap())
        );

        getContentPane().add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 323, -1, -1));

        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW5T2Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T2Disc.setText("lblW5T2Disc");

        lblW5T2Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T2Prof.setText("lblW5T2Prof");

        btnW5T2.setText("Editar");
        btnW5T2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW5T2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW5T2)
                .addContainerGap(67, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW5T2Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW5T2Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW5T2Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW5T2Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW5T2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(737, 323, 198, -1));

        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW6T2Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T2Disc.setText("lblW6T2Disc");

        lblW6T2Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T2Prof.setText("lblW6T2Prof");

        btnW6T2.setText("Editar");
        btnW6T2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW6T2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW6T2)
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW6T2Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW6T2Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW6T2Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblW6T2Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW6T2)
                .addContainerGap())
        );

        getContentPane().add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(941, 323, 200, -1));

        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("INTERVALO");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        getContentPane().add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 412, -1, -1));

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW5T3Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T3Disc.setText("lblW5T3Disc");

        lblW5T3Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW5T3Prof.setText("lblW5T3Prof");

        btnW5T3.setText("Editar");
        btnW5T3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW5T3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW5T3Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW5T3Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW5T3)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW5T3Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW5T3Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW5T3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(737, 447, 198, -1));

        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW3T3Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T3Disc.setText("lblW3T3Disc");

        lblW3T3Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW3T3Prof.setText("lblW3T3Prof");

        btnW3T3.setText("Editar");
        btnW3T3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW3T3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW3T3)
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW3T3Disc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW3T3Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW3T3Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW3T3Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnW3T3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 447, 200, -1));

        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW4T3Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T3Disc.setText("lblW4T3Disc");

        lblW4T3Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW4T3Prof.setText("lblW4T3Prof");

        btnW4T3.setText("Editar");
        btnW4T3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW4T3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel27Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(btnW4T3)
                        .addGap(0, 58, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel27Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblW4T3Prof, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblW4T3Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW4T3Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW4T3Prof)
                .addGap(6, 6, 6)
                .addComponent(btnW4T3)
                .addContainerGap())
        );

        getContentPane().add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 447, 195, -1));

        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW6T3Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T3Disc.setText("lblW6T3Disc");

        lblW6T3Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW6T3Prof.setText("lblW6T3Prof");

        btnW6T3.setText("Editar");
        btnW6T3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW6T3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnW6T3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblW6T3Disc, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                    .addComponent(lblW6T3Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW6T3Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW6T3Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnW6T3)
                .addContainerGap())
        );

        getContentPane().add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(941, 447, -1, -1));

        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblW2T3Disc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T3Disc.setText("lblW2T3Disc");

        lblW2T3Prof.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblW2T3Prof.setText("lblW2T3Prof");

        btnW2T3.setText("Editar");
        btnW2T3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnW2T3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(btnW2T3)
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblW2T3Disc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblW2T3Prof, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblW2T3Disc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblW2T3Prof)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnW2T3)
                .addContainerGap())
        );

        getContentPane().add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 447, 195, -1));

        jPanel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("INTERVALO");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        getContentPane().add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 412, 201, -1));

        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("INTERVALO");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
        );

        getContentPane().add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 412, 195, -1));

        jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("INTERVALO");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addContainerGap())
        );

        getContentPane().add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(737, 412, 198, -1));

        jPanel38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("INTERVALO");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        getContentPane().add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(941, 412, 201, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblT1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblT1.setText("lblT1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT1, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT1, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 234, 116, 83));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblT3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblT3.setText("lblT3");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT3, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT3, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 447, 116, 83));

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblT2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblT2.setText("lblT2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT2, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblT2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 323, 116, 83));

        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblIntervalo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIntervalo.setText("lblIntervalo");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIntervalo, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblIntervalo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 412, 116, -1));

        btnCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/close_256.png"))); // NOI18N
        btnCan.setText("Cancelar");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 560, -1, -1));

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/if_button_ok_3207.png"))); // NOI18N
        btnOk.setText("Salvar");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });
        getContentPane().add(btnOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 560, 119, -1));

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/refresh_23502.png"))); // NOI18N
        btnRefresh.setText("Desfazer Alterações");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 560, 199, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 542, 1136, 16));

        jPanel4.setBackground(new java.awt.Color(255, 0, 51));
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("X");
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 10, -1, -1));

        jLabel17.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel17.setText("Curso:");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 50, -1));

        jLabel18.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel18.setText("Turma:");
        jPanel4.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 105, -1, -1));

        jLabel19.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel19.setText("Turno:");
        jPanel4.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 130, -1, -1));

        lblCur.setText("lblCur");
        jPanel4.add(lblCur, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, -1, -1));

        lblTurno.setText("lblTurno");
        jPanel4.add(lblTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 130, -1, -1));

        jLabel16.setFont(new java.awt.Font("Lucida Sans", 1, 21)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("QUADRO DE HORÁRIO");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 1130, -1));

        lblTurma.setText("lblTurNome");
        jPanel4.add(lblTurma, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 105, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(204, 204, 255));
        jSeparator2.setOpaque(true);
        jPanel4.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 158, 900, -1));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, 166));

        btnChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/change.png"))); // NOI18N
        btnChange.setText("Mudar Turma");
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });
        getContentPane().add(btnChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 560, -1, -1));

        BackGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/background.jpg"))); // NOI18N
        BackGround.setText("jLabel15");
        getContentPane().add(BackGround, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 1146, 610));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Agility/icones/print.png"))); // NOI18N
        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 560, 120, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnW2T1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW2T1ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[0], allIdProf[0]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[0] = qForm.disc_id;
            allIdProf[0] = qForm.teacher_id;
            lblW2T1Disc.setText(qForm.lblDisc);
            lblW2T1Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW2T1ActionPerformed

    private void btnW3T1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW3T1ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[3], allIdProf[3]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[3] = qForm.disc_id;
            allIdProf[3] = qForm.teacher_id;
            lblW3T1Disc.setText(qForm.lblDisc);
            lblW3T1Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW3T1ActionPerformed

    private void btnW4T1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW4T1ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[6], allIdProf[6]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[6] = qForm.disc_id;
            allIdProf[6] = qForm.teacher_id;
            lblW4T1Disc.setText(qForm.lblDisc);
            lblW4T1Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW4T1ActionPerformed

    private void btnW5T1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW5T1ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[9], allIdProf[9]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[9] = qForm.disc_id;
            allIdProf[9] = qForm.teacher_id;
            lblW5T1Disc.setText(qForm.lblDisc);
            lblW5T1Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW5T1ActionPerformed

    private void btnW6T1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW6T1ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[12], allIdProf[12]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[12] = qForm.disc_id;
            allIdProf[12] = qForm.teacher_id;
            lblW6T1Disc.setText(qForm.lblDisc);
            lblW6T1Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW6T1ActionPerformed

    private void btnW3T2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW3T2ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[4], allIdProf[4]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[4] = qForm.disc_id;
            allIdProf[4] = qForm.teacher_id;
            lblW3T2Disc.setText(qForm.lblDisc);
            lblW3T2Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW3T2ActionPerformed

    private void btnW2T2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW2T2ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[1], allIdProf[1]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[1] = qForm.disc_id;
            allIdProf[1] = qForm.teacher_id;
            lblW2T2Disc.setText(qForm.lblDisc);
            lblW2T2Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW2T2ActionPerformed

    private void btnW4T2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW4T2ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[7], allIdProf[7]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[7] = qForm.disc_id;
            allIdProf[7] = qForm.teacher_id;
            lblW4T2Disc.setText(qForm.lblDisc);
            lblW4T2Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW4T2ActionPerformed

    private void btnW5T2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW5T2ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[10], allIdProf[10]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[10] = qForm.disc_id;
            allIdProf[10] = qForm.teacher_id;
            lblW5T2Disc.setText(qForm.lblDisc);
            lblW5T2Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW5T2ActionPerformed

    private void btnW6T2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW6T2ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[13], allIdProf[13]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[13] = qForm.disc_id;
            allIdProf[13] = qForm.teacher_id;
            lblW6T2Disc.setText(qForm.lblDisc);
            lblW6T2Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW6T2ActionPerformed

    private void btnW5T3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW5T3ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[11], allIdProf[11]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[11] = qForm.disc_id;
            allIdProf[11] = qForm.teacher_id;
            lblW5T3Disc.setText(qForm.lblDisc);
            lblW5T3Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW5T3ActionPerformed

    private void btnW3T3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW3T3ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[5], allIdProf[5]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[5] = qForm.disc_id;
            allIdProf[5] = qForm.teacher_id;
            lblW3T3Disc.setText(qForm.lblDisc);
            lblW3T3Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW3T3ActionPerformed

    private void btnW4T3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW4T3ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[8], allIdProf[8]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[8] = qForm.disc_id;
            allIdProf[8] = qForm.teacher_id;
            lblW4T3Disc.setText(qForm.lblDisc);
            lblW4T3Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW4T3ActionPerformed

    private void btnW6T3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW6T3ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[14], allIdProf[14]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[14] = qForm.disc_id;
            allIdProf[14] = qForm.teacher_id;
            lblW6T3Disc.setText(qForm.lblDisc);
            lblW6T3Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW6T3ActionPerformed

    private void btnW2T3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnW2T3ActionPerformed
        qForm = new QuadroHorarioEdit(this, this.course_id, allIdDisc[2], allIdProf[2]);
        qForm.setVisible(true);
        if (qForm.modified) {
            allIdDisc[2] = qForm.disc_id;
            allIdProf[2] = qForm.teacher_id;
            lblW2T3Disc.setText(qForm.lblDisc);
            lblW2T3Prof.setText(qForm.lblProf);
        }
    }//GEN-LAST:event_btnW2T3ActionPerformed

    private void btnCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja fechar esta janela? Todos os dados serão perdidos.", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_btnCanActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        save();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        int q = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja desfazer todas as alterações?", "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (q == JOptionPane.YES_OPTION) {
            listData();
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        QuadroHorarioSelec qhs = new QuadroHorarioSelec(new Home());
        qhs.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnChangeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JOptionPane.showMessageDialog(this, "Implementar!!");
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(QuadroHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuadroHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuadroHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuadroHorario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuadroHorario dialog = new QuadroHorario(new JFrame(), "", "");
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
    private javax.swing.JLabel BackGround;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnChange;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnW2T1;
    private javax.swing.JButton btnW2T2;
    private javax.swing.JButton btnW2T3;
    private javax.swing.JButton btnW3T1;
    private javax.swing.JButton btnW3T2;
    private javax.swing.JButton btnW3T3;
    private javax.swing.JButton btnW4T1;
    private javax.swing.JButton btnW4T2;
    private javax.swing.JButton btnW4T3;
    private javax.swing.JButton btnW5T1;
    private javax.swing.JButton btnW5T2;
    private javax.swing.JButton btnW5T3;
    private javax.swing.JButton btnW6T1;
    private javax.swing.JButton btnW6T2;
    private javax.swing.JButton btnW6T3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblCur;
    private javax.swing.JLabel lblIntervalo;
    private javax.swing.JLabel lblT1;
    private javax.swing.JLabel lblT2;
    private javax.swing.JLabel lblT3;
    private javax.swing.JLabel lblTurma;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JLabel lblW2T1Disc;
    private javax.swing.JLabel lblW2T1Prof;
    private javax.swing.JLabel lblW2T2Disc;
    private javax.swing.JLabel lblW2T2Prof;
    private javax.swing.JLabel lblW2T3Disc;
    private javax.swing.JLabel lblW2T3Prof;
    private javax.swing.JLabel lblW3T1Disc;
    private javax.swing.JLabel lblW3T1Prof;
    private javax.swing.JLabel lblW3T2Disc;
    private javax.swing.JLabel lblW3T2Prof;
    private javax.swing.JLabel lblW3T3Disc;
    private javax.swing.JLabel lblW3T3Prof;
    private javax.swing.JLabel lblW4T1Disc;
    private javax.swing.JLabel lblW4T1Prof;
    private javax.swing.JLabel lblW4T2Disc;
    private javax.swing.JLabel lblW4T2Prof;
    private javax.swing.JLabel lblW4T3Disc;
    private javax.swing.JLabel lblW4T3Prof;
    private javax.swing.JLabel lblW5T1Disc;
    private javax.swing.JLabel lblW5T1Prof;
    private javax.swing.JLabel lblW5T2Disc;
    private javax.swing.JLabel lblW5T2Prof;
    private javax.swing.JLabel lblW5T3Disc;
    private javax.swing.JLabel lblW5T3Prof;
    private javax.swing.JLabel lblW6T1Disc;
    private javax.swing.JLabel lblW6T1Prof;
    private javax.swing.JLabel lblW6T2Disc;
    private javax.swing.JLabel lblW6T2Prof;
    private javax.swing.JLabel lblW6T3Disc;
    private javax.swing.JLabel lblW6T3Prof;
    // End of variables declaration//GEN-END:variables

}
