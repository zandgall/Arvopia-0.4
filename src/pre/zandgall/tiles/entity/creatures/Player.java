package pre.zandgall.tiles.entity.creatures;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.Entity;
import pre.zandgall.tiles.entity.moveableStatics.Beehive;
import pre.zandgall.tiles.entity.moveableStatics.Cloud;
import pre.zandgall.tiles.gfx.Animation;
import pre.zandgall.tiles.gfx.Assets;
import pre.zandgall.tiles.gfx.ImageLoader;
import pre.zandgall.tiles.gfx.transform.Tran;
import pre.zandgall.tiles.guis.Inventory;
import pre.zandgall.tiles.guis.Menu;
import pre.zandgall.tiles.input.MouseManager;
import pre.zandgall.tiles.items.tools.*;
import pre.zandgall.tiles.tiles.Tile;
import pre.zandgall.tiles.utils.Public;

public class Player extends Creature {

	// Items
	public int metal;
	public int petals;
	public int honey;
	public int foxFur;
	public int butterflyWing;

	private Tool currentTool;
	private Torch torch;
	private Sword sword;

	// Fall Damage
	private double lastHeight;

	// Attacking
	private boolean attackReady;

	private Rectangle attack;
	private boolean attacking, primed;
	private int attackDelay, delayRange, damage;

	// Timing
	private long timer;

	//
	private long attackTimer;

	// Inventory
	public Inventory inventory;
	public Menu menu;

	public boolean viewInventory;
	public boolean viewMenu;

	// Create animations
	private Animation jump;
	private Animation still;
	private Animation crouch;
	private Animation walk;

	private Animation punch;
	private Animation stab;

	private Animation hold;
	private BufferedImage airKick;

	private Assets player;

	private boolean jumping = false;

	int renderCount = 0;

	int widthFlip = 1;

	private MouseManager mouse;
	private int mouseX, mouseY;
	@SuppressWarnings("unused")
	private boolean leftMouse, fullLeftMouse, rightMouse;

	public int lives;

	Entity closest;

	public Player(Handler handler, double x, double y, boolean direction, double speed, int lives) {
		super(handler, x, y, DEFAULT_CREATURE_WIDTH, 47, direction, speed, DEFAULT_ACCELERATION, (int) MAX_SPEED, false,
				false, DEFAULT_JUMP_FORCE, DEFAULT_JUMP_CARRY, "Player");

		mouse = handler.getMouse();

		attack = new Rectangle((int) x, (int) y, 72, 54);

		health = 20;
		MAX_HEALTH = 20;

		layer = 0;

		attackReady = false;
		attacking = false;
		attackDelay = 15;
		delayRange = 15;
		damage = 1;

		timer = 0;
		attackTimer = 0;

		this.lives = lives;

		torch = new Torch(handler);
		sword = new Sword(handler);

		inventory = new Inventory(handler);
		menu = new Menu(handler);

		bounds.x = 12;
		bounds.y = 8;
		bounds.width = 10;
		bounds.height = 45;

		player = new Assets(ImageLoader.loadImage("/textures/Player/Player.png"), 36, 54, "Player");

		// Initiate animations
		jump = new Animation(1000, new BufferedImage[] { player.get(0, 1) }, "Jump", "Player");
		still = new Animation(750, new BufferedImage[] { player.get(0, 0), player.get(1, 0) }, "Still", "Player");
		walk = new Animation(250, new BufferedImage[] { player.get(1, 1), player.get(3, 1) }, "Walk", "Player");
		crouch = new Animation(750, new BufferedImage[] { player.get(2, 0), player.get(3, 0) }, "Crouch", "Player");

		player = new Assets(ImageLoader.loadImage("/textures/Player/PlayerPunch.png"), 36, 54, "Player Punching");
		punch = new Animation(150, new BufferedImage[] { player.get(1, 0), player.get(2, 0), player.get(0, 0) },
				"Punch", "Player");
		airKick = ImageLoader.loadImage("/textures/Player/PlayerAirKick.png");

		player = new Assets(ImageLoader.loadImage("/textures/Player/PlayerHolding.png"), 36, 54, "Player Holding");
		hold = new Animation(750, new BufferedImage[] { player.get(0, 0), player.get(1, 0) }, "Hold", "Player");

		player = new Assets(ImageLoader.loadImage("/textures/Player/PlayerStab.png"), 36, 54, "Player Stabbing");
		stab = new Animation(150, new BufferedImage[] { player.get(1, 0), player.get(2, 0), player.get(3, 0) }, "Stab",
				"Player");
	}

