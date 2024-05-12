$(document).ready(function () {
    CategoryController.load();
});

const CategoryController = {

    _categoryForm: $("#categoryForm"),

    _submitButton: $("#submit"),

    _errorHead: $("#errorHead"),
    _errorText: $("#errorText"),
    _errorModal: $("#errorModal"),

    load: function () {
        let self = this;

        self._categoryForm.on("submit", function (e) {
            e.preventDefault();
            self._submitButton.attr("disabled", true);
            let url = "/categories";
            let type = "post";
            if (categoryId != null) {
                url = url + '/' + categoryId;
                type = "put";
            }
            $.ajax({
                type: type,
                url: url,
                data: JSON.stringify({
                    id: $("#categoryIdInput").val(),
                    name: $("#nameInput").val()
                }),
                contentType: "application/json",
                success: function(response) {
                    console.log("Success:", response);
                    window.location = "/categories";
                },
                error: function(error) {
                    console.error("Error:", error);
                }
            });
        });
    }
};