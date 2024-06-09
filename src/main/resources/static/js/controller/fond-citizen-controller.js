//было перенесено в саму html

document.addEventListener("DOMContentLoaded", function() {
//    const selectAllCheckbox = document.getElementById("selectAll");
    const rowCheckboxes = document.querySelectorAll(".rowCheckbox");

    // Обработчик для выбора всех элементов
    selectAllCheckbox.addEventListener("change", function() {
        rowCheckboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });

    // Функция для получения выбранных элементов
    function getSelectedRows() {
        let selectedRows = [];
        rowCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                selectedRows.push(checkbox.value);
            }
        });
        return selectedRows;
    }

    // Пример функции для отправки выбранных элементов на сервер
    function sendSelectedRows() {
        const selectedRows = getSelectedRows();
        if (selectedRows.length === 0) {
            alert("Пожалуйста, выберите хотя бы один элемент.");
            return;
        }

        fetch("/your-api-endpoint", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ selectedIds: selectedRows })
        })
        .then(response => response.json())
        .then(data => {
            console.log("Response from server:", data);
        })
        .catch(error => {
            console.error("Error:", error);
        });
    }

    // Пример привязки функции отправки к кнопке
    document.getElementById("sendButton").addEventListener("click", sendSelectedRows);
});

