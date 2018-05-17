/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author ricardopazdemiquel
 */
public class Iniciador implements ServletContextListener{

    private hilo hilo;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.hilo = new hilo();
        hilo.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    
       this.hilo.setCorriendo(true);
       
    }
    
}
