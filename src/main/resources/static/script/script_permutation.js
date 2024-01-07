const inputText = document.getElementById("inputText");
const outputText = document.getElementById("outputText");
const inputRows = document.getElementById("inputRows");
const inputCols = document.getElementById("inputCols");
const table = document.getElementById("table");
const animationDelay = 450;

inputText.addEventListener("input", () => {
    redrawTable();
    insertLetters();
    encode();

    currentRow = 0;
    currentCol = 0;

    if (isAnimating) {
        return;
    }

    if (animationTimeout) {
        clearTimeout(animationTimeout);
        animationTimeout = null;
    }

    isAnimating = true;
    animateTraversal();
});

inputRows.addEventListener("input", function() {
    const value = Number(this.value);
    if (value > 15) {
        this.value = 15;
    }
});

inputCols.addEventListener("input", function() {
    const value = Number(this.value);
    if (value > 15) {
        this.value = 15;
    }
});

inputRows.addEventListener("change", () => {
    const value = Number(this.value);
    if (value > 15) {
        this.value = 15;
    }
    redrawTable();
    insertLetters();
    encode();
});

inputCols.addEventListener("change", () => {
    const value = Number(this.value);
    if (value > 15) {
        this.value = 15;
    }
    redrawTable();
    insertLetters();
    encode();
});


window.onload = () => {
    redrawTable();
    insertLetters();
};

function encode() {
    const inputRowsValue = parseInt(inputRows.value);
    const inputColsValue = parseInt(inputCols.value);

    if (inputRowsValue <= 0 || inputColsValue <= 0) {
        alert("Размеры таблицы должны быть больше 0");
        return;
    }

    let text = inputText.value;

    if (text === "") {
        outputText.value = "";
        return;
    }

    let table = [];
    for (let i = 0; i < inputRowsValue; i++) {
        table.push(new Array(inputColsValue).fill("%"));
    }

    if (text.length > inputRowsValue * inputColsValue) {
        alert("Текст слишком большой для таблицы");
        return;
    }

    let index = 0;
    for (let i = 0; i < inputRowsValue; i++) {
        for (let j = 0; j < inputColsValue; j++) {
            if (index < text.length) {
                table[i][j] = text[index];
                index++;
            }
        }
    }

    let encoded = "";
    for (let j = 0; j < inputColsValue; j++) {
        for (let i = 0; i < inputRowsValue; i++) {
            encoded += table[i][j];
        }
    }

    outputText.value = encoded;
}

function redrawTable() {
    table.innerHTML = "";

    const rows = parseInt(inputRows.value);
    const cols = parseInt(inputCols.value);

    const cellPadding = 15 - rows;
    const cellFontSize = 48 - 2 * rows;

    for (let i = 0; i < rows; i++) {
        const row = document.createElement("tr");
        for (let j = 0; j < cols; j++) {
            const cell = document.createElement("td");
            cell.textContent = "%";
            cell.id = `cell-${i}-${j}`;
            cell.style.padding = `${cellPadding}px`;
            cell.style.fontSize = `${cellFontSize}px`;
            row.appendChild(cell);
        }
        table.appendChild(row);
    }
}

let currentRow = 0;
let currentCol = 0;

let isAnimating = false;
let animationTimeout;
let prevCells = [];

function animateTraversal() {
    if (currentCol >= inputCols.value && currentRow >= inputRows.value) {
        currentRow = 0;
        currentCol = 0;
        isAnimating = false;
        return;
    }

    const text = inputText.value;

    const cell = document.getElementById(`cell-${currentRow}-${currentCol}`);
    prevCells.push(cell);

    prevCells.forEach(c => {
        if (!c.classList.contains('cell-gradient-highlight')) {
            c.classList.add('cell-gradient-highlight');
        }
    });

    const index = currentRow * inputCols.value + currentCol;

    if (index < text.length) {
        cell.textContent = text[index];

    } else {
        drawSymbol(currentRow, currentCol);
    }

    currentRow++;

    if (currentRow >= inputRows.value) {
        currentRow = 0;
        currentCol++;
    }

    if (currentCol < inputCols.value) {
        setTimeout(animateTraversal, animationDelay);
    } else {
        currentRow = 0;
        currentCol = 0;
        setTimeout(animateTraversal, animationDelay * 4);
        setTimeout(resetGradientHighlight, animationDelay * 2);
    }
}

function insertLetters() {
    const text = inputText.value;

    for (let i = 0; i < inputRows.value; i++) {
        for (let j = 0; j < inputCols.value; j++) {
            const cell = document.getElementById(`cell-${i}-${j}`);
            const index = i * inputCols.value + j;

            if (index < text.length) {
                cell.textContent = text[index];
            } else {
                cell.textContent = "%";
            }
        }
    }
}

function drawSymbol(row, col) {
    const cell = document.getElementById(`cell-${row}-${col}`);
    cell.textContent = "%";
}

function resetGradientHighlight() {
    prevCells.forEach(c => c.classList.remove('cell-gradient-highlight'));
    prevCells = [];
}