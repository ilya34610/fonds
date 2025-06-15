document.addEventListener("DOMContentLoaded", () => {
    const passwordInput = document.getElementById("password");
    const difficultyInput = document.getElementById("difficultyCheck");
    let debounceTimer = null;

    if (!passwordInput || !difficultyInput) {
        console.warn("AuthenticationController: элементы password или difficultyCheck не найдены.");
        return;
    }

    passwordInput.addEventListener("input", function() {
        const pwd = passwordInput.value;
        // Проверка длины < 1 до отправки
        if (pwd.length < 1) {
            difficultyInput.value = "";
            difficultyInput.classList.remove("text-danger", "text-warning", "text-success");
            return;
        }
        if (debounceTimer) {
            clearTimeout(debounceTimer);
        }
        debounceTimer = setTimeout(() => {
            if (!pwd) {
                difficultyInput.value = "";
                difficultyInput.classList.remove("text-danger", "text-warning", "text-success");
                return;
            }
            const headers = {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            };
            // CSRF, если нужно:
            const csrfMeta = document.querySelector('meta[name="_csrf"]');
            const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
            if (csrfMeta && csrfHeaderMeta) {
                headers[csrfHeaderMeta.getAttribute('content')] = csrfMeta.getAttribute('content');
            }
            fetch('/checkingPasswordComplexity', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ password: pwd })
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        console.error("Response error:", text);
                        throw new Error("Ошибка при запросе сложности пароля");
                    });
                }
                return response.json();
            })
            .then(code => {
                difficultyInput.classList.remove("text-danger", "text-warning", "text-success");
                if (code === 1) {
                    difficultyInput.value = "Лёгкий";
                    difficultyInput.classList.add("text-danger");
                } else if (code === 2) {
                    difficultyInput.value = "Нормальный";
                    difficultyInput.classList.add("text-warning");
                } else if (code === 3) {
                    difficultyInput.value = "Сложный";
                    difficultyInput.classList.add("text-success");
                } else {
                    difficultyInput.value = "";
                }
            })
            .catch(err => {
                console.error(err);
                difficultyInput.value = "";
                difficultyInput.classList.remove("text-danger", "text-warning", "text-success");
            });
        }, 300);
    });
});
