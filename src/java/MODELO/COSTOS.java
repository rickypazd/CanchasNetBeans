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

public class COSTOS {

    private int ID;
    private int DIA;
    private int PRECIO;
    private int ID_CANCHA;
    private Time HORA;

    private Conexion con = null;

    public COSTOS(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.costos(\n"
                + "	 dia, hora, precio, id_cancha)\n"
                + "	VALUES (?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setInt(1, getDIA());
        ps.setTime(2, getHORA());
        ps.setInt(3, getPRECIO());
        ps.setInt(4, getID_CANCHA());
        ps.execute();

        consulta = "select last_value from costos_id_seq";
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
        String consulta = "select * from costos where id_cancha=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("DIA", rs.getInt("dia"));
            obj.put("HORA", rs.getString("hora"));
            obj.put("PRECIO", rs.getInt("precio"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray todos_de_complejo_dia(int id, int dia) throws SQLException, JSONException, IOException {
        String consulta = "select * from costos where id_cancha=" + id + " and dia=" + dia + " order by(hora) asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("DIA", rs.getInt("dia"));
            obj.put("HORA", rs.getString("hora"));
            obj.put("PRECIO", rs.getInt("precio"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray horas_mas_estado(int id, int dia, String fecha) throws SQLException, JSONException, IOException {
        String consulta = "select *, (\n"
                + "	select rp.estado  \n"
                + "	from reserva_padre rp , reservas res\n"
                + "	where rp.id_cancha = cos.id_cancha\n"
                + "	and res.id_respa = rp.id\n"
                + "	and res.fecha = to_timestamp(concat('" + fecha + " ',cos.hora),'dd/MM/yyyy HH24:MI')\n"
                + "	and estado < 3\n"
                + ")\n"
                + "from costos cos\n"
                + "where cos.id_cancha = " + id + "\n"
                + "and cos.dia = " + dia + "\n"
                + "";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("ID", rs.getInt("id"));
            obj.put("DIA", rs.getInt("dia"));
            obj.put("HORA", rs.getString("hora"));
            obj.put("PRECIO", rs.getInt("precio"));
            obj.put("ESTADO", rs.getInt("estado"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONObject horas_mas_estados_tipo(int id_complejo, int id_tipo, int dia, String hora, String fecha) throws SQLException, JSONException, IOException {
        String consulta = "select cos.* , \n"
                + "	case when (\n"
                + "	select rp.estado  \n"
                + "	from reserva_padre rp , reservas res\n"
                + "	where rp.id_cancha = cos.id_cancha\n"
                + "	and res.id_respa = rp.id\n"
                + "	and res.fecha = to_timestamp(concat('" + fecha + " ',cos.hora),'dd/MM/yyyy HH24:MI')\n"
                + "	and estado < 3\n"
                + ") is null then 0 else (\n"
                + "	select rp.estado  \n"
                + "	from reserva_padre rp , reservas res\n"
                + "	where rp.id_cancha = cos.id_cancha\n"
                + "	and res.id_respa = rp.id\n"
                + "	and res.fecha = to_timestamp(concat('" + fecha + " ',cos.hora),'dd/MM/yyyy HH24:MI')\n"
                + "	and estado < 3\n"
                + ") end as estado\n"
                + "from cancha can, costos cos\n"
                + "where can.id_complejo = " + id_complejo + "\n"
                + "and can.id_tipo = " + id_tipo + "\n"
                + "and cos.id_cancha = can.id\n"
                + "and cos.dia = " + dia + "\n"
                + "and cos.hora = '" + hora + "'\n"
                + "order by (estado) asc limit 1";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        if (rs.next()) {
            obj.put("ID", rs.getInt("id"));
            obj.put("DIA", rs.getInt("dia"));
            obj.put("HORA", rs.getString("hora"));
            obj.put("PRECIO", rs.getInt("precio"));
            obj.put("ESTADO", rs.getInt("estado"));
            obj.put("ID_CANCHA", rs.getInt("id_cancha"));

        }
        ps.close();
        rs.close();
        return obj;
    }

    public JSONArray canchas_disponibles(int id_complejo, int id_tipo, int dia, String fecha) throws SQLException, JSONException, IOException {
        String consulta = "select cos.* , \n"
                + "	case when (\n"
                + "	select rp.estado  \n"
                + "	from reserva_padre rp , reservas res\n"
                + "	where rp.id_cancha = cos.id_cancha\n"
                + "	and res.id_respa = rp.id\n"
                + "	and res.fecha = to_timestamp(concat('" + fecha + " ',cos.hora),'dd/MM/yyyy HH24:MI')\n"
                + "	and estado < 3\n"
                + ") is null then 0 else (\n"
                + "	select rp.estado  \n"
                + "	from reserva_padre rp , reservas res\n"
                + "	where rp.id_cancha = cos.id_cancha\n"
                + "	and res.id_respa = rp.id\n"
                + "	and res.fecha = to_timestamp(concat('" + fecha + " ',cos.hora),'dd/MM/yyyy HH24:MI')\n"
                + "	and estado < 3\n"
                + ") end as estado\n"
                + "from cancha can, costos cos\n"
                + "where can.id_complejo = " + id_complejo + "\n"
                + "and can.id_tipo = " + id_tipo + "\n"
                + "and cos.id_cancha = can.id\n"
                + "and cos.dia = " + dia + "\n"
                + "order by cos.hora asc, cos.id_cancha asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject costo;
        JSONArray arrCostos= null;
        String hora;
        String hora_temp = "";
        JSONObject obj=null;
        while (rs.next()) {
            hora = rs.getString("hora");

            if (hora_temp.equals(hora)) {
              
                costo = new JSONObject();
                costo.put("precio", rs.getInt("precio"));
                costo.put("id_cancha", rs.getInt("id_cancha"));
                costo.put("estado", rs.getInt("estado"));
                arrCostos.put(costo);
                obj.put("CANCHAS", arrCostos);
            } else {
                if(obj != null){
                    json.put(obj);
                }
                obj = new JSONObject();
                obj.put("HORA", hora);
                costo = new JSONObject();
                costo.put("precio", rs.getInt("precio"));
                costo.put("id_cancha", rs.getInt("id_cancha"));
                costo.put("estado", rs.getInt("estado"));
                arrCostos = new JSONArray();
                arrCostos.put(costo);
            }
             hora_temp = hora;
        }
        ps.close();
        rs.close();
        return json;

    }

    public JSONArray horas_complejo(int id_complejo, int id_tipo, int dia, String fecha) throws SQLException, JSONException, IOException {
        String consulta = "select distinct (costo1.hora)\n"
                + "from cancha can, costos costo1\n"
                + "where can.id_complejo = " + id_complejo + "\n"
                + "and can. id_tipo = " + id_tipo + "\n"
                + "and costo1.id_cancha = can.id\n"
                + "and costo1.dia = " + dia + "\n"
                + "order by (costo1.hora) asc";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        while (rs.next()) {
            json.put(horas_mas_estados_tipo(id_complejo, id_tipo, dia, rs.getString("hora"), fecha));
        }
        ps.close();
        rs.close();
        return json;

    }

    public JSONArray dias_activos(int id) throws SQLException, JSONException, IOException {
        String consulta = "select dia from costos where id_cancha=" + id + " group by(dia) order by dia";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("DIA", rs.getInt("dia"));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray horas(int id) throws SQLException, JSONException, IOException {
        String consulta = "select hora from costos where id_cancha=" + id + " group by(hora) order by hora";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        JSONObject obj;
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("HORA", rs.getString("hora"));
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

    public int getDIA() {
        return DIA;
    }

    public void setDIA(int DIA) {
        this.DIA = DIA;
    }

    public int getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(int PRECIO) {
        this.PRECIO = PRECIO;
    }

    public int getID_CANCHA() {
        return ID_CANCHA;
    }

    public void setID_CANCHA(int ID_CANCHA) {
        this.ID_CANCHA = ID_CANCHA;
    }

    public Time getHORA() {
        return HORA;
    }

    public void setHORA(Time HORA) {
        this.HORA = HORA;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

}
