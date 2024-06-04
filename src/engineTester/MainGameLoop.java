package engineTester;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) throws FileNotFoundException {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		float[] vertices = {
				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f

		};

		float[] textureCoords = {

				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0


		};

		int[] indices = {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};

		 RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		 RawModel monkey = OBJLoader.loadOBJModel("monkey", loader);
		 ModelTexture texture = new ModelTexture(loader.loadTexture("test"));
		 ModelTexture nullTexture = new ModelTexture(loader.loadTexture("null"));
		 TexturedModel texturedModel = new TexturedModel(model,texture);
		 TexturedModel texturedMonkey = new TexturedModel(monkey, nullTexture);
		 Entity entity = new Entity(texturedMonkey, new Vector3f (-2.5f, 0, -10), 0, 0, 0, 1);
		 Entity cube = new Entity(texturedModel, new Vector3f (2.5f, 0, -10), 0, 0, 0, 1);
		 Camera camera = new Camera();
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(1, 1, 1);
			cube.increaseRotation(-1, -1, -1);
			renderer.prepare();
			shader.start();
			shader.loadViewMatrix(camera);
			camera.move();
			renderer.render(entity, shader);
			renderer.render(cube, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
