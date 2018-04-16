package pre.zandgall.tiles.entity.statics;

import java.awt.Graphics;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.PublicAssets;

public class Stone extends StaticEntity{
	
	private int type;
	private int orType;
	
	public Stone(Handler handler, double x, double y,int type) {
		super(handler, x, y, 18, 18, false);
		this.type = type;
		orType = type;
		
		if(type == 0) {
			layer = Math.random()+1;
			bounds.x = 2;
			bounds.y = 8;
			bounds.width = 15;
			bounds.height = 10;
		} else if(type == 1) {
			layer = Math.random()-0.5;
			bounds.x = 2;
			bounds.y = 4;
			bounds.width = 15;
			bounds.height = 14;
		} else {
			layer = Math.random()-1;
			bounds.x = 2;
			bounds.width = 15;
			bounds.y = 3;
			bounds.height = 15;
		}
	}

	public int getType() {
		return type;
	}
	
	public int getOrType() {
		return orType;
	}

	public void setType(int type) {
		this.type = type;
		
		if(type == 0) {
			layer = 3;
			bounds.x = 2;
			bounds.y = 8;
			bounds.width = 15;
			bounds.height = 10;
		} else if(type == 1) {
			layer = 2;
			bounds.x = 3;
			bounds.y = 4;
			bounds.width = 12;
			bounds.height = 16;
		} else {
			layer = 1;
			bounds.x = 3;
			bounds.width = 15;
			bounds.y = 3;
			bounds.height = 15;
		}
	}

	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(PublicAssets.stone[type], (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), width, height, null);
		
	}
}
