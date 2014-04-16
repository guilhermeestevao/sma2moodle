time = 300;
var loading = null;

var file;

var id_course = -1;
var id_activity = -1;
var id_subject = -1;

var select_course = null;
var select_activity = null;
var select_subject = null;
var select_type = null;
var file_view = null;
var link_view = null;

function clear(){
	select_subject.empty();
	select_activity.empty();
	hide($('#view_activity_select'));
	hide($('#select_subject_view'));
	hide(select_type);
	hide(link_view);
	hide(file_view);
	hide($('#pano'));		
	loadFiles();
}

function download(path){
	window.open("_download_file.php?file="+path, "_blank");
}

function redirect(link){
	window.open(link, '_blank');
}

function deleteFile(id){
	option = confirm("Delete this file?");
	if(option){

		show(loading);

		$.ajax({
			
			url:"_delete_file.php",
			type:"POST",
			data:"file="+id,

			success:function(res){
				if(res == 0){
					loadFiles();
				}else alert(res);

				hide(loading);
			}

		});	
	}
}

function editFile(id, pos){
	
	show(loading);

	//show($("#edit_file_view"));
	$('#select_course_').empty();

	$.ajax({

		url:"_get_courses.php",
		type:"POST",
		success:function(res){
			
			var json = $.parseJSON(res);



			for(i in json){
				var course = json[i];
				var text = "<option value='"+course.id+"'>"+course.fullname+"</option>";
				$('#select_course_').append(text);
				
				hide(loading);
			}

			// var itens = $('#select_course_ option');
			// i = 0;
			// while(itens[i] != undefined){

			// 	if(i == pos) alert(i);

			// 	i+=1;
			// }


			$.ajax({

				url:"_get_activities.php",
				type:"POST",
				data:"course="+id,
				success:function(res){

					$('#select_activity_').empty();

					var json = $.parseJSON(res);

					for(i in json){
						var activity = json[i];

						var text = "<option value='"+activity.id+"'>"+activity.name+"</option>";
						$('#select_activity_').append(text);
					}

					hide(loading);
					show($('#pano'));
					show($('#add_file_view'));
					show($('#view_activity_select'));
					show($('#view_type_select'));
					show($('#select_subject_view'));

				}
				
			});

		}

	});

	
}

function loadFiles(){

	show($('#show_files'));

	show(loading);

	var files_table = $('#files_table');
	files_table.empty();

	$.ajax({

		url:"_get_files.php",
		type:"POST",

		success:function(res){
			// alert(res);
			var infos = $.parseJSON(res);
			var obj = infos.data;

			var op = 0;
			var value = "t1";

			var header = "<tr><th>"+infos.course_name+"</th><th>"+infos.name+"</th> <th>"+infos.access_options+"</th> <th>"+infos.manager_options+"</th></tr>";
			files_table.append(header);

			for(i in obj){
				var file = obj[i];

				var text = "";

				if(file.link == ''){
					text+=("<tr class='type_file' id='"+value+"'>");
					
					text+=("<td>");
					text+=(file.course);
					text+=("</td>");

					text+=("<td>");
					text+=(file.name);
					text+=("</td>");

					text+=("<td class='right'><center>");
					text+=("<input type='button' onclick='download("+'"'+file.path+'"'+")' class='reddown' value='"+infos.access_file+"'/>");
					text+=("</center></td>");

				}else{
					text+=("<tr class='type_link' id='"+value+"'>");

					text+=("<td>");
					text+=(file.course);
					text+=("</td>");

					text+=("<td>");
					text+=(file.name);
					text+=("</td>");
					
					text+=("<td class='fixed'><center>");
					text+=("<input type='button' onclick='redirect("+'"'+file.link+'"'+")' class='reddown' value='"+infos.access_link+"'/>");
					text+=("</center></td>");
				}

				text+=("<td class='fixed'>");
				text+=("<input type='button' onclick='editFile("+file.id+", "+i+")' class='edit' value='"+infos.edit+"'/>");
				text+=("<input type='button' onclick='deleteFile("+file.id+")' class='delete' value='"+infos.delete+"'/>");
				text+=("</td>");

				text+=("<tr/>");

				files_table.append(text);

				if(op == 0){
					op = 1;
					value = "t2";
				}else{
					op = 0;
					value = "t1";
				}

			}

			hide(loading);
		}

	});
}

function show(view){
	view.fadeIn(time);
}

function hide(view){
	view.fadeOut(time);
}

function prepareUpload(event)
{
  file = event.target.files;
}

function startUpload(){
	show(loading);
}

function stopUpload(res){
	alert(res);
}

function sendFile(file){
	$.ajax({
	    type: 'post',
	    url: '_add_file.php?name=' + file.fileName,
	    enctype:"multipart/form-data",
	    data: file,
	    success: function (res) {
	      // do something
	      alert(res);
	    },
	    xhrFields: {
	      // add listener to XMLHTTPRequest object directly for progress (jquery doesn't have this yet)
	      onprogress: function (progress) {
	        // calculate upload progress
	        var percentage = Math.floor((progress.total / progress.totalSize) * 100);
	        // log upload progress to console
	        console.log('progress', percentage);
	        if (percentage === 100) {
	          console.log('DONE!');
	        }
	      }
	    },
	    processData: false,
	    contentType: file.type
	  });
}

