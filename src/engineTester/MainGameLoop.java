package engineTester;

import entities.Camera;
import entities.Entity;
import entities.EntityManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
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

		while(!Display.isCloseRequested()) {
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

			spinner.increaseRotation(1, 1, 1);

			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			shader.setUniformVector("viewPos", camera.getPosition());
			camera.move();
			entityManager.renderEntities();
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
