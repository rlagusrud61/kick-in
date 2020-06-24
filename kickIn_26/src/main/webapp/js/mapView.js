let mapId, modal, trash, closeDelete, closeDelete2, map, printer, imgGroup,
    modal2, printPlugin;

mapId = window.location.search.split("=")[1];
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
window.onclick = function (event) {
    if (event.target === modal2) {
        modal2.style.display = "none";
    }
}

function initMap() {
    let newMap = L.map('mapid', {
        center: [52.2413, -353.1531],
        zoom: 16,
        keyboard: false,
        fullscreenControl: true,
        fullscreenControlOptions: {
            position: 'topleft'
        }
    });
    let tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    L.easyPrint({
        sizeModes: ['A4Landscape']
    }).addTo(newMap);
    return newMap;
}
function parseLatLangs(stringyLatLangs) {
    let latLangArray = JSON.parse(stringyLatLangs);
    let coords = [];
    latLangArray.forEach(function (point) {
        coords.push(L.latLng(point.lat, point.lng))
    })
    return coords;
}

//inits the map and properties.
map = initMap();
imgGroup = [];
//all available resources
resources = new Map();
//their images
images = new Map();
mapObjects = new Map();

function bringAllObjectsForMap(mapId) {
    getAllObjectsForMap(mapId, function () {
        let response = JSON.parse(this.responseText);
        response.forEach(function (object) {
            mapObjects.set(object.objectId, object)
        })
        insertObjectsToMap();
    })
}
function insertObjectsToMap() {
    mapObjects.forEach(insertObjectIntoMap)
}
function insertObjectIntoMap(object) {
    let corners = parseLatLangs(object.latLangs);
    let newImage = L.distortableImageOverlay(images.get(object.resourceId), {
        editable: false,
        actions: [],
        corners: corners
    })
    newImage.objectId = object.objectId;
    newImage.resourceId = object.resourceId;
    imgGroup.push(newImage);
    newImage.addTo(map);
}
function bringAllResources() {
    getAllResources(function () {
        let response = JSON.parse(this.responseText);
        response.forEach(function (resource) {
            images.set(resource.resourceId, resource.image);
            resources.set(resource.resourceId, resource.name)
        })
        bringAllObjectsForMap(mapId);
    })
}

/**
 * @summary This method is used to call on the two methods when the window is loaded.
 */
function initPage() {
    getMap(mapId, function () {
        bringAllResources();
        let mapEditBtn = document.getElementById("mapEditBtn");
        mapEditBtn.href = "mapEdit.html?mapId=" + mapId;
        let mapData = JSON.parse(this.responseText);
        document.getElementById("mapName").innerHTML = mapData.name;
        document.getElementById("description").innerHTML = mapData.description;
        listItems(mapData.report);
    })
}

window.onload = initPage;

/**
 * @summary This method is used to display the list of all the items that have been placed on the map and their
 * respective quantities in the map.
 *
 * @description First, the ID of the required map is extracted from the URL. Then, the 'generateReportForMap' method is
 * called with the map ID as a parameter. If a successful result is received from the back-end, a table is built with
 * the information received. This table has two columns 'Name' and 'Quantity', where the column 'Name' gives the name of
 * the item that was placed on the map and the column 'Quantity' gives the count of each item that was placed on the map.
 */
function listItems(report) {
    let returnedItems, col, key, table, th, tr, i, j, tableCell;
    returnedItems = report;
    col = ["Name", "Count"];
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
            tableCell.innerHTML = returnedItems[i][col[j].toLowerCase()]; // adds the required data to the table
        }
    }
    document.getElementById("listItems").appendChild(table);
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
        window.location.href = "list.html";
    })
}