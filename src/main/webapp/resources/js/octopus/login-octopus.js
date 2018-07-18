var loginOctopus = {
    init() {
        this.checkIfLogin();
        loginView.init();
    },

    checkIfLogin() {
        if (window.localStorage.getItem("token")) {
            window.location.href = "/home";
        }
    }
};