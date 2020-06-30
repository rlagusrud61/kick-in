let localReport, map, lengthMeasurer, mapId, resources, images, mapObjects, measurementObjects,
    imgGroup, newObjects, deletedObjects, zoomControl, mapName, actions, options, zindex;

localReport = new Map();
actions = [L.DragAction, L.ScaleAction, L.RotateAction, L.DeleteAction];
options = {
    position: 'topleft',            // Position to show the control. Values: 'topright', 'topleft', 'bottomright', 'bottomleft'
    unit: 'metres',                 // Show imperial or metric distances. Values: 'metres', 'landmiles', 'nauticalmiles'
    clearMeasurementsOnStop: true,  // Clear all the measurements when the control is unselected
    showBearings: false,            // Whether bearings are displayed within the tooltips
    bearingTextIn: 'In',             // language dependend label for inbound bearings
    bearingTextOut: 'Out',          // language dependend label for outbound bearings
    tooltipTextFinish: 'Click to <b>finish line</b><br>',
    tooltipTextDelete: 'Press SHIFT-key and click to <b>delete point</b>',
    tooltipTextMove: 'Click and drag to <b>move point</b><br>',
    tooltipTextResume: '<br>Press CTRL-key and click to <b>resume line</b>',
    tooltipTextAdd: 'Press CTRL-key and click to <b>add point</b>',
    // language dependend labels for point's tooltips
    measureControlTitleOn: 'Turn on PolylineMeasure',   // Title for the control going to be switched on
    measureControlTitleOff: 'Turn off PolylineMeasure', // Title for the control going to be switched off
    measureControlLabel: '&#8614;', // Label of the Measure control (maybe a unicode symbol)
    measureControlClasses: [],      // Classes to apply to the Measure control
    showClearControl: false,        // Show a control to clear all the measurements
    clearControlTitle: 'Clear Measurements', // Title text to show on the clear measurements control button
    clearControlLabel: '&times',    // Label of the Clear control (maybe a unicode symbol)
    clearControlClasses: [],        // Classes to apply to clear control button
    showUnitControl: false,         // Show a control to change the units of measurements
    distanceShowSameUnit: false,    // Keep same unit in tooltips in case of distance less then 1 km/mi/nm
    unitControlTitle: {             // Title texts to show on the Unit Control button
        text: 'Change Units',
        metres: 'metres',
        landmiles: 'land miles',
        nauticalmiles: 'nautical miles'
    },
    unitControlLabel: {             // Unit symbols to show in the Unit Control button and measurement labels
        metres: 'm',
        kilometres: 'km',
        feet: 'ft',
        landmiles: 'mi',
        nauticalmiles: 'nm'
    },
    tempLine: {                     // Styling settings for the temporary dashed line
        color: '#00f',              // Dashed line color
        weight: 2                   // Dashed line weight
    },
    fixedLine: {                    // Styling for the solid line
        color: '#006',              // Solid line color
        weight: 2                   // Solid line weight
    },
    startCircle: {                  // Style settings for circle marker indicating the starting point of the polyline
        color: '#000',              // Color of the border of the circle
        weight: 1,                  // Weight of the circle
        fillColor: '#0f0',          // Fill color of the circle
        fillOpacity: 1,             // Fill opacity of the circle
        radius: 3                   // Radius of the circle
    },
    intermedCircle: {               // Style settings for all circle markers between startCircle and endCircle
        color: '#000',              // Color of the border of the circle
        weight: 1,                  // Weight of the circle
        fillColor: '#ff0',          // Fill color of the circle
        fillOpacity: 1,             // Fill opacity of the circle
        radius: 3                   // Radius of the circle
    },
    currentCircle: {                // Style settings for circle marker indicating the latest point of the polyline during drawing a line
        color: '#000',              // Color of the border of the circle
        weight: 1,                  // Weight of the circle
        fillColor: '#f0f',          // Fill color of the circle
        fillOpacity: 1,             // Fill opacity of the circle
        radius: 3                   // Radius of the circle
    },
    endCircle: {                    // Style settings for circle marker indicating the last point of the polyline
        color: '#000',              // Color of the border of the circle
        weight: 1,                  // Weight of the circle
        fillColor: '#f00',          // Fill color of the circle
        fillOpacity: 1,             // Fill opacity of the circle
        radius: 3                   // Radius of the circle
    },
};
zindex = 250;
map = initMap();
zoomControl = map.zoomControl;
mapId = window.location.search.split("=")[1];
mapName = mapId;
//all available resources
resources = new Map();
//their images
images = new Map();
//stuff on the map
mapObjects = new Map();
measurementObjects = new Map();
//the image group for the library
imgGroup = [];
//new objects added to the map.
newObjects = [];
deletedObjects = new Set();

bringAllResources();

