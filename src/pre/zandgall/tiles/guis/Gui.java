package pre.zandgall.tiles.guis;

import java.awt.Graphics;

import pre.zandgall.tiles.Handler;

public abstract class Gui {
	
	protected Handler game;
	
	public Gui(Handler game) {
		this.game = game;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public abstract void init();
	
}
