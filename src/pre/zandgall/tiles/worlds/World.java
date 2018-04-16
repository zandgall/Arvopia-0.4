package pre.zandgall.tiles.worlds;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import pre.zandgall.tiles.enviornment.Enviornment;
import pre.zandgall.tiles.Game;
import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.Entity;
import pre.zandgall.tiles.entity.EntityManager;
import pre.zandgall.tiles.entity.creatures.Bee;
import pre.zandgall.tiles.entity.creatures.Butterfly;
import pre.zandgall.tiles.entity.creatures.Cannibal;
import pre.zandgall.tiles.entity.creatures.Creature;
import pre.zandgall.tiles.entity.creatures.Fox;
import pre.zandgall.tiles.entity.creatures.Player;
import pre.zandgall.tiles.entity.moveableStatics.Cloud;
import pre.zandgall.tiles.entity.statics.Flower;
import pre.zandgall.tiles.entity.statics.Stone;
import pre.zandgall.tiles.gfx.PublicAssets;
import pre.zandgall.tiles.items.Item;
import pre.zandgall.tiles.items.ItemManager;
import pre.zandgall.tiles.tiles.Tile;
import pre.zandgall.tiles.utils.Button;
import pre.zandgall.tiles.utils.LoaderException;
import pre.zandgall.tiles.utils.Public;
import pre.zandgall.tiles.utils.Utils;

public class World {

	private Enviornment enviornment;

	private Handler handler;

	private static int width;

	private static int height;
	private int spawnx, spawny;
	private static int[][] tiles;

	public int bee, butterfly, fox, stone0, stone1, stone2, flower0, flower1, flower2, cloud0, cloud1, cloud2, cloud3,
			cloudY, cannibalTribes, minPerTribe, maxPerTribe;
	private int maxBee, maxButterfly, maxFox, maxStone, maxFlower, maxCannibalTribes;

	int rencount = 0;

	boolean waitingForCreature;

	// Respawn
	private Button respawn;
	private boolean dead;

	// Entities
	private EntityManager entityManager;

	private Entity center;
	private boolean Box = false;

	private boolean loading;

	private int[] heights;

	// Items
	private ItemManager itemManager;

	// File path
	public World(Handler handler, String path, boolean isPath) {
		this.handler = handler;

		entityManager = new EntityManager(handler, new Player(handler, 100, 0, false, 2, 3));
		itemManager = new ItemManager(handler);

		center = entityManager.getPlayer();
		waitingForCreature = false;

		loading = true;

		respawn = new Button(handler, handler.getWidth() / 2 - 50, handler.getHeight() / 2 - 25, 100, 25,
				PublicAssets.respawn, "Respawns the character", "Respawn");

		loadWorld(path, isPath);
		
		enviornment.setupStars();

		Creature.init();

		highestTile();

		// entityManager.addEntity(new Beehive(handler, 72, 0));

		for(int i = 0; i < cannibalTribes; i++)
			addCannibalTribe((int) Public.random(minPerTribe, maxPerTribe), (int) Public.random(5, width-5));

		addCloud(cloud0, 0);
		addCloud(cloud1, 1);
		addCloud(cloud2, 2);
		addCloud(cloud3, 3);
		addFox(fox);
		addBee(bee, 100000);
		addButterfly(butterfly, 100000);
		addStone(stone0, 0);
		addStone(stone1, 1);
		addStone(stone2, 2);
		addFlower(flower0, 2);
		addFlower(flower1, 1);
		addFlower(flower2, 0);
		entityManager.getPlayer().setX(spawnx);
		entityManager.getPlayer().setY(spawny);
	}

	public void reset() {
		entityManager.getEntities().clear();
		tiles = new int[][] { {} };
		dead = true;
		center = null;
		heights = new int[] {};
		handler = null;
	}

