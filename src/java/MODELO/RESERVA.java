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
import java.sql.Timestamp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RESERVA {

    private int ID;
    private Date FECHA;
    private int ID_RESPA;
    private int COSTO;
    private Conexion con = null;

    public RESERVA(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.reservas(\n"
                + "	 fecha, id_respa, costo)\n"
                + "	VALUES ( ?, ?,?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setTimestamp(1, new Timestamp(getFECHA().getTime()));
        ps.setInt(2, getID_RESPA());
        ps.setInt(3, getCOSTO());
        ps.execute();
        consulta = "select last_value from reservas_id_seq";
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

    public JSONArray get_res_can(int id, String fe_ini, String fe_fin) throws SQLException, JSONException, IOException {
        String consulta = "select r.id, rp.estado, r.fecha, rp.id_usuario \n"
                + "from reserva_padre rp, reservas r \n"
                + "where rp.id_cancha=" + id + " \n"
                + "and rp.id=r.id_respa \n"
                + "and r.fecha BETWEEN '" + fe_ini + "' AND '" + fe_fin + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("FECHA", rs.getString("fecha"));
            obj.put("ID_USR", rs.getString("id_usuario"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray get_res_de_admin(int id) throws SQLException, JSONException, IOException {
        String consulta = "select com.nombre as nombre_comp, c.nombre ,rp.* ,sum(costo) as total, count(r.id) as horas \n"
                + "from complejo com , cancha c,reserva_padre rp, reservas r \n"
                + "where rp.id_cancha=c.id and com.id_usr=" + id + " and c.id_complejo=com.id and rp.id=r.id_respa \n"
                + "group by (rp.id, com.nombre , c.nombre) order by(fecha) desc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("NOMBRE_COMP", rs.getString("nombre_comp"));
            obj.put("NOMBRE_CAN", rs.getString("nombre"));
            obj.put("ID_RES_PA", rs.getInt("id"));
            obj.put("ID_CANCHA", rs.getInt("id_cancha"));
            obj.put("ID_USUARIO", rs.getString("id_usuario"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("TOTAL", rs.getInt("total"));
            obj.put("HORAS", rs.getInt("horas"));
            obj.put("TIPO_PAGO", rs.getInt("tipo_pago"));
            obj.put("FECHA", rs.getString("fecha"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray get_res_de_usuario(String id) throws SQLException, JSONException, IOException {
        String consulta = "select com.nombre as nombre_comp, c.nombre ,rp.* ,sum(costo) as total, count(r.id) as horas \n"
                + "from complejo com , cancha c, usuario us ,reserva_padre rp, reservas r \n"
                + "where us.id='" + id + "' and us.id=rp.id_usuario and rp.id_cancha=c.id  and c.id_complejo=com.id and rp.id=r.id_respa \n"
                + "group by (rp.id, com.nombre , c.nombre) order by(fecha) desc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("NOMBRE_COMP", rs.getString("nombre_comp"));
            obj.put("NOMBRE_CAN", rs.getString("nombre"));
            obj.put("ID_RES_PA", rs.getInt("id"));
            obj.put("ID_CANCHA", rs.getInt("id_cancha"));
            obj.put("ID_USUARIO", rs.getString("id_usuario"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("TOTAL", rs.getInt("total"));
            obj.put("HORAS", rs.getInt("horas"));
            obj.put("TIPO_PAGO", rs.getInt("tipo_pago"));
            obj.put("FECHA", rs.getString("fecha"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONObject get_res_detalle(int id) throws SQLException, JSONException, IOException {
        String consulta = "select com.id as id_com, com.nombre as nombre_com, c.id as id_ca, c.nombre as nombre_ca, us.id as id_usr, us.nombre as nombre_usr\n"
                + "from reserva_padre rp, cancha c, complejo com, usuario us\n"
                + "where rp.id="+id+" and c.id=rp.id_cancha and com.id=c.id_complejo and rp.id_usuario=us.id";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
             obj.put("ID_COM", rs.getInt("id_com"));
             obj.put("ID_CA", rs.getInt("id_ca"));
            obj.put("ID_USR", rs.getString("id_usr"));
            obj.put("NOMBRE_CA", rs.getString("nombre_ca"));
            obj.put("NOMBRE_COM", rs.getString("nombre_com"));
            obj.put("NOMBRE_USR", rs.getString("nombre_usr"));
        }
        ps.close();
        rs.close();
        return obj;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public int getID_RESPA() {
        return ID_RESPA;
    }

    public void setID_RESPA(int ID_RESPA) {
        this.ID_RESPA = ID_RESPA;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public int getCOSTO() {
        return COSTO;
    }

    public void setCOSTO(int COSTO) {
        this.COSTO = COSTO;
    }

}
