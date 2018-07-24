var GET ="GET", POST = "POST", PUT = "PUT", DELETE = "DELETE";
var templateModel = {
    getTemplateHabit(id, callback) {
        callAjax("/habit/get", { username: "template", habitId: id}, GET, true, true, callback);
    },

    addTemplateHabit(data, callback) {
        callAjaxJson("/habit/add", data, POST, true, true, callback);
    },

    updatePrivateMode(id, privateMode, callback) {
        var data = JSON.stringify({ username: "template", id: id, privateMode: privateMode });
        callAjaxJson("/habit/edit", data, PUT, true, true, callback);
    },

    markAsTemplate(id, callback) {
        callAjax("/habit/v1/mark-template", { habitId: id}, POST, true, true, callback);
    }
};