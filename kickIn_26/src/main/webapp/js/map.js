let localReport = new Map();

function openAccordion(id) {
    let  availableItems = document.getElementById(id);
    if (availableItems.className.indexOf("w3-show") === -1) {
        availableItems.className += " w3-show";
        availableItems.previousElementSibling.className += " w3-green";
    } else {
        availableItems.className = availableItems.className.replace(" w3-show", "");
        availableItems.previousElementSibling.className = availableItems.previousElementSibling.className.replace(" w3-green", "");
    }
}
function updateLocalReport() {
    let itemReport, tableHTML;
    itemReport = document.getElementById("displayitems");
    generateReportForMap(mapId, function () {
        localReport = JSON.parse(this.responseText);
        tableHTML = "";
        localReport.forEach(function(object) {
            tableHTML += "<li>" + object.name + " : " + object.count + "</li>";
        })
        itemReport.innerHTML = tableHTML;
    })
}
function addResourcesToMap(rid) {
    let numberToAdd = document.getElementById("input"+rid).value;
    for (let i = 0; i < numberToAdd;i++) {
        addResourceToMap(rid)
    }
}
function addItems() {
    let row, label, numInput, addButton, table;
    table = document.createElement("table"); // creates the table
    table.setAttribute("id", "addItems");
    resources.forEach(function (resourceName, id) {
        row = table.insertRow(-1); // adds a new row
        label = row.insertCell(0);
        numInput = row.insertCell(1);
        addButton = row.insertCell(2);
        label.innerHTML = '<label>' + resourceName + '</label>';
        numInput.innerHTML = '<input id ="input' + id + '" type = "number" name = "quantity"/>';
        addButton.innerHTML = '<button onclick ="addResourcesToMap(' + id + ')" style="background-color: #58BD0F; ' +
            'border-color: #C1FF94;">+</button>';
    })
    document.getElementById("itemlist").appendChild(table);
}