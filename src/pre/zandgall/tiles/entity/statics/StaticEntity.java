package pre.zandgall.tiles.entity.statics;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.Entity;

public abstract class StaticEntity extends Entity {

	public StaticEntity(Handler handler, double x, double y, int width, int height, boolean solid) {
		super(handler, x, y, width, height, solid, false, false);
	}
	
	public void reset() {
		
	}
	
}
