function validate() {
    let result = true;
    if ($("#name").val() === "") {
        $("#nameLabel").text("Имя (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#nameLabel").text("Имя").css("color", "#000000");
    }
    if ($("#password").val() === "") {
        $("#passwordLabel").text("Пароль (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#passwordLabel").text("Пароль").css("color", "#000000");
    }
    return result;
}