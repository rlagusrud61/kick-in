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

function displayItems(inum) {
    let itemnum, inputvalue, displayitems, htmltext, x;
    itemnum = jsonObj.items["item" + inum];
    inputvalue = document.getElementById("input" + inum).value
    inputvalue = Number(inputvalue);
    displayitems = document.getElementById("displayitems");
    if (itemnum === null) {
        itemnum = inputvalue;
    } else {
        itemnum = itemnum + inputvalue;
    }
    jsonObj.items["item" + inum] = itemnum;
    htmltext = "";
    for (x in jsonObj.items) {
        htmltext += "<li>" + x + " : " + jsonObj.items[x] + "</li>";
        console.log(x);
        console.log(jsonObj.items[x]);
    }
    displayitems.innerHTML = htmltext;
    console.log(jsonObj.items);
}

function addItems() {
    let row, label, numInput, addButton, materialsList, table, i;
    materialsList = null;
    getAllMaterials(function() {
        materialsList = JSON.parse(this.responseText);
        console.log(materialsList);
        table = document.createElement("table"); // creates the table
        table.setAttribute("id", "addItems");
        for (i = 0; i < materialsList.length; i++) {
            row = table.insertRow(-1); // adds a new row
            label = row.insertCell(0);
            numInput = row.insertCell(1);
            addButton = row.insertCell(2);
            label.innerHTML = '<label>' + materialsList[i].name + '</label>';
            numInput.innerHTML = '<input id ="input' + materialsList[i].resourceId + '" type = "number" name = "quantity"/>';
            addButton.innerHTML = '<button onclick ="displayItems(' + materialsList[i].resourceId + ')" style="background-color: #58BD0F; ' +
                'border-color: #C1FF94;">+</button>';
        }
        document.getElementById("itemlist").appendChild(table);
    })
}
window.onload = addItems;