#version 400 core

in vec2 pass_textureCoords;
in vec3 vertexNormal;
in vec3 fragPosition;
in vec3 colour;

out vec4 out_Color;
out vec3 aNormal;
out vec3 lightColour;

uniform sampler2D textureSampler;
uniform vec3 lightPosition;
uniform vec3 viewPos;

void main(void){
    //the customizable stuff
    lightColour = vec3(1, 1, 1);
    float ambientStrength = 0.3;
    float specularStrength = 0.5;
    float shininess = 4;

    //everything becomes normal
    vec3 normalizedNormal = normalize(vertexNormal);
    vec3 lightDir = normalize(lightPosition - fragPosition);
    vec3 viewDir = normalize(viewPos - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normalizedNormal);

    //material calculations
    vec3 ambient = ambientStrength * colour;
    vec3 diffuse = max(dot(normalizedNormal, lightDir), 0.0) * lightColour;
    vec3 specular = specularStrength * pow(max(dot(viewDir, reflectDir), 0.0), shininess) * lightColour;

    //output
    vec3 result = (ambient + diffuse + specular) * vec3(1, 1, 1);
    out_Color = vec4(result, 1.0) * texture(textureSampler, pass_textureCoords);
}