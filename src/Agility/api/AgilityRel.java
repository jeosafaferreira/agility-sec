package Agility.api;

import Agility.dal.ModuloConexao;
import Agility.telas.relatorios.AluSelec;
import Agility.telas.relatorios.CurSelec;
import Agility.telas.relatorios.DisSelec;
import Agility.telas.relatorios.ResSelec;
import Agility.telas.relatorios.TurSelec;
import Agility.telas.Dialog;
import Agility.telas.Home;
import Agility.telas.Login;
import Agility.telas.relatorios.BimSelec;
import Agility.telas.relatorios.DataSelec;
import Agility.telas.relatorios.LivSelec;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author jferreira
 */
public class AgilityRel {

    //Colocar o mouse com loading, ou tela de loading junto, tipo ajax, só que com jOptionPane
    static Connection con = ModuloConexao.conector();
    static ResultSet rs;
    static JDialog j;
    public static String url = ((Login.sys.equals("Linux")) ? "/media/jferreira/Arquivos/NetBeans Projects/JaspersoftWorkspace/Agility/" : "D:\\NetBeans Projects\\JaspersoftWorkspace\\Agility\\");

    public static void openReport(String dialogTitle, JasperPrint document) {
        if (document.hasParts()) {
            JOptionPane.showMessageDialog(null, "Nenhum dado a ser exibido!");
        } else {
            JasperViewer jv = new JasperViewer(document, false);
            j = new JDialog(new JDialog(), dialogTitle, true);
            j.setSize(900, 700);
            j.setLocationRelativeTo(null);
            j.getContentPane().add(jv.getContentPane());
            j.setVisible(true);

        }
    }
    //As classes estão nomeadas de acordo com o menu da tela "Home"

    public static void openReport(JasperPrint document) {
        if (document.hasParts()) {
            JOptionPane.showMessageDialog(null, "Nenhum dado a ser exibido!");
        } else {
            JasperViewer jv = new JasperViewer(document, false);
            jv.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            jv.setTitle("Agility® - Visualizar Relatório");
            jv.setVisible(true);
        }
    }

    public static void aluDados() {
        String cod;
        HashMap<String, Object> params = new HashMap<>();

        AluSelec dialog = new AluSelec();

        dialog.setVisible(true);
        if (dialog.modified) {
            cod = dialog.getCod();

            params.put("studentCod", cod);
            params.put("academicYear", Login.academic_year);

            try {
                JasperPrint document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluDados.jasper", params, con);
                openReport(document);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1097", e);
                System.out.println(e);
            }
        }
    }

    public static void aluDados(String cod) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentCod", cod);
        params.put("academicYear", Login.academic_year + "");

