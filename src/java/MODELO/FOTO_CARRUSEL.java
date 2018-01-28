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

public class FOTO_CARRUSEL {

    private int ID;
    private String FOTO;
    private int ID_COMPLEJO;

    private Conexion con = null;

    public FOTO_CARRUSEL(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.fotos_carrusel(\n"
                + "	foto, id_complejo)\n"
                + "	VALUES (?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getFOTO());
        ps.setInt(2, getID_COMPLEJO());
        ps.execute();

        consulta = "select last_value from fotos_carrusel_id_seq";
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

    public int eliminar(int id) throws SQLException {
        String consulta = "DELETE FROM public.fotos_carrusel\n"
                + "	WHERE id=?;";
        PreparedStatement ps = con.statamet(consulta);
        ps.setInt(1, id);
        ps.execute();
        ps.close();
        return id;
    }

    public JSONArray todos_de_complejo(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from fotos_carrusel a where a.id_complejo"
                + "=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();

            obj.put("ID", rs.getInt("id"));
            String foto = rs.getString("foto") + "";
            String b64 = "";
            if (foto.length() > 0 && !foto.equals("null")) {
                b64 = URL.ruta_complejo_carrusel + id + URL.barra + foto;
            }
            obj.put("FOTO", b64);
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

    public String getFOTO() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
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
