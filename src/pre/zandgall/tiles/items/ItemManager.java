package pre.zandgall.tiles.items;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import pre.zandgall.tiles.Handler;

public class ItemManager {
	private Handler handler;
	private ArrayList<Item> items;
	
	public ItemManager(Handler handler){
		Item.init();
		this.handler = handler;
		items = new ArrayList<Item>();
	}
	
	public void tick(){
		Iterator<Item> it = items.iterator();
		while(it.hasNext()){
			Item i = it.next();
			i.tick();
			if(i.isPickedUp())
				it.remove();
		}
	}
	
	public void render(Graphics g, boolean box){
		for(Item i : items)
			i.render(g, box);
	}
	
	public void addItem(Item i){
		i.setHandler(handler);
		items.add(i);
	}
	
	// Getters and Setters

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}