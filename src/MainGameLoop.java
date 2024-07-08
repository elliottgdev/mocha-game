import entities.Camera;
import entities.Entity;
import entities.EntityManager;
import experimental.audio.Audio;
import experimental.audio.Pool;
import experimental.audio.SourcePool;
import guis.GUIRenderer;
import guis.GUITexture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
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
		Audio.init();
		Audio.setListenerData(new Vector3f(0, 0, 0));
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

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
		Entity player = entityManager.entities.get(entityManager.getEntityByName("test"));
		SourcePool sourcePool = new SourcePool(5);
		sourcePool.playBufferOnSourcePool("res/bounce.wav", new Vector3f(3, 0, 3), 1, 1, Pool.entity);

		while(!Display.isCloseRequested()) {
			boolean renderWireframe = Keyboard.isKeyDown(Keyboard.KEY_TAB);
            if (renderWireframe){
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			} else{
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_TAB))
				sourcePool.playBufferOnSourcePool("res/bounce.wav", new Vector3f(2, 0, 3), 1, 1, Pool.entity);

			entityManager.update();

			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);

			camera.getPosition().x = player.getPosition().x;
			camera.getPosition().y = player.getPosition().y + 16;
			camera.getPosition().z = player.getPosition().z + 20;
			Audio.setListenerData(camera.getPosition());

			shader.setPointLight(0, new Vector3f(2, 1, 2), new Vector3f(5, 0, 0), new Vector3f(5, 0, 0), new Vector3f(5, 0, 0), 1, 0.7f, 1.8f);
			shader.setDirectionalLight(new Vector3f(-0.2f, -1, -0.3f), new Vector3f(0.6f, 0.6f, 0.9f), new Vector3f(.3f, .3f, .5f), new Vector3f(0.2f, 0.2f, 0.2f));
			shader.setUniformVector("viewPos", camera.getPosition());

			entityManager.renderEntities();
			//guiRenderer.render(guis);
			shader.stop();
			DisplayManager.updateDisplay();
		}

		sourcePool.cleanUp();
		Audio.cleanUp();
		guiRenderer.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
