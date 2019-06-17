package firmament.mob;

import firmament.Firmament;

public class Goblin extends Actor {
	
	public static final int VIEWDISTANCE = 6;
	
	public Goblin(){
		super.img = Firmament.goblin;
		super.sight = VIEWDISTANCE;
		super.symbol = 'g';
	}
	
	@Override
	public void act(){
		if (Math.random() > .5){
			super.img = Firmament.goblin;
		}else super.img = Firmament.altar;
	}

}