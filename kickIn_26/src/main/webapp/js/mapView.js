let modal, trash, closeDelete, closeDelete2, map, tiles, printer, editToggle, imgGroup, inFreeHand, materialsList,
    modal2, share, closeExport, closeExport2;

// Get the modal
modal = document.getElementById("popupMapDelete");
// Button for deletion
trash = document.getElementById("deleteEvent");
// Closing popup
closeDelete = document.getElementsByClassName("close")[0];
//Closing by pressing "No"
closeDelete2 = document.getElementById("no");

trash.onclick = function () {
    modal.style.display = "block";
}

closeDelete.onclick = function () {
    modal.style.display = "none";
}

closeDelete2.onclick = function () {
    modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}
//Modal for exporting the map

//Get modal
modal2 = document.getElementById("modalExport");
//Button for exporting
share = document.getElementById("share");
//Closing popup
closeExport = document.getElementsByClassName("close")[0];
//Closing by pressing "No"
closeExport2 = document.getElementById("no1");

share.onclick = function () {
    modal2.style.display = "block";
}

closeExport.onclick = function () {
    modal2.style.display = "none";
}

closeExport2.onclick = function () {
    modal2.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === modal2) {
        modal2.style.display = "none";
    }
}

//inits the map and properties.
map = L.map('mapid', {
    center: [52.2413, -353.1531],
    zoom: 16,
    keyboard: false
});
tiles = L
    .tileLayer(
        'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
        {
            maxZoom: 19,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        });
tiles.addTo(map);

printer = L.easyPrint({
    tileLayer: tiles,
    sizeModes: ['Current', 'A4Landscape', 'A4Portrait'],
    filename: 'myMap',
    exportOnly: true,
    hideControlContainer: true
}).addTo(map);

editToggle = L.easyButton({
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
imgGroup = [];
//boolean to check weather free hand drawing is being done.
inFreeHand = false;

materialsList = null;

getAllMaterials(function () {
    materialsList = JSON.parse(this.responseText);
    console.log(materialsList);
})


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

/**
 * @summary function to enter free hand, if already in free hand, we exit free hand drawing.
 */
function callEnterFreeHand() {
    if (!inFreeHand) {
        enterFreeDrawingMode();
    } else {
        exitFreeDraw();
    }
}

/**
 * @summary function to enter free hand drawing.
 */
function enterFreeDrawingMode() {
    let divContainer, dimensionData, stage, layer, circle;
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
    divContainer = document.createElement('div');
    divContainer.setAttribute('id', 'container');
    // document.getElementsByClassName('leaflet-pane leaflet-overlay-pane')[0].appendChild(divContainer);
    document.getElementById('mapContainer').appendChild(
        divContainer);
    dimensionData = document.getElementById('mapContainer');
    stage = new Konva.Stage({
        container: 'container', // id of container <div>
        width: dimensionData.offsetWidth,
        height: dimensionData.offsetHeight,
    });
    layer = new Konva.Layer();
    circle = new Konva.Circle({
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

/**
 * @summary function to exit free hand drawing.
 */
function exitFreeDraw() {
    let data, needForAdd;
    let canvas, imgFree, nw, ne, sw, se = null;
    try {
        data = trimCanvas(document
            .getElementsByTagName('canvas')[0]);
    } catch (err) {
        data = null;
    }
    needForAdd = true;
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

/**
 * @summary function to get the geoJSON data that can be used to reconstruct the map if needed.
 */
function getGeoJSON() {
    imgGroup.forEach(function (image) {
        console.log('{corners: [' + image.getCorners()
            + '], url: ' + image._url + '}');
    })
}

/**
 * @summary function for filtering materials depending on entered text.
 */
function filterOn() {

}

/**
 * @summary This method is used to call on the two methods when the window is loaded.
 */
function loadNameDescriptionAndItems() {
    getMapNameAndDescription();
    listItems();
}

window.onload = loadNameDescriptionAndItems;

/**
 * @summary This method is used to display the list of all the items that have been placed on the map and their
 * respective quantities in the map.
 *
 * @description First, the ID of the required map is extracted from the URL. Then, the 'generateReportForMap' method is
 * called with the map ID as a parameter. If a successful result is received from the back-end, a table is built with
 * the information received. This table has two columns 'Name' and 'Quantity', where the column 'Name' gives the name of
 * the item that was placed on the map and the column 'Quantity' gives the count of each item that was placed on the map.
 */
function listItems() {

    let mapId, returnedItems, col, key, table, th, tr, i, j, tableCell;
    mapId = window.location.search.split("=")[1];
    returnedItems = '';
    generateReportForMap(mapId, function () {
        console.log(this.responseText);

        returnedItems = JSON.parse(this.responseText);

        col = [];
        for (i = 0; i < returnedItems.length; i++) {
            for (key in returnedItems[i]) {
                if (col.indexOf(key) === -1 && (key === 'name' || key === 'count')) {
                    if (key === 'name'){
                        col.push('Name');
                    } else if (key === 'count'){
                        col.push('Quantity');
                    }
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
    });
}

/**
 * @summary This method is used to get the name and description of the map required.
 *
 * @description After the ID of the required map is retrieved from the URL, the 'getMap' function is called with the ID
 * as a parameter. If the map information is successfully retrieved from the back-end, the name and description of the
 * map are displayed on the page where required.
 */
function getMapNameAndDescription() {
    let mapId, jsonData;
    mapId = window.location.search.split("=")[1];
    getMap(mapId, function () {
        jsonData = JSON.parse(this.responseText);
        document.getElementById("mapName").innerHTML = jsonData.name;
        document.getElementById("description").innerHTML = jsonData.description;
        console.log(this.responseText);
    })
}

/**
 * @summary This method is used to delete the map from the database.
 *
 * @description After the ID of the map is retrieved from the URL, the 'deleteMap' function is called with the ID of the
 * map to be deleted as a parameter. If the map was successfully deleted, the user is redirected to the 'list.html' page.
 */
function removeMap() {
    let mapId;
    mapId = window.location.search.split("=")[1];
    deleteMap(mapId, function () {
        window.location.href = "http://localhost:8080/kickInTeam26/list.html";
    })
}
