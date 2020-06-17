//Event functions

/**
 * @param {number} eventId - The ID of the event required from the database.
 *
 * @summary This method gets the required event from the database.
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
 * @summary This method updates the required event in the database.
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
	let eventId, xhr;
	eventId= event.eventId;
	xhr = new XMLHttpRequest();
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
 * @param {number} eventId - The ID of the event to be deleted from the database.
 *
 * @summary This method deletes the required event from the database.
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
 * array of events. Each event will be a JSON object.
 */
function getAllEvents(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return(xhr.responseText)
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();	
}
a = getAllEvents()
/**
 * @param {json | Object} event - The event to be added to the database.
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
function addEvent(event, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 201)) {
			console.log(xhr.responseText);
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
function deleteAllEvents(callback) {
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
 * @param {number} mapId - The ID of the map required from the database.
 *
 * @summary This method gets the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL, where the content is in JSON format. The method then returns
 * the value from the back-end if the request was successful.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON object
 * of the required map.
 */
function getMap(mapId, callback) {
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
 * @summary This method updates the required map in the database.
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
function updateMap(map, callback) {
	let xhr, mapId;
	mapId = map.mapId;
	xhr = new XMLHttpRequest();
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
 * @param {number} mapId - The ID of the map to be deleted from the database.
 *
 * @summary This method deletes the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method then
 * returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function deleteMap(mapId, callback) {
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

/**
 * @summary This method gets all the maps stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL,
 * where the content is in JSON format. The method then returns the value from the back-end
 * if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be an
 * array of maps. Each map will be a JSON object.
 */
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

/**
 * @param {json | Object} event - The data on the map to be added to the database.
 *
 * @summary This method adds the map data to the database.
 *
 * @description This method takes the JSON object of the map data to be added as a parameter.
 * A POST request is sent to the RESTful service provider with the given URL, where the content of
 * the body is the JSON object that was taken as the parameter. The method then returns the
 * response from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method deletes all the map data stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then returns the value from the back-end if the
 * deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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
/**
 * @param {number} eventId - The ID of the event for which maps are required.
 *
 * @summary This method gets all the maps stored in the database for a specific event.
 *
 * @description  This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be
 * an array of the map data for the specific event. Each map data will be a JSON object.
 */
function getAllMapsForEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/eventMap/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} mapId - The ID of the map for which the associated events are required.
 *
 * @summary This method gets all the maps stored in the database for a specific event.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful service provider
 * with the given URL, where the content is in JSON format. The method then returns the value from the back-end
 * if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be
 * an array of events associated with the specific map. Each event will be a JSON object.
 */
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

/**
 * @param eventMap {json | Object} - The IDs of the map and event to be linked.
 *
 * @summary This method associates a map with an event (or an event with a map) to fulfill the many
 * to many relationship between maps and events.
 *
 * @description This method takes the JSON object consisting of the IDs of a map and an event to be
 * linked as a parameter. A POST request is sent to the RESTful service provider with the given URL,
 * where the content of the body is the JSON object that was taken as the parameter. The method then
 * returns the response from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} eventId - The ID of the event which has the maps to be deleted from the database.
 *
 * @summary This method deletes all the maps for the required event from the database.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the deletion of the maps linked to it on the back-end. A DELETE
 * request is sent to the RESTful service provider with the given URL, where the content is in JSON
 * format. The method then returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function clearMapsForEvent(eventId) {
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

/**
 * @param {number} eventId - the ID of the event.
 * @param {number} mapId - the ID of the map.
 *
 * @summary This method deletes the link between a map and an event from the database.
 *
 * @description This method takes the IDs of the required event and map as a parameter. These IDs
 * are then appended to the URL to request the deletion of link between the map and event on the
 * back-end. A DELETE request is sent to the RESTful service provider with the given URL, where the
 * content is in JSON format. The method then returns the value from the back-end if the deletion
 * was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method deletes all the links between maps and events from the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL to delete
 * all the associations between maps and events, where the content is in JSON format. The method
 * then returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} objectId - The ID of the map object required from the database.
 *
 * @summary This method gets the required map object from the database.
 *
 * @description This method takes the ID of the required map object as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the request was successful. A map object is
 * basically an item that has already been placed on the map.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON
 * object of the required map object.
 */
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

/**
 * @param {json | Object} mapObject - The map object to be updated.
 *
 * @summary This method updates the required map object in the database.
 *
 * @description This method takes the JSON object of the map object to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the map object. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then returns the response from the back-end if the update request was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function updateObject(mapObject) {
	let xhr, objectId;
	xhr = new XMLHttpRequest();
	objectId = mapObject.objectId;
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(mapObject));
}

/**
 * @param {number} objectId - The ID of the object to be deleted from the database.
 *
 * @summary This method deletes the required object from the database.
 *
 * @description This method takes the ID of the required object as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} mapId - The ID of the map for which the associated objects are required.
 *
 * @summary This method gets all the objects stored in the database for a specific map.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be
 * an array of objects associated with the specific map. Each object will be a JSON object.
 */
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

/**
 * @param {number} mapId - The ID of the map for which the report is required.
 *
 * @summary This method gets all the map objects and their respective quantities for a specific map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be
 * an array of JSON objects, where each object consists of the name of the map object and its
 * quantity in the map. All the map objects are associated with the specific map.
 */
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

/**
 * @param {json | Object} mapObject - The map object to be added to the database.
 *
 * @summary This method adds the map object to the database.
 *
 * @description This method takes the JSON object of the map object to be added as a parameter.
 * A POST request is sent to the RESTful service provider with the given URL, where the content of
 * the body is the JSON object that was taken as the parameter. The method then returns the
 * response from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} mapId - The ID of the map which has the map objects to be deleted.
 *
 * @summary This method deletes all the map objects from the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the deletion of the map objects placed on that map on the
 * back-end. A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then returns the value from the back-end if the
 * deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method deletes all the map objects from all the maps in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then returns the value from the back-end if the
 * deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} drawingId - The ID of the drawing required from the database.
 *
 * @summary This method gets the required drawing from the database.
 *
 * @description This method takes the ID of the required drawing as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the request was successful.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON
 * object of the required drawing.
 */
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

/**
 * @param {json | Object} drawing - The drawing to be updated in the database.
 *
 * @summary This method updates the required drawing in the database.
 *
 * @description This method takes the JSON object of the drawing to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the drawing. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then returns the response from the back-end if the update request was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function updateDrawing(drawing) {
	let xhr, drawingId;
	xhr = new XMLHttpRequest();
	drawingId = drawing.drawingId;
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/drawing/" + drawingId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(drawing));
}

/**
 * @param {number} drawingId - The ID of the drawing to be deleted from the database.
 *
 * @summary This method deletes the required drawing from the database.
 *
 * @description This method takes the ID of the required drawing as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method gets all the drawings stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL,
 * where the content is in JSON format. The method then returns the value from the back-end
 * if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be an
 * array of drawings. Each drawing will be a JSON object.
 */
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

/**
 * @param {json | Object} event - The drawing to be added to the database.
 *
 * @summary This method adds the drawing to the database.
 *
 * @description This method takes the JSON object of the drawing to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then returns the response
 * from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} materialId - The ID of the material required from the database.
 *
 * @summary This method gets the required material from the database.
 *
 * @description This method takes the ID of the required material as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL, where the content is in JSON format. The method then returns
 * the value from the back-end if the request was successful.
 *
 * @returns {json | Object} - The response from the RESTful service provider which will be a JSON object
 * of the required material.
 */
function getMaterial(materialId) {
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

/**
 * @param {json | Object} mapObject - The material to be updated in the database.
 *
 * @summary This method updates the required material in the database.
 *
 * @description This method takes the JSON object of the material to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the material. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then returns the response from the back-end if the update request was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
function updateMaterial(material) {
	let xhr, materialId;
	xhr = new XMLHttpRequest();
	materialId = material.materialId;
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/material/" + materialId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState === 4) && (xhr.status === 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(material));
}

/**
 * @param {number} materialId - The ID of the material to be deleted from the database.
 *
 * @summary This method deletes the required material from the database.
 *
 * @description This method takes the ID of the required material as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method
 * then returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method gets all the materials stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL,
 * where the content is in JSON format. The method then returns the value from the back-end
 * if the request was successful.
 *
 * @returns {json | Object | Array} - The response from the RESTful service provider which will be an
 * array of materials. Each material will be a JSON object.
 */
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

/**
 * @param {json | Object} material - The material to be added to the database.
 *
 * @summary This method adds the material to the database.
 *
 * @description This method takes the JSON object of the material to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then returns the response
 * from the back-end if the addition was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @summary This method deletes all the resources stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then returns the value from the back-end if the
 * deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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

/**
 * @param {number} resourceId - The ID of the resource to be deleted.
 *
 * @summary This method deletes the required resource from the database.
 *
 * @description This method takes the ID of the required resource as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL, where the content is in JSON format. The method then
 * returns the value from the back-end if the deletion was successful.
 *
 * @returns {string} - The response from the RESTful service provider.
 */
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