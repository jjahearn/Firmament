package firmament.map;



public class World {
	private Map[] maps;
	public Map cm; // current map
	public float turnCount = 0;

	public static final int WORLDSIZE = 1;

	
	public World(){
		maps = new Map[WORLDSIZE];
		loadMap(0);
	}

	public World(Vault v){
		maps = new Map[v.depth];
		loadMap(0);
	}

	/**Set the current map to the map at target index.
	 * Generate a map if none exists.
	 * @param target
	 */
	public void loadMap(int target){
		if (maps[target] == null){
			maps[target] = new Map();
			maps[target] = MapGen.randMap(maps[target]);
			for (int i = 0; i < 4; i++){
				maps[target] = MapGen.caveMap(maps[target]);
			}

			for (int i = 0; i < 1; i++){
				maps[target] = MapGen.shaveMap(maps[target]);
			}
			maps[target] = MapGen.featureMap(maps[target]);
			maps[target] = MapGen.dropMobs(maps[target]);
			maps[target] = MapGen.randomDropPlayer(maps[target]);
			//System.out.print("\n\n");
			System.out.print(maps[target].toString());
		}
		cm = maps[target];
	}


}