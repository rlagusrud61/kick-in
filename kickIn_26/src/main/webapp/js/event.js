let yesBtn, noBtn, deleteMapModal, span, editBtn, modalMapInfoEdit, span2;
yesBtn = document.getElementById("yesDeleteButton");
noBtn = document.getElementById("noBtn");
editBtn = document.getElementById("editBtn");
deleteMapModal = document.getElementById("modalMapDelete");
modalMapInfoEdit = document.getElementById("modalMapInfoEdit");
span = document.getElementsByClassName("close")[0];
span2 = document.getElementsByClassName("close")[1];

//Close the modal if user clicks on NO button
noBtn.onclick = function () {
    deleteMapModal.style.display = "none";
};

//Close the modal if user clicks on close (x) button
span.onclick = function () {
    deleteMapModal.style.display = "none";
};
span2.onclick = function () {
    modalMapInfoEdit.style.display = "none";
};

//Close the modal if user clicks outside the modal window
window.onclick = function (event) {
    if (event.target === deleteMapModal) {
        deleteMapModal.style.display = "none";
    } else if (event.target === modalMapInfoEdit) {
        modalMapInfoEdit.style.dislay = "none";
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
    document.getElementById("editEvent").href = "http://localhost:8080/kickInTeam26/edit.html?id=" + id;

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
                "style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" + maps[i].mapId + ")'" +
                "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>" +
                "<a href='javascript: window.openModalMapDataEdit(" + maps[i].mapId + ")' class='text-success'>" +
                "<i class='glyphicon glyphicon-wrench' style='font-size:20px'></i></a>";
        }
    })
}

yesBtn = document.getElementById("yesDeleteButton");
deleteMapModal = document.getElementById("modalMapDelete");
selectMapModal = document.getElementById("mapSelectModal");

function openCoolModal() {
	selectMapModal.style.display = "block";
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
function confirmDelete(mapId) {
    yesBtn.setAttribute("onclick", "removeMap(" + mapId + ")");
    deleteMapModal.style.display = "block";
}

function removeMap(mapId) {
    deleteMap(mapId, function () {
        location.reload();
    })
}

//Open popup for edit
function openModalMapDataEdit(mapId) {
    yesBtn.setAttribute("onclick", "updateMapData(" + mapId + ")");
    modalMapInfoEdit.style.display = "block";

}

function updateMapData(mapId) {
    let mapName, description, eventId, mapJSON;
    mapName = document.getElementById("mapName").value;
    description = document.getElementById("description").value;
    mapJSON = {
        "name": mapName,
        "mapId": mapId,
        "description": description
    };
    console.log(mapJSON);
    updateMap(mapJSON, function () {
        window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
    })
}

//function loadMuhMaps() {
//	getAllMaps(function() {
//		maps = JSON.parse(this.responseText);
//        mapCheckList = document.getElementById("nicenicenicenice");
//        mapCheckList.innerHTML = "";
//        displayEventInfo();
//        for (i = 0; i < maps.length; i++) {
//        	mapCheckList.innerHTML += '<input class="form-check-input" type="checkbox" value="" id="defaultCheck1"><label class="form-check-label" for="defaultCheck1">Default checkbox</label>'
//        }
//        console.log(mapCheckList.innerHTML);
//      
//	})
//}
//window.onload = loadMuhMaps;
window.onload = displayEventInfo;

