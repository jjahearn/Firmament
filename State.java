package firmament;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import firmament.map.World;

public abstract class State {
	
	public abstract int process(Input inp, World w);
	
	public abstract void draw(Graphics g, World w);
}
