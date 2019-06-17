package firmament;

import java.util.Random;

import org.lwjgl.openal.AL;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import firmament.map.EditState;
import firmament.map.World;


/**
 * Firmament contains our main method.
 * This class has all the junk related to setting up the app; initializing resources,
 * declaring a boatload of constants, and actually starting the app. This class's update
 * and render method delegate these tasks to the game's current State class.
 * @author John is a pretty cool guy.
 */
public class Firmament extends BasicGame{

	//TODO check all these declares for unused values
	public static final int FRAMEWIDTH = 1360;
	public static final int FRAMEHEIGHT = 730;
	public static final int FRAMESCALE = 1;
	public static final int GRAPHICSCALE = 3;
	public static final String TITLE = "Firmament testbuild 0.1";
	public static final int MAXFPS = 60;

	public static final int GRIDCELLSHEIGHT = 15;
	public static final int GRIDCELLSWIDTH = 19;
	public static final int PLAYEROFFSETX = (GRIDCELLSWIDTH / 2);
	public static final int PLAYEROFFSETY = (GRIDCELLSHEIGHT / 2);
	public static final int GRIDCELLSIZE = 15 * GRAPHICSCALE;
	public static final int GRIDPIXELHEIGHT = GRIDCELLSHEIGHT * GRIDCELLSIZE;
	public static final int GRIDPIXELWIDTH = GRIDCELLSWIDTH * GRIDCELLSIZE;

	public static final int STATE_NIL = 0;
	public static final int STATE_BASE = 1;
	public static final int STATE_MENU = 2;
	public static final int STATE_EDIT = 3;
	public static int ds = 0; // change in state

	public static final int ACTIONDELAYMAX = 40;
	public static final int ACTIONDELAYMIN = 4;
	public int actionDelay = 0;
	public float turnCount = 0;
	public boolean hasDelayed;

	public Music mu;

	public static Random rand;

	//world for the current game;
	private static World world;
	
	private State state;
	private State menuS;
	private State baseS;
	private State editS;

	// individual images to be filled from spriteSheet
	public static Image grass;
	public static Image grass1;
	public static Image grass2;
	public static Image player;
	public static Image wall;
	public static Image tree;
	public static Image icon;
	public static Image goblin;
	public static Image altar;
	public static Image error;
	public static SpriteSheet spriteSheet;

	public Firmament(String title) {
		super(title);
	}

	/*
	 * HEY WHERE'S MAIN?
	 * 
	 * WHERE IS IT?
	 * 
	 * vvv RIGHT DOWN HERE vvv 
	 */
	public static void main(String args[]) throws SlickException{

		AppGameContainer app = new AppGameContainer(new Firmament(TITLE));
		app.setDisplayMode(FRAMEWIDTH, FRAMEHEIGHT, false);
		app.setTargetFrameRate(MAXFPS);

		app.start();
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		state.draw(g, world);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// load ALL values
		mu = new Music();
		mu.start();

		rand = new Random();
		
		loadSprites();
		
		world = new World();
		world.loadMap(0);
		menuS = new MenuState();
		baseS = new BaseState();
		editS = new EditState();
		state = menuS;
	}
	
	private void loadSprites(){
		try {
			spriteSheet = new SpriteSheet("./res/sprites.png", 15,15, Color.black);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		grass = spriteSheet.getSprite(8, 0);
		grass1 = spriteSheet.getSprite(7, 0);
		grass2 = spriteSheet.getSprite(7, 0);
		player = spriteSheet.getSprite(0, 2);
		wall = spriteSheet.getSprite(9, 0);
		tree = spriteSheet.getSprite(5, 0);
		goblin = spriteSheet.getSprite(3, 2);
		altar = spriteSheet.getSprite(1 , 1);
		error = spriteSheet.getSprite(0, 0);
	}

	/** 
	 * Pass the input and world to the current State.
	 * Depending on the return value of state.Process(), 
	 * may change the current state.
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//core game logics
		ds = state.process(gc.getInput(), world);
		switch(ds){
		case (STATE_NIL) : return;
		case(STATE_BASE) : {
			mu.stop();
			state = baseS;
			break;
		}
		case (STATE_MENU) : {
			mu.start();
			state = menuS;
			break;
		}
		case (STATE_EDIT) : {
			mu.stop();
			state = editS;
			break;
		}
		default : return;
		}
//		if (ds == STATE_NIL){
//			return;
//		} else if (ds == STATE_BASE){
//			mu.stop();
//			state = baseS;
//		} else if (ds == STATE_MENU){
//			mu.start();
//			state = menuS;
//		} else if (ds == STATE_EDIT){
//			mu.stop();
//			state = editS;
//		}
	}

	/**
	 * close the program
	 */
	public static void close(){
		AL.destroy(); //get rid of audio stuff
		System.exit(0);
	}
}