	public void tick() {
		
		if(currentTool != null && currentTool.hasLight()) {
			currentTool.getLight().turnOff();
			currentTool.getLight().setX(-200);
			currentTool.getLight().setY(-200);
		}
		
		if(game.getKeyManager().k2) {
			currentTool = sword;
			delayRange = 15;
			attackDelay = 15;
			damage = 3;
		} else if(game.getKeyManager().k3) {
			currentTool = torch;
			delayRange = 15;
			attackDelay = 15;
			damage = 2;
		} else if(game.getKeyManager().k1) {
			currentTool = null;
			delayRange = 15;
			attackDelay = 15;
			damage = 1;
		} 
		if(currentTool != null)
			currentTool.tick();

		inventory.tick();
		if (viewMenu) {
			menu.tick();
		}

		getInput();

		if (!game.getGame().paused) {
			if (bottoms) {
				if (y - lastHeight > 180) {
					damage((int) Math.floor((y - lastHeight) / 180));
				}

				lastHeight = y;
			} else {
				if (y < lastHeight)
					lastHeight = y;
			}

			mouseX = mouse.getMouseX();
			mouseY = mouse.getMouseY();
			fullLeftMouse = mouse.fullLeft;
			leftMouse = mouse.isLeft();
			rightMouse = mouse.isRight();

			if (!mouse.isIn()) {
				viewMenu = true;
				game.getGame().pause();
			}

			// If it's attacking, and the left mouse button was let go of
			if (!attacking) {
				attack.x = (int) x;
				attack.y = (int) y;
			}

			if (!fullLeftMouse)
				attacking = false;

			// Animation ticks
			jump.tick();
			still.tick();
			walk.tick();
			crouch.tick();

			punch.tick();
			hold.tick();
			stab.tick();

			game.getWorld().outOfBounds(this);
			move();

			checkMouse();
			game.getKeyManager().typed = false;

			if (timer >= 1000000)
				timer = 0;
			if (attackTimer >= 1000000)
				attackTimer = 0;
			if (attackTimer == attackDelay) {
				attackReady = true;
			} else if (attackTimer < attackDelay || attackTimer > attackDelay + delayRange) {
				attackReady = false;
				if (attackTimer > attackDelay + delayRange)
					attackTimer = 0;
			}

			if (bottoms && jumping) {
				jumping = false;
			}

			timer++;
			attackTimer++;
		}
	}

