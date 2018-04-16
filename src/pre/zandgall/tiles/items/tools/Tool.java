package pre.zandgall.tiles.items.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.enviornment.Light;

public abstract class Tool {
	
	protected Handler game;
	
	public Tool(Handler game, boolean weapon) {
		this.game = game;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, int x, int y);
	
	public abstract void custom1(int x, int y);

	public BufferedImage texture() {
		return null;
	}
	
	public abstract BufferedImage getFrame();

	public abstract int getYOffset();
	
	public boolean hasLight() {
		return false;
	}
	
	public Light getLight() {
		return null;
	}
	
}
