package pre.zandgall.tiles.entity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.creatures.Creature;
import pre.zandgall.tiles.entity.creatures.Player;

public class EntityManager {

	private Handler handler;
	private Player player;

	private ArrayList<Entity> entities;
	private Comparator<Entity> sort;

	public EntityManager(Handler handler, Player player) {
		this.handler = handler;
		this.player = player;
		entities = new ArrayList<Entity>();
		addEntity(player);
		sort = new Comparator<Entity>() {
			public int compare(Entity a, Entity b) {
				if (a.layer < b.layer)
					return 1;
				return -1;
			}

		};
	}

	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			if(i>=entities.size())
				return;
			entities.get(i).tick();
			if(i>=entities.size())
				return;
			if(entities.get(i).creature) {
				Creature c = (Creature) entities.get(i);
				c.regen();
			}
		}
		entities.sort(sort);
	}

	public void render(Graphics g, boolean tf) {
		for (Entity e : entities) {
			e.render(g);
			if (tf && e.creature) {
				e.showBox(g);
			}
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
		handler.log("Entity " + e + " added at (" + e.x + ", " + e.y + ")");
	}

	// GETTERS AND SETTER

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		addEntity(player);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}

}
