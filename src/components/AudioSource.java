package components;

import entities.Entity;
import experimental.audio.Pool;
import experimental.audio.SourcePool;

public class AudioSource implements Component{
    SourcePool sourcePool = SourcePool.getSourcePool();
    Entity entity;

    @Override
    public void update(Entity entity) {}

    @Override
    public void tick(Entity entity) {

    }

    @Override
    public void awake(Entity entity) {
        this.entity = entity;
        System.out.println("YIPPEE");
    }

    public void play(String buffer){
        sourcePool.playBufferOnSourcePool(buffer, entity.getPosition(), 1, 1, Pool.entity);
    }
}
