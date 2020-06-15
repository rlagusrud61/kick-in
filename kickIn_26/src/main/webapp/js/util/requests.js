/**
 * 
 */

//Event functions
function getEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/event", true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function updateEvent(event) {
	eventId = event.eventId
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

function deleteEvent(eventId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/event/" + eventId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function getAllEvents() {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function addEvent(event) {
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 201)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send(JSON.stringify(event));
}

function deleteAllEvents() {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/events" , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

//Map functions
function getMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function updateMap(map) {
	mapId = map.mapId
	let xhr = new XMLHttpRequest();
	xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function deleteMap(mapId) {
	let xhr = new XMLHttpRequest();
	xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/map/" + mapId , true);
	xhr.onreadystatechange = function() {
		if ((xhr.readyState == 4) && (xhr.status = 200)) {
			return xhr.responseText;
		}
	}
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
	
}

function getAllMaps() {
	
}

function addMap(map) {
	
}

function deleteAllMaps() {
	
}

//Eventmap functions
function getAllMapsForEvent(eventId) {
	
}

function getAllEventsForMap(mapId) {
	
}

function addEventMap(eventMap) {
	
}

function clearEvent(eventId) {
	
}

function deleteEventMap(eventId, mapId) {
	
}

function deleteAllRelations() {
	
}

//Functions for objects
function getObject(objectId) {
	
}

function updateObject(mapObject) {
	
}

function deleteObject(objectId) {
	
}

function getAllObjects() {
	
}

function generateReportForMap(mapId) {
	
}

function addObjectToMap(mapObject) {
	
}

function clearAllMaps() {
	
}

//Functions for drawings
function getDrawing(drawingId) {
	
}

function updateDrawing(drawing) {
	
}

function deleteDrawing(drawingId) {
	
}

function getAllDrawings() {
	
}

function addDrawing(drawing) {
	
}

//Functions for materials
function getMaterialDrawing(materialId) {
	
}

function updateMaterial(material) {
	
}

function deleteMaterial(materialId) {
	
}

function getAllMaterials() {
	
}

function addMaterial(material) {
	
}

//Functions for resources
function deleteAllResources() {
	
}

function deleteResource(resourceId) {
	
}