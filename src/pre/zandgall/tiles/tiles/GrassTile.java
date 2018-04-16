package pre.zandgall.tiles.tiles;

import pre.zandgall.tiles.gfx.Assets;
import pre.zandgall.tiles.gfx.ImageLoader;

public class GrassTile extends Tile {

	private static Assets grassTileset = new Assets(ImageLoader.loadImage("/textures/DirtTileset.png"), 18, 18,
			"GrassTile");

	public GrassTile(int id, int x, int y) {
		super(grassTileset.get(x, y), id);
	}

	public boolean isSolid() {
		return true;
	}

	public void init() {
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void reset() {
		grassTileset.reset(ImageLoader.loadImage("/textures/DirtTileset.png"), 18, 18,
				"GrassTile");
	}
}