	public void tick() {

		if (handler.getKeyManager().b) {
			if (Box) {
				Box = false;
			} else {
				Box = true;
			}
		}

		if (dead) {
			respawn.tick();
		}

		entityManager.tick();
		itemManager.tick();
		enviornment.tick();

		if (respawn.on) {
			if (entityManager.getPlayer() != null) {
				entityManager.getPlayer().kill();
			}
			entityManager.setPlayer(new Player(handler, 100, 0, false, 2, 3));
			entityManager.getPlayer().setHealth(entityManager.getPlayer().MAX_HEALTH);
			entityManager.getPlayer().setX(spawnx);
			entityManager.getPlayer().setY(spawny);
			respawn.tick();
			center = entityManager.getPlayer();
			dead = false;
		}

		if (waitingForCreature) {
			for (Entity e2 : entityManager.getEntities()) {
				if (e2.creature) {
					center = e2;
					handler.log("Centered on: " + e2.getClass());
					waitingForCreature = false;
					return;
				}
			}
		}

		if (Math.random() < 0.001 && flower0 + flower1 + flower2 < maxFlower) {
			if (Math.random() < 1 / 3) {
				addFlower(2, 0);
				flower0 += 2;
			} else if (Math.random() < 0.5) {
				addFlower(2, 1);
				flower1 += 2;
			} else {
				addFlower(2, 2);
				flower2 += 2;
			}
		}

		if (Math.random() < 0.0001 && stone0 + stone1 + stone2 < maxStone) {
			if (Math.random() < 1 / 3) {
				addStone(2, 0);
				stone0 += 2;
			} else if (Math.random() < 0.5) {
				addStone(2, 1);
				stone1 += 2;
			} else {
				addStone(1, 2);
				stone2++;
			}
		}

		if (Math.random() < 0.0005 && fox < maxFox) {
			addFox(2);
			fox += 2;
		}

		if (Math.random() < 0.001 && bee < maxBee) {
			addBee(1, 10000);
			bee++;
		}

		if (Math.random() < 0.001 && butterfly < maxButterfly) {
			addButterfly(1, 10000);
			butterfly++;
		}
		
		if(Math.random() < 0.0025 && cannibalTribes < maxCannibalTribes) {
			addCannibalTribe((int) Public.random(minPerTribe, maxPerTribe), (int) Public.random(5, width-5));
			cannibalTribes++;
		}

		if (loading) {
			loading = false;
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void render(Graphics g, Graphics2D g2d) {

		g2d.translate(-(Game.scale - 1) * handler.getWidth() / 2, -(Game.scale - 1) * handler.getHeight() / 2);
		g2d.scale(Game.scale, Game.scale);

		handler.getGameCamera().centerOnEntity(center);
		
		
		enviornment.renderSunMoon(g);
		enviornment.renderStars(g);
		
		if (rencount == 0)
			resetGraphics();

		int xStart = (int) (Math.max(handler.getGameCamera().getxOffset() / (Tile.TILEWIDTH), 0)
				);
		int xEnd = (int) (Math.min(width,
				(handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH + 1)
				);
		int yStart = (int) (Math.max(handler.getGameCamera().getyOffset() / (Tile.TILEHEIGHT), 0)
				);
		int yEnd = (int) (Math.min(height,
				(handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT + 1)
				);

		for (int y = yStart; y < yEnd; y++) {
			for (int x = xStart; x < xEnd; x++) {
				getTile(x, y).render(g, (int) (x * Tile.TILEWIDTH - (handler.getGameCamera().getxOffset())),
						(int) (y * Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
			}
		}
		entityManager.render(g, Box);
		itemManager.render(g, false);
		if (dead) {
			respawn.render(g);
		}

		rencount++;

		enviornment.render(g);
		
		g2d.setTransform(handler.getGame().getDefaultTransform());

		entityManager.getPlayer().renScreens(g);

	}

	public ItemManager getItemManager() {
		return itemManager;
	}

	public void setItemManager(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	private void resetGraphics() {
		for (int i = entityManager.getEntities().size() - 1; i > 0; i--) {
			Entity e = entityManager.getEntities().get(i);
			e.reset();
		}

	}

	public static Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return Tile.n0;

		Tile t = Tile.tiles[tiles[x][y]];
		if (t == null)
			return Tile.n0;

		return t;
	}

	private void loadWorld(String path, boolean isPath) {
		String file = null;
		if (isPath) {
			file = LoaderException.readFile(path);
		} else {
			try {
				file = LoaderException.streamToString(LoaderException.loadResource(path), path.length());
			} catch (IOException e) {
				e.printStackTrace();
				path = "/Worlds/DefaultWorld";
				try {
					file = LoaderException.streamToString(LoaderException.loadResource(path), path.length());
				} catch (IOException e1) {
					e1.printStackTrace();
					file = null;
				}
			}
		}
		
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		spawnx = Utils.parseInt(tokens[2]) * 18;
		spawny = Utils.parseInt(tokens[3]) * 18;

		bee = Utils.parseInt(tokens[4]);
		butterfly = Utils.parseInt(tokens[5]);
		fox = Utils.parseInt(tokens[6]);
		stone0 = Utils.parseInt(tokens[7]);
		stone1 = Utils.parseInt(tokens[8]);
		stone2 = Utils.parseInt(tokens[9]);
		flower0 = Utils.parseInt(tokens[10]);
		flower1 = Utils.parseInt(tokens[11]);
		flower2 = Utils.parseInt(tokens[12]);
		cannibalTribes = Utils.parseInt(tokens[13]);
		minPerTribe = Utils.parseInt(tokens[14]);
		maxPerTribe = Utils.parseInt(tokens[15]);

		maxBee = Utils.parseInt(tokens[16]);
		maxButterfly = Utils.parseInt(tokens[17]);
		maxFox = Utils.parseInt(tokens[18]);
		maxStone = Utils.parseInt(tokens[19]);
		maxFlower = Utils.parseInt(tokens[20]);
		maxCannibalTribes = Utils.parseInt(tokens[21]);

		enviornment = new Enviornment(handler, Utils.parseInt(tokens[22]), Utils.parseInt(tokens[23]),
				Utils.parseInt(tokens[24]));

		cloud0 = Utils.parseInt(tokens[25]);
		cloud1 = Utils.parseInt(tokens[26]);
		cloud2 = Utils.parseInt(tokens[27]);
		cloud3 = Utils.parseInt(tokens[28]);
		cloudY = Utils.parseInt(tokens[29]);

		tiles = new int[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 30]);
			}
		}
	}

	public Enviornment getEnviornment() {
		return enviornment;
	}

	public int getSpawnx() {
		return spawnx;
	}

	public int getSpawny() {
		return spawny;
	}

	public void outOfBounds(Entity e) {
		if (e.getY() > (height + 10) * Tile.TILEHEIGHT || e.getX() > (width + 10) * Tile.TILEWIDTH
				|| e.getX() < (-10 * Tile.TILEWIDTH) || e.getY() < (-15 * Tile.TILEHEIGHT)) {
			if (e.getClass() == Player.class) {
				Player p = (Player) e;
				if (p.lives == 0) {
					kill(e);
				} else {
					p.lives--;
					p.setX(spawnx);
					p.setY(spawny);
					p.setHealth(p.MAX_HEALTH);
					handler.log("Player lives: " + p.lives);
				}
			} else if (e.getClass() != Cloud.class && (e.getClass() == Flower.class || e.getClass() == Stone.class)) {
				e.kill();
			} else if (e.getClass() != Cloud.class) {
				kill(e);
			}
		}
	}

	public void kill(Entity e) {
		if (e.getClass() != Stone.class && e.getClass() != Player.class) {
			e.dead = true;
			e.kill();
		}
		handler.log("Killed: " + e);

		if (e.getClass() == Flower.class) {
			Flower f = (Flower) e;

			if (f.getType() == 0) {
				flower0--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.whitePetal.createNew((int) f.getX(), (int) f.getY()));
				}
			} else if (f.getType() == 1) {
				flower1--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.pinkPetal.createNew((int) f.getX(), (int) f.getY()));
				}
			} else {
				flower2--;
				for (int i = 0; i < (int) Math.ceil(Math.random() * 3); i++) {
					handler.getWorld().getItemManager()
							.addItem(Item.bluePetal.createNew((int) f.getX(), (int) f.getY()));
				}
			}

		} else if (e.getClass() == Stone.class) {
			Stone s = (Stone) e;
			int i = 1;
			if (s.getType() == 0) {
				if (s.getOrType() == 0) {
					stone0--;
				} else if (s.getOrType() == 1) {
					stone1--;
				} else {
					stone2--;
				}
				s.kill();
				i = 1;
			} else if (s.getType() == 1) {
				s.setType(s.getType() - 1);
				i = 2;
			} else {
				s.setType(s.getType() - 1);
				i = 3;
			}

			if (!loading) {
				for (int b = 0; b < i; b++) {
					itemManager.addItem(Item.metal.createNew((int) (s.getX()), (int) (s.getY() - 36)));
				}
			}

		} else if (e.getClass() == Butterfly.class) {
			butterfly--;
		} else if (e.getClass() == Bee.class) {
			bee--;
		} else if (e.getClass() == Fox.class) {
			fox--;
		}

