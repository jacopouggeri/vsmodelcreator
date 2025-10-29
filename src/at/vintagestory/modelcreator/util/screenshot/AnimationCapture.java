package at.vintagestory.modelcreator.util.screenshot;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public abstract class AnimationCapture
{
	public abstract boolean isComplete();
	
	public abstract void PrepareFrame();
	
	public abstract void CaptureFrame(int width, int height);

	public static BufferedImage GenFrame(int width, int height, int bpp, ByteBuffer buffer) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return image;
	}
}
