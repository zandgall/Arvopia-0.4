package pre.zandgall.tiles.gfx.transform;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tran {
	private static BufferedImage createTransformed(BufferedImage image, AffineTransform at, double width, double height) {
		BufferedImage newImage = new BufferedImage(image.getWidth()*(int) Math.ceil(width), image.getHeight()*(int) Math.ceil(height), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage flip(BufferedImage image, int width, int height) {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(width, height));
		if(height==1)
			height=0;
		if(width==1)
			width=0;
		at.concatenate(AffineTransform.getTranslateInstance(width*image.getWidth(), height*image.getHeight()));
		return createTransformed(image, at, 1, 1);
	}
	
	public static BufferedImage scale(BufferedImage image, double width, double height) {
		AffineTransform at = new AffineTransform();
		at.scale(width, height);
		
		return createTransformed(image, at, width, height);
	}
	
	public static Rectangle toRect(BufferedImage image, int xOff, int yOff) {
		return new Rectangle(xOff, yOff, image.getWidth(), image.getHeight());
	}
}
