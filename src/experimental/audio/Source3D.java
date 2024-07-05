package experimental.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class Source3D {
    private int sourceId;

    public Source3D(){
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, 0);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, 10);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, 20);
    }

    public void setVolume(float volume){
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void setPitch(float pitch){
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position){
        AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public void setVelocity(Vector3f velocity){
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

    public void setLooping(boolean looping){
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, looping ? AL10.AL_TRUE:AL10.AL_FALSE);
    }

    public boolean isPlaying(){
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void play(int buffer){
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        unpause();
    }

    public void pause(){
        AL10.alSourcePause(sourceId);
    }

    public void unpause(){
        AL10.alSourcePlay(sourceId);
    }

    public void stop(){
        AL10.alSourceStop(sourceId);
    }

    public void delete(){
        stop();
        AL10.alDeleteSources(sourceId);
    }
}
