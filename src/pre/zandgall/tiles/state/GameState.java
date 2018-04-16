package pre.zandgall.tiles.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.gfx.PublicAssets;
import pre.zandgall.tiles.guis.Gui;
import pre.zandgall.tiles.guis.PlayerGui;
import pre.zandgall.tiles.utils.FileChooser;
import pre.zandgall.tiles.utils.Sound;
import pre.zandgall.tiles.worlds.World;

public class GameState extends State{
	private World world;
	
	private Gui u;
	
	private final String[] levels = new String[] {
			"/Worlds/LevelOne",
			"/Worlds/LevelTwo",
			"/Worlds/DefaultWorld",
			"/Worlds/world1.txt",
			"/Worlds/world2.txt",
			"/Worlds/Staircase.txt"
	};
	
	private Sound SweetGuitar, StarsInTheNight, Playtime;
	
	private boolean songPlaying;
	
	public boolean isSongPlaying() {
		return songPlaying;
	}
	
	public GameState(Handler handler){
		super(handler);
		
		songPlaying = true;
		
		PublicAssets.init();
		
		SweetGuitar = new Sound("Songs/SweetGuitar.wav");
		StarsInTheNight = new Sound("Songs/StarsInTheNight.wav");
		Playtime = new Sound("Songs/Playtime.wav");
		
		world = new World(handler, levels[2], false);
		handler.setWorld(world);
		
		u = new PlayerGui(handler);
		
	}
	
	public void openWorld(boolean open, int index) {
		
		world.reset();
		
		if(open) {
			
			FileChooser fileGet = new FileChooser();
	
			String i = fileGet.getFile();
			
			if(i.length()>0) {
				loadWorld(i, true);
			} else {
				State.setState(getPrev());
				handler.log("Couldn't load the world specified");
			}
		} else {
			loadWorld(levels[index], false);
		}
	}
	
	public void loadWorld(String path, boolean tf) {
		handler.log("World: "+path+" loaded");
		world = new World(handler, path, tf);
		handler.setWorld(world);
	}
	
	@Override
	public void tick() {
		world.tick();
		u.tick();
		
		songPlaying = State.getSong() != null && !(State.getSong().hasEnded());
		
		if(!songPlaying) {
			if(world.getEnviornment().getHours() > 9 && world.getEnviornment().getHours() < 19) {
				if(Math.random() < 2 && world.getEntityManager().getPlayer().health >= world.getEntityManager().getPlayer().MAX_HEALTH) {
					Playtime.setVolume(-5, true);
					Playtime.Start(0, true);
					setSong(Playtime);
				} else {
					SweetGuitar.setVolume(-5, true);
					SweetGuitar.Start(0, true);
					setSong(SweetGuitar);
				}
			} else {
				StarsInTheNight.setVolume(-5, true);
				StarsInTheNight.Start(0, true);
				setSong(StarsInTheNight);
			}
			
			handler.setVolume();
		}
		
		getSong().tick(false);
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		world.render(g, handler.getGame().get2D());
		g2d.setTransform(handler.getGame().getDefaultTransform());
		u.render(g);
	}

	@Override
	public void init() {
		
	}
	
	
	
}