function loadSubjects(){
	var option = $('#select_activity option:selected');
	var id = option.val();
	id_activity = id;

	if(id != "..."){

		var select_subject_view = $('#select_subject_view');

		select_subject.empty()
		select_subject.append("<option value='...'>...</option>");

		show(loading);
		hide(select_subject_view);


		$.ajax({
			type:"POST",
			url:"_get_subjects.php",
			success:function(res){

				var json = $.parseJSON(res);

				for(i in json){
					var subject = json[i];

					var text = "<option value='"+subject.id+"'>"+subject.name+"</option>";

					select_subject.append(text);

				}

				show(select_subject_view);
				show(select_subject);
				hide(loading);
			}
		});

	}

	else{

	}
}

$(document).ready(function(){

	select_course = $('#select_course');
	select_activity = $('#select_activity');
	select_subject = $('#select_subject');
	select_type = $('#select_type');

	loading = $('#loading');

	loadFiles();

	show(loading);

	select_course.empty();
	select_course.append("<option value='...'>...</option>");

	$('#select_course_').empty();

	$.ajax({

		url:"_get_courses.php",
		type:"POST",
		success:function(res){
			
			var json = $.parseJSON(res);

			for(i in json){
				var course = json[i];
				var text = "<option value='"+course.id+"'>"+course.fullname+"</option>";
				select_course.append(text);
				$('#select_course_').empty();
				show($('#view_course_select'));
				hide(loading);
			}

		}

	});

	$('#select_course').change(function(e){
		
		var option = $('#select_course option:selected');
		var id = option.val();
		id_course = id;

		if(id != "..."){
			show(loading);
			hide($('#view_activity_select'));

			select_activity.empty();
			select_activity.append("<option value='...'>...</option>");

			$.ajax({

				url:"_get_activities.php",
				type:"POST",
				data:"course="+id,
				success:function(res){

					var json = $.parseJSON(res);

					for(i in json){
						var activity = json[i];

						var text = "<option value='"+activity.id+"'>"+activity.name+"</option>";
						select_activity.append(text);
					}

					show($('#view_activity_select'));
					hide(loading);

				}


			});
		}

		else{

		}

	});

	$('#select_activity').change(function(e){
		
		loadSubjects();

	});

	$('#add_subject_button').click(function(){

		var add_subject_view = $('#add_subject_view');

		show(add_subject_view);

	});

	$('#add_subject_submit').click(function(){

		var text = $('#text_subject').val();

		if(text.length < 3){
			alert("Your subject must contain at least 3 characterss");
		}else{
			$.ajax({
				url:"_add_subject.php",
				type:"POST",
				data:"subject="+text,

				success:function(res){
					if(res == 0){
						loadSubjects();
					}else{
						alert(res);
					}

					var add_subject_view = $('#add_subject_view');
					hide(add_subject_view);
				}

			});
		}

	});

	$('#add_subject_cancel').click(function(){

		var add_subject_view = $('#add_subject_view');

		hide(add_subject_view);

	});

	$('#select_subject').change(function(e){
		
		var option = $('#select_subject option:selected');
		var id = option.val();
		id_subject = id;

		if(id != "..."){
			show($('#view_type_select'));
		}

	});

	$('#select_type').change(function(e){
		
		var option = $('#select_type option:selected');
		var id = option.val();

		file_view = $('#file_view');
		link_view = $('#link_view');

		if(id != "..."){
			
			if(id == 1){
				hide(file_view);
				show(link_view);
			}else if(id == 2){
				hide(link_view);
				show(file_view);
			}

		}else{
			hide(file_view);
			hide(link_view);
		}

	});

	$('#link_submit').click(function(){
		

		var title = $('#link_text_title').val();
		var link = $('#link_text_link').val();

		if(title.length == 0 || link.length == 0){
			alert("Title and link must be wrote");
		}else{
			$.ajax({
				url:"_add_link.php",
				type:"POST",
				data:"course="+id_course+"&activity="+id_activity+"&subject="+id_subject+"&title="+title+"&link="+link,

				success:function(res){
					if(res == 0){
						hide($('#add_file_view'));
						clear();
					}else{
						alert(res);
					}
				}

			});
		}
	});

	$('#file_submit').click(function(){

		var file = $('#file_file');

		show(loading);

		$.ajax({
            url: "_add_file.php",
            type: "POST",
            contentType: false,
            processData: false,
            data: function() {
                var data = new FormData();
                data.append("file", $("#file_file").get(0).files[0]);
                
                data.append("course", id_course);
                data.append("activity", id_activity);
                data.append("subject", id_subject);

                return data;
                // Or simply return new FormData(jQuery("form")[0]);
            }(),
            error: function(_, textStatus, errorThrown) {
                
            },
            success: function(response, textStatus) {
            	hide(loading);

                if(response == 0){
                	hide($('#add_file_view'));
                	clear();
                }
                else{
                	alert(response);
                }
            }
        });

	});

	$('#menu_add_file').click(function(){

		show($('#add_file_view'));
		show($('#select_course'));

		show($('#pano'));


	});

	$('#menu_show_files').click(function(){

		hide($('#add_file_view'));
		hide($('#pano'));

	});

	$('#close_add_file').click(function(){
		hide($('#add_file_view'));
		hide($('#pano'));	
		hide($('#pano'));
		hide($('#add_file_view'));
		hide($('#view_activity_select'));
		hide($('#view_type_select'));
		hide($('#select_subject_view'));	
	});

});