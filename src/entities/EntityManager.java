package entities;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityManager {
    public List<Entity> entities = new ArrayList<>();
    Renderer renderer;
    StaticShader shader;

    public EntityManager(Renderer renderer, StaticShader shader, Loader loader){
        this.renderer = renderer;
        this.shader = shader;

        File dir = new File("entities/ntt");
        File[] directoryListing = dir.listFiles();

        FileReader fr = null;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    fr = new FileReader(child);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                String line = " ads";
                BufferedReader reader = new BufferedReader(fr);

                //entity base component
                String entName = "PLACEHOLDER";
                String entModel = "null";
                String entTexture = "null";
                Vector3f position = new Vector3f(0, 0 ,0);
                Vector3f rotation = new Vector3f(0, 0, 0);
                float scale = 1;

                try {
                    while (true){
                        line = reader.readLine();
                        String[] currentLine = line.split(" ");

                        if (line.startsWith("! ")){
                            System.out.println(Arrays.asList(currentLine));
                            entName = currentLine[1];
                            entModel = currentLine[2];
                            entTexture = currentLine[3];
                        } else if (line.startsWith("@ ")) {
                            System.out.println(Arrays.asList(currentLine));
                            String[] transformMemory;
                            transformMemory = currentLine[1].split(",");
                            position = new Vector3f(Float.parseFloat(transformMemory[0]), Float.parseFloat(transformMemory[1]), Float.parseFloat(transformMemory[2]));
                            transformMemory = currentLine[2].split(",");
                            rotation = new Vector3f(Float.parseFloat(transformMemory[0]), Float.parseFloat(transformMemory[1]), Float.parseFloat(transformMemory[2]));
                            scale = Integer.parseInt(currentLine[3]);
                        } else if (line.startsWith("end ")){
                            RawModel rawModel = OBJLoader.loadOBJModel(entModel, loader);
                            ModelTexture modelTexture = new ModelTexture(loader.loadTexture(entTexture));
                            TexturedModel texturedModel = new TexturedModel(rawModel, modelTexture);
                            entities.add(new Entity(entName, texturedModel, position, rotation.x, rotation.y, rotation.z, scale));
                            break;
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
    }

    public void renderEntities(){
        for (Entity entity:entities)
            renderer.render(entity, shader);
    }

    public int getEntityByName(String string){
        int index = 0;
        for (int i = 0; i < entities.size(); i++) {
            if (Objects.equals(entities.get(i).name, string)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
