package MODELO;

import Conexion.Conexion;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HORARIO {

    private int ID;
    private String HORA_INI;
    private String HORA_FIN;
    private int DIA;
    private int ID_COMPLEJO;
    private int ABIERTO;

    private Conexion con = null;

    public HORARIO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.horario(\n"
                + "	id_complejo, hora_ini, hora_fin, dia, abierto)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getID_COMPLEJO());
        ps.setString(2, getHORA_INI());
        ps.setString(3, getHORA_FIN());
        ps.setInt(4, getDIA());
        ps.setInt(5, getABIERTO());
        ps.execute();

        consulta = "select last_value from horario_id_seq";
        ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("last_value");
        }
        rs.close();
        ps.close();
        return id;
    }

    public JSONArray todos_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from horario a where a.id_complejo"
                + "=" + id + " order by dia";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("ABIERTO", rs.getInt("abierto"));
            obj.put("HORA_INI", rs.getString("hora_ini"));
            obj.put("HORA_FIN", rs.getString("hora_fin"));
            obj.put("DIA", rs.getString("dia"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHORA_INI() {
        return HORA_INI;
    }

    public void setHORA_INI(String HORA_INI) {
        this.HORA_INI = HORA_INI;
    }

    public String getHORA_FIN() {
        return HORA_FIN;
    }

    public void setHORA_FIN(String HORA_FIN) {
        this.HORA_FIN = HORA_FIN;
    }

    public int getDIA() {
        return DIA;
    }

    public void setDIA(int DIA) {
        this.DIA = DIA;
    }

    public int getID_COMPLEJO() {
        return ID_COMPLEJO;
    }

    public void setID_COMPLEJO(int ID_COMPLEJO) {
        this.ID_COMPLEJO = ID_COMPLEJO;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public int getABIERTO() {
        return ABIERTO;
    }

    public void setABIERTO(int ABIERTO) {
        this.ABIERTO = ABIERTO;
    }

}
