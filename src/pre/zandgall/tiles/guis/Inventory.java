package pre.zandgall.tiles.guis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.ImageLoader;

public class Inventory extends Gui{
	
	public static final int SPACEWIDTH = 20, SPACEHEIGHT = 20;
	
	private InventoryItem metal, petal, honey, foxFur, butterflyWing;
	
	private ArrayList<InventoryItem> items;
	
	public Inventory(Handler game) {
		super(game);
		
		metal = new InventoryItem(ImageLoader.loadImage("/textures/Inventory/Metal.png"), 1, 1, 0);
		petal = new InventoryItem(ImageLoader.loadImage("/textures/Inventory/Petals.png"), 2, 1, 0);
		honey = new InventoryItem(ImageLoader.loadImage("/textures/Inventory/Honey.png"), 3, 1, 0);
		foxFur = new InventoryItem(ImageLoader.loadImage("/textures/Inventory/FoxFur.png"), 4, 1, 0);
		butterflyWing = new InventoryItem(ImageLoader.loadImage("/textures/Inventory/ButterflyWing.png"), 5, 1, 0);
		items = new ArrayList<InventoryItem>();
		items.add(metal);
		items.add(petal);
		items.add(honey);
	}

	@Override
	public void tick() {
		metal.tick(game.getWorld().getEntityManager().getPlayer().metal);
		petal.tick(game.getWorld().getEntityManager().getPlayer().petals);
		honey.tick(game.getWorld().getEntityManager().getPlayer().honey);
		foxFur.tick(game.getWorld().getEntityManager().getPlayer().foxFur);
		butterflyWing.tick(game.getWorld().getEntityManager().getPlayer().butterflyWing);
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(0, 0, 200, 100));
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		metal.render(g);
		petal.render(g);
		honey.render(g);
		foxFur.render(g);
		butterflyWing.render(g);
	}

	@Override
	public void init() {
		
	}

}
