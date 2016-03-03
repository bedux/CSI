"use strict"

var BABYLON = require("babylonjs");
var build = require("./building.js");
var mock = require("../mock/templateScene.js");




class MainScene{
    constructor(canvans){
        this.engine = new BABYLON.Engine(canvas, true);
        this.engine.isPointerLock = false;
        this.scene  = new BABYLON.Scene(this.engine);

        // create a FreeCamera, and set its position to (x:0, y:5, z:-10)
        this.camera = new BABYLON.FreeCamera("FreeCamera", new BABYLON.Vector3(mock.pipoo.data.width, mock.pipoo.data.height, mock.pipoo.data.deep), this.scene);
        this.camera.maxZ = 100000;
        // target the camera to scene origin
        this.camera.setTarget(BABYLON.Vector3.Zero());

        // attach the camera to the canvas
        this.camera.attachControl(canvas, false);
        this.camera.speed=40;
        // create a basic light, aiming 0,1,0 - meaning, to the sky
        this.light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(10,10,0), this.scene);

        this.scene.data = {};
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
        window.addEventListener('resize', (function(){
            this.resize();
            this.engine.resize();
        }).bind(this));


        window.addEventListener("click", (function () {
            // We try to pick an object
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);

            var info = this.scene.data[pickResult.pickedMesh.id];
            document.getElementById("bar").innerHTML="Name:"+ info.id+ " | NOM: "+info.NOM + " | WC: "+info.WC + " | Size: "+info.size  + " | Width: "+info.width;

        }).bind(this));

    }


    updateScene(data){
        console.log(data,this.scene)
        this.scene.data = {};

      this.width = data.dta.data.width;
        this.deep = data.dta.data.deep;
        this.pivot = new BABYLON.Mesh.CreatePlane("plane", 0, this.scene, false, BABYLON.Mesh.DEFAULTSIDE);

        build.recursiveDraw(this.scene,new BABYLON.Vector3(0,0,0),data.dta.data,this.pivot);

       // this.pivot.position = new BABYLON.Vector3(-this.width ,0,-this.deep );

    }


    render(){

    }

    resize(){

    }

}

var canvas = document.getElementById('renderCanvas');
var scn = new MainScene(canvas);

    $.post("/getData",scn.updateScene.bind(scn));
