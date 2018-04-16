package pre.zandgall.tiles.gfx;

import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Game;
import pre.zandgall.tiles.Handler;

public class Animation {

	private Game g;
	private Handler game = new Handler(g);

	private int speed, index;
	private long lastTime, timer;

	private BufferedImage[] frames;

	public Animation(int speed, BufferedImage[] frames, String name, String source) {
		this.speed = speed;
		this.frames = frames;

		index = 0;

		timer = 0;
		lastTime = System.currentTimeMillis();

		game.log("Animation: " + name + " loaded for " + source);
	}

	public void tick() {
		timer += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();

		if (timer > speed) {
			index++;
			timer = 0;
			if (index >= frames.length) {
				index = 0;
			}
		}
	}
	
	public BufferedImage[] getArray() {
		return frames;
	}

	public BufferedImage getFrame() {
		return frames[index];
	}
	
	public void setFrame(int frame) {
		index = frame;
	}
}
