/**
 * Created by bedux on 26/02/16.
 */
"use strict"
var BABYLON = require("babylonjs");
function createBuilding(scene, position, data) {
    var cylinder = BABYLON.Mesh.CreateCylinder(data.id+"_model", data.height, data.width, data.width, data.segment,  scene, false);
    cylinder.position = position;
    var material =  new BABYLON.StandardMaterial(data.id+"_texture", scene);
    material.diffuseColor = new BABYLON.Color3(data.color[0],data.color[1],data.color[2]);
    material.specularColor = BABYLON.Color3.Black();

    cylinder.material = material;
    cylinder.convertToFlatShadedMesh();


}

function recursiveDraw(scene,position,data){
        createBuilding(scene,position,data);
        if(data.children.length>0){
            for(var i of  data.children){
                recursiveDraw(scene,position.add(new BABYLON.Vector3(i.position[0],i.position[1],i.position[2])),i.data);
            }
        }


}

module.exports = {
     createBuilding:createBuilding,

    recursiveDraw:recursiveDraw


};