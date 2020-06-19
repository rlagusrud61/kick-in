let modal, modal1, btn, btn1, span;

// Get the modal
modal = document.getElementById("popupEventDelete");
modal1 = document.getElementById("popupMapDelete");

// Get the button that opens the modal
btn = document.getElementById("deleteEvent");
btn1 = document.getElementById("deleteMap");

// Get the <span> element that closes the modal
span = document.getElementsByClassName("close")[0];


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

function displayEventInfo(){
    let id, event, maps;
    id = window.location.search.split("=")[1];
    document.getElementById("editEvent").href = "http://localhost:8080/kickInTeam26/edit.html?id=" + id
    getEvent(id, function() {
        event = JSON.parse(this.responseText);
        document.getElementById("introtext").innerHTML = event.description;
        document.getElementById("eventlocation").innerHTML = event.location;
        document.getElementById("eventname").innerHTML = event.name;
        document.getElementById("eventdate").innerHTML = event.date;
        document.getElementById("addNewMap").href = "http://localhost:8080/kickInTeam26/newMap.html?id=" + event.eventId;
    })

    getAllMapsForEvent(id, function() {
        maps = JSON.parse(this.responseText);
        console.log(maps);
    })
}

window.onload = displayEventInfo;
