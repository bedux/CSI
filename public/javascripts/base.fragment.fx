precision mediump float;

varying vec3 vColor;
varying vec3 vPositionW;
varying vec3 vNormalW;
varying vec2 vUV;
varying vec3 vLightPosition;

void main(void) {
    float ToonThresholds[4];
    ToonThresholds[0] = 4000.0;
    ToonThresholds[1] = 1500.0;
    ToonThresholds[2] = 1000.0;
    ToonThresholds[3] = 500.0;

    float ToonBrightnessLevels[5];
    ToonBrightnessLevels[0] = 0.45;
    ToonBrightnessLevels[1] = 0.75;
    ToonBrightnessLevels[2] = 0.90;
    ToonBrightnessLevels[3] = 0.95;
    ToonBrightnessLevels[4] = 1.0;


    // Light
    vec3 lightVectorW = normalize(vLightPosition-vPositionW);
    vec3 pos = normalize(vNormalW);


    // diffuse
    float ndl = max(0., dot(vNormalW, lightVectorW));

    vec3 color = vColor;

    float distance1 = distance(vLightPosition,vPositionW);
    if (distance1 > ToonThresholds[0])
    {
        color *= ToonBrightnessLevels[0];
//                        gl_FragColor = vec4(0.0,1.0,0.0, 1.);

    }
    else if (distance1 > ToonThresholds[1])
    {
        color *= ToonBrightnessLevels[1];
//                gl_FragColor = vec4(0.0,0.0,1.0, 1.);

    }
    else if (distance1 > ToonThresholds[2])
    {
        color *= ToonBrightnessLevels[2];
//        gl_FragColor = vec4(1.0,0.0,1.0, 1.);

    }
    else if (distance1 > ToonThresholds[3])
    {
        color *= ToonBrightnessLevels[3];
//        gl_FragColor = vec4(1.0,1.0,0.0, 1.);

    }
    else
    {
        color *= ToonBrightnessLevels[4];
//        gl_FragColor = vec4(1.0,0.0,0.0, 1.);

    }

color = ndl * color + vColor*0.5;
    gl_FragColor = vec4(color, 1.);
}
