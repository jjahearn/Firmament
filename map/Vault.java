package firmament.map;

import java.util.ArrayList;

/** Vaults are collections of pre-arranged cells that can be dropped onto a randomized map 
 * @author John is a pretty cool guy.
 */
public class Vault {
	public int depth = 1;
	private ArrayList<Cell> cells;
	public int rowLength = 1;
	public int colLength = 1;

	public Vault(int x, int y, int z){
		rowLength = x;
		colLength = y;
		cells = new ArrayList<Cell>(x*y);
		depth = z;
		for (int i = 0; i < x * y; i++){
			cells.add(new Cell(Cell.TERRAIN_GROUND));
		}
	}
	
	public void setSize(int y, int x){
		if ((x < 0) || (y < 0)){
			System.out.println("invalid dimensions X: " + x +" Y: " + y);
		}
		if ((x == rowLength) && (y == colLength)) return;
		
		int dx = x - rowLength;
		int dy = y - colLength;
		if (dy <= 0){ //efficiency!
			resizeCols(dy);
			resizeRows(dx);
		} else {
			resizeRows(dx);
			resizeCols(dy);
		}
	}
	
	private void resizeCols(int dy){
		if (dy == 0) return;
		if (dy >= 0){
			for (int i = 0; i < dy * rowLength; i++){
				cells.add(new Cell(Cell.TERRAIN_GROUND));
			}
		} else {
			for(int i = cellCount(); i > cellCount() - (dy * cellCount()); i--){
				cells.remove(i);
			}
		}
		colLength += dy;
	}
	
	private void resizeRows(int dx){
		if (dx == 0) return;
		if (dx >= 0){
			for (int i = cellCount(); i > 0; i -= rowLength){
				for(int j = 1; j <= dx; j++){
					if(i+j > cellCount()){
						cells.add( new Cell(Cell.TERRAIN_GROUND));
					}
					cells.add(i+j, new Cell(Cell.TERRAIN_GROUND));
				}
			}
		} else{
			for(int i = cellCount(); i > 0; i -= rowLength){
				for(int j = 0; j > dx; j--){
					cells.remove(i+j);
				}
			}
		}
		rowLength += dx;
	}
	
	public int cellCount(){
		return colLength*rowLength;
	}

	public int arraySize(){
		return cells.size();
	}
	
	public Cell[][] buildGrid() {
		Cell[][] result = new Cell[colLength][rowLength];
		int i = 0;
		for (int y = 0; y < colLength; y++){
			for (int x = 0; x < rowLength; x++){
				result[y][x] = cells.get(i);
				i++;
			}
		}
		return result;
	}
}
