/**
 * Created by bedux on 26/02/16.
 */
"use strict"
var BABYLON = require("babylonjs");

function updateMaximumPos(x,y,z){
    module.exports.maxX = Math.max(module.exports.maxX,x);
    module.exports.maxY = Math.max(module.exports.maxY,y);
    module.exports.maxZ = Math.max(module.exports.maxZ,z);

}

function createBuilding(scene, position, data,parent) {

    let cylinder;

    scene.data[data.id+ "_model"] = data;
    cylinder = BABYLON.Mesh.CreateBox(data.id + "_model", 1, scene, false);


    cylinder.scaling = new BABYLON.Vector3(data.width, data.height, data.deep);
    cylinder.position = new BABYLON.Vector3(position.x+data.width/2,position.y+data.height/2,position.z+data.deep/2);
    cylinder.parent = parent;
    updateMaximumPos(position.x+data.width,position.y+data.height,position.z+data.deep);


    let material =  new BABYLON.StandardMaterial(data.id+"_texture", scene);
    material.diffuseColor = new BABYLON.Color3(data.color[0],data.color[1],data.color[2]);
    material.specularColor = BABYLON.Color3.Black();


    cylinder.material = material;
    scene.models[data.id+ "_model"] = cylinder

}

function recursiveDraw(scene,position,data,parent){
        createBuilding(scene,position,data,parent);
        if(data.children==null) return;

        if(data.children.length>0){
            for(var i of  data.children){
                recursiveDraw(scene,position.add(new BABYLON.Vector3(i.position[0],i.position[1]+data.height,i.position[2])),i.data,parent);
            }
        }
}

module.exports = {
     createBuilding:createBuilding,
    recursiveDraw:recursiveDraw,
    maxX:-1000,
    maxY:-1000,
    maxZ:-1000


};
