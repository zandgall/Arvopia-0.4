package pre.zandgall.tiles.tiles;

import pre.zandgall.tiles.gfx.ImageLoader;

public class EmptyTile extends Tile {

	public EmptyTile(int id) {
		super(ImageLoader.loadImage("/textures/Null.png"), id);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
