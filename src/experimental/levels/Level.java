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
            new Sector(new Vector2f(0, 3), new Vector2f(0, 0), new Vector2f(12, 3), new Vector2f(12, 0), 1f),
            new Sector(new Vector2f(0, 0), new Vector2f(0, -3), new Vector2f(12, 0), new Vector2f(12, -3), 2f)
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
            positions.add(new float[]{
                    sectors[i].v0.x, sectors[i].floorHeight, sectors[i].v0.y,
                    sectors[i].v1.x, sectors[i].floorHeight, sectors[i].v1.y,
                    sectors[i].v2.x, sectors[i].floorHeight, sectors[i].v2.y,
                    sectors[i].v3.x, sectors[i].floorHeight, sectors[i].v3.y,
            });
            uvs.add(new float[]{
                    sectors[i].v0.x, sectors[i].v0.y,
                    sectors[i].v1.x, sectors[i].v1.y,
                    sectors[i].v2.x, sectors[i].v2.y,
                    sectors[i].v3.x, sectors[i].v3.y,
            });
            normals.add(new float[]{
                    sectors[i].normals[0].x, sectors[i].normals[0].y, sectors[i].normals[0].z,
                    sectors[i].normals[1].x, sectors[i].normals[1].y, sectors[i].normals[1].z,
                    sectors[i].normals[2].x, sectors[i].normals[2].y, sectors[i].normals[2].z,
                    sectors[i].normals[3].x, sectors[i].normals[3].y, sectors[i].normals[3].z,
            });
            index.add(new int[]{
                    0, 2, 1,
                    1, 2, 3
            });
            rawModel.add(loader.loadToVAO(positions.get(i), uvs.get(i), normals.get(i), index.get(i)));
            texture.add(new ModelTexture(loader.loadTexture("null")));
        }
    }

    public static void renderSectorModel(Renderer renderer, StaticShader shader){
        for (int i = 0; i < sectors.length; i++) {
            shader.setMaterial(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), new Vector3f(1, 1, 1), 1, true);
            renderer.render(new TexturedModel(rawModel.get(i), texture.get(i)),new Vector3f(0, 0, 0), 1, shader);
        }
    }

    public static float getFloorHeight(Vector2f position){
        float height = 0;
        for (Sector sector : sectors){
            if (pointTriangleIntersection(sector.v1, sector.v0, sector.v2, position) || pointTriangleIntersection(sector.v2, sector.v1, sector.v3, position)) {
                height = sector.floorHeight;
                break;
            }
        }
        return height;
    }
}

class Sector {
    public final Vector2f v0;
    public final Vector2f v1;
    public final Vector2f v2;
    public final Vector2f v3;

    public float floorHeight;

    public final int[][] index = {
        {0, 1, 2},
        {2, 1, 3},
    };

    public final Vector3f[] normals;

    public Sector(Vector2f v0, Vector2f v1, Vector2f v2, Vector2f v3, float floorHeight){
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.floorHeight = floorHeight;

        normals = new Vector3f[4];
        for (int i = 0; i < normals.length; i++) {
            normals[i] = new Vector3f(0, 0, 0);
        }

        Vector3f[] edge = {
            new Vector3f(v1.x - v2.x, v1.y - v2.y, 0),
            new Vector3f(v1.x - v0.x, v1.y - v0.y, 0),
            new Vector3f(v3.x - v2.x, v3.y - v2.y, 0),
        };

        Vector3f triangle1norm = new Vector3f(
                edge[0].y * edge[1].z - edge[0].z * edge[1].y,
                edge[0].z * edge[1].x - edge[0].x * edge[1].z,
                edge[0].x * edge[1].y - edge[0].y * edge[1].x
        );
        Vector3f triangle2norm = new Vector3f(
                edge[0].y * edge[2].z - edge[0].z * edge[2].y,
                edge[0].z * edge[2].x - edge[0].x * edge[2].z,
                edge[0].x * edge[2].y - edge[0].y * edge[2].x
        );
        normals[index[0][0]] = triangle1norm;
        normals[index[0][1]] = triangle1norm;
        normals[index[0][2]] = triangle1norm;
        normals[index[1][2]] = triangle2norm;

        for (Vector3f normal : normals){
            normal.normalise(normal);
        }
    }
}