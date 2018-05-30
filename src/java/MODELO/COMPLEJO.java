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

public class COMPLEJO {

    private int ID;
    private String NOMBRE;
    private String POLITICAS;
    private String PRESENTACION;
    private String DIRECCION;
    private String FOTO_PERFIL;
    private double LAT;
    private double LON;
    private int ID_USR;

    private Conexion con = null;

    public COMPLEJO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.complejo(\n"
                + "      nombre, presentacion, lat, lon, direccion, id_usr, politicas)\n"
                + "	VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getPRESENTACION());
        ps.setDouble(3, getLAT());
        ps.setDouble(4, getLON());
        ps.setString(5, getDIRECCION());
        ps.setInt(6, getID_USR());
        ps.setString(7, getPOLITICAS());
        ps.execute();

        consulta = "select last_value from complejo_id_seq";
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

    public String update_nombre(int id, String nombre) throws SQLException {
        String consulta = "UPDATE public.complejo\n"
                + " SET nombre=?\n"
                + " WHERE complejo.id=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, nombre);
        ps.execute();
        ps.close();
        return "exito";
    }

    public String update_presentacion(int id, String presentacion) throws SQLException {
        String consulta = "UPDATE public.complejo\n"
                + " SET presentacion=?\n"
                + " WHERE id=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, presentacion);
        ps.execute();
        ps.close();
        return "exito";
    }

    public String update_direccion(int id, String direccion) throws SQLException {
        String consulta = "UPDATE public.complejo\n"
                + " SET direccion=?\n"
                + " WHERE id=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, direccion);
        ps.execute();
        ps.close();
        return "exito";
    }

    public String update_politicas(int id, String politicas) throws SQLException {
        String consulta = "UPDATE public.complejo\n"
                + " SET politicas=?\n"
                + " WHERE id=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, politicas);
        ps.execute();
        ps.close();
        return "exito";
    }

    public boolean subir_foto() throws SQLException {
        String consulta = "UPDATE public.complejo\n"
                + "	SET  foto_perfil=?\n"
                + "	WHERE id=" + getID();
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getFOTO_PERFIL());
        ps.execute();
        ps.close();
        return true;
    }

