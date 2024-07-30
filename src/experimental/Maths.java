package experimental;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static toolbox.Maths.*;

public class Maths {
    public static boolean lineCircleCollision(Vector2f line1, Vector2f line2, Vector2f position, float radius){
        if (pointCircleCollision(line1, position, radius) || pointCircleCollision(line2, position, radius)) return true;

        float distX = line1.x - line2.x;
        float distY = line1.y - line2.y;
        float lineLength = (float) sqrt((distX*distX) + (distY*distY));
        float dot = (float) ((((position.x - line1.x) * (line2.x - line1.x)) + ((position.y - line1.y) * (line2.y - line1.y))) / pow(lineLength, 2));

        float closestX = line1.x + (dot * (line2.x - line1.x));
        float closestY = line1.y + (dot * (line2.y - line1.y));

        if (!linePointCollision(new Vector2f(closestX, closestY), line1, line2)) return false;

        distX = closestX - position.x;
        distY = closestY - position.y;
        float distance = (float) sqrt((distX*distX) + (distY*distY));
        return distance <= radius;
    }

    public static boolean linePointCollision(Vector2f point, Vector2f lineA, Vector2f lineB){
        float dist1 = distance(point, lineA);
        float dist2 = distance(point, lineB);
        float lineLength = distance(lineA, lineB);
        float buffer = 0.1f;
        return dist1 + dist2 >= lineLength-buffer && dist1 + dist2 <= lineLength + buffer;
    }

    public static float distance(Vector2f origin, Vector2f point){
        float distX = origin.x - point.x;
        float distY = origin.y - point.y;
        return (float) sqrt((distX * distX) + (distY * distY));
    }
}
