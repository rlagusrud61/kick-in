let modal, imageModal, btn, span, yesBtn, deleteModal, close;

// Get the modal
modal = document.getElementById("addResource");
imageModal = document.getElementById("imageModal");
deleteModal = document.getElementById("resourceDeleteModal");
// Get the button that trigger the modal
btn = document.getElementById("addResourceBtn");
noBtn = document.getElementById("noBtn");
yesBtn = document.getElementById("yesDeleteButton");
// Get the button to close (x) the modal
span = document.getElementsByClassName("close close_multi")[0];
span2 = document.getElementsByClassName("close close_multi")[1];
span3 = document.getElementsByClassName("close close_multi")[2];

btn.onclick = function () {
    modal.style.display = "block";
};

noBtn.onclick = function () {
    deleteModal.style.display = "none";
};

span.onclick = function () {
    modal.style.display = "none";
};

span2.onclick = function () {
    deleteModal.style.display = "none";

};

span3.onclick = function () {
    imageModal.style.display = "none";

};

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    } else if (event.target === deleteModal) {
        deleteModal.style.display = "none";
    } else if (event.target === imageModal){
        imageModal.style.display = "none";
    }
}

/**
 * @summary This method is used to create a table that displays the information on the resources.
 *
 * @description Once all the information on the resources is retrieved using the 'getAllResources' function, a table is created
 * to display the information received. The table includes the columns 'Name', 'Description' and 'Action' where 'Name'
 * gives the name of the resource, 'Description' gives the description of the resource and 'Action' allows the user to
 * edit the data on the resource as well as delete the resource.
 */
//TODO edit resource
function loadTable() {
    let header, tr, th, i, table, resources, row, name, description, action;
    getAllResources(function () {
        table = document.getElementById("resourceTable");
        resources = JSON.parse(this.responseText);
        console.log(resources);
        header = [];
        header.push('Name');
        header.push('Description');
        header.push('Action');

        tr = table.insertRow(-1); // add a row to the table
        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }

        for (i = 0; i < resources.length; i++) {
            row = table.insertRow(-1);
            name = row.insertCell(0);
            description = row.insertCell(1);
            action = row.insertCell(2);
            name.innerHTML = resources[i].name;
            description.innerHTML = resources[i].description;
            action.innerHTML = "<a href='javascript: window.viewResource(" + resources[i].resourceId + ")' class='text-success'>" +
                "<i class='glyphicon glyphicon-eye-open' style='font-size:20px;'></i></a><a href='javascript: window.confirmDelete(" +
                resources[i].resourceId + ")'" + "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:20px;'></i></a>";
        }
    });
}

window.onload = loadTable;

/**
 * @param {number} resourceId - The ID of the resource for which the image is required.
 *
 * @summary This method allows the user to view the image of the required resource.
 *
 * @description Once all the information on the required resource is retrieved using the 'getResource' function which takes the ID of
 * the required resource as a parameter, the image of the resource is displayed in the popup.
 */
function viewResource(resourceId) {
    let resourcePicture, resource;
    resourcePicture = document.getElementById("resourcePicture");
    getResource(resourceId, function () {
        resource = JSON.parse(this.responseText);
        resourcePicture.setAttribute("src", resource.image);
        imageModal.style.display = "block";
    });
}

/**
 * @summary This method is used to add a new resource to the database.
 *
 * @description The name, description and image of the resource is retrieved from the popup. Then, a JSON object
 * is created from the information retrieved and either the 'addMaterial' or 'addResource' function is called depending
 * on the type of the resource with this JSON object as a parameter. If the resource was successfully added to the database,
 * the page is reloaded.
 */
function addResourcePopup() {
    let description, resourceImage, resourceName, resourceType, resourceJSON, material, drawing;
    description = document.getElementById("resourceDescription").value;
    resourceName = document.getElementById("resourceName").value;
    material = document.getElementById("material");
    drawing = document.getElementById("drawing");
    resourceImage = document.getElementById("resourceImage");
    console.log("x" + resourceImage);
    if (material.checked){
        resourceType = material.value;
        console.log("material");
    } else if (drawing.checked){
        resourceType = drawing.value;
        console.log("drawing");
    }
    console.log("type: " + resourceType);
    var fileInput = document.getElementById("resourceImage");
    var reader = new FileReader();
    reader.readAsDataURL(fileInput.files[0]);
    resourceJSON = {
        "name": resourceName,
        "description": description,
        "image": reader.result,
    };
    console.log(reader.result);
    if (resourceType === "drawing"){
        addDrawing(resourceJSON, function () {
            location.reload();
        });
    } else if (resourceType === "material"){
        addMaterial(resourceJSON, function () {
            location.reload();
        });
    }
}

/**
 * @param {number} resourceId - the ID of the resource for which the trash button was clicked.
 *
 * @summary This method is used to open the modal and set the ID of the resource to be deleted when the 'YES' button is
 * clicked in the resource deletion confirmation popup.
 */
function confirmDelete(resourceId) {
    yesBtn.setAttribute("onclick", "removeResource(" + resourceId + ")");
    deleteModal.style.display = "block";
}

/**
 * @param {number} resourceId - the ID of the resource that is to be deleted from the database.
 *
 * @summary This method is used to delete the required resource from the database and reloads the page.
 *
 * @description When the 'YES' button is clicked in the resource deletion confirmation popup, the 'removeResource'
 * function is called with the ID of the resource as a parameter so that it can be deleted from the database.
 * The ID of the resource is taken as a parameter to the 'deleteResource' function so that it can be deleted from
 * the database. If the deletion is successful, the page is reloaded.
 */
function removeResource(resourceId) {
    deleteResource(resourceId, function () {
        location.reload();
    })
}

