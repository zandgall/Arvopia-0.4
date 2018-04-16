package pre.zandgall.tiles;

public class Launcher {

	public static Log log = new Log("/logs/main.txt", "Main");

	public static void main(String[] args) {
		Game game = new Game("Arvopia", 720, 400, false, log);
		game.start();

		log.log("Game Launched: Arvopia");
	}

}
