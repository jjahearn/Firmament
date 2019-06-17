package firmament.map;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import firmament.Firmament;
import firmament.State;

public class EditState extends State{
	
	private Vault v;
	
	public EditState(){
		v = new Vault(1,1,1);
	}

	@Override
	public int process(Input inp, World w) {
		inp.disableKeyRepeat();
		// TODO Auto-generated method stub
		if (inp.isKeyDown(Input.KEY_ESCAPE)){
			return Firmament.STATE_MENU;
		}
		if (inp.isKeyDown(Input.KEY_ENTER)){
			w = new World();
		}
		if (inp.isKeyPressed(Input.KEY_DOWN)){
			v.setSize(v.colLength+1,v.rowLength);
		}
		if (inp.isKeyPressed(Input.KEY_RIGHT)){
			v.setSize(v.colLength,v.rowLength+1);
		}
		if (inp.isKeyPressed(Input.KEY_UP)){
			v.setSize(v.colLength-1,v.rowLength);
		}
		if (inp.isKeyPressed(Input.KEY_LEFT)){
			v.setSize(v.colLength,v.rowLength-1);
		}
		return Firmament.STATE_NIL;
	}
	
	@Override
	public void draw(Graphics g, World w) {
		g.drawString("vault X: " + v.rowLength + " vault Y : " + v.colLength + " array: " + v.arraySize(), 100, 0);
		

		Cell[][] grid = v.buildGrid();
		Cell c;

		for (int y = 0; y < Firmament.GRIDCELLSIZE * v.colLength; y += Firmament.GRIDCELLSIZE){
			for (int x = 0; x < Firmament.GRIDCELLSIZE * v.rowLength; x += Firmament.GRIDCELLSIZE){

				c = grid[y/Firmament.GRIDCELLSIZE][x/Firmament.GRIDCELLSIZE];
				//draw terrain
				
					if (c.getTerrainType() == Cell.TERRAIN_DOODAD){
						g.drawImage(c.getGroundImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
					}
					g.drawImage(c.getTerrainImage(), x, y,
							x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
				
				//draw ground effect

				//draw top item				

				//draw actor
				if(true){ //TODO place in visibility conditions
					if (c.getActor() != null) {
						g.drawImage(c.getActor().getImage(), x, y,
								x + Firmament.GRIDCELLSIZE, y + Firmament.GRIDCELLSIZE, 0, 0, 15, 15);
					}
				}

				//draw effect(s)
			}
		}
	}

}