        try {
            JasperPrint document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluDados.jasper", params, con);
            openReport(document);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1146", e);

        }
    }

    public static void aluBaixoRend() {
        BimSelec bs = new BimSelec();
        bs.setVisible(true);
        JasperPrint document = null;

        if (bs.modified) {
            try {
                switch (bs.getBim()) {
                    case 0:
                        //1º Bimestre
                        document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluBaixoRend/Bim1.jasper", null, con);
                        break;
                    case 1:
                        //2º Bimestre
                        document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluBaixoRend/Bim2.jasper", null, con);
                        break;
                    case 2:
                        //3º Bimestre
                        document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluBaixoRend/Bim3.jasper", null, con);
                        break;
                    case 3:
                        //4º Bimestre
                        document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluBaixoRend/Bim4.jasper", null, con);
                        break;
                }
            } catch (Exception e) {
                AgilitySec.showErrorRel("#1163", e);
            }
            openReport(document);
        }

    }

    public static void aluFaltosos() {
        BimSelec bs = new BimSelec();
        bs.setVisible(true);
        JasperPrint document = null;
        Map<String, Object> params = new HashMap();

        if (bs.modified) {
            JPanel jp = new JPanel();
            JLabel lbl = new JLabel("Número de faltas maior que:");
            SpinnerNumberModel spinMdl = new SpinnerNumberModel(1, 1, 99, 1);
            JSpinner qtd = new JSpinner(spinMdl);
            jp.add(lbl);
            jp.add(qtd);
            int q = JOptionPane.showOptionDialog(null, jp, "Digite um número", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (q == JOptionPane.OK_OPTION) {
                params.put("qtd", Integer.parseInt(qtd.getValue().toString()));
                try {
                    switch (bs.getBim()) {
                        case 0:
                            //1º Bimestre
                            document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluFaltosos/Bim1.jasper", params, con);
                            break;
                        case 1:
                            //2º Bimestre
                            document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluFaltosos/Bim2.jasper", params, con);
                            break;
                        case 2:
                            //3º Bimestre
                            document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluFaltosos/Bim3.jasper", params, con);
                            break;
                        case 3:
                            //4º Bimestre
                            document = (JasperPrint) JasperFillManager.fillReport(url + "Alunos/AluFaltosos/Bim4.jasper", params, con);
                            break;
                    }
                } catch (Exception e) {
                    AgilitySec.showErrorRel("#1164", e);
                }
                openReport(document);
            }
        }
    }

    public static void aluLista() {
        try {
            JasperPrint rel = JasperFillManager.fillReport(url + "Alunos/AluLista.jasper", null, con);
            AgilityRel.openReport(rel);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1103", e);
            System.out.println(e);
        }
    }

    public static void respDados() {
        String id;
        Map<String, Object> params = new HashMap<>();
        ResSelec di = new ResSelec();

        di.setVisible(true);
        id = di.getId();
        if (di.modified) {
            params.put("respId", id);
            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Responsaveis/RespDados.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1105", e);
                System.out.println(e);
            }
        }

    }

    public static void respLista() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Responsaveis/RespLista.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1106", e);
            System.out.println(e);
        }
    }

    public static void respInad() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Responsaveis/RespInad.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1107", e);
            System.out.println(e);
        }

    }

    public static void ocoByAlu() {
        String cod;
        HashMap<String, Object> params = new HashMap<>();

        AluSelec di = new AluSelec();

        di.setVisible(true);
        cod = di.getCod();
        if (cod != null) {
            params.put("studentCod", cod);
            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Ocorrencias/OcoByAlu.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1108", e);
                System.out.println(e);
            }
        }
    }

    public static void ocoByTurma() {
        String id;
        Map<String, Object> params = new HashMap<>();
        TurSelec di = new TurSelec();

        di.setVisible(true);
        id = di.getId();
        if (di.modified) {
            params.put("classId", id);

            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Ocorrencias/OcoByTurma.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1110", e);
                System.out.println(e);
            }
        }
    }

    public static void ocoByCurso() {
        String id;
        Map<String, Object> params = new HashMap<>();
        CurSelec di = new CurSelec();
        di.setVisible(true);

        id = di.getId();
        if (di.modified) {
            params.put("courseId", id);
            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Ocorrencias/OcoByCurso.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1109", e);
                System.out.println(e);
            }
        }
    }

    public static void ocoTodas() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Ocorrencias/OcoTodas.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1111", e);
            System.out.println(e);
        }
    }

    public static void curDados() {
        String id;
        Map<String, Object> params = new HashMap<>();
        CurSelec di = new CurSelec();
        di.setVisible(true);

        if (di.modified) {
            id = di.getId();

            params.put("courseId", id);
            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Cursos/CurDados.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1112", e);
                System.out.println(e);
            }
        }
    }

    public static void curMatriz() {
        String id;
        Map<String, Object> params = new HashMap<>();
        CurSelec di = new CurSelec();
        di.setVisible(true);

        id = di.getId();
        if (di.modified) {
            params.put("courseId", id);
            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Cursos/CurMatriz.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1113", e);
                System.out.println(e);
            }
        }
    }

    public static void curLista() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Cursos/CurLista.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1114", e);
            System.out.println(e);
        }
    }

    public static void turDados() {
        String id;
        Map<String, Object> params = new HashMap<>();
        TurSelec di = new TurSelec();

        di.setVisible(true);
        id = di.getId();
        if (di.modified) {
            params.put("classId", id);

            try {
                JasperPrint rep = JasperFillManager.fillReport(url + "Turmas/TurDados.jasper", params, con);
                AgilityRel.openReport(rep);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1115", e);
                System.out.println(e);
            }
        }
    }

    public static void turLista() {
        //Ainda não tem
    }

    public static JasperPrint turQuaHor(String classId) {
        JasperPrint jp = null;
        String allCodDisc[] = new String[15];
        String allIdProf[] = new String[15];
        String allIdDisc[] = new String[15];
        String allNameProf[] = new String[15];
        String allNameDisc[] = new String[15];

        Arrays.fill(allCodDisc, " ");

        Arrays.fill(allNameDisc, " ");
        Arrays.fill(allNameProf, " ");

        Map<String, Object> params = new HashMap<>();
        TurSelec di = new TurSelec();

        if (classId.isEmpty()) {
            di.setVisible(true);
            classId = (di.getId() != null) ? di.getId() : "";
        }
        if (!classId.isEmpty()) {

            //HEADER
            try {
                //CURSO, TURMA, TURNO
                rs = con.prepareStatement("SELECT CLA.cod, CLA.name, CLA.turno, CRO.cod, CRO.name, COU.cod, COU.name FROM classes CLA LEFT JOIN courses COU ON COU.id=CLA.course_id LEFT JOIN classrooms CRO ON CRO.id=CLA.classroom_id WHERE CLA.id='" + classId + "'").executeQuery();
                rs.next();
                params.put("CLA.cod", rs.getString("CLA.cod"));
                params.put("CLA.name", rs.getString("CLA.name"));
                params.put("CLA.turno", rs.getString("CLA.turno"));
                params.put("COU.cod", rs.getString("COU.cod"));
                params.put("COU.name", rs.getString("COU.name"));
                params.put("CRO.cod", rs.getString("CRO.cod"));
                params.put("CRO.name", rs.getString("CRO.name"));

                //HORÁRIOS
                if (rs.getString("turno").equals("Manhã")) {
                    params.put("T1", "07:30h ~ 08:30h");
                    params.put("T2", "08:30h ~ 09:30h");
                    params.put("tInter", "09:30h ~ 10:00h");
                    params.put("T3", "10:00h ~ 11:00h");

                } else if (rs.getString("turno").equals("Tarde")) {
                    params.put("T1", "13:30h ~ 14:30h");
                    params.put("T2", "14:30h ~ 15:30h");
                    params.put("tInter", "15:30h ~ 16:00h");
                    params.put("T3", "16:00h ~ 17:00h");
                }

            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1116", e);
                System.out.println(e);
            }

            try {
                rs = con.prepareStatement("SELECT S.*,E.name,D.name,D.cod FROM schedules S LEFT JOIN employees E ON E.id=S.employee_id LEFT JOIN disciplines D ON D.id=S.disc_id WHERE S.class_id=" + classId + " ORDER BY S.weekday ASC, S.time ASC").executeQuery();
                System.out.println(rs.getStatement());

                int i = 0;
                while (rs.next()) {

                    allIdProf[i] = rs.getString("S.employee_id");
                    allIdDisc[i] = rs.getString("S.disc_id");

                    allNameDisc[i] = rs.getString("D.name");
                    allNameProf[i] = rs.getString("E.name");

                    allCodDisc[i] = rs.getString("D.cod");
                    i++;
                }
                params.put("disc0", allCodDisc[0] + " - " + allNameDisc[0]);
                params.put("disc1", allCodDisc[1] + " - " + allNameDisc[1]);
                params.put("disc2", allCodDisc[2] + " - " + allNameDisc[2]);
                params.put("disc3", allCodDisc[3] + " - " + allNameDisc[3]);
                params.put("disc4", allCodDisc[4] + " - " + allNameDisc[4]);
                params.put("disc5", allCodDisc[5] + " - " + allNameDisc[5]);
                params.put("disc6", allCodDisc[6] + " - " + allNameDisc[6]);
                params.put("disc7", allCodDisc[7] + " - " + allNameDisc[7]);
                params.put("disc8", allCodDisc[8] + " - " + allNameDisc[8]);
                params.put("disc9", allCodDisc[9] + " - " + allNameDisc[9]);
                params.put("disc10", allCodDisc[10] + " - " + allNameDisc[10]);
                params.put("disc11", allCodDisc[11] + " - " + allNameDisc[11]);
                params.put("disc12", allCodDisc[12] + " - " + allNameDisc[12]);
                params.put("disc13", allCodDisc[13] + " - " + allNameDisc[13]);
                params.put("disc14", allCodDisc[14] + " - " + allNameDisc[14]);

                params.put("prof0", allNameProf[0]);
                params.put("prof1", allNameProf[1]);
                params.put("prof2", allNameProf[2]);
                params.put("prof3", allNameProf[3]);
                params.put("prof4", allNameProf[4]);
                params.put("prof5", allNameProf[5]);
                params.put("prof6", allNameProf[6]);
                params.put("prof7", allNameProf[7]);
                params.put("prof8", allNameProf[8]);
                params.put("prof9", allNameProf[9]);
                params.put("prof10", allNameProf[10]);
                params.put("prof11", allNameProf[11]);
                params.put("prof12", allNameProf[12]);
                params.put("prof13", allNameProf[13]);
                params.put("prof14", allNameProf[14]);

                jp = JasperFillManager.fillReport(url + "Turmas/TurQuaHor.jasper", params, con);

            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1116", e);
                System.out.println(e);

            } catch (JRException e) {
                AgilitySec.showErrorRel("#1117", e);
                System.out.println(e);
            }
        }
        return jp;
    }

    public static void disDados() {
        String id;
        Map<String, Object> params = new HashMap<>();
        DisSelec di = new DisSelec();
        di.setVisible(true);

        id = di.getId();
        if (di.modified) {
            params.put("discId", id);

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Disciplinas/DisDados.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1121", e);
                System.out.println(e);

            }
        }
    }

    public static void disDisxProf() {
        try {
            JasperPrint jp = JasperFillManager.fillReport(url + "Disciplinas/DisDisxProf.jasper", null, con);
            AgilityRel.openReport(jp);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1122", e);
            System.out.println(e);

        }
    }

    public static void funDados() {
        String id, sql;
        String[] headers = new String[4];
        int[] widths = new int[4];
        Map<String, Object> params = new HashMap<>();

        sql = "SELECT E.id, E.name, O.title, E.date_admission FROM employees E LEFT JOIN occupations O ON E.occupation_id = O.id ORDER BY E.name";
        headers[0] = "COD";
        headers[1] = "Nome Completo";
        headers[2] = "Cargo";
        headers[3] = "Data de Admissão";
        widths[0] = 10;
        widths[1] = 200;
        widths[3] = 100;
        widths[2] = 40;

        Dialog di = new Dialog(new JDialog(), true, sql, headers, widths);
        di.setVisible(true);
        if (di.idFDialog() > 0) {
            try {
                rs = con.prepareStatement(sql).executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("E.id"));
                }
                rs.absolute(di.idFDialog());
                params.put("empId", rs.getString("E.id"));
                JasperPrint jp = JasperFillManager.fillReport(url + "Funcionários/FunDados.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (SQLException | JRException e) {
                AgilitySec.showErrorRel("#1123", e);
                System.out.println(e);

            }
        }
    }

    public static void funAtivos() {
        try {

            JasperPrint jp = JasperFillManager.fillReport(url + "Funcionários/FunAtivos.jasper", null, con);
            AgilityRel.openReport(jp);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1124", e);
            System.out.println(e);

        }
    }

    public static void funInativos() {
        try {
            JasperPrint jp = JasperFillManager.fillReport(url + "Funcionários/FunInativos.jasper", null, con);
            AgilityRel.openReport(jp);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1125", e);
            System.out.println(e);

        }
    }

    public static void livDados() {
        String id;
        Map<String, Object> params = new HashMap<>();
        LivSelec di = new LivSelec();
        di.setVisible(true);

        id = di.getId();
        if (di.modified) {
            params.put("bookId", id);

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Livros/LivDados.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1127", e);
                System.out.println(e);

            }
        }
    }

    public static void livLista() {
        String id;
        Map<String, Object> params = new HashMap<>();
        CurSelec di = new CurSelec();
        di.setVisible(true);

        id = di.getId();
        if (di.modified) {
            params.put("courseId", id);

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Livros/LivLista.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1128", e);
                System.out.println(e);
            }
        }
    }

    public static void bolBim() {

    }

    public static void bolFin() {
        String cod, year = null;
        Map<String, Object> params = new HashMap<>();
        AluSelec di = new AluSelec();
        di.setVisible(true);
        cod = di.getCod();

        //Pega anos letivos que tem dados na tabela de boletins
        try {
            rs = con.prepareStatement("SELECT academic_year FROM agility.boletins GROUP BY academic_year").executeQuery();
            List opt = new ArrayList();
            while (rs.next()) {
                opt.add(rs.getString("academic_year"));
            }
            year = JOptionPane.showInputDialog(null, "<html><b>Ano Letivo", "Escolha uma opção", JOptionPane.PLAIN_MESSAGE, null, opt.toArray(), "").toString();
        } catch (SQLException e) {
            AgilitySec.showErrorRel("#1129", e);
        }

        if (year != null && cod != null) {
            params.put("studentCod", cod);
            params.put("academicYear", year);

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Boletins/BolFin.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1130", e);
                System.out.println(e);
            }
        }
    }

    public static void finConPagar() {
        String[] getData;
        String ano, anoMax = null, anoMin = null, mesInt, mesStr;
        Map<String, Object> params = new HashMap<>();
        DataSelec di = new DataSelec();
        di.setVisible(true);

        getData = di.getData().split(";");
        System.out.println(Arrays.toString(getData));
        if (di.modified) {

            try {
                rs = con.prepareStatement("SELECT MIN(YEAR(venc)), MAX(YEAR(venc)) from finances").executeQuery();
                rs.next();
                anoMin = rs.getString("MIN(YEAR(venc))");
                anoMax = rs.getString("MAX(YEAR(venc))");
            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1137", e);
                System.out.println(e);
            }

            ano = getData[0];
            mesInt = getData[1];
            mesStr = getData[2];

            if (mesStr.equals("<HTML><B>TODOS")) {
                if (ano.equals("<HTML><B>TODOS")) {
                    params.put("startDate", anoMin + "-" + 01 + "-01");
                    params.put("endDate", anoMax + "-" + 12 + "-31");
                    params.put("period", "TODAS");
                } else {
                    params.put("startDate", ano + "-" + 01 + "-01");
                    params.put("endDate", ano + "-" + 12 + "-31");
                    params.put("period", "TODAS DO ANO " + ano);
                }
            } else {
                params.put("startDate", ano + "-" + mesInt + "-01");
                params.put("endDate", ano + "-" + mesInt + "-31");
                params.put("period", mesStr + "/" + ano);
            }

            try {
                System.out.println(params.toString());

                JasperPrint jp = JasperFillManager.fillReport(url + "Financeiro/FinConPagar.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1132", e);
                System.out.println(e);
            }
        }
    }

    public static void finConBaixadas() {
        String[] getData;
        String ano, anoMax = null, anoMin = null, mesInt, mesStr;
        Map<String, Object> params = new HashMap<>();
        DataSelec di = new DataSelec();
        di.setVisible(true);

        getData = di.getData().split(";");
        System.out.println(Arrays.toString(getData));
        if (di.modified) {

            try {
                rs = con.prepareStatement("SELECT MIN(YEAR(venc)), MAX(YEAR(venc)) from finances").executeQuery();
                rs.next();
                anoMin = rs.getString("MIN(YEAR(venc))");
                anoMax = rs.getString("MAX(YEAR(venc))");
            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1138", e);
                System.out.println(e);
            }

            ano = getData[0];
            mesInt = getData[1];
            mesStr = getData[2];

            if (mesStr.equals("<HTML><B>TODOS")) {
                if (ano.equals("<HTML><B>TODOS")) {
                    params.put("startDate", anoMin + "-" + 01 + "-01");
                    params.put("endDate", anoMax + "-" + 12 + "-31");
                    params.put("period", "TODAS");
                } else {
                    params.put("startDate", ano + "-" + 01 + "-01");
                    params.put("endDate", ano + "-" + 12 + "-31");
                    params.put("period", "TODAS DO ANO " + ano);
                }
            } else {
                params.put("startDate", ano + "-" + mesInt + "-01");
                params.put("endDate", ano + "-" + mesInt + "-31");
                params.put("period", mesStr + "/" + ano);
            }

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Financeiro/FinConBaixadas.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1133", e);
                System.out.println(e);
            }
        }
    }

    public static void finConVencidas() {

        try {
            JasperPrint jp = JasperFillManager.fillReport(url + "Financeiro/FinConVencidas.jasper", null, con);
            AgilityRel.openReport(jp);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1134", e);
            System.out.println(e);
        }
    }

    public static void finFluxoMensal() {
        JasperPrint rel;
        Map<String, Object> params = new TreeMap<>();
        Map<String, String> entSql = new TreeMap<>();
        Map<String, String> saiSql = new TreeMap<>();
        Map<String, String> linhaPlanId = new TreeMap<>();

        //OS MESES COMEÇAM EM 0! PORTANTO, JANEIRO = 0, FEVEREIRO = 1, ...
        String[] getData;
        String ano, mesInt, mesStr, mesCZero;

        DataSelec di = new DataSelec("DisableOptionAll");
        di.setVisible(true);

        getData = di.getData().split(";");
        if (di.modified) {
            ano = getData[0];
            mesInt = getData[1];
            mesStr = getData[2];
            mesCZero = (Integer.parseInt(mesInt) < 10) ? "0" + mesInt : mesInt;

            params.put("mes", mesStr.toUpperCase());
            params.put("ano", "2018");

            //DIAS DO MÊS E RESPECTIVOS DIAS DA SEMANA
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, Integer.parseInt(mesInt) - 1); //Em mesInt -> Janeiro=1; No Calendar, Janeiro=0; Portanto, mesInt-1
            int qtdDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            Calendar loopPeriod = Calendar.getInstance();
            loopPeriod.set(Integer.parseInt(ano), Integer.parseInt(mesInt) - 1, 1);
            for (int i = 1; i < qtdDays + 1; i++) {
                params.put("dM" + i, "" + i);
                params.put("dS" + i, new DateFormatSymbols().getShortWeekdays()[loopPeriod.get(Calendar.DAY_OF_WEEK)]);
                loopPeriod.add(Calendar.DAY_OF_MONTH, +1);
            }

            try {
                //ENTRADAS (MENSALIDADES) --------------------------------------
                rs = con.prepareStatement("SELECT DAY(data_baixa), sum(valor_pago) FROM tuitions WHERE valor_pago IS NOT NULL AND data_baixa BETWEEN '" + ano + "-" + mesInt + "-01' AND '" + ano + "-" + mesInt + "-31' GROUP BY data_baixa").executeQuery();
                System.out.println("PST1->" + rs.getStatement());
                while (rs.next()) {
                    entSql.put(rs.getString(1), rs.getString(2));
                }
                System.out.println("entSql->" + entSql.toString());

                for (int d = 1; d < qtdDays + 1; d++) {
                    if (entSql.get(d + "") != null) {
                        params.put("ent" + d, entSql.get(d + ""));
                    } else {
                        params.put("ent" + d, "--");
                    }
                }

                //SAÍDAS --------------------------------------------------------
                //busca planos de conta
                rs = con.prepareStatement("SELECT id,title FROM acc_plan WHERE type='Pagamentos' ORDER BY id ASC").executeQuery();
                while (rs.next()) {
                    linhaPlanId.put("linha" + rs.getRow(), rs.getString("id"));
                    params.put("plan" + rs.getRow(), rs.getString("title").toUpperCase());
                }

                //loop nas linhas do relatório
                for (int l = 1; l < linhaPlanId.size() + 1; l++) {
                    saiSql.clear();//Apaga dados do loop anterior
                    rs = con.prepareStatement("SELECT DAY(FIN.data_pag), sum(FIN.valor_pago) FROM finances FIN "
                            + "WHERE FIN.data_pag IS NOT NULL "
                            + "AND FIN.acc_plan_id='" + linhaPlanId.get("linha" + l) + "' "
                            + "AND data_pag BETWEEN '" + ano + "-" + mesInt + "-01' AND '" + ano + "-" + mesInt + "-31' "
                            + "GROUP BY FIN.data_pag").executeQuery();
                    while (rs.next()) {
                        saiSql.put(rs.getString(1), rs.getString(2));
                    }

                    for (int d = 1; d < qtdDays + 1; d++) {
                        if (saiSql.get(d + "") != null) {
                            params.put("sai" + l + d, saiSql.get(d + ""));
                        } else {
                            params.put("sai" + l + d, "--");
                        }
                    }

                }
                System.out.println("Params->" + params.toString());

                //MOVIMENTO
                for (int d = 1; d < qtdDays + 1; d++) {
                    String num;
                    BigDecimal mov = new BigDecimal("0.00");

                    //ENTRADAS
                    //Verifica se é número ou se é "--"
                    try {
                        num = new BigDecimal(params.get("ent" + d).toString()).toString();
                        Double.parseDouble(num);
                    } catch (NumberFormatException e) {
                        num = "0.00";
                    }
                    mov = mov.add(new BigDecimal(num));

                    //SAÍDAS
                    for (int line = 1; line < linhaPlanId.size() + 1; line++) {
                        //Verifica se é número ou se é "--"
                        try {
                            num = new BigDecimal(params.get("sai" + line + d).toString()).toString();
                            Double.parseDouble(num);
                        } catch (NumberFormatException e) {
                            num = "0.00";
                        }
                        mov = mov.subtract(new BigDecimal(num));
                    }
                    params.put("mov" + d, (mov.compareTo(new BigDecimal("0.00")) > 0 ? "+" : "") + mov.toString());
                }

                //CHAMA RELATÓRIO
                rel = JasperFillManager.fillReport(url + "Financeiro/FinFluxoMensal.jasper", params, con);
                openReport(rel);
            } catch (SQLException ex) {
                System.out.println(ex);

            } catch (JRException ex) {
                Logger.getLogger(Home.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void finFluxoAnual() {
        Map<String, Object> paramsToReport = new TreeMap<>();
        Map<String, String> linhaPlanId = new TreeMap<>();
        Map<String, String> saiSql = new TreeMap<>();
        Map<String, String> entSql = new TreeMap<>();
        DataSelec di = new DataSelec("DisableMonth");
        di.setVisible(true);
        if (di.modified) {
            String getData[] = di.getData().split(";");
            String year = getData[0];
            paramsToReport.put("ano", year);

            try {
                //Busca plano de contas
                rs = con.prepareStatement("SELECT id, title FROM acc_plan WHERE type='Pagamentos'").executeQuery();
                while (rs.next()) {
                    linhaPlanId.put("linha" + rs.getRow(), rs.getString("id"));
                    paramsToReport.put("plan" + rs.getRow(), rs.getString("title").toUpperCase());
                }
                //-----------ENTRADAS-----------------------------
                rs = con.prepareStatement("SELECT month(data_baixa), sum(valor_pago) FROM tuitions WHERE valor_pago IS NOT NULL AND YEAR(data_baixa)='" + year + "' GROUP BY MONTH(data_baixa)").executeQuery();
                while (rs.next()) {
                    entSql.put(rs.getString(1), rs.getString(2));
                }
                for (int m = 1; m < 13; m++) {
                    if (entSql.get("" + m) != null) {
                        paramsToReport.put("ent" + m, entSql.get("" + m));
                    } else {
                        paramsToReport.put("ent" + m, "--");
                    }
                }

                //-----------SAÍDAS-------------------------------
                //Busca Dados
                for (int l = 1; l < linhaPlanId.size() + 1; l++) {
                    saiSql.clear();
                    //ADICIONAR FILTRO DO ANO(DE FORMA DINAMICA, BUSCANDO NA TABELA DE DADOS DA ESCOLA)
                    rs = con.prepareStatement("SELECT month(data_pag), sum(valor_pago) FROM agility.finances WHERE data_pag IS NOT NULL AND YEAR(data_pag)='" + year + "'AND acc_plan_id=" + linhaPlanId.get("linha" + l) + " GROUP BY MONTH(data_pag)").executeQuery();
                    while (rs.next()) {
                        saiSql.put(rs.getString(1), rs.getString(2));
                    }

                    for (int m = 1; m < 13; m++) {
                        if (saiSql.get("" + m) != null) {
                            paramsToReport.put("sai" + l + m, saiSql.get("" + m));
                        } else {
                            paramsToReport.put("sai" + l + m, "--");
                        }
                    }
                }
                //----------MOVIMENTAÇÃO
                BigDecimal mov;
                for (int m = 1; m < 13; m++) {
                    mov = new BigDecimal("0.00");
                    //SOMA(ENTRADAS)
                    if (!paramsToReport.get("ent" + m).equals("--")) {
                        mov = mov.add(new BigDecimal(paramsToReport.get("ent" + m).toString()));
                    }
                    //SUBTRAI SAÍDAS
                    for (int l = 1; l < linhaPlanId.size() + 1; l++) {
                        if (!paramsToReport.get("sai" + l + m).equals("--")) {
                            mov = mov.subtract(new BigDecimal(paramsToReport.get("sai" + l + m).toString()));
                        }
                    }
                    paramsToReport.put("mov" + m, (mov.compareTo(new BigDecimal("0.00")) > 0 ? "+" : "") + mov.toString());
                }

                JasperPrint rel = JasperFillManager.fillReport(url + "Financeiro/FinFluxoAnual.jasper", paramsToReport, con);
                openReport(rel);
            } catch (SQLException | JRException e) {
                AgilitySec.showErrorRel("#1141", e);
            }
        }
    }

    public static void finMenVencidas() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Financeiro/Mensalidades/FinMenVencidas.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1139", e);
            System.out.println(e);
        }
    }

    public static void finMenAberto() {
        String[] getData;
        String ano, anoMax = null, anoMin = null, mesInt, mesStr;
        Map<String, Object> params = new HashMap<>();
        DataSelec di = new DataSelec();
        di.setVisible(true);

        getData = di.getData().split(";");
        System.out.println(Arrays.toString(getData));
        if (di.modified) {
            try {
                rs = con.prepareStatement("SELECT MIN(YEAR(venc)), MAX(YEAR(venc)) from tuitions WHERE valor_pago IS NULL ").executeQuery();
                rs.next();
                anoMin = rs.getString("MIN(YEAR(venc))");
                anoMax = rs.getString("MAX(YEAR(venc))");
            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1140", e);
                System.out.println(e);
            }

            ano = getData[0];
            mesInt = getData[1];
            mesStr = getData[2];

            if (mesStr.equals("<HTML><B>TODOS")) {
                if (ano.equals("<HTML><B>TODOS")) {
                    params.put("startDate", anoMin + "-" + 01 + "-01");
                    params.put("endDate", anoMax + "-" + 12 + "-31");
                    params.put("period", "TODAS");
                } else {
                    params.put("startDate", ano + "-" + 01 + "-01");
                    params.put("endDate", ano + "-" + 12 + "-31");
                    params.put("period", "TODAS DO ANO " + ano);
                }
            } else {
                params.put("startDate", ano + "-" + mesInt + "-01");
                params.put("endDate", ano + "-" + mesInt + "-31");
                params.put("period", mesStr + "/" + ano);
            }

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Financeiro/Mensalidades/FinMenAberto.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1133", e);
                System.out.println(e);
            }
        }
    }

    public static void finMenBaixadas() {
        String[] getData;
        String ano, anoMax = null, anoMin = null, mesInt, mesStr;
        Map<String, Object> params = new HashMap<>();
        DataSelec di = new DataSelec();
        di.setVisible(true);

        getData = di.getData().split(";");
        System.out.println(Arrays.toString(getData));
        if (di.modified) {
            try {
                rs = con.prepareStatement("SELECT MIN(YEAR(venc)), MAX(YEAR(venc)) from tuitions WHERE valor_pago IS NOT NULL ").executeQuery();
                rs.next();
                anoMin = rs.getString("MIN(YEAR(venc))");
                anoMax = rs.getString("MAX(YEAR(venc))");
            } catch (SQLException e) {
                AgilitySec.showErrorRel("#1140", e);
                System.out.println(e);
            }

            ano = getData[0];
            mesInt = getData[1];
            mesStr = getData[2];

            if (mesStr.equals("<HTML><B>TODOS")) {
                if (ano.equals("<HTML><B>TODOS")) {
                    params.put("startDate", anoMin + "-" + 01 + "-01");
                    params.put("endDate", anoMax + "-" + 12 + "-31");
                    params.put("period", "TODAS");
                } else {
                    params.put("startDate", ano + "-" + 01 + "-01");
                    params.put("endDate", ano + "-" + 12 + "-31");
                    params.put("period", "TODAS DO ANO " + ano);
                }
            } else {
                params.put("startDate", ano + "-" + mesInt + "-01");
                params.put("endDate", ano + "-" + mesInt + "-31");
                params.put("period", mesStr + "/" + ano);
            }

            try {
                JasperPrint jp = JasperFillManager.fillReport(url + "Financeiro/Mensalidades/FinMenBaixadas.jasper", params, con);
                AgilityRel.openReport(jp);
            } catch (JRException e) {
                AgilitySec.showErrorRel("#1133", e);
                System.out.println(e);
            }
        }
    }

    public static void profLista() {
        try {
            JasperPrint rep = JasperFillManager.fillReport(url + "Professores/ProfLista.jasper", null, con);
            AgilityRel.openReport(rep);
        } catch (JRException e) {
            AgilitySec.showErrorRel("#1140", e);
            System.out.println(e);
        }
    }

    public static void outListaDeNotas() {

    }

    public static void outListaDeChamada() {

    }
}
