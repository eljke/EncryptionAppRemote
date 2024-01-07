// Получаем таблицу и текстовое поле
var table = document.getElementById('alphabetTable');
var textarea = document.getElementById('letterTextArea');
var table2 = document.getElementById('coordinateTable')
var inp2 = document.getElementById('coordinate-input')
var text2 = document.getElementById('coordinate-text')

// Добавляем обработчик событий для каждой ячейки таблицы
for (var i = 0; i < table.rows.length; i++) {
    for (var j = 0; j < table.rows[i].cells.length; j++) {
        table.rows[i].cells[j].onclick = function () {
            var cell = this; // Сохраняем ссылку на ячейку

            // Изменяем цвет фона на зеленый на 1 секунду
            cell.style.backgroundColor = '#D3FF77';
            setTimeout(function () {
                cell.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку
            }, 350);

            // Изменяем цвет текста на черный на 1 секунду
            cell.style.color = 'black';
            setTimeout(function () {
                cell.style.color = ''; // Используем сохраненную ссылку на ячейку
            }, 350);

            // Находим индексы текущей ячейки
            var rowIndex = cell.parentNode.rowIndex;
            var cellIndex = cell.cellIndex;

            if (rowIndex === table.rows.length - 1) {
                // Если ячейка в последней строке, выбираем соответствующую ячейку из первой строки
                var cellBelow = table.rows[0].cells[cellIndex];
            } else {
                // Иначе выбираем ячейку из следующей строки
                var cellBelow = table.rows[rowIndex + 1].cells[cellIndex];
            }

            // Изменяем цвет фона на голубой на 1 секунду
            cellBelow.style.backgroundColor = '#7CE8E2';
            setTimeout(function () {
                cellBelow.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку снизу
            }, 350);

            // Изменяем цвет текста на черный на 1 секунду
            cellBelow.style.color = 'black';
            setTimeout(function () {
                cellBelow.style.color = ''; // Используем сохраненную ссылку на ячейку
            }, 350);


            // Добавляем букву в текстовое поле
            textarea.value += cell.innerHTML;

            // После изменения значения вызываем функцию замены букв
            заменитьБуквы();
        };
    }
}


var table2 = document.getElementById('coordinateTable');
var inputField = document.getElementById('coordinate-input');
var coordinateText = document.getElementById('coordinate-text');

