function enableMedia(){
	var rulesEnabledValues = "Media:"+"disable;";
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			checkMediaPlayerStatus();
			$('#myModal').modal();
			$('#modalBody').html("Updated Media Service settings");
			$('#myModal').modal('show');
			
		}
	});	
}
function disableMedia(){
	var rulesEnabledValues = "Media:"+"enable;";
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			checkMediaPlayerStatus();
			$('#myModal').modal();
			$('#modalBody').html("Updated Media Service settings");
			$('#myModal').modal('show');
					
		}
	});
}
function enableEdu(){
	var rulesEnabledValues = "Edu:"+"disable;";
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			checkEducationStatus();
			$('#myModal').modal();
			$('#modalBody').html("Updated Education Service settings");
			$('#myModal').modal('show');
					
		}
	});	
}
function disableEdu(){
	var rulesEnabledValues = "Edu:"+"enable;";
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			checkEducationStatus();
			$('#myModal').modal();
			$('#modalBody').html("Updated Education Service settings");
			$('#myModal').modal('show');			
		}
	});	
}
function updateParentalStatus(){
	//var urlString = $("#urlStr").val();
	var parentalValue = "Enabled";
	var socialMediaValue = "Disabled";
	var adultContentValue = "Disabled";
	var blogsValue = "Disabled";
	if ($('#socialMediaChk').is(":checked")) {
		socialMediaValue = "Enabled";
	}
	if ($('#adultContentChk').is(":checked")) {
		adultContentValue = "Enabled";
	}
	if ($('#blogsChk').is(":checked")) {
		blogsValue="Enabled";
	}	
	$.ajax({
		url : "ChangeParentalStatusServlet",
		data : {
			'Parental' : parentalValue,
			'SocialMedia': socialMediaValue,
			'AdultContent': adultContentValue,
			'Blogs': blogsValue			
		},
		success : function(result) {
			console.log("After changing status");
			console.log(result);
			checkParentalStatus();
			$('#myModal').modal();
			$('#modalBody').html("Changed Parental Control settings");
			$('#myModal').modal('show');
			}
	});
}

function checkParentalStatus() {
	$.ajax({
		url : "CheckParentalStatusServlet",
		data : {			
		},
		success : function(result) {
			var resultStr = new String(result);
			var statusArray = resultStr.split(";");
			var arrayLength = statusArray.length;
			var keyValArray=statusArray[0].split(":");
			if(keyValArray[1]=="Enabled"){
				$("#enabledParentalControls").show();
				$("#disabledParentalControls").hide();
				if(statusArray[1].split(":")[1]=="Enabled"){
					$("#socialMediaChk").prop("checked", true);
				}
				if(statusArray[2].split(":")[1]=="Enabled"){
					$("#adultContentChk").prop("checked", true);
				}
				if(statusArray[3].split(":")[1]=="Enabled"){
					$("#blogsChk").prop("checked", true);
				}
			} else {
				$("#enabledParentalControls").hide();
				$("#disabledParentalControls").show();
			}
		}
	});
}
function changeParentalStatus(parentalValue, socialMediaValue, adultContentValue, blogsValue) {
	$.ajax({
		url : "ChangeParentalStatusServlet",
		data : {
			'Parental' : parentalValue,
			'SocialMedia': socialMediaValue,
			'AdultContent': adultContentValue,
			'Blogs': blogsValue
		},
		success : function(result) {
			checkParentalStatus();
			$('#myModal').modal();
			$('#modalBody').html("SUCCESS: Parental Status is changed successfully.");
			$('#myModal').modal('show');
		}
	});
}
function checkEducationStatus() {
	$.ajax({
		url : "LoadRulesServlet",
		data : {			
		},
		success : function(result) {
            console.log(result);
			var resultStr = new String(result);
			if(resultStr.search("Block-Education':disabled")!=-1){
				$("#enabledEduControl").show();
				$("#disabledEduControl").hide();			
			}
			if(resultStr.search("Block-Education':enabled")!=-1){
				$("#enabledEduControl").hide();
				$("#disabledEduControl").show();
			}
		}
	});
}
function checkMediaPlayerStatus() {
	$.ajax({
		url : "LoadRulesServlet",
		data : {			
		},
		success : function(result) {
            console.log(result);
			var resultStr = new String(result);
			if(resultStr.search("Block-Mediaplayer':disabled")!=-1){
				$("#enabledMediaControl").show();
				$("#disabledMediaControl").hide();				
			}
			if(resultStr.search("Block-Mediaplayer':enabled")!=-1){
				$("#enabledMediaControl").hide();
				$("#disabledMediaControl").show();	
			}
		}
	});
}
function enableRules() {
	var rulesEnabledValues= "";
	for (var i = 0; i < noRules; i++) {
		var ruleId="#rule"+i;
		var actualRuleId=$(ruleId).val();		
		if ($(ruleId).is(":checked")) {
			rulesEnabledValues = rulesEnabledValues+actualRuleId+":"+"enable;";
		}else{
			rulesEnabledValues = rulesEnabledValues+actualRuleId+":"+"disable;";
		}
	}
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			$('#myModal').modal();
			$('#modalBody').html("Custom Rule(s) are enabled.");
			$('#myModal').modal('show');
			loadRules();
		}
	});	
}

