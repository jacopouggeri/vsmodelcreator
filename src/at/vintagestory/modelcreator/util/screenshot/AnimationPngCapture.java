package at.vintagestory.modelcreator.util.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


import at.vintagestory.modelcreator.ModelCreator;
import org.lwjgl.opengl.GL12;

public class AnimationPngCapture extends AnimationCapture
{
	int currentFrame = 0;
	String filename;
	
	public AnimationPngCapture(String filename) {
		this.filename = filename;
		if (!filename.endsWith(".png")) filename += ".png";
	}
	
	public boolean isComplete()
	{
		return ModelCreator.currentProject.SelectedAnimation == null || currentFrame >= ModelCreator.currentProject.SelectedAnimation.GetQuantityFrames() - 1;
	}

	public void PrepareFrame()
	{
		ModelCreator.currentProject.SelectedAnimation.currentFrame = currentFrame;
	}

	public void CaptureFrame(int width, int height)
	{
		GL11.glReadBuffer(GL11.GL_FRONT);
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, buffer);

		try
		{
			BufferedImage image = GenFrame(width, height, bpp, buffer);

			String fname = filename.replace(".png", "-" + currentFrame + ".png");
			ImageIO.write(image, "PNG", new File(fname));
			
			currentFrame++;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
