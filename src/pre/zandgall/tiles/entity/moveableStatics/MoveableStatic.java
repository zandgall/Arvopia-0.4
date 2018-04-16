package pre.zandgall.tiles.entity.moveableStatics;


import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.Entity;

public abstract class MoveableStatic extends Entity{

	public MoveableStatic(Handler handler, double x, double y, int width, int height, boolean solid) {
		super(handler, x, y, width, height, solid, false, false);
		
	}

	public abstract void tick();
	
	public abstract void init();
	
}
