function submitValues() {
	var blockBSNLValue = false;
	if ($('#blockBSNL').is(":checked")) {
		blockBSNLValue = true;
	}

	var blockYahooValue = false;
	if ($('#blockYahoo').is(":checked")) {
		blockYahooValue = true;
	}

	var blockYoutubeValue = false;
	if ($('#blockYoutube').is(":checked")) {
		blockYoutubeValue = true;
	}

	$.ajax({
		url : "JqueryServlet",
		data : {
			'blockBSNL' : blockBSNLValue,
			'blockYahoo' : blockYahooValue,
			'blockYoutube' : blockYoutubeValue
		},
		success : function(result) {
			if (result.indexOf("blockBSNL") >= 0) {
				$("#blockBSNL").prop("checked", true);
				$("#blockYahoo").prop("checked", false);
				$("#blockYoutube").prop("checked", false);
			} 
			if (result.indexOf("blockYoutube") >= 0) {
				$("#blockBSNL").prop("checked", false);
				$("#blockYahoo").prop("checked", false);
				$("#blockYoutube").prop("checked", true);
			} 
			if (result.indexOf("blockYahoo") >= 0) {
				$("#blockBSNL").prop("checked", false);
				$("#blockYahoo").prop("checked", true);
				$("#blockYoutube").prop("checked", false);
			}
			alert("Updated.");
		}
	});
}

function login() {
	var username = $("#username").val();
	var password = $("#password").val();
	$("#loginPage").hide();
	$("#welcomeMessage").html("Welcome, " + username);
	$("#welcomePage").show();
}