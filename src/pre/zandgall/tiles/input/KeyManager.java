package pre.zandgall.tiles.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import pre.zandgall.tiles.Game;

public class KeyManager implements KeyListener {
	
	private boolean[] keys;
	public boolean up, down, left, right, esc, invtry, k1, k2, k3, k4, k5, k6, k7, k8, k9, k0;
	public boolean b;
	public boolean typed;
	
	public KeyManager() {
		keys = new boolean[524];
	}
	
	public void tick(){
		up = (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_SPACE]);
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
		invtry = (keys[KeyEvent.VK_Z] && typed);
		b = keys[KeyEvent.VK_B] && typed;
		
		k1 = keys[KeyEvent.VK_1];
		k2 = keys[KeyEvent.VK_2];
		k3 = keys[KeyEvent.VK_3];
		k4 = keys[KeyEvent.VK_4];
		k5 = keys[KeyEvent.VK_5];
		k6 = keys[KeyEvent.VK_6];
		k7 = keys[KeyEvent.VK_7];
		k8 = keys[KeyEvent.VK_8];
		k9 = keys[KeyEvent.VK_9];
		k0 = keys[KeyEvent.VK_0];
		
		esc = keys[KeyEvent.VK_ESCAPE] && typed;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!keys[e.getKeyCode()]) { 
			Game.log.log("Key Code pressed: "+e.getKeyCode() + " Name: "+e.getKeyChar());
		}
		keys[e.getKeyCode()]=true;
		typed = false;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()]=false;
		typed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keys[e.getKeyCode()]=true;
		typed = true;
	}

}
