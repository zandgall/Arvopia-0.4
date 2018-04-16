package pre.zandgall.tiles.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class InventoryItem {
	
	private int x, y, amount;
	private BufferedImage texture;
	private boolean moving;
	
	public InventoryItem(BufferedImage texture, int x, int y, int amount) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.amount = amount;
		moving = false;
	}
	
	public void tick(int amount) {
		this.amount = amount;
	}
	
	public void render(Graphics g) { 
		if(moving) {
			g.drawImage(texture, x, y, null);
		} else {
			g.drawImage(texture, x*Inventory.SPACEWIDTH, y*Inventory.SPACEHEIGHT, null);
			g.setColor(new Color(0, 0, 0, 200));
			g.setFont(new Font("Dialog", Font.BOLD, 12));
			g.drawString(""+amount, x*Inventory.SPACEWIDTH+1, y*Inventory.SPACEHEIGHT+16);
		}
	}
}
