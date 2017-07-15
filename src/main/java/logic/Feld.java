package logic;

import javafx.scene.image.Image;

public enum Feld {
	
	SCHIFF("resources/images/schiff.png"), TREFFER("logic/treffer.png"), NIETE("logic/niete.png"), WASSER("logic/wasser.png");

		   private final Image image;

		   Feld(String path) { 
			   this.image = new Image(path); 
		   }
		   
		   public Image getImage() {
			   return image;
		   }
}
