package logic;

import javafx.scene.image.Image;

public enum Feld {
	
	SCHIFF("/images/schiff.png"), TREFFER("/images/treffer.png"), 
	NIETE("/images/niete.png"), WASSER("/images/wasser.png");

		   private final Image image;

		   Feld(String path) { 
			   this.image = new Image(path); 
		   }
		   
		   public Image getImage() {
			   return image;
		   }
}
