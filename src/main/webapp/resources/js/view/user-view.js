var userView = {

    bootgrid: null,

    init: function () {
        this.bindBootgrid();
    },

    bindBootgrid: function () {
        var grid = jQuery("#user-table").bootgrid({
            ajax: true,
            url: "/user/page",
            method: "GET",
            rowCount: 100,
            navigation: 2,
            requestHandler: function (request) {
                return request;
            },
            ajaxSettings: {
                method: "GET",
                cache: false
            },
            formatters: {
                "commands": function (column, row) {
                    return "<button type=\"button\" class=\"btn btn-xs btn-default command-edit\" data-row-id=\"" + row.username + "\"><span class=\"fa fa-pencil\"></span></button> " +
                        "<button type=\"button\" class=\"btn btn-xs btn-default command-delete\" data-row-id=\"" + row.username + "\"><span class=\"fa fa-trash-o\"></span></button>";
                }
            }
        }).on("loaded.rs.jquery.bootgrid", function () {
            /* Executes after data is loaded and rendered */
            grid.find(".command-edit").on("click", function (e) {
                alert("You pressed edit on row: " + $(this).data("row-id"));
            }).end().find(".command-delete").on("click", function (e) {
                alert("You pressed delete on row: " + $(this).data("row-id"));
            });
        });


    }
};