#version 330 core

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
    bool textured;
};

struct DirectionalLight {
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct PointLight {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
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

uniform DirectionalLight directionalLight;
#define NR_POINT_LIGHTS 1
uniform PointLight pointLight[NR_POINT_LIGHTS];

vec3 CalculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDir){
    vec3 lightDir = normalize(-light.direction);

    float diff = max(dot(normal, lightDir), 0);

    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0), material.shininess);

    vec3 ambient = light.ambient * material.diffuse;
    vec3 diffuse = light.diffuse * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    return (ambient + diffuse + specular);
}

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPosition, vec3 viewDirection){
    vec3 lightDir = normalize(light.position - fragPosition);

    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDirection = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), material.shininess);

    float distance    = length(light.position - fragPosition);
    float attenuation = 1.0 / (light.constant + light.linear * distance +
    light.quadratic * (distance * distance));

    vec3 ambient  = light.ambient  * material.diffuse;
    vec3 diffuse  = light.diffuse  * diff * material.diffuse;
    vec3 specular = light.specular * spec * material.specular;
    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

void main(void){
    //input
    vec3 normalizedNormal = normalize(vertexNormal);
    vec3 viewDir = normalize(viewPos - fragPosition);

    //output
    vec3 result = CalculateDirectionalLight(directionalLight, normalizedNormal, viewDir);

    for (int i = 0; i < NR_POINT_LIGHTS; i++) {
        result += CalculatePointLight(pointLight[i], normalizedNormal, fragPosition, viewDir);
    }


    if (material.textured)
        out_Color = vec4(result, 1.0) * texture(textureSampler, pass_textureCoords);
    else
        out_Color = vec4(result, 1.0);
}