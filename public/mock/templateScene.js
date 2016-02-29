/**
 * Created by bedux on 26/02/16.
 */



var t1 =
{"position":[0.0,10.0,0.0],"data":{"width":16.0,"height":10.0,"deep":16.0,"color":[1.0,0.0,1.0],"segment":4,"id":"1","children":[{"position":[0.0,0.0,16.0],"data":{"width":0.0,"height":0.0,"deep":8.0,"color":[0.5599263310432434,0.8047444224357605,0.29696187376976013],"segment":4,"id":"1/.DS_Store","children":null}},{"position":[0.0,0.0,0.0],"data":{"width":8.0,"height":6.0,"deep":8.0,"color":[0.8480561375617981,0.6561819911003113,0.10977436602115631],"segment":4,"id":"1/Component copy 2.java","children":null}},{"position":[0.0,0.0,8.0],"data":{"width":8.0,"height":6.0,"deep":8.0,"color":[0.3324410021305084,0.2592712640762329,0.22700762748718262],"segment":4,"id":"1/Component copy 3.java","children":null}},{"position":[8.0,0.0,0.0],"data":{"width":16.0,"height":6.0,"deep":8.0,"color":[0.05811548978090286,0.41430529952049255,0.22117725014686584],"segment":4,"id":"1/Component copy.java","children":null}},{"position":[8.0,0.0,8.0],"data":{"width":8.0,"height":6.0,"deep":8.0,"color":[0.28187650442123413,0.38856813311576843,0.26381874084472656],"segment":4,"id":"1/Component.java","children":null}}]}}



var test = {
    width: 3,
    height: 10,
    color: [1, 0, 1],
    segment: 10,
    id: "1",
    children: [
        {
            position: [0, 10, 0],
            data: {
                width: 3,
                height: 10,
                color: [1, 0, 0],
                segment: 10,
                id: "2",
                children: []
            }
        },
        {
            position: [3, 10, 0],
            data: {
                width: 3,
                height: 10,
                color: [1, 0, 0],
                segment: 10,
                id: "3",
                children: []

            }

        }
    ]
};

module.exports = {


    pipoo: t1,

    buildExample: {
        width: 10,
        height: 10,
        color: [1, 0, 0],
        segment: 30,
        children: null,
        id: "5"

    }
}