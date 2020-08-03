
var context;
var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var paint = false;
var startX;
var startY;
var recPoint = 0;
var tempoStartRecX;
var tempoStartRecY;
var FirstStartX = 'null';
var FirstStartY = 'null';
var XList = new Array();
var YList = new Array();
var storestartX;
var storestartY;
var storeEndX;
var storeEndY;
var click = 0;
var done = false;

function prepareRecCanvas(image, xLeft, xTop, existRoom)
{
    var img = document.getElementById('floorplanImg');
    var canvasDiv = document.getElementById('canvasDiv');
    var height = parseInt(img.height * (1040 / img.width));
    if (document.getElementById('canvasp') !== null) {
        canvasDiv.removeChild(document.getElementById('canvasp'));
    }
    canvas = document.createElement('canvas');
    canvas.setAttribute('width', 1040);
    canvas.setAttribute('height', height);
    canvas.setAttribute('id', 'canvasr');
    canvas.setAttribute('style', "background-image: url('" + image + "');background-size:cover;");
    canvasDiv.appendChild(canvas);
    context = canvas.getContext("2d");
    var coordinate = document.getElementById('coordinate');
    coordinate.value = "";
    click = 0;
    $('#canvasr').mousedown(function (e)
    {
        click = 1;
        coordinate.value = "";
        var mouseX = e.pageX - xLeft - 3;
        var mouseY = e.pageY - xTop - 3;
        startX = mouseX;
        startY = mouseY;
        paint = true;
        addClick(mouseX, mouseY);
        redrawRec();
    });
    $('#canvasr').mousemove(function (e) {
        if (paint) {
            recPoint = 0;
            addClick(e.pageX - xLeft - 3, e.pageY - xTop - 3, true);
            redrawRec();
        }
    });
    $('#canvasr').mouseup(function (e) {
        if (!(tempoStartRecX === e.pageX - xLeft - 3)) {
            recPoint = 1;
            redrawRec();
            var inside = false;
            for (i = 0; i < existRoom.length; i++) {
                var tempoXList = existRoom[i]["xCoor"].split(" ");
                var tempoYList = existRoom[i]["yCoor"].split(" ");
                for (j = 0; j < tempoXList.length; j++) {
                    if (context.isPointInPath(parseInt(tempoXList[j]), parseInt(tempoYList[j]))) {
                        inside = true;
                        break;
                    }
                }
            }
            if (inside) {
                alert("Selected area is consist of other room. Please redraw");
                location.reload();
            } else {
                alert('Area is Highlighted');
                coordinate.value = parseInt(storestartX,0) + "," + parseInt(storestartY,0) + "_" 
                                   + (parseInt(storestartX,0) + parseInt(storeEndX,0)) + "," + parseInt(storestartY,0) + "_" 
                                   + (parseInt(storestartX,0) + parseInt(storeEndX,0)) + "," + (parseInt(storestartY,0) + parseInt(storeEndY,0)) + "_" 
                                   + parseInt(storestartX,0) + "," + (parseInt(storestartY,0) + parseInt(storeEndY,0)) + "_" ;
            }
        }
        paint = false;
    });
}

function addClick(x, y, dragging)
{
    clickX.push(x);
    clickY.push(y);
    clickDrag.push(dragging);
}