/**
 * @param {String} id - the ID of the HTML element to open up.
 *
 * @summary This method opens the accordion element in the 'mapEdit.html' page, for the HTML element whose ID is given.
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
 * @summary This method updates the report that is shown on the 'mapEdit.html' page.
 */
function updateLocalReport() {
    let itemReport, tableHTML;
    itemReport = document.getElementById("displayitems");
    generateReportForMap(mapId, function () {
        localReport = JSON.parse(this.responseText);
        tableHTML = "";
        localReport.forEach(function (object) {
            tableHTML += "<li>" + object.name + " : " + object.count + "</li>";
        })
        itemReport.innerHTML = tableHTML;
    })
}

/**
 * @param {number} rid - The ID of the resource to be placed in the map.
 *
 * @summary When the '+' button next to the resources available is clicked, this method is called to add the resource to
 * the map. This is different from pre-saved objects that are retrieved from the database.
 */
function addResourcesToMap(rid) {
    let numberToAdd, i;
    numberToAdd = document.getElementById("input" + rid).value;
    for (i = 0; i < numberToAdd; i++) {
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

/**
 * @summary This is the initializing function for the map on the 'mapView.html' page and it uses the leaflet
 * library.
 *
 * @returns {L.map} newMap - This is an instance of a new leaflet map.
 */
function initMap() {
    let newMap, tiles;
    newMap = L.map('mapid', {
        center: [52.2413, -353.1531],
        zoomSnap: 0,
        zoom: 16,
        maxZoom: 19,
        maxNativeZoom: 18,
        keyboard: false
    });
    tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    lengthMeasurer = L.control.polylineMeasure(options);
    lengthMeasurer.addTo(newMap);
    newMap.on('click', function () {
        measurementObjects.forEach(function (measureTool) {
            newMap.removeLayer(measureTool);
        })
    })
    return newMap;
}

/**
 * @summary This method adds a fence to the map.
 */
function addFenceToMap() {
    addResourceToMap(23);
}

/**
 * @param {number | Array} deleteObjectArray - The IDs of the objects to be deleted from the map.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to execute the next function in line.
 *
 * @summary This method deletes the objects from the database, that were deleted from the map, by sending an array of
 * the object IDs as stored on the database. Once completed, the 'callback' function is called.
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
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to execute the next function in line.
 *
 * @summary This method updates the information on the map objects in a map in the database.
 *
 * @description This method updates on the database all the objects that were updated on the map, by sending an array of the
 * objects as they are now on the map along with their IDs. The function checks if a change is made by comparing their
 * current latLangs to the ones which were received when the all the objects were brought for the map. Once completed, the
 * 'callback' function is called.
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
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to execute the next function in line.
 *
 * @summary This method is used to add the map objects that were added to the map to the database.
 *
 * @description This method sends the objects that were newly added to the map to the database, after parsing them
 * into the correct format, i.e. the format expected by the back-end. Once it receives the expected response, it calls
 * the 'callback' function.
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
 * @summary This method saves the current state of the map.
 *
 * @description This function saves the current state of the map by deleting all the objects from the database that were deleted,
 * adding all the objects to the database that were newly added, updating the position of all the objects in the database
 * that were moved around and then finally in the chain of callbacks, it calls the 'bringAllObjectsForMap' function to
 * get the updates in case someone else also edits the map.
 */
function saveState() {
    disableEditingForAll();
    massDelete(deletedObjects, function () {
        massPost(function () {
            massUpdate(function () {
                postNewImage(function () {
                    bringAllObjectsForMap(mapId);
                    alert("Your Map has been saved!");
                });
            });
        });
    });
}

function postNewImage(callback) {
    map.removeControl(lengthMeasurer);
    measurementObjects.forEach(function (measurer) {
        measurer.hideMeasurements();
    })
    screenshot(function (image) {
        updateMapImage({mapId: mapId, image: image}, function () {
            if (this.responseText) {
                response = JSON.parse(this.responseText);
                console.log(response);
                map.addControl(lengthMeasurer);
                callback();
            } else {
                map.addControl(lengthMeasurer);
                callback();
            }
        })
    });
}

/**
 * @param {number} rid - The ID of the resource to be added to the map.
 *
 * @summary It adds the required resource to the map.
 *
 * @description This method takes the ID of the resource that was chosen to be added to the map using the '+' button
 * on the 'mapEdit.html' page. After that, it produces an instance of the 'distortableImageOverlay' library of leaflet
 * being used to show images on screen and then adds it to the map. It attaches a listener to the images, so that on
 * having the image removed, we know it needs to be deleted from the 'newObjects' array. This is important since these
 * objects may not have been saved in the database yet, hence do not exist in the mapObjects map.
 */
function addResourceToMap(rid) {
    let newResourceImage;
    newResourceImage = L.distortableImageOverlay(images.get(rid), {
        actions: actions,
    }).addTo(map);
    newResourceImage.on('remove', function () {
        const index = newObjects.indexOf(newResourceImage);
        if (index !== -1) {
            newObjects.splice(index, 1);
        }
    });
    newResourceImage.resourceId = rid;
    newObjects.push(newResourceImage);
}

/**
 * @summary This method retrieves all the resources stored in the database.
 *
 * @description This method brings all the resources that are currently in offer in the database to be added on to the map.
 * It creates a map for the images and the resource names so that they can be called independently as required by different
 * functions, like displaying the list of resources and adding a new image on the map.
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
 * @summary This method removes all the objects from the map, but not from the database.
 *
 * @description When all the objects are brought back for the map after saving, the current objects being displayed
 * need to be cleared so that these are not duplicated. imgGroup[0] and newObjects[0] being hardcoded at 0 is not an error,
 * it is a necessity for how the 'remove' function works. The method finishes with creating a new instance of the objects
 * to be filled again.
 */
function clearMap() {
    let i;
    const lengthImgGroup = imgGroup.length;
    const newObjectsLength = newObjects.length;
    deletedObjects.clear();
    mapObjects.clear();
    for (i = 0; i < lengthImgGroup; i++) {
        imgGroup[0].editing._overlay.remove();
    }

    for (i = 0; i < newObjectsLength; i++) {
        newObjects[0].editing._overlay.remove();
    }
    imgGroup = [];
    newObjects = [];
}

/**
 * @summary This method disables the ability to move around the objects on the map.
 */
function disableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.enable();
    });
}

