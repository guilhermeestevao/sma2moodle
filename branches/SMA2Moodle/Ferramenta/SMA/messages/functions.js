deftime = 300;
wayId = -1;
pos = 0;
count = 0;
visible = false;
shiftPress = false;

messagepos = -1;

var agents = null;
var sendWays = null;

var messages = null;

function show(view){
	view.fadeIn(deftime);
}

function changeMessage(pos){


	show($('#new_message'));
	show($('#pano'));

	var message = messages[pos];
	messagepos = message.id;

	var options = $('#new_message_select_agent option');

	i = 0;
	while(options[i] != undefined){

		if(options[i].innerHTML == message.agent){
			options[i].selected = true;
			$('#new_message_select_agent').change();
			break;
		}

		i++;
	}

	var sendWays = message.sendWays;

	var text = "";

	for(j in sendWays){
		var way = sendWays[j];

		if(way.checked == '1')
			text+="<input type='checkbox' value='"+way.id+"' checked>"+way.name+"</input>";
		else text+="<input type='checkbox' value='"+way.id+"'>"+way.name+"</input>";
	}

	$('#new_message_ways').empty();
	$('#new_message_ways').append(text);

	$('#new_message_text_area').val(message.message);
}

function deleteMessage(id){
	a = confirm("Deseja mesmo deletar esta mensagem?");

	if(a){
		$.ajax({

			url:"_delete_message.php",
			data:"id="+id,
			type:"POST",

			success:function(res){
				if(res == 0){
					loadMessages();
				}else{
					alert(res);
					console.log(res);
				}
			}

		});
	}
}

function updateSelected(){
	if(pos <= 0) pos = count;
	else if(pos > count) pos = 1;

	for(i = 1; i<=count; i++){
		var tag = "#tag_"+i;

		$(tag).removeClass("choosed");		
	}

	var tag = "#tag_"+pos;

	$(tag).addClass("choosed");
}

function hide(view){
	view.fadeOut(deftime);
}

function closeNewMessage(){
	hide($('#new_message'));
	hide($('#pano'));
}

function editSendingWays(id, name){
	wayId = id;
	$('#way_name').val(name);
	show($('#add_way'));
}

function sendNewMessage(){

	var message = $('#new_message_text_area').val();
	selectedAgent = $('#new_message_select_agent :selected').html();
	selectedAction = $('#new_message_select_action :selected').html();

	var items = $('#new_message_ways input');
	length = items.length;
	i = 0;

	var sendWays = "";

	while(i < length){

		checked = items[i].checked;
		
		if(checked){
			sendWays += "&sendWay[]="+items[i].value;
		}

		i++;
	}

	$.ajax({

		url:"_add_message.php",
		type:"POST",
		data:"message="+message+"&id="+messagepos+"&agent="+selectedAgent+"&action="+selectedAction+sendWays,
		success:function(res){
			console.log(res);
			if(res == 0){
				alert("Mensagem salva com sucesso!");
				hide($('#new_message'));
				hide($('#pano'));

				loadMessages();
			}else{
				alert(res);
			}
		},

		error:function(a, b, c){
			alert(b);
		}

	});
}

function deleteSendingWays(id, name){
	wayId = id;

	var c = confirm("Delete this way?");

	if(c){
		$.ajax({
			url:"_delete_way.php",
			type:"POST",
			data:"id="+id,

			success:function(res){
				if(res != 0){
					alert(res);
				}

				showManageSendingWays();
			}

		});
	}
	
}

function showNewMessage(){
	show($('#new_message'));
	show($('#pano'));

	messagepos = -1;

	var select_agents = $('#new_message_select_agent');

	select_agents.empty();

	select_agents.append('<option value="-1">...</option>');
	select_agents.append(agents);
	$('#new_message_ways').empty();
	$('#new_message_ways').append(sendWays);
}

function loadMessages(){
	startLoading();

	var list = $('#message_list');
	list.empty();
	
	$.ajax({
		url:"_get_messages.php",
		type:"POST",

		success:function(res){
			// alert(res);
			console.log(res);
			var obj = $.parseJSON(res);

			var edit = obj.edit;
			var del = obj.delete;

			obj = obj.data;
			messages = obj;

			// 

			for(i in obj){
				var message = obj[i];

				if($('#agent_filter :selected').val() == "..." || $('#agent_filter :selected').html() == message.agent){
					var text = "<li>";

					text += "<textarea class='menor' disabled>"+message.message+"</textarea><br/>";

					text+="<br/>";

					text += "<span class='agente'>"+message.agent+"</span>";

					text+="<br/>";

					text += "<span class='action'>"+message.action+"</span>";

					text+="<br/>";

					text += "<span class='sendways'>";

					var sendWays = message.sendWays;

					for(j in sendWays){
						var way = sendWays[j];

						if(way.checked == '1')
							text+="<input type='checkbox' value='"+way.id+"' checked disabled>"+way.name+"</input>";
						else text+="<input type='checkbox' value='"+way.id+"' disabled>"+way.name+"</input>";
					}

					text += "</span>";

					text += "<br/>";

					text += "<span class='options'>";				

						text+="<input type='button' value='"+edit+"' onclick='changeMessage("+i+")'/>";
						text+="<input type='button' value='"+del+"' onclick='deleteMessage("+message.id+")'/>";

					text += "</span>";

					text += "</li>";

					text+="<br/>";text+="<br/>";

					list.append(text);
				}

			}

			show(list);
			show($('#messages'));
			stopLoading();
		},
		error:function(a, b, c){
			alert("erro");
		}


	});
}

