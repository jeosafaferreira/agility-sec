package Agility.api;

import Agility.dal.ModuloConexao;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author jeosafaferreira
 */
public class AgilitySec {

    static String cont = "Contate nosso suporte ligando para: (xx) xxxx-xxxx, ou através do nosso email: suporte@Agility.com.br";
    static Connection con = ModuloConexao.conector();
    static PreparedStatement pst;
    static byte[] c = "JeOsAFáLoReNA-23".getBytes(); //SnSnSSn

    //Quando houver Exception
    private static void saveError(String cod, Exception error) {
        try {
            con.prepareStatement("INSERT INTO errors SET error='" + error + "', cod='" + cod + "'").executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    //Quando não houver Exception
    private static void saveError(String cod, String msg) {
        try {
            con.prepareStatement("INSERT INTO errors SET error='" + msg + "', cod='" + cod + "'").executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public static void showError(Component parent, String cod, Exception e) {
        switch (cod) {
            case "#1037":
                JOptionPane.showMessageDialog(parent, "Desculpe, ocorreu um erro ao listar Responsáveis.\n" + cont + "\nCódigo do Erro: " + cod + "\nDetalhes do erro:\n" + e);
                break;
            default:
                JOptionPane.showMessageDialog(parent, "Desculpe, ocorreu um erro ao consultar banco de dados.\n" + cont + "\nCódigo do Erro: " + cod + "\nDetalhes do erro:\n" + e);
        }
        saveError(cod, e);
    }

    public static void showError(Component parent, String cod) {
        String msg;
        switch (cod) {
            case "#1067":
                msg = "Desculpe, ocorreu um erro ao matricular este aluno(a).\n" + cont + "\nCódigo do Erro: " + cod;
                break;
            case "#1068":
                msg = "Desculpe, ocorreu um erro ao gerar as mensalidades deste aluno(a).\n\n" + cont + "\nCódigo do Erro: " + cod;
                break;
            default:
                msg = "Desculpe, ocorreu um erro ao consultar banco de dados.\n" + cont + "\nCódigo do Erro: " + cod;

        }
        JOptionPane.showMessageDialog(parent, msg);
        saveError(cod, msg);
    }

    public static void showErrorRel(String cod, Exception e) {
        switch (cod) {
            default:
                JOptionPane.showMessageDialog(null, "Desculpe, ocorreu um erro ao emitir este documento.\n" + cont + "\nCódigo do Erro: " + cod);
        }
        saveError(cod, e);
    }

    public static BigDecimal calcMed(BigDecimal n1, BigDecimal n2) {
        return n1.divide(n2);
    }

    public static class onlyNumbers extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str.replaceAll("[^0-9]", ""), a);
        }
    }

    public static class onlyNumbersNPoints extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str.replaceAll("[^0-9,.]", ""), a);
        }
    }

    /**
     *
     * @param con Conector()
     * @param table Tabela do db
     * @param fields colunas do db
     * @param values valores à serem checados
     * @return true, se existir.
     * @throws java.sql.SQLException
     */
    public static boolean alreadyExists(Connection con, String table, String[] fields, String[] values) throws SQLException {
        ResultSet rs;
        boolean r = false;
        String columns = fields[0];
        String clause = fields[0] + "='" + values[0] + "'";

        if (fields.length > 1) {
            for (int i = 1; i < fields.length; i++) {
                columns = columns + "," + fields[i];
                clause = clause + " AND " + fields[i] + " = '" + values[i] + "'";
            }
        }

        for (String field : fields) {
            columns = columns + "," + field;
        }

        System.out.println(clause);
        PreparedStatement pst;
        pst = con.prepareStatement("SELECT " + columns + " FROM " + table + " WHERE " + clause);
        System.out.println(pst);
        rs = pst.executeQuery();
        if (rs.next()) {
            r = true;
        }

        return r;
    }

    /**
     *
     * @param con Conector()
     * @param table Tabela do db
     * @param clause Cláusula where
     * @return true, se existir.
     * @throws java.sql.SQLException
     *
     */
    public static boolean alreadyExists(Connection con, String table, String clause) throws SQLException {
        ResultSet rs;
        PreparedStatement pst;

        boolean r = false;

        pst = con.prepareStatement("SELECT * FROM " + table + " WHERE " + clause);
        System.out.println(pst);
        rs = pst.executeQuery();
        if (rs.next()) {
            r = true;
        }
        return r;
    }

    //MENSAGENS DE ERRO AVISANDO PARA PREENCHER CAMPO
    public static void checkMessageError(Component parent, JTextField field, String name) {
        JOptionPane.showMessageDialog(parent, "Por favor, preencha corretamente o campo \"" + name + "\".", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
        field.grabFocus();
    }

    public static void checkMessageError(Component parent, JTextArea field, String name) {
        JOptionPane.showMessageDialog(parent, "Por favor, preencha corretamente o campo \"" + name + "\".", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
        field.grabFocus();
    }

    public static void checkMessageError(Component parent, JComboBox field, String name) {
        JOptionPane.showMessageDialog(parent, "Por favor, selecione uma opção no campo \"" + name + "\".", "Atenção!", JOptionPane.INFORMATION_MESSAGE);
        field.grabFocus();
    }

    /**
     *
     * @param table JTable
     */
    public static void getColumnsWidth(JTable table) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            System.out.println(table.getColumnModel().getColumn(i).getWidth());
        }
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            //Lê conteúdo da URL
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            String jsonText = sb.toString().replace("[", "").replace("]", ""); //Faço replace pq no webService do cep, começa com [ ].
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * @param d (Date)
     * @return dd/MM/yyyy - HH:mm:ss
     */
    public static String df(Date d) {
        if (d != null) {
            return new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(d);
        } else {
            return "";
        }
    }

    public static byte[] encr(byte[] msg) {
        byte[] encr = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(c, "AES"));
            encr = cipher.doFinal(msg);

        } catch (Exception e) {
            showError(null, "#1159", e);
        }
        return encr;
    }

    public static String decr(byte[] msg) {
        byte[] decr = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(c, "AES"));
            decr = cipher.doFinal(msg);
        } catch (Exception e) {
            showError(null, "#1160", e);
        }
        return new String(decr);
    }

    public static void checkUpdate(Component parent) {
        String cli_id = null;
        String serverUrl = "http://127.0.0.1";
        try {
            ResultSet rs = con.prepareStatement("SELECT school_cod FROM school_data").executeQuery();
            rs.next();
            cli_id = rs.getString(1);
            System.out.println(serverUrl + "/fun.php?method=2&cli=" + cli_id);
            JSONObject json = readJsonFromUrl(serverUrl + "/fun.php?method=2&cli=" + cli_id);
            int l = json.length();
            System.out.println("L ->" + l);
            if (l > 0) {//TEM COISA PRA ATUALIZAR
                boolean upUpdater = false;
                //VERIFICA SE TEM QUE ATUALIZAR UPDATE
                for (int i = 0; i < l; i++) {
                    String op = json.getJSONObject(i + "").getString("op");
                    if (op.equals("5")) {
                        System.out.println("Op: 5");
                        boolean status = true;
                        upUpdater = true;
                        try {
                            URL url = new URL(serverUrl + "/agilityUpdate/Agility_-_AutoUpdate.jar");
                            File file = new File("Agility_-_AutoUpdate.jar");
                            FileUtils.copyURLToFile(url, file);
                        } catch (Exception e) {
                            status = false;
                            AgilitySec.saveError("#1162", e);
                        }
                        //RETORNA STATUS
                        new URL(serverUrl + "/fun.php?method=3&id=" + json.getJSONObject(i + "").getString("id") + "&q=" + status).openStream();
                    }
                }

                if (upUpdater) {//VERIFICA SE TEM OUTRAS ATUALIZAÇÕES FORA O UPDATE
                    if (l > 1) {
                        Runtime.getRuntime().exec("java -jar Agility_-_AutoUpdate.jar");
                        System.exit(0);
                    }
                } else {
                    Runtime.getRuntime().exec("java -jar Agility_-_AutoUpdate.jar");
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            showError(parent, "#1161", e);
        }

    }
}
