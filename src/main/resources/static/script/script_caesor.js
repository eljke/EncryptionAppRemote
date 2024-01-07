function caesarCipher() {
    console.log('Функция caesarCipher сработала'); // Добавлено сообщение в консоль

    var inputText = document.getElementById('inputText').value;
    var shiftValue = parseInt(document.getElementById('shiftValue').value) || 1;

    if (isNaN(shiftValue)) {
        alert('Пожалуйста, введите корректное число для сдвига.');
        return;
    }

    var outputText = '';

    for (var i = 0; i < inputText.length; i++) {
        var char = inputText.charAt(i);
        var originalCharCode = char.charCodeAt(0);
        console.log('Исходный символ:', char, 'Код:', originalCharCode);

        var shiftedCharCode;

        if (/^[A-Za-z]$/.test(char)) {
            var isUpperCase = char === char.toUpperCase();
            shiftedCharCode = ((originalCharCode - (isUpperCase ? 65 : 97) + shiftValue + 26) % 26) + (isUpperCase ? 65 : 97);
        } else if (/^[А-Яа-я]$/.test(char)) {
            var isUpperCase = char === char.toUpperCase();
            var baseCharCode = isUpperCase ? 'А'.charCodeAt(0) : 'а'.charCodeAt(0);
            shiftedCharCode = ((originalCharCode - baseCharCode + shiftValue + 32) % 32) + baseCharCode;
        } else {
            shiftedCharCode = originalCharCode;
        }

        var shiftedChar = String.fromCharCode(shiftedCharCode);
        console.log('Шифрованный символ:', shiftedChar, 'Код:', shiftedCharCode);

        outputText += shiftedChar;
    }

    document.getElementById('outputText').value = outputText;
}

document.getElementById('inputText').addEventListener('input', caesarCipher);
document.getElementById('shiftValue').addEventListener('input', caesarCipher);

// Вызываем функцию сразу при загрузке страницы для установки значения сдвига по умолчанию
caesarCipher();
