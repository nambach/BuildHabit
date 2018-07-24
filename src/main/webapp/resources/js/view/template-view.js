var templateView = {

    bootgrid: null,

    btnNew: null,
    staticForm: {},
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
        var form = templateView.staticForm;
        form.title = $("#inputTitle");
        form.description = $("#inputDescription");
        form.icon = $("#inputIcon");
        form.tags = $("#inputTags");

        form.repetition = $("#selectRepetition");
        form.times = $("#selectTimes");
        templateView.repetitionBox.initRepetition(form.repetition, form.times);

        form.from = $("#inputFrom");
        form.to = $("#inputTo");
        templateView.hourMinuteBox.init(form.from);
        templateView.hourMinuteBox.init(form.to);

        templateView.btnSubmit = $("#btnSubmit").on("click", function (e) {
            var data = templateView.validateForm();
            if (!data) return;
            templateModel.addTemplateHabit(data, function (data) {
                alert("Added successfully");
                templateView.bootgrid.bootgrid("reload");
                templateView.staticModal.modal("hide");
            })
        });

        templateView.btnNew = $("#btnNew").on("click", function () {
            templateView.staticModal.modal();
        })
    },

    validateForm() {
        var getHourMinute = templateView.hourMinuteBox.getValue;
        var getRepetitionTimes = templateView.repetitionBox.getValue;
        var form = templateView.staticForm;

        var title, description, icon, tags, repetition, from, to;
        if ((title = form.title.val().trim()) === "") return false;
        if ((description = form.description.val().trim()) === "") return false;
        if ((icon = form.icon.val().trim()) === "") return false;
        if ((tags = form.tags.val().trim()) === "") return false;

        tags = tags.split(",");

        repetition = getRepetitionTimes(form.repetition, form.times);
        from = getHourMinute(form.from);
        to = getHourMinute(form.to);

        var schedule = $.extend({}, repetition, {from: from, to: to});

        var data = JSON.stringify({
            username: "template",
            title: title,
            description: description,
            icon: icon,
            tags: tags,
            schedule: schedule,
            startTime: new Date().getTime(),
            endTime: -1
        });

        console.log(JSON.parse(data));
        return data;
    },

    repetitionBox: {
        initRepetition($elRep, $elTimes) {

            //Render Yearly
            templateView.repetitionBox.renderYearly();

            //Render Repetition and Times
            $elRep.append("<option value='daily'>Daily</option>")
                .append("<option value='weekly'>Weekly</option>")
                .append("<option value='monthly'>Monthly</option>")
                .append("<option value='yearly'>Yearly</option>")
                .on("change", function () {
                    // Hide Yearly Box
                    $("#inputYearly").addClass("hidden");

                    var $timesGroup = $("#times-group");

                    var rep = $(this).find("option:selected").val();
                    switch (rep) {
                        case "daily":
                            $timesGroup.addClass("hidden");
                            break;
                        case "weekly":
                            $timesGroup.removeClass("hidden");
                            $elTimes.chosen("destroy");
                            templateView.repetitionBox.renderWeekly($elTimes);
                            $elTimes.chosen();
                            break;
                        case "monthly":
                            $timesGroup.removeClass("hidden");
                            $elTimes.chosen("destroy");
                            templateView.repetitionBox.renderMonthly($elTimes);
                            $elTimes.chosen();
                            break;
                        case "yearly":
                            $timesGroup.removeClass("hidden");
                            $("#inputYearly").removeClass("hidden")
                            break;
                    }
                });
        },

        renderWeekly($el) {
            $el.find("option").remove();

            $el.append("<option value='mon'>Monday</option>")
                .append("<option value='tue'>Tuesday</option>")
                .append("<option value='wed'>Wednesday</option>")
                .append("<option value='thu'>Thursday</option>")
                .append("<option value='fri'>Friday</option>")
                .append("<option value='sat'>Saturday</option>")
                .append("<option value='sun'>Sunday</option>");
        },

        renderMonthly($el) {
            $el.find("option").remove();

            for (var i = 1; i <= 31; i++) {
                $el.append($("<option>", {
                    value: i,
                    text: i + ""
                }));
            }
        },

        renderYearly() {
            var $el = $("#inputYearly");
            var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

            //Render dates
            templateView.repetitionBox.renderMonthly($el.find("select[name='date']"));

            //Render months
            var $date = $el.find("select[name='month']");
            for (var i = 1; i <= 12; i++) {
                $date.append($("<option>", {
                    value: i,
                    text: months[(i-1)]
                }));
            }
        },

        getValue($elRep, $elTimes) {
            var rep = $elRep.val();
            var times = $elTimes.val();
            return {
                repetition: rep,
                times: times
            }
        }
    },

    hourMinuteBox: {

        init($el) {
            var i;
            var selectHour = $el.find("select[name='hour']");
            for (i = 0; i < 24; i++) {
                selectHour.append($("<option>", {
                    value: i,
                    text: i > 9 ? i : "0" + i
                }));
            }

            var selectMinute = $el.find("select[name='minute']");
            for (i = 0; i < 60; i++) {
                selectMinute.append($("<option>", {
                    value: i,
                    text: i > 9 ? i : "0" + i
                }));
            }
        },

        getValue($el) {
            return {
                hour: $el.find("select[name='hour']").val(),
                minute: $el.find("select[name='minute']").val()
            }
        }
    }
};