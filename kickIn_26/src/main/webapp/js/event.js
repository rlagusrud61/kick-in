let yesBtn, noBtn, deleteMapModal, span1, editBtn, modalMapInfoEdit, span2, span3, span4 , selectMapModal, listBtn , existingMapBtn , modalAddMap, addMapBtn;
yesBtn = document.getElementById("yesDeleteButton");
noBtn = document.getElementById("noBtn");
editBtn = document.getElementById("editBtn");
addMapBtn = document.getElementById("addMap");
listBtn = document.getElementById("addMapsToEvent");
existingMapBtn = document.getElementById("existingMapBtn");
deleteMapModal = document.getElementById("modalMapDelete");
modalMapInfoEdit = document.getElementById("modalMapInfoEdit");
modalAddMap = document.getElementById("modalAddMap");
selectMapModal = document.getElementById("mapSelectModal");
span1 = document.getElementById("closeMapInfoEdit");
span2 = document.getElementById("closeMapSelect");
span3 = document.getElementById("closeMapDelete");
span4 = document.getElementById("closeAddMap");


addMapBtn.onclick = function() {
    modalAddMap.style.display = "block";
}

existingMapBtn.onclick = function() {
    modalAddMap.style.display = "none";
    selectMapModal.style.display = "block";
};
listBtn.onclick = function () {
    selectMapModal.style.display = "block";
};
//Close the modal if user clicks on NO button
noBtn.onclick = function () {
    deleteMapModal.style.display = "none";
};

//Close the modal if user clicks on close (x) button
span1.onclick = function () {
    modalMapInfoEdit.style.display = "none";
};
span2.onclick = function () {
    selectMapModal.style.display = "none";
};
span3.onclick = function () {
    deleteMapModal.style.display = "none";
};
span4.onclick = function () {
    modalAddMap.style.display = "none";
};

//Close the modal if user clicks outside the modal window
window.onclick = function (event) {
    if (event.target === deleteMapModal) {
        deleteMapModal.style.display = "none";
    } else if (event.target === modalMapInfoEdit) {
        modalMapInfoEdit.style.display = "none";
    } else if (event.target === selectMapModal) {
        selectMapModal.style.display = "none";
    } else if (event.target === modalAddMap) {
        modalAddMap.style.display = "none";
    }
};

/**
 * @summary This method is used to display the information on the event required.
 *
 * @description First, the ID of the event at hand is retrieved from the URL. This ID is then
 * used to edit the event, display the information on this event, add a new map for this event
 * and to get all the maps for this event. The information retrieved on the event is displayed where required
 * and the information on all the maps for this event is displayed on a table.
 */
function displayEventInfo() {
    let id, event, maps, table, header, th, tr, row, i, mapName, creator, lastEditor, action;

    id = window.location.search.split("=")[1];
    document.getElementById("editEvent").href = "http://localhost:8080/kickInTeam26/editEvent.html?id=" + id;

    getEvent(id, function () {
        event = JSON.parse(this.responseText);
        document.getElementById("introtext").innerHTML = event.description;
        document.getElementById("eventlocation").innerHTML = event.location;
        document.getElementById("eventname").innerHTML = event.name;
        document.getElementById("eventdate").innerHTML = event.date;
        document.getElementById("addNewMap").href = "http://localhost:8080/kickInTeam26/newMap.html?id=" + event.eventId;
    })

    getAllMapsForEvent(id, function () {

        maps = JSON.parse(this.responseText);
        table = document.getElementById("mapTable");
        console.log(maps);

        header = [];
        header.push('Name');
        header.push('Creator');
        header.push('Last Edited By');
        header.push('Action');

        tr = table.insertRow(-1); // add a row to the table
        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }

        for (i = 0; i < maps.length; i++) {
            row = table.insertRow(-1);
            mapName = row.insertCell(0);
            creator = row.insertCell(1);
            lastEditor = row.insertCell(2);
            action = row.insertCell(3);
            mapName.innerHTML = maps[i].name;
            creator.innerHTML = maps[i].createdBy;
            lastEditor.innerHTML = maps[i].lastEditedBy;
            action.innerHTML = "<a href='http://localhost:8080/kickInTeam26/mapView.html?mapId=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-eye-open' " +
                "style='font-size:20px;'></i></a><a href='http://localhost:8080/kickInTeam26/mapEdit.html?id=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:20px;'></i></a><a href='javascript: window.openModalMapDelete(" + maps[i].mapId + ")'" +
                "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>" +
                "<a href='javascript: window.openModalMapDataEdit(" + maps[i].mapId + ")' class='text-success'>" +
                "<i class='glyphicon glyphicon-wrench' style='font-size:20px'></i></a>";
        }
    })
}

