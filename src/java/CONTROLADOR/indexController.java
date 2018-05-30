/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CONTROLADOR;

import Conexion.Conexion;
import MODELO.CANCHA;
import MODELO.COMENTARIO;
import MODELO.COMPLEJO;
import MODELO.CORREO;
import MODELO.COSTOS;
import MODELO.EVENTOS;
import MODELO.FOTO_CARRUSEL;
import MODELO.HORARIO;
import MODELO.RESERVA;
import MODELO.RESERVA_PA;
import MODELO.TELEFONO;
import MODELO.TIPO_CANCHA;
import MODELO.URL;
import MODELO.USUARIO;
import MODELO.USUARIO_PAGE;
import java.io.*;

import java.nio.file.*;
import java.sql.SQLException;
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
@WebServlet(name = "indexController", urlPatterns = {"/indexController"})
public class indexController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Conexion con = new Conexion(URL.db_usr, URL.db_pass); //conexion linux

            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            String evento = request.getParameter("evento");
            boolean retornar = true;
            String html = "";
            switch (evento) {
                case "get_complejos":
                    html = get_complejos(request, con);
                    break;
                case "get_complejos_id":
                    html = get_complejos_id(request, con);
                    break;
                case "get_canchas_comple":
                    html = get_canchas_comple(request, con);
                    break;
                case "get_horario_complejo":
                    html = get_horario_complejo(request, con);
                    break;
                case "get_dias_activos":
                    html = get_dias_activos(request, con);
                    break;
                case "get_precio_cancha":
                    html = get_precio_cancha(request, con);
                    break;
                case "login_facebook":
                    html = login_facebook(request, con);
                    break;
                case "ok_res_sin_targeta":
                    html = ok_res_sin_targeta(request, con);
                    break;
                case "get_reservas_can":
                    html = get_reservas_can(request, con);
                    break;
                case "get_mis_reservas":
                    html = get_mis_reservas(request, con);
                    break;
                case "comentar_complejo":
                    html = comentar_complejo(request, con);
                    break;
            }
            con.Close();
            if (retornar) {
                response.getWriter().write(html);
            }
        } catch (SQLException ex) {
            Logger.getLogger(indexController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (JSONException ex) {
            Logger.getLogger(indexController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(indexController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(indexController.class.getName()).log(Level.SEVERE, null, ex);
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

    private String get_complejos(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        COMPLEJO cp = new COMPLEJO(con);
        return cp.todos().toString();
    }

    private String get_complejos_id(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        COMPLEJO comple = new COMPLEJO(con);
        return comple.get_complejo_x_id(id).toString();
    }

    private String get_horario_complejo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HORARIO hor = new HORARIO(con);
        return hor.todos_de_complejo(id).toString();
    }

    private String get_precio_cancha(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        COSTOS cos = new COSTOS(con);
        return cos.todos_de_complejo(id).toString();
    }

    private String login_facebook(HttpServletRequest request, Conexion con) throws JSONException, SQLException {
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        USUARIO_PAGE usr = new USUARIO_PAGE(con);
        if (!usr.existe(id)) {
            usr.setID(id);
            usr.setNOMBRE(nombre);
            usr.Insertar();
        }
        return "exito";
    }

    private String ok_res_sin_targeta(HttpServletRequest request, Conexion con) throws JSONException, ParseException, SQLException {
        int id_cancha = Integer.parseInt(request.getParameter("id_can"));
        String id_usr = request.getParameter("id_usr");
        JSONArray arr = new JSONArray(request.getParameter("json"));
        JSONObject obj;
        String fecha;
        String hora;
        SimpleDateFormat form = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date fechafin;
        RESERVA res;
        int precio;
        RESERVA_PA respa = new RESERVA_PA(con);
        respa.setID_CANCHA(id_cancha);
        respa.setID_USUARIO(id_usr);
        respa.setESTADO(1);
        respa.setTIPO_PAGO(1);
        respa.setFECHA(new Date());
        int id_respa = respa.Insertar();
        for (int i = 0; i < arr.length(); i++) {
            obj = arr.getJSONObject(i);
            fecha = obj.getString("fecha");
            hora = obj.getString("hora");
            fechafin = form.parse(fecha + " " + hora);
            res = new RESERVA(con);
            res.setFECHA(fechafin);
            res.setCOSTO(obj.getInt("costo"));
            res.setID_RESPA(id_respa);
            res.Insertar();
        }
        return "EXITO";
    }

    private String get_reservas_can(HttpServletRequest request, Conexion con) throws SQLException, IOException, JSONException {
        int id_cancha = Integer.parseInt(request.getParameter("id_can"));
        String fe_ini = request.getParameter("fe_in");
        String fe_fin = request.getParameter("fe_fin");
        RESERVA res = new RESERVA(con);
        return res.get_res_can(id_cancha, fe_ini, fe_fin).toString();
    }

    private String get_canchas_comple(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id_cancha = Integer.parseInt(request.getParameter("id"));
        CANCHA can = new CANCHA(con);
        return can.todas_de_complejo(id_cancha).toString();
    }

    private String get_mis_reservas(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        String id = request.getParameter("id");
        RESERVA res = new RESERVA(con);
        return res.get_res_de_usuario(id).toString();
    }

    private String get_dias_activos(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
          int id = Integer.parseInt(request.getParameter("id"));
        COSTOS cos = new COSTOS(con);
        JSONArray arrdias=cos.dias_activos(id);
        JSONArray arrhroas=cos.horas(id);
        JSONArray arrcostos=cos.todos_de_complejo(id);
        String fe_ini = request.getParameter("fe_in");
        String fe_fin = request.getParameter("fe_fin");
        RESERVA res = new RESERVA(con);
        JSONArray arrreservas= res.get_res_can(id, fe_ini, fe_fin);
        JSONObject obj = new JSONObject();
        obj.put("dias", arrdias);
        obj.put("horas", arrhroas);
        obj.put("costos", arrcostos);
        obj.put("reservas", arrreservas);
        return obj.toString();
    }

    private String comentar_complejo(HttpServletRequest request, Conexion con) throws SQLException {
      int id_com=Integer.parseInt(request.getParameter("id_com"));
      int clasi=Integer.parseInt(request.getParameter("clasi"));
      String id_usr = request.getParameter("id_usr");
      String coment = request.getParameter("coment");
        COMENTARIO come = new COMENTARIO(con);
        come.setID_USR(id_usr);
        come.setID_COMPLEJO(id_com);
        come.setCOMENTARIO(coment);
        come.setCLASIFICACION(clasi);
       return come.Insertar()+"";
    }
}
