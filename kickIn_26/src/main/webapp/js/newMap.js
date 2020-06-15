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
    let eventId = 3;

    createMap.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {

            mapId = parseInt(createMap.responseText);
            let createMapRelation = new XMLHttpRequest();
            createMapRelation.onreadystatechange = function () {
                if (createMapRelation.readyState === 4 && createMapRelation.status === 200) {
                    window.location.href = "http://localhost:8080/kickInTeam26/list.html";
                }
            }
            createMapRelation.open("POST", ("http://localhost:8080/kickInTeam26/rest/eventMap"));
            createMapRelation.setRequestHeader("Content-Type", "application/json");
            createMapRelation.send('{"eventId":"' + eventId + '","mapId":"' + mapId + '"}')
        }
    }
    createMap.open("POST", ("http://localhost:8080/kickInTeam26/rest/maps"));
    createMap.setRequestHeader("Content-Type", "application/json");
    createMap.send('{"name":"' + mapName + '","description":"' + description + '"}');
}
