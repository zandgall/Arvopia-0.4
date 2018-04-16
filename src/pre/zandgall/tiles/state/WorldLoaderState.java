package pre.zandgall.tiles.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.utils.Button;

public class WorldLoaderState extends State{
	
	public Button world1, world2, level1, level2, staircase, defaultworld, loadWorld, back;
	
	int init = 0;
	
	public WorldLoaderState(Handler handler) {
		super(handler);
		
		world1 = new Button(handler, 10, 10, 80, 25, new BufferedImage[] {}, "Loads world1.txt", "World 1");
		world2 = new Button(handler, 10, 40, 80, 25, new BufferedImage[] {}, "Loads world2.txt", "World 2");
		level1 = new Button(handler, 10, 70, 80, 25, new BufferedImage[] {}, "Loads levelone", "World 3");
		level2 = new Button(handler, 10, 100, 80, 25, new BufferedImage[] {}, "Loads leveltwo", "World 4");
		staircase = new Button(handler, 10, 130, 80, 25, new BufferedImage[] {}, "Loads staircase.txt", "World 5");
		defaultworld = new Button(handler, 10, 160, 80, 25, new BufferedImage[] {}, "Loads DefaultWorld", "Default");
		loadWorld = new Button(handler, 10, handler.getHeight()-40, 115, 25, new BufferedImage[] {}, "Opens a file browser to select a file", "Open Other");
		back = new Button(handler, 140, handler.getHeight()-40, 55, 25, new BufferedImage[] {}, "Takes you back to title screen", "Back");
	}

	@Override
	public void tick() {
		
		world1.tick();
		world2.tick();
//		level1.tick();
//		level2.tick();
//		staircase.tick();
//		defaultworld.tick();
		
		loadWorld.tick();
		
		back.tick();
		
		if(back.on)
			State.setState(handler.getGame().menuState);
		
		if(world1.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 0);
			
		} else if(world2.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 1);
			
		} else if(level1.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 0);
			
		} else if(level2.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 1);
			
		} else if(defaultworld.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 2);
			
		} else if(staircase.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(false, 5);
			
		} else if(loadWorld.on) {
			State.setState(handler.getGame().gameState);
			handler.getGame().gameState.openWorld(true, 0);
			
		}
		
		
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
		
		g.setColor(Color.lightGray);
		g.fillRect(0, handler.getHeight()-50, handler.getWidth(), 51);
		g.setColor(Color.darkGray);
		g.fillRect(3, handler.getHeight()-47, handler.getWidth(), 51);
		g.setColor(Color.gray);
		g.fillRect(3, handler.getHeight()-47, handler.getWidth()-6, 44);
		g.setColor(Color.black);
		g.drawRect(0, handler.getHeight()-50, handler.getWidth(), 51);
		
//		level1.render(g);
//		level2.render(g);
		world1.render(g);
		world2.render(g);
//		defaultworld.render(g);
//		staircase.render(g);
		
		loadWorld.render(g);
		
		back.render(g);
	}

	@Override
	public void init() {
		
	}

}
