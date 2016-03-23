/**
 * Created by bedux on 26/02/16.
 */
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
        diffuseColor:new BABYLON.Color3(0.443137,0.776471,0.443137)
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



function createBuilding(scene, position, data,parent,lp) {

    let cylinder;

    scene.data[data.id+ "_model"] = data;
    cylinder = BABYLON.Mesh.CreateBox(data.id + "_model", 1, scene, false);


    cylinder.scaling = new BABYLON.Vector3(data.width, data.height, data.deep);
    cylinder.position = new BABYLON.Vector3(position.x+data.width/2,position.y+data.height/2,position.z+data.deep/2);
    cylinder.parent = parent;
    updateMaximumPos(position.x+data.width,position.y+data.height,position.z+data.deep);

    let material = new BABYLON.ShaderMaterial(data.id+"_texture", scene,"/assets/javascripts/base",
        {
            attributes: ["position","uv","normal"],
            uniforms: ["worldViewProjection","world","color","LightPosition"]
        });
    if(data.color==-1){
        console.log(materials[data.buildingType].diffuseColor,"asdasd")
        material.setColor3("color",materials[data.buildingType].diffuseColor);


    }else{
        material.setColor3("color",   new BABYLON.Color3(0,data.color,data.color));


    }


    material.setVector3("LightPosition",lp);

    cylinder.material = material;

    scene.models[data.id+ "_model"] = cylinder
    return cylinder;
}


function recursiveDraw(scene,position,data,parent,lp){
   createBuilding(scene,position,data,parent,lp);

        if(data.children==null) return;

        if(data.children.length>0){
            for(let i of  data.children){
                recursiveDraw(scene,position.add(new BABYLON.Vector3(i.position[0],i.position[1]+data.height,i.position[2])),i.data,parent);
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
