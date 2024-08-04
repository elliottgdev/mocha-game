package experimental.levels;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static experimental.Maths.calculateTriangleNormals;

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

    public final Vector3f[] floorNormals1;
    public final Vector3f[] floorNormals2;

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

        floorNormals1 = calculateTriangleNormals(new Vector3f[]{new Vector3f(v0.x, floorHeight, v0.y), new Vector3f(v1.x, floorHeight, v1.y), new Vector3f(v2.x, floorHeight, v2.y)}, new int[]{0, 2, 1});
        floorNormals2 = calculateTriangleNormals(new Vector3f[]{new Vector3f(v1.x, floorHeight, v1.y), new Vector3f(v2.x, floorHeight, v2.y), new Vector3f(v3.x, floorHeight, v3.y)}, new int[]{0, 1, 2});
    }
}
