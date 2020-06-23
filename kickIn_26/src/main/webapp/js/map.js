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