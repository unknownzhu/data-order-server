var BASE_URL = "http://" + window.location.host;
var SERVER_NAME = "/data-order-server";
var SERVER_URL = "http://" + window.location.host + SERVER_NAME;
var ws = {}

function GetQueryString(name) {
    var after = window.location.href.split("?")[1];
    if (after) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = after.match(reg);
        if (r != null) {
            return decodeURIComponent(r[2]);
        } else {
            return null;
        }
    }
}
