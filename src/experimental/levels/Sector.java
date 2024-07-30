package experimental.levels;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Sector {
    public final Vector2f v0;
    public final Vector2f v1;
    public final Vector2f v2;
    public final Vector2f v3;

    public float floorHeight;
    public float ceilingHeight;

    public final String texture;
    public final boolean stretchTexture;

    public SectorType sectorType;

    public String fWall, bWall, lWall, rWall;
    public final int walls;

    public final int[][] index = {
            {0, 1, 2},
            {2, 1, 3},
    };

    public final Vector3f[] normals;

    public Sector(Vector2f v0, Vector2f v1, Vector2f v2, Vector2f v3, float floorHeight, float ceilingHeight, String texture, boolean stretchTexture, SectorType sectorType, String fWall, String bWall, String lWall, String rWall, int walls) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.floorHeight = floorHeight;
        this.ceilingHeight = ceilingHeight;
        this.sectorType = sectorType;
        this.texture = texture;
        this.stretchTexture = stretchTexture;
        this.fWall = fWall;
        this.bWall = bWall;
        this.lWall = lWall;
        this.rWall = rWall;
        this.walls = walls;

        normals = new Vector3f[4];
        for (int i = 0; i < normals.length; i++) {
            normals[i] = new Vector3f(0, 0, 0);
        }

        Vector3f[] edge = {
                new Vector3f(v1.x - v2.x, 0, v1.y - v2.y),
                new Vector3f(v1.x - v0.x, 0, v1.y - v0.y),
                new Vector3f(v3.x - v2.x, 0, v3.y - v2.y),
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

        for (Vector3f normal : normals) {
            normal.normalise(normal);
        }
    }
}
