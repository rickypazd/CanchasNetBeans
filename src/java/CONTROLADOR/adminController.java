/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import MODELO.CANCHA;
import MODELO.CARACTERISTICA;
import MODELO.COMPLEJO;
import MODELO.CORREO;
import MODELO.COSTOS;
import MODELO.EVENTOS;
import MODELO.FOTO_CARRUSEL;
import MODELO.HORARIO;
import MODELO.RESERVA;
import MODELO.TELEFONO;
import MODELO.TIPO_CANCHA;
import MODELO.URL;
import MODELO.USUARIO;
import java.io.*;

import java.nio.file.*;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author RICKY
 */
@MultipartConfig
@WebServlet(name = "adminController", urlPatterns = {"/admin/adminController"})
public class adminController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Conexion con = new Conexion(URL.db_usr, URL.db_pass); //conexion linux

            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            String evento = request.getParameter("evento");
            boolean retornar = true;
            String html = "";
            switch (evento) {
                case "iniciar_session":
                    html = iniciar_session(request, con);
                    break;
                case "crear_usuario":
                    html = crear_usuario(request, con);
                    break;
                case "crear_cancha":
                    html = crear_cancha(request, con);
                    break;
                case "get_complejos_usr":
                    html = get_complejos_usr(request, con);
                    break;
                case "get_complejos_id":
                    html = get_complejos_id(request, con);
                    break;
                case "editar_cancha":
                    html = editar_cancha(request, con);
                    break;
                case "cargar_foto_perfil_complejo":
                    html = cargar_foto_perfil_complejo(request, con);
                    break;
                case "cargar_foto_carrusel_complejo":
                    html = cargar_foto_carrusel_complejo(request, con);
                    break;
                case "eliminar_foto_carrusel":
                    html = eliminar_foto_carrusel(request, con);
                    break;
                case "agg_tipo_cancha":
                    html = agg_tipo_cancha(request, con);
                    break;
                case "get_tipos_canchas":
                    html = get_tipos_canchas(request, con);
                    break;
                case "get_horario_complejo":
                    html = get_horario_complejo(request, con);
                    break;
                case "agg_cancha":
                    html = agg_cancha(request, con);
                    break;
                case "get_reservas_admin":
                    html = get_reservas_admin(request, con);
                    break;
                case "get_detalle_res":
                    html = get_detalle_res(request, con);
                    break;
                case "confirmar_reserva":
                    html = confirmar_reserva(request, con);
                    break;

            }
            con.Close();
            if (retornar) {
                response.getWriter().write(html);
            }
        } catch (SQLException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (JSONException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String iniciar_session(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {

        String usr = request.getParameter("usr");
        String pass = request.getParameter("pass");
        String passMd5 = DigestUtils.md5Hex(pass);
        USUARIO per = new USUARIO(con);
        per.setUSUARIO(usr);
        per.setPASSWORD(passMd5);

        JSONObject obj_usr = per.Buscar_por_usr_y_pass();
        if (obj_usr.length() <= 1) {
            return "error";
        }
        return obj_usr.toString();
    }

    private String crear_usuario(HttpServletRequest request, Conexion con) throws SQLException {
        /*      nombre:nombre,
                apellidos:apellidos,
                telefono:telefono,
                password:password,
                usuario:usuario,
                email:email */
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        String usuario = request.getParameter("usuario");
        String email = request.getParameter("email");
        USUARIO usr = new USUARIO(con);
        usr.setNOMBRE(nombre);
        usr.setAPELLIDO(apellidos);
        usr.setEMAIL(email);
        usr.setTELEFONO(telefono);
        usr.setUSUARIO(usuario);
        String passMd5 = DigestUtils.md5Hex(password);
        usr.setPASSWORD(passMd5);
        usr.setROL(2);
        int id = usr.Insertar();
        return "valido";
    }

    private String crear_cancha(HttpServletRequest request, Conexion con) throws JSONException, SQLException {
        String jso = request.getParameter("datos");
        JSONObject obj = new JSONObject(jso);
        COMPLEJO comple = new COMPLEJO(con);
        comple.setNOMBRE(request.getParameter("nombre"));
        comple.setDIRECCION(request.getParameter("direccion"));
        comple.setLAT(obj.getDouble("lat"));
        comple.setLON(obj.getDouble("lng"));
        comple.setPOLITICAS(request.getParameter("politicas"));
        comple.setPRESENTACION(request.getParameter("presentacion"));
        comple.setID_USR(Integer.parseInt(request.getParameter("id_usr")));
        int id_comp = comple.Insertar();
        JSONArray jso_tel = obj.getJSONArray("telefonos");
        JSONArray jso_corr = obj.getJSONArray("correos");
        JSONArray jso_horarios = obj.getJSONArray("horario");
        JSONArray jso_caracteristica = obj.getJSONArray("caracteristicas");
        TELEFONO telefono;
        JSONObject obje_tel;
        for (int i = 0; i < jso_tel.length(); i++) {
            obje_tel = jso_tel.getJSONObject(i);
            telefono = new TELEFONO(con);
            telefono.setID_COMPLEJO(id_comp);
            telefono.setTELEFONO(obje_tel.getString("tel"));
            telefono.Insertar();
        }
        CORREO correo;
        JSONObject obje_corr;
        for (int i = 0; i < jso_corr.length(); i++) {
            obje_corr = jso_corr.getJSONObject(i);
            correo = new CORREO(con);
            correo.setID_COMPLEJO(id_comp);
            correo.setCORREO(obje_corr.getString("corr"));
            correo.Insertar();
        }
        CARACTERISTICA caract;
        JSONObject obje_caract;
        for (int i = 0; i < jso_caracteristica.length(); i++) {
            obje_caract = jso_caracteristica.getJSONObject(i);
            caract = new CARACTERISTICA(con);
            caract.setID_COMPLEJO(id_comp);
            caract.setCARACTERISTICA(obje_caract.getString("caract"));
            caract.Insertar();
        }
        HORARIO horario;
        JSONObject obje_hora;
        for (int i = 0; i < jso_horarios.length(); i++) {
            obje_hora = jso_horarios.getJSONObject(i);
            horario = new HORARIO(con);
            horario.setID_COMPLEJO(id_comp);
            horario.setDIA(i);
            if (obje_hora.getString("act").equals("si")) {
                horario.setABIERTO(1);
            } else {
                horario.setABIERTO(0);
            }
            horario.setHORA_INI(obje_hora.getString("hora_ini"));
            horario.setHORA_FIN(obje_hora.getString("hora_fin"));
            horario.Insertar();
        }

        return id_comp + "";
    }

    private String get_complejos_usr(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        COMPLEJO comple = new COMPLEJO(con);
        return comple.todos_de_usr_con_datos(id).toString();
    }

    private String get_complejos_id(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        COMPLEJO comple = new COMPLEJO(con);
        return comple.get_complejo_x_id(id).toString();
    }

    private String editar_cancha(HttpServletRequest request, Conexion con) throws SQLException {

        COMPLEJO comple = new COMPLEJO(con);
        int id = Integer.parseInt(request.getParameter("id_cancha"));
        String nombre = request.getParameter("nombre");
        if (!nombre.equals("0")) {
            comple.update_nombre(id, nombre);
        }
        String presentacion = request.getParameter("presentacion");
        if (!presentacion.equals("0")) {
            comple.update_presentacion(id, presentacion);
        }
        String direccion = request.getParameter("direccion");
        if (!direccion.equals("0")) {
            comple.update_direccion(id, direccion);
        }
        String politicas = request.getParameter("politicas");
        if (!politicas.equals("0")) {
            comple.update_politicas(id, politicas);
        }

        try {
            String jso = request.getParameter("tele");
            JSONObject obj = new JSONObject(jso);
            JSONArray jso_tel = obj.getJSONArray("telefonos");
            TELEFONO telefono;
            JSONObject obje_tel;
            for (int i = 0; i < jso_tel.length(); i++) {
                obje_tel = jso_tel.getJSONObject(i);
                telefono = new TELEFONO(con);
                telefono.setID(obje_tel.getInt("id"));
                telefono.setTELEFONO(obje_tel.getString("tel"));
                telefono.update_telefono();
            }
        } catch (JSONException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String jso = request.getParameter("corre");
            JSONObject obj = new JSONObject(jso);
            JSONArray jso_corr = obj.getJSONArray("correos");
            CORREO correo;
            JSONObject obje_corr;
            for (int i = 0; i < jso_corr.length(); i++) {
                obje_corr = jso_corr.getJSONObject(i);
                correo = new CORREO(con);
                correo.setID(obje_corr.getInt("id"));
                correo.setCORREO(obje_corr.getString("corr"));
                correo.update_correo();
            }

        } catch (JSONException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String jso = request.getParameter("caracteristicas");
            JSONObject obj = new JSONObject(jso);
            JSONArray json_caract = obj.getJSONArray("caracteristicas");
            CARACTERISTICA caract;
            JSONObject obje_carac;
            for (int i = 0; i < json_caract.length(); i++) {
                obje_carac = json_caract.getJSONObject(i);
                caract = new CARACTERISTICA(con);
                caract.setID(obje_carac.getInt("id"));
                caract.setCARACTERISTICA(obje_carac.getString("caract"));
                caract.update_caracteristica();
            }

        } catch (JSONException ex) {
            Logger.getLogger(adminController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "EXITO";
    }

    private String cargar_foto_perfil_complejo(HttpServletRequest request, Conexion con) throws IOException, SQLException, ServletException {

        int id = Integer.parseInt(request.getParameter("id_complejo"));
        Part file = request.getPart("foto_perfil");
        String name = "";
        if (file != null) {
            name = file.getSubmittedFileName();
            String ruta = request.getSession().getServletContext().getRealPath("/");
            name = EVENTOS.guardar_file(file, ruta + URL.barra + URL.ruta_complejo_perfil + id, name);
        }
        COMPLEJO com = new COMPLEJO(con);
        com.setID(id);
        com.setFOTO_PERFIL(name);
        com.subir_foto();
        String b64 = URL.ruta_complejo_perfil + id + URL.barra + name;

        return b64;

    }

    private String cargar_foto_carrusel_complejo(HttpServletRequest request, Conexion con) throws IOException, SQLException, ServletException, JSONException {
        int id = Integer.parseInt(request.getParameter("id_complejo_c"));
        Part file = request.getPart("foto_carr");
        String name = "";
        if (file != null) {
            name = file.getSubmittedFileName();
            String ruta = request.getSession().getServletContext().getRealPath("/");
            name = EVENTOS.guardar_file(file, ruta + URL.barra + URL.ruta_complejo_carrusel + id, name);
        }
        FOTO_CARRUSEL com = new FOTO_CARRUSEL(con);
        com.setID_COMPLEJO(id);
        com.setFOTO(name);
        int id_foto = com.Insertar();
        String b64 = URL.ruta_complejo_carrusel + id + URL.barra + name;
        JSONObject obj = new JSONObject();
        obj.put("FOTO", b64);
        obj.put("ID", id_foto);
        return obj.toString();
    }

    private String eliminar_foto_carrusel(HttpServletRequest request, Conexion con) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        FOTO_CARRUSEL com = new FOTO_CARRUSEL(con);
        com.eliminar(id);
        return "exito";
    }

    private String agg_tipo_cancha(HttpServletRequest request, Conexion con) throws SQLException {
        String tipo = request.getParameter("tipo");
        TIPO_CANCHA tipo_c = new TIPO_CANCHA(con);
        tipo_c.setTIPO(tipo);
        int id = tipo_c.Insertar();
        return tipo;
    }

    private String get_tipos_canchas(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        TIPO_CANCHA tipo = new TIPO_CANCHA(con);
        return tipo.todos().toString();
    }

    private String get_horario_complejo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HORARIO hor = new HORARIO(con);
        return hor.todos_de_complejo(id).toString();
    }

    private String agg_cancha(HttpServletRequest request, Conexion con) throws JSONException, SQLException, ParseException {
        int id_complejo = Integer.parseInt(request.getParameter("id_complejo"));
        String nombre = request.getParameter("nombre");
        int tipo = Integer.parseInt(request.getParameter("tipo"));
        String json_costos = request.getParameter("costos");
        JSONArray arr = new JSONArray(json_costos);
        CANCHA canc = new CANCHA(con);
        canc.setID_COMPLEJO(id_complejo);
        canc.setNOMBRE(nombre);
        canc.setID_TIPO(tipo);
        int id_cancha = canc.Insertar();
        JSONObject obj;
        COSTOS cos;
        String hora;
        Time tim;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < arr.length(); i++) {
            obj = arr.getJSONObject(i);
            cos = new COSTOS(con);
            cos.setDIA(obj.getInt("dia"));
            cos.setPRECIO(obj.getInt("precio"));
            cos.setID_CANCHA(id_cancha);
            hora = obj.getString("hora");
            long ms = sdf.parse(hora).getTime();
            tim = new Time(ms);
            cos.setHORA(tim);
            cos.Insertar();
        }
        return "exito";
    }

    private String get_reservas_admin(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        RESERVA res = new RESERVA(con);
        return res.get_res_de_admin(id).toString();
    }

    private String get_detalle_res(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
       int id= Integer.parseInt(request.getParameter("id"));
        RESERVA res = new RESERVA(con);
        return res.get_res_detalle(id).toString();
    }

    private String confirmar_reserva(HttpServletRequest request, Conexion con) throws SQLException {
      int id = Integer.parseInt(request.getParameter("id"));
      RESERVA res = new RESERVA(con);
      return res.update(id);
    }
}
