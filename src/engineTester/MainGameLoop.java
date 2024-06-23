package engineTester;

import entities.Camera;
import entities.Entity;
import entities.EntityManager;
import models.LevelModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;

import java.io.FileNotFoundException;

public class MainGameLoop {

	public static void main(String[] args) throws FileNotFoundException {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
		Renderer renderer = new Renderer(shader);

		Camera camera = new Camera();

		EntityManager entityManager = new EntityManager(renderer, shader, loader);
		Entity entity = entityManager.entities.get(entityManager.getEntityByName("test"));
		Entity spinner = entityManager.entities.get(entityManager.getEntityByName("test2"));

		LevelModel levelModel = new LevelModel(loader.createTexturedModel("level", "grass"), 1);

		while(!Display.isCloseRequested()) {
			Boolean renderWireframe = false;
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
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)){
				renderWireframe = true;
			}
			if (renderWireframe){
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			} else{
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}

			spinner.increaseRotation(1, 1, 1);

			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			shader.setUniformVector("viewPos", camera.getPosition());
			camera.move();
			shader.setMaterial(new Vector3f(0, 0.05f, 0), new Vector3f(0.4f, 0.5f, 0.4f), new Vector3f(0.04f, 0.7f, 0.04f), 0.078125f, true);
			renderer.renderLevel(levelModel, shader);
			entityManager.renderEntities();
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
