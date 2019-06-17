package firmament;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import firmament.map.World;

public class MenuState extends State{

	Animation background;

	int[] bgDur = {100,100,100,100,100,100};
	float bgWidth = (float) (447 * 2.4);
	float bgHeight = (float) (315 * 2.4);
	float bgX = Firmament.FRAMEWIDTH /2 - bgWidth/2;
	float bgY = Firmament.FRAMEHEIGHT /2 - bgHeight/2;
	Image title;

	public MenuState(){
		try{
			Image[] bg = {new Image("./res/menuimage1.png"), new Image("./res/menuimage2.png"), new Image("./res/menuimage3.png"),
					new Image("./res/menuimage4.png"), new Image("./res/menuimage5.png"), new Image("./res/menuimage6.png"),};
			background = new Animation(bg, bgDur, true);
			background.setLooping(true);
			title = new Image("./res/firmament_title.png");
		}catch(SlickException e){
			//e.printStackTrace();
		}
	}

	@Override
	public int process(Input inp, World w) {
		if (inp.isKeyPressed(Input.KEY_ENTER)){
			return Firmament.STATE_BASE;
		}
		if (inp.isKeyPressed(Input.KEY_Q)){
			Firmament.close();
		}
		if (inp.isKeyPressed(Input.KEY_E)){
			return Firmament.STATE_EDIT;
		}
		return Firmament.STATE_NIL;
	}

	@Override
	public void draw(Graphics g, World w) {
		background.draw(bgX, bgY, bgWidth, bgHeight);
		g.drawString("   FIRMAMENT \n  Enter to Start\n   E to Edit\n  Q to Quit", bgWidth/2 + bgX/2, bgHeight/2 +bgX);
	}
}
