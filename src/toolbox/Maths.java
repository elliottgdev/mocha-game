package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import org.lwjgl.util.vector.Vector4f;
import sun.awt.X11.XSystemTrayPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Maths {
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}

	//this is all sebastion lague https://www.youtube.com/watch?v=HYAgJN3x4GA watch to understand :)
	public static boolean pointTriangleIntersection(Vector2f a, Vector2f b, Vector2f c, Vector2f p){
		Vector4f s = new Vector4f();
		s.x = c.y - a.y;
		s.y = c.x - a.x;
		s.z = b.y - a.y;
		s.w = p.y - a.y;

		Vector2f w = new Vector2f();
		w.x = (a.x * s.x + s.w * s.y - p.x * s.x) / (s.z * s.y - (b.x-a.x) * s.x);
		w.y = (s.w- w.x * s.z) / s.x;

		if (w.x >= 0 && w.y >= 0 && (w.x + w.y) <= 1)
			return true;
		else
			return false;
	}
}
