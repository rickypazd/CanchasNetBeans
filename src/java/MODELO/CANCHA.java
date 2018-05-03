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
import java.sql.Time;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CANCHA {

    private int ID;
    private String NOMBRE;
    private int ID_TIPO;
    private int ID_COMPLEJO;
    private Conexion con = null;

    public CANCHA(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.cancha(\n"
                + "	 nombre, id_tipo, id_complejo)\n"
                + "	VALUES ( ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getNOMBRE());
        ps.setInt(2, getID_TIPO());
        ps.setInt(3, getID_COMPLEJO());
        ps.execute();

        consulta = "select last_value from cancha_id_seq";
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

    public JSONArray todas_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "SELECT *\n"
                + "	FROM public.cancha\n"
                + "    where id_complejo="+id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("ID_TIPO", rs.getInt("id_tipo"));
             obj.put("ID_COMPLEJO", rs.getInt("id_complejo"));
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

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public int getID_TIPO() {
        return ID_TIPO;
    }

    public void setID_TIPO(int ID_TIPO) {
        this.ID_TIPO = ID_TIPO;
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

}
