function initMap() {
    let newMap = L.map('mapid', {
        center: [52.2413, -353.1531],
        zoom: 16,
        keyboard: false
    });
    let tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    });
    tiles.addTo(newMap);
    return newMap;
}