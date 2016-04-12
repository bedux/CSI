"use strict"

var BABYLON = require("babylonjs");
var build = require("./building.js");
var mock = require("../mock/templateScene.js");
var Request = require("./request.js");



class MainScene{
    constructor(){
        let canvas = document.getElementById('renderCanvas');

        this.canvas = canvas;
        this.engine = new BABYLON.Engine(canvas, true);
        this.engine.isPointerLock = false;
        this.scene  = new BABYLON.Scene(this.engine);
        this.scene.clearColor = new BABYLON.Color3(0.7,0.9,0.9);
        // create a FreeCamera, and set its position to (x:0, y:5, z:-10)
        this.camera = new BABYLON.FreeCamera("FreeCamera", new BABYLON.Vector3(1,0,0), this.scene);
        this.camera.maxZ = 100000;

        // target the camera to scene origin
        this.camera.setTarget(BABYLON.Vector3.Zero());

        // attach the camera to the canvas
        this.camera.attachControl(canvas, false);
        this.camera.speed=40;

        // create a basic light, aiming 0,1,0 - meaning, to the sky
        this.light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(10,10,0), this.scene);

        //init data content
        this.scene.data = {};
        this.scene.models ={};




        this.width = 0;
        this.deep = 0;
        // Shadows



        this.engine.runRenderLoop((function(){

        //    this.pivot.rotate(BABYLON.Axis.Y,  this.angle, BABYLON.Space.WORLD);
          //  this.pivot.translate(BABYLON.Axis.X, this.width, BABYLON.Space.LOCAL);
            this.render();
            this.scene.render();
        }).bind(this));




        // the canvas/window resize event handler
        this.canvas.addEventListener('resize', (function(){
            this.resize();
            this.engine.resize();
        }).bind(this));




