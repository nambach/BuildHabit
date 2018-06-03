var callAjax = function (url, data, method, async, loading, callback, errorCallback) {
    $.ajax({
        url: url,
        data: data,
        type: method,
        async: async,
        success: function(result){
            callback(result);
        },
        error: function (xhr, status, error) {
            errorCallback(status);
        }
    });
};