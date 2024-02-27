function showModalDialog(title, message) {

	$("#modelTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModal(message) {

	showModalDialog("Error", message);
}

function showWarningModal(message) {

	showModalDialog("Warning", message);
}