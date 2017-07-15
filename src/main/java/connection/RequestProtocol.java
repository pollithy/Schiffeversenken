package connection;

import java.io.Serializable;

/**
 * Diese Klasse stellt ein einfaches Protokoll für die Anfrage an den Gegner dar. Es werden jeweils
 * die beiden Koordinatenwerte eines Beschusses in einem Spielzug übermittelt und anschließend vom
 * Gegner ausgewertet.
 * 
 * @author Dennis Pollithy
 */

public class RequestProtocol implements Serializable {

	private static final long serialVersionUID = 8937037607309368084L;
	private int x;
	private int y;
	
	public RequestProtocol(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}