function clearCanvas()
{
    if (click !== 0 && coordinate.value === "") {
        alert("Selected area is consist of other room. Please redraw");
        location.reload();
    }
}
function redrawRec()
{
    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    context.strokeStyle = "#df4b26";
    context.lineJoin = "bevel";
    context.lineWidth = 3;
    var endX;
    var endY;
    for (var i = 0; i < clickX.length; i++) {
        context.beginPath();
        endX = clickX[i] - startX;
        endY = clickY[i] - startY;
        context.rect(startX, startY, endX, endY);
        storestartX = startX;
        storestartY = startY;
    }
    if (recPoint === 0) {
        tempoStartRecX = startX;
        tempoStartRecY = startY;
        recPoint++;
    } else if (recPoint === 1) {
        endX = startX - tempoStartRecX;
        endY = startY - tempoStartRecY;
        context.rect(tempoStartRecX, tempoStartRecY, endX, endY);
        storestartX = tempoStartRecX;
        storestartY = tempoStartRecY;
        if (endX !== 0) {
            storeEndX = endX;
            storeEndY = endY;
        } else {
            storeEndX = clickX[clickX.length - 1] - startX;
            storeEndY = clickY[clickX.length - 1] - startY;
        }
        recPoint--;
    }
    context.stroke();
}
function preparePolCanvas(image, xLeft, xTop, existRoom)
{
    var img = document.getElementById('floorplanImg');
    var canvasDiv = document.getElementById('canvasDiv');
    var height = parseInt(img.height * (1040 / img.width));
    if (document.getElementById('canvasr') !== null) {
        canvasDiv.removeChild(document.getElementById('canvasr'));
    }
    canvas = document.createElement('canvas');
    canvas.setAttribute('width', 1040);
    canvas.setAttribute('height', height);
    canvas.setAttribute('id', 'canvasp');
    canvas.setAttribute('style', "background-image: url('" + image + "');background-size:cover;");
    canvasDiv.appendChild(canvas);
    context = canvas.getContext("2d");
    var coordinate = document.getElementById('coordinate');
    coordinate.value = "";
    click = 0;
    $('#canvasp').mousedown(function (e)
    {
        var mouseX = e.pageX - xLeft - 3;
        var mouseY = e.pageY - xTop - 3;
        if (FirstStartX === 'null' && FirstStartX === 'null') {
            FirstStartX = mouseX;
            FirstStartY = mouseY;
        }
        if (FirstStartX !== 'null' && FirstStartX !== 'null' && paint === false && XList.length > 1) {
            FirstStartX = mouseX;
            FirstStartY = mouseY;
            XList = new Array();
            YList = new Array();
            click = 0;
            coordinate.value = "";
        }
        if (click > 9) {
            FirstStartX = mouseX;
            FirstStartY = mouseY;
            XList = new Array();
            YList = new Array();
            click = 0;
        }
        startX = mouseX;
        startY = mouseY;
        paint = true;
        addClick(mouseX, mouseY);
        redrawPol();
    });
    $('#canvasp').mousemove(function (e) {
        if (!(FirstStartX !== 'null' && FirstStartX !== 'null' && paint === false && XList.length > 1)) {
            context.clearRect(0, 0, context.canvas.width, context.canvas.height);
            var mouseX = e.pageX - xLeft - 3;
            var mouseY = e.pageY - xTop - 3;
            addClick(mouseX, mouseY);
            context.strokeStyle = "#df4b26";
            context.lineWidth = 3;
            context.beginPath();
            for (i = 0; i <= XList.length; i++) {
                context.lineTo(XList[i], YList[i]);
            }
            context.lineTo(mouseX, mouseY);
            context.stroke();
        }
        if (click > 9 && !done) {
            context.clearRect(0, 0, context.canvas.width, context.canvas.height);
        }
    });
    $('#canvasp').mouseup(function (e) {
        latestEndX = clickX[clickX.length - 1];
        latestEndY = clickY[clickY.length - 1];
        startX = latestEndX;
        startY = latestEndY;
        click++;
        if (FirstStartX + 3 >= latestEndX && FirstStartX - 3 <= latestEndX
                && FirstStartY + 3 >= latestEndY && FirstStartY - 3 <= latestEndY && XList.length > 1) {
            paint = false;
            inside = false;
            for (i = 0; i < existRoom.length; i++) {
                var tempoXList = existRoom[i]["xCoor"].split(" ");
                var tempoYList = existRoom[i]["yCoor"].split(" ");
                for (j = 0; j < tempoXList.length; j++) {
                    if (context.isPointInPath(parseInt(tempoXList[j]), parseInt(tempoYList[j]))) {
                        console.log(parseInt(tempoXList[j]), parseInt(tempoYList[j]));
                        console.log(inside);
                        inside = true;
                        break;
                    }
                }
            }
            if (inside) {
                alert("Selected area is consist of other room. Please redraw");
                location.reload();
            } else {
                alert('Area is Highlighted');
                var coorValue = "";
                for (i = 0; i < XList.length - 1; i++) {
                    coorValue += parseInt(XList[i],0) + "," + parseInt(YList[i],0) + "_";
                }
                coordinate.value = coorValue;
                done = true;
            }
        } else if (click > 9) {
            done = false;
            alert('Too many side for polygon. Please redraw');
        }
    });
}
function redrawPol()
{
    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    context.strokeStyle = "#df4b26";
    context.lineJoin = "bevel";
    context.lineWidth = 3;
    context.beginPath();
    context.moveTo(FirstStartX, FirstStartY);
    addPath(clickX[clickX.length - 1], clickY[clickY.length - 1]);
    for (i = 0; i < XList.length; i++) {
        context.lineTo(XList[i], YList[i]);
    }
    context.stroke();
}

function addPath(x, y) {
    XList.push(x);
    YList.push(y);
}