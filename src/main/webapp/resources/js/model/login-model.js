var loginModel = {
    checkLogin(username, password, errorCallback) {
        var data = {
            username: username,
            password: password
        };
        $.ajax({
            url: "/login",
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            type: "POST",
            async: false,
            success: function (data) {
                console.log(data);
                window.sessionStorage.setItem("token", data);
                window.location.href = "/home";
            },
            error: function (xhr, status, error) {
                if (errorCallback) {
                    errorCallback(status);
                }
            }
        });
    }
};