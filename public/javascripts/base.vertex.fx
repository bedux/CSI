precision mediump float;

// Attributes
attribute vec3 position;
attribute vec3 normal;
attribute vec2 uv;



// Uniforms
uniform mat4 world;
uniform mat4 worldViewProjection;
uniform vec3 color;
uniform vec3 LightPosition;


// Varying
varying vec3 vColor;
varying vec3 vPositionW;
varying vec3 vNormalW;
varying vec2 vUV;
varying vec3 vLightPosition;



void main(void) {

    vec4 outPosition = worldViewProjection * vec4(position, 1.0);
    gl_Position = outPosition;

    vPositionW = vec3(world * vec4(position, 1.0));
    vNormalW = normalize(vec3(world * vec4(normal, 0.0)));


    vUV = uv;
    vColor = color;
    vLightPosition = LightPosition;

}