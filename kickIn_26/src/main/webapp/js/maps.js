let modal, btn, span, yesBtn, deleteModal, close;

// Get the modal
modal = document.getElementById("addMap");
deleteModal = document.getElementById("mapDeleteModal");
btn = document.getElementById("addMapBtn");
noBtn = document.getElementById("noBtn");
yesBtn = document.getElementById("yesDeleteButton");
span = document.getElementsByClassName("close close_multi")[0];
span2 = document.getElementsByClassName("close close_multi")[1];

btn.onclick = function(){
    modal.style.display = "block";
}

noBtn.onclick = function () {
    deleteModal.style.display = "none";
}

span.onclick = function() {
    modal.style.display = "none";
}

span2.onclick = function(){
    deleteModal.style.display = "none";

}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    }
}

/**
 * @summary This method is used to create a table that displays the information on the maps.
 *
 * @description Once all the information on the maps is retrieved using the 'getAllMaps' function, a table is created
 * to display the information received. The table includes the columns 'Name', 'Description', 'Creator', 'Last Edited By'
 * and 'Action' where 'Name' gives the name of the map, 'Description' gives the description of the map, 'Creator' gives the
 * name of the user that created the map, 'Last Edited By' gives name of the user that last edited the data on the the
 * map and 'Action' allows the user to view the map, edit the data on the map as well as delete the map.
 */
function loadTable() {
    let header, tr, th, i, table, maps, row, name, description, creator, lastEditor, action;
    getAllMaps(function () {
        table = document.getElementById("mapTable");
        maps = JSON.parse(this.responseText);

        header = [];
        header.push('Name');
        header.push('Description');
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
            name = row.insertCell(0);
            description = row.insertCell(1);
            creator = row.insertCell(2);
            lastEditor = row.insertCell(3);
            action = row.insertCell(4);
            name.innerHTML = maps[i].name;
            description.innerHTML = maps[i].description;
            creator.innerHTML = maps[i].createdBy;
            lastEditor.innerHTML = maps[i].lastEditedBy;
            action.innerHTML = "<a href='mapView.html?id=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-eye-open' " +
                "style='font-size:20px;'></i></a><a href='mapEdit.html?id=" +
                maps[i].mapId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" + maps[i].mapId + ")'" +
                "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        }
    });
}

window.onload = loadTable;

/**
 * @summary This method is used to add a new map to the database.
 *
 * @description The name, description and events to link the map to are retrieved from the popup. Then, a JSON object
 * is created from the information retrieved and the 'addMap' function is called with this JSON object as a parameter.
 * If the map was successfully added to the database, the page is reloaded.
 */
//TODO checkbox stuff
function addMapPopup() {
    let description, mapName, mapJSON;
    description = document.getElementById("mapDescription").value;
    mapName = document.getElementById("mapName").value;
    mapJSON = {
        "name": mapName,
        "description": description,
    };
    addMap(mapJSON, function () {
        location.reload();
    });
}

/**
 * @param {number} mapId - the ID of the map for which the trash glyphicon was clicked.
 *
 * @summary This method is used to open the modal and set the ID of the map to be deleted when the 'YES' button is
 * clicked in the map deletion confirmation popup.
 */
function confirmDelete(mapId) {
    yesBtn.setAttribute("onclick", "removeMap(" + mapId + ")");
    deleteModal.style.display = "block";
}

/**
 * @param {number} mapId - the ID of the map that is to be deleted from the database.
 *
 * @summary This method is used to delete the required map from the database and reloads the page.
 *
 * @description When the 'YES' button is clicked in the map deletion confirmation popup, the 'removeMap'
 * function is called with the ID of the map as a parameter so that it can be deleted from the database.
 * The ID of the map is taken as a parameter to the 'deleteMap' function so that it can be deleted from
 * the database. If the deletion is successful, the page is reloaded.
 */
function removeMap(mapId) {
    deleteMap(mapId, function() {
        location.reload();
    })
}

