let yesBtn, noBtn, deleteMapModal, span1, editBtn, modalMapInfoEdit, span2, span3, span4, span5, selectMapModal,
    listBtn, existingMapBtn, modalAddMap, addMapBtn, yes, no,
    modalEventDelete, deleteEventBtn;

yesBtn = document.getElementById("yesDeleteButton");
noBtn = document.getElementById("noBtn");
editBtn = document.getElementById("editBtn");
yes = document.getElementById("yes");
no = document.getElementById("no");
modalEventDelete = document.getElementById("modalEventDelete");
deleteEventBtn = document.getElementById("deleteEvent");
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
span5 = document.getElementById("closeEventDelete");

deleteEventBtn.onclick = function () {
    modalEventDelete.style.display = "block";
}

addMapBtn.onclick = function () {
    modalAddMap.style.display = "block";
}

existingMapBtn.onclick = function () {
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
no.onclick = function () {
    modalEventDelete.style.display = "none";
}

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
span5.onclick = function () {
    modalEventDelete.style.display = "none";
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
    } else if (event.target === modalEventDelete) {
        modalEventDelete.style.display = "none";
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
    });

    getAllMapsForEvent(id, function () {

        maps = JSON.parse(this.responseText);
        table = document.getElementById("mapTable");

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

window.onload = displayEventInfo;

/**
 * @param {number} mapId - the ID of the map for which the trash button was clicked.
 *
 * @summary This method is used to delete the required map from the database and reloads the page.
 *
 * @description When the 'YES' button is clicked in the map deletion confirmation popup, the 'deleteMap'
 * function is called with the ID of the map as a parameter so that it can be deleted from the database.
 */
function openModalMapDelete(mapId) {
    yesBtn.setAttribute("onclick", "removeMap(" + mapId + ")");
    deleteMapModal.style.display = "block";
}

/**
 * @param {number} mapId - The ID of the map for which the link to the event is to be deleted.
 *
 * @summary This method deletes the association between the map and the event.
 *
 * @description The ID of the event is retrieved from the URL. Then, the function 'deleteEventMap' is called which takes
 * both the ID of the map and the event as parameters and deletes the link between the map and the event. After that, the
 * page is reloaded.
 */
function removeMap(mapId) {
    eventId = window.location.search.split("=")[1];
    deleteEventMap(eventId, mapId, function () {
        location.reload();
    })
}

/**
 * @param {number} mapId - the ID of the map for which the wrench button was clicked.
 *
 * @summary This method is used to update the data of the required map in the database.
 *
 * @description When the 'YES' button is clicked in the map data editing popup, the 'updateMapData'
 * function is called with the ID of the map as a parameter so that it can be updated in the database.
 */
function openModalMapDataEdit(mapId) {
    editBtn.setAttribute("onclick", "updateMapData(" + mapId + ")");
    modalMapInfoEdit.style.display = "block";

}

/**
 * @param {number} mapId - The ID of the map whose name and description is to be updated in the database.
 *
 * @summary This method is used to update the information on a specific map.
 *
 * @description The values of the description and name of the map are retrieved from the popup
 * and the ID of the event to which map is linked is retrieved from the URL. The map name, description and ID is then
 * used to create a JSON object which is taken as a parameter to the function 'updateMap' so that the information on the
 * map can be updated in the database. Once the information is updated, the user is redirected to the page of the event to
 * which the map is linked.
 */
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
    updateMap(mapJSON, function () {
        window.location.href = "event.html?id=" + eventId;
    })
}

/**
 * @summary This method is used to link maps to the event at hand.
 *
 * @description First, the ID of the event at hand is retrieved from the URL. Then, if a check box is checked, the ID of
 * the map for which the check box was checked is retrieved. A JSON object is then created with both the event and map IDs
 * and this JSON object is taken as a parameter to the 'addEventMap' function where the association between the map and event
 * is created and the page is reloaded.
 */
function checkMaps() {
    let eventId, checkboxes, i, eventMapJSON;
    eventId = window.location.search.split("=")[1];
    checkboxes = document.getElementsByName("checkBox");
    clearMapsForEvent(eventId, function () {
        for (i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].checked) {
                mapId = parseInt(checkboxes[i].id.split("_")[1]);
                eventMapJSON = {
                    "eventId": eventId,
                    "mapId": mapId
                }
                addEventMap(eventMapJSON, function () {
                    location.reload();
                })
            }
        }
    })
}

