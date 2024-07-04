package components;

import entities.Entity;
import org.lwjgl.input.Keyboard;

public class Player implements Component {
    @Override
    public void update(Entity entity) {
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            entity.increasePosition(0, 0, -0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            entity.increasePosition(0, 0, 0.3f);
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            entity.increasePosition(-0.3f, 0, 0);
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            entity.increasePosition(0.3f, 0, 0);
    }
}
