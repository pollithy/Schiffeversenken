package logic;

import graphics.Controller;

public class GegnerischeMap extends Spielfeld {
	
	public GegnerischeMap() {
		super();
	}
	
	public void eintragen(int x, int y, boolean getroffen) {
		if(getroffen) {
			matrix[y][x] = Feld.TREFFER;
			treffer++;
		} else {
			matrix[y][x] = Feld.NIETE;
		}
	}
	
	public void paint(Controller contr) {
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				contr.paintGegnMap(matrix[y][x].getImage(), x * 10 + y);
			}
		}
	}
}
