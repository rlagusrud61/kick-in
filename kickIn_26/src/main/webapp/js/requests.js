const baseUrlPrevider = "http://env-di-team26.paas.hosted-by-previder.com/kickInTeam26";

/**
 * @param {XMLHttpRequest} xhr - the XmlHTTPRequest response.
 *
 * @param {function} callback - the function that needs to be called if successful.
 *
 * @summary The function that handles all HTML status codes.
 */
function handleResponse(xhr, callback) {
	switch (xhr.status) {
		case 200:
		case 201:
		case 204:
			callback.apply(xhr);
			break;
		default:
			const errorMessage = JSON.parse(xhr.responseText);
			alert('errorCode: ' + errorMessage.errorCode + '\n' +
				  'errorMessage: ' + errorMessage.errorMessage
			);
	}
}

var baseUrl = "http://env-di-team26.paas.hosted-by-previder.com/kickInTeam26";

//Event functions

/**
 * @param {number} eventId - The ID of the event required from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets the required event from the database.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL. The method then calls the callback function on 'xhr' if the
 * request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getEvent(eventId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} event - The event to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required event in the database.
 *
 * @description This method takes the JSON object of the event to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the event. This ID is then appended to the URL
 * to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was
 * successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateEvent(event, callback) {
	let eventId, xhr;
	eventId= event.eventId;
	xhr = new XMLHttpRequest();
	xhr.open('PUT', baseUrl + "/rest/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

/**
 * @param {number} eventId - The ID of the event to be deleted from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required event from the database.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL. The method then
 * calls the callback function on 'xhr' if the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteEvent(eventId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/event/" + eventId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the events stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllEvents(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/events" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();	
}

/**
 * @param {json | Object} event - The event to be added to the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the event to the database.
 *
 * @description This method takes the JSON object of the event to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then calls the callback function
 * on 'xhr' if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addEvent(event, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/events" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the events stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteAllEvents(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/events" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback)
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Map functions

/**
 * @param {number} mapId - The ID of the map required from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} map - The map to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required map in the database.
 *
 * @description This method takes the JSON object of the map to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the map. This ID is then appended to the URL
 * to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was
 * successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateMap(map, callback) {
	let xhr, mapId;
	mapId = map.mapId;
	xhr = new XMLHttpRequest();
	xhr.open('PUT', baseUrl + "/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(map));
}

function updateMapImage(map, callback) {
	let xhr, mapId;
	mapId = map.mapId;
	xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://env-di-team26.paas.hosted-by-previder.com/kickInTeam26/rest/map/image/" + mapId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(map));
}

/**
 * @param {number} mapId - The ID of the map to be deleted from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback function on 'xhr' if the deletion
 * was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the maps stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllMaps(callback) {

	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} map - The data on the map to be added to the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the map data to the database.
 *
 * @description This method takes the JSON object of the map data to be added as a parameter.
 * A POST request is sent to the RESTful service provider with the given URL, where the content of
 * the body is the JSON object that was taken as the parameter. The method then calls the
 * callback function on 'xhr' if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addMap(map, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(map));
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the map data stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteAllMaps(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/maps" , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback)
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
	
}

//Eventmap functions
/**
 * @param {number} eventId - The ID of the event for which maps are required.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the maps stored in the database for a specific event.
 *
 * @description  This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllMapsForEvent(eventId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/eventMap/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} mapId - The ID of the map for which the associated events are required.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the maps stored in the database for a specific event.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful service provider
 * with the given URL. The method then calls the callback function on 'xhr'
 * if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllEventsForMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/eventMap/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param eventMap {json | Object} - The IDs of the map and event to be linked.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method associates a map with an event (or an event with a map) to fulfill the many
 * to many relationship between maps and events.
 *
 * @description This method takes the JSON object consisting of the IDs of a map and an event to be
 * linked as a parameter. A POST request is sent to the RESTful service provider with the given URL,
 * where the content of the body is the JSON object that was taken as the parameter. The method then calls the callback
 * function on 'xhr' if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addEventMap(eventMap, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/eventMap", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(eventMap));
}

/**
 * @param {number} eventId - The ID of the event which has the maps to be deleted from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the maps for the required event from the database.
 *
 * @description This method takes the ID of the required event as a parameter. This ID is then
 * appended to the URL to request the deletion of the maps linked to it on the back-end. A DELETE
 * request is sent to the RESTful service provider with the given URL. The method then calls the callback function on
 * 'xhr' if the deletion was successful.
 */
