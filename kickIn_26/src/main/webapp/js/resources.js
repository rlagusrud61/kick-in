let resources, modal, imageModal, btn, span, yesBtn, deleteModal, close;

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
//init resources
resources = new Map();

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
function loadTable() {
    let header, tr, th, i, table, response, row, name, description, type, action;
    getAllResources(function () {
        table = document.getElementById("resourceTable");
        response = JSON.parse(this.responseText);
        response.forEach(function (resource) {
            resources.set(resource.resourceId, resource)
        });

        header = [];
        header.push('Name');
        header.push('Description');
        header.push('Type');
        header.push('Action');

        tr = table.insertRow(-1); // add a row to the table
        for (i = 0; i < header.length; i++) {
            th = document.createElement("th"); // add a header to the table
            th.innerHTML = header[i];
            tr.appendChild(th);
        }

        for (i = 0; i < response.length; i++) {
            row = table.insertRow(-1);
            name = row.insertCell(0);
            description = row.insertCell(1);
            type = row.insertCell(2);
            action = row.insertCell(3);
            name.innerHTML = response[i].name;
            description.innerHTML = response[i].description;
            type.innerHTML = response[i].type;
            action.innerHTML = "<a href='javascript: window.viewResource(" + response[i].resourceId + ")' class='text-success'>" +
                "<i class='glyphicon glyphicon-eye-open' style='font-size:15px;'></i></a><a href='editResource.html?id=" +
                response[i].resourceId + "' class='text-success'><i class='glyphicon glyphicon-pencil' " +
                "style='font-size:15px;'></i></a><a href='javascript: window.confirmDelete(" +
                response[i].resourceId + ")'" + "class='text-success'><i class='glyphicon glyphicon-trash' style='font-size:15px;'></i></a>";
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
    let resourcePicture;
    resourcePicture = document.getElementById("resourcePicture");
    resourcePicture.setAttribute("src", resources.get(resourceId).image);
    imageModal.style.display = "block";
}

/**
 * @summary This method is used to add a new resource to the database.
 *
 * @description The name, description and image of the resource is retrieved from the popup. The image is first converted to base64 if
 * the file size is less than 1MB and if it is in PNG format. If the file is not in PNG format or if it is greater than 1MB in size,
 * an alert message is displayed to the user and the form is cleared. If the file is less than 1MB in size and is in PNG format,
 * a JSON object is created from the information retrieved and either the 'addMaterial' or 'addResource' function is called
 * depending on the type of the resource with this JSON object as a parameter. If the resource was successfully added to
 * the database, the page is reloaded.
 */
async function addResourcePopup() {
    let description, resourceName, resourceType, resourceJSON, material, drawing, file, fileSize, image;
    description = document.getElementById("resourceDescription").value;
    resourceName = document.getElementById("resourceName").value;
    material = document.getElementById("material");
    drawing = document.getElementById("drawing");
    if (material.checked){
        resourceType = material.value;
    } else if (drawing.checked){
        resourceType = drawing.value;
    }

    file = document.querySelector('#resourceImage').files[0];
    fileSize = file.size / 1024 / 1024;
    if (fileSize > 1) {
        alert("File Size Exceeds 1MB- Icons should not be that big.");
        clearForm();
        return;
    } else if (file.name.split(".")[1] !== "png") {
        alert("Only png is supported due to smaller size.")
        clearForm();
        return;
    }

    image =  await toBase64(file);
    resourceJSON = {
        "name": resourceName,
        "description": description,
        "image": image,
    };
    if (resourceType === "drawing"){
        addDrawing(resourceJSON, function () {
            location.reload();
        });
    } else if (resourceType === "material") {
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

/**
 *
 * @param {Blob} file - The image to be converted to base64.
 *
 * @summary This method is used to convert the file taken as a parameter into base64.
 *
 * @returns {Promise<unknown>} - The image in base64.
 */
const toBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
});

/**
 * @summary This method clears the form for adding a new resource to the database.
 */
function clearForm() {
    document.getElementById("resourceImage").value = '';
}