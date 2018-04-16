package pre.zandgall.tiles.state;

import java.awt.Graphics;
import java.awt.Graphics2D;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.utils.Sound;

public abstract class State {
	
	protected Handler handler;
	static Handler handlerStatic;
	
	static Sound currentSong;
	
	private static State currentState = null, prevState = null;
	
	public static void setState(State state){
		prevState = currentState;
		currentState = state;
		handlerStatic.log("State is now: "+state);
	}
	
	public static State getState(){
		return currentState;
	}
	
	public static State getPrev() {
		return prevState;
	}
	//CLASS
	
	public State(Handler handler) {
		this.handler = handler;
		handlerStatic = handler;
	}
	
	public void openWorld(boolean open, int index) {
		
	}
	
	public void setSong(Sound song) {
		currentSong = song;
	}
	
	public static Sound getSong() {
		return currentSong;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, Graphics2D g2d);
	
	public abstract void init();
	
	
}