/**
 * @summary This method is used to create a table that displays the information on the maps linked to the event at hand.
 *
 * @description The ID od the event is first retrieved from the URL. Then, all the maps that are in the database are
 * retrieved using the 'getAllMaps' function and used to fill a check list that the user can use to link different maps
 * to the specific event. Once that is done, the method 'getAllMapsForEvent' is called which takes the ID of the event
 * as a parameter. Once all the maps for the event are retrieved, these maps are checked in the check list.
 */
function loadMaps() {
    let eventId, allMaps, mapCheckList, i, tiedMaps, mapSize, mapBox;
    eventId = window.location.search.split("=")[1];
    getAllMaps(function () {
        allMaps = JSON.parse(this.responseText);
        mapCheckList = document.getElementById("mapCheckList");
        mapCheckList.innerHTML = "";
        displayEventInfo();
        mapSize = allMaps.length;
        for (i = 0; i < allMaps.length; i++) {
            mapCheckList.innerHTML += '<div class="custom-control custom-checkbox">' +
                '<input name = "checkBox" type="checkbox" class="custom-control-input" id="mapBox_' + allMaps[i].mapId + '">' +
                '<label class="custom-control-label checkboxtext" for="mapBox_' + allMaps[i].mapId + '">' + allMaps[i].name + '</label></div>'
        }
        getAllMapsForEvent(eventId, function () {
            tiedMaps = JSON.parse(this.responseText);
            for (i = 0; i < tiedMaps.length; i++) {
                mapBox = document.getElementById("mapBox_" + tiedMaps[i].mapId);
                mapBox.checked = true;
            }

        });
    });
}

window.onload = loadMaps;

function generateReport() {
    let id, event, report, doc;
    id = window.location.search.split("=")[1];
    getEvent(id, function () {
        event = JSON.parse(this.responseText);
        report = event.report;
        doc = new jsPDF();
        if (report !== null) {
            report.forEach(function (item, i) {
                doc.text(20, 10 + (i * 10),
                    "Name: " + item.name + " | " +
                    "Count: " + item.count);
            });
            doc.save('EventReport.pdf');
        } else {
            alert("There are no items on maps for this event.");
        }
    });
}

/**
 * @summary This method is used to download a report of the items placed on the maps and their respective quantities for the
 * specific event.
 *
 * @description First, the ID of the event is retrieved from the URL. This ID is then used as a parameter of the 'getEvent'
 * function. Once the data on the event is retrieved, the value of the 'report' key is retrieved. If it is empty, an alert
 * message is displayed. If not, a .json file is created and downloaded which contains the name of the items placed on the
 * maps along with their respective quantities.
 */
function generateReportInJSON() {
    let a, json, blob, url, data, fileName, event, id;
    id = window.location.search.split("=")[1];
    getEvent(id, function () {
        event = JSON.parse(this.responseText);
        data = event.report;
        if (data !== null) {
            fileName = "Event" + id + "Report.json";
            a = document.createElement("a");
            document.body.appendChild(a);
            a.style = "display: none";
            json = JSON.stringify(data);
            blob = new Blob([json], {type: "octet/stream"});
            url = window.URL.createObjectURL(blob);
            a.href = url;
            a.download = fileName;
            a.click();
            window.URL.revokeObjectURL(url);
        } else {
            alert("There are no items on maps for this event.");
        }
    });
}



