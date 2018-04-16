package pre.zandgall.tiles.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.ImageLoader;
import pre.zandgall.tiles.gfx.PublicAssets;
import pre.zandgall.tiles.utils.Button;
import pre.zandgall.tiles.utils.Sound;

public class TitleState extends State {

//	private int mouseX;
//	private int mouseY;

	public Sound titleSong;
	
	
	private Button start, quit, options, changelog, howToPlay;
	
//	private BufferedImage[] info;
//	private String[][] infoDesc;
	
	private BufferedImage title;
	
	public TitleState(Handler handler) {
		super(handler);
		
		titleSong = new Sound("Songs/Title.wav");
		
		PublicAssets.init();
		
//		info = new BufferedImage[] {PublicAssets.grass};  s
//		infoDesc = new String[][] {{"Grass is the most common ", "tile in the game. There is ", "nothing interesting ", "about Grass"}};
		
		title = ImageLoader.loadImage("/textures/Title.png");
		
		options = new Button(handler, handler.getWidth()/2 - 30, handler.getHeight()/2 + 30, 80, 25, new BufferedImage[] {}, "Takes you to the options menu", "Options");
		start = new Button(handler, handler.getWidth()/2-25, handler.getHeight()/2, 72, 25, PublicAssets.start, "Starts the game by loading in the world", "Start");
		quit = new Button(handler, handler.getWidth()/2 - 10, handler.getHeight()/2 + 120, 45, 25, new BufferedImage[] {}, "Quits the application", "Quit");
		changelog = new Button(handler, handler.getWidth()/2 - 40, handler.getHeight()/2 + 90, 100, 25, new BufferedImage[] {}, "View the changelog", "Changelog");
		howToPlay = new Button(handler, handler.getWidth()/2 - 50, handler.getHeight()/2 + 60, 120, 25, new BufferedImage[] {}, "View the instructions", "How to play");
		
		titleSong.Start(-1, true);
		setSong(titleSong);
		
		handler.setVolume();
		
		handler.getMouse().setHandler(handler);
	}

	@Override
	public void tick() {
		
		options.tick();
		start.tick();
		quit.tick();
		changelog.tick();
		howToPlay.tick();
		
		if(howToPlay.on) {
			State.setState(handler.getGame().instructionsState);
			titleSong.Start(0, false);
		}
		
		if(changelog.on) {
			State.setState(handler.getGame().changelogState);
			titleSong.Start(0, false);
		}
		
		if(options.on) {
			State.setState(handler.getGame().optionState);
			titleSong.Start(0, false);
		}
			
		
		if(quit.on)
			handler.getGame().stop();
		
		if (start.on) {
			State.setState(handler.getGame().worldState); 
			titleSong.Start(0, false);
			titleSong.Stop(true);
		}
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(120, 225, 255));
		
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		g.drawImage(title, handler.getWidth()/2-title.getWidth()/2, 10, null);
		
		options.render(g);
		start.render(g);
		quit.render(g);
		changelog.render(g);
		howToPlay.render(g);
	}

	@Override
	public void init() {

	}

}
