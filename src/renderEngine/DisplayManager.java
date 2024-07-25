package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	private static final int width = 1280;
	private static final int height = 720;
	public static int fps = 120;

	private static long lastFrameTime;
	private static long lastSecondFrameTime;
	private static float deltaTime;
	private static int frameCount;
	private static int gameFps;
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("mocha game");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, width, height);
		lastFrameTime = getTime();
		lastSecondFrameTime = getTime();
	}
	public static void updateDisplay() {
		Display.sync(fps);
		Display.update();
		long currentFrameTime = getTime();
		frameCount++;
		deltaTime = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;

		//debug remove later
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) fps = 120;
		else if (Keyboard.isKeyDown(Keyboard.KEY_2)) fps = 60;
		else if (Keyboard.isKeyDown(Keyboard.KEY_3)) fps = 20;
		else if (Keyboard.isKeyDown(Keyboard.KEY_4)) fps = 10;
		else if (Keyboard.isKeyDown(Keyboard.KEY_5)) fps = 1;
		else if (Keyboard.isKeyDown(Keyboard.KEY_6)) fps = 0;

		if ((currentFrameTime - lastSecondFrameTime)/ 1000f >= 1){
			gameFps = frameCount;
			frameCount = 0;
			lastSecondFrameTime = currentFrameTime;
		}

		Display.setTitle("mocha game | fps: " + gameFps + " / " + getDeltaTime() + "ms");
	}
	public static void closeDisplay() {
		Display.destroy();
	}

	public static float getDeltaTime(){
		return deltaTime;
	}

	public static long getTime(){
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
}
