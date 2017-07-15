package logic;

public abstract class Spielfeld {

	protected Feld[][] matrix;
	protected int treffer;
	
	protected Spielfeld() {
		treffer = 0;
		matrix = new Feld[10][10];
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				matrix[x][y] = Feld.WASSER;
			}
		}
	}
	
	public boolean alleVersenkt() {
		return treffer == 15;
	}
}
