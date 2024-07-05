package entities;

import components.Component;
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

                String line;
                BufferedReader reader = new BufferedReader(fr);

                //entity base component
                String entName = "PLACEHOLDER";
                String entModel = "null";
                String entTexture = "null";

                //transform component
                Vector3f position = new Vector3f(0, 0 ,0);
                Vector3f rotation = new Vector3f(0, 0, 0);
                float scale = 1;

                //other components
                List<Component> componentsToAdd = new ArrayList<>();

                try {
                    while (true){
                        line = reader.readLine();
                        String[] currentLine = line.split(" ");

                        if (line.startsWith("! ")){
                            entName = currentLine[1];
                            entModel = currentLine[2];
                            entTexture = currentLine[3];
                        } else if (line.startsWith("@ ")) {
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
                        } else if (line.startsWith("$ ")) {
                            componentsToAdd.add(getComponent(currentLine[1]));
                        } else if (line.startsWith("end")){
                            RawModel rawModel = OBJLoader.loadOBJModel(entModel, loader);
                            ModelTexture modelTexture = new ModelTexture(loader.loadTexture(entTexture));
                            TexturedModel texturedModel = new TexturedModel(rawModel, modelTexture);
                            Entity entity = new Entity(entName, texturedModel, position, rotation.x, rotation.y, rotation.z, scale);
                            for (Component component : componentsToAdd){
                                entity.addComponent(component);
                                entity.getComponent(component).awake(entity);
                            }
                            entities.add(entity);
                            break;
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(){
        for (Entity entity : entities){
            entity.update();
        }
    }

    private Component getComponent(String component) {
        System.out.println(component);
        try {
            Class<?> _class = Class.forName("components." + component);
            return (Component) _class.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void renderEntities(){
        for (int i = 0; i < entities.size(); i++) {
            //apply material
            shader.setMaterial(ambient.get(i), diffuse.get(i), specular.get(i), shininess.get(i), textured.get(i));

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
