package firmament.mob;

import org.newdawn.slick.Image;

public class Actor {
	protected Image img;
	protected char symbol;
	public int sight;
	
	public static Actor player = new Player();

	public Actor(){
		symbol = '@';
	}
	
	public Image getImage(){
		return img;
	}
	
	public char getSymbol(){
		return symbol;
	}
	
	public void act(){

	}
}
