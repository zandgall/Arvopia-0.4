package pre.zandgall.tiles.entity.statics;

import java.awt.Graphics;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.PublicAssets;

public class Flower extends StaticEntity{
	
	private int type;
	
	public Flower(Handler handler, double x, double y,int type, double layer) {
		super(handler, x, y, 18, 18, false);
		this.type = type;
		
		this.layer = layer;
		
		bounds.x = -4;
		bounds.width = 7;
		bounds.y = 5;
		bounds.height = 13;
	}

	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(PublicAssets.flower[type], (int) (x - game.getGameCamera().getxOffset()-9), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		
	}

	public int getType() {
		return type;
	}
	
}
