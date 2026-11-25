let karta;
let markorer = [];
let markorerData = [];

function visaKarta() {
    karta = L.map('karta').setView([59.33, 18.07], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: 'Kartdata från OpenStreetMap'
    }).addTo(karta);
}

/*function hamtaEvenemang() {
    const stad = document.getElementById('stad').value.trim();
    if (!stad) {
        alert('Ange en stad!');
        return;
    }

    const btn = document.getElementById('sokKnapp');
    btn.textContent = 'Söker...';
    btn.disabled = true;

    const url = `/api/v1/events/search/city?city=${encodeURIComponent(stad)}&size=30`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('HTTP-fel: ' + response.status);
            return response.json();
        })
        .then(data => {
            if (!data || data.length === 0) {
                alert('Inga evenemang hittades i ' + stad);
                visaMarkorer([]);
                return;
            }

            let evenemang = data.map(e => ({
                id: e.eventId,
                namn: e.name || 'Namnlöst',
                lat: e.latitude,
                lon: e.longitude,
                datum: e.eventDate || '2099-12-31',
                stad: e.eventCity || stad, // använd sökstaden om Ticketmaster inte ger stad
                land: e.eventcountry
            }));

            // Sortera efter datum (närmast/tidigaste först)
            evenemang.sort((a, b) => new Date(a.datum) - new Date(b.datum));

            visaMarkorer(evenemang);
        })
        .catch(error => {
            console.error('Fel:', error);
            alert('Kunde inte hämta evenemang. Se konsolen (F12).');
        })
        .finally(() => {
            btn.textContent = 'Sök';
            btn.disabled = false;
        });
}
*/
function hamtaEvenemang() {
    const stad = document.getElementById('stad').value.trim();
    if (!stad) {
        alert('Ange en stad!');
        return;
    }

    const btn = document.getElementById('sokKnapp');
    btn.textContent = 'Söker...';
    btn.disabled = true;

    // Anropa vår nya mashup-endpoint istället för den gamla sökvägen
    const url = `/api/v1/events/mashup?city=${encodeURIComponent(stad)}&size=30`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('HTTP-fel: ' + response.status);
            return response.json();
        })
        .then(data => {
            // 1. Visa stadsfakta från Wikipedia överst
            visaStadInfo(data.city, data.cityInfo);

            // 2. Mappa evenemangen och rita ut dem på kartan (data.events innehåller listan)
            const evenemang = data.events.map(e => ({
                id: e.eventId,
                namn: e.name || 'Namnlöst',
                lat: e.latitude,
                lon: e.longitude,
                datum: e.eventDate || '2099-12-31',
                stad: e.eventCity || data.city,
                land: e.eventcountry
            }));

            evenemang.sort((a, b) => new Date(a.datum) - new Date(b.datum));
            visaMarkorer(evenemang);
        })
        .catch(error => {
            console.error('Fel:', error);
            alert('Kunde inte hämta evenemang. Se konsolen (F12).');
        })
        .finally(() => {
            btn.textContent = 'Sök';
            btn.disabled = false;
        });
}

function visaMarkorer(evenemang) {
    // Rensa gamla markörer
    for (let m of markorer) {
        karta.removeLayer(m);
    }
    markorer = [];
    markorerData = [];

    const lista = document.getElementById('eventListItems');
    const container = document.getElementById('eventLista');
    lista.innerHTML = '';

    if (evenemang.length === 0) {
        container.style.display = 'none';
        return;
    }

    evenemang.forEach((e, index) => {
        if (e.lat == null || e.lon == null) return;

        // ---- Google sökning med stad ----
        const searchQuery = encodeURIComponent(`${e.namn} ${e.stad || ''} evenemang`);
        const googleLink = `https://www.google.com/search?q=${searchQuery}`;

        // ---- Skapa markör ----
        const popupHtml = `
            <b>${e.namn}</b><br>
            📅 ${e.datum}<br>
            📍 ${e.stad || ''} ${e.land || ''}<br>
            <a href="${googleLink}" target="_blank">🔍 Sök på Google</a>
        `;
        const marker = L.marker([e.lat, e.lon])
            .bindPopup(popupHtml)
            .addTo(karta);

        // Klick på markör: markera i listan men sidan SCROLLA INTE
        marker.on('click', function() {
            markeraListElement(index, false); // scroll = false
        });

        markorer.push(marker);
        markorerData.push(e);

        // ---- Skapa listelement ----
        const li = document.createElement('li');
        li.dataset.index = index;

        const textSpan = document.createElement('span');
        textSpan.className = 'event-text';
        const datumStr = e.datum ? new Date(e.datum).toLocaleDateString('sv-SE') : 'Datum saknas';
        textSpan.textContent = `${e.namn} - ${datumStr}`;

        const link = document.createElement('a');
        link.className = 'event-link';
        link.href = googleLink;
        link.target = '_blank';
        link.textContent = '🔍';
        link.title = 'Sök på Google';

        li.appendChild(textSpan);
        li.appendChild(link);

        // Klick i listan: zooma, öppna popup, markera OCH scrolla
        li.addEventListener('click', function(e) {
            if (e.target.tagName === 'A') return;
            const idx = parseInt(this.dataset.index);
            const marker = markorer[idx];
            if (marker) {
                karta.setView(marker.getLatLng(), 15);
                marker.openPopup();
                markeraListElement(idx, true); // scroll = true
            }
        });

        lista.appendChild(li);
    });

    container.style.display = 'block';

    if (evenemang.length > 0) {
        const group = L.featureGroup(markorer);
        karta.fitBounds(group.getBounds(), { padding: [50, 50] });
    }
}

// Tar emot en parameter "scroll" som avgör om vi ska scrolla till elementet
function markeraListElement(index, scroll = false) {
    const items = document.querySelectorAll('#eventListItems li');
    items.forEach((li, i) => {
        if (i === index) {
            li.classList.add('active');
            if (scroll) {
                li.scrollIntoView({ block: 'center', behavior: 'smooth' });
            }
        } else {
            li.classList.remove('active');
        }
    });
}

// ---- Event listeners ----
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('sokKnapp').addEventListener('click', hamtaEvenemang);
    document.getElementById('stad').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            hamtaEvenemang();
        }
    });
});

function visaStadInfo(stad, info) {
    const container = document.getElementById('stadInfo');
    if (!container) return;

    if (info.error) {
        container.innerHTML = `<p>ℹ Ingen information tillgänglig för ${stad}</p>`;
        container.style.display = 'block';
        return;
    }

    let html = `<h3>📌 ${stad}</h3>`;

    if (info.shortDescription) {
        html += `<p><em>${info.shortDescription}</em></p>`;
    }

    if (info.description) {
        let desc = info.description;
        if (desc.length > 300) {
            desc = desc.substring(0, 300) + '...';
        }
        html += `<p>${desc}</p>`;
    }

    if (info.image) {
        html += `<img src="${info.image}" alt="${stad}" style="max-width: 100%; max-height: 150px; border-radius: 8px; margin-top: 10px;">`;
    }

    html += `<p style="font-size: 0.8rem; color: #999;">Källa: ${info.source}</p>`;
    container.innerHTML = html;
    container.style.display = 'block';
}

window.onload = function() {
    visaKarta();
    document.getElementById('stad').value = 'Stockholm';
    hamtaEvenemang();
};