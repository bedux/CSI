"use strict"
var BABYLON = require("babylonjs");
var materials = {
    //Binary File
    0:{
        diffuseColor:new BABYLON.Color3(0.8,0.8,0.8)
    },
    //Non Java File
    1:{
        diffuseColor:new BABYLON.Color3(0.5,0.5,0.5)
    },

    2:{
        diffuseColor:new BABYLON.Color3(1,0.388235,0.278431),
    },
    3:{
        diffuseColor:new BABYLON.Color3(0.823529,0.705882,0.54902)
    },
    4:{
        diffuseColor:new BABYLON.Color3(0.6,0.6,0.6)
    },
    5:{
        diffuseColor:new BABYLON.Color3(0.0980392,0.0980392,0.439216)
    },
    6:{
        diffuseColor:new BABYLON.Color3( 0.529412,0.807843,0.980392)
    },
    7:{
        diffuseColor:new BABYLON.Color3(1,0.411765,0.705882)
    }

};

function hsvToRgb(h, s, v) {
    var r, g, b;
    var i;
    var f, p, q, t;

    // Make sure our arguments stay in-range
    h = Math.max(0, Math.min(360, h));
    s = Math.max(0, Math.min(100, s));
    v = Math.max(0, Math.min(100, v));

    // We accept saturation and value arguments from 0 to 100 because that's
    // how Photoshop represents those values. Internally, however, the
    // saturation and value are calculated from a range of 0 to 1. We make
    // That conversion here.
    s /= 100;
    v /= 100;

    if(s == 0) {
        // Achromatic (grey)
        r = g = b = v;
        return [r, g, b];
    }

    h /= 60; // sector 0 to 5
    i = Math.floor(h);
    f = h - i; // factorial part of h
    p = v * (1 - s);
    q = v * (1 - s * f);
    t = v * (1 - s * (1 - f));

    switch(i) {
        case 0:
            r = v;
            g = t;
            b = p;
            break;

        case 1:
            r = q;
            g = v;
            b = p;
            break;

        case 2:
            r = p;
            g = v;
            b = t;
            break;

        case 3:
            r = p;
            g = q;
            b = v;
            break;

        case 4:
            r = t;
            g = p;
            b = v;
            break;

        default: // case 5:
            r = v;
            g = p;
            b = q;
    }

    return [r , g, b];
}

function colorMesh(data,scene,lp){
    if(data.color==NaN){
        data.color = 0;
    }
    let material = new BABYLON.ShaderMaterial(data.id+"_texture", scene,"/assets/javascripts/base",
        {
            attributes: ["position","uv","normal"],
            uniforms: ["worldViewProjection","world","color","LightPosition","maxSize"]
        });
    if(data.color==-1){

        material.setColor3("color",materials[data.buildingType].diffuseColor);


    }else{
        if(data.buildingType==2){
            var color = hsvToRgb(180+((data.color)*90),100,100);

            material.setColor3("color",   new BABYLON.Color3(color[0],color[1],color[2]));

        }
        else if(data.buildingType==3){
            //   data.color = 1-data.color; //Invert
            //var color = hsvToRgb((data.color)*90,100,100);
            var color = hsvToRgb(180+((data.color)*90),100,100);

            //   var color = hsvToRgb(1,(data.color)*90,100); //Saturation
            //  var color = hsvToRgb(1,1,(data.color)*90); //b/n

            material.setColor3("color",   new BABYLON.Color3(color[0],color[1],color[2]));

        }

        else{
            if(data.color == 0){

                material.setColor3("color",   new BABYLON.Color3(0.6,0.6,0.6));

            }else{


                //coloring package

                //   data.color = 1-data.color;
                // var color = hsvToRgb(300+(data.color)*60,100,100);
                var color = hsvToRgb(240,(data.color)*60,100);
                //   var color = hsvToRgb(1,(data.color)*90,100); //Saturation
                //  var color = hsvToRgb(1,1,(data.color)*90); //b/n

                //material.setColor3("color",   new BABYLON.Color3(0,data.color*0.7 + 0.3,data.color*0.7 + 0.3));
                material.setColor3("color",   new BABYLON.Color3(color[0],color[1],color[2]));


            }
        }



    }


    material.setVector3("maxSize",new BABYLON.Vector3(data.width,data.height,data.deep));

    material.setVector3("LightPosition",lp);
    return material;
}


function createBuilding(scene, position, data,parent,lp) {

    if(data.buildingType==8) return;
    let cylinder;

    scene.data[data.id+ "_model"] = data;
    cylinder = BABYLON.Mesh.CreateBox(data.id + "_model", 1, scene, false);


    cylinder.scaling = new BABYLON.Vector3(data.width, data.height, data.deep);
    cylinder.position = new BABYLON.Vector3(position.x+data.width/2,position.y+data.height/2,position.z+data.deep/2);
    cylinder.parent = parent;
    updateMaximumPos(position.x+data.width,position.y+data.height,position.z+data.deep);



    cylinder.material = colorMesh(data,scene,lp);

    scene.models[data.id+ "_model"] = cylinder
    return cylinder;
}


function recursiveDraw(scene,position,data,parent,lp){
   createBuilding(scene,position,data,parent,lp);

        if(data.children==null) return;

        if(data.children.length>0){
            for(let i of  data.children){
                recursiveDraw(scene,position.add(new BABYLON.Vector3(i.position[0],i.position[1]+data.height,i.position[2])),i.data,parent,lp);
            }
        }
}

function updateMaximumPos(x,y,z){
    module.exports.maxX = Math.max(module.exports.maxX,x);
    module.exports.maxY = Math.max(module.exports.maxY,y);
    module.exports.maxZ = Math.max(module.exports.maxZ,z);

}


module.exports = {
        createBuilding:createBuilding,
        recursiveDraw:recursiveDraw,
        maxX:-1000,
        maxY:-1000,
        maxZ:-1000
};
