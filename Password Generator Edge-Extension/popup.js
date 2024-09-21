function generateRandomNumberSecure(max) {
    const array = new Uint32Array(1);
    window.crypto.getRandomValues(array);
    return array[0] % max;
}

function generatePassword(length, includeSymbols, includeNumbers, includeUppercase, useCrypto) {
    const lowercase = "abcdefghijklmnopqrstuvwxyz";
    const uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const numbers = "0123456789";
    const symbols = "!@#$%^&*()_+~`|}{[]:;?><,./-=";

    let characterPool = lowercase;
    if (includeSymbols) characterPool += symbols;
    if (includeNumbers) characterPool += numbers;
    if (includeUppercase) characterPool += uppercase;

    let password = "";
    for (let i = 0; i < length; i++) {
        const randomIndex = useCrypto ? generateRandomNumberSecure(characterPool.length) : Math.floor(Math.random() * characterPool.length);
        password += characterPool[randomIndex];
    }

    return password;
}

document.getElementById("generate").addEventListener("click", () => {
    const length = document.getElementById("length").value;
    const includeSymbols = document.getElementById("includeSymbols").checked;
    const includeNumbers = document.getElementById("includeNumbers").checked;
    const includeUppercase = document.getElementById("includeUppercase").checked;
    const useCrypto = document.getElementById("useCrypto").checked;

    const password = generatePassword(length, includeSymbols, includeNumbers, includeUppercase, useCrypto);
    document.getElementById("passwordOutput").value = password;
});

document.getElementById("copy").addEventListener("click", () => {
    const password = document.getElementById("passwordOutput").value;
    navigator.clipboard.writeText(password);
    alert("Password copied to clipboard!");
});
