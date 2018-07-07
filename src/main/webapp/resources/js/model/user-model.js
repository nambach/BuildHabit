var userModel = {
    init: function () {
    },
    
    uploadImage(data, callback) {
        callAjax("/upload-img", data, "POST", true, true, callback);
    },

    getUser(username, callback) {
        callAjax("/user/get", {username: username}, "GET", true, true, callback);
    },

    blockUser(username, callback) {
        callAjax("/user/block", { username: username }, "PUT", true, true, callback);
    },

    activatedUser(username, callback) {
        callAjax("/user/activate", { username: username }, "PUT", true, true, callback);
    },

    deactivatedUser(username, callback) {
        callAjax("/user/deactivate", { username: username }, "PUT", true, true, callback);
    }
};