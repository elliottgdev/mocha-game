package components;

import entities.Entity;

import java.util.List;

public interface Component {
    //runs every frame
    void update(Entity entity);
    //runs on entity creation
    void awake(Entity entity);
}