	private void getInput() {
		setxMove(0);
		yMove = 0;

		if (viewInventory) {
			if (game.getKeyManager().invtry && game.getKeyManager().typed) {
				viewInventory = false;
			}
		} else if (viewMenu) {
			if (game.getKeyManager().esc && game.getKeyManager().typed) {
				viewMenu = false;
				game.getGame().unPause();
			}
		} else {

			if (game.getKeyManager().invtry && game.getKeyManager().typed) {
				viewInventory = true;
			}

			if (game.getKeyManager().esc && game.getKeyManager().typed) {
				viewMenu = true;
				game.getGame().pause();
				game.getKeyManager().typed = false;
			}
			
			if(!game.getKeyManager().down) {
				if (xVol > 0)
					setxMove((float) (getxMove() + (speed + xVol)));
				if (xVol < 0)
					setxMove((float) (getxMove() + (-speed + xVol)));
			}

			if (game.getKeyManager().left) {
				if (!game.getKeyManager().down) {
					if (xVol < maxSpeed)
						if (xVol > 0)
							xVol -= FRICTION * 2;
					xVol -= acceleration;
				}
				widthFlip = -1;
			} else if (game.getKeyManager().right) {
				widthFlip = 1;
				if (!game.getKeyManager().down) {
					if (xVol < maxSpeed)
						if (xVol < 0)
							xVol += FRICTION * 2;
					xVol += acceleration;
				}
			} else {
				if(!game.getKeyManager().down) {
					float txv = (float) xVol;
					if (xVol < 0)
						xVol += acceleration + FRICTION;
					else if (xVol > 0)
						xVol -= acceleration + FRICTION;
					if ((txv > 0 && xVol < 0) || (txv < 0 && xVol > 0)) {
						xVol = 0;
					}
				}
			}

			if (game.getKeyManager().b) {
				game.log("Marked X: " + Math.round(x) + ", Y: "
						+ Math.round(y + bounds.y + bounds.height - Tile.TILEHEIGHT) + " Tile: ("
						+ Math.round(x / Tile.TILEWIDTH) + ", "
						+ Math.round((y + bounds.y + bounds.getHeight()) / Tile.TILEHEIGHT - 1) + ")"
						+ " Tile full X Y: (" + Math.round(x / Tile.TILEWIDTH) * Tile.TILEWIDTH + ", "
						+ Math.round((y + bounds.y + bounds.getHeight()) / Tile.TILEHEIGHT - 1) * Tile.TILEHEIGHT
						+ ")");
				game.log("X and Y offset: " + game.getGameCamera().getxOffset() + ", "
						+ game.getGameCamera().getyOffset());
			}

			if (game.getKeyManager().up) {
				if (bottoms && !jumping) {
					yVol = (float) -DEFAULT_JUMP_FORCE;
					jumping = true;
				} else if (yVol < 0) {
					yVol -= DEFAULT_JUMP_CARRY;
				}
			}
			
			if(game.getKeyManager().down) {
				if (xVol > 0) {
					xVol=xVol*0.75;
					setxMove((float) (xVol + speed));
					if(xVol < 0.001)
						xVol=0;
				}
				
				if (xVol < 0) {
					xVol=xVol*0.75;
					setxMove((float) (xVol - speed));
					if(xVol > -0.001)
						xVol=0;
				}
				
				
				bounds.y=37;
				bounds.height=16;
			} else {
				bounds.y=7;
				bounds.height=46;
			}
			
		}

	}

	private void checkMouse() {

		boolean first = (punch.getFrame() == punch.getArray()[2] || stab.getFrame() == stab.getArray()[2]);

		if (getClosest(mouseX + game.xOffset(), mouseY + game.yOffset()) != null && closest != getClosest(mouseX + game.xOffset(), mouseY + game.yOffset())
				&& getClosest(mouseX + game.xOffset(), mouseY + game.yOffset()) != this
				&& getClosest(mouseX + game.xOffset(), mouseY + game.yOffset()).getClass() != Cloud.class) {
			closest = getClosest(mouseX + game.xOffset(), mouseY + game.yOffset());
		}

		boolean thereCanOnlyBeOne = true;

		if (leftMouse && !viewInventory) {

			double cx = mouseX + game.xOffset();
			double cy = mouseY + game.yOffset();
			if (closest != null) {
				boolean a = (cx > closest.getX() - closest.getWidth() / 2
						&& cx < closest.getX() + closest.getbounds().x + closest.getbounds().width * 1.5);
				boolean b = (cy > closest.getY() - closest.getHeight() / 2
						&& cy < closest.getY() + closest.getbounds().height * 1.5);

				if (a && b && mouse.isClicked() && !closest.creature) {

					thereCanOnlyBeOne = false;

					if (closest.getClass() == Beehive.class) {

						honey += (int) Math.ceil(Math.random() * 4);
						game.getWorld().kill(closest);

					} else {
						game.getWorld().kill(closest);
					}

					closest = null;
				}
			}
		}
		if (fullLeftMouse && thereCanOnlyBeOne && attackReady) {
			punch.setFrame(0);
			attacking = true;
			// Attack
			if (widthFlip == 1) { // Set bounds in correct direction
				attack.x = (int) (x);
			} else {
				attack.x = (int) (x - 72 + bounds.x + bounds.width);
			}
			attack.y = (int) y;
			// Check if it hits anything

			ArrayList<Creature> c = getInRadius(x + bounds.getCenterX(), y + bounds.getCenterY(), 100);

			for (Creature e : c) {
				if (attackReady && first) {
					game.log("GO");
					attackReady = false;
					// if (Public.dist(e.getX()+e.getbounds().getCenterX(),
					// e.getY()+e.getbounds().getCenterY(), x+bounds.getCenterX(),
					// y+bounds.getCenterY()) < 100) {
					Creature d = (Creature) e;
					d.damage(damage);
					game.log("Damaged: " + d.health);
					if (d.dead)
						if (d.getClass() == Fox.class) {
							foxFur++;
						} else if (d.getClass() == Butterfly.class) {
							butterflyWing += 2;
						} else if (d.getClass() == Bee.class) {
							honey++;
						}
					// }
				}
			}
		} else if (fullLeftMouse && !attacking && !attackReady) {
			primed = true;
			attacking = false;
			punch.setFrame(0);
			stab.setFrame(0);
		} else {
			primed = false;
		}
	}

