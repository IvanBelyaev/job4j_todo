function validate() {
    let result = true;
    if ($("#name").val() === "") {
        $("#nameLabel").text("Имя пользователя (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#nameLabel").text("Имя пользователя").css("color", "#000000");
    }
    if ($("#email").val() === "") {
        $("#emailLabel").text("Email (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#emailLabel").text("Email").css("color", "#000000");
    }
    if ($("#password").val() === "") {
        $("#passwordLabel").text("Пароль (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#passwordLabel").text("Пароль").css("color", "#000000");
    }
    console.log(result);
    return result;
}