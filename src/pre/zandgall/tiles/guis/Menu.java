package pre.zandgall.tiles.guis;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.state.State;
import pre.zandgall.tiles.utils.*;

public class Menu extends Gui{
	
	private Button returnToGame, titleScreen, options, exit;
	
	
	public Menu(Handler game) {
		super(game);
		
		exit = new Button(game, game.getWidth()/2 - 10, game.getHeight()/2 - 40, 45, 20, new BufferedImage[] {}, "Quits the application", "Quit");
		options = new Button(game, game.getWidth()/2 - 30, game.getHeight()/2-70, 80, 20, new BufferedImage[] {}, "Takes you to the options menu", "Options");
		returnToGame = new Button(game, game.getWidth()/2-65, game.getHeight()/2-130, 150, 20, new BufferedImage[] {}, "Takes you back the the current game", "Return to Game");
		titleScreen = new Button(game, game.getWidth()/2-50, game.getHeight()/2-100, 120, 20, new BufferedImage[] {}, "Takes you to the title screen", "Title Screen");
	}

	@Override
	public void tick() {
		
		if(exit.on)
			game.getGame().stop();
		
		if(returnToGame.on) {
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		if(options.on) {
			State.setState(game.getGame().optionState);
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		if(titleScreen.on) {
			State.setState(game.getGame().menuState);
			game.getGame().unPause();
			game.getWorld().getEntityManager().getPlayer().viewMenu = false;
		}
		
		exit.tick();
		options.tick();
		returnToGame.tick();
		titleScreen.tick();
	}

	@Override
	public void render(Graphics g) {
		exit.render(g);
		options.render(g);
		returnToGame.render(g);
		titleScreen.render(g);
	}

	@Override
	public void init() {
		
	}

}
