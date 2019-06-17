package firmament.map;

import firmament.Firmament;
import firmament.mob.Actor;
import firmament.mob.Goblin;



/*
 * Generate a map of total random features
 */
public class MapGen {
	public static Map randMap(Map m){
		for(int y = 0; y < m.cells.length; y++){
			for(int x = 0; x < m.cells[0].length; x++){
				//System.out.println("now testing "+ i +" " + j);
				if (Firmament.rand.nextFloat()  > Map.FILLPERCENT){
					m.cells[y][x] = new Cell(Cell.TERRAIN_GROUND);

				}
				else m.cells[y][x] = new Cell(Cell.TERRAIN_WALL);
			}

		}
		return m;
	}

	/*
	 * Take a map and shape it using cellular automata
	 */
	public static Map caveMap(Map m){
		short n = 0;
		boolean[][] change = new boolean[Map.MAXMAPY][Map.MAXMAPX];	
		for (int y = 0; y < Map.MAXMAPY; y++){
			for (int x = 0; x < Map.MAXMAPX; x++){
				n = m.passableNeighborCount(y,x);
				if (n >= 5 || n == 0 ){
					change[y][x] = false;
				} else change[y][x] = true;
			}
		}
		for (int y = 0; y < Map.MAXMAPY; y++){
			for (int x = 0; x < Map.MAXMAPX; x++){
				m.cells[y][x].setPassable(change[y][x]);
			}
		}
		return m;
	}

	public static Map shaveMap(Map m){
		short n = 0;
		boolean[][] change = new boolean[Map.MAXMAPY][Map.MAXMAPX];	
		for (int y = 0; y < Map.MAXMAPY; y++){
			for (int x = 0; x < Map.MAXMAPX; x++){
				n = m.passableNeighborCount(y,x);
				if (n >= 5){
					change[y][x] = false;
				} else change[y][x] = true;
			}
		}
		for (int y = 0; y < Map.MAXMAPY; y++){
			for (int x = 0; x < Map.MAXMAPX; x++){
				m.cells[y][x].setPassable(change[y][x]);
			}
		}
		return m;
	}


	public static Map featureMap(Map m){
		Coord[] frees = m.freeCells();
		for(int i = 0; i < frees.length; i++){
			if (Firmament.rand.nextFloat()  < Map.TREEPERCENT){
				m.cells[frees[i].y][frees[i].x].setTerrain(Cell.TERRAIN_DOODAD);
			}
		}
		return m;
	}

	public static Map dropMobs(Map m){
		Coord[] frees = m.freeCells();
		int space = frees.length;
		if (space == 0) return m;
		int mobPos;
		int mobfill = (int) (Map.MOBFILLMIN + (Math.random() * (Map.MOBFILLMAX - Map.MOBFILLMIN)));
		for (int i = 0; i < mobfill; i++){
			Actor mob = new Goblin();
			mobPos = (int) (Math.random() * (frees.length));
			m.cells[frees[mobPos].y][frees[mobPos].x].setActor(mob);
			m.mobs.add(mob);
		}
		return m;
	}
	
	

	public static Map randomDropPlayer(Map m){
		Coord[] frees = m.freeCells();
		int playerPos = (int) (Math.random()* (frees.length));
		if (frees.length == 0){
			m.cells[0][0].setActor(Actor.player);
			m.playerLoc = new Coord(0,0);
			m.updateFov();
			return m;
		}
		m.cells[frees[playerPos].y][frees[playerPos].x].setActor(Actor.player);
		m.cells[frees[playerPos].y][frees[playerPos].x].see();
		m.playerLoc = new Coord(frees[playerPos].y, frees[playerPos].x);
		m.updateFov();
		return m;
	}

}
