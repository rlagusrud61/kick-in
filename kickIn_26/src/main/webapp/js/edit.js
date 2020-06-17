function XSSInputSanitation(id) {
    let element = document.getElementById(id).value;
    if (element.indexOf("onload") !== -1 || element.indexOf("<script>") !== -1 ||
        element.indexOf("onerror") !== -1 || element.indexOf("alert") !== -1) {
        console.log("done")
        document.getElementById(id).value = "";
    }
}

//function updateEvent() {
//    description = document.getElementById("eventdescription").value;
//    eventId = window.location.search.split("=")[1];
//    namestuff = document.getElementById("eventname").value;
//    locationstuff = document.getElementById("eventlocation");
//    eventloc = locationstuff.options[locationstuff.selectedIndex].value;
//    eventdate = document.getElementById("eventdate").value;
//    eventjson = {
//        "description": description,
//        "date": evendate,
//        "location": eventloc,
//        "name": namestuff
//    };
//    console.log(JSON.stringify(eventjson));
//    var xhr = new XMLHttpRequest();
//    xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
//    xhr.onreadystatechange = function () {
//        if ((xhr.readyState === 4) && (xhr.status = 200)) {
//            console.log(xhr.responseText);
//            window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
//        }
//    }
//    xhr.setRequestHeader("Content-Type", "application/json");
//    xhr.send(JSON.stringify(eventjson));
//}

function logout() {
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', "http://localhost:8080/kickInTeam26/rest/authentication", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText);
            window.location.href = "http://localhost:8080/kickInTeam26/login.html";
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function updateEvent() {
    description = document.getElementById("eventdescription").value;
    eventId = window.location.search.split("=")[1];
    namestuff = document.getElementById("eventname").value;
    locationstuff = document.getElementById("eventlocation");
    eventloc = locationstuff.options[locationstuff.selectedIndex].value
    eventdate = document.getElementById("eventDate").value;
    eventjson = {
        "description": description,
        "eventId": eventId,
        "location": eventloc,
        "name": namestuff
    };
    console.log(JSON.stringify(eventjson));
    var xhr = new XMLHttpRequest();
    xhr.open('PUT', "http://localhost:8080/kickInTeam26/rest/event/" + eventId, true);
    xhr.onreadystatechange = function () {
        if ((xhr.readyState === 4) && (xhr.status = 200)) {
            console.log(xhr.responseText);
            window.location.href = "http://localhost:8080/kickInTeam26/event.html?id=" + eventId;
        }
    }
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(eventjson));
}

function goBack() {
    window.history.back();
}