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

public class CORREO {

    private int ID;
    private String CORREO;
    private int ID_COMPLEJO;

    private Conexion con = null;

    public CORREO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.correos(\n"
                + "	 id_complejo, correo)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getID_COMPLEJO());
        ps.setString(2, getCORREO());
        ps.execute();

        consulta = "select last_value from correos_id_seq";
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

    public String update_correo() throws SQLException {
        String consulta = "UPDATE public.correos\n"
                + "	SET correo=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getCORREO());
        ps.execute();
        ps.close();
        return "exito";
    }

    public JSONArray todos_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from correos a where a.id_complejo"
                + "=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();

            obj.put("ID", rs.getInt("id"));
            obj.put("CORREO", rs.getString("correo"));
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

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
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
