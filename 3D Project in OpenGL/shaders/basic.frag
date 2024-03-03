#version 410 core

in vec3 fPosition;
in vec3 fNormal;
in vec2 fTexCoords;

out vec4 fColor;

//matrices
uniform mat4 model;
uniform mat4 view;

//lighting
uniform vec3 lightDir;
uniform vec3 lightColor;
uniform mat3 normalMatrix;

//spot light
uniform vec3 spotLightPos;
uniform vec3 spotLightDir;
uniform float cutOff;
uniform float outerCutOff;

// textures
uniform sampler2D diffuseTexture;
uniform sampler2D specularTexture;

//control 

uniform bool showShadow;
uniform bool showFog;
uniform bool showSnow;
uniform bool showRain;
uniform bool showSpotLight;
uniform bool showNight;

//components
vec3 ambient;
float ambientStrength = 0.2f;
vec3 diffuse;
vec3 specular;
float specularStrength = 0.5f;

//shadows
uniform sampler2D shadowMap;
in vec4 fragPosLightSpace;

vec4 fPosEye;
vec3 color;

//positonal light characteristics
vec3 pointLightPos = vec3(-2.5f, 0.3f, 4.1f);
vec3 yellowishColor = vec3(0.5,0.3,0.0);
vec3 whiteColor = vec3(1,1,1);


//timp pentru a face animatia de ploaie si snow(desi se vede foarte putin fiind noise)
uniform float time; // Global time



void computeDirLight()
{
	
    //compute eye space coordinates
    fPosEye = view * model * vec4(fPosition, 1.0f);
    vec3 normalEye = normalize(normalMatrix * -fNormal);

    //normalize light direction
    vec3 lightDirN = vec3(normalize(view * vec4(lightDir, 0.0f)));

    //compute view direction (in eye coordinates, the viewer is situated at the origin
    vec3 viewDir = normalize(- fPosEye.xyz);

    //compute ambient light
    ambient = ambientStrength * lightColor;

    //compute diffuse light
		diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;
	

    //compute specular light
    vec3 reflectDir = reflect(-lightDirN, normalEye);
    float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    specular = specularStrength * specCoeff * lightColor;
	
}
vec3 computeNightMode() {
    vec3 fPositionWorld = vec3(model * vec4(fPosition, 1.0));
    vec3 pointLightPosToFragDir = normalize(pointLightPos - fPositionWorld);
    
    vec4 fPosEye = view * model * vec4(fPosition, 1.0f);
    vec3 normalEye = normalize(normalMatrix * -fNormal);
    vec3 lightDirN = vec3(normalize(view * vec4(pointLightPosToFragDir, 0.0f)));
    vec3 viewDir = normalize(- fPosEye.xyz);

    // Ambient
    vec3 ambient = ambientStrength * whiteColor;

    // Diffuse shading
    vec3 diffuse = max(dot(normalEye, lightDirN), 0.0f) * whiteColor;

    // Specular shading
    vec3 reflectDir = reflect(-lightDirN, normalEye);
    float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    vec3 specular = specularStrength * specCoeff * whiteColor;

    // Attenuation
    float distance = length(pointLightPos - fPositionWorld);
    float attenuation = 1.0 / (1.0f + 0.9 * distance + 0.32 * (distance * distance));

    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;

    // Combine the components and make it darker
    float darknessFactor = 0.09;  // Adjust this factor for the desired darkness
    vec3 color = min((ambient + diffuse + specular) * texture(diffuseTexture, fTexCoords).rgb, 1.0f) * darknessFactor;

    return color;
}


