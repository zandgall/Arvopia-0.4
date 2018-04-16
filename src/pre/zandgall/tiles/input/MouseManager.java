package pre.zandgall.tiles.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import pre.zandgall.tiles.Game;
import pre.zandgall.tiles.Handler;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private Handler game;
	
	private boolean left, right, dragged, in, clicked, held, prevHeld, sliderMalfunc;
	public boolean fullLeft;
	private int mouseX, mouseY, mouseScroll;
	
	private long timer = 0;
	
	public MouseManager(Handler game) {
		this.game = game;
	}
	
	public boolean isLeft() {
		return left;
	} 
	
	public boolean isRight() {
		return right;
	}
	
	public int getMouseX() {
		try {
			return (int) (mouseX*Game.scale-((Game.scale-1)*game.getWidth()/2));
		} catch (NullPointerException e) {
			System.out.println("Error: "+e);
			return 0;
		}
	}
	
	public int rMouseX() {
		return mouseX;
	}
	
	public int rMouseY() {
		return mouseY;
	}
	
	public int getMouseY() {
		try {
			return (int) (mouseY*Game.scale-((Game.scale-1)*game.getHeight()/2));
		} catch (NullPointerException e) {
			System.out.println("Error: "+e);
			return 0;
		}
	}
	
	public boolean isDragged() {
		return dragged;
	}
	
	public boolean isHeld() {
		return held;
	}
	
	public void setDragged(boolean dragged) {
		this.dragged = dragged;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}
	
	public boolean isIn() {
		return in;
	}
	
	
	// Reset
	
	public void tick() {
		
		if(timer%100 == 0) {
			if(prevHeld) {
				if(left) {
					held = true;
				} else {
					prevHeld = false;
				}
			}
		}
		
		if(!clicked && !dragged) {
			left = false;
			right = false;
		}
		
		mouseScroll = 0;
		
		
		timer ++;
		clicked = false;
		
		if(timer >= 1000000) 
			timer = 0;
		
		
	}
	
	public void setSliderMalfunction(boolean tf) {
		sliderMalfunc = tf;
	}
	
	public void setHandler(Handler game) {
		this.game = game;
	}
	
	// Implemented

	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		mouseX = e.getX();
		mouseY = e.getY();
		
		if(sliderMalfunc) {
			int b1 = MouseEvent.BUTTON1_DOWN_MASK;
			int b2 = MouseEvent.BUTTON2_DOWN_MASK;
			if ((e.getModifiersEx() & (b1 | b2)) == b1) {
			    left = true;
			} else if((e.getModifiersEx() & (b1 | b2)) == b2) {
				right = true;
			}
		} else {
			if(e.getButton() == MouseEvent.BUTTON1) {
				left = true;
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				right = true;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		clicked = true;
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = true;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
		
		game.log("Mouse clicked: ("+getMouseX()+", "+getMouseY()+")");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		in = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		in = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = true;
			fullLeft = true;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clicked = false;
		dragged = false;
		if(e.getButton() == MouseEvent.BUTTON1) {
			left = false;
			fullLeft = false;
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			right = false;
		}
	}

	public void reset() {
		clicked = false;
		dragged = false;
		left = false;
		right = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseScroll = e.getWheelRotation();
		game.log("Mouse Scrolled: "+mouseScroll);
	}

	public int getMouseScroll() {
		return mouseScroll;
	}

}