/**
 * @param {number} mapId - the ID of the map for which the trash glyphicon was clicked.
 *
 * @summary This method is used to delete the required map from the database and reloads the page.
 *
 * @description When the 'YES' button is clicked in the map deletion confirmation popup, the 'deleteMap'
 * function is called with the ID of the map as a parameter so that it can be deleted from the database and the
 * page is reloaded.
 */
function openModalMapDelete(mapId) {
    yesBtn.setAttribute("onclick", "removeMap(" + mapId + ")");
    deleteMapModal.style.display = "block";
}

function removeMap(mapId) {
	eventId = window.location.search.split("=")[1];
    deleteEventMap(eventId, mapId, function () {
        location.reload();
    })
}

//Open popup for edit
function openModalMapDataEdit(mapId) {
    editBtn.setAttribute("onclick", "updateMapData(" + mapId + ")");
    modalMapInfoEdit.style.display = "block";
    console.log("hellotherefriend");

}

//Update the Information of the Map
function updateMapData(mapId) {
    let mapName, mapDescription, eventId, mapJSON;
    mapName = document.getElementById("mapName").value;
    mapDescription = document.getElementById("mapDescription").value;
    eventId = window.location.search.split("=")[1];
    mapJSON = {
        "name": mapName,
        "mapId": mapId,
        "description": mapDescription
    };
    console.log(mapJSON);
    updateMap(mapJSON, function () {
        window.location.href = "event.html?id=" + eventId;
    })
}
var mapsize;

function checkMaps() {
	eventId = window.location.search.split("=")[1];
	checkboxes = document.getElementsByName("nicecheckbox");
	clearMapsForEvent(eventId, function() {
		for (i = 0; i < checkboxes.length; i++) {
			if (checkboxes[i].checked) {
				mapId = parseInt(checkboxes[i].id.split("_")[1]);
				eventMapJSON = {
						"eventId": eventId,
						"mapId": mapId
				}
				addEventMap(eventMapJSON, function() {
					location.reload();
				})
			}
		}
	})	
}
function loadMaps() {
	eventId = window.location.search.split("=")[1];
    getAllMaps(function () {
        let maps, mapCheckList;
        allMaps = JSON.parse(this.responseText);
        mapCheckList = document.getElementById("mapCheckList");
        mapCheckList.innerHTML = "";
        displayEventInfo();
        mapsize = allMaps.length;
        for (let i = 0; i < allMaps.length; i++) {
            mapCheckList.innerHTML += '<div class="custom-control custom-checkbox">' +
                '<input name = "nicecheckbox" type="checkbox" class="custom-control-input" id="mapBox_'+ allMaps[i].mapId +'">' +
                '<label class="custom-control-label checkboxtext" for="mapBox_'+ allMaps[i].mapId +'">' + allMaps[i].name + '</label></div>'
        }
        getAllMapsForEvent(eventId, function() {
        	tiedMaps = JSON.parse(this.responseText);
        	for (i = 0; i < tiedMaps.length; i++) {
        		mapBox = document.getElementById("mapBox_" + tiedMaps[i].mapId);
        		mapBox.checked = true;
        	}
        
        })
    })
}

window.onload = loadMaps();

