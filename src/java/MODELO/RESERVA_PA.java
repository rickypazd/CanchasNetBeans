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

public class RESERVA_PA {

    private int ID;
    private int ESTADO;
    private int ID_CANCHA;
    private int TIPO_PAGO;
    private Date FECHA;
    private int ID_USUARIO;
    private Conexion con = null;

    public RESERVA_PA(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.reserva_padre(\n"
                + "	estado, fecha, id_cancha, id_usuario,tipo_pago)\n"
                + "	VALUES ( ?, ?, ?, ?,?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getESTADO());
        ps.setTimestamp(2, new Timestamp(getFECHA().getTime()));
        ps.setInt(3, getID_CANCHA());
        ps.setInt(4, getID_USUARIO());
        ps.setInt(5, getTIPO_PAGO());
        ps.execute();
        consulta = "select last_value from reserva_padre_id_seq";
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
        String consulta = "select * from reservas where id_cancha=" + id + " and fecha BETWEEN '" + fe_ini + "' AND '" + fe_fin + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("FECHA", rs.getString("fecha"));
            obj.put("ID_USR", rs.getInt("id_usuario"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray get_reservas_usuario(int id) throws SQLException, JSONException, IOException {
        String consulta = "select \n"
                + "rp.id,\n"
                + "rp.estado,\n"
                + "rp.tipo_pago,\n"
                + "rp.id_usuario,\n"
                + "ca.nombre as nombre_cancha,\n"
                + "com.nombre as nombre_complejo,(\n"
                + "		select re.fecha::timestamp::time\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		limit 1\n"
                + ") as hora_incio,(\n"
                + "		select re.fecha::timestamp::time\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		order by fecha desc\n"
                + "		limit 1\n"
                + ") as hora_fin,(\n"
                + "		select re.fecha::timestamp::date\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		order by fecha desc\n"
                + "		limit 1\n"
                + ") as fecha\n"
                + "from \n"
                + "reserva_padre rp, \n"
                + "cancha ca, \n"
                + "complejo com\n"
                + "where \n"
                + "ca.id= rp.id_cancha\n"
                + "and \n"
                + "ca.id_complejo = com.id\n"
                + "and \n"
                + "rp.id_usuario =" + id+" "
                + "and fecha < current_date";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("TIPO_PAGO", rs.getString("tipo_pago"));
            obj.put("ID_USUARIO", rs.getInt("id_usuario"));
            obj.put("NOMBRE_CANCHA", rs.getString("nombre_cancha") != null ? rs.getString("nombre_cancha") : "");
            obj.put("NOMBRE_COMPLEJO", rs.getString("nombre_complejo") != null ? rs.getString("nombre_complejo") : "");
            obj.put("HORA_INICIO",rs.getString("hora_incio"));
            obj.put("HORA_FIN",rs.getString("hora_fin"));
            obj.put("FECHA",rs.getString("fecha"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }
    public JSONArray get_reservas_usuario_proximas(int id) throws SQLException, JSONException, IOException {
        String consulta = "select \n"
                + "rp.id,\n"
                + "rp.estado,\n"
                + "rp.tipo_pago,\n"
                + "rp.id_usuario,\n"
                + "ca.nombre as nombre_cancha,\n"
                + "com.nombre as nombre_complejo,(\n"
                + "		select re.fecha::timestamp::time\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		limit 1\n"
                + ") as hora_incio,(\n"
                + "		select re.fecha::timestamp::time\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		order by fecha desc\n"
                + "		limit 1\n"
                + ") as hora_fin,(\n"
                + "		select re.fecha::timestamp::date\n"
                + "		from reservas re \n"
                + "		where re.id_respa = rp.id\n"
                + "		order by fecha desc\n"
                + "		limit 1\n"
                + ") as fecha\n"
                + "from \n"
                + "reserva_padre rp, \n"
                + "cancha ca, \n"
                + "complejo com\n"
                + "where \n"
                + "ca.id= rp.id_cancha\n"
                + "and \n"
                + "ca.id_complejo = com.id\n"
                + "and \n"
                + "rp.id_usuario =" + id+" "
                + "and fecha >= current_date";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("TIPO_PAGO", rs.getString("tipo_pago"));
            obj.put("ID_USUARIO", rs.getInt("id_usuario"));
            obj.put("NOMBRE_CANCHA", rs.getString("nombre_cancha") != null ? rs.getString("nombre_cancha") : "");
            obj.put("NOMBRE_COMPLEJO", rs.getString("nombre_complejo") != null ? rs.getString("nombre_complejo") : "");
            obj.put("HORA_INICIO",rs.getString("hora_incio"));
            obj.put("HORA_FIN",rs.getString("hora_fin"));
            obj.put("FECHA",rs.getString("fecha"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public void Cancelar_Reservas_menores_fecha(String fecha) throws SQLException {
        String consulta = "UPDATE public.reserva_padre\n"
                + "	SET estado=3\n"
                + "	WHERE estado=1\n"
                + "	and fecha   < '" + fecha + "'";
        PreparedStatement ps = con.statamet(consulta);

        ps.executeUpdate();
        ps.close();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getESTADO() {
        return ESTADO;
    }

    public void setESTADO(int ESTADO) {
        this.ESTADO = ESTADO;
    }

    public int getID_CANCHA() {
        return ID_CANCHA;
    }

    public void setID_CANCHA(int ID_CANCHA) {
        this.ID_CANCHA = ID_CANCHA;
    }

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public int getID_USUARIO() {
        return ID_USUARIO;
    }

    public void setID_USUARIO(int ID_USUARIO) {
        this.ID_USUARIO = ID_USUARIO;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public int getTIPO_PAGO() {
        return TIPO_PAGO;
    }

    public void setTIPO_PAGO(int TIPO_PAGO) {
        this.TIPO_PAGO = TIPO_PAGO;
    }

}
