#version 330 core

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
    bool textured;
};

struct Light {
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

in vec2 pass_textureCoords;
in vec3 vertexNormal;
in vec3 fragPosition;
in vec3 colour;

out vec4 out_Color;
out vec3 aNormal;

uniform sampler2D textureSampler;
uniform vec3 viewPos;
uniform Material material;
uniform Light light;

void main(void){
    //ambient
    vec3 ambient = light.ambient * material.ambient;

    //diffuse
    vec3 normalizedNormal = normalize(vertexNormal);
    vec3 lightDir = normalize(-light.direction);
    float diffuseCalculations = max(dot(normalizedNormal, lightDir), 0.0);
    vec3 diffuse = light.diffuse * (diffuseCalculations * material.diffuse);

    //specular
    vec3 viewDir = normalize(viewPos - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normalizedNormal);
    float specularCalculations = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * (specularCalculations * material.specular);

    //output
    vec3 result = ambient + diffuse + specular;

    if (material.textured)
        out_Color = vec4(result, 1.0) * texture(textureSampler, pass_textureCoords);
    else
        out_Color = vec4(result, 1.0);
}