let add = function(){
    let taskName = $('#description').val();
    addTask(0, taskName, "", false);
};

let makeJson = function (id, taskName, created, isDone) {
    let item = {};
    item["id"] = Number(id);
    item["description"] = taskName;
    item["created"] = created;
    item["done"] = isDone;
    return JSON.stringify(item);
};

let addTask = function (id, taskName, created, isDone) {
    let json = makeJson(id, taskName, created, isDone);
    $.ajax({
        type: 'POST',
        url: './add.do',
        data: 'item=' + json,
        dataType: 'json'
    }).done(function (data) {
        let item = data.item;
        addToFront(item.id, item.description, item.created, JSON.parse(item.done));
    }).fail(function (err) {

    });
}

let addToFront = function (id, taskName, created, done) {
    let doneSpan = $("<span></span>").addClass("col-1").append("<input type='checkbox'>");
    let idSpan = $("<span></span>").addClass("col-1").text(id);
    let descSpan = $("<span></span>").addClass("col-6").text(taskName);
    let createdSpan = $("<span></span>").addClass("col-3").text(formatDate(created));
    let deleteSpan = $("<span></span>").append("<i class='fa fa-trash col-1'></i>");
    let item = $("<div></div>")
        .addClass("todo-item")
        .prop('id', id)
        .append(doneSpan, " ", idSpan, " ", descSpan, " ", createdSpan, " ", deleteSpan);
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
    let json = makeJson(id, "", "", done);
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
        for (let i = 0; i < items.length; i++) {
            let id = items[i]["id"];
            let description = items[i]["description"];
            let created = items[i]["created"]
            let done = JSON.parse(items[i]["done"]);
            addToFront(id, description, created, done);
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
    let json = makeJson(id, "", "", true);
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