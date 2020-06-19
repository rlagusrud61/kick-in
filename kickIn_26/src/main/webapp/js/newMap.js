function XSSInputSanitation(id) {
    let element = document.getElementById(id).value;
    if (element.indexOf("onload") !== -1 || element.indexOf("<script>") !== -1 ||
        element.indexOf("onerror") !== -1 || element.indexOf("alert") !== -1) {
        console.log("done")
        document.getElementById(id).value = "";
    }
}

function addNewMap() {
    let mapName = document.getElementById("mapName").value;
    let description = document.getElementById("description").value;

    let createMap = new XMLHttpRequest();
    let mapId;
    let eventId = window.location.search.split("=")[1];
    let mapJSON = {
    		"name" : mapName,
    		"description" : description
    }
    console.log(mapJSON)
    addMap(mapJSON, function() {
    	mapId = parseInt(this.responseText);
    	let eventMapJSON = {
    			"eventId": eventId,
    			"mapId": mapId
    	}
    	addEventMap(eventMapJSON, function (){
    		window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
    	})
    })
}

function goBack() {
    window.history.back();
}