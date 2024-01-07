function changeOpacity(element) {
    const paragraphs = document.querySelectorAll('.semi-transparent');
    const imageContainer = document.querySelector('.image-container');

    paragraphs.forEach((paragraph, index) => {
        if (paragraph === element) {
            paragraph.style.opacity = 1;
            // Проверяем индекс выделенного параграфа и устанавливаем соответствующее содержимое в imageContainer
            if (index === 0) {
                imageContainer.innerHTML = '<img src="../images/vyzhener_table.png" alt="Image">';
            } else if (index === 1) {
                imageContainer.innerHTML = '<video loop muted controls style="width: 100%; height: auto;" autoplay="autoplay"><source src="../videos/Key_norm.mp4" type="video/mp4"></video>';
            } else if (index === 2) {
                imageContainer.innerHTML = '<video loop muted controls style="width: 100%; height: auto;" autoplay="autoplay"><source src="../videos/Vuzhiner.mp4" type="video/mp4"></video>';
            }
        } else {
            paragraph.style.opacity = 0.4;
        }
    });
}

document.getElementById('inputText').addEventListener('input', function() {
    var inputText = this.value;
    var tableRow = document.getElementById('tableRow');
    var secondRow = document.getElementById('secondRow');

    updateRow(tableRow, inputText); // Обновляем первую строку

    // Повторяем символы во второй строке до равенства длин
    updateSecondRow(secondRow, document.getElementById('shiftValue').value, inputText.length);
});

document.getElementById('shiftValue').addEventListener('input', function() {
    var shiftValue = this.value;
    var secondRow = document.getElementById('secondRow');
    var tableRow = document.getElementById('tableRow');

    // Обновляем вторую строку с учетом нового ввода во втором поле
    updateSecondRow(secondRow, shiftValue, tableRow.cells.length);
});

function updateRow(row, text) {
    row.innerHTML = '';
    for (var i = 0; i < text.length; i++) {
        var cell = document.createElement('td');
        cell.appendChild(document.createTextNode(text[i]));
        row.appendChild(cell);
    }
}

function updateSecondRow(row, text, targetLength) {
    row.innerHTML = '';
    var index = 0;

    // Проверяем, что переменная text не пустая и не undefined
    if (text && text.trim().length > 0) {
        while (row.cells.length < targetLength) {
            var cell = document.createElement('td');
            cell.appendChild(document.createTextNode(text[index % text.length]));
            row.appendChild(cell);
            index++;
        }
    } else {
        // Если переменная text пустая или undefined, оставляем ячейки пустыми
        for (var i = 0; i < targetLength; i++) {
            var emptyCell = document.createElement('td');
            row.appendChild(emptyCell);
        }
    }
}







