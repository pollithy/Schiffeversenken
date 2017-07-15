package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import logic.Schiffeversenken;

/**
 * Klasse zur Repr‰sentation der Client-Seite der Verbindung.
 * 
 * @author Dennis Pollithy
 */

public class Client extends Peer {
	private final String ipString;
	
	public Client(Schiffeversenken ss, String ipString) {
		super(ss);
		this.ipString = ipString;
    } 
	
	/**
	 * Methode zum Verbinden des Clients an den Server mittels der vom Benutzer eingegebenen
	 * IP-Adresse und des vorgegeben Ports. Die IO-Streams zum Datenaustausch werden nach der
	 * erfolgreichen Verbindung erzeugt.
	 * Anschlieﬂend werden in der Hauptklasse durch den Aufruf der Methode verbindungHergestellt() 
	 * die auf der Verbindung aufbauenden Schritte eingeleitet.
	 */
	public void connect() {
		try { 
            socket = new Socket(InetAddress.getByName(ipString), 21235);
            try {
            	out = new ObjectOutputStream(socket.getOutputStream());
    			in = new ObjectInputStream(socket.getInputStream());
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            ss.verbindungHergestellt();
        } catch (UnknownHostException e) { 
            System.err.println("Unbekannter Server");
        } catch (IOException e) { 
            System.err.println("IO-Probleme..."); 
            this.disconnect();
            ss.verbindungHerstellen(ipString);
        }
	}
	
	/**
	 * Trennt die Verbindung zum anderen Peer
	 */
	public void disconnect() {
		if (socket != null) {
            try { 
                socket.close();
            } catch (IOException e) { 
                disconnect();
            }
    	}
	}
}