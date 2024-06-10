#version 400 core

in vec2 pass_textureCoords;
in vec3 normal;
out vec3 aNormal;
in vec3 fragPos;
in vec3 colour;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightPos;
uniform vec3 viewPos;

void main(void){
	float ambientStrength = 0.3;
    vec3 ambient = ambientStrength * colour;
    float specularStrength = 0.5;

    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(vec3(0, 0, 2) - fragPos);

    vec3 lightColour = vec3(1, 1, 1);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColour;

    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 512);
    vec3 specular = specularStrength * spec * lightColour;

    vec3 result = (ambient + diffuse + specular) * vec3(1, 1, 1);
    out_Color = vec4(result, 1.0) * texture(textureSampler, pass_textureCoords);
}