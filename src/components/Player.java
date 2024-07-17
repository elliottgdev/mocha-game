package components;

import entities.Entity;
import experimental.levels.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.security.Key;

public class Player implements Component {
    public float floorHeight = 1, ceilingHeight = 2.3f;
    public float gravity = -0.01f;
    public float velocity = 0;
    public float speed = 0.1f;
    public float jumpVelocity = 0.17f;
    public float yModelOffset = 1f;
    public float stepHeight = 0.5f;

    private float yPosition;
    private Vector2f lastPosition;
    private float lastFloor;

    public boolean grounded = false;

    @Override
    public void awake(Entity entity) {

    }

    @Override
    public void update(Entity entity) {
        lastFloor = floorHeight;
        entity.getPosition().y = yPosition;
        boolean up = false, down = false, left = false, right = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            entity.increasePosition(0, 0, -speed);
            up = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            entity.increasePosition(0, 0, speed);
            down = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            entity.increasePosition(-speed, 0, 0);
            left = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            entity.increasePosition(speed, 0, 0);
            right = true;
        }

        Vector2f levelHeights = Level.getFloorHeight(new Vector2f(entity.getPosition().x, entity.getPosition().z));
        floorHeight = levelHeights.x;
        ceilingHeight = levelHeights.y;
        if (floorHeight != lastFloor && floorHeight > entity.getPosition().y + stepHeight){
            floorHeight = lastFloor;
            entity.getPosition().x = lastPosition.x;
            entity.getPosition().z = lastPosition.y;
        }

        if (up && left && !right && !down){
            entity.setRotY(225);
        } else if (up && right && !left && !down) {
            entity.setRotY(135);
        } else if (up && !right && !left && !down) {
            entity.setRotY(180);
        } else if (!up && right && !left && !down) {
            entity.setRotY(90);
        } else if (!up && right && !left && down) {
            entity.setRotY(45);
        } else if (!up && !right && !left && down) {
            entity.setRotY(0);
        } else if (!up && !right && left && down) {
            entity.setRotY(315);
        } else if (!up && !right && left && !down) {
            entity.setRotY(270);}

        entity.increasePosition(0, velocity, 0);
        if (entity.getPosition().y <= floorHeight){
            velocity = 0;
            entity.getPosition().y = floorHeight;
            grounded = true;
        }else {
            velocity += gravity;
            grounded = false;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && grounded){
            entity.getComponent(AudioSource.class).play("res/bounce.wav");
            velocity = jumpVelocity;
        }

        if (entity.getPosition().y > ceilingHeight) {
            velocity = 0;
            entity.getPosition().y = ceilingHeight;
        }

        lastPosition = new Vector2f(entity.getPosition().x, entity.getPosition().z);
        yPosition = entity.getPosition().y;
        entity.getPosition().y += yModelOffset;
    }
}
