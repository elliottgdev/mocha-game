package models;

import shaders.StaticShader;

public class LevelModel {
    TexturedModel texturedModel;
    float scale;

    public LevelModel(TexturedModel texturedModel, float scale){
        this.texturedModel = texturedModel;
        this.scale = scale;
    }

    public TexturedModel getModel(){
        return texturedModel;
    }

    public float getScale(){
        return scale;
    }
}
