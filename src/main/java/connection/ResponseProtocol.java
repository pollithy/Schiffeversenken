package connection;

import java.io.Serializable;

/**
 * Diese Klasse stellt ein einfaches Protokoll f�r die Antwort des Gegners dar, der zur�ckliefert,
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
