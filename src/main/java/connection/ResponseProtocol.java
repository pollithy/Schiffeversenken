package connection;

import java.io.Serializable;

/**
 * Diese Klasse stellt ein einfaches Protokoll für die Antwort des Gegners dar, der zurückliefert,
 * ob ein Schiff getroffen wurde.
 * 
 * @author Dennis Pollithy
 */

public class ResponseProtocol implements Serializable {

	private static final long serialVersionUID = 2942634479741780592L;
	private boolean getroffen;
	
	public ResponseProtocol(boolean getroffen) {
		this.getroffen = getroffen;
	}
	
	public boolean isGetroffen() {
		return getroffen;
	}
}