	public void render(Graphics g) {

		if (currentTool != null) {
			if (getFrame() != stab.getFrame()) {
				g.drawImage(Tran.flip(currentTool.texture(), widthFlip, 1),
						(int) (x - game.getGameCamera().getxOffset()) + (getToolxoffset()),
						(int) (y - game.getGameCamera().getyOffset()) + getToolyoffset(), null);
				currentTool.custom1((int) Public.xO(x + getToolxoffset() + currentTool.texture().getWidth()),
						(int) Public.yO(y));
			} else {
				currentTool.custom1((int) Public.xO(x + getToolxoffset() + currentTool.getFrame().getWidth()),
						(int) Public.yO(y));
			}
		}

		g.drawImage(Tran.flip(getFrame(), widthFlip, 1), (int) Public.xO(x), (int) Public.yO(y), null);

		if (currentTool != null)
			if (getFrame() == stab.getFrame())
				g.drawImage(Tran.flip(currentTool.getFrame(), widthFlip, 1), (int) Public.xO(x) + (getToolxoffset()),
						(int) Public.yO(y) + getToolyoffset(), null);

		if (closest != null) {
			if (closest.creature) {
				Creature c = (Creature) closest;
				c.showHealthBar(g);
			} else {
				closest.showBox(g);
			}
		}

		if (health < MAX_HEALTH) {
			showHealthBar(g);
		}

		renderCount++;
	}

	public int getToolxoffset() {
		if (getFrame() == airKick || getFrame() == jump.getFrame())
			if (widthFlip == 1)
				return 33 - currentTool.texture().getWidth() / 2;
			else
				return 3 - currentTool.texture().getWidth() / 2;
		if (getFrame() == crouch.getFrame())
			if (widthFlip == 1)
				return 28 - currentTool.texture().getWidth() / 2;
			else
				return 8 - currentTool.texture().getWidth() / 2;
		if (getFrame() == walk.getFrame())
			if (walk.getFrame() == walk.getArray()[0])
				if (widthFlip == 1)
					return 33 - currentTool.texture().getWidth() / 2;
				else
					return 3 - currentTool.texture().getWidth() / 2;
			else if (walk.getFrame() == walk.getArray()[1])
				if (widthFlip == 1)
					return 26 - currentTool.texture().getWidth() / 2;
				else
					return 10;
			else if (widthFlip == 1)
				return 25 - currentTool.texture().getWidth() / 2;
			else
				return 14 - currentTool.texture().getWidth() / 2;
		if (getFrame() == hold.getFrame())
			if (widthFlip == 1)
				return 33 - currentTool.texture().getWidth() / 2;
			else
				return 3 - currentTool.texture().getWidth() / 2;
		if (getFrame() == stab.getFrame())
			if (stab.getFrame() == stab.getArray()[0])
				if (widthFlip == 1)
					return 16;
				else
					return 20 - currentTool.getFrame().getWidth();
			else if (stab.getFrame() == stab.getArray()[1])
				if (widthFlip == 1)
					return 13;
				else
					return 23 - currentTool.getFrame().getWidth();
			else if (widthFlip == 1)
				return 33;
			else
				return 3 - currentTool.getFrame().getWidth();
		return 0;
	}

