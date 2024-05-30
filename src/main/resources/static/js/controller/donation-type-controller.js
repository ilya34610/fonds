$(document).ready(function () {
    DonationTypeController.load();
});

const DonationTypeController = {

    _donationTypeForm: $("#donationTypeForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._donationTypeForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/donation_types";
            let type = "post";
            if (donationTypeId != null) {
                url = url + '/' + donationTypeId;
                type = "put";
            }
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#donationTypeIdInput").val(),
                    name: $("#nameInput").val()
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/donation_types";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};

           // Получение CSRF токена из мета-тегов
//        let csrfToken = $("meta[name='_csrf']").attr("content");
//        let csrfHeader = $("meta[name='_csrf_header']").attr("content");


//                beforeSend: function (xhr) {
//                    // Добавляем CSRF токен в заголовок
//                    xhr.setRequestHeader(csrfHeader, csrfToken);
//                },