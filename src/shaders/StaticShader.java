package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import static org.lwjgl.opengl.GL20.glGetUniform;

public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public StaticShader(String vertex, String fragment) {
		super("shaders/"+vertex+".glsl", "shaders/"+fragment+".glsl");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	public void setUniformVector(String uniform, Vector3f vector) {
		super.loadVector(super.getUniformLocation(uniform), vector);
	}

	public void setUniformMatrix(String uniform, Matrix4f matrix4f) {
		super.loadMatrix(super.getUniformLocation(uniform), matrix4f);
	}

	public void setUniformFloat(String uniform, float uFloat) {
		super.loadFloat(super.getUniformLocation(uniform), uFloat);
	}

	public void setUniformBool(String uniform, Boolean bool) {
		super.loadBoolean(super.getUniformLocation(uniform), bool);
	}

	public void setMaterial(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess, Boolean textured){
		setUniformVector("material.ambient", ambient);
		setUniformVector("material.diffuse", diffuse);
		setUniformVector("material.specular", specular);
		setUniformFloat("material.shininess", shininess);
		setUniformBool("material.textured", textured);
	}

	public void setDirectionalLight(Vector3f direction, Vector3f ambient, Vector3f diffuse, Vector3f specular){
		setUniformVector("directionalLight.direction", direction);
		setUniformVector("directionalLight.ambient", ambient);
		setUniformVector("directionalLight.diffuse", diffuse);
		setUniformVector("directionalLight.specular", specular);
	}

	public void setPointLight(int index, Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, float constant, float linear, float quadratic){
		setUniformVector("pointLight["+index+"].position", position);
		setUniformVector("pointLight["+index+"].ambient", ambient);
		setUniformVector("pointLight["+index+"].diffuse", diffuse);
		setUniformVector("pointLight["+index+"].specular", specular);
		setUniformFloat("pointLight["+index+"].constant", constant);
		setUniformFloat("pointLight["+index+"].linear", linear);
		setUniformFloat("pointLight["+index+"].quadratic", quadratic);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
