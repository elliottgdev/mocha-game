package components;

import entities.Entity;

public class AudioSource implements Component{
    @Override
    public void update(Entity entity) {}

    @Override
    public void awake(Entity entity) {
        System.out.println("YIPPEE");
    }

    public void play(String buffer){

    }
}
