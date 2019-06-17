package firmament.map;

import java.util.ArrayList;

import firmament.Firmament;
import firmament.mob.Actor;

public class Map {
	protected Cell[][] cells;
	protected Coord playerLoc;
	protected ArrayList<Actor> mobs = new ArrayList<Actor>();

	public static final int MAXMAPX = 100;
	public static final int MAXMAPY = 75;
	public static final double FILLPERCENT = .45;
	public static final double TREEPERCENT = .08;
	public static final int[] NW = {-1 , -1};
	public static final int[] NN = {-1 ,  0};
	public static final int[] NE = {-1 ,  1};
	public static final int[] EE = { 0 ,  1};
	public static final int[] WW = { 0 , -1};
	public static final int[] SW = { 1 , -1};
	public static final int[] SS = { 1 ,  0};
	public static final int[] SE = { 1 ,  1};
	public static final int[][] DIRS = {NW, NN, NE, EE, SE, SS, SW, WW};
	
	public static final int ACTION_NONE = -1;
	public static final int ACTION_WAIT = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_ATTACK = 2;
	public static final int ACTION_MIGRATE = 3;
	
	public static final int MOBFILLMIN = 40;
	public static final int MOBFILLMAX = 80;

	public Map(){
		cells = new Cell[MAXMAPY][MAXMAPX];
	}

	public Map(int x, int y){
		cells = new Cell[y][x];
	}

	public Map(Cell[][] grid){
		cells = grid;
	}

	/**
	 * Attempt to move the player. Write "bump" if target square is impassable
	 * @param n
	 * @param e
	 * @param s
	 * @param w
	 */
	public int movePlayer(boolean n, boolean e, boolean s, boolean w){
		int destX = playerLoc.x;
		int destY = playerLoc.y;
		if (n) destY--;
		if (s) destY++;
		if (w) destX--;
		if (e) destX++;
		if (!coordinatesAreInMap(destY, destX)){
			//System.out.println("bump");
			updateFov();
			return ACTION_NONE;
		}
		if (cells[destY][destX].passable()){
			cells[playerLoc.y][playerLoc.x].setActor(null);
			cells[playerLoc.y][playerLoc.x].unsee();
			playerLoc.x = destX;
			playerLoc.y = destY;
			cells[destY][destX].setActor(Actor.player);
			cells[destY][destX].see();
		} else {
			//System.out.println("bump");
			updateFov();
			return ACTION_NONE;
		}
		updateFov();
		return ACTION_MOVE;
	}

	/**
	 * update the player's field of vision by casting shadows.
	 */
	public void updateFov(){
		//clear current field of vision
		for (Cell[] ca : cells){
			for (Cell c : ca){
				c.unsee();
			}
		}

		//reveal the square the player stands on
		cells[playerLoc.y][playerLoc.x].see();
		
		//cardinal and ordinal directions first
		float i = 0;
		int trackY;
		int trackX;
		for (int[] dir : DIRS){
			trackY = playerLoc.y + dir[0];
			trackX = playerLoc.x + dir[1];
			i = 0;
			while (i < Actor.player.sight){
				if (!coordinatesAreInMap(trackY,trackX)){
					i = Actor.player.sight + 1;
					continue;
				}
				cells[trackY][trackX].see();

				if(cells[trackY][trackX].opaque()){
					i = Actor.player.sight + 1;
				}else if (isDiag(dir)){
					i += 1.5;
				} else i++;
				trackY += dir[0];
				trackX += dir[1];
			}
		}
		
		//fill in the 8 fields in between the main directions
		for (int[] dir : DIRS){
			trackY = playerLoc.y + dir[0] + dir[0];
			trackX = playerLoc.x + dir[1] + dir[1];
			int[] checkDir = getCheckDirection(dir);
			trackY += checkDir[0];
			trackX += checkDir[1];
			if (!coordinatesAreInMap(trackY, trackX)){
				continue;
			}
			int[] nextDir = nextDirClockwise(dir);
			if ((cells[playerLoc.y + dir[0]][playerLoc.x + dir[1]].opaque()) 
					&& (cells[playerLoc.y + nextDir[0]][playerLoc.x + nextDir[1]].opaque())){
				continue;
			}
			cells[trackY][trackX].see();
			
		}
	}
	
	public void updateFov(int i){
		int rise = 0;
		int run = 0;
	}	
	
