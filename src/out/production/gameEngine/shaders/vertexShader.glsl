#version 400 core

in vec3 vertexPosition;
in vec2 textureCoords;
layout (location = 0) in vec3 normal;

out vec2 pass_textureCoords;
out vec3 vertexNormal;
out vec3 fragPosition;
out vec3 colour;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 inverseTransformationMatrix;

void main(void){
	//customizable stuff
	colour = vec3(1, 1, 1);

	//positional calculations
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertexPosition, 1.0);
	fragPosition = vec3(transformationMatrix * vec4(vertexPosition, 1.0));

	//normal calculation
	vertexNormal = mat3(transpose(inverseTransformationMatrix) + transformationMatrix) * normal;

	//uvs
	pass_textureCoords = textureCoords;
}