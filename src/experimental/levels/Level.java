package experimental.levels;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;

import static toolbox.Maths.pointTriangleIntersection;

public class Level {
    static Sector[] sectors = {
            new Sector(new Vector2f(0, 3), new Vector2f(0, 0), new Vector2f(12, 3), new Vector2f(12, 0), 0.5f, 7f, "test", false ,SectorType.sector_static, "test", "test", "test", "test", 2),
            new Sector(new Vector2f(0, 0), new Vector2f(0, -3), new Vector2f(12, 0), new Vector2f(12, -3), 1.5f, 7f, "grass", true, SectorType.sector_static, "grass", "grass", "grass", "grass", 2),
            new Sector(new Vector2f(-4, 0), new Vector2f(-4, -3), new Vector2f(0, 0), new Vector2f(0, -3), 4f, 7f, "test", true, SectorType.sector_static, "white", "test", "null", "monkey", 2),
            new Sector(new Vector2f(-4, 3), new Vector2f(-4, 0), new Vector2f(0, 3), new Vector2f(0, 0), 1f, 8f, "white", true, SectorType.sector_static, "null", "null", "null", "null", 0),
    };
    private static List<float[]> positions = new ArrayList<>();
    private static List<float[]> uvs = new ArrayList<>();
    private static List<float[]> normals = new ArrayList<>();
    private static List<int[]> index = new ArrayList<>();
    private static List<RawModel> rawModel = new ArrayList<>();
    private static List<ModelTexture> texture = new ArrayList<>();


    public static void generateSectorModels(Loader loader){
        positions = new ArrayList<>();
        uvs = new ArrayList<>();
        normals = new ArrayList<>();
        index = new ArrayList<>();
        for (int i = 0; i < sectors.length; i++) {
            //floor face
            if (sectors[i].texture != "null") {
                positions.add(new float[]{
                        sectors[i].v0.x, sectors[i].floorHeight, sectors[i].v0.y,
                        sectors[i].v1.x, sectors[i].floorHeight, sectors[i].v1.y,
                        sectors[i].v2.x, sectors[i].floorHeight, sectors[i].v2.y,
                        sectors[i].v3.x, sectors[i].floorHeight, sectors[i].v3.y,
                        sectors[i].v0.x, sectors[i].ceilingHeight, sectors[i].v0.y,
                        sectors[i].v1.x, sectors[i].ceilingHeight, sectors[i].v1.y,
                        sectors[i].v2.x, sectors[i].ceilingHeight, sectors[i].v2.y,
                        sectors[i].v3.x, sectors[i].ceilingHeight, sectors[i].v3.y,
                });
                if (!sectors[i].stretchTexture) {
                    uvs.add(new float[]{
                            sectors[i].v0.x, sectors[i].v0.y,
                            sectors[i].v1.x, sectors[i].v1.y,
                            sectors[i].v2.x, sectors[i].v2.y,
                            sectors[i].v3.x, sectors[i].v3.y,
                            sectors[i].v0.x, sectors[i].v0.y,
                            sectors[i].v1.x, sectors[i].v1.y,
                            sectors[i].v2.x, sectors[i].v2.y,
                            sectors[i].v3.x, sectors[i].v3.y,
                    });
                } else {
                    uvs.add(new float[]{
                            0, 1,
                            0, 0,
                            1, 1,
                            1, 0,
                            0, 1,
                            0, 0,
                            1, 1,
                            1, 0,
                    });
                }
                normals.add(new float[]{
                        sectors[i].floorNormals1[0].x, sectors[i].floorNormals1[0].y, sectors[i].floorNormals1[0].z,
                        sectors[i].floorNormals1[1].x, sectors[i].floorNormals1[1].y, sectors[i].floorNormals1[1].z,
                        sectors[i].floorNormals1[2].x, sectors[i].floorNormals1[2].y, sectors[i].floorNormals1[2].z,
                        sectors[i].floorNormals2[2].x, sectors[i].floorNormals2[2].y, sectors[i].floorNormals2[2].z,
                        sectors[i].floorNormals1[0].x, sectors[i].floorNormals1[0].y, sectors[i].floorNormals1[0].z,
                        sectors[i].floorNormals1[1].x, sectors[i].floorNormals1[1].y, sectors[i].floorNormals1[1].z,
                        sectors[i].floorNormals1[2].x, sectors[i].floorNormals1[2].y, sectors[i].floorNormals1[2].z,
                        sectors[i].floorNormals2[2].x, sectors[i].floorNormals2[2].y, sectors[i].floorNormals2[2].z,

                });
                index.add(new int[]{
                        0, 2, 1,
                        1, 2, 3,
                        5, 6, 4,
                        7, 6, 5
                });

            }
            try {
                rawModel.add(loader.loadToVAO(positions.get(i), uvs.get(i), normals.get(i), index.get(i)));
            } catch (Exception ignored) {

            }
            try {
                texture.add(new ModelTexture(loader.loadTexture(sectors[i].texture)));
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Texture location does not exist. Please check code and verify texture exists.");
                texture.add(new ModelTexture(loader.loadTexture("null")));
            }
        }
    }

    public static void renderSectorModel(Renderer renderer, StaticShader shader){
        for (int i = 0; i < sectors.length; i++) {
            try {
                shader.setMaterial(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), 1, true);
                renderer.render(new TexturedModel(rawModel.get(i), texture.get(i)),new Vector3f(0, 0, 0), 1, shader);
            } catch (Exception e){

            }
        }
    }

    public static Vector2f getFloorHeight(Vector2f position){
        float height = 0;
        float ceilingHeight = 3;
        for (Sector sector : sectors){
            if (pointTriangleIntersection(sector.v1, sector.v0, sector.v2, position) || pointTriangleIntersection(sector.v2, sector.v1, sector.v3, position)) {
                height = sector.floorHeight;
                ceilingHeight = sector.ceilingHeight;
                break;
            }
        }
        return new Vector2f(height, ceilingHeight);
    }

    public static Sector[] getSectors() {
        return sectors;
    }
}