	public int getVisionCircumference(){
		
		return Actor.player.sight;
	}
	
	public int[] nextDirClockwise(int[] dir){
		for(int i = 0; i <= 7; i++){
			if (dir == DIRS[i]){
				if (i == 7) return DIRS[0];
				else return DIRS[i + 1];
			}
		}
		int[] fail = {0,0};
		System.out.println("dir lookup failed");
		return fail;
	}
	
	//helper method for updateFov()
	public int[] getCheckDirection(int[] dir){
		if(dir == NW || dir == NN){
			return EE;
		}
		if(dir == NE || dir == EE){
			return SS;
		}
		if(dir == SE || dir == SS){
			return WW;
		}
		if(dir == SW || dir == WW){
			return NN;
		}
		int[] fail = {0,0};
		System.out.println("dir lookup failed");
		return fail;
	}
	
	public int getSlope(){
		return 0;
	}

	/**
	 * checks a direction coordinate pair for diagonality
	 * @param dir direction to check
	 * @return true if the dir is diagonal
	 */
	public boolean isDiag(int[] dir){
		if((dir[0]!= 0) && (dir[1]!= 0)){
			return true;
		}
		return false;
	}


	/**
	 * Get the # of passable neighbors of a given cell. Value returned will be incremented
	 * if the square in question is not passable.
	 * @param y y coord
	 * @param x x coord
	 * @return short from 0 to 9
	 */
	public short passableNeighborCount(int y, int x) {
		short n = 0;
		int newx = 0;
		int newy = 0;

		if (cells[y][x].passable() == true ){
			n = 0;
		} else n = 1;
		for (int[] dir : DIRS){
			newy = y + dir[0];
			newx = x + dir[1];
			if (coordinatesAreInMap(newy, newx) != true){
				n++;
			}
			else if (cells[newy][newx].passable() == false){
				n++;
			}
		}
		return n;
	}

	/**
	 * return a small map representing the squares that are displayed on
	 * the battle grid.
	 * This method is relative to the player.
	 * @return the grid to be displayed
	 */
	public Cell[][] buildVisibleGrid(){
		Cell[][] result = new Cell[Firmament.GRIDCELLSHEIGHT][Firmament.GRIDCELLSWIDTH];
		int resultX = playerLoc.x - Firmament.PLAYEROFFSETX;
		int resultY = playerLoc.y - Firmament.PLAYEROFFSETY;

		int maxX = resultX + Firmament.GRIDCELLSWIDTH;
		int maxY = resultY + Firmament.GRIDCELLSHEIGHT;
		int iterY = 0;
		int iterX = 0;
		for (int y = resultY; y < maxY; y++){
			for (int x = resultX; x < maxX; x++){
				if (coordinatesAreInMap(y, x)){
					result[iterY][iterX] = cells[y][x];
				} else result[iterY][iterX] = new Cell(Cell.TERRAIN_WALL);
				if (iterX == Firmament.GRIDCELLSWIDTH -1){
					iterX = 0;
					iterY++;
				}else iterX++;
			}
		}
		return result;
	}

	/**
	 * Get the passable cells in the map.
	 * @return an array of coordinates of the passable cells in this map
	 */
	public Coord[] freeCells(){
		int i = 0;
		Coord[] collector = new Coord[MAXMAPY * MAXMAPX];
		for (int y = 0; y < MAXMAPY; y++){
			for ( int x = 0; x < MAXMAPX; x++){
				if (cells[y][x].passable()){
					collector[i] = new Coord(y,x);
					i++;
				}
			}
		}
		Coord[] result = new Coord[i];
		for (int j = 0; j < i; j++){
			result[j] = collector [j];
		}
		return result;
	}

	/**
	 * Determine if a given coordinate is within the range of a maximum-size map.
	 * @param y y coord to test
	 * @param x x coord to test
	 * @return true -> in map false - > out of map
	 */
	public static boolean coordinatesAreInMap(int y, int x){
		if ((y < 0) || (x < 0) || (y >= MAXMAPY) || (x >= MAXMAPX)){
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
        String output = "\n";
		for(int y = 0; y < cells.length; y++){
			for(int x = 0; x < cells[0].length; x++){
				output = output + (cells[y][x].getAsciiSymbol());
			}output = output + ("\n");

		}
		return output.toString();
	}

	public void process() {
		// TODO move the monsters, update effects, etc
		for (Actor e : mobs){
			e.act();
		}
	}


}
