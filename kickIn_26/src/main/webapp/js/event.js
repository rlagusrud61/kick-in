// Get the modal
var modal = document.getElementById("popupEventDelete");
var modal1 = document.getElementById("popupMapDelete");

// Get the button that opens the modal
var btn = document.getElementById("deleteEvent");
var btn1 = document.getElementById("deleteMap");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];


// When the user clicks the button, open the modal
btn.onclick = function() {
    modal.style.display = "block";
}

btn1.onclick = function() {
    modal1.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === modal1) {
        modal1.style.display = "none";
    }
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === modal1) {
        modal1.style.display = "none";
    }
}

//  javascript from Joep
//function getMaps() {
//    var id = window.location.search.split("=")[1];
//    const xhr = new XMLHttpRequest();
//    xhr.open('GET', "http://localhost:8080/kickInTeam26/rest/eventMap/event/" + id, true);
//    xhr.onreadystatechange = function () {
//        if (xhr.readyState === 4) {
//            console.log(xhr.responseText);
//        }
//    }
//    xhr.setRequestHeader("Content-Type", "application/json");
//    xhr.send();
//
//}

window.onload = function() {
    var id = window.location.search.split("=")[1];
    document.getElementById("editEvent").href = "http://localhost:8080/kickInTeam26/edit.html?id=" + id
    const eventDetails = new XMLHttpRequest();
    eventDetails.open('GET', "http://localhost:8080/kickInTeam26/rest/event/" + id, true);
    eventDetails.onreadystatechange = function () {
        if (eventDetails.readyState === 4) {
            var event = JSON.parse(eventDetails.responseText);
            document.getElementById("introtext").innerHTML = event.description;
            document.getElementById("eventlocation").innerHTML = event.location;
            document.getElementById("eventname").innerHTML = event.name;
            document.getElementById("eventdate").innerHTML = event.date;
            document.getElementById("addNewMap").href = "http://localhost:8080/kickInTeam26/newMap.html?id=" + event.eventId;
        }
    }
    eventDetails.setRequestHeader("Content-Type", "application/json");
    eventDetails.send();

    let mapsList = new XMLHttpRequest();
    mapsList.open('GET', "http://localhost:8080/kickInTeam26/rest/eventMap/event/"+id);
//    mapsList.setRequestHeader("Content-Type", "application/json")
    mapsList.onreadystatechange = function () {
        if (mapsList.status === 4 && mapsList.status === 200) {
            let maps = JSON.parse(mapsList.responseText);
            console.log(maps);
        }
    }
    mapsList.send();
}

function logout(){
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
            window.location.href="http://localhost:8080/kickInTeam26/login.html";
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function goBack() {
    window.history.back();
}