        this.canvas.addEventListener("dblclick",this.getBlockOfCode.bind(this));
        this.canvas.addEventListener("mousemove",this.sceneInformation.bind(this));
        this.canvas.addEventListener("mouseout",this.mouseOut.bind(this));



    }

    updateScene(data){
        console.log(data);
        this.scene.data = {};
        this.width = data.data.width;
        this.deep = data.data.deep;

        //this.light = new BABYLON.PointLight("Omnu1", new BABYLON.Vector3(0, 10000,0),this.scene);
        //this.light.setEnabled(1);

      // this.pivot = BABYLON.Mesh.CreateBox("box", 0.0, this.scene, false, BABYLON.Mesh.DEFAULTSIDE);



        //this.shadowGenerator = new BABYLON.ShadowGenerator(1024, this.light);
        //
        //this.shadowGenerator.useVarianceShadowMap = true;

        build.recursiveDraw(this.scene,new BABYLON.Vector3(0,0,0),data.data,this.pivot,new BABYLON.Vector3(0,0,0));


        this.camera.position = new BABYLON.Vector3(build.maxX, build.maxY, build.maxZ);
        this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, build.maxZ/2));



    }

    color(id,data){
        function hexToR(h) {return parseInt((cutHex(h)).substring(0,2),16)}
        function hexToG(h) {return parseInt((cutHex(h)).substring(2,4),16)}
        function hexToB(h) {return parseInt((cutHex(h)).substring(4,6),16)}
        function cutHex(h) {return (h.charAt(0)=="#") ? h.substring(1,7):h}

        let obj = data.data;


        let material;
        material=new BABYLON.StandardMaterial("_texture_"+id, this.scene);
        material.diffuseColor = new BABYLON.Color3(hexToR(data.color)/255,hexToG(data.color)/255,hexToB(data.color)/255);
        if(data.action.indexOf("Hide")!=-1){
            material.alpha = 0.1;
        }

        for(var c in obj){
            let current =  this.scene.models[obj[c].fileName+"_model"];
            current["filterMaterial"+id] =  current.material;


            if(!current.hasOwnProperty("listFilter")){
                current["listFilter"]=[];
                current["baseMaterial"] = current.material;
            }
            if(this.old && this.old.id == current.id){
                current["baseMaterial"] = current.oldMaterial;
                this.old = undefined;
            }

            current["listFilter"].push("filterMaterial"+id)
            current.material = material;
        }
    }

    deleteFilter(id){

        let modelsList = this.scene.models;
        for(let c in  modelsList) {
            if(modelsList[c].hasOwnProperty("filterMaterial"+id)){
                let index = modelsList[c]["listFilter"].indexOf("filterMaterial"+id);
                if(index!=-1){
                    let deleted = modelsList[c]["listFilter"].splice(index, 1);
                    let lastMaterial;
                    if( modelsList[c]["listFilter"].length == 0){
                        modelsList[c].material = modelsList[c]["baseMaterial"];
                    }else {
                        lastMaterial = modelsList[c]["listFilter"][modelsList[c]["listFilter"].length - 1];
                        modelsList[c].material = modelsList[c][lastMaterial];
                    }
                }
            }
        }
    }

    render(){

        //var m = BABYLON.Matrix.RotationX(this.camera.rotation.x)
        //m = m.multiply(BABYLON.Matrix.RotationY(this.camera.rotation.y));
        //m = m.multiply(BABYLON.Matrix.RotationZ(this.camera.rotation.z)) ;


        for(var i in this.scene.models){
            if( this.scene.models[i].material.setVector3) {
                this.scene.models[i].material.setVector3("LightPosition",this.camera.position);
            }

        }
    }

    resize(){
    }

    keyPress(e){

        if(e.keyCode == 48){
                var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
                h = h+h/2;
                h = (h>build.maxY)?h:build.maxY+h;
            this.camera.position = new BABYLON.Vector3(build.maxX/2, h, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, build.maxZ/2));
        }else
        if(e.keyCode == 49){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX/2 , h+h/2, build.maxZ+build.maxZ);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, 0));
        }else
        if(e.keyCode == 50){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX/2 , h+h/2, 0-build.maxZ);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, build.maxZ));
        }else
        if(e.keyCode == 51){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX+build.maxX , h+h/2, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(0, 0, build.maxZ/2 -1));
        }else
        if(e.keyCode == 52){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(0-build.maxX , h+h/2, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX, 0, build.maxZ/2 -1));
        }else if(e.keyCode == 53){
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
            if(!pickResult.hit){return;}
            let bbs = pickResult.pickedMesh.getBoundingInfo().boundingSphere;
            var h = Math.cos(this.camera.fov)*(bbs.radiusWorld)
            h = h*2;
            h = h < bbs.radiusWorld*2?bbs.radiusWorld*2:h;
            this.camera.position = (new BABYLON.Vector3(bbs.centerWorld.x,h,bbs.centerWorld.z));
            this.camera.setTarget(new BABYLON.Vector3(bbs.centerWorld.x,0,bbs.centerWorld.z));

        }
        if(this.camera.position.y > this.camera.maxZ){
            this.camera.maxZ = this.camera.position.y + 30;
        }



    }

    getBlockOfCode(){

        let pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
        let info = this.scene.data[pickResult.pickedMesh.id];

        Request.getFileContent(info);
        //Request.getFileStatistics(info);
        Request.getDiscussions(info);

    }


    sceneInformation(){
        // We try to pick an object

        var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
        if(!pickResult.hit){
            if(this.old){
                this.old.material = this.old.oldMaterial;
                this.old = undefined;
            }
            return;
        }
        if(this.old){
            this.old.material = this.old.oldMaterial;
        }

        this.old =  pickResult.pickedMesh;
        this.old.oldMaterial = pickResult.pickedMesh.material;

        let material=new BABYLON.StandardMaterial("hightlight", this.scene);
        material.diffuseColor = new BABYLON.Color3(1,1,0);
        pickResult.pickedMesh.material =material;
        var info = this.scene.data[pickResult.pickedMesh.id];

        var arr = $("#tabInfo").find("td");


        for(var i in arr){
            if(arr[i].id && arr[i].id!="") {
                arr[i].innerHTML = info.features[arr[i].id];
            }
        }
        $("#pathIdTab").text(info.id)
    }


    mouseOut(){
        if(this.old){
            this.old.material = this.old.oldMaterial;
            this.old = undefined;
        }
    }
}



var scn;

window.addEventListener("keypress", (
    function(e){
            scn.keyPress(e);
    }
).bind(scn));

window.createScene = function(url) {
    scn = new MainScene();
    $.get(url, scn.updateScene.bind(scn));
};

window.addFilter = function(id,obj){
    scn.color(id,obj);

}

window.deleteFilter = function(id){
    scn.deleteFilter(id);
}