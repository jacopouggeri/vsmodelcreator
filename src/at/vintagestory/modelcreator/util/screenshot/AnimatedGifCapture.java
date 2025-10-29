package at.vintagestory.modelcreator.util.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.stream.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


import at.vintagestory.modelcreator.ModelCreator;
import org.lwjgl.opengl.GL12;

public class AnimatedGifCapture extends AnimationCapture
{
	int currentFrame = 0;
	
	GifSequenceWriter gifwriter;
	
	public AnimatedGifCapture(String filename) {
		ImageOutputStream outstream;
		try
		{
			outstream = new FileImageOutputStream(new File(filename));
			gifwriter = new GifSequenceWriter(outstream, BufferedImage.TYPE_INT_BGR, 33, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
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

			gifwriter.writeToSequence(image);
			
			currentFrame++;
			
			if (isComplete()) {
				gifwriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
