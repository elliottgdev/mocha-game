package guis;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GUITexture {
    private int texture;
    private Vector2f position;
    private Vector2f scale;

    public GUITexture(int texture, Vector2f position, Vector2f scale){
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getTexture() {
        return texture;
    }
}