    public JSONArray todos_de_usr_con_datos(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from complejo where id_usr"
                + "=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        TELEFONO tel = new TELEFONO(con);
        CORREO cor = new CORREO(con);
        CARACTERISTICA car = new CARACTERISTICA(con);
        HORARIO hor = new HORARIO(con);
        JSONObject obj;
        int ids;
        while (rs.next()) {
            obj = new JSONObject();
            ids = rs.getInt("id");
            obj.put("ID", ids);
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("PRESENTACION", rs.getString("presentacion"));
            obj.put("DIRECCION", rs.getString("direccion"));
            obj.put("POLITICAS", rs.getString("politicas"));

            String foto = rs.getString("foto_perfil") + "";
            String b64 = "";
            if (foto.length() > 0 && !foto.equals("null")) {
                b64 = URL.ruta_complejo_perfil + ids + URL.barra + foto;
            }
            obj.put("FOTO_PERFIL", b64);
            obj.put("B64", b64);
            obj.put("ID_USR", rs.getInt("id_usr"));
            obj.put("LAT", rs.getDouble("lat"));
            obj.put("LNG", rs.getDouble("lon"));
            obj.put("TELEFONOS", tel.todos_de_complejo(ids));
            obj.put("CORREOS", cor.todos_de_complejo(ids));
            obj.put("CARACTERISTICAS", car.todos_de_complejo(ids));
            obj.put("HORARIOS", hor.todos_de_complejo(ids));
            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONArray todos() throws SQLException, JSONException, IOException {
        String consulta = "select * from complejo";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONArray json = new JSONArray();
        TELEFONO tel = new TELEFONO(con);
        CARACTERISTICA car = new CARACTERISTICA(con);
        CORREO cor = new CORREO(con);
        HORARIO hor = new HORARIO(con);
        JSONObject obj;
        int ids;
        while (rs.next()) {
            obj = new JSONObject();
            ids = rs.getInt("id");
            obj.put("ID", ids);
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("PRESENTACION", rs.getString("presentacion"));
            obj.put("DIRECCION", rs.getString("direccion"));
            obj.put("POLITICAS", rs.getString("politicas"));

            String foto = rs.getString("foto_perfil") + "";
            String b64 = "";
            if (foto.length() > 0 && !foto.equals("null")) {
                b64 = URL.ruta_complejo_perfil + ids + URL.barra + foto;
            }
            obj.put("FOTO_PERFIL", b64);
            obj.put("B64", b64);
            obj.put("ID_USR", rs.getInt("id_usr"));
            obj.put("LAT", rs.getDouble("lat"));
            obj.put("LNG", rs.getDouble("lon"));
            obj.put("TELEFONOS", tel.todos_de_complejo(ids));
            obj.put("CORREOS", cor.todos_de_complejo(ids));
            obj.put("CARACTERISTICAS", car.todos_de_complejo(ids));
            obj.put("HORARIOS", hor.todos_de_complejo(ids));

            json.put(obj);
        }
        ps.close();
        rs.close();
        return json;
    }

    public JSONObject get_complejo_x_id(int id) throws SQLException, JSONException, IOException {
        String consulta = "select * from complejo where id"
                + "=" + id;
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        TELEFONO tel = new TELEFONO(con);
        CORREO cor = new CORREO(con);
        HORARIO hor = new HORARIO(con);
        FOTO_CARRUSEL fot = new FOTO_CARRUSEL(con);
        CARACTERISTICA car = new CARACTERISTICA(con);
        CANCHA can = new CANCHA(con);
        JSONObject obj = new JSONObject();
        int ids;
        if (rs.next()) {
            ids = rs.getInt("id");
            obj.put("ID", ids);
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("PRESENTACION", rs.getString("presentacion"));
            obj.put("DIRECCION", rs.getString("direccion"));
            obj.put("POLITICAS", rs.getString("politicas"));

            String foto = rs.getString("foto_perfil") + "";
            String b64 = "";
            if (foto.length() > 0 && !foto.equals("null")) {
                b64 = URL.ruta_complejo_perfil + ids + URL.barra + foto;
            }
            obj.put("FOTO_PERFIL", b64);
            obj.put("B64", b64);
            obj.put("ID_USR", rs.getInt("id_usr"));
            obj.put("LAT", rs.getDouble("lat"));
            obj.put("LNG", rs.getDouble("lon"));
            obj.put("TELEFONOS", tel.todos_de_complejo(ids));
            obj.put("CORREOS", cor.todos_de_complejo(ids));
            obj.put("HORARIOS", hor.todos_de_complejo(ids));
            obj.put("FOTOS_CARRUSEL", fot.todos_de_complejo(ids));
            obj.put("CARACTERISTICAS", car.todos_de_complejo(ids));
            obj.put("CANCHAS", can.todas_de_complejo(ids));
        }
        ps.close();
        rs.close();
        return obj;
    }

    public int get_cant_resul() throws SQLException, JSONException, IOException {
        String consulta = "select count(id) as cant from complejo";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        int resul = 0;
        if (rs.next()) {
            resul = rs.getInt("cant");
        }
        ps.close();
        rs.close();
        return resul;
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

    public String getPOLITICAS() {
        return POLITICAS;
    }

    public void setPOLITICAS(String POLITICAS) {
        this.POLITICAS = POLITICAS;
    }

    public String getPRESENTACION() {
        return PRESENTACION;
    }

    public void setPRESENTACION(String PRESENTACION) {
        this.PRESENTACION = PRESENTACION;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLON() {
        return LON;
    }

    public void setLON(double LON) {
        this.LON = LON;
    }

    public int getID_USR() {
        return ID_USR;
    }

    public void setID_USR(int ID_USR) {
        this.ID_USR = ID_USR;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public String getFOTO_PERFIL() {
        return FOTO_PERFIL;
    }

    public void setFOTO_PERFIL(String FOTO_PERFIL) {
        this.FOTO_PERFIL = FOTO_PERFIL;
    }

}
