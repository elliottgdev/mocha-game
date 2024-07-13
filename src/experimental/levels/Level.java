package experimental.levels;

import org.lwjgl.util.vector.Vector2f;

import static toolbox.Maths.pointTriangleIntersection;

public class Level {
    static Sector[] sectors = {
            new Sector(new Vector2f(0, 3), new Vector2f(0, 0), new Vector2f(12, 3), new Vector2f(12, 0), 0.3f),
            new Sector(new Vector2f(0, 6), new Vector2f(0, 0), new Vector2f(12, 6), new Vector2f(12, 0), 0.6f)
    };

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

/*
 diagram
 *v0 - *v2
 |  \  |
 *v1 - *v3
*/

class Sector {
    public final Vector2f v0;
    public final Vector2f v1;
    public final Vector2f v2;
    public final Vector2f v3;

    public float floorHeight;

    public Sector(Vector2f v0, Vector2f v1, Vector2f v2, Vector2f v3, float floorHeight){
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.floorHeight = floorHeight;
    }
}