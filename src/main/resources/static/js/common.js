var baseURL = "/";

function loadTables(datasourceId) {
    var tableList = [];
    var url = baseURL + "datasource/listTable/" + datasourceId;
    $.ajax({
        type: "GET",
        url: url,
        async: false,
        success: function (r) {
            tableList = r.tableList;
        }
    });

    return tableList;
}


