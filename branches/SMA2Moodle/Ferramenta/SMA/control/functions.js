var res = null;
var filter = "";

function fill(res){

	var lista = $('#listAgentes');

	lista.empty();

	var infos = $.parseJSON(res);
	var obj = infos.data;

	var classe = "form_1";

	for(i in obj){
		if(classe == "form_1"){
			classe = "form_2";
		}
		else {
			classe = "form_1";
		}

		var curso = obj[i];

		var nome = "";
		nome += ("alan");

		//alert(curso.name.indexOf(filter));

		var text = "";

		if(curso.name.indexOf(filter) >= 0){
			text+=("<li class='"+classe+"'>");

				text+="<form action='_salvar.php' method='POST'>";

					text+=("<dd>"+curso.name+"</dd>");

					text+=("<input type='hidden' name='course' value='"+curso.id+"'/>");

					for(j in curso.agentes){

						var agente = curso.agentes[j];

							text+=("<ul id='sublist'>");

								text+=("<li>");

									if(agente.ativo > 0)
										text+=("<input type='checkbox' name='agente_"+agente.id+"' value='"+agente.id+"' checked>"+agente.name+"</input>");
									else text+=("<input type='checkbox' name='agente_"+agente.id+"' value='"+agente.id+"' unchecked>"+agente.name+"</input>");

								text+=("</li>");

							text+=("</ul>");

					}
				text+=("<br/><input type='submit' value='"+infos.submit+"'/>");

				text+=("</form>");

			text+=("</li>");
		}

		lista.append(text);
	}
}

$(document).ready(function(){

	$('#loading').fadeIn(300);

	if(res == null){
		$.ajax({
			type:"POST",
			url:"agentes.php",
			success:function(result){

				res = result;

				fill(res);

				$('#loading').fadeOut(300);
				
			}
		});
	}
	else fill(res);

	$('#field').keyup(function(){

		filter = $('#field').val();

		fill(res);

		$('#loading').fadeOut(300);

	});
});