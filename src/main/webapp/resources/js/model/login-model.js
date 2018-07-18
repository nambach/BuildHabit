var loginModel = {
    checkLogin(username, password, errorCallback) {
        var data = {
            username: username,
            password: password
        };
        $.ajax({
            url: "/login",
            data: data,
            type: "POST",
            async: false,
            success: function (data) {
                if (data) data = JSON.parse(data);
                window.localStorage.setItem("token", data);
                window.href = "/";
            },
            error: function (xhr, status, error) {
                if (errorCallback) {
                    errorCallback(status);
                }
            }
        });
    }
};