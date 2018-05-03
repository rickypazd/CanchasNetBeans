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

public class USUARIO_PAGE {

    private String ID;
    private String NOMBRE;

    private Conexion con = null;

    public USUARIO_PAGE(Conexion con) {
        this.con = con;
    }

    public void Insertar() throws SQLException {
        String consulta = "INSERT INTO public.usuario(\n"
                + "	id, nombre)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getID());
        ps.setString(2, getNOMBRE());
        ps.execute();
        ps.close();
    }

    public boolean existe(String id) throws SQLException {
        String consulta = "SELECT *\n"
                + "	FROM public.usuario\n"
                + "    where id='" + id + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        boolean exist=false;
        if (rs.next()) {
           exist= true;
        }
        ps.close();
        rs.close();
        return exist;
    }

    public JSONArray todas_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "SELECT *\n"
                + "	FROM public.cancha\n"
                + "    where id_complejo=" + id;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

}
