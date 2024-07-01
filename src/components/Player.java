package components;

import entities.Entity;

public class Player implements Component {
    @Override
    public void update(Entity entity) {
        entity.increaseRotation(1, 1, 1);
    }
}
