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
        // target the camera to scene origin
        this.camera.setTarget(BABYLON.Vector3.Zero());

        // attach the camera to the canvas
        this.camera.attachControl(canvas, false);
        this.camera.speed=54;
        // create a basic light, aiming 0,1,0 - meaning, to the sky
       this.light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(10,10,0), this.scene);






        this.engine.runRenderLoop((function(){
            this.render();
            this.scene.render();
        }).bind(this));


        build.recursiveDraw(this.scene,new BABYLON.Vector3(0,0,0),mock.pipoo.data);

        var cylinder = BABYLON.Mesh.CreateCylinder("zeerp", 100,1,1, 4,  this.scene, false);

        // the canvas/window resize event handler
        window.addEventListener('resize', (function(){
            this.resize();
            this.engine.resize();
        }).bind(this));


    }



    render(){

    }

    resize(){

    }

}

var canvas = document.getElementById('renderCanvas');
var scn = new MainScene(canvas);