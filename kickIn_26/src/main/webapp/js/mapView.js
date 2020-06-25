let mapId, modal, trash, closeDelete, closeDelete2, map, printer, imgGroup,
    modal2, exportButton, mapName;

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

/**
 * @summary this is the initializing function for the map on the html page, it uses the leaflet
 * library.
 *
 * @returns {L.map}
 */
function initMap() {
    let newMap, tiles;
     newMap = L.map('mapid', {
        center: [52.2413, -353.1531],
        zoom: 16,
        keyboard: false,
        fullscreenControl: true,
        fullscreenControlOptions: {
            position: 'topleft'
        }
    });
     tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    printer = L.easyPrint({
        tileLayer: tiles,
        sizeModes: ['A4Landscape'],
        filename: mapName,
        exportOnly: true,
        hideControlContainer: true
    }).addTo(newMap);
    return newMap;
}

/**
 * @summary Due to how latLangs are also a JSONArray, parsing the only the top level object
 * does not parse the latLangs to a JSONArray as well, this needs to be done separately, and is done in this method.
 *
 * @param {String} stringyLatLangs the latLangs in a Stirng.
 *
 * @returns {L.latLng[]} the array of LeafletPoints which are needed by the distprtableImageOverlay
 * to add objects to the map.
 */
function parseLatLangs(stringyLatLangs) {
    let latLangArray, coords;
     latLangArray = JSON.parse(stringyLatLangs);
     coords = [];
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

/**
 * @summary loads all the objects stored for the map as stored on the database, and displays them onto the map.
 *
 * @param {number} mapId
 */
function bringAllObjectsForMap(mapId) {
    let response;
    getAllObjectsForMap(mapId, function () {
        response = JSON.parse(this.responseText);
        response.forEach(function (object) {
            mapObjects.set(object.objectId, object)
        })
        insertObjectsToMap();
    })
}

/**
 * @summary It iterates through the all the mapObjects, theses are the objects as loaded from the database,
 * and inserts them into the map.
 */
function insertObjectsToMap() {
    mapObjects.forEach(insertObjectIntoMap)
}

/**
 * @summary Insert the object on to the map.
 *
 * @param {Object} object the to add.
 */
function insertObjectIntoMap(object) {
    let corners, newImage;
    corners = parseLatLangs(object.latLangs);
    newImage = L.distortableImageOverlay(images.get(object.resourceId), {
        editable: false,
        actions: [],
        corners: corners
    })
    newImage.objectId = object.objectId;
    newImage.resourceId = object.resourceId;
    imgGroup.push(newImage);
    newImage.addTo(map);
}

/**
 * @summary brings all the resources that are currently in offer in the
 * database to be added on to the map. It creates a map for the images and the resource names
 * so that they can be called independently as required by different functions, like displaying the list of resources
 * adding a new image on the map.
 */
function bringAllResources() {
    let response;
    getAllResources(function () {
        response = JSON.parse(this.responseText);
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
    let mapEditBtn, mapData;
    getMap(mapId, function () {
        bringAllResources();
         mapEditBtn = document.getElementById("mapEditBtn");
         mapEditBtn.href = "mapEdit.html?mapId=" + mapId;
         mapData = JSON.parse(this.responseText);
         mapName = mapData.name;
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