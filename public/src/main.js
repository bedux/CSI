"use strict"

var BABYLON = require("babylonjs");
var build = require("./building.js");
var mock = require("../mock/templateScene.js");




class MainScene{
    constructor(){
        let canvas = document.getElementById('renderCanvas');

        this.canvas = canvas;
        this.engine = new BABYLON.Engine(canvas, true);
        this.engine.isPointerLock = false;
        this.scene  = new BABYLON.Scene(this.engine);

        // create a FreeCamera, and set its position to (x:0, y:5, z:-10)
        this.camera = new BABYLON.FreeCamera("FreeCamera", new BABYLON.Vector3(mock.pipoo.data.width, mock.pipoo.data.height, mock.pipoo.data.deep), this.scene);
        this.camera.maxZ = 100000;
        // Create an ArcRotateCamera aimed at 0,0,0, with no alpha, beta or radius, so be careful. It will look broken.

        // target the camera to scene origin
        this.camera.setTarget(BABYLON.Vector3.Zero());

        // attach the camera to the canvas
        this.camera.attachControl(canvas, false);
        this.camera.speed=40;
        // create a basic light, aiming 0,1,0 - meaning, to the sky
        this.light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(10,10,0), this.scene);

        this.scene.data = {};
        this.scene.models ={};

        this.pivot =  BABYLON.Mesh.CreateBox("sphere",0, this.scene);
        this.pivot1 =  BABYLON.Mesh.CreateBox("sphere1",1, this.scene);
        this.pivot1.scaling =   new BABYLON.Vector3(1, 1000,1);

        this.width = 0;
        this.deep = 0;



        this.angle = 0.001;
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

        this.canvas.addEventListener("mousemove", (function () {
            // We try to pick an object

            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
            if(!pickResult.hit) return;
            if(this.old){
                this.old.material = this.old.oldMaterial;
            }

            this.old =  pickResult.pickedMesh;
            this.old.oldMaterial = pickResult.pickedMesh.material;

            let material=new BABYLON.StandardMaterial("hightlight", this.scene);
            material.diffuseColor = new BABYLON.Color3(1,1,0);
            pickResult.pickedMesh.material =material;
            var info = this.scene.data[pickResult.pickedMesh.id];
            document.getElementById("fileNameField").innerHTML=info.id;
            document.getElementById("wcField").innerHTML=info.WC;
            document.getElementById("sizeField").innerHTML=info.size;
            document.getElementById("nomField").innerHTML=info.NOM;




        }).bind(this));

        this.canvas.addEventListener("keydown", (
            function(e){
                var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
                if(e.shiftKey && pickResult.hit) {
                    let camera1 = new BABYLON.ArcRotateCamera("ArcRotateCamera", 0, 0,0, pickResult.pickedPoint, this.scene);


                    // Quick, let's use the setPosition() method... with a common Vector3 position, to make our camera better aimed.
                    camera1.setPosition(this.camera.position);
                    camera1.attachControl(this.canvas, false);


                    this.scene.activeCamera = camera1;


                }else{
                    //this.camera.setPosition(this.scene.activeCamera.position);

                    //this.camera.rotation =  this.scene.activeCamera.rotation ;

                    this.scene.activeCamera = this.camera;

                    this.camera.attachControl(this.canvas, false);




                }
            }
        ).bind(this), false);

        this.canvas.addEventListener("dblclick",this.getBlockOfCode.bind(this));

    }

    updateScene(data){

        this.scene.data = {};
        this.width = data.data.width;
        this.deep = data.data.deep;
        this.pivot = new BABYLON.Mesh.CreatePlane("plane", 0, this.scene, false, BABYLON.Mesh.DEFAULTSIDE);

        build.recursiveDraw(this.scene,new BABYLON.Vector3(0,0,0),data.data,this.pivot);

    }

    color(id,data){
        function hexToR(h) {return parseInt((cutHex(h)).substring(0,2),16)}
        function hexToG(h) {return parseInt((cutHex(h)).substring(2,4),16)}
        function hexToB(h) {return parseInt((cutHex(h)).substring(4,6),16)}
        function cutHex(h) {return (h.charAt(0)=="#") ? h.substring(1,7):h}

        let obj = data.data;


        var material;
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
    }

    resize(){
    }

    getBlockOfCode(){
        console.log("Getting code")
        let pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
        let info = this.scene.data[pickResult.pickedMesh.id];
        console.log((info.id).split("/").join("%2F"))
        $.get("/fileContent/"+encodeURI(info.id).split("/").join("%2F"),function(data){
                console.log(data);
                if(data.length>1){
                    $("#javaCode").empty();
                    $("#javaCode").append(data);
                        $('pre code').each(function(i, block) {
                            hljs.highlightBlock(block);
                        });

                    $("#codeModal").modal('show');

                }
        });

    }



}



var scn;



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