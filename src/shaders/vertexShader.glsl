#version 400 core

in vec3 position;
in vec2 textureCoords;
layout (location = 0) in vec3 aNormal;

out vec2 pass_textureCoords;
out vec3 normal;
out vec3 fragPos;
out vec3 colour;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
	fragPos = vec3(transformationMatrix * vec4(position, 1.0));
	colour = vec3(1, 1, 1);
	normal = mat3(transpose(inverse(transformationMatrix))) * aNormal;
	pass_textureCoords = textureCoords;
}