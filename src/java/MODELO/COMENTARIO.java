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

public class COMENTARIO {

    private int ID;
    private String COMENTARIO;
    private String ID_USR;
    private int ID_COMPLEJO;
    private int CLASIFICACION;

    private Conexion con = null;

    public COMENTARIO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.comentario(\n"
                + "	id_usr, id_complejo, clasificacion, comentario)\n"
                + "	VALUES (?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getID_USR());
        ps.setInt(2, getID_COMPLEJO());
        ps.setInt(3, getCLASIFICACION());
        ps.setString(4, getCOMENTARIO());
        ps.execute();

        consulta = "select last_value from comentario_id_seq";
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
        String consulta = "select a.*, u.nombre from comentario a, usuario u where a.id_usr=u.id and a.id_complejo =" +id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("CLASIFICACION", rs.getInt("clasificacion"));
            obj.put("COMENTARIO", rs.getString("comentario"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("ID_USR", rs.getString("id_usr"));
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

    public String getCOMENTARIO() {
        return COMENTARIO;
    }

    public void setCOMENTARIO(String COMENTARIO) {
        this.COMENTARIO = COMENTARIO;
    }

    public String getID_USR() {
        return ID_USR;
    }

    public void setID_USR(String ID_USR) {
        this.ID_USR = ID_USR;
    }


    public int getID_COMPLEJO() {
        return ID_COMPLEJO;
    }

    public void setID_COMPLEJO(int ID_COMPLEJO) {
        this.ID_COMPLEJO = ID_COMPLEJO;
    }

    public int getCLASIFICACION() {
        return CLASIFICACION;
    }

    public void setCLASIFICACION(int CLASIFICACION) {
        this.CLASIFICACION = CLASIFICACION;
    }

  

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

}
