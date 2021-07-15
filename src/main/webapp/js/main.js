let add = function(){
    if (validate()) {
        let taskName = $('#description').val();
        let categories = $('#categories').val();
        addTask(0, taskName, "", false, categories);
    }
};

let validate = function () {
    let result = true;
    if ($("#description").val() === "") {
        $("#descriptionLabel").text("Новое задание (заполните поле)").css("color", "#740000");
        result = false;
    } else {
        $("#descriptionLabel").text("Новое задание").css("color", "#000000");
    }
    if ($("#categories").val().length === 0) {
        $("#categoriesLabel").text("Категории (не выбрано)").css("color", "#740000");
        result = false;
    } else {
        $("#categoriesLabel").text("Категории").css("color", "#000000");
    }
    return result;
}

let makeJson = function (id, taskName, created, isDone, categories) {
    let item = {};
    item["id"] = Number(id);
    item["description"] = taskName;
    item["created"] = created;
    item["done"] = isDone;
    item["categories"] = categories;
    return JSON.stringify(item);
};

let addTask = function (id, taskName, created, isDone, categories) {
    let json = makeJson(id, taskName, created, isDone, categories);
    $.ajax({
        type: 'POST',
        url: './add.do',
        data: 'item=' + json,
        dataType: 'json'
    }).done(function (data) {
        let item = data.item;
        addToFront(item.id, item.description, item.created, JSON.parse(item.done), item.user.name, item.categories);
    }).fail(function (err) {

    });
}

let addToFront = function (id, taskName, created, done, username, categories) {
    let doneSpan = $("<span></span>").addClass("col-1").append("<input type='checkbox'>");
    let idSpan = $("<span></span>").addClass("col-1").text(id);
    let descSpan = $("<span></span>").addClass("col-4").text(taskName);
    let namesOfCategories = "";
    for (let i = 0; i < categories.length; i++) {
        namesOfCategories += categories[i].name;
        if (i < categories.length - 1) {
            namesOfCategories += ", ";
        }
    }
    let categorySpan = $("<span></span>").addClass("col-2").text(namesOfCategories);
    let createdSpan = $("<span></span>").addClass("col-2").text(formatDate(created));
    let authorSpan = $("<span></span>").addClass("col-1").text(username);
    let deleteSpan = $("<span></span>").append("<i class='fa fa-trash col-1'></i>");
    let item = $("<div></div>")
        .addClass("todo-item")
        .prop('id', id)
        .append(
            doneSpan, " ", idSpan, " ", descSpan, " ", categorySpan, " ",
            createdSpan, " ", authorSpan, " ", deleteSpan
        );
    $("#list").append(item);
    if (done) {
        $("#" + id + " input:checkbox").prop('checked', true);
        $("#" + id).addClass('complete');
    }
};

let formatDate = function (created) {
    let date = new Date(created);
    let options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
    };
    return date.toLocaleString("ru", options);
};

$(document).ready(function() {
    $('.todo-list').on('click', '.todo-item input:checkbox',function () {
        if ($(this).is(':checked')) {
            $(this).parent().parent().addClass('complete');
        } else {
            $(this).parent().parent().removeClass('complete');
        }
        updateItem(this);
    });
});

let updateItem = function(checkbox) {
    let done = $(checkbox).is(":checked");
    let id = $(checkbox).parent().parent().prop("id");
    let json = makeJson(id, "", "", done, "");
    $.ajax({
        type: 'POST',
        url: './update.do',
        data: 'item=' + json,
        dataType: 'json'
    }).done(function (data) {

    }).fail(function (err) {
        if ($(checkbox).is(':checked')) {
            $(checkbox).parent().parent().removeClass('complete');
            $(checkbox).prop("checked", false);
        } else {
            $(checkbox).parent().parent().addClass('complete');
            $(checkbox).prop("checked", true);
        }
    });
};

$(document).ready(function() {
   $('#showAll').click(function () {
       if ($(this).is(':checked')) {
           $('#list').removeClass('only-active');
       } else {
           $('#list').addClass('only-active');
       }
   })
});

let getAllTasks = function () {
    $.ajax({
        type: 'GET',
        url: './tasks.do',
        dataType: 'json'
    }).done(function (data) {
        let items = data.items;
        let user = data.user;
        let categories = data.categories;
        for (let i = 0; i < items.length; i++) {
            let id = items[i]["id"];
            let description = items[i]["description"];
            let created = items[i]["created"]
            let done = JSON.parse(items[i]["done"]);
            let username = items[i]["user"]["name"];
            let categories = items[i]["categories"];
            addToFront(id, description, created, done, username, categories);
        }
        for (let i = 0; i < categories.length; i++) {
            let option = $('<option></option>').text(categories[i].name).prop("value", categories[i].id);
            $("#categories").append(option);
        }
        $("#userinfo a").text(user + " | Выйти");
    }).fail(function (err) {

    });
};

$(document).ready(function () {
    getAllTasks();
})

$(document).ready(function () {
    $('.todo-list').on('click', '.fa-trash',function () {
        let id = $(this).parent().parent().prop("id");
        deleteItem(id);
    });
});

let deleteItem = function (id) {
    let json = makeJson(id, "", "", true, "");
    $.ajax({
        type: 'POST',
        url: './delete.do',
        data: 'item=' + json,
        dataType: 'json'
    }).done(function (data) {
        $("#" + id).remove();
    }).fail(function (err) {

    });
}