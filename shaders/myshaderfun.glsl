
out vec4 outputColor;

uniform vec4 input_color;

uniform mat4 view_matrix;

// Light properties
uniform vec3 lightPos;
uniform vec3 torchlightPos;
uniform vec3 sunlightIntensity;
uniform vec3 torchlightIntensity;
uniform vec3 ambientIntensity;
uniform float spotCutoff;
uniform int torchOn;
uniform float spotExp;

// Material properties
uniform vec3 ambientCoeff;
uniform vec3 diffuseCoeff;
uniform vec3 specularCoeff;
uniform float phongExp;

uniform sampler2D tex;

in vec4 viewPosition;
in vec3 m;

in vec2 texCoordFrag;

void main()
{
    // Compute the s, v and r vectors
    vec3 sunS = normalize(view_matrix*vec4(lightPos,0)).xyz;
    vec3 torchS = normalize(view_matrix*vec4(torchlightPos,1) - viewPosition).xyz;

    vec3 v = normalize(-viewPosition.xyz);

    vec3 sunR = normalize(reflect(-sunS,m));
    vec3 torchR = normalize(reflect(-torchS,m));



    vec3 ambient = ambientIntensity*ambientCoeff;
    vec3 sunDiffuse = max(sunlightIntensity*diffuseCoeff*dot(m,sunS), 0.0);
    vec3 sunSpecular;

    // Only show specular reflections for the front face
    if (dot(m,sunS) > 0)
        sunSpecular = max(sunlightIntensity*specularCoeff*pow(dot(sunR,v),phongExp), 0.0);
    else
        sunSpecular = vec3(0);


    //checking if fragment is in the spotlight; if so, add the torchlight
    float spotD = dot(-torchS, vec3(0, 0, -1));

    spotD = max(spotD,0);
    vec3 diffuse;
    vec3 specular;
    if (acos(spotD) < radians(spotCutoff) && torchOn == 1){
        vec3 torchDiffuse = max(torchlightIntensity*diffuseCoeff*dot(m,torchS), 0.0);
        vec3 torchSpecular;
        float spotAtt = pow(cos(spotD), spotExp); //spotlight attenuation
        if (dot(m,torchS) > 0)
            torchSpecular = max(spotAtt*torchlightIntensity*specularCoeff*pow(dot(torchR,v),phongExp), 0.0);
        else
            torchSpecular = vec3(0);
        specular = sunSpecular + torchSpecular;
        diffuse = sunDiffuse + torchDiffuse;
        ambient = ambient*2;
    }
    else{
        specular = sunSpecular;
        diffuse = sunDiffuse;
    }

    vec4 ambientAndDiffuse = vec4(ambient + diffuse, 1);

    outputColor = ambientAndDiffuse*input_color*texture(tex, texCoordFrag) + vec4(specular, 1);
}
