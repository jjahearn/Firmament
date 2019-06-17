package firmament;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import firmament.map.Cell;
import firmament.map.Map;
import firmament.map.World;

/**
 * This is the state that would be at the "bottom" of the state stack
 * it handles the gameplay experience when you are not in a menu or other special mode
 */
public class BaseState extends State{

	/*
	 * actionDelay and hasDelayed are used to ensure that one button press makes one 
	 * discrete action, unless the user is clearly holding down the key
	 */
	public int actionDelay; 
	public boolean hasDelayed;

	public BaseState(){
		actionDelay = 0;
		hasDelayed = false;
	}

	@Override
	public int process(Input inp, World world) {

		int actionType = Map.ACTION_NONE;
		
		if (inp.isKeyDown(Input.KEY_ESCAPE)){
			return Firmament.STATE_MENU;
		}
		
		//logic to process the ordinal directions properly
		boolean w = inp.isKeyDown(Input.KEY_LEFT) || inp.isKeyDown(Input.KEY_NUMPAD4) || inp.isKeyDown(Input.KEY_NUMPAD7) || inp.isKeyDown(Input.KEY_NUMPAD1);
		boolean e = inp.isKeyDown(Input.KEY_RIGHT) || inp.isKeyDown(Input.KEY_NUMPAD6) || inp.isKeyDown(Input.KEY_NUMPAD9) || inp.isKeyDown(Input.KEY_NUMPAD3);
		boolean n = inp.isKeyDown(Input.KEY_UP) || inp.isKeyDown(Input.KEY_NUMPAD8) || inp.isKeyDown(Input.KEY_NUMPAD7) || inp.isKeyDown(Input.KEY_NUMPAD9);
		boolean s = inp.isKeyDown(Input.KEY_DOWN) || inp.isKeyDown(Input.KEY_NUMPAD2) || inp.isKeyDown(Input.KEY_NUMPAD1) || inp.isKeyDown(Input.KEY_NUMPAD3);
		boolean wait = inp.isKeyDown(Input.KEY_NUMPAD5);
		
		if (actionDelay > 0) {
			if(!(n || e || s || w || wait)){
				actionDelay = 0;
				hasDelayed = false;
			} 
			actionDelay --;
			if (actionDelay == 1) {
				hasDelayed = true;
			}
			return Firmament.STATE_NIL;
		}

		if (n || e || s || w || wait){ //if there is any input
			if (wait){
				actionType = Map.ACTION_WAIT;
			} else if (!(n && s)  && !(e && w)){
				actionType = world.cm.movePlayer(n, e, s, w);
				//System.out.println("turn: " + turnCount);
			}

			if (hasDelayed == false){
				actionDelay = Firmament.ACTIONDELAYMAX;
			} else actionDelay = Firmament.ACTIONDELAYMIN;
		}
		
		if (actionType != Map.ACTION_NONE){
			world.cm.process();
			world.turnCount++;
		}

		return Firmament.STATE_NIL;
	}

	/**
	 * draw the visible parts of the map, piece by piece
	 */
	@Override
	public void draw(Graphics g, World world) {

		g.drawString(("turn: " + world.turnCount), 0, Firmament.GRIDPIXELHEIGHT);

		Cell[][] grid = world.cm.buildVisibleGrid();
		Cell c;

		for (int y = 0; y < Firmament.GRIDPIXELHEIGHT; y += Firmament.GRIDCELLSIZE){
			for (int x = 0; x < Firmament.GRIDPIXELWIDTH; x += Firmament.GRIDCELLSIZE){

				c = grid[y/Firmament.GRIDCELLSIZE][x/Firmament.GRIDCELLSIZE];
				if (!c.explored) continue;

				//draw terrain
				if(c.visible){
					if (c.getTerrainType() == Cell.TERRAIN_DOODAD){
						g.drawImage(c.getGroundImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
					}
					g.drawImage(c.getTerrainImage(), x, y,
							x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
					if (c.getActor() != null) {
						g.drawImage(c.getActor().getImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
					}
				}else{
					if (c.getTerrainType() == Cell.TERRAIN_DOODAD){
						g.drawImage(c.getGroundImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15, Color.gray);
					}
					g.drawImage(c.getTerrainImage(), x, y,
							x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15, Color.gray);
					if (c.getActor() != null) {
						g.drawImage(c.getActor().getImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15, Color.gray);
					}
				}
				//draw ground effect

				//draw top item				

				//draw actor
				

				//draw effect(s)
			}
		}
		//TODO minimap, text logs, health, control hints, etc
	}

}
