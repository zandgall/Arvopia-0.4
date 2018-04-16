package pre.zandgall.tiles.gfx;

import pre.zandgall.tiles.Game;
import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.Entity;
import pre.zandgall.tiles.tiles.Tile;
import pre.zandgall.tiles.worlds.World;

public class GameCamera {

	private Handler handler;

	private float xOffset, yOffset;
	
	
	public GameCamera(Handler handler, float xOffset, float yOffset) {
		this.handler = handler;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void checkBlankSpace() {
		if(xOffset<(-(Game.scale-1)*handler.getWidth()/(2*Game.scale))) {
			xOffset = (float) (-(Game.scale-1)*handler.getWidth()/(2*Game.scale));
		}
		if (yOffset>-(8 - (World.getHeight()-15))*Tile.TILEHEIGHT+(Game.scale-1)*handler.getHeight()/2) {
			yOffset = (float) (-(8 - (World.getHeight()-15))*Tile.TILEHEIGHT+(Game.scale-1)*handler.getHeight()/2);
		}
	}
	
	public void centerOnEntity(Entity e) {
		xOffset = (float) (e.getX() - handler.getWidth() / 2 + e.getWidth() / 2);
		yOffset = (float) (e.getY() - handler.getHeight() / 2 + e.getHeight() / 2);
		checkBlankSpace();
	}

	public void move(float x, float y) {
		xOffset += x;
		yOffset += y;
		checkBlankSpace();
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

}
