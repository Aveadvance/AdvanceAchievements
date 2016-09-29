window.onload = function () {
}

function deleteTask(id) {
	var r = confirm("Do yo want delete task?")
	if (r == true) {
		window.location = rootPath + "/deleteusertask?id="+id;
	}
}

function deleteTaskCategory(id) {
	var r = confirm("Do yo want delete task category?")
	if (r == true) {
		window.location = rootPath + "/deletetaskcategory?id="+id;
	}
}