var loginView = {
    txtUsername: null,
    txtPassword: null,
    btnSubmit: null,
    boxMessage: null,
    init() {
        this.txtUsername = $("#username");
        this.txtPassword = $("#password");
        this.btnSubmit = $("#btnSubmit");
        this.boxMessage = $("#message");

        this.bindEvent();
    },

    bindEvent() {
        loginView.btnSubmit.on("click", function (e) {
            e.preventDefault();
            e.stopImmediatePropagation();

            var username = loginView.txtUsername.val();
            var password = loginView.txtPassword.val();

            loginModel.checkLogin(username, password, function () {
                loginView.boxMessage.removeClass("hidden");
                loginView.txtUsername.addClass("is-invalid");
                loginView.txtPassword.addClass("is-invalid");
            });
        });
    }
};