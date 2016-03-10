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


        this.canvas.addEventListener("click", (function () {
            // We try to pick an object
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

            var info = this.scene.data[pickResult.pickedMesh.id];
            document.getElementById("fileNameField").innerHTML=info.id;
            document.getElementById("wcField").innerHTML=info.WC;
            document.getElementById("sizeField").innerHTML=info.size;
            document.getElementById("nomField").innerHTML=info.NOM;
                $("#tabInfo").show();
                $("#showHideInfo").removeClass('glyphicon glyphicon-chevron-down').addClass('glyphicon glyphicon-chevron-up');



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





    }


    updateScene(data){
        console.log(data,this.scene)
        this.scene.data = {};

      this.width = data.data.width;
        this.deep = data.data.deep;
        this.pivot = new BABYLON.Mesh.CreatePlane("plane", 0, this.scene, false, BABYLON.Mesh.DEFAULTSIDE);

        build.recursiveDraw(this.scene,new BABYLON.Vector3(0,0,0),data.data,this.pivot);

       // this.pivot.position = new BABYLON.Vector3(-this.width ,0,-this.deep );

    }


    color(data){
        data = JSON.parse(data);
        for(var c in data){
            var material =  new BABYLON.StandardMaterial(data[c].fileName+"_texture", this.scene);
            material.diffuseColor = new BABYLON.Color3(1,0,0);
            this.scene.models[data[c].fileName+"_model"].material = material;

        }
    }

    render(){

    }

    resize(){

    }

}



var scn;
window.createScene = function(url) {


    scn = new MainScene();
    $.get(url, scn.updateScene.bind(scn));
};


window.filter = function(id){
    $.get("/getAllMatch/"+id+"/"+$("#searchBox").val(), scn.color.bind(scn));
}