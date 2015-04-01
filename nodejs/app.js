
var express = require('express');
var app = express();

app.get('/ping', function (req, res) {
    res.send("PONG");
});

var server = app.listen(3001, function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('Example app listening at http://%s:%s', host, port);
})