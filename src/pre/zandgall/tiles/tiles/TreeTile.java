package pre.zandgall.tiles.tiles;

import pre.zandgall.tiles.gfx.Assets;
import pre.zandgall.tiles.gfx.ImageLoader;

public class TreeTile extends Tile{
	
	private static Assets tree = new Assets(ImageLoader.loadImage("/textures/TreeTileset.png"), 18, 18, "TreeTile");

	public TreeTile( int id, int x, int y) {
		super(tree.get(x,y), id);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void reset() {
		
	}

}
