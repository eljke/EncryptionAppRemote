const currentTime = new Date().getHours();

let greeting;
if (currentTime > 5 && currentTime < 12) {
    greeting = "Доброе утро";
} else if (currentTime > 11 && currentTime < 18) {
    greeting = "Добрый день";
} else if (currentTime > 17 && currentTime < 24) {
    greeting = "Добрый вечер";
} else {
    greeting = "Доброй ночи";
}

document.getElementById("greeting").innerText = greeting;
document.getElementById("greeting-container").style.visibility = 'visible';