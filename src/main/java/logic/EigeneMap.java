package logic;

import java.util.Random;

import graphics.Controller;

public class EigeneMap extends Spielfeld {
	
	public EigeneMap() {
		super();
	}
	
	public boolean istGetroffen(int x, int y) {
		if(matrix[x][y] == Feld.SCHIFF) {
			matrix[x][y] = Feld.TREFFER;
			treffer++;
			return true;
		} else {
			matrix[x][y] = Feld.NIETE;
			return false;
		}
	}
	
	public boolean bereitsBeschossen(int x, int y) {
		if(matrix[x][y] == Feld.TREFFER) {
			return true;
		} else if(matrix[x][y] == Feld.NIETE){
			return true;
		} else {
			return false;
		}
	}
	
	public void schiffeSetzenAuto() {
		for(int i=5; i>=1; i--){
			SchiffSetzen(i);
		}
	}
	public void SchiffSetzen(int Laenge){
		Aussen:
		while(true){
			int x0 = new Random().nextInt(10);
			int y0 = new Random().nextInt(10);
			int richtung = new Random().nextInt(4);
			
			switch(richtung){
			case 0:
				for(int x=x0; x<x0+Laenge; x++){
					if(!inbounds(x, y0))
						continue Aussen;
				}
				for(int x=x0-1; x<x0+Laenge+1; x++){
					for(int y=y0-1; y<=y0+1; y++){
						if(inbounds(x, y) && matrix[x][y]==Feld.SCHIFF)
							continue Aussen;
					}
				}
				for(int x=x0; x<x0+Laenge; x++){
					matrix[x][y0] = Feld.SCHIFF;
				}
				return;
			case 1:
				for(int x=x0; x>x0-Laenge; x--){
					if(!inbounds(x, y0))
						continue Aussen;
				}
				for(int x=x0+1; x>x0-Laenge-1; x--){
					for(int y=y0-1; y<=y0+1; y++){
						if(inbounds(x, y) && matrix[x][y]==Feld.SCHIFF)
							continue Aussen;
					}
				}
				for(int x=x0; x>x0-Laenge; x--){
					matrix[x][y0] = Feld.SCHIFF;
				}
				return;
			case 2:
				for(int y=y0; y<y0+Laenge; y++){
					if(!inbounds(x0, y))
						continue Aussen;
				}
				for(int y=y0-1; y<y0+Laenge+1; y++){
					for(int x=x0-1; x<=x0+1; x++){
						if(inbounds(x, y) && matrix[x][y]==Feld.SCHIFF)
							continue Aussen;
					}
				}
				for(int y=y0; y<y0+Laenge; y++){
					matrix[x0][y] = Feld.SCHIFF;
				}
				return;
			case 3:
				for(int y=y0; y>y0-Laenge; y--){
					if(!inbounds(x0, y))
						continue Aussen;
				}
				for(int y=y0+1; y>y0-Laenge-1; y--){
					for(int x=x0-1; x<=x0+1; x++){
						if(inbounds(x, y) && matrix[x][y]==Feld.SCHIFF)
							continue Aussen;
					}
				}
				for(int y=y0; y>y0-Laenge; y--){
					matrix[x0][y] = Feld.SCHIFF;
				}
				return;

			}
			break;
		}
		
	}
	
	private boolean inbounds(int x, int y){
		return x>=0 && x<10 && y>=0 && y<10;
	}
	
	public void paint(Controller contr) {
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				contr.paintEigeneMap(matrix[x][y].getImage(), x * 10 + y);
			}
		}
	}
}
