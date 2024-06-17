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
import java.util.*;

public class EntityManager {
    public List<Entity> entities = new ArrayList<>();
    Renderer renderer;
    StaticShader shader;

    //material
    List<Vector3f> ambient = new ArrayList<>();
    List<Vector3f> diffuse = new ArrayList<>();
    List<Vector3f> specular = new ArrayList<>();
    List<Float> shininess = new ArrayList<>();
    List<Boolean> textured = new ArrayList<>();

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

                //transform component
                Vector3f position = new Vector3f(0, 0 ,0);
                Vector3f rotation = new Vector3f(0, 0, 0);
                float scale = 1;

                try {
                    while (true){
                        line = reader.readLine();
                        String[] currentLine = line.split(" ");

                        if (line.startsWith("! ")){
                            //System.out.println(Arrays.asList(currentLine));
                            entName = currentLine[1];
                            entModel = currentLine[2];
                            entTexture = currentLine[3];
                        } else if (line.startsWith("@ ")) {
                            //System.out.println(Arrays.asList(currentLine));
                            String[] transformMemory;
                            transformMemory = currentLine[1].split(",");
                            position = new Vector3f(Float.parseFloat(transformMemory[0]), Float.parseFloat(transformMemory[1]), Float.parseFloat(transformMemory[2]));
                            transformMemory = currentLine[2].split(",");
                            rotation = new Vector3f(Float.parseFloat(transformMemory[0]), Float.parseFloat(transformMemory[1]), Float.parseFloat(transformMemory[2]));
                            scale = Integer.parseInt(currentLine[3]);
                        } else if (line.startsWith("# ")) {
                            String[] matMemory;
                            matMemory = currentLine[1].split(",");
                            ambient.add(new Vector3f(Float.parseFloat(matMemory[0]), Float.parseFloat(matMemory[1]), Float.parseFloat(matMemory[2])));
                            matMemory = currentLine[2].split(",");
                            diffuse.add(new Vector3f(Float.parseFloat(matMemory[0]), Float.parseFloat(matMemory[1]), Float.parseFloat(matMemory[2])));
                            matMemory = currentLine[3].split(",");
                            specular.add(new Vector3f(Float.parseFloat(matMemory[0]), Float.parseFloat(matMemory[1]), Float.parseFloat(matMemory[2])));
                            shininess.add(Float.parseFloat(currentLine[4]));
                            textured.add(Boolean.parseBoolean(currentLine[5]));
                        } else if (line.startsWith("end ")){
                            RawModel rawModel = OBJLoader.loadOBJModel(entModel, loader);
                            ModelTexture modelTexture = new ModelTexture(loader.loadTexture(entTexture));
                            TexturedModel texturedModel = new TexturedModel(rawModel, modelTexture);
                            entities.add(new Entity(entName, texturedModel, position, rotation.x, rotation.y, rotation.z, scale));
                            System.out.println(ambient);
                            System.out.println(diffuse);
                            System.out.println(specular);
                            System.out.println(shininess);
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
        for (int i = 0; i < entities.size(); i++) {
            //apply material
            shader.setUniformVector("material.ambient", ambient.get(i));
            shader.setUniformVector("material.diffuse", diffuse.get(i));
            shader.setUniformVector("material.specular", specular.get(i));
            shader.setUniformFloat("material.shininess", shininess.get(i));
            shader.setUniformFloat("material.shininess", shininess.get(i));
            shader.setUniformBool("material.textured", textured.get(i));

            //render
            renderer.render(entities.get(i), shader);
        }
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
