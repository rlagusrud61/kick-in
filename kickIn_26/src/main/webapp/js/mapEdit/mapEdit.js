let map = L.map('mapid').setView([52.2413, -353.1531], 16);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);


let initMap = function() {
    let map = L.map('mapid').setView([52.2413, -353.1531], 16);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);
    return map;
}

let imgGroup = [];

let editToggle = L.easyButton({
    id: 'edit-mode-toggle',
    states: [{
        stateName: 'disable-editing',
        icon: 'fa-map-marker',
        //TODO: add an image for this icon.
        title: 'Disable Editing',
        onClick: function (control) {
            imgGroup.forEach(function (img) {
                img.editing.disable();
            });
            control.state('enable-editing');
        }
    }, {
        stateName: 'enable-editing',
        icon: 'fa-map-marker',
        //TODO: add an image for this icon.
        title: 'Enable Editing',
        onClick: function(control) {
            imgGroup.forEach(function (img) {
                img.editing.enable();
            });
            control.state('disable-editing');
        }
    }]
});
editToggle.addTo(map);

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

let inFreeHand = false;
function callEnterFreeHand() {
    if (!inFreeHand) {
        enterFreeDrawingMode();
    } else {
        exitFreeDraw();
    }
}

function enterFreeDrawingMode() {
    inFreeHand = true;
    map.dragging.disable();
    map.touchZoom.disable();
    map.doubleClickZoom.disable();
    map.scrollWheelZoom.disable();
    map.boxZoom.disable();
    map.keyboard.disable();
    map.zoomControl.disable();
    if (map.tap) map.tap.disable();
    document.getElementById('mapid').style.cursor='crosshair';
    if(editToggle.state() === 'disable-editing') {
        imgGroup.forEach(function (img) {
            img.editing.disable();
        });
    }
    editToggle.disable();

    let divContainer = document.createElement('div');
    divContainer.setAttribute('id', 'container');
    // document.getElementsByClassName('leaflet-pane leaflet-overlay-pane')[0].appendChild(divContainer);
    document.getElementById('mapContainer').appendChild(divContainer);
    let dimensionData = document.getElementById('mapContainer');

    let stage = new Konva.Stage({
        container: 'container',   // id of container <div>
        width: dimensionData.offsetWidth,
        height: dimensionData.offsetHeight,
    });
    let layer = new Konva.Layer();
    let circle = new Konva.Circle({
        x: (stage.width() / 2),
        y: (stage.height() / 2),
        radius: 70,
        fill: 'red',
        stroke: 'black',
        strokeWidth: 4,
        draggable: true,
    });
    layer.add(circle);
    stage.add(layer);
    layer.draw();
}

function exitFreeDraw() {
    let data;
    try {
        data = trimCanvas(document.getElementsByTagName('canvas')[0]);
    } catch(err) {
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
            nw = map.containerPointToLatLng([data[0], data[1]]);
            ne = map.containerPointToLatLng([data[0] + data[2], data[1]]);
            sw = map.containerPointToLatLng([data[0], data[1] + data[3]]);
            se = map.containerPointToLatLng([data[0] + data[2], data[1] + data[3]]);
            canvas = data[4];
            imgFree = canvas.toDataURL("image/png");
        } else {
            needForAdd = false;
        }
    }

    document.getElementById('container').remove();
    map.dragging.enable();
    map.touchZoom.enable();
    map.doubleClickZoom.enable();
    map.scrollWheelZoom.enable();
    map.boxZoom.enable();
    map.keyboard.enable();
    map.zoomControl.enable()
    if (map.tap) map.tap.enable();
    document.getElementById('mapid').style.cursor='grab';

    if(editToggle.state() === 'disable-editing') {
        imgGroup.forEach(function (img) {
            img.editing.enable();
        });
    }
    editToggle.enable();

    if (needForAdd) {
        let freeDrawing = L.distortableImageOverlay(imgFree, {
            corners: [nw, ne, sw, se],
            actions: [L.LockAction, L.UnlockAction, L.OpacityAction, L.DeleteAction],
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

function getGeoJSON() {
    imgGroup.forEach(function (image) {
        console.log('{corners: [' + image.getCorners() + '], url: ' + image._url + '}');
    })
}