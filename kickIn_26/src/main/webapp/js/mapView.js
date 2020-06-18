// Get the modal
var modal = document.getElementById("popupMapDelete");
// Button for deletion
var trash = document.getElementById("deleteEvent");
// Closing popup
var closeDelete = document.getElementsByClassName("close")[0];
//Closing by pressing "No"
var closeDelete2 = document.getElementById("no");

     trash.onclick = function() {
         modal.style.display = "block";
     }

     closeDelete.onclick = function() {
            modal.style.display = "none";
     }

     closeDelete2.onclick = function() {
         modal.style.display = "none";
     }

     window.onclick = function(event) {
         if (event.target === modal) {
             modal.style.display = "none";
         }
     }


//inits the map and properties.
let map = L.map('mapid', {
    center: [52.2413, -353.1531],
    zoom: 16,
    keyboard: false
});
let tiles = L
    .tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
            maxZoom: 19,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        });
tiles.addTo(map);

var printer = L.easyPrint({
    tileLayer: tiles,
    sizeModes: ['Current', 'A4Landscape', 'A4Portrait'],
    filename: 'myMap',
    exportOnly: true,
    hideControlContainer: true
}).addTo(map);

let editToggle = L.easyButton({
    id: 'edit-mode-toggle',
    states: [{
        stateName: 'disable-editing',
        icon: 'fa-edit',
        title: 'Disable Editing',
        onClick: function (control) {
            imgGroup.forEach(function (img) {
                img.editing.disable();
            });
            control.state('enable-editing');
        }
    }, {
        stateName: 'enable-editing',
        icon: 'fa-window-close',
        title: 'Enable Editing',
        onClick: function (control) {
            imgGroup.forEach(function (img) {
                img.editing.enable();
            });
            control.state('disable-editing');
        }
    }]
});
editToggle.addTo(map);

//the array in which all the images added are stored
let imgGroup = [];
//boolean to check weather free hand drawing is being done.
let inFreeHand = false;

let materialsList = null;

let objReq = new XMLHttpRequest();
objReq.onreadystatechange = function () {
    console.log("something")
    if (this.readyState === 4 && this.status === 200) {
        materialsList = JSON.parse(objReq.responseText);
        console.log(materialsList)
    } else {
        console.log("failure")
    }
}
objReq.open("GET",
    ("http://localhost:8080/kickInTeam26/rest/materials"));
objReq.send();

// let img = L.distortableImageOverlay('..\\resources\\testImages\\example.jpg', {
//     actions: [L.RotateAction, L.DragAction, L.ScaleAction, L.DeleteAction]
// }).addTo(map);
// imgGroup.push(img);
// img.on('remove', function() {
//     const index = imgGroup.indexOf(img);
//     if (index !== -1) {
//         imgGroup.splice(index, 1);
//     }
// });

//function to enter free hand, if already in free hand, we exit free hand drawing.
function callEnterFreeHand() {
    if (!inFreeHand) {
        enterFreeDrawingMode();
    } else {
        exitFreeDraw();
    }
}

//enter free hand drawing.
function enterFreeDrawingMode() {
    inFreeHand = true;
    map.dragging.disable();
    map.touchZoom.disable();
    map.doubleClickZoom.disable();
    map.scrollWheelZoom.disable();
    map.boxZoom.disable();
    map.keyboard.disable();
    map.zoomControl.disable();
    if (map.tap)
        map.tap.disable();
    document.getElementById('mapid').style.cursor = 'crosshair';
    if (editToggle.state() === 'disable-editing') {
        imgGroup.forEach(function (img) {
            img.editing.disable();
        });
    }
    editToggle.disable();

    let divContainer = document.createElement('div');
    divContainer.setAttribute('id', 'container');
    // document.getElementsByClassName('leaflet-pane leaflet-overlay-pane')[0].appendChild(divContainer);
    document.getElementById('mapContainer').appendChild(
        divContainer);
    let dimensionData = document.getElementById('mapContainer');

    let stage = new Konva.Stage({
        container: 'container', // id of container <div>
        width: dimensionData.offsetWidth,
        height: dimensionData.offsetHeight,
    });
    let layer = new Konva.Layer();
    let circle = new Konva.Circle({
        x: (stage.width() / 2),
        y: (stage.height() / 2),
        radius: 70,
        fill: 'red',
        stroke: 'black',
        strokeWidth: 4,
        draggable: true,
    });
    layer.add(circle);
    stage.add(layer);
    layer.draw();
}

