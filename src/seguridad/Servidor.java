package seguridad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author Nicoyarce
 */
public class Servidor {
    private static int PUERTO = 7777;
    private static String keyStore = "C:\\Users\\Nicoyarce\\llavesServidor.jks";
    private static String trustStore = "C:\\Users\\Nicoyarce\\truststore_servidor.jks";
    private static char[] pass ="holas1".toCharArray(); 
    
    public static SSLServerSocket crearServerSocket(int puerto) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStore),pass);
        
        KeyStore ts = KeyStore.getInstance("JKS");           
	ts.load(new FileInputStream(trustStore),pass);	
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); //SunX509
        kmf.init(ks,pass); //usar solo una password para todas las llaves  
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	tmf.init(ts);
        
        
        SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
	sslcontext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),null);
	ServerSocketFactory factory= sslcontext.getServerSocketFactory();
        SSLServerSocket server = (SSLServerSocket)factory.createServerSocket(PUERTO);  
	server.setEnabledCipherSuites(server.getSupportedCipherSuites());	
        return server;
    }

    public static void main(String args[]) {
        BufferedReader entrada;
        PrintWriter salida;
        SSLServerSocket server;
        ArrayList<String> IPs = new ArrayList<>();
        try {            
            // creamos server socket con SSL
            server = crearServerSocket(PUERTO);
            server.setNeedClientAuth(true);
            boolean stop = false;
            while (!stop) {
                System.out.println("Esperando una conexión...");
                SSLSocket client = (SSLSocket) server.accept();
                //client.setEnabledCipherSuites(client.getSupportedCipherSuites());
                SSLSession session = client.getSession();
                Certificate[] cchain2 = session.getLocalCertificates();
                System.out.println("Un cliente se ha conectado...");
                System.out.println("Se presento al cliente con la siguiente identificacion");
                for (int i = 0; i < cchain2.length; i++) {
                    System.out.println(((X509Certificate) cchain2[i]).getSubjectDN());
                }
                //Se agrega ip de cliente a arrayList
                String ip = client.getInetAddress().getHostAddress();
                IPs.add(ip);
                System.out.println("IP del cliente: " + ip);
                System.out.println("Algoritmo de criptografia: " + session.getCipherSuite());
                System.out.println("Version SSL: " + session.getProtocol());

                // Se crean canales de entrada y salida de datos 
                entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
                salida = new PrintWriter(client.getOutputStream(), true);

                // Se concatena la hora de recibo
                String str = entrada.readLine();
                System.out.println("El cliente envió: " + str);
                str = str.toUpperCase();
                str = str.concat(" - Recibido a las: " + obtenerHora());
                salida.println(str);
                client.close();
            }//while
            // Cerrando la conexión
            server.close();
        } catch (Exception e) {
            System.out.println("Error de entrada/salida." + e.getLocalizedMessage());
        } 
    }

    public static String obtenerHora() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
