
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
        $.get("/getStatistics/"+encodeURI(info.id).split("/").join("%2F"),function(data){
            $("#statistics").empty();
            var table = $('<table class="table table-inverse"></table>');
            table.append($('  <thead> <tr> <th>Metrics</th> <th>Value</th></tr> </thead>'));
            var tbody = $(' <tbody></tbody>');
            data = JSON.parse(data)[0];
            renderData(data);

            for(var i in data){
                var row = $('<tr></tr>');
                var column = $('<td></td>').text(Information.idToText[i]);
                var column1 = $('<td></td>').text(data[i]);
                row.append(column);
                row.append(column1);
                tbody.append(row);
            }
            table.append(tbody)
            $("#statistics").append(table);

        });
    }
};