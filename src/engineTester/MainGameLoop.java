package engineTester;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.FileNotFoundException;

public class MainGameLoop {

	public static void main(String[] args) throws FileNotFoundException {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
		Renderer renderer = new Renderer(shader);

		RawModel monkey = OBJLoader.loadOBJModel("monkey", loader);
		RawModel stall = OBJLoader.loadOBJModel("stall", loader);
		RawModel cubeModel = OBJLoader.loadOBJModel("cube", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		ModelTexture stallTexture = new ModelTexture(loader.loadTexture("stallTexture"));
		ModelTexture nullTexture = new ModelTexture(loader.loadTexture("monkey"));
		TexturedModel texturedMonkey = new TexturedModel(monkey, nullTexture);
		TexturedModel texturedStall = new TexturedModel(stall, stallTexture);
		TexturedModel cube = new TexturedModel(cubeModel, texture);
		Entity entity = new Entity(texturedMonkey, new Vector3f (0, 0, -5), 0, 0, 0, 1);
		//Entity entity2 = new Entity(texturedStall, new Vector3f (2.5f, -1, -10), 0, 0, 0, 1);
		//Entity light = new Entity(cube, new Vector3f (-2.5f, 0, -8), 0, 0, 0, 1);
		//entity2.setRotY(180);
		Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			//entity.increaseRotation(1, 1, 1);
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
				entity.increaseRotation(-1, 0, 0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				entity.increaseRotation(1, 0, 0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				entity.increaseRotation(0, -1, 0);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				entity.increaseRotation(0, 1, 0);
			}
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			shader.setUniformVector("viewPos", camera.getPosition());
			camera.move();
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
