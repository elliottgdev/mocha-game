package components;

import entities.Entity;

import java.util.List;

public interface Component {
    //runs every frame
    void update(Entity entity);
    //runs every tick (60 ticks per second)
    //to edit tick rate, visit EntityManager.update()
    void tick(Entity entity);
    //runs on entity creation
    void awake(Entity entity);
}
