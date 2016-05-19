"use strict"

var BABYLON = require("babylonjs");
var build = require("./building.js");
var mock = require("../mock/templateScene.js");
var Request = require("./request.js");
var Enum = require("./types.js");



class MainScene{
    constructor(){
        let canvas = document.getElementById('renderCanvas');

        this.canvas = canvas;
        this.engine = new BABYLON.Engine(canvas, true);
        this.engine.isPointerLock = false;
        this.scene  = new BABYLON.Scene(this.engine);
        this.scene.clearColor = new BABYLON.Color3(0.7,0.9,0.9);
        // create a FreeCamera, and set its position to (x:0, y:5, z:-10)

        if(mobileAndTabletcheck()){
            this.camera = new BABYLON.VirtualJoysticksCamera("VJC", new BABYLON.Vector3(1,0,0), this.scene);

        }else{
            this.camera = new BABYLON.FreeCamera("FreeCamera", new BABYLON.Vector3(1,0,0), this.scene);

        }

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



        this.engine.runRenderLoop($.proxy(function(){

        //    this.pivot.rotate(BABYLON.Axis.Y,  this.angle, BABYLON.Space.WORLD);
          //  this.pivot.translate(BABYLON.Axis.X, this.width, BABYLON.Space.LOCAL);
            this.render();
            this.scene.render();
        },this));




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

        var first = true;
        if(  Object.keys(this.scene.data).length>0 ){
            first = false;
           for(var i in  this.scene.data){
               this.scene.removeMesh(this.scene.getMeshByID(i))
           }
        }

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


        if(first) {
            this.camera.position = new BABYLON.Vector3(build.maxX, build.maxY, build.maxZ);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX / 2, 0, build.maxZ / 2));
        }

        $("#globalSpinner").hide();



    }

    color(id,data){
        function hexToR(h) {return parseInt((cutHex(h)).substring(0,2),16)}
        function hexToG(h) {return parseInt((cutHex(h)).substring(2,4),16)}
        function hexToB(h) {return parseInt((cutHex(h)).substring(4,6),16)}
        function cutHex(h) {return (h.charAt(0)=="#") ? h.substring(1,7):h}

        let obj = data.data;


        let material;
        material=new BABYLON.StandardMaterial(id+"_texture", this.scene);
        material.diffuseColor = new BABYLON.Color3(hexToR(data.color)/255,hexToG(data.color)/255,hexToB(data.color)/255);
        if(data.action.indexOf("Hide")!=-1){
            material.alpha = 0.1;
        }

        for(var c in obj){
            let current =  this.scene.models[obj[c].name+"_model"];
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
        console.log("asd",e.which);

        if(e.which == 48){
                var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
                h = h+h/2;
                h = (h>build.maxY)?h:build.maxY+h;
            this.camera.position = new BABYLON.Vector3(build.maxX/2, h, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, build.maxZ/2));
        }else
        if(e.which == 49){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX/2 , h+h/2, build.maxZ+build.maxZ);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, 0));
        }else
        if(e.which == 50){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX/2 , h+h/2, 0-build.maxZ);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX/2, 0, build.maxZ));
        }else
        if(e.which == 51){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(build.maxX+build.maxX , h+h/2, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(0, 0, build.maxZ/2 -1));
        }else
        if(e.which == 52){
            var h = Math.cos(this.camera.fov)*(build.maxX>build.maxZ?build.maxX:build.maxZ)
            this.camera.position = new BABYLON.Vector3(0-build.maxX , h+h/2, build.maxZ/2);
            this.camera.setTarget(new BABYLON.Vector3(build.maxX, 0, build.maxZ/2 -1));
        }else if(e.which == 53){
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
            if(!pickResult.hit){return;}
            let bbs = pickResult.pickedMesh.getBoundingInfo().boundingSphere;
            var h = Math.cos(this.camera.fov)*(bbs.radiusWorld)
            h = h*2;
            h = h < bbs.radiusWorld*2?bbs.radiusWorld*2:h;
            this.camera.position = (new BABYLON.Vector3(bbs.centerWorld.x,h,bbs.centerWorld.z));
            this.camera.setTarget(new BABYLON.Vector3(bbs.centerWorld.x,0,bbs.centerWorld.z));

        }else if(e.which == 104) {
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
            if (!pickResult.hit) {
                return;
            }
            console.log(pickResult)
            $("#pathP input").val(pickResult.pickedMesh.id.substr(0, pickResult.pickedMesh.id.indexOf("_model")))
            $('#sel1 option').filter(function () {
                return ($(this).text() == 'Hide'); //To select Blue
            }).prop('selected', true);

            $("#appFilter").trigger("click");
        }else if(e.which == 115) {
            var pickResult = this.scene.pick(this.scene.pointerX, this.scene.pointerY);
            if (!pickResult.hit) {
                return;
            }
            console.log(pickResult)
            $("#pathP input").val(pickResult.pickedMesh.id.substr(0, pickResult.pickedMesh.id.indexOf("_model")))
            $('#sel1 option').filter(function () {
                return ($(this).text() == 'Hide Other'); //To select Blue
            }).prop('selected', true);

            $("#appFilter").trigger("click");
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

window.addEventListener("keypress", $.proxy(
    function(e){

            scn.keyPress(e);
    }
,scn));

window.createScene = function(url) {
    scn = new MainScene();
    $.get(url,  $.proxy(scn.updateScene,scn));
};

window.addFilter = function(id,obj){
    scn.color(id,obj);

}

window.deleteFilter = function(id){
    scn.deleteFilter(id);
}

window.reloadStuff= function(data,name){
    if($("#globalSpinner").is(":visible")){
        return;
    }
    $("#TableElemcolorMetrics td" ).text(name);

    (function(){$.get(data,  $.proxy(scn.updateScene,scn))})();


}

window.createSceneWithData = function(json){
    console.log(JSON.parse(json))
    scn.updateScene(JSON.parse(json));
}


function mergeInfo(data){

    if(data[0].data.features.colorMetrics < 0 ){

        data[0].data.features.colorMetrics = 0;
    }
    
    if(data[0].data.color<0){

        data[0].data.color = 0;
    }

    var changes = 1;    
    for(var i in data){

     

            if(i!=0 &&  (data[i].data.buildingType == Enum.BuildingType.COLOR)){

                if(data[0].data.buildingType != Enum.BuildingType.COLOR){
                    data[0].data.features.colorMetrics =  data[i].data.features.colorMetrics;
                    data[0].data.color =  data[i].data.color;
                    data[0].data.buildingType = Enum.BuildingType.COLOR;
                }

                

                data[0].data.features.colorMetrics  += data[i].data.features.colorMetrics;
                data[0].data.color  += data[i].data.color;
                changes++;


            }else if(data[i].data.buildingType == Enum.BuildingType.NO_CARE){
                data[0].data.buildingType = Enum.BuildingType.NO_CARE;

            }
        


    }

    data[0].data.features.colorMetrics/=changes;
    data[0].data.color /=changes;

    




    var res = {};
    for(var i in data){

        var children = data[i].data.children;

        for(var l in children){

            if(!res[children[l].data.id]){

                res[children[l].data.id] = [];
            }

            res[children[l].data.id].push(children[l])
        }

    }


    for (var key in res) {
        mergeInfo(res[key])
    }


}


window.doItForMe = function(data){
    $.ajaxSetup({async:false});
    var c = [];
    for(var i in data){
        $.get(data[i],function(dta){
            c.push(dta);
        })
    }
    mergeInfo(c);

    scn.updateScene(c[0])
    $.ajaxSetup({async:true});



}
window.mobileAndTabletcheck = function() {
    var check = false;
    (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))check = true})(navigator.userAgent||navigator.vendor||window.opera);
    return check;
}