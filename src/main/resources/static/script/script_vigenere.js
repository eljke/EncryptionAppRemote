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
                imageContainer.innerHTML = '<iframe src="https://vk.com/video_ext.php?oid=-224157165&id=456239019&hash=cc33ba7ac61f7cbc&autoplay=1" width="748" height="748" frameborder="0" allowfullscreen="allowfullscreen" allow="autoplay; encrypted-media; fullscreen; picture-in-picture"></iframe>';
            } else if (index === 2) {
                imageContainer.innerHTML = '<iframe src="https://vk.com/video_ext.php?oid=-224157165&id=456239017&hash=54f8dbabe7302001&autoplay=1" width="748" height="748" frameborder="0" allowfullscreen="allowfullscreen" allow="autoplay; encrypted-media; fullscreen; picture-in-picture"></iframe>';
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







