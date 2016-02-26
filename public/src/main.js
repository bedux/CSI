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
        this.camera = new BABYLON.FreeCamera("FreeCamera", new BABYLON.Vector3(0, 20, 20), this.scene);
        // target the camera to scene origin
        this.camera.setTarget(BABYLON.Vector3.Zero());

        // attach the camera to the canvas
        this.camera.attachControl(canvas, false);

        // create a basic light, aiming 0,1,0 - meaning, to the sky
       this.light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(10,10,100), this.scene);



        this.engine.runRenderLoop((function(){
            this.render();
            this.scene.render();
        }).bind(this));


        build.recursiveDraw(this.scene,BABYLON.Vector3.Zero(),mock.pipoo);

        // the canvas/window resize event handler
        window.addEventListener('resize', (function(){
            this.resize();
            this.engine.resize();
            this.light0.position = this.camera.position;
        }).bind(this));


    }



    render(){

    }

    resize(){

    }

}

var canvas = document.getElementById('renderCanvas');
var scn = new MainScene(canvas);