vec3 computePostionalLight() {
	
    vec3 fPositionWorld = vec3(model * vec4(fPosition, 1.0));
	vec3 pointLightPosToFragDir = normalize(pointLightPos - fPositionWorld); // dir from spot light pos to fragment
	
	vec4 fPosEye = view * model * vec4(fPosition, 1.0f);
	vec3 normalEye = normalize(normalMatrix * -fNormal);
	vec3 lightDirN = vec3(normalize(view * vec4(pointLightPosToFragDir, 0.0f)));
	vec3 viewDir = normalize(- fPosEye.xyz); 

	
    //ambient
	vec3 ambient = ambientStrength * yellowishColor;
    // diffuse shading
	vec3 diffuse = max(dot(normalEye, lightDirN), 0.0f) * yellowishColor;
    // specular shading
    vec3 reflectDir = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    vec3 specular = specularStrength * specCoeff * yellowishColor;

	
    // attenuation
    float distance = length(pointLightPos - fPositionWorld);
    float attenuation = 1.0 / (1.0f + 0.9 * distance + 0.32 * (distance * distance));   
	
	ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;
	
	vec3 color = min((ambient + diffuse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    return color;
}

vec3 computeSpotLight() {
   
	vec3 fPositionWorld = vec3(model * vec4(fPosition, 1.0));
	vec3 spotLightPosToFragDir = normalize(spotLightPos - fPositionWorld); // dir from spot light pos to fragment
	
	vec4 fPosEye = view * model * vec4(fPosition, 1.0f);
	vec3 normalEye = normalize(normalMatrix * -fNormal);
	vec3 lightDirN = vec3(normalize(view * vec4(spotLightDir, 0.0f)));
	vec3 viewDir = normalize(- fPosEye.xyz); 

	
    //ambient
	vec3 ambient = ambientStrength * whiteColor;
    // diffuse shading
	vec3 diffuse = max(dot(normalEye, lightDirN), 0.0f) * whiteColor;
    // specular shading
    vec3 reflectDir = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    vec3 specular = specularStrength * specCoeff * whiteColor;

	
    // attenuation
    float distance = length(spotLightPos - fPositionWorld);
    float attenuation = 1.0 / (1.0f + 0.09 * distance + 0.032 * (distance * distance));   
	
    // spotlight intensity
    float theta = dot(normalize(-spotLightDir), spotLightPosToFragDir); //with minus??
    float epsilon = cutOff - outerCutOff;
    float intensity = clamp((theta - outerCutOff) / epsilon, 0.0, 1.0);
	
	ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
	
	vec3 color = min((ambient + diffuse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    return color;
}

//make the snow effect using noise

float computeSnow()
{
    float snow = 0.0f;
    float snowFlakeSize = 10000000.0f;
    float snowFlakeSpeed = 1.5f; // Initial speed of the snowflake
    float snowFlakeAcceleration = 0.05f; // Acceleration due to gravity for snow
    float snowFlakeDensity = 0.05f;
    float snowFlakeLength = 0.5f;
    float initialHeight = 20.0f; // Starting height of the snowflake

    float x = fTexCoords.x;
    float y = fTexCoords.y;

    // Add some noise to the snowflake
    float noise = fract(sin(dot(fTexCoords.xy, vec2(1200.9898f, 780.233f))) * 43758.5453f);

    // Compute the snowflake density
    float density = snowFlakeDensity * noise;

    // Compute the snowflake position with acceleration
    float timeSquared = time * time;
    float snowFlakePosY = initialHeight - (snowFlakeSpeed * time + 1.5f * snowFlakeAcceleration * timeSquared);

    // Ensure snowFlakePosY does not go below the ground level
    snowFlakePosY = max(snowFlakePosY, 0.0f);

    // Compute the snowflake distance to the camera
    float distance = abs(snowFlakePosY - fPosition.y);

    // Compute the snowflake length
    float length = snowFlakeLength * (1.0f + noise);

    // Compute the snowflake size
    float size = snowFlakeSize * (100000000.0f + 2000000.0f * noise);

    // Compute the snowflake factor
    float factor = clamp((length - distance) / length, 0.0f, 1.0f);

    // Compute the snowflake color with a stronger white
    float snowFlakeColor = 1.0f + 0.05f * noise; // Adjust color to make it stronger white

    // Compute the snow factor
    snow = density * factor * snowFlakeColor;

    return snow;
}



float computeShadow()
{	
    vec3 normalizedCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    if(normalizedCoords.z > 1.0f)
        return 0.0f;

    // Transform to [0,1] range
    normalizedCoords = normalizedCoords * 0.5f + 0.5f;

    // Get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMap, normalizedCoords.xy).r;    

    // Get depth of current fragment from light's perspective
    float currentDepth = normalizedCoords.z;

    // Check whether current frag pos is in shadow
    float bias = 0.005f;
    
    float shadow = currentDepth - bias > closestDepth  ? 1.0f : 0.0f;

    return shadow;	
}

//make the rain effect using noise
float computeRain()
{
    float rain = 0.0f;
    float rainDropSize = 20000000.0f;  // Increased rainDropSize
    float rainDropSpeed = 10.5f;       // Initial speed of the raindrop
    float rainDropAcceleration = 0.98f; // Acceleration due to gravity
    float rainDropDensity = 0.02f;      // Further reduced density
    float rainDropLength = 0.5f;
    float initialHeight = 20.0f;        // Starting height of the raindrop

    int numParticles = 8;               // Reduced number of particles

    for (int i = 0; i < numParticles; ++i)
    {
        float x = fTexCoords.x + (float(i) / float(numParticles)) * 2.0 - 1.0; // Spread particles along the x-axis
        float y = fTexCoords.y;

        // Add some noise to the rain
        float noise = fract(sin(dot(vec2(x, y), vec2(1200.9898f, 780.233f))) * 43758.5453f);

        // Compute the rain density
        float density = rainDropDensity * noise;

        // Compute the rain position with acceleration
        float timeSquared = time * time;
        float rainPosY = initialHeight - (rainDropSpeed * time + 1.5f * rainDropAcceleration * timeSquared);

        // Ensure rainPosY does not go below the ground level
        rainPosY = max(rainPosY, 0.0f);

        // Compute the rain drop distance to the camera
        float distance = abs(rainPosY - fPosition.y);

        // Compute the rain drop length
        float length = rainDropLength * (1.0f + noise);

        // Compute the rain drop size (increase the multiplier for larger particles)
        float size = rainDropSize * (100000000.0f + 20000000.0f * noise);

        // Compute the rain drop factor
        float factor = clamp((length - distance) / length, 0.0f, 1.0f);

        // Compute the rain drop color
        float rainDropColor = 0.5f + 0.5f * noise;

        // Compute the rain factor
        rain += density * factor * rainDropColor;
    }

    return rain;
}







float computeFog()
{
 float fogDensity = 0.1f;
 float fragmentDistance = length(fPosEye);
 float fogFactor = exp(-pow(fragmentDistance * fogDensity, 2));

 return clamp(fogFactor, 0.0f, 1.0f);
}


void main() 
{
    computeDirLight();
	
    if(showShadow) {
        float shadow = computeShadow();
        color = min((ambient + (1.0f - shadow)*diffuse) * texture(diffuseTexture, fTexCoords).rgb + ((1.0f - shadow)*specular) * texture(specularTexture, fTexCoords).rgb, 1.0f);
    }  else {
        color = min((ambient + diffuse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    }

    if (showNight) {
        vec3 nightColor = computeNightMode();
        color = mix(color, nightColor, 0.5f);
    }

    if(showSpotLight) {
        vec3 colorResultFromSpot = computeSpotLight();
        color += colorResultFromSpot;
    }

    if(showSnow) {
        float snow = computeSnow();
        color = mix(color, vec3(1.0f, 1.0f, 1.0f), snow);
    }

    if(showFog) {
        float fogFactor = computeFog();
        vec4 fogColor = vec4(0.8f, 0.8f, 1.0f, 1.0f);
        fColor = mix(fogColor, vec4(color, 1.0f), fogFactor);
    } else {
        fColor = vec4(color, 1.0f);
    }

    if(showRain){
        float rain = computeRain();
        fColor = mix(fColor, vec4(0.0f, 0.0f, 1.0f, 1.0f), rain);
    }
}
