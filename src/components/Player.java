package components;

import entities.Entity;
import org.lwjgl.input.Keyboard;

import java.security.Key;

public class Player implements Component {
    public float floorHeight = 1;
    public float gravity = -0.01f;
    public float velocity = 0;
    public float speed = 0.1f;
    public float jumpVelocity = 0.17f;

    public boolean grounded = false;

    @Override
    public void awake(Entity entity) {

    }

    @Override
    public void update(Entity entity) {
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
            velocity = jumpVelocity;
        }
    }
}
