//Event functions

/**
 * @param {number} eventId - The ID of the event required.
 *
 * @summary This method gets the required event.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL, where the content is in JSON format. The method then returns
 * the value from the back-end if the request was successful.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON object
 * of the required event.
 */
function getEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} event - The event to be updated.
 *
 * @summary This method updates the required event.
 *
 * @description This method takes the JSON object of the event to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the event. This ID is then appended to the URL
 * to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then returns the response from the back-end if the update request was
 * successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function updateEvent(event) {
	let eventId = event.eventId;
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

/**
 * @param {number} eventId - The ID of the event to be deleted.
 *
 * @summary This method deletes the required event.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method then
 * returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function deleteEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/event/" + eventId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @summary This method gets all the events stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL,
 * where the content is in JSON format. The method then returns the value from the back-end
 * if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be an
 * array of all the events. Each event will be a JSON object.
 */
function getAllEvents() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} event - The event to be added.
 *
 * @summary This method adds the event to the database.
 *
 * @description This method takes the JSON object of the event to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then returns the response
 * from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function addEvent(event) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

/**
 * @summary This method deletes all the events stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then returns the value from the back-end if the
 * deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function deleteAllEvents() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Map functions
/**
 * @param {number} mapId - The ID of the map required.
 *
 * @summary This method gets the required map.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL, where the content is in JSON format. The method then returns
 * the value from the back-end if the request was successful.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON object
 * of the required map.
 */
function getMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} map - The map to be updated.
 *
 * @summary This method updates the required map.
 *
 * @description This method takes the JSON object of the map to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the map. This ID is then appended to the URL
 * to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then returns the response from the back-end if the update request was
 * successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function updateMap(map) {
	mapId = map.mapId;
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(map));
}

/**
 * @param {number} mapId - The ID of the map to be deleted.
 *
 * @summary This method deletes the required map.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method then
 * returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function deleteMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
	
}

function getAllMaps() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addMap(map) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function deleteAllMaps() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
	
}

//Eventmap functions
function getAllMapsForEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/eventMap/event/" + eventId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function getAllEventsForMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/eventMap/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addEventMap(eventMap) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/eventMap", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(eventMap));
}

function clearEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/eventMap/" + eventId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function deleteEventMap(eventId, mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/eventMap/" + eventId + "/" + mapId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function deleteAllRelations() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/eventMap", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for objects
function getObject(objectId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function updateObject(mapObject) {
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/object", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(mapObject));
}

function deleteObject(objectId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function getAllObjectsForMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/objects/" + mapId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function generateReportForMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/objects/" + mapId + "/report", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addObjectToMap(mapObject) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/objects", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(mapObject));
}

function clearMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/objects/" + mapId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function clearAllMaps() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/objects", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for drawings
function getDrawing(drawingId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/drawing/" + drawingId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function updateDrawing(drawing) {
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/drawing", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(drawing));
}

function deleteDrawing(drawingId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/drawing/" + drawingId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function getAllDrawings() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/drawings", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addDrawing(drawing) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/drawings", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(drawing));
}

//Functions for materials
function getMaterialDrawing(materialId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/material/" + materialId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function updateMaterial(material) {
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/material", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(material));
}

function deleteMaterial(materialId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/material/" + materialId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function getAllMaterials() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/materials", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addMaterial(material) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/materials", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(material));
}

//Functions for resources
function deleteAllResources() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/resources", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function deleteResource(resourceId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/resources/" + resourceId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}