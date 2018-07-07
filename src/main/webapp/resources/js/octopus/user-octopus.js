var userOctopus = {
    init() {
        userModel.init();
        userView.init();
    },

    upload(data, callback) {
        userModel.uploadImage(data, callback);
    },

    getUser(username, callback) {
        userModel.getUser(username, callback);
    }
};