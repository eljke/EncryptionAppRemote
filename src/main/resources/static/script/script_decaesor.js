
function getBarColors(data) {
    const colors = [];

    // Сортируем индексы букв в порядке убывания частоты
    const sortedIndices = data.map((value, index) => index).sort((a, b) => data[b] - data[a]);

    for (let i = 0; i < data.length; i++) {
        if (i < 3) {
            colors[sortedIndices[i]] = '#FF77F1'; // Три самые частые буквы
        } else if (i < 6) {
            colors[sortedIndices[i]] = '#7CE8E2'; // Следующие три буквы
        } else if (i < 10) {
            colors[sortedIndices[i]] = '#D3FF77'; // Следующие четыре буквы
        } else {
            colors[sortedIndices[i]] = '#D9D9D9'; // Все остальные буквы
        }
    }

    return colors;
}


let myChart;
// Функция для построения графика на основе текста из поля ввода
function buildChart() {
    const inputText = document.getElementById('inputText').value;
    const russianAlphabet = 'абвгдеёжзийклмнопрстуфхцчшщъыьэюя';
    const letterFrequency = {};

    // Подсчитываем частоту каждой буквы
    for (let i = 0; i < inputText.length; i++) {
        const letter = inputText[i].toLowerCase();
        if (russianAlphabet.includes(letter)) {
            if (letterFrequency[letter]) {
                letterFrequency[letter]++;
            } else {
                letterFrequency[letter] = 1;
            }
        }
    }

    // Преобразуем объект частот в массив данных для графика
    const labels = Object.keys(letterFrequency);
    const data = labels.map(letter => letterFrequency[letter]);

    if (myChart) {
        myChart.destroy(); // Уничтожаем предыдущий график, если он существует
    }

    // Создаем график с помощью Chart.js
    const ctx = document.getElementById('myChart').getContext('2d');

    const barColors = getBarColors(data);

    myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Частота букв в тексте',
                data: data,
                backgroundColor: barColors,
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Вызываем функцию buildChart() при изменении значения в поле ввода
document.getElementById('inputText').addEventListener('input', buildChart);

// Инициализируем график
buildChart();

let number = 0;

function changeTextPlus() {
    number++;
    document.getElementById('numberDisplay').innerText = number;

    var cipherText = document.getElementById('cipherText').textContent;
    var decryptedText = '';

    for (var i = 0; i < cipherText.length; i++) {
        var charCode = cipherText.charCodeAt(i);

        // Если символ - буква русского алфавита, смещаем на одну позицию вперед
        if ((charCode >= 1040 && charCode <= 1071) || (charCode >= 1072 && charCode <= 1103)) {
            charCode = (charCode === 1071) ? 1040 : // Я -> А
                (charCode === 1103) ? 1072 : // я -> а
                    charCode + 1;
        }

        decryptedText += String.fromCharCode(charCode);
    }

    document.getElementById('cipherText').textContent = decryptedText;
}

function decrypt() {
    number--;
    document.getElementById('numberDisplay').innerText = number;

    var cipherText = document.getElementById('cipherText').textContent;
    var decryptedText = '';

    for (var i = 0; i < cipherText.length; i++) {
        var charCode = cipherText.charCodeAt(i);

        // Если символ - буква русского алфавита, смещаем на одну позицию назад
        if ((charCode >= 1040 && charCode <= 1071) || (charCode >= 1072 && charCode <= 1103)) {
            charCode = (charCode === 1040) ? 1071 : // А -> Я
                (charCode === 1072) ? 1103 : // а -> я
                    charCode - 1;
        }

        decryptedText += String.fromCharCode(charCode);
    }

    document.getElementById('cipherText').textContent = decryptedText;
}