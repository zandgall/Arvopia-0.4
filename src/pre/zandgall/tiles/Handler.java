package pre.zandgall.tiles;

import pre.zandgall.tiles.display.Display;
import pre.zandgall.tiles.gfx.GameCamera;
import pre.zandgall.tiles.input.KeyManager;
import pre.zandgall.tiles.input.MouseManager;
import pre.zandgall.tiles.state.OptionState;
import pre.zandgall.tiles.state.State;
import pre.zandgall.tiles.utils.Public;
import pre.zandgall.tiles.worlds.World;

public class Handler {

//	private Log player = new Log("/logs/player.txt", "Player");
//	private Log worldl = new Log("/logs/world.txt", "World");
//	private Log keyEvent = new Log("/logs/keyEvent.txt", "Keys");

	private Game game;
	private World world;

	public Game getGame() {
		return game;
	}
	
	public void setVolume() {
		State.getSong().setVolume((int) Public.range(-80, 6, getVolume()), true);
	}

	public int getWidth() {
		return game.getWidth();
	}

	public int getHeight() {
		return game.getHeight();
	}

	public GameCamera getGameCamera() {
		return game.getGameCamera();
	}
	
	public float xOffset() {
		return game.getGameCamera().getxOffset();
	}
	
	public float yOffset() {
		return game.getGameCamera().getyOffset();
	}

	public KeyManager getKeyManager() {
		return game.getKeyManager();
	}

	public MouseManager getMouse() {
		return game.getMouse();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public World getWorld() {
		return world;
	}
	
	public double getWind() {
		return world.getEnviornment().getWind();
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Handler(Game game) {
		this.game = game;
	}

	public void log(String string) {
		Game.log.log(string);
	}
	
	public Display display() {
		return game.getDisplay();
	}

	public double getVolume() {
		OptionState o = (OptionState) game.optionState;
		
		return o.volume.getValue()-80;
	}
	
//	public Sound getTitle() {
//		return game.menuState.getTitle();
//	}

//	public void logPlayer(String string) {
//		player.log(string);
//	}
//
//	public void logWorld(String string) {
//		worldl.log(string);
//	}
//
//	public void logKeys(String string) {
//		keyEvent.log(string);
//	}
}
