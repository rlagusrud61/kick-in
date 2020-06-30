/**
 * @param {string} tableId - The ID of the table to be sorted.
 *
 * @param {string} sortCriteria - The table header on which the table is to be sorted.
 *
 * @summary This method sorts the table in ascending order using the bubble sort algorithm.
 */
function sortTableAscending(tableId, sortCriteria) {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById(tableId);
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            if (sortCriteria === "name" || sortCriteria === "event" || sortCriteria === "map" ||  sortCriteria === "resource" ){
                x = rows[i].getElementsByTagName("TD")[0];
                y = rows[i + 1].getElementsByTagName("TD")[0];
            } else if (sortCriteria === "date"){
                x = rows[i].getElementsByTagName("TD")[1];
                y = rows[i + 1].getElementsByTagName("TD")[1];
            }
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

/**
 * @param {string} tableId - The ID of the table to be sorted.
 *
 * @param {string} sortCriteria - Indication on the table column on which the table is to be sorted.
 *
 * @summary This method sorts the table in descending order using the bubble sort algorithm.
 */
function sortTableDescending(tableId, sortCriteria) {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById(tableId);
    switching = true;
    /* Make a loop that will continue until
    no switching has been done: */
    while (switching) {
        // Start by saying: no switching is done:
        switching = false;
        rows = table.rows;
        /* Loop through all table rows (except the
        first, which contains table headers): */
        for (i = 1; i < (rows.length - 1); i++) {
            // Start by saying there should be no switching:
            shouldSwitch = false;
            /* Get the two elements you want to compare,
            one from current row and one from the next: */
            if (sortCriteria === "name" || sortCriteria === "event" || sortCriteria === "map" || sortCriteria === "resource"){
                x = rows[i].getElementsByTagName("TD")[0];
                y = rows[i + 1].getElementsByTagName("TD")[0];
            } else if (sortCriteria === "date"){
                x = rows[i].getElementsByTagName("TD")[1];
                y = rows[i + 1].getElementsByTagName("TD")[1];
            }
            // Check if the two rows should switch place:
            if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                // If so, mark as a switch and break the loop:
                shouldSwitch = true;
                break;
            }
        }
        if (shouldSwitch) {
            /* If a switch has been marked, make the switch
            and mark that a switch has been done: */
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
}

/**
 * @summary This method is used to log out.
 *
 * @description A DELETE request is sent to the RESTful service provider with the given URL. The method then redirects
 * the user to the 'login.html' page if the request was successful and takes the cookie data away from the user.
 */
function logout() {
    let xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            window.location.href = "login.html";
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

/**
 *
 * @param {string} id - The ID of the element that takes input from the user.
 *
 * @summary This method is used to prevent Cross-Site Scripting (XSS).
 *
 * @description This method takes the value from the input element using its ID and checks whether the phrases 'onload',
 * 'onerror', '<script>' and 'alert' are found. If they are found, the input element is emptied and an empty string is
 * returned. If not, the value found is returned.
 *
 * @returns {string} - If the phrases are found, an empty string is returned. If not, the value found is returned.
 */
function XSSInputSanitation(id) {
    let element = document.getElementById(id).value;
    if (element.indexOf("onload") !== -1 || element.indexOf("<script>") !== -1 ||
        element.indexOf("onerror") !== -1 || element.indexOf("alert") !== -1) {
        document.getElementById(id).value = "";
        return "";
    } else {
        return element;
    }
}

/**
 * @summary This method is used to go back to the previously accessed page within the website.
 */
function goBack() {
    window.history.back();
}

/**
 * @param {string} tableID - the ID of the table on which the search on the first column is to be done.
 *
 * @param {string} searchBoxID - the ID of the search input box which has the search criteria.
 *
 * @summary This method is used to search through the table for the required information.
 *
 * @description This method first sanitises the value in the search box to prevent cross site scripting. If the value is
 * emptied, the whole table is shown. If not, the required data is shown on the table if found, and an empty table is
 * shown if the required data was not found.
 */
function searchTables(tableID, searchBoxID) {
    // Declare variables
    let searchValue, filter, table, tr, td, i, txtValue;
    searchValue = document.getElementById(searchBoxID).value;
    filter = searchValue.toUpperCase();
    table = document.getElementById(tableID);
    tr = table.getElementsByTagName("tr");
    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

/**
 * Takes a picture of the map after fitting all objects on the map into the frame.
 */
function screenshot(callback) {
    map.removeControl(zoomControl);
    fitMapBounds(function () {
        domtoimage.toPng(map.getContainer(), {
            width: map.getSize().x,
            height: map.getSize().y
        }).then(function(img) {
            map.addControl(zoomControl);
            callback(img);
        });
    })
}

/**
 * Util function for finding the bounds that fit all the objects.
 * @returns {({lng: number, lat: number})[]|*[]}
 */
function getMapBounds() {
    const topLeft = {lat:Infinity, lng:Infinity}
    const bottomRight = {lat:-Infinity, lng:-Infinity};
    if (mapObjects.size >= 1) {
        mapObjects.forEach(function (object) {
            const corners = JSON.parse(object.latLangs);
            corners.forEach(function (latLng) {
                if (latLng.lat < topLeft.lat) {
                    topLeft.lat = latLng.lat;
                }
                if (latLng.lng < topLeft.lng) {
                    topLeft.lng = latLng.lng;
                }
                if (latLng.lat > bottomRight.lat) {
                    bottomRight.lat = latLng.lat;
                }
                if (latLng.lng > bottomRight.lng) {
                    bottomRight.lng = latLng.lng;
                }
            })
        })
    } else {
        map.panTo([52.2413, -353.1531]);
        map.setZoom(16);
        return [map.containerPointToLatLng([0,0]), map.containerPointToLatLng([map.getSize().x, map.getSize().y])];
    }
    return [topLeft, bottomRight]
}

/**
 * Pans the map to fit the map exactly all the objects.
 */
async function fitMapBounds(callback) {
    map.fitBounds(getMapBounds())
    await new Promise(r => setTimeout(r, 500));
    callback();
}