function loadBases(){

	startLoading();

	$.ajax({

		url:"_load_base.php",
		type:"POST",

		success:function(res){

			var obj = $.parseJSON(res);

			var _agents = obj.agents;

			agents = "";

			for(i in _agents){
				var agent = _agents[i];

				agents += "<option value='"+agent.id+"'>";

				agents += agent.name;

				agents += "</option>";
			}

			$('#agent_filter').append("<option value='...'>...</option>");
			$('#agent_filter').append(agents);

			$('#new_message_select_agent').append(agents);

			var _sendWays = obj.send_ways;

			sendWays = "";

			for(i in _sendWays){
				var sendWay = _sendWays[i];

				sendWays += "<input type='checkbox' value='"+sendWay.id+"'>"+sendWay.name+"</input>";
			}

			// alert(sendWays);

			loadMessages();

		}

	});
}

function addWay(){
	var value = $('#way_name').val();

	var URL = "_add_way.php";

	if(wayId != -1) URL = "_edit_way.php";

	if(value.length == 0){
		alert("You must insert a name");
	}else{
		startLoading();
		$.ajax({

			type:"POST",
			url:URL,
			data:"name="+value+"&id="+wayId,

			success:function(res){
				stopLoading();

				if(res == 0){
					showManageSendingWays();
					cancelAddingWay();
				}else{
					alert(res);
				}
			}

		});
	}
}

function showAddItem(){
	show($('#add_way'));
	wayId = -1;
}

function closeWays(){
	hide($('#manage_sending_ways'));
}

function cancelAddingWay(){
	hide($('#add_way'));
}

function showManageSendingWays(){
	show($('#manage_sending_ways'));

	var table = $('#ways_table');
	table.empty();

	startLoading();

	table.append("<tr> <th>Name</th> <th>Change</th> <th>Delete</th> </tr>");

	$.ajax({

		url:"_get_sending_ways.php",
		type:"POST",

		success:function(res){

			var obj = $.parseJSON(res);

			for(i in obj){
				var way = obj[i];

				var text = "";

				if(i%2 == 0)
					text+="<tr>";
				else text+="<tr class='type_1'>";


					text+="<td>";
						text+=way.name;
					text+="</td>";

					text+="<td>";
						text+="<input onclick='editSendingWays("+way.id+", "+'"'+way.name+'"'+")' type='button' value='Edit'/>";
					text+="</td>";

					text+="<td>";
						text+="<input onclick='deleteSendingWays("+way.id+", "+'"'+way.name+'"'+")' type='button' value='Delete'/>";
					text+="</td>";

				text+="</tr>";

				table.append(text);
			}

			stopLoading();
			show($('#manage_sending_ways'));
		}

	});
}

function startLoading(){
	show($('#blank'));
	show($('#loading'));
}

function stopLoading(){
	hide($('#blank'));
	hide($('#loading'));
}

function showTags(){
	show($('#tags'));
	visible = true;
}

function hideTag(){
	hide($('#tags'));
	visible = false;
}

function insertAtCursor(myField, myValue) {
	// alert(myValue);
    //IE support
    if (document.selection) {
        myField.focus();
        sel = document.selection.createRange();
        sel.text = myValue;
    }
    //MOZILLA and others
    else if (myField.selectionStart || myField.selectionStart == '0') {
        var startPos = myField.selectionStart;
        var endPos = myField.selectionEnd;
        myField.value = myField.value.substring(0, startPos)
            + myValue
            + myField.value.substring(endPos, myField.value.length);
    } else {
        myField.value += myValue;
    }
}

$(document).ready(function(){

	count = $('#tags li').length;

	$(document).keydown(function(e){

		

	});

	loadBases();	

	$('#new_message_text_area').keydown(function(e){

		var ta = $('#new_message_text_area');

		if(e.keyCode == 16){
			shiftPress = true;
		}
		else{
			if(e.keyCode == 188 && shiftPress){
			showTags();
			}
			if(e.keyCode == 190 && shiftPress){
				hideTag();
			}
			else if(e.keyCode == 38 && visible){
				pos--;
				updateSelected();
				return false;
			}else if(e.keyCode == 40 && visible){
				pos++;
				updateSelected();
				return false;
			}else if(e.keyCode == 27){
				hideTag();
				return false;
			}
			else if(e.keyCode == 13){
				if(visible){
					var tag = "#tag_"+pos;
					insertAtCursor(document.getElementById("new_message_text_area"), $(tag).html()+">");
					hideTag();
					return false;
				}
			}
			else if(e.keyCode == 8 && visible){
				hideTag();
				return true;
			}

			shiftPress = false;
		}
		// ta.val(e.keyCode);	
	});

	$('#new_message_text_area').empty();

	$('#new_message_select_agent').change(function(){

		var selected = $('#new_message_select_agent option:selected');

		var id = selected.val();

		$('#new_message_select_action').empty();

		$.ajax({

			type:"POST",
			data:"agent="+id,
			url:"_get_actions.php",

			success:function(res){

				var obj = $.parseJSON(res);

				for(i in obj){
					var action = obj[i];

					var text = "";

					text += "<option value='"+action.id+"'>"+action.name+"</option>";

					$('#new_message_select_action').append(text);
				}

			}

		});

	});

});
