package pre.zandgall.tiles.items.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.PublicAssets;

public class Sword extends Tool{
	
	private BufferedImage sword, stab;
	
	public Sword(Handler game) {
		super(game, false);
		
		sword = PublicAssets.sword;
		stab = PublicAssets.swordStab;
	}

	@Override
	public void tick() {
	}

	public void custom1(int x, int y) {
	}
	
	public BufferedImage texture() {
		return sword;
	}

	@Override
	public BufferedImage getFrame() {
		return stab;
	}

	@Override
	public void render(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public int getYOffset() {
		return 8;
	}

	
}