for (var i = 1; i < table2.rows.length; i++) {
    for (var j = 1; j < table2.rows[i].cells.length; j++) {
        table2.rows[i].cells[j].onclick = function () {
            var cell = this; // Сохраняем ссылку на ячейку

            // Находим индексы текущей ячейки
            var rowIndex = cell.parentNode.rowIndex;
            var cellIndex = cell.cellIndex;


                // Изменяем цвет фона на зеленый на 1 секунду
                cell.style.backgroundColor = '#D3FF77';
                setTimeout(function () {
                    cell.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                cell.style.color = 'black';
                setTimeout(function () {
                    cell.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                // Добавляем букву в текстовое поле
                inputField.value += cell.innerHTML;

                // Выбираем соответствующие ячейки из первой строки и первого столбца
                var cellInFirstRow = table2.rows[0].cells[cellIndex];
                var cellInFirstColumn = table2.rows[rowIndex].cells[0];

                // Изменяем их цвет фона на голубой на 1 секунду
                cellInFirstRow.style.backgroundColor = '#7CE8E2';
                cellInFirstColumn.style.backgroundColor = '#7CE8E2';

                cellInFirstRow.style.color = 'black';
                setTimeout(function () {
                    cellInFirstRow.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                cellInFirstColumn.style.color = 'black';
                setTimeout(function () {
                    cellInFirstColumn.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                setTimeout(function () {
                    cellInFirstRow.style.backgroundColor = ''; // Используем сохраненные ссылки на ячейки
                    cellInFirstColumn.style.backgroundColor = '';
                }, 350);

                // Вызываем функцию замены букв для обновления координат во втором поле
                заменитьДругиеБуквы();

        };
    }
}




document.getElementById("letterTextArea").oninput = function() {
    var symbol = this.value.slice(-1).toUpperCase(); // Получаем последний введенный символ

    var table = document.getElementById("alphabetTable");

    // Находим символ в таблице и эмулируем клик на соответствующей ячейке
    for (var i = 0; i < table.rows.length; i++) {
        for (var j = 0; j < table.rows[i].cells.length; j++) {
            if (table.rows[i].cells[j].innerHTML === symbol) {
                var cell = table.rows[i].cells[j];

                // Эмулируем клик на ячейке таблицы
                cell.style.backgroundColor = '#D3FF77';
                setTimeout(function () {
                    cell.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                // Изменяем цвет текста на черный на 1 секунду
                cell.style.color = 'black';
                setTimeout(function () {
                    cell.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                // Находим индексы текущей ячейки
                var rowIndex = cell.parentNode.rowIndex;
                var cellIndex = cell.cellIndex;

                if (rowIndex === table.rows.length - 1) {
                    // Если ячейка в последней строке, выбираем соответствующую ячейку из первой строки
                    var cellBelow = table.rows[0].cells[cellIndex];
                } else {
                    // Иначе выбираем ячейку из следующей строки
                    var cellBelow = table.rows[rowIndex + 1].cells[cellIndex];
                }

                // Изменяем цвет фона на голубой на 1 секунду
                cellBelow.style.backgroundColor = '#7CE8E2';
                setTimeout(function () {
                    cellBelow.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку снизу
                }, 350);

                // Изменяем цвет текста на черный на 1 секунду
                cellBelow.style.color = 'black';
                setTimeout(function () {
                    cellBelow.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);
            }
        }
    }
};

document.getElementById("coordinate-input").oninput = function() {
    var symbol = this.value.slice(-1).toUpperCase(); // Получаем последний введенный символ

    var table2 = document.getElementById("coordinateTable");

    // Находим символ в таблице и эмулируем клик на соответствующей ячейке
    for (var i = 0; i < table2.rows.length; i++) {
        for (var j = 0; j < table2.rows[i].cells.length; j++) {
            if (table2.rows[i].cells[j].innerHTML === symbol) {
                var cell = table2.rows[i].cells[j];

                // Находим индексы текущей ячейки
                var rowIndex = cell.parentNode.rowIndex;
                var cellIndex = cell.cellIndex;


                // Изменяем цвет фона на зеленый на 1 секунду
                cell.style.backgroundColor = '#D3FF77';
                setTimeout(function () {
                    cell.style.backgroundColor = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                cell.style.color = 'black';
                setTimeout(function () {
                    cell.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);


                // Выбираем соответствующие ячейки из первой строки и первого столбца
                var cellInFirstRow = table2.rows[0].cells[cellIndex];
                var cellInFirstColumn = table2.rows[rowIndex].cells[0];

                // Изменяем их цвет фона на голубой на 1 секунду
                cellInFirstRow.style.backgroundColor = '#7CE8E2';
                cellInFirstColumn.style.backgroundColor = '#7CE8E2';

                cellInFirstRow.style.color = 'black';
                setTimeout(function () {
                    cellInFirstRow.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                cellInFirstColumn.style.color = 'black';
                setTimeout(function () {
                    cellInFirstColumn.style.color = ''; // Используем сохраненную ссылку на ячейку
                }, 350);

                setTimeout(function () {
                    cellInFirstRow.style.backgroundColor = ''; // Используем сохраненные ссылки на ячейки
                    cellInFirstColumn.style.backgroundColor = '';
                }, 350);
            }
        }
    }
};

function заменитьБуквы() {
    // Получаем значение из первого поля
    let исходныйТекст = document.getElementById('letterTextArea').value.toUpperCase(); // Преобразуем текст в верхний регистр

    // Объект с соответствиями заменяемых букв
    const замены = {
        'А': 'Ё', 'Б': 'Ж', 'В': 'З', 'Г': 'И', 'Д': 'Й', 'Е': 'К', 'Ё': 'Л', 'Ж': 'М', 'З': 'Н',
        'И': 'О', 'Й': 'П', 'К': 'Р', 'Л': 'С', 'М': 'Т', 'Н': 'У', 'О': 'Ф', 'П': 'Х', 'Р': 'Ц',
        'С': 'Ч', 'Т': 'Ш', 'У': 'Щ', 'Ф': 'Ъ', 'Х': 'Ы', 'Ц': 'Ь', 'Ч': 'Э', 'Ш': 'Ю', 'Щ': 'Я',
        'Ъ': '.', 'Ы': ',', 'Ь': '-', 'Э': 'А', 'Ю': 'Б', 'Я': 'В', '.': 'Г', ',': 'Д', '-': 'Е'
    };

    let замененныйТекст = '';
    // Проходим по каждой букве в исходном тексте и заменяем, если есть соответствующая замена
    for (let буква of исходныйТекст) {
        if (замены[буква]) {
            замененныйТекст += замены[буква];
        } else {
            замененныйТекст += буква; // Если буква не нуждается в замене, оставляем как есть
        }
    }

    // Устанавливаем заменённый текст во второе текстовое поле
    document.getElementById('замененныйTextArea').value = замененныйТекст;
}

function заменитьДругиеБуквы() {
    // Получаем значение из первого поля
    let исходныйТекст = document.getElementById('coordinate-input').value.toUpperCase(); // Преобразуем текст в верхний регистр

    // Объект с соответствиями заменяемых букв
    const замены = {
        'А': '11', 'Б': '12', 'В': '13', 'Г': '14', 'Д': '15', 'Е': '16', 'Ё': '21', 'Ж': '22', 'З': '23',
        'И': '24', 'Й': '25', 'К': '26', 'Л': '31', 'М': '32', 'Н': '33', 'О': '34', 'П': '35', 'Р': '36',
        'С': '41', 'Т': '42', 'У': '43', 'Ф': '44', 'Х': '45', 'Ц': '46', 'Ч': '51', 'Ш': '52', 'Щ': '53',
        'Ъ': '54', 'Ы': '55', 'Ь': '56', 'Э': '61', 'Ю': '62', 'Я': '63', '.': '64', ',': '65', '-': '66'/* Другие замены здесь */ };

    // Проходимся по каждому символу в исходном тексте и делаем замену, если возможно
    let новыйТекст = '';
    for (let i = 0; i < исходныйТекст.length; i++) {
        let текущийСимвол = исходныйТекст[i];
        // Проверяем, есть ли символ в объекте замен
        if (замены[текущийСимвол]) {
            новыйТекст += замены[текущийСимвол];
        } else {
            новыйТекст += текущийСимвол;
        }
    }

    // Обновляем значение второго поля
    document.getElementById('coordinate-text').value = новыйТекст;
}


// Добавляем обработчик события для поля ввода
document.getElementById('letterTextArea').addEventListener('input', заменитьБуквы);
document.getElementById('coordinate-input').addEventListener('input', заменитьДругиеБуквы);


