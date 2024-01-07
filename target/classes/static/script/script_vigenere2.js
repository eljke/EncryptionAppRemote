// Получение текста из инпута
const inputText = document.getElementById('inputText');
// Получение ключевого слова из инпута
const shiftValue = document.getElementById('shiftValue');

// Функция для шифрования текста шифром Виженера
function vigenereCipher(text, key) {
    let result = '';
    const alphabetEng = 'abcdefghijklmnopqrstuvwxyz';
    const alphabetRus = 'абвгдеёжзийклмнопрстуфхцчшщъыьэюя';

    for (let i = 0; i < text.length; i++) {
        const char = text[i].toLowerCase();
        const keyChar = key[i % key.length].toLowerCase();

        let alphabet;
        if (alphabetEng.includes(char)) {
            alphabet = alphabetEng;
        } else if (alphabetRus.includes(char)) {
            alphabet = alphabetRus;
        } else {
            result += char; // Символ не из алфавита, оставляем как есть
            continue;
        }

        const charIndex = alphabet.indexOf(char);
        const keyIndex = alphabet.indexOf(keyChar);
        const shiftedIndex = (charIndex + keyIndex) % alphabet.length;

        const encryptedChar = alphabet[shiftedIndex];
        // Сохраняем регистр символов
        result += text[i] === text[i].toUpperCase() ? encryptedChar.toUpperCase() : encryptedChar;
    }

    return result;
}

// Функция для обработки введенного текста и ключа и вывода результата
function encryptText() {
    const text = inputText.value;
    const key = shiftValue.value;

    const encryptedText = vigenereCipher(text, key);

    // Вывод зашифрованного текста в текстовое поле
    document.getElementById('outputText').value = encryptedText;
}

// Вызов функции шифрования при изменении текста или ключа
inputText.addEventListener('input', encryptText);
shiftValue.addEventListener('input', encryptText);

// Вызов функции шифрования для инициализации результата при загрузке страницы
encryptText();