var userView = {

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
        userView.bootgrid = jQuery("#user-table").bootgrid({
            ajax: true,
            url: "/user/page",
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
                "commands": function (column, row) {
                    return "<button type=\"button\" class=\"btn btn-sm btn-outline-primary command-activate\" data-row-id=\"" + row.username + "\"><span class=\"fa fa-check\"></span></button> " +
                        "<button type=\"button\" class=\"btn btn-sm btn-outline-danger command-block\" data-row-id=\"" + row.username + "\"><span class=\"fa fa-minus-circle\"></span></button> " +
                        "<button type=\"button\" class=\"btn btn-sm btn-outline-secondary command-deactivate\" data-row-id=\"" + row.username + "\"><span class=\"fa fa-minus-circle\"></span></button>";
                },
                "status": function (column, row) {
                    var style = "badge ";
                    if (row.accountStatus === "activated") {
                        style += "badge-success";
                    } else if (row.accountStatus === "blocked") {
                        style += "badge-danger";
                    } else if (row.accountStatus === "pending") {
                        style += "badge-warning";
                    } else if (row.accountStatus === "deactivated") {
                        style += "badge-secondary";
                    }
                    return "<span class='" + style + "'>" + row.accountStatus + "</span>"
                }
            }
        }).on("loaded.rs.jquery.bootgrid", function () {
            /* Executes after data is loaded and rendered */
            userView.bootgrid.find(".command-activate").on("click", function (e) {
                var username = $(this).data("row-id");
                userModel.activatedUser(username, function () {
                    userView.bootgrid.bootgrid("reload");
                });
            }).end().find(".command-block").on("click", function (e) {
                var username = $(this).data("row-id");
                userModel.blockUser(username, function () {
                    userView.bootgrid.bootgrid("reload");
                });
            }).end().find(".command-deactivate").on("click", function (e) {
                var username = $(this).data("row-id");
                userModel.deactivatedUser(username, function () {
                    userView.bootgrid.bootgrid("reload");
                });
            });
        });


    },

    bindForm() {
        userView.fileInput = $("#inputFile");
        userView.btnSubmit = $("#btnSubmit");

        userView.btnSubmit.on("click", function (e) {
            e.preventDefault();

            var data = {
                image: document.getElementById("inputFileImg").src
            };
            userOctopus.upload(data, function (data) {
                userView.staticModal.modal("hide");
                alert(data);
            });
        });

        function readFile() {
            if (this.files && this.files[0]) {

                var FR= new FileReader();

                FR.addEventListener("load", function(e) {
                    document.getElementById("inputFileImg").src = e.target.result;
                });

                FR.readAsDataURL( this.files[0] );
            }
        }
        userView.fileInput.on("change", readFile);
    },


};