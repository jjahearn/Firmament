package firmament.map;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import firmament.Firmament;
import firmament.item.Item;
import firmament.mob.Actor;
import firmament.mob.Effect;

/**
 * The stored contents of one square of the map. The potential contents, from lowest to highest:
 *  terrain type ("wall" precludes other contents) (limit to one)
 *  cosmetic ground effect (limit to one)
 *  items (an arrayList)
 *  players or npcs (limit to one)
 *  spell effects & animations (an arrayList)
 *  exploration status (seen, unseen, unexplored)
 *  
 *  Additionally, Cell stores the images for its contents and returns a sorted
 *  array of the images visible in the cell
 */
public class Cell {

	// contents
	private int terrainType;
	private int groundType;
	private int groundEffect;
	private ArrayList<Item> items;
	private Actor actor;
	private ArrayList<Effect> effects;
	public boolean explored;
	public boolean visible;

	// stored images references
	private Image groundImage;
	private Image terrainImage;
	private Image groundEffectImage;
	private Image topItemImage;
	private Image actorImage;

	public static final int TERRAIN_GROUND = 0;
	public static final int TERRAIN_DOODAD = 1;
	public static final int TERRAIN_WALL = 2;
	public static final int GROUNDEFFECT_NONE = 0;

	/**
	 * create an empty, unexplored cell
	 */
	public Cell(){
		terrainType = TERRAIN_GROUND;
		groundEffect = GROUNDEFFECT_NONE;
		items = null;
		actor = null;
		effects = null;
		explored = false;
		visible = false;

	}

	/**
	 * create an unexplored cell with a given terrainType
	 * @param terrainType
	 */
	public Cell(int terr){
		this.terrainType = terr;
		groundEffect = GROUNDEFFECT_NONE;
		items = null;
		actor = null;
		effects = null;
		explored = false;
		visible = false;
	}
	
	
	/**
	 * sets the terrain image for the cell based on terrain type
	 */
	private void pickTerrainImage(){
		if (terrainType == TERRAIN_WALL){
			terrainImage = Firmament.wall;
		}else if (terrainType == TERRAIN_DOODAD){
			terrainImage = Firmament.tree;
		} else System.out.println("no terrainType, terrain " + terrainType);
	}

	private void pickGroundImage(){
			groundType = (int) (Math.random()*3);
		if(groundType == 0){
			groundImage = Firmament.grass;
		} else if (groundType == 1){
			groundImage = Firmament.grass1;
		} else if (groundType == 2){
			groundImage = Firmament.grass2;
		} else groundImage = Firmament.error;
	}


	/**
	 * @return true if the cell is passable
	 */
	public boolean passable(){
		return (actor == null && terrainType == TERRAIN_GROUND);
	}

	/**
	 * @return true if the cell cannot be seen through
	 */
	public boolean opaque(){
		return (terrainType == TERRAIN_WALL);
	}

	/**
	 * @param passable true: remove actor and set terrain to ground
	 * false: remove actor and set terrain to wall
	 */
	public void setPassable(boolean passable) {
		actor = null;

		if (passable){
			terrainType = TERRAIN_GROUND;
		} else terrainType = TERRAIN_WALL;
	}

	/**
	 * clear the terrain
	 */
	public void empty(){
		setTerrain(TERRAIN_GROUND);
	}

	public void setActor(Actor a){
		this.actor = a;
	}

	public Actor getActor(){
		return actor;
	}

	public void setTerrain(int terr){
		terrainType = terr;
	}

	public void setTerrainImage(Image img){
		terrainImage = img;
	}

	public void setGroundImage(Image img){
		groundImage = img;
	}

	public int getTerrainType(){
		return terrainType;
	}

	/**
	 * make the cell visible and mark it as explored
	 */
	public void see(){
		explored = true;
		visible = true;
	}

	/**
	 * make the cell non-visible
	 */
	public void unsee(){
		visible = false;
	}

	//	/**
	//	 * pass off the visible contents of the cell,
	//	 * lowest to highest at given screen coordinates
	//	 * 
	//	 * unexplored cells return null !!!!!
	//	 */
	//	public Image[] draw(){
	//		if (explored == false){ // returns null for unexplored cells
	//			return null;
	//		}
	//
	//		//count number of images to draw. we want to properly size the image[]
	//		int imageCount = 1; //(we are guaranteed to have a terrain)		
	//		if (groundEffect != GROUNDEFFECT_NONE){
	//			imageCount++;
	//		}
	//		if (items != null){
	//			imageCount++; //only render one item
	//		}
	//		if (actor != null){
	//			imageCount++;
	//		}
	//		if (effects != null){
	//			imageCount += effects.size(); //render all effects
	//		}
	//
	//		//compile the actual image array 
	//		Image[] images = new Image[imageCount];
	//		int i = 0;
	//		images[i] = terrainImage;
	//		i++;
	//		if (groundEffect != GROUNDEFFECT_NONE){
	//			images[i] = groundEffectImage;
	//			i++;
	//		}
	//		if (items != null){
	//			images[i] = topItemImage;
	//			i++;
	//		}
	//		if (actor != null){
	//			images[i] = actorImage;
	//			i++;
	//		}
	//		if(effects != null){
	//			for (Effect e : effects){
	//				images[i] = e.img;
	//			}
	//		}
	//
	//		return images;
	//	}

	public char getAsciiSymbol() {
		if (actor != null) return actor.getSymbol();
		switch(terrainType){
		case TERRAIN_GROUND : return '.';
		case TERRAIN_WALL : return '#';
		case TERRAIN_DOODAD : return 't';
		}
		return '?';
	}


	public Image getTerrainImage() {
		if (terrainType == TERRAIN_GROUND){
			return getGroundImage();
		}
		if (terrainImage == null){
			pickTerrainImage();
		}
		return terrainImage;
	}

	public Image getGroundImage(){
		if (groundImage == null){
			pickGroundImage();
		}
		return groundImage;
	}
}