	public int getToolyoffset() {
		if (getFrame() == airKick || getFrame() == jump.getFrame())
			return 40 - currentTool.texture().getHeight();

		if (getFrame() == crouch.getFrame())
			return 50 - currentTool.texture().getHeight();

		if (getFrame() == walk.getFrame())
			if (walk.getFrame() == walk.getArray()[0])
				return 40 - currentTool.texture().getHeight();

			else if (walk.getFrame() == walk.getArray()[1])
				return 42 - currentTool.texture().getHeight();

			else
				return 25 - currentTool.texture().getHeight();

		if (getFrame() == hold.getFrame())
			if (hold.getFrame() == hold.getArray()[0])
				return 40 - currentTool.texture().getHeight();
			else
				return 41 - currentTool.texture().getHeight();

		if (getFrame() == stab.getFrame())
			if (stab.getFrame() == stab.getArray()[0])
				return 40 - currentTool.getYOffset();

			else if (stab.getFrame() == stab.getArray()[1])
				return 42 - currentTool.getYOffset();
			else
				return 33 - currentTool.getYOffset();

		return 33 - currentTool.texture().getHeight() / 2;
	}

	public void renScreens(Graphics g) {

		game.getGame().get2D().setTransform(game.getGame().getDefaultTransform());

		if (viewInventory) {
			inventory.render(g);
		}
		if (viewMenu) {
			menu.render(g);
		}
	}

	private BufferedImage getFrame() {

		checkCol();

		 if (game.getKeyManager().down) {
				return crouch.getFrame();
		} else if (!bottoms || jumping) {
			if (attacking && punch.getFrame() == punch.getArray()[2] && attackReady) {
				return airKick;
			}
			return jump.getFrame();
		} else if (Math.round(Math.abs(getxMove()) - speed + 0.49) > 0) {
			return walk.getFrame();
		} else if (attacking || primed) {
			if (currentTool != null)
				return stab.getFrame();
			return punch.getFrame();
		} else {
			if (currentTool != null)
				return hold.getFrame();
			
			if(tops)
				return crouch.getFrame();
			return still.getFrame();
		}

	}

	public void reset() {
		player.reset(ImageLoader.loadImage("/textures/Player/Player.png"), 36, 54, "Player");
	}

	public int getMetal() {
		return metal;
	}

	public void setMetal(int metal) {
		this.metal = metal;
	}

	public int getPetals() {
		return petals;
	}

	public void setPetals(int petals) {
		this.petals = petals;
	}

	public void checkCol() {

		down = false;
		left = false;
		right = false;
		top = false;
		bottom = false;
		lefts = false;
		rights = false;
		tops = false;
		bottoms = false;

		int ty = (int) ((y + yMove + bounds.y + bounds.height) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			bottom = true;
		} else if (collisionWithDown((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithDown((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)) {
			if (y + bounds.y + bounds.height < ty * Tile.TILEHEIGHT + 4) {

				down = true;
			}

			if (y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT + 1 && yMove >= 0) {
				bottoms = true;
				bottom = true;
			}
		}
		ty = (int) ((y + yMove + bounds.y + bounds.height + 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove + 1)
				|| ((collisionWithDown((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
						|| collisionWithDown((int) ((x + bounds.x + bounds.width + 2) / Tile.TILEWIDTH), ty))
						&& y + bounds.y + bounds.height <= ty * Tile.TILEHEIGHT + 1 && !jumping)) {
			bottoms = true;
		}

		ty = (int) ((y + yMove + bounds.y) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove)) {
			top = true;
		}
		ty = (int) ((y + yMove + bounds.y - 2) / Tile.TILEHEIGHT);
		if (collisionWithTile((int) ((x + bounds.x + 2) / Tile.TILEWIDTH), ty)
				|| collisionWithTile((int) ((x + bounds.x + bounds.width - 2) / Tile.TILEWIDTH), ty)
				|| checkCollision(0f, yMove - 1)) {
			tops = true;
		}

		int tx = (int) ((x + getxMove() + bounds.x + bounds.width) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			right = true;
		}
		tx = (int) ((x + getxMove() + bounds.x + bounds.width + 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() + 1, 0f)) {
			rights = true;
		}

		tx = (int) ((x + getxMove() + bounds.x) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove(), 0f)) {
			left = true;
		}
		tx = (int) ((x + getxMove() + bounds.x - 2) / Tile.TILEWIDTH);
		if (collisionWithTile(tx, (int) (y + bounds.y + 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 2) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height / 4) / Tile.TILEHEIGHT)
				|| collisionWithTile(tx, (int) (y + bounds.y + bounds.height * 0.75) / Tile.TILEHEIGHT)
				|| checkCollision(getxMove() - 1, 0f)) {
			lefts = true;
		}
	}

}
