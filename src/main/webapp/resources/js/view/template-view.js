var templateView = {

    bootgrid: null,

    fileInput: null,
    fileImg: null,
    btnSubmit: null,

    staticModal: null,

    init: function () {
        this.staticModal = $("#staticModal");
        this.bindBootgrid();
        this.bindForm();
    },

    bindBootgrid() {
        templateView.bootgrid = jQuery("#template-table").bootgrid({
            ajax: true,
            url: "/template/page",
            method: "GET",
            rowCount: 10,
            navigation: 2,
            requestHandler: function (request) {
                return request;
            },
            ajaxSettings: {
                method: "GET",
                cache: false
            },
            formatters: {
                "schedule": function (column, row) {
                    var convertHour = function(timepoint) {
                        return timepoint.hour + ":" + timepoint.minute;
                    };

                    function capitalizeFirstLetter(string) {
                        return string.charAt(0).toUpperCase() + string.slice(1);
                    }

                    var convertRepetition = function (schedule) {
                        switch (schedule.repetition) {
                            case "daily":
                                return "Everyday";
                            case "weekly":
                                return "<span class='badge badge-warning'>Weekly</span> on day <br/>" + schedule.times.reduce(function (accum, item) {
                                    return accum + ", " + capitalizeFirstLetter(item.day);
                                }, "").replace(/^, /g, "");
                            case "monthly":
                                return "<span class='badge badge-info'>Monthly</span> on date <br/>" + schedule.times.reduce(function (accum, item) {
                                    return accum + ", " + item.date;
                                }, "").replace(/^, /g, "");
                            case "yearly":
                                return "<span class='badge badge-primary'>Yearly</span> in month <br/>" + schedule.times.reduce(function (accum, item) {
                                    return accum + ", " + item.month;
                                }, "").replace(/^, /g, "");
                        }
                    };

                    var schedule = row.schedule;
                    var from = convertHour(schedule.from);
                    var to = convertHour(schedule.to);

                    return "<p style='word-wrap: break-word'>" + from + " - " + to + "<br/>" + convertRepetition(schedule) + "</p>";
                },
                "tags": function (column, row) {
                    var blank = " ";
                    var result = "";
                    for(var i = 0; i < row.tags.length; i++) {
                        result += "<span class='badge badge-secondary'>" + row.tags[i] + "</span>" + blank;
                    }
                    return result;
                },
                "privateMode": function (column, row) {
                    var style = "badge ";
                    if (row.privateMode === "public") {
                        style += "badge-success";
                    } else {
                        style += "badge-secondary";
                    }
                    return "<span class='" + style + "'>" + row.privateMode + "</span>"
                },
                "commands": function (column, row) {
                    return "<button type=\"button\" class=\"btn btn-sm btn-outline-primary command-public\" data-row-id=\"" + row.id + "\"><span class=\"fa fa-check\"></span></button> " +
                        "<button type=\"button\" class=\"btn btn-sm btn-outline-secondary command-private\" data-row-id=\"" + row.id + "\"><span class=\"fa fa-minus-circle\"></span></button>";
                }
            }
        }).on("loaded.rs.jquery.bootgrid", function () {
            /* Executes after data is loaded and rendered */
            templateView.bootgrid.find(".command-public").on("click", function (e) {
                var id = $(this).data("row-id");
                templateModel.updatePrivateMode(id, "public", function () {
                    templateView.bootgrid.bootgrid("reload");
                });
            }).end().find(".command-private").on("click", function (e) {
                var id = $(this).data("row-id");
                templateModel.updatePrivateMode(id, "private", function () {
                    templateView.bootgrid.bootgrid("reload");
                });
            });
        });


    },

    bindForm() {
        templateView.fileInput = $("#inputFile");
        templateView.btnSubmit = $("#btnSubmit");


    }


};