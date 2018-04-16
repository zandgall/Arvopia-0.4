package pre.zandgall.tiles.entity.moveableStatics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.PublicAssets;
import pre.zandgall.tiles.tiles.Tile;
import pre.zandgall.tiles.worlds.World;

public class Cloud extends MoveableStatic{

	private double speed;
	
	private BufferedImage cloud;
	
	public Cloud(Handler handler, double x, double y, int type, double speed) {
		super(handler, x, y, 54, 36, false);
		this.speed = speed;
		
		this.layer = (Math.random()*4-2);
		
		cloud = PublicAssets.cloud[type];
	}

	@Override
	public void tick() {
		x+=speed+game.getWorld().getEnviornment().getWind();
		if(x>World.getWidth()*Tile.TILEWIDTH+game.getWidth()/2) {
			x = -54;
		}
	}

	@Override
	public void init() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(cloud, (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), null);
	}

}
