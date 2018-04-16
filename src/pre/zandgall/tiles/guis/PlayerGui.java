package pre.zandgall.tiles.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.entity.creatures.Player;
import pre.zandgall.tiles.gfx.ImageLoader;

public class PlayerGui extends Gui{
	
	Player p;
	
	private BufferedImage player, sword, torch, fists;
	
	public PlayerGui(Handler game) {
		super(game);
		p = game.getWorld().getEntityManager().getPlayer();
		
		player = ImageLoader.loadImage("/textures/Player/Player.png").getSubimage(9, 7, 18, 18);
		
		sword = ImageLoader.loadImage("/textures/Inventory/Tools/Sword/SwordStab.png");
		torch = ImageLoader.loadImage("/textures/Inventory/Tools/Torch/TorchStab.png").getSubimage(0, 0, 36, 21);
		fists = ImageLoader.loadImage("/textures/Player/PlayerPunch.png").getSubimage(49, 19, 18, 18);
	}

	@Override
	public void tick() {
		if(p != game.getWorld().getEntityManager().getPlayer()) {
			p = game.getWorld().getEntityManager().getPlayer();
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 18, 18);
		g.drawImage(player, 0, 0, null);
		g.setColor(Color.black);
		g.drawRect(0, 0, 18, 18);
		g.setFont(new Font("Dialog", Font.BOLD, 12));
		g.drawString("Lives: "+p.lives, 20, 18);
		
		g.drawImage(fists, 80, 0, null);
		g.drawString("1", 80, 10);
		g.drawImage(sword, 100, 2, null);
		g.drawString("2", 100, 10);
		g.drawImage(torch, 141, 2, null);
		g.drawString("3", 141, 10);
	}

	@Override
	public void init() {
		
	}
	
}
