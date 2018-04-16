package pre.zandgall.tiles.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pre.zandgall.tiles.Game;
import pre.zandgall.tiles.Handler;
import pre.zandgall.tiles.utils.Button;
import pre.zandgall.tiles.utils.LoaderException;
import pre.zandgall.tiles.utils.Slider;
import pre.zandgall.tiles.utils.ToggleButton;
import pre.zandgall.tiles.utils.Utils;

public class OptionState extends State{
	
	public Slider fps, timeSpeed, scale, lightQuality, volume;
	private Button back;
	private ToggleButton sliderProblem;
	
	public OptionState(Handler handler) {
		super(handler);
		
		back = new Button(handler, 10, handler.getHeight()-30, 55, 20, new BufferedImage[] {}, "Brings you back to the main menu", "Back");
		//screenset =
		new Button(handler, 10, 140, 55, 20, new BufferedImage[] {}, "Switches between fullscreen and deafault size", "Fullscreen");
		volume = new Slider(handler, 0, 100, 75, true, "Volume");
		sliderProblem = new ToggleButton(handler, 10, 140, 130, 20, new BufferedImage[] {}, "Turn on if you're having problems with the sliders", "Slider Debug");
		fps = new Slider(handler, 1, 90, 60, true, "FPS");
		scale = new Slider(handler, 1, 25, 5, true, "Scale");
		timeSpeed = new Slider(handler, 0, 60, 1, true, "Time Speed");
		lightQuality = new Slider(handler, 1, 36, 6, true, "Light Quality");
		
		if(LoaderException.readFile("C:\\Arvopia\\Options.txt") != null) {
			
			String[] tokens = new String[5]; 
			tokens = LoaderException.readFile("C:\\Arvopia\\Options.txt").split("\\s+");
			
			fps.setValue(Utils.parseInt(tokens[0]));
			timeSpeed.setValue(Utils.parseInt(tokens[1]));
			scale.setValue(Utils.parseInt(tokens[2]));
			lightQuality.setValue(Utils.parseInt(tokens[3]));
			volume.setValue(Utils.parseInt(tokens[4]));
			
			sliderProblem.on = Utils.parseBoolean(tokens[5]);
		}
	}

	@Override
	public void tick() {
		
		Game.scale = scale.getWholeValue()/5;
		
		if(back.on) {
			State.setState(State.getPrev());
			
			String filename = "C:\\Arvopia\\Options.txt";
			
			Utils.fileWriter(fps.getValue()+" "+timeSpeed.getValue()+" "+scale.getValue()+ " "+lightQuality.getValue()+" "+volume.getValue()+" "+sliderProblem.on, filename);
			
			
		}
		
		handler.getMouse().setSliderMalfunction(sliderProblem.on);
		
//		screenset.tick();
		lightQuality.tick(130, 20);
		sliderProblem.tick();
		back.tick();
		timeSpeed.tick(15, 60);
		fps.tick(15, 20);
		scale.tick(15, 100);
		volume.tick(130, 60);
		
		handler.getWorld().getEnviornment().lightQuality = lightQuality.getValue();
		
		handler.getGame().setFps(fps.getValue());
		handler.getWorld().getEnviornment().setTimeSpeed(timeSpeed.getValue());
		
		handler.setVolume();
		
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(134,200,255));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
//		screenset.render(g);
		sliderProblem.render(g);
		back.render(g);
		lightQuality.render(g);
		volume.render(g);
		timeSpeed.render(g);
		fps.render(g);
		scale.render(g);
	}

	@Override
	public void init() {
		
	}

}
