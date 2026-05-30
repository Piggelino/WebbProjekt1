let karta;
let markorer = [];

function visaKarta() {
    karta = L.map('karta').setView([59.33, 18.07], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Kartdata från OpenStreetMap'
    }).addTo(karta);
}

function hamtaEvenemang() {
    let stad = document.getElementById('stad').value;
    if (!stad) return;
    let testEvenemang = [
        { namn: "Test 1", lat: 59.33, lon: 18.07, datum: "2025-06-15" },
        { namn: "Test 2", lat: 59.32, lon: 18.05, datum: "2025-06-16" }
    ];
    visaMarkorer(testEvenemang);
}

function visaMarkorer(evenemang) {
    for (let m of markorer) {
        karta.removeLayer(m);
    }
    markorer = [];
    for (let e of evenemang) {
        let m = L.marker([e.lat, e.lon]).bindPopup(e.namn + " " + e.datum);
        m.addTo(karta);
        markorer.push(m);
    }
}

window.onload = function() {
    visaKarta();
    document.getElementById('stad').value = "Stockholm";
    hamtaEvenemang();
};