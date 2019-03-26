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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
@WebServlet(name = "androidController", urlPatterns = {"/androidController"})
public class androidController extends HttpServlet {

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
                case "realizar_reserva":
                    html = realizar_reserva(request, con);
                    break;
                case "get_dia_con_reservas":
                    html = get_dia_con_reservas(request, con);
                    break;
                case "get_dia_con_reservas_tipo":
                    html = get_dia_con_reservas_tipo(request, con);
                    break;
                case "get_reservas_usuario":
                    html = get_reservas_usuario(request, con);
                    break;
                case "get_reservas_usuario_proximas":
                    html = get_reservas_usuario_proximas(request, con);
                    break;
                case "get_complejos_fecha_hora":
                    html = get_complejos_fecha_hora(request, con);
                    break;
                case "get_tipos_canchas_de_complejo":
                    html = get_tipos_canchas_de_complejo(request, con);
                    break;
                case "get_comentarios":
                    html = get_comentarios(request, con);
                    break;
                case "ok_res_sin_tarjeta":
                    html = ok_res_sin_tarjeta(request, con);
                    break;

            }
            con.Close();
            if (retornar) {
                response.getWriter().write(html);
            }
        } catch (SQLException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (JSONException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
        } catch (IOException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("falso");
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
        return cp.todos_android().toString();
    }

    private String realizar_reserva(HttpServletRequest request, Conexion con) {
        con.Transacction();
        try {
            int id_cancha = Integer.parseInt(request.getParameter("id_cancha"));
            int id_usr = Integer.parseInt(request.getParameter("id_usr"));
            RESERVA_PA reserva_pa = new RESERVA_PA(con);
            reserva_pa.setESTADO(1);
            reserva_pa.setID_CANCHA(id_cancha);
            reserva_pa.setID_USUARIO(id_usr);
            reserva_pa.setTIPO_PAGO(1);
            reserva_pa.setFECHA(new Date());
            int id_reserva_pa = reserva_pa.Insertar();
            JSONArray horas = new JSONArray(request.getParameter("arr"));
            String fecha = request.getParameter("fecha");
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            RESERVA reserva;
            JSONObject obj;
            for (int i = 0; i < horas.length(); i++) {
                obj = horas.getJSONObject(i);
                reserva = new RESERVA(con);
                reserva.setID_RESPA(id_reserva_pa);
                reserva.setCOSTO(obj.getInt("PRECIO"));
                reserva.setFECHA(form.parse(fecha + " " + obj.getString("HORA")));
                reserva.Insertar();
            }
            con.commit();
            return "exito";
        } catch (SQLException ex) {
            con.rollback();
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            return "falso";
        } catch (JSONException ex) {
            con.rollback();
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            return "falso";
        } catch (ParseException ex) {
            con.rollback();
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            return "falso";
        }
    }

    private String get_dia_con_reservas(HttpServletRequest request, Conexion con) throws SQLException, IOException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        int dia = Integer.parseInt(request.getParameter("dia"));
        String fecha = request.getParameter("fecha");
        COSTOS cos = new COSTOS(con);
        JSONArray arrcostos = cos.horas_mas_estado(id, dia, fecha);
        return arrcostos.toString();
    }

    private String get_dia_con_reservas_tipo(HttpServletRequest request, Conexion con) throws SQLException, IOException, JSONException {
        int id_complejo = Integer.parseInt(request.getParameter("id_complejo"));
        int dia = Integer.parseInt(request.getParameter("dia"));
        int id_tipo = Integer.parseInt(request.getParameter("id_tipo"));
        String fecha = request.getParameter("fecha");
        COSTOS cos = new COSTOS(con);
        JSONArray arrcostos = cos.canchas_disponibles(id_complejo, id_tipo, dia, fecha);
        return arrcostos.toString();
    }

    private String get_reservas_usuario(HttpServletRequest request, Conexion con) throws SQLException, IOException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        RESERVA_PA reserva_pa = new RESERVA_PA(con);
        JSONArray arrcostos = reserva_pa.get_reservas_usuario(id);
        return arrcostos.toString();
    }

    private String get_reservas_usuario_proximas(HttpServletRequest request, Conexion con) throws SQLException, IOException, JSONException {
        int id = Integer.parseInt(request.getParameter("id"));
        RESERVA_PA reserva_pa = new RESERVA_PA(con);
        JSONArray arrcostos = reserva_pa.get_reservas_usuario_proximas(id);
        return arrcostos.toString();
    }

    private String get_complejos_fecha_hora(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        try {
            String fecha = request.getParameter("fecha");
            String hora = request.getParameter("hora");
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date ff = form.parse(fecha + " " + hora);
            Calendar cal = Calendar.getInstance();
            cal.setTime(ff);
            int dia = cal.get(Calendar.DAY_OF_WEEK) - 2;
            if (dia <= 0) {
                dia = 6;
            }
            SimpleDateFormat form2 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat form3 = new SimpleDateFormat("HH:mm:ss");
            String fechafinal = form2.format(ff);
            String hora_inicio = form3.format(cal.getTime());
            cal.add(Calendar.HOUR_OF_DAY, 1);
            String hora_fin = form3.format(cal.getTime());
            COMPLEJO comple = new COMPLEJO(con);

            return comple.get_complejos_filtro_fecha(dia, hora_inicio, hora_fin, fechafinal).toString();
        } catch (ParseException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            return "Error al convertir fecha y hora.";
        }
    }

    private String get_tipos_canchas_de_complejo(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id_complejo = Integer.parseInt(request.getParameter("id_complejo"));
        TIPO_CANCHA tc = new TIPO_CANCHA(con);
        return tc.todos_complejo(id_complejo).toString();
    }

    private String get_comentarios(HttpServletRequest request, Conexion con) throws SQLException, JSONException, IOException {
        int id_complejo = Integer.parseInt(request.getParameter("id_complejo"));
        COMENTARIO comentario = new COMENTARIO(con);
        return comentario.todos_de_complejo(id_complejo).toString();
    }

    private String ok_res_sin_tarjeta(HttpServletRequest request, Conexion con){
        try {
            con.Transacction();
            int id_usr = Integer.parseInt(request.getParameter("id_usr"));
            JSONArray arr = new JSONArray(request.getParameter("arr"));
            String fechaStr = request.getParameter("fecha");
            HashMap<Integer, RESERVA_PA> map = new HashMap<>();
            JSONObject obj;
            String fecha;
            String hora;
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fechafin;
            RESERVA res;
            int precio;
            RESERVA_PA respa = null;
            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);
                if (map.containsKey(obj.getInt("id_cancha"))) {
                    respa = map.get(obj.getInt("id_cancha"));
                } else {
                    respa = new RESERVA_PA(con);
                    respa.setID_CANCHA(obj.getInt("id_cancha"));
                    respa.setID_USUARIO(id_usr);
                    respa.setESTADO(1);
                    respa.setTIPO_PAGO(1);
                    respa.setFECHA(new Date());
                    int id_respa = respa.Insertar();
                    respa.setID(id_respa);
                    map.put(respa.getID_CANCHA(), respa);
                }
                hora = obj.getString("hora");
                fechafin = form.parse(fechaStr + " " + hora);
                res = new RESERVA(con);
                res.setFECHA(fechafin);
                res.setCOSTO(obj.getInt("precio"));
                res.setID_RESPA(respa.getID());
                res.Insertar();
                
            }
            con.commit();
            return "EXITO";
        } catch (JSONException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
            return "falso";
        } catch (ParseException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
            return "falso";
        } catch (SQLException ex) {
            Logger.getLogger(androidController.class.getName()).log(Level.SEVERE, null, ex);
            con.rollback();
            return "falso";
        }
    }

}
