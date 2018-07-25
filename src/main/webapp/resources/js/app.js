var callAjax = function (url, data, method, async, loading, callback, errorCallback) {
    var token = window.sessionStorage.getItem("token");

    if (token) {
        $.ajaxSetup({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', token);
            }
        });
    }

    $.ajax({
        url: url,
        data: data,
        type: method,
        async: async,
        success: function (result) {
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

var callAjaxJson = function (url, data, method, async, loading, callback, errorCallback) {
    var token = window.sessionStorage.getItem("token");

    if (token) {
        $.ajaxSetup({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Authorization', token);
            }
        });
    }

    $.ajax({
        url: url,
        data: data,
        contentType: 'application/json',
        dataType: 'json',
        type: method,
        async: async,
        success: function (result) {
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