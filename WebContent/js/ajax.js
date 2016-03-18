$(document).ready(function() {
	$.ajax({

		context : document.body,
		success : function() {
			//alert("called on load");
		}
	});
});

//$('#dashboard').click(function () { console.log("works"); });

function test() {
	console.log("works");
}

function loadHomeTab(){
	
	$("#homeTab").show();
	$("#customTab").hide();
	$("#parentalTab").hide();
	$("#mediaTab").hide();
	$("#eduTab").hide();
	
}

function enableMedia(){
	var rulesEnabledValues = "Media:"+"disable;";
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {
			checkMediaPlayerStatus();
			alert("Updated.");
			
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
			alert("Updated.");
			
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
			alert("Updated.");
			
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
			alert("Updated.");
			
		}
	});
	
}

function loadMediaTab() {
	
	$("#homeTab").hide();
	$("#customTab").hide();
	$("#parentalTab").hide();
	$("#mediaTab").show();
	$("#eduTab").hide();
	$("#mediaTab").html("");
	checkMediaPlayerStatus();	
	
	
	
}
function loadParentalTab() {

	$("#homeTab").hide();
	$("#customTab").hide();
	$("#parentalTab").show();
	$("#mediaTab").hide();
	$("#eduTab").hide();
	checkParentalStatus();
	
}
function loadEduTab() {

	$("#homeTab").hide();
	$("#customTab").hide();
	$("#parentalTab").hide();
	$("#mediaTab").hide();
	$("#eduTab").show();
	checkEducationStatus();
}


function loadCustomTab() {

	$("#homeTab").hide();
	$("#customTab").show();
	$("#parentalTab").hide();
	$("#mediaTab").hide();
	$("#eduTab").hide();

}

var noRules=0;
function submitValues() {
	var urlString = $("#urlStr").val();

	$.ajax({
		url : "JqueryServlet",
		data : {
			'urlStr' : urlString
		},
		success : function(result) {

			alert("Updated.");
		}
	});
}


function changeParentalStatus(parentalValue,socialMediaValue, adultContentValue, blogsValue){
	//var urlString = $("#urlStr").val();
    console.log(parentalValue);
    console.log(socialMediaValue);
    console.log(adultContentValue);
    console.log(blogsValue);
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
			alert("Changed Parental Status");
			
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
			alert("Changed Parental Status");
			
		}
	});
}

