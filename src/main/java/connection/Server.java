package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import logic.Schiffeversenken;

/**
 * Klasse zur Repräsentation der Server-Seite der Verbindung.
 * 
 * @author Dennis Pollithy
 */
public class Server extends Peer {

	private final ServerSocket server; 
	
    public Server(Schiffeversenken ss) throws IOException { 
    	super(ss);
        server = new ServerSocket(21235);
        socket = null;
    } 
    
    /**
	 * Methode zum Erzeugen einer Socket, sobald der Client am abgehörten Port verbindet.
	 * Die IO-Streams zum Datenaustausch werden nach der erfolgreichen Verbindung erzeugt.
	 */
    public void connect() {
    	try {
    		while(socket == null) {
    			socket = server.accept();
    			try {
    				out = new ObjectOutputStream(socket.getOutputStream());
    				in = new ObjectInputStream(socket.getInputStream());
    			} catch (IOException e) {
    				connect();
    			}
    			ss.verbindungHergestellt();
    		}
    	} catch (IOException e) {
    		System.err.println("IO-Exception");
    		e.printStackTrace();
    	}
    }
    
    /**
	 * Trennt die Verbindung zum anderen Peer
	 */
	public void disconnect() {
		if (socket != null) {
            try { 
                socket.close();
                server.close();
            } catch (IOException e) { 
                disconnect();
            }
    	}
	}
}
