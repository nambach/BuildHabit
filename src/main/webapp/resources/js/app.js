var callAjax = function (url, data, method, async, loading, callback, errorCallback) {
    var token = window.localStorage.getItem("token");
    var isTokenSetup = window.localStorage.getItem("isTokenSetup");

    if (token && isTokenSetup === undefined) {
        $.ajaxSetup({
            beforeSend: function(xhr) {
                xhr.setRequestHeader('Authorization', token);
            }
        });
        window.localStorage.setItem("isTokenSetup", "true");
    }

    $.ajax({
        url: url,
        data: data,
        type: method,
        async: async,
        success: function(result){
            if (callback) {
                callback(result);
            }
        },
        error: function (xhr, status, error) {
            if (errorCallback) {
                errorCallback(status);
            }
        }
    });
};