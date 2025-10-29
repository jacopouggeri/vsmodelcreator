package at.vintagestory.modelcreator.util.screenshot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.vintagestory.modelcreator.util.Url;

import static at.vintagestory.modelcreator.util.screenshot.AnimationCapture.GenFrame;

public class ScreenshotCapture
{
	public static void getScreenshot(int width, int height, ScreenshotCallback callback)
	{
		try
		{
			getScreenshot(width, height, callback, File.createTempFile("screenshot", ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void getScreenshot(int width, int height, ScreenshotCallback callback, File file)
	{
		GL11.glReadBuffer(GL11.GL_FRONT);
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		try
		{
			String format = "PNG";
			BufferedImage image = GenFrame(width, height, bpp, buffer);
			ImageIO.write(image, format, file);

			if (callback != null)
				callback.callback(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isUrl(String url)
	{
		if(url == null | (url != null && url.equals("null")))
		{
			JOptionPane message = new JOptionPane();
			message.setMessage("Failed to upload screenshot. Check your internet connection then try again.");
			JDialog dialog = message.createDialog(null, "Error");
			dialog.setLocationRelativeTo(null);
			dialog.setModal(false);
			dialog.setVisible(true);
			return false;
		}
		return true;
	}

	public static void shareToFacebook(String link)
	{
		try
		{
			if (isUrl(link))
			{
				String url = "https://www.facebook.com/sharer/sharer.php?";
				url += "u=" + URLEncoder.encode(link, "UTF-8");
				Url.openUrl(url);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void shareToTwitter(String link)
	{
		try
		{
			if (isUrl(link))
			{
				String url = "https://twitter.com/intent/tweet?";
				url += "text=" + URLEncoder.encode("Check out this awesome model I created with @MrCraayfish's Model Creator", "UTF-8");
				url += "&url=" + URLEncoder.encode(link, "UTF-8");
				Url.openUrl(url);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void shareToReddit(String link)
	{
		try
		{
			if (isUrl(link))
			{
				String url = "http://www.reddit.com/r/Minecraft/submit?";
				url += "title=" + URLEncoder.encode("[Model] <enter name and description here> (Created using MrCrayfish's Model Creator)", "UTF-8");
				url += "&url=" + URLEncoder.encode(link, "UTF-8");
				Url.openUrl(url);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
