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

/**
 * @summary opens the accordion element in the html, for the id if the html element supplied.
 *
 * @param {number} id the id of the html element to open up.
 */
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

/**
 * @summary updates the report that is shown on the mapEdit.js page.
 */
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

/**
 * @summary when the + button next to the resources available is clicked,
 * this method is called to add the resource to the map, this is different pre-saved objects.
 *
 * @param {number} rid
 */
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
        numInput.innerHTML = '<input id ="input' + id + '" type = "number" name = "quantity"/>';
        addButton.innerHTML = '<button onclick ="addResourcesToMap(' + id + ')" style="background-color: #58BD0F; ' +
            'border-color: #C1FF94;">+</button>';
    })
    document.getElementById("itemlist").appendChild(table);
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
        keyboard: false
    });
     tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    return newMap;
}

/**
 * This method adds a fence to the map. This is test function, and needs to be removed.
 *
 * @deprecated
 */
function addFenceToMap() {
    addResourceToMap(23);
}

/**
 * @summary This method executes the deleting all the objects that were deleted form the map,
 * from the database, by sending an array of the object ids as stored on the database.
 * Once completed the callback function is called.
 *
 * @param {[]} deleteObjectArray
 *
 * @param {function} callback
 */
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

/**
 * @summary This method executes the updating of all the objects that were updated on the map,
 * from the database, by sending an array of the objects as now on the map along with their objectIds.
 * The function checks if a change is amde by comparing their current latLangs to the ones which were received
 * when the all the objects were brought for the map.
 * Once completed the callback function is called.
 *
 * @param {function} callback
 */
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

/**
 * @summary This sends the objects that were newly added to the map to the database, after parsing them
 * into the correct format (the format expected by the backend) and then once it receives the expected
 * response it calls the callback function.
 *
 * @param {function} callback
 */
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

/**
 * @summary this function saves the current state of the map, by deleting all the objects that
 * were deleted, posting all the objects that were newly added, and updating the position of all
 * the objects that were moved around, and then finally in the chain of callbacks,
 * it calls the bringAllObjectsForMap function to get the updates in case someone edits the map too.
 */
function saveState() {
    massDelete(deletedObjects, function () {
        massPost(function () {
            massUpdate(function () {
                bringAllObjectsForMap(mapId);
            });
        });
    });
}

/**
 * @summary it takes the id of the resource that was chosen to be added to the map using the + button
 * on the html page and following that it produces an instance of the distortableImageOverlay
 * the library of leaflet being used to show images on screen and then adds it to the map.
 * It attaches a listener to the images, so that on having the image removed we know it needs to be deleted form
 * the newObjects array, this is important since these objects may not have been saved in the database yet,
 * hence do not exist in the MapObjects().
 *
 * @param {number} iid
 */
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
        addItems();
        bringAllObjectsForMap(mapId);
    })
}

/**
 * @summary when all the objects are brought back for the map after saving,
 * the current objects being displayed needs to be cleared so that these are not duplicated.
 * imgGroup[0], newObjects[0] being hardcoded at 0 is not an error, it is a necessity for how
 * the remove() function works. The method finishes with creating a new instance of the objects to be filled again.
 */
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

/**
 * @summary Disables the ability to move around the objects on the map.
 */
function disableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.enable();
    });
}

/**
 * @summary Enables the ability to move around the objects on the map.
 */
function enableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.disable();
    });
}

/**
 * @summary loads all the objects stored for the map as stored on the database, and displays them onto the map.
 *
 * @param {number} mapId
 */
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

/**
 * @summary creates the instance of the object as required by the database.
 *
 * @param {Object} newObject
 * @returns {{resourceId: number, mapId: number, latLangs: string}}
 */
function parseObject(newObject) {
    return {
        mapId: mapId,
        resourceId: newObject.resourceId,
        latLangs: JSON.stringify(newObject.getCorners())
    };
}

/**
 * @summary It iterates through the all the mapObjects, theses are the objects as loaded from the database,
 * and inserts them into the map.
 */
function insertObjectsToMap() {
    mapObjects.forEach(insertObjectIntoMap)
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

/**
 * @summary adds the proper listeners to MapObjects images since they are handled differently to how the
 * images added in the current session (before saving) are handled. THis deletes the images from the mapObjects,
 * such that if this image was moved arounf, it is not sent for the put request, and adds it to the deletedoBjects
 * set so that this can be deleted if deleted. This uses the listening on 'remove' event fired by leaflet whenever
 * and imageoverlay is removed.
 *
 * @param {Object} newImage the newImage to be added.
 *
 * @param {Object} object the object we out inside deletedObject when sending deleteReq.
 */
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

/**
 * @summary Insert the object on to the map.
 *
 * @param {Object} object the to add.
 */
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