//exit free hand drawing.
function exitFreeDraw() {
    let data;
    try {
        data = trimCanvas(document
            .getElementsByTagName('canvas')[0]);
    } catch (err) {
        data = null;
    }

    let needForAdd = true;
    let canvas = null;
    let imgFree = null;
    let nw, ne, sw, se = null;

    if (data !== null && (data[2] === 0 || data[3] === 0)) {
        needForAdd = false;
    } else {
        if (data !== null) {
            nw = map
                .containerPointToLatLng([data[0], data[1]]);
            ne = map.containerPointToLatLng([
                data[0] + data[2], data[1]]);
            sw = map.containerPointToLatLng([data[0],
                data[1] + data[3]]);
            se = map.containerPointToLatLng([
                data[0] + data[2], data[1] + data[3]]);
            canvas = data[4];
            imgFree = canvas.toDataURL("image/png");
        } else {
            needForAdd = false;
        }
    }

    document.getElementById('container').remove();
    map.dragging.enable();
    map.touchZoom.enable();
    map.doubleClickZoom.enable();
    map.scrollWheelZoom.enable();
    map.boxZoom.enable();
    map.keyboard.enable();
    map.zoomControl.enable()
    if (map.tap)
        map.tap.enable();
    document.getElementById('mapid').style.cursor = 'grab';

    if (editToggle.state() === 'disable-editing') {
        imgGroup.forEach(function (img) {
            img.editing.enable();
        });
    }
    editToggle.enable();

    if (needForAdd) {
        let freeDrawing = L.distortableImageOverlay(
            imgFree,
            {
                corners: [nw, ne, sw, se],
                actions: [L.LockAction, L.UnlockAction,
                    L.OpacityAction, L.DeleteAction],
            }).addTo(map);
        imgGroup.push(freeDrawing);
        freeDrawing.on('remove', function () {
            const index = imgGroup.indexOf(freeDrawing);
            if (index !== -1) {
                imgGroup.splice(index, 1);
            }
        });
    }
    inFreeHand = false;
}

//get the geoJSON data that can be used to reconstruct the map if needed.
function getGeoJSON() {
    imgGroup.forEach(function (image) {
        console.log('{corners: [' + image.getCorners()
            + '], url: ' + image._url + '}');
    })
}

//for filtering materials depending on entered text
function filterOn() {

}

function getMap() {
    let url, xhr;
    url = window.location.href;
    url = url.split("/");
    url = url[10];
    xhr = new XMLHttpRequest();
    xhr.open('GET',
        "http://localhost:8080/kickInTeam26/rest/map/"
        + url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function everything() {
    getMapNameAndDescription();
    listItems();
}

function listItems() {

    let xhr, mapId, returnedItems, col, key, table, th, tr, i, j, tableCell;
    xhr = new XMLHttpRequest();
    mapId = 1;
    returnedItems = '';
    xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/objects/1/report",true);

    xhr.onreadystatechange = function () {

        if (xhr.readyState === 4 && xhr.status === 200) {

            console.log(xhr.responseText);

            returnedItems = JSON.parse(xhr.responseText);

            col = [];
            for (i = 0; i < returnedItems.length; i++) {
                for (key in returnedItems[i]) {
                    if (col.indexOf(key) === -1 && (key === 'name' || key === 'count')) {
                        col.push(key);
                    }
                }
            }

            table = document.createElement("table"); // creates the table
            table.setAttribute("id", "resources")
            table.setAttribute("class", "table table-hover")
            tr = table.insertRow(-1); // add a row to the table

            for (i = 0; i < col.length; i++) {
                th = document.createElement("th"); // add a header to the table
                th.innerHTML = col[i];
                tr.appendChild(th);
            }

            for (i = 0; i < returnedItems.length; i++) {

                tr = table.insertRow(-1); // adds a new row
                for (j = 0; j < col.length; j++) {
                    tableCell = tr.insertCell(-1);
                    tableCell.innerHTML = returnedItems[i][col[j]]; // adds the required data to the table
                }
            }
            document.getElementById("listItems").appendChild(table);
        }
    }

    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();

}

function getMapNameAndDescription() {

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {

        if (xhr.readyState == 4 && xhr.status == 200) {
            let jsonData = JSON.parse(xhr.responseText);
            document.getElementById("mapName").innerHTML = jsonData.name;
            document.getElementById("description").innerHTML = jsonData.description;
            console.log(xhr.responseText);
        }

    }

    xhr.open('GET',
        "http://localhost:8080/kickInTeam26/rest/map/1",
        true)
    xhr.send();

}

function deleteMap() {
    let url, xhr;
    url = window.location.href;
    url = url.split("/");
    url = url[10];
    xhr = new XMLHttpRequest();
    xhr.open('DELETE',
        "http://localhost:8080/kickInTeam26/rest/map/"
        + url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function logout() {
    let xhr = new XMLHttpRequest();
    xhr.open(
        'DELETE',
        "http://localhost:8080/kickInTeam26/rest/authentication",
        true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
            window.location.href = "http://localhost:8080/kickInTeam26/login.html";
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function goBack() {
    window.history.back();
}