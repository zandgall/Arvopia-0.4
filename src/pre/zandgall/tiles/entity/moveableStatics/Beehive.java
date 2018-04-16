package pre.zandgall.tiles.entity.moveableStatics;

import java.awt.Graphics;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.creatures.Bee;
import pre.zandgall.tiles.gfx.PublicAssets;

public class Beehive extends MoveableStatic{
	
	private long timer;
	
	public Beehive(Handler handler, double x, double y) {
		super(handler, x, y, 18, 18, true);
		
		timer = 0;
	}

	@Override
	public void tick() {
		if(timer >= 10000) {
			if(Math.random()<0.05) {
				game.getWorld().getEntityManager().addEntity(new Bee(game, x, y, true, 10000));
			}
			timer = 0;
		} else {
			timer++;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(PublicAssets.beehive, (int) (x - game.getGameCamera().getxOffset()), (int) (y - game.getGameCamera().getyOffset()), null);
	}

	@Override
	public void init() {}
}
