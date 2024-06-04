package models;

import renderEngine.Loader;
import textures.ModelTexture;

import java.io.FileNotFoundException;

public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel (RawModel model, ModelTexture texture) {
			this.rawModel = model;
			this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
}
