let localReport, map, mapId, resources, images, mapObjects, imgGroup, newObjects, deletedObjects;

localReport = new Map();
map = initMap();
mapId = window.location.search.split("=")[1];
//all available resources
resources = new Map();
//their images
images = new Map();
//stuff on the map
mapObjects = new Map();
//the image group for the library
imgGroup = [];
//new objects added to the map.
newObjects = [];
deletedObjects = new Set();

bringAllResources();

function openAccordion(id) {
    let availableItems;
     availableItems = document.getElementById(id);
    if (availableItems.className.indexOf("w3-show") === -1) {
        availableItems.className += " w3-show";
        availableItems.previousElementSibling.className += " w3-green";
    } else {
        availableItems.className = availableItems.className.replace(" w3-show", "");
        availableItems.previousElementSibling.className = availableItems.previousElementSibling.className.replace(" w3-green", "");
    }
}

function updateLocalReport() {
    let itemReport, tableHTML;
    itemReport = document.getElementById("displayitems");
    generateReportForMap(mapId, function () {
        localReport = JSON.parse(this.responseText);
        tableHTML = "";
        localReport.forEach(function(object) {
            tableHTML += "<li>" + object.name + " : " + object.count + "</li>";
        })
        itemReport.innerHTML = tableHTML;
    })
}

function addResourcesToMap(rid) {
    let numberToAdd, i;
    numberToAdd = document.getElementById("input"+rid).value;
    for ( i = 0; i < numberToAdd;i++) {
        addResourceToMap(rid)
    }
}

/**
 * @summary This method is used to display the all the items that can be added to the map, an input box
 * for each item so that the quantity required can be added as well as a button to add the item to the map.
 *
 * @description All the items that can be added to the map are retrieved from the database and their names are displayed
 * along with a number input box for each one so that the quantity can be entered as well as a button to add it to the
 * map. A table is created and this information is displayed on the table, where there is a column for names, input boxes
 * and buttons.
 */
function addItems() {
    let row, label, numInput, addButton, table;
    table = document.createElement("table"); // creates the table
    table.setAttribute("id", "addItems");
    resources.forEach(function (resourceName, id) {
        row = table.insertRow(-1); // adds a new row
        label = row.insertCell(0);
        numInput = row.insertCell(1);
        addButton = row.insertCell(2);
        label.innerHTML = '<label>' + resourceName + '</label>';
        numInput.innerHTML = '<input id ="input' + id + '" type = "number" min="0" name = "quantity"/>';
        addButton.innerHTML = '<button type="button" class="btn btn-light-green" onclick ="addResourcesToMap(' + id + ')">+</button>';
    });
    document.getElementById("itemlist").appendChild(table);
}

function initMap() {
    let newMap, tiles;
     newMap = L.map('mapid', {
        center: [52.2413, -353.1531],
        zoom: 16,
        keyboard: false
    });
     tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    return newMap;
}

function addFenceToMap() {
    addResourceToMap(23);
}

function massDelete(deleteObjectArray, callback) {
    let array, response;
     array = [];
    deleteObjectArray.forEach(function (object) {
        array.push(object.objectId);
    })
    if (array.length !== 0) {
        deleteObjects(array, mapId, function () {
            if (this.responseText) {
                response = JSON.parse(this.responseText);
                console.log(response);
            }
            deletedObjects.clear();
            callback();
        })
    } else {
        callback();
    }
}

function massUpdate(callback) {
    let array, parsed, putObject, response;
     array = [];
    imgGroup.forEach(function (object) {
         parsed = parseObject(object)
        if (parsed.latLangs !== mapObjects.get(object.objectId).latLangs) {
             putObject = parsed;
            putObject.objectId = object.objectId;
            array.push(putObject);
        }
    })
    if (array.length !== 0) {
        updateObjects(array, mapId, function () {
            if (this.responseText) {
                 response = JSON.parse(this.responseText);
                console.log(response);
            }
            callback();
        })
    } else {
        callback();
    }
}

function massPost(callback) {
    let array, response;
     array = [];
    newObjects.forEach(function (object) {
        array.push(parseObject(object))
    });
    if (array.length !== 0) {
        addObjectsToMap(array, mapId, function () {
            if (this.responseText) {
                 response = JSON.parse(this.responseText);
                console.log(response);
            }
            callback();
        });
    } else {
        callback();
    }
}

function saveState() {
    massDelete(deletedObjects, function () {
        massPost(function () {
            massUpdate(function () {
                bringAllObjectsForMap(mapId);
            });
        });
    });
}

function addResourceToMap(iid) {
    let woof;
    woof = L.distortableImageOverlay(images.get(iid)).addTo(map);
    woof.on('remove', function () {
        const index = newObjects.indexOf(woof);
        if (index !== -1) {
            newObjects.splice(index, 1);
        }
    });
    woof.resourceId = iid;
    newObjects.push(woof);
}

function bringAllResources() {
    let response;
    getAllResources(function () {
         response = JSON.parse(this.responseText);
        response.forEach(function (resource) {
            images.set(resource.resourceId, resource.image);
            resources.set(resource.resourceId, resource.name)
        })
        addItems();
        bringAllObjectsForMap(mapId);
    })
}

function clearMap() {
    let i;
    const lengthImgGroup = imgGroup.length;
    const newObjectsLength = newObjects.length;
    deletedObjects.clear();
    mapObjects.clear();
    for ( i = 0; i < lengthImgGroup; i++) {
        imgGroup[0].editing._overlay.remove();
    }

    for ( i = 0; i < newObjectsLength; i++) {
        newObjects[0].editing._overlay.remove();
    }
    imgGroup = [];
    newObjects = [];
}

function disableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.enable();
    });
}

function enableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.disable();
    });
}

function bringAllObjectsForMap(mapId) {
    let response;
    disableEditingForAll();
    getAllObjectsForMap(mapId, function () {
        response = JSON.parse(this.responseText);
        clearMap();
        deletedObjects.clear();
        mapObjects = new Map();
        response.forEach(function (object) {
            mapObjects.set(object.objectId, object)
        })
        insertObjectsToMap();
        updateLocalReport();
        enableEditingForAll();
    })
}

function parseObject(newObject) {
    return {
        mapId: mapId,
        resourceId: newObject.resourceId,
        latLangs: JSON.stringify(newObject.getCorners())
    };
}

function insertObjectsToMap() {
    mapObjects.forEach(insertObjectIntoMap)
}

function parseLatLangs(stringyLatLangs) {
    let latLangArray, coords;
     latLangArray = JSON.parse(stringyLatLangs);
     coords = [];
    latLangArray.forEach(function (point) {
        coords.push(L.latLng(point.lat, point.lng))
    })
    return coords;
}

function pushToMap(newImage, object) {
    newImage.on('remove', function () {
        deletedObjects.add(object);
        mapObjects.delete(newImage.objectId);
        const index = imgGroup.indexOf(newImage);
        if (index !== -1) {
            imgGroup.splice(index, 1);
        }
    });
    imgGroup.push(newImage);
    newImage.addTo(map);
}

function insertObjectIntoMap(object) {
    let corners, newImage;
     corners = parseLatLangs(object.latLangs);
     newImage = L.distortableImageOverlay(images.get(object.resourceId), {
        corners: corners
    })
    newImage.objectId = object.objectId;
    newImage.resourceId = object.resourceId;
    pushToMap(newImage, object);
}