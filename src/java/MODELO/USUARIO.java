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

public class USUARIO {

    private int ID;
    private String USUARIO;
    private String PASSWORD;
    private String NOMBRE;
    private String APELLIDO;
    private String TELEFONO;
    private String EMAIL;
    private String CORREO;
    private int ROL;
    private String ID_FACE;

    private Conexion con = null;

    public USUARIO(Conexion con) {
        this.con = con;
    }

    public int Insertar() throws SQLException {
        String consulta = "INSERT INTO public.usuario_admin(\n"
                + "	 usuario, password, rol, nombre, apellidos, telefono, email)\n"
                + "	VALUES ( ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);

        ps.setString(1, getUSUARIO());
        ps.setString(2, getPASSWORD());
        ps.setInt(3, getROL());
        ps.setString(4, getNOMBRE());
        ps.setString(5, getAPELLIDO());
        ps.setString(6, getTELEFONO());
        ps.setString(7, getEMAIL());
        ps.execute();

        consulta = "select last_value from usuario_admin_id_seq ";
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

    public int Insertar_face() throws SQLException {
        String consulta = "INSERT INTO public.usuario(\n"
                + "	nombre,apellidos, id_face,  correo, telefono)\n"
                + "	VALUES (?, ?, ?, ?, ?);";
        PreparedStatement ps = con.statamet(consulta);
        ps.setString(1, getNOMBRE());
        ps.setString(2, getAPELLIDO());
        ps.setString(3, getID_FACE());
        ps.setString(4, getCORREO());
        ps.setString(5, getTELEFONO());

        ps.execute();
        consulta = "select last_value from usuario_id_seq ";
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

    public JSONObject Buscar_por_usr_y_pass() throws SQLException, JSONException {
        String consulta = "select * from public.usuario_admin where usuario='" + getUSUARIO() + "' and password='" + getPASSWORD() + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        int tipo = 0;
        int tipo_usr = 0;
        if (rs.next()) {
            tipo_usr = rs.getInt("rol");
            obj.put("ROL", tipo_usr);
            obj.put("PASSWORD", rs.getString("password"));
            obj.put("USUARIO", rs.getString("usuario"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("APELLIDO", rs.getString("apellidos"));
               obj.put("TELEFONO", rs.getString("telefono") != null ? rs.getString("telefono") : "");
            obj.put("EMAIL", rs.getString("email"));
            obj.put("ID", rs.getInt("id"));
        }
        ps.close();
        rs.close();
        return obj;
    }

    public JSONObject getCliente_por_usr_y_pass(String usr, String pass) throws SQLException, JSONException {
        String consulta = "select * from public.usuario where usuario='" + getUSUARIO() + "' and password='" + getPASSWORD() + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        int tipo = 0;
        int tipo_usr = 0;
        if (rs.next()) {
            obj.put("PASSWORD", rs.getString("password"));
            obj.put("USUARIO", rs.getString("usuario"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("APELLIDO", rs.getString("apellidos"));
            obj.put("FOTO_PEFIL", rs.getString("foto_perfil"));
            obj.put("ID", rs.getInt("id"));
            obj.put("ID_FACE", rs.getString("id_face"));
            obj.put("exito", "si");
        } else {
            obj.put("exito", "no");
        }
        ps.close();
        rs.close();
        return obj;
    }

    public JSONObject getClienteFace(String id) throws SQLException, JSONException {
        String consulta = "select * from public.usuario where id_face='" + id + "'";
        PreparedStatement ps = con.statamet(consulta);
        ResultSet rs = ps.executeQuery();
        JSONObject obj = new JSONObject();
        int tipo = 0;
        int tipo_usr = 0;
        if (rs.next()) {
            obj.put("PASSWORD", rs.getString("password"));
            obj.put("USUARIO", rs.getString("usuario"));
            obj.put("NOMBRE", rs.getString("nombre"));
            obj.put("APELLIDO", rs.getString("apellidos"));
            obj.put("FOTO_PEFIL", rs.getString("foto_perfil"));
            obj.put("TELEFONO", rs.getString("telefono") != null ? rs.getString("telefono") : "");
            obj.put("ID", rs.getInt("id"));
            obj.put("ID_FACE", rs.getString("id_face"));
            obj.put("exito", "si");
        } else {
            obj.put("exito", "no");
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

    public String getUSUARIO() {
        return USUARIO;
    }

    public void setUSUARIO(String USUARIO) {
        this.USUARIO = USUARIO;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public int getROL() {
        return ROL;
    }

    public void setROL(int ROL) {
        this.ROL = ROL;
    }

    public Conexion getCon() {
        return con;
    }

    public void setCon(Conexion con) {
        this.con = con;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getAPELLIDO() {
        return APELLIDO;
    }

    public void setAPELLIDO(String APELLIDO) {
        this.APELLIDO = APELLIDO;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getID_FACE() {
        return ID_FACE;
    }

    public void setID_FACE(String ID_FACE) {
        this.ID_FACE = ID_FACE;
    }

}
