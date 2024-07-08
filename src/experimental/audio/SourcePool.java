package experimental.audio;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class SourcePool {
    private List<Source3D> entityPool = new ArrayList();
    private List<Source3D> occupiedEntityPool = new ArrayList();
    private List<Source3D> freeEntityPool = new ArrayList();

    public SourcePool(int entityPoolCount){
        for (int i = 0; i < entityPoolCount; i++) {
            Source3D source3D = new Source3D();
            entityPool.add(source3D);
            freeEntityPool.add(source3D);
        }
    }

    public void playBufferOnSourcePool(String buffer, Vector3f position, float pitch, float volume, Pool pool){
        Source3D source3D;
        updatePool();
        try {
            switch (pool) {
                case entity:
                    source3D = freeEntityPool.get(0);
                    freeEntityPool.remove(0);
                    occupiedEntityPool.add(source3D);
                    occupiedEntityPool.get(occupiedEntityPool.indexOf(source3D)).play(Audio.loadSound(buffer));
                    break;
                default:
                    System.out.println("ya fucked up. not adding buffer to source pool.");
                    break;
            }
        } catch (IndexOutOfBoundsException e){
            System.out.println("Pool is full. Could not play buffer.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updatePool(){
        List<Source3D> removePool = new ArrayList<>();

        for (Source3D source3D:occupiedEntityPool){
            if (source3D.isPlaying() == false){
                removePool.add(source3D);
            }
        }

        occupiedEntityPool.removeAll(removePool);
        freeEntityPool.addAll(removePool);
    }

    public void cleanUp(){
        for (Source3D source3D:entityPool){
            source3D.delete();
        }
    }
}
