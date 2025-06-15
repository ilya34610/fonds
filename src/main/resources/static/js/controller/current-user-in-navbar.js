document.addEventListener("DOMContentLoaded", () => {
  fetch('/api/currentUser')
    .then(res => {
      if (res.status === 200) return res.json();
      throw new Error('Not authenticated');
    })
    .then(user => {
      const el = document.getElementById('navbar-username');
      if (el) el.textContent = user.login;
    })
    .catch(() => {
      const el = document.getElementById('navbar-login');
      if (el) el.style.display = 'block';
    });
});


document.addEventListener('DOMContentLoaded', function() {
    const qrTrigger = document.getElementById('login-by-qr-code');

    qrTrigger.addEventListener('click', async function() {
        const originalContent = this.innerHTML;

        try {
            // Показываем индикатор загрузки
            this.innerHTML = `
                <span class="me-2 navbar-text">Генерация QR</span>
                <span class="spinner-border spinner-border-sm"></span>
            `;

            // Отправляем запрос на генерацию QR-кода
            const response = await fetch('/api/qr-login/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'include' // Важно для передачи сессии
            });

            if (!response.ok) {
                throw new Error(`Ошибка ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();

            // Устанавливаем данные в модальное окно
            document.getElementById('qrImage').src = `data:image/png;base64,${data.qrImageBase64}`;
            document.getElementById('loginUrl').href = data.loginUrl;
            document.getElementById('loginUrl').textContent = data.loginUrl;

            // Показываем модальное окно
            const modal = new bootstrap.Modal(document.getElementById('qrModal'));
            modal.show();

        } catch (error) {
            console.error('Ошибка:', error);
            alert('Не удалось сгенерировать QR-код: ' + error.message);
        } finally {
            // Восстанавливаем исходный вид
            this.innerHTML = originalContent;
        }
    });
});