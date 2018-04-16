package pre.zandgall.tiles.items.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.enviornment.Light;
import pre.zandgall.tiles.gfx.Animation;
import pre.zandgall.tiles.gfx.PublicAssets;

public class Torch extends Tool{
	
	
	private Animation torchAn, stab;
	private Light torch;
	
	private boolean one = false;
	
	public Light getLight() {
		return torch;
	}
	
	public boolean hasLight() {
		return true;
	}
	
	public Torch(Handler game) {
		super(game, false);
		
		one = false;
		
		torchAn = new Animation(150, new BufferedImage[] {PublicAssets.torch.getSubimage(0, 0, 18, 45), PublicAssets.torch.getSubimage(18, 0, 18, 45)}, "", "Torch");
		stab = new Animation(150, new BufferedImage[] {PublicAssets.torchStab.getSubimage(0, 0, 36, 21), PublicAssets.torchStab.getSubimage(36, 0, 36, 21)}, "Stab", "Torch");
		torch = new Light(game, 100, 100, 15, 1, Color.orange);
	}

	@Override
	public void tick() {
		if(!one) {
			game.getWorld().getEnviornment().getLightManager().addLight(torch);
			one = true;
			torch.turnOn();
		}
		
		torchAn.tick();
		stab.tick();
	}

	public void custom1(int x, int y) {
		torch.setX(x-9);
		torch.setY(y+7);
	}
	
	public BufferedImage texture() {
		return torchAn.getFrame();
	}

	@Override
	public BufferedImage getFrame() {
		return stab.getFrame();
	}

	@Override
	public void render(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public int getYOffset() {
		return 22;
	}

}
