package firmament.mob;

import firmament.Firmament;

public class Player extends Actor {
	
	public static final int VIEWDISTANCE = 6;
	
	public Player(){
		super.img = Firmament.player;
		super.sight = VIEWDISTANCE;
		super.symbol = '@';
	}
	
	

}