function deleteRules() {
	var rulesEnabledValues= "";
	for (var i = 0; i < noRules; i++) {
		var ruleId="#rule"+i;
		var actualRuleId=$(ruleId).val();		
		if ($(ruleId).is(":checked")) {
			rulesEnabledValues = rulesEnabledValues+actualRuleId+":"+"enable;";
		}else{
			rulesEnabledValues = rulesEnabledValues+actualRuleId+":"+"disable;";
		}
	}
	$.ajax({
		url : "DeleteRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			$('#myModal').modal();
			$('#modalBody').html("Custom Rule(s) are deleted.");
			$('#myModal').modal('show');
			loadRules();
		}
	});	
}
function loadRules() {
	$.ajax({
		url : "LoadCustomRulesServlet",
		success : function(result) {
			$('#myModal').modal();
			$('#modalBody').html("Custom Firewall Rules are loaded successfully.");
			$('#myModal').modal('show');
			var rulesArray = result.split(";");
			var arrayLength = rulesArray.length;
			var formOpen = "<FORM name='rulesForm' METHOD='post' align='CENTER'>";
			$("#rulesPane").html("");
			$("#rulesPane").append(formOpen);
			for (var i = 0; i < arrayLength-1; i++) {
				console.log(rulesArray[i]);
				var rule = rulesArray[i];
				var map=rule.split(":");
				var actualRuleId=map[0];
				var label= "'"+map[1].split("-")[2];
				var ruleName=map[1];
				var enabledValString=map[2];
				console.log(enabledValString);
				var enabledVal=false;
				if(enabledValString =='enabled'){
					enabledVal=true;
				}
				var ruleId = "rule" + i;				
                //var chkBox= '<INPUT TYPE="CHECKBOX" NAME="'+ruleId+'" VALUE="'+rule+'" id="'+ruleId+ '" checked='+enabledVal+'>'
				var chkBox = $(document.createElement('input')).attr({
					id : ruleId,
					name : ruleId,
					value : ruleName,
					type : 'checkbox',
					checked :enabledVal	
				});
				var newLine = "<br>";
				$("#rulesPane").append(chkBox, label, newLine);
			}
			noRules=i;
			var formClose="</FORM>";
			$("#rulesPaneHolder").show();
			$("#rulesPane").append(formClose);
		}
	});
}
function blockSite() {
	var urlString = $("#urlStr").val();
    var ruleString=$("#ruleStr").val();
	$.ajax({
		url : "BlockSiteServlet",
		data : {
			'urlStr' : urlString,
			'ruleStr' : ruleString
		},
		success : function(result) {
			$('#myModal').modal();
			$('#modalBody').html("Blocked the Site.");
			$('#myModal').modal('show');
			loadRules();
		}
	});
}
function login() {
	var username = $("#username").val();
	var password = $("#password").val();
	$.ajax({
		url : "LoginServlet",
		data : {
			'username' : username,
			'password' : password
		},
		success : function(result) {
			if(result=="Valid"){
				$('#myModal').modal();
				$('#modalBody').html("Login Successful. Redirected to the Configuration Page");
				$('#myModal').modal('show');
				$("#loginPage").hide();
				$("#firewallControls").show();
				$("#pageHeader").html("vCPE configuration for " + username);
			} else{
				$('#myModal').modal();
				$('#modalBody').html("Login Unsuccessful");
				$('#myModal').modal('show');
				
			}
		}
	});
}
function showOrHideControls(moduleToBeShown) {
	$("#firewallControls").show();
	if(moduleToBeShown == 'Parental Controls') {
		checkParentalStatus();
		$("#parentalControls").show();
		$("#customControls").hide();
		$("#mediaControl").hide();
		$("#eduControl").hide();
	} else if(moduleToBeShown == 'Custom Controls') {
		loadRules();
		$("#parentalControls").hide();
		$("#customControls").show();
		$("#mediaControl").hide();
		$("#eduControl").hide();
	} else if (moduleToBeShown == 'Media Controls') {
		checkMediaPlayerStatus();
		$("#parentalControls").hide();
		$("#customControls").hide();
		$("#mediaControl").show();
		$("#eduControl").hide();
	} else if (moduleToBeShown == 'Edu Controls') {
		checkEducationStatus();
		$("#parentalControls").hide();
		$("#customControls").hide();
		$("#mediaControl").hide();
		$("#eduControl").show();
	} 
}

/**
 * 
 */
function closePopup() {
	$("#myModal").hide();
}

/**
 * 
 */
function pollLoggers(pollFor) {
	if(pollFor == 'ONOS') {
		setTimeout(function() {
			readOnosLogs();
		}, 2000);
	} else {
		setTimeout(function() {
			readLogs();
		}, 2000);
	}
}

/**
 * 
 */
function readLogs() {
	$.ajax({
		url : "ReadLogServlet",
		data : {			
		},
		success : function(result) {
			$("#instanceCreationLogs").html(result);
			pollLoggers();
		}
	});
}

/**
 * 
 */
function readOnosLogs() {
	$.ajax({
		url : "ReadONOSLogsServlet",
		data : {			
		},
		success : function(result) {
			$("#onosLoggers").html(result);
			pollLoggers('ONOS');
			scrollToBottom('ONOS');
		}
	});
}

/**
 * @param app
 */
function scrollToBottom(app) {
	if (app == 'ONOS') {
		$('#onosLoggersHolder').scrollBottom();
	}
}