/**
 * @summary This method enables the ability to move around the objects on the map.
 */
function enableEditingForAll() {
    imgGroup.forEach(function (img) {
        img.editing.disable();
    });
}

/**
 * @param {number} mapId - The ID of the map for which objects are to be displayed.
 *
 * @summary This method loads all the objects stored for the map as stored on the database and displays them onto the map.
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
        addMeasurements();
        enableEditingForAll();
    })
}

/**
 * @param {json | Object} newObject - The JSON object of the map object that is to be parsed.
 *
 * @summary This method creates the instance of the object as required by the database.
 *
 * @returns {{resourceId: (number|Object.resourceId), mapId: number, latLangs: string}} - The JSON object in a format
 * required by the database.
 */
function parseObject(newObject) {
    return {
        mapId: mapId,
        resourceId: newObject.resourceId,
        latLangs: newObject.getCorners()
    };
}

/**
 * @summary This method iterates through the all the map objects. These are the objects as loaded from the database and
 * these are inserted on to the map.
 */
function insertObjectsToMap() {
    mapObjects.forEach(insertObjectIntoMap)
}
/**
 * @param {json | Object} newImage - the new image to be added.
 *
 * @param {json | Object} object - the object we put inside deletedObjects when sending the 'delete' request.
 *
 * @summary It adds the event listeners to the images and objects being added to the map.
 *
 * @description This method adds the proper listeners to the images of the map objects since they are handled differently
 * to how the images added in the current session (before saving) are handled. This deletes the images from the mapObjects
 * map, such that if this image was moved around, it is not sent for the put request, and adds it to the deletedObjects
 * set so that this can be deleted if deleted by the user on the map. This uses the listening on the 'remove' event fired
 * by leaflet whenever the user deletes an image and image overlay is removed.
 */
function pushToMap(newImage, object) {
    newImage.on('update', function () {
        measurementObjects.forEach(function (measurer) {
            if (measurer.objectId !== newImage.objectId) {
                map.removeLayer(measurer);
            } else {
                map.addLayer(measurer);
                measurer.showMeasurements({showArea: false});
                measurer.setLatLngs([newImage.getCorner(0), newImage.getCorner(1), newImage.getCorner(3), newImage.getCorner(2)]);
            }
        })
    });
    newImage.on('remove', function () {
        measurementObjects.get(newImage.objectId).remove();
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
 * @param {json | Object} object - The object to be added to the map.
 *
 * @summary This method insert the object on to the map.
 */
function insertObjectIntoMap(object) {
    let image, corners;
    corners = JSON.parse(object.latLangs);
    image = L.distortableImageOverlay(images.get(object.resourceId), {
        actions: actions,
        corners: corners
    })
    image.objectId = object.objectId;
    image.resourceId = object.resourceId;
    image.setZIndex(zindex);
    image.edgeMinWidth = 10;
    pushToMap(image, object)
    zindex++;
}

function addMeasurements() {
    imgGroup.forEach(function (image) {
        let newMeasurement = L.polygon([image.getCorner(0), image.getCorner(1), image.getCorner(3), image.getCorner(2)],
            {fill: false, weight: 2, color: '#000'})
            .addTo(map)
            .hideMeasurements();
        newMeasurement.objectId = image.objectId;
        measurementObjects.set(image.objectId, newMeasurement);
        map.removeLayer(newMeasurement);
    })
}