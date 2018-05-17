/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import Conexion.Conexion;
import MODELO.RESERVA_PA;
import MODELO.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardopazdemiquel
 */
public class hilo extends Thread{
    private boolean corriendo =true;
    private  Conexion con;    
    private  Calendar cal;
    private RESERVA_PA respa;
    private SimpleDateFormat form;
    public void run(){
        while(corriendo){
            try {
                if(con==null){
                    con= new Conexion(URL.db_usr, URL.db_pass);
                }
                respa = new RESERVA_PA(con);
                 cal = Calendar.getInstance();
                 cal.add(Calendar.HOUR, -3);    
                form= new SimpleDateFormat("yyyy-MM-dd HH:mm");
                respa.Cancelar_Reservas_menores_fecha(form.format(cal.getTime()));
                Thread.sleep(1000*60);
            } catch (InterruptedException ex) {
                Logger.getLogger(hilo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(hilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    public boolean isCorriendo() {
        return corriendo;
    }

    public void setCorriendo(boolean corriendo) {
        this.corriendo = corriendo;
    }
    
    
}
