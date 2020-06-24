/**
 * @summary This method is used to update the information on a specific resource.
 *
 * @description The values of the description, name, and image of the resource are retrieved from the web page
 * and the ID of the resource to be updated is retrieved from the URL. The image is first converted to base64 if
 * the file size is less than 1MB and if it is in PNG format. If the file is not in PNG format or if it is greater than 1MB in size,
 * an alert message is displayed to the user and the form is cleared. If the file is less than 1MB in size and is in PNG format,
 * a JSON object is created from the information retrieved and either the 'updateMaterial' or 'updateResource' function is called
 * depending on the type of the resource with this JSON object as a parameter. Once the information is updated, the user
 * is redirected to the 'resources.html' page.
 */
async function updateResourceData() {
    let resourceDescription, resourceId, resourceName, resourceType, response, file, fileSize,
        resourceJSON, image;
    resourceDescription = document.getElementById("resourceDescription").value;
    resourceId = window.location.search.split("=")[1];
    resourceName = document.getElementById("resourceName").value;

    file = document.querySelector('#resourceImage').files[0];
    fileSize = file.size / 1024 / 1024;
    if (fileSize > 1) {
        alert("File Size Exceeds 1MB- Icons should not be that big.");
        clearForm();
        return;
    }

    image = await toBase64(file);

    resourceJSON = {
        "name": resourceName,
        "resourceId": resourceId,
        "description": resourceDescription,
        "image": image,
    };

    getResource(resourceId, function () {
        response = JSON.parse(this.responseText);
        resourceType = response.type;
        if (resourceType === "material"){
            updateMaterial(resourceJSON, function () {
                window.location.href = "resources.html";
            });
        } else if (resourceType === "drawing"){
            updateDrawing(resourceJSON, function () {
                window.location.href = "resources.html";
            });
        }
    });
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