		if (e.getClass() == Player.class) {
			Player p = (Player) e;
			if (p.lives == 0) {
				e.kill();
				e.dead = true;
				dead = true;
			} else {
				p.lives--;
				p.setX(spawnx);
				p.setY(spawny);
				p.setHealth(p.MAX_HEALTH);
				handler.log("Player lives: " + p.lives);
			}
		}

		if (e.getClass() == center.getClass() && e.dead) {
			for (Entity e2 : entityManager.getEntities()) {
				if (e2.creature) {
					center = e2;
					handler.log("Centered on: " + e2);
					return;
				}
			}
			if (entityManager.getEntities().size() > 0) {
				center = entityManager.getEntities().get(0);
				handler.log("Centered on: " + center);
				waitingForCreature = true;
			} else {
				handler.log("No more entities to center on");
				waitingForCreature = true;
			}
		}
	}

	public static int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		World.width = width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		World.height = height;
	}

	public void highestTile() {
		heights = new int[width + 2];
		for (int x = 0; x < width + 2; x++) {
			handler.log("Tile chosen: " + x);
			for (int y = 0; y < height + 1; y++) {
				handler.log("Checking if tile (" + x + ", " + y + ") is solid");
				if (getTile(x, y).isSolid()) {
					handler.log("Highest tile in area: " + y);
					heights[x] = y;
					x++;
					y = 0;
				}
			}
			handler.log("404: No tile found");
			heights[x] = -Tile.TILEHEIGHT * 2;
		}
	}

	public boolean checkCollision(int x, int y) {
		x = (int) x / Tile.TILEWIDTH;
		y = (int) y / Tile.TILEHEIGHT;
		if (getTile(x, y).isSolid()) {
			return true;
		} else {
			return false;
		}
	}

	private void addFlower(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width * Tile.TILEWIDTH + 1);
			double l = Math.random() * 2 - 1;
			handler.log("Layer = " + l);
			entityManager
					.addEntity(new Flower(handler, x, (heights[x / Tile.TILEWIDTH] - 1) * Tile.TILEHEIGHT, type, l));
		}
	}

	private void addStone(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1) * Tile.TILEWIDTH;
			entityManager.addEntity(new Stone(handler, x, (heights[x / Tile.TILEWIDTH] - 1) * Tile.TILEHEIGHT, type));
		}
	}

	private void addBee(int amount, long timer) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);
			int y = (int) (Math.random() * height + 1);

			while (getTile(x, y).isSolid()) {
				x = (int) (Math.random() * width + 1);
				y = (int) (Math.random() * height + 1);
			}

			entityManager.addEntity(new Bee(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, false, timer));
		}
	}

	private void addButterfly(int amount, long timer) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);
			int y = (int) (Math.random() * height + 1);

			while (getTile(x, y).isSolid()) {
				x = (int) (Math.random() * width + 1);
				y = (int) (Math.random() * height + 1);
			}

			entityManager.addEntity(new Butterfly(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, false, timer));
		}
	}

	private void addFox(int amount) {
		for (int i = 0; i < amount; i++) {
			int x = (int) (Math.random() * width + 1);

			while (getTile(x, 0).isSolid()) {
				x = (int) (Math.random() * width + 1);
			}

			entityManager.addEntity(new Fox(handler, x * Tile.TILEWIDTH, 0));
		}
	}

	public void addConBee(int x, int y, long timer) {
		if (bee < maxBee) {
			entityManager.addEntity(new Bee(handler, x, y, false, timer));
		} else {
			handler.log("Too many bees!");
		}
	}

	public void addConButterfly(int x, int y, long timer) {
		if (butterfly < maxButterfly) {
			entityManager.addEntity(new Butterfly(handler, x, y, false, timer));
		} else {
			handler.log("Too many butterflies!");
		}
	}

	public void addCloud(int amount, int type) {
		for (int i = 0; i < amount; i++) {
			int y = (int) (Math.random() * -handler.getHeight() + cloudY * Tile.TILEHEIGHT);
			double x = Math.random() * (Tile.TILEWIDTH * (width + 8)) - (Tile.TILEWIDTH * 4);
			entityManager.addEntity(new Cloud(handler, x, y, type, Math.random() / 2));
		}
	}
	
	private void addCannibalTribe(int amount, int groupX) {
		entityManager.addEntity(new Cannibal(handler, groupX, heights[groupX]-2, false, Public.random(0.1, 0.6), 1, true));
		for (int i = 0; i < amount-1; i++) {
			int x = (int) Public.random(groupX-2, groupX+2);
			int y = heights[x] - 2;

			entityManager.addEntity(new Cannibal(handler, x * Tile.TILEWIDTH, y * Tile.TILEHEIGHT, false, Public.random(0.51, 0.8), 1, false));
		}
	}

}
