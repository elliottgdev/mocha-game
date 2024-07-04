import entities.Camera;
import entities.EntityManager;
import guis.GUIRenderer;
import guis.GUITexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import shaders.StaticShader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

	public static void main(String[] args) throws FileNotFoundException {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
		Renderer renderer = new Renderer(shader);

		Camera camera = new Camera();
		List<GUITexture> guis = new ArrayList<>();
		GUITexture gui = new GUITexture(loader.loadTexture("test"), new Vector2f(0, 0.5f), new Vector2f(0.25f, 0.25f));
		GUITexture guiTransparent = new GUITexture(loader.loadTexture("mrtransparn"), new Vector2f(0.25f, 0.5f), new Vector2f(0.25f, 0.25f));
		guis.add(gui);
		guis.add(guiTransparent);
		GUIRenderer guiRenderer = new GUIRenderer(loader);
		EntityManager entityManager = new EntityManager(renderer, shader, loader);

		while(!Display.isCloseRequested()) {
			Boolean renderWireframe = false;
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)){
				renderWireframe = true;
			}
			if (renderWireframe){
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			} else{
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}

			entityManager.update();

			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);

			shader.setPointLight(0, new Vector3f(2, 1, 2), new Vector3f(5, 0, 0), new Vector3f(5, 0, 0), new Vector3f(5, 0, 0), 1, 0.7f, 1.8f);
			shader.setDirectionalLight(new Vector3f(-0.2f, -1, -0.3f), new Vector3f(0.6f, 0.6f, 0.9f), new Vector3f(.3f, .3f, .5f), new Vector3f(0.2f, 0.2f, 0.2f));
			shader.setUniformVector("viewPos", camera.getPosition());

			shader.setMaterial(new Vector3f(0, 0.5f, 0), new Vector3f(0.4f, 0.5f, 0.4f), new Vector3f(0.04f, 0.7f, 0.04f), 0.5f, true);
			entityManager.renderEntities();
			//guiRenderer.render(guis);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
