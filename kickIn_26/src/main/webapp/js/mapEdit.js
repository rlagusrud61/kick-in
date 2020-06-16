//inits the map and properties.
let map = L.map('mapid', {
    center: [52.2413, -353.1531],
    zoom: 16,
    keyboard: false
});

let tiles = L.tileLayer(
    'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
tiles.addTo(map);

var printer = L.easyPrint({
    tileLayer: tiles,
    sizeModes: ['Current', 'A4Landscape', 'A4Portrait'],
    filename: 'myMap',
    exportOnly: true,
    hideControlContainer: true
}).addTo(map);

let editToggle = L.easyButton({
    id: 'edit-mode-toggle',
    states: [{
        stateName: 'disable-editing',
        icon: 'fa-edit',
        title: 'Disable Editing',
        onClick: function (control) {
            imgGroup.forEach(function (img) {
                img.editing.disable();
            });
            control.state('enable-editing');
        }
    }, {
        stateName: 'enable-editing',
        icon: 'fa-window-close',
        title: 'Enable Editing',
        onClick: function (control) {
            imgGroup.forEach(function (img) {
                img.editing.enable();
            });
            control.state('disable-editing');
        }
    }]
});
editToggle.addTo(map);

//the array in which all the images added are stored
let imgGroup = [];
let resourceArray = [];
//boolean to check weather free hand drawing is being done.
let inFreeHand = false;

let materialsList = null;

let objReq = new XMLHttpRequest();
objReq.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
        materialsList = JSON.parse(objReq.responseText);
        console.log(materialsList);
        itemlist = document.getElementById("itemlist");
        var htmltext = ""
//				<label><li>Item 1 :</li></label>
//				<input id="input" type="number" name="quantity">
//				<button style="background-color: #58BD0F; border-color: #C1FF94">+</button>
//				<br>
//				<br>
        for (let i = 0; i < materialsList.length; i++) {
            htmltext += "<label><li>Item " + materialsList[i].resourceId + ":</li></label> ";
            htmltext += "<input id ='input" + materialsList[i].resourceId + "' type = 'number' name = 'quantity'> "
            htmltext += "<button onclick ='addItem2(" + materialsList[i].resourceId + ")' style='background-color: #58BD0F; border-color: #C1FF94'>+</button> "
            htmltext += "<br><br>";

        }
        itemlist.innerHTML = htmltext;
    } else {
        console.log("failure")
    }
}
objReq.open("GET", ("http://localhost:8080/kickInTeam26/rest/materials"));
objReq.send();
var jsonObj = {
    items: {}
}