function checkParentalStatus() {
	
	
	$.ajax({
		url : "CheckParentalStatusServlet",
		data : {
			
		},
		success : function(result) {
            console.log(result);
			var resultStr = new String(result);
			var statusArray = resultStr.split(";");
			var arrayLength = statusArray.length;
			var keyValArray=statusArray[0].split(":");
			if(keyValArray[1]=="Enabled"){
				console.log("Enabled");
				console.log("Found that parental control service is enabled");
				$("#parentalTab").html("<div id='title_section'><h1>Parental Control</h1></div>");
				$("#parentalTab").append("Parental Control Service is Enabled currently. Would You like to Disable it?");
				var formOpen = "<FORM name='parentalForm' METHOD='post' align='CENTER'>";
				$("#parentalTab").append(formOpen);
				var parental = "Disabled";
				var social = "Disabled";
				var adult = "Disabled";
				var blogs = "Disabled";
				var button='<a class="vz_btn_primary" href="javascript:changeParentalStatus(\'Disabled\',\'Disabled\',\'Disabled\',\'Disabled\');">Disable Parental Control Service</a>';
				var formClose="</FORM>";		
				$("#parentalTab").append(button, formClose);
				
				var formOpen2 = "<br><br><br><FORM name='parentalOptionsForm' METHOD='post' align='CENTER'>";
				$("#parentalTab").append(formOpen2);
				var chkBox1='<INPUT TYPE="CHECKBOX" NAME="socialMediaChk" id="socialMediaChk" >Social Media </INPUT><BR>';
				var chkBox2='<INPUT TYPE="CHECKBOX" NAME="adultContentChk" id="adultContentChk" >Adult Content</INPUT><BR>';
				var chkBox3='<INPUT TYPE="CHECKBOX" NAME="blogsChk" id="blogsChk" >Blogs </INPUT><BR>';
				$("#parentalTab").append(chkBox1, chkBox2, chkBox3);
				if(statusArray[1].split(":")[1]=="Enabled"){
					$("#socialMediaChk").prop("checked", true);
				}
				if(statusArray[2].split(":")[1]=="Enabled"){
					$("#adultContentChk").prop("checked", true);
				}
				if(statusArray[3].split(":")[1]=="Enabled"){
					$("#blogsChk").prop("checked", true);
				}
				
				
				
				var button2='<a class="vz_btn_primary" href="javascript:updateParentalStatus();">Customize Parental Control Service</a>';
				var formClose2="</FORM>";		
				$("#parentalTab").append(button2, formClose2);
			}else{
				console.log("Disabled");
				console.log("Found that parental control service is disabled");
				$("#parentalTab").html("<div id='title_section'><h1>Parental Control</h1></div>");
				$("#parentalTab").append("Parental Control Service is Disabled currently. Would You like to Enable it?");
				var formOpen = "<FORM name='parentalForm' METHOD='post' align='CENTER'>";
				$("#parentalTab").append(formOpen);
				var parental="Enabled";
				var social="Disabled";
				var adult="Disabled";
				var blogs="Disabled";
				var button='<a class="vz_btn_primary" href="javascript:changeParentalStatus(\'Enabled\',\'Disabled\',\'Disabled\',\'Disabled\');">Enable Parental Control Service</a>';
				var formClose="</FORM>";		
				$("#parentalTab").append(button, formClose);
			}
			
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
				console.log("Enabled");
				console.log("Found that education service is enabled");
				$("#eduTab").html("<div id='title_section'><h1>Education Control</h1></div>");
				$("#eduTab").append("Education Service is Enabled currently. <br>You may access the education portal at https://www.coursera.org/ <br>Would You like to Disable it?");
				var formOpen = "<FORM name='eduForm' METHOD='post' align='CENTER'>";
				$("#eduTab").append(formOpen);
				
				var button='<a class="vz_btn_primary" href="javascript:disableEdu();">Disable Education Service</a>';
				var formClose="</FORM>";		
				$("#eduTab").append(button, formClose);
				
			}
			if(resultStr.search("Block-Education':enabled")!=-1){
				console.log("Disabled");
				
				console.log("Found that education service is disabled");
				$("#eduTab").html("<div id='title_section'><h1>Education Control</h1></div>");

				$("#eduTab").append("Education Service is Disabled currently. Would You like to Enable it?");
				var formOpen = "<FORM name='eduForm' METHOD='post' align='CENTER'>";
				$("#eduTab").append(formOpen);
				
				var button='<a class="vz_btn_primary" href="javascript:enableEdu();">Enable Education Service</a>';
				var formClose="</FORM>";
				$("#eduTab").append(button, formClose);
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
				console.log("Enabled");
				console.log("Found that media service is enabled");
				$("#mediaTab").html("<div id='title_section'><h1>Media Control</h1></div>");
				$("#mediaTab").append("Media Service is Enabled currently.<br>You may access the media player at 192.168.0.79:32400/web  <br>Would You like to Disable it?");
				var formOpen = "<FORM name='mediaForm' METHOD='post' align='CENTER'>";
				$("#mediaTab").append(formOpen);
				
				var button='<a class="vz_btn_primary" href="javascript:disableMedia();">Disable Media Service</a>';
				var formClose="</FORM>";		
				$("#mediaTab").append(button, formClose);
				
			}
			if(resultStr.search("Block-Mediaplayer':enabled")!=-1){
				console.log("Disabled");
				
				console.log("Found that media service is disabled");
				$("#mediaTab").html("<div id='title_section'><h1>Media Control</h1></div>");

				$("#mediaTab").append("Media Service is Disabled currently. Would You like to Enable it?");
				var formOpen = "<FORM name='mediaForm' METHOD='post' align='CENTER'>";
				$("#mediaTab").append(formOpen);
				
				var button='<a class="vz_btn_primary" href="javascript:enableMedia();">Enable Media Service</a>';
				var formClose="</FORM>";
				$("#mediaTab").append(button, formClose);
			}
		}
	});
	
	
	
	
}

function enableRules() {
	console.log("Enabling rules");
	
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
	console.log(rulesEnabledValues);
	
	$.ajax({
		url : "EnableRulesServlet",
		data : {
			'rulesEnabledValues' : rulesEnabledValues
		},
		success : function(result) {

			alert("Updated.");
		}
	});
	
}

function loadRules() {

	$.ajax({
		url : "LoadRulesServlet",
		success : function(result) {

			alert("Loaded Firewall Rules");
			console.log("Rules Detected :" + result);
			var rulesArray = result.split(";");
			var arrayLength = rulesArray.length;
			var formOpen = "<FORM name='rulesForm' METHOD='post' align='CENTER'>";
			$("#rulesPane").append(formOpen);
			for (var i = 0; i < arrayLength-1; i++) {
				console.log(rulesArray[i]);
				var rule = rulesArray[i];
				var map=rule.split(":");
				var actualRuleId=map[0];
				var label= map[1];
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
					value : actualRuleId,
					type : 'checkbox',
					checked :enabledVal	
				});
				var newLine = "<br>";

				$("#rulesPane").append(chkBox, label, newLine);

				
			}
			noRules=i;
			var button='<a class="vz_btn_primary" href="javascript:enableRules();">Enable selected rules</a>';
			var formClose="</FORM>";
			$("#rulesPane").append(button, formClose);

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

			alert("Updated.");
		}
	});
}

function login() {
	var username = $("#username").val();
	var password = $("#password").val();
	$("#loginPage").hide();
	$("#welcomeMessage").html("Welcome, " + username);
	$("#nav-side-menu").show();
	$("#MainPage").show();
	$("#homeTab").show();
	
	
}

