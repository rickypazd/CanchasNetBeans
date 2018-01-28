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

public class TELEFONO {

    private int ID;
    private String TELEFONO;
    private int ID_COMPLEJO;

    private Conexion con = null;

    public TELEFONO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.telefonos(\n"
                + "	id_complejo, telefono)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getID_COMPLEJO());
        ps.setString(2, getTELEFONO());
        ps.execute();

        consulta = "select last_value from telefonos_id_seq";
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

    public String update_telefono() throws SQLException {
        String consulta = "UPDATE public.telefonos\n"
                + "	SET telefono=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getTELEFONO());
        ps.execute();
        ps.close();
        return "exito";
    }

    public JSONArray todos_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from telefonos a where a.id_complejo"
                + "=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();

            obj.put("ID", rs.getInt("id"));
            obj.put("TELEFONO", rs.getString("telefono"));
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

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
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
