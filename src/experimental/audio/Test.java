package experimental.audio;

import org.lwjgl.Sys;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Audio.init();
        Audio.setListenerData(new Vector3f(0, 0, 0));
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

        int buffer = Audio.loadSound("res/bounce.wav");
        Source3D source = new Source3D();
        source.setLooping(true);
        source.play(buffer);
        float x = 0;
        source.setPosition(new Vector3f(x, 0, 0));
        char c = ' ';
        while (c!='q'){
           // x -= 0.3f;
            source.setPosition(new Vector3f(x, 0,  0));
            Thread.sleep(40);
            System.out.println(x);
        }
        source.delete();
        Audio.cleanUp();
    }
}
