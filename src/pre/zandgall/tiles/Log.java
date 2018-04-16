package pre.zandgall.tiles;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

	Logger logger;

	public Log(String file, String source) {
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler(file);
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			
			// Log init
//			logger.info("Logger initiated");
			this.log("Logger Initiated for "+source);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(String log) {
		System.out.println(log);
//		logger.info(log);
	}

	public void out(String string) {
		System.out.println(string);
	}
}