function clearMapsForEvent(eventId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/eventMap/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 204) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} eventId - the ID of the event.
 *
 * @param {number} mapId - the ID of the map.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the link between a map and an event from the database.
 *
 * @description This method takes the IDs of the required event and map as a parameter. These IDs
 * are then appended to the URL to request the deletion of link between the map and event on the
 * back-end. A DELETE request is sent to the RESTful service provider with the given URL, where the
 * content is in JSON format. The method then calls the callback function on 'xhr' if the deletion
 * was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteEventMap(eventId, mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/eventMap/" + eventId + "/" + mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the links between maps and events from the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL to delete
 * all the associations between maps and events. The method then calls the
 * callback function on 'xhr' if the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteAllRelations(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/eventMap", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback)
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for objects

/**
 * @param {number} objectId - The ID of the map object required from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets the required map object from the database.
 *
 * @description This method takes the ID of the required map object as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback function on 'xhr' if the request was
 * successful. A map object is basically an item that has already been
 * placed on the map, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getObject(objectId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} mapObject - The map object to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required map object in the database.
 *
 * @description This method takes the JSON object of the map object to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the map object. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateObject(mapObject, callback) {
	let xhr, objectId;
	xhr = new XMLHttpRequest();
	objectId = mapObject.objectId;
	xhr.open('PUT', baseUrl + "/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(mapObject));
}

/**
 * @param {number} objectId - The ID of the object to be deleted from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required object from the database.
 *
 * @description This method takes the ID of the required object as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteObject(objectId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl  + "/rest/object/" + objectId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * map.
 *
 * @param deleteObjectArray the objectIds of the objects to be deleted.
 * @param {number} mapId - The ID of the map for which map objects are to be deleted.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required objects from the database for a specific map.
 *
 * @description This method takes an array of the IDs of the required objects and the ID of the map which contains the
 * objects as parameters. This ID is then appended to the URL to request the deletion on the back-end. A DELETE request
 * is sent to the RESTful service provider with the given URL. The method then calls the callback function on 'xhr' if
 * the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteObjects(deleteObjectArray, mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/objects/selected/"+mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(deleteObjectArray));
}

/**
 * @param {json | Object | Array} putObjectArray - The map objects to be updated for a specific map.
 *
 * @param {number} mapId - The ID of the map for which the map objects are to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required map objects for a specific map in the database.
 *
 * @description This method takes an array of JSON objects of the map objects and ID of the map in which changes are to
 * be made as parameters. This ID is then appended to the URL to request the update on the back-end. A PUT request is
 * sent to the RESTful service provider with the given URL, where the content of the body is the array of JSON objects
 * that was taken as a parameter. The method then calls the callback function on 'xhr' if the update request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateObjects(putObjectArray, mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', baseUrl + "/rest/objects/selected/"+mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(putObjectArray));
}

/**
 * @param {number} mapId - The ID of the map for which the associated objects are required.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the objects stored in the database for a specific map.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllObjectsForMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/objects/" + mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} mapId - The ID of the map for which the report is required.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the map objects and their respective quantities for a specific map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function generateReportForMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/objects/" + mapId + "/report", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object | Array} mapObjectsArray - The map objects to be added to the database for a specific map.
 *
 * @param {number} mapId - The ID of the map to which objects are to be added.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the map objects to the database for a specific map.
 *
 * @description This method takes the array of JSON objects of the map objects to be added as a parameter.
 * A POST request is sent to the RESTful service provider with the given URL, where the content of
 * the body is the array of JSON objects that was taken as a parameter. The method then calls the callback function on 'xhr'
 * if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addObjectsToMap(mapObjectsArray, mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/objects/"+mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(mapObjectsArray));
}

/**
 * @param {number} mapId - The ID of the map which has the map objects to be deleted.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the map objects from the required map from the database.
 *
 * @description This method takes the ID of the required map as a parameter. This ID is then
 * appended to the URL to request the deletion of the map objects placed on that map on the
 * back-end. A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function clearMap(mapId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/objects/" + mapId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the map objects from all the maps in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function clearAllMaps(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/objects", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback)
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for drawings

/**
 * @param {json | Object} drawing - The drawing to be updated in the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required drawing in the database.
 *
 * @description This method takes the JSON object of the drawing to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the drawing. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateDrawing(drawing, callback) {
	let xhr, drawingId;
	xhr = new XMLHttpRequest();
	drawingId = drawing.resourceId;
	xhr.open('PUT', baseUrl + "/rest/resources/drawing/" + drawingId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(drawing));
}

/**
 * @param {json | Object} drawing - The drawing to be added to the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the drawing to the database.
 *
 * @description This method takes the JSON object of the drawing to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then calls the callback function on 'xhr' if the
 * addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addDrawing(drawing, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/resources/drawing", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(drawing));
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the drawings stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllDrawings(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/resources/drawing", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for materials

/**
 * @param {json | Object} material - The material to be updated in the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required material in the database.
 *
 * @description This method takes the JSON object of the material to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the material. This ID is then appended to the
 * URL to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateMaterial(material, callback) {
	let xhr, materialId;
	xhr = new XMLHttpRequest();
	materialId = material.resourceId;
	xhr.open('PUT', baseUrl + "/rest/resources/material/" + materialId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(material));
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the materials stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful.
 */
function getAllMaterials(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/resources/material", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} material - The material to be added to the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the material to the database.
 *
 * @description This method takes the JSON object of the material to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then calls the callback function on 'xhr'
 * if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addMaterial(material, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/resources/material", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(material));
}

//Functions for resources

/**
 * @param {number} resourceId - The ID of the resource required from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets the required resource from the database.
 *
 * @description This method takes the ID of the required resource as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getResource(resourceId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/resource/" + resourceId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} resourceId - The ID of the resource to be deleted.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required resource from the database.
 *
 * @description This method takes the ID of the required resource as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteResource(resourceId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/resource/" + resourceId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the resources stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllResources(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/resources", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();	
}
/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the resources stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteAllResources(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/resources", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Functions for users

/**
 * @param {number} userId - The ID of the user required from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets the required user from the database.
 *
 * @description This method takes the ID of the required user as a parameter. This ID is then
 * appended to the URL to request the service from the back-end. A GET request is sent to the RESTful
 * service provider with the given URL. The method then calls the callback
 * function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getUser(userId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/user/" + userId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} user - The user to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method updates the required user in the database.
 *
 * @description This method takes the JSON object of the user to be updated as a parameter.
 * This object is then parsed to retrieve the ID of the user. This ID is then appended to the URL
 * to request the update on the back-end. A PUT request is sent to the RESTful service provider
 * with the given URL, where the content of the body is the JSON object that was taken as the
 * parameter. The method then calls the callback function on 'xhr' if the update request was
 * successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function updateUser(user, callback) {
	let xhr, userId;
	xhr = new XMLHttpRequest();
	userId = user.userId;
	xhr.open('PUT', baseUrl + "/rest/user/" + userId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(user));
}

/**
 * @param {number} userId - The user to be updated.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method resets the password of the matching user in the database.
 *
 * @description This method takes the userId of the user for whom the password has to be
 * reset as a parameter. This ID is then appended to the URL to request the update on the back-end.
 * A PUT request is sent to the RESTful service provider with the given URL.
 * The method then calls the callback function on 'xhr' if the update request was
 * successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function sendPasswordReset(userId, callback) {
	let xhr;
	xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://env-di-team26.paas.hosted-by-previder.com/kickInTeam26/rest/credential/reset/" + userId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {number} userId - The ID of the user to be deleted from the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes the required user from the database.
 *
 * @description This method takes the ID of the required user as a parameter. This ID is then
 * appended to the URL to request the deletion on the back-end. A DELETE request is sent to the
 * RESTful service provider with the given URL. The method then
 * calls the callback function on 'xhr' if the deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteUser(userId, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/user/" + userId, true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method gets all the users stored in the database.
 *
 * @description A GET request is sent to the RESTful service provider with the given URL. The method then calls the
 * callback function on 'xhr' if the request was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function getAllUsers(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', baseUrl + "/rest/users", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			if (xhr.status === 404 || xhr.status === 200) {
				callback.apply(xhr);
			} else {
				handleResponse(xhr, callback);
			}
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} user - The user to be added to the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method adds the user to the database.
 *
 * @description This method takes the JSON object of the user to be added as a parameter. A POST
 * request is sent to the RESTful service provider with the given URL, where the content of the body
 * is the JSON object that was taken as the parameter. The method then calls the callback function
 * on 'xhr' if the addition was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function addUser(user, callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('POST', baseUrl + "/rest/users", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(user));
}

/**
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method deletes all the users stored in the database.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL, where
 * the content is in JSON format. The method then calls the callback function on 'xhr' if the
 * deletion was successful, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function deleteAllUsers(callback) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', baseUrl + "/rest/users", true);
	xhr.onreadystatechange = function() {
		if (xhr.readyState === 4) {
			handleResponse(xhr, callback);
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

/**
 * @param {json | Object} credentials - The credentials of the user that have to be checked on the database.
 *
 * @param {function} callback - Once an response from the RESTful service provider has been
 * received, this function is called to analyse the response.
 *
 * @summary This method is used to authenticate the user's credentials.
 *
 * @description A POST request is sent to the RESTful service provider with the given URL, where the content of the
 * body is the JSON object that was taken as the parameter. The method then calls the callback function on 'xhr' if the
 * authentication was successful or if wrong credentials were entered, by using the handleResponse method, else an alert is shown with the error
 * message.
 */
function loginUser(credentials, callback) {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', baseUrl + "/rest/authentication", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 204 || xhr.status === 403) {
                callback.apply(xhr);
            } else {
            	handleResponse(xhr, callback);
			}
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(credentials));
}