function addItem2(inum) {
    itemnum = jsonObj.items["item" + inum];
    inputvalue = document.getElementById("input" + inum).value
    inputvalue = Number(inputvalue);
    itemlist2 = document.getElementById("itemlist2");
    if (itemnum == null) {
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
    itemlist2.innerHTML = htmltext;
    console.log(jsonObj.items);
}

let img = L.distortableImageOverlay('..\\resources\\testImages\\example.jpg', {
    actions: [L.RotateAction, L.DragAction, L.ScaleAction, L.DeleteAction]
}).addTo(map);
imgGroup.push(img);
img.on('remove', function() {
    const index = imgGroup.indexOf(img);
    if (index !== -1) {
        imgGroup.splice(index, 1);
    }
});
//function to enter free hand, if already in free hand, we exit free hand drawing.
function callEnterFreeHand() {
	if (!inFreeHand) {
		enterFreeDrawingMode();
	} else {
		exitFreeDraw();
	}
}

//enter free hand drawing.
function enterFreeDrawingMode() {
	inFreeHand = true;
	map.dragging.disable();
	map.touchZoom.disable();
	map.doubleClickZoom.disable();
	map.scrollWheelZoom.disable();
	map.boxZoom.disable();
	map.keyboard.disable();
	map.zoomControl.disable();
	if (map.tap)
		map.tap.disable();
	document.getElementById('mapid').style.cursor = 'crosshair';
	if (editToggle.state() === 'disable-editing') {
		imgGroup.forEach(function(img) {
			img.editing.disable();
		});
	}
	editToggle.disable();

	let divContainer = document.createElement('div');
	divContainer.setAttribute('id', 'freeDrawContainer');
	// document.getElementsByClassName('leaflet-pane leaflet-overlay-pane')[0].appendChild(divContainer);
	document.getElementById('mapContainer').appendChild(divContainer);
	let dimensionData = document.getElementById('mapContainer');

	let stage = new Konva.Stage({
		container : 'freeDrawContainer', // id of container <div>
		width : dimensionData.offsetWidth,
		height : dimensionData.offsetHeight,
	});
	let layer = new Konva.Layer();
	let circle = new Konva.Circle({
		x : (stage.width() / 2),
		y : (stage.height() / 2),
		radius : 70,
		fill : 'red',
		stroke : 'black',
		strokeWidth : 4,
		draggable : true,
	});
	layer.add(circle);
	stage.add(layer);
	layer.draw();
}

//exit free hand drawing.
function exitFreeDraw() {
	let data;
	try {
		data = trimCanvas(document.getElementsByTagName('canvas')[0]);
	} catch (err) {
		data = null;
	}

	let needForAdd = true;
	let canvas = null;
	let imgFree = null;
	let nw, ne, sw, se = null;

	if (data !== null && (data[2] === 0 || data[3] === 0)) {
		needForAdd = false;
	} else {
		if (data !== null) {
			nw = map.containerPointToLatLng([ data[0], data[1] ]);
			ne = map.containerPointToLatLng([ data[0] + data[2], data[1] ]);
			sw = map.containerPointToLatLng([ data[0], data[1] + data[3] ]);
			se = map.containerPointToLatLng([ data[0] + data[2],
					data[1] + data[3] ]);
			canvas = data[4];
			imgFree = canvas.toDataURL("image/png");
		} else {
			needForAdd = false;
		}
	}

	document.getElementById('freeDrawContainer').remove();
	map.dragging.enable();
	map.touchZoom.enable();
	map.doubleClickZoom.enable();
	map.scrollWheelZoom.enable();
	map.boxZoom.enable();
	map.keyboard.enable();
	map.zoomControl.enable()
	if (map.tap)
		map.tap.enable();
	document.getElementById('mapid').style.cursor = 'grab';

	if (editToggle.state() === 'disable-editing') {
		imgGroup.forEach(function(img) {
			img.editing.enable();
		});
	}
	editToggle.enable();

	if (needForAdd) {
		let freeDrawing = L.distortableImageOverlay(
				imgFree,
				{
					corners : [ nw, ne, sw, se ],
					actions : [ L.LockAction, L.UnlockAction,
							L.OpacityAction, L.DeleteAction ],
				}).addTo(map);
		imgGroup.push(freeDrawing);
		freeDrawing.on('remove', function() {
			const index = imgGroup.indexOf(freeDrawing);
			if (index !== -1) {
				imgGroup.splice(index, 1);
			}
		});
	}
	inFreeHand = false;
}

function addItem(resourceId) {
    let count = document.getElementById(resourceId.toString()).value;

    for (let i = 0; i < count; i++) {
        let img = L.distortableImageOverlay("data:image/png;utf-8;base64," + materialsList[resourceId].image, {
            actions: [L.RotateAction, L.DragAction, L.ScaleAction, L.DeleteAction]
        }).addTo(map);

        imgGroup.push(img);
        resourceArray.push(materialsList[resourceId]);

        const index = imgGroup.indexOf(img);
        img.on('remove', function () {

            if (index !== -1) {
                imgGroup.splice(index, 1);
                resourceArray.splice(index, 1);
            }
        });
    }
}

function save() {
    let save = new XMLHttpRequest();
    save.open("POST", "http://localhost:8080/kickInTeam26/rest/objects/")
    save.setRequestHeader("Content-Type", "application/json");
    save.send(constructSave())
}

function constructSave() {
    let string = "[";
    for (let i = 0; i < imgGroup.length; i++) {
        if (i !== 0) {
            string += ",";
        }
        string += '{ "mapid":"' + mapId + '", "resourceId":"' + resourceArray[i].resourceId + '", "latlangs":"' + '[' + imgGroup[i].getCorners() + ']" }';
    }
    string += "]";
    return string;
}

function retrieve() {
    let mapState = new XMLHttpRequest();
    mapState.open("GET", "http://localhost:8080/kickInTeam26/rest/objects/" + id);
}

//get the geoJSON data that can be used to reconstruct the map if needed.
function getGeoJSON() {
    imgGroup.forEach(function (image) {
        console.log('{corners: [' + image.getCorners() + '], url: '
            + image._url + '}');
    })
}

//for filtering materials depending on entered text
function filterOn() {

}

function XSSInputSanitation(id) {
    let element = document.getElementById(id).value;
    if (element.indexOf("onload") !== -1 || element.indexOf("<script>") !== -1 ||
        element.indexOf("onerror") !== -1 || element.indexOf("alert") !== -1) {
        console.log("done")
        document.getElementById(id).value = "";
    }
}