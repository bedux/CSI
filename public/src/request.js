
var Information = require("./information");

module.exports = {
    getFileContent: function (info) {
        $.get("/fileContent/" + encodeURI(info.id).split("/").join("%2F"), function (data) {
            if (data.length > 1) {
                $("#javaCode").empty();
                $("#javaCode").append(data);
                $('pre code').each(function (i, block) {
                    hljs.highlightBlock(block);
                });

                $("#codeModal").modal('show');
            }
        });
    },
    getFileStatistics: function(info){
        //$.get("/getStatistics/"+encodeURI(info.id).split("/").join("%2F"),function(data){
        //    $("#statistics").empty();
        //    var table = $('<table class="table table-inverse"></table>');
        //    table.append($('  <thead> <tr> <th>Metrics</th> <th>Value</th></tr> </thead>'));
        //    var tbody = $(' <tbody></tbody>');
        //    data = JSON.parse(data)[0];
        //    renderData(data);
        //
        //    for(var i in data){
        //        var row = $('<tr></tr>');
        //        var column = $('<td></td>').text(Information.idToText[i]);
        //        var column1 = $('<td></td>').text(data[i]);
        //        row.append(column);
        //        row.append(column1);
        //        tbody.append(row);
        //    }
        //    table.append(tbody)
        //    $("#statistics").append(table);
        //
        //});
    },

    getDiscussions: function (info) {
        console.log(info.id)
        $.ajax({ type: "POST",
            url: "/getDiscussion",
            data:JSON.stringify({path:info.id}),
            contentType:"application/json",
            success: function (data) {

                console.log(data);
               var jsonData = (JSON.parse(data));
                console.log(jsonData);

                $("#discussions").empty();


                if (jsonData != null) {

                        var table = $('<table class="table table-inverse"></table>');
                        table.append($('  <thead> <tr> <th>Discussion URL</th></tr> </thead>'));
                        var tbody = $(' <tbody></tbody>');
                        for(var i in jsonData){
                            var row = $('<tr></tr>');
                            var current = i;
                            current = current.replace(".json","");
                            current = current.substring(current.lastIndexOf("/")+1);
                            current = "http://stackoverflow.com/questions/"+current;
                            var column = $('<td></td>').append($('<a  href="'+current+'" target="_blank"></a>').text(current));
                            row.append(column);
                            var c = $('<td></td>');
                            for(var o in jsonData[i]){
                                var column = $('<span></span>').text(jsonData[i][o]+" | ");
                                c.append(column)
                            }
                            row.append(c);
                            tbody.append(row);
                        }
                        table.append(tbody)
                        $("#discussions").append(table);


                }
            }
        });
    }
};