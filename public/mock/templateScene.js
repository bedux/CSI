/**
 * Created by bedux on 26/02/16.
 */
var   test  = {
        width: 3,
        height: 10,
        color: [1,0,1],
        segment: 10,
        id:"1",
        children: [
        {
        position: [0, 10, 0],
        data: {
            width: 3,
            height: 10,
            color: [1,0,0],
            segment: 10,
            id:"2",
            children: []
            }
        },
    {
        position: [3, 10, 0],
        data: {
            width: 3,
            height: 10,
            color: [1,0,0],
            segment: 10,
            id:"3",
            children: []

            }

        }
]
};

module.exports = {



 pipoo : {
    width: 3,
    height: 10,
    color:  [1,0,0],
    segment: 10,
    id:"4",
    children: [{
        position: [5, 0, 5],
        data:test
    }


    ]


},

 buildExample : {
    width: 10,
    height: 10,
    color:  [1,0,0],
    segment: 30,
    children: null,
     id:"5"

}

}