package MODELO;

import java.io.*;
import javax.servlet.http.Part;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author RICKY
 */
public class EVENTOS {

    public static String guardar_file(Part file, String url, String nombre) throws IOException {
        int dbs = 2048;
        File d = new File(url);
        if (!d.exists()) {
            d.mkdirs();
        }
        File f = new File(url +URL.barra+ nombre);
        int contador = 0;
        while (f.exists()) {
            contador++;
            nombre = contador + nombre;
            f = new File(url +URL.barra+ nombre);
        }
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(file.getInputStream(), dbs);
            out = new BufferedOutputStream(new FileOutputStream(f), dbs);
            byte[] bufer = new byte[dbs];
            for (int i = 0; ((i = in.read(bufer)) > 0);) {
                out.write(bufer, 0, i);
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                try {
                    in.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
        return nombre;
    }

    public static String obtener_file(String url, String nombre) throws IOException {
        File file = new File(url +URL.barra+ nombre);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);

            encodedBase64 = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            System.out.println(e);
        }

        return encodedBase64;
    }

    public static String obtener_file_cuadrado(String url, String nombre) throws IOException {
        File file = new File(url +URL.barra+ nombre);
        String encodedBase64 = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
          
            encodedBase64 = new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            System.out.println(e);
        }

        return encodedBase64;
    }
    
}
