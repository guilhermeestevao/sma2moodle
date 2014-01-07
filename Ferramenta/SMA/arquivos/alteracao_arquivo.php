<script>
function criaRequest(){
	try {
		request = new XMLHttpRequest();
	} catch (trymicrosoft) {

		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");

		} catch (othermicrosoft) {

		try {
				request = new ActiveXObject("Microsoft.XMLHTTP");

			} catch (failed) {
				request = false;
			}
		}
	}

	if (!request)
		alert("Error initializing XMLHttpRequest!");
	else
		return request;


}

function recebeCursoAction(){

	if (linkReq.readyState == '4'){

		var obj = linkReq;

		resposta = (obj.response);

		var at = document.getElementById('atividade');		

		var values=resposta.split("#separator_moodle_g#"); 

				for(index in at) at.remove(index);
		at.options[at.options.length] = new Option("...", at.options.length);

		for(var item in values){
				if(values[item].indexOf("<meta") != 0 && values[item].indexOf("<style") && values[item] != "" && values[item].indexOf("\n") != 0)
					at.options[at.options.length] = new Option(values[item], at.options.length);
		}

	}
}

function cursoAction(){
	linkReq = criaRequest();

	if(linkReq != undefined){

		var msgBox = document.getElementById('curso');
		if(msgBox.selectedIndex == 0) {return 0;}

		linkReq.open("POST","_get_atividades.php",true);
		try{linkReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');}
		catch(failed){alert(failed);}



		linkReq.onreadystatechange = recebeCursoAction;


		
		var params = "msg="+msgBox.value;
		

		linkReq.send(params);

	}

}

function recebeAtividadeAction(){

	if (linkReq.readyState == '4'){

		var obj = linkReq;

		resposta = (obj.response);

		var at = document.getElementById('assunto');		

		var values=resposta.split("#separator_moodle_g#"); 

		for(index in at) at.remove(index); 
		at.options[at.options.length] = new Option("...", at.options.length);

		for(var item in values){
				if(values[item].indexOf("<meta") != 0 && values[item].indexOf("<style") && values[item] != "" && values[item].indexOf("\n") != 0)
					at.options[at.options.length] = new Option(values[item], at.options.length);
		}

	}
}

function atividadeAction(){

	linkReq = criaRequest();

	if(linkReq != undefined){

		var msgBox1 = document.getElementById('curso');
		curso = msgBox1.value;
		var msgBox = document.getElementById('atividade');
		if(msgBox.selectedIndex == 0) {return 0;}
		
		linkReq.open("POST","_get_assuntos.php",true);
		try{linkReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');}
		catch(failed){alert(failed);}



		linkReq.onreadystatechange = recebeAtividadeAction;

		
		var params = "curso="+curso+"&atividade="+msgBox.value;
		

		linkReq.send(params);

	}

}

function tipoAction(){

	var msgBox1 = document.getElementById('informacao');
	var file = document.getElementById('file_upload');
	var link = document.getElementById('link_upload');

	if(msgBox1.selectedIndex == 0) {
		file.style.display = "block";
		link.style.display = "none";
	}
	else {
		file.style.display = "none";
		link.style.display = "block";
	}	
}
</script>



<form action="_update_arquivo.php" method="POST" enctype="multipart/form-data">
	<?php
		include_once "../config.php";
		include_once "funcoes.php";

			echo $strings['escolha_o_curso']."<select name='curso' id='curso' onchange='cursoAction()'>";
				$result = getCursos($_SESSION['USER']->id);
				echo "<option>...</option>";
				while($linha = pg_fetch_array($result)){
					echo "<option>".$linha['fullname']."</option>";
				}
			echo "</select>";
			echo "</br><br/>"; 			

			$i = 0;
			echo $strings['tipo_de_atividade'].": <select name='atividade' id='atividade' onchange='atividadeAction()'>";
								echo "<option>...</option>";
			echo "</select>";
			echo "</br></br>"; 				
			
			echo $strings['assunto_da_atividade'].": <select name='assunto' id='assunto'>";
				echo "<option>...</option>";
			echo "</select>";
			echo "</br></br>"; 				

			echo $strings['tipo_informacao'].": <select name='informacao' id='informacao' onchange='tipoAction()'>";
				echo "<option>".$strings['arquivo']."</option>";
				echo "<option>".$strings['link']."</option>";
			echo "</select>";
			echo "</br></br>"; 				
			

			
			echo "<div id='file_upload' style='display:block;'>".$strings['escolha_arquivo']." <input type='file' name='arquivo'/>"."</div>";


			echo "<div id='link_upload' style='display:none;'>";
			echo $strings['titulo_link']." <br/><input type='text' name='titulo'/><br/>";

			echo $strings['insira_link']."<br/><input type='text' name='link'/><br/>";
			echo "</div><br/>";

			echo "<input type='submit' value='".$strings['proximo']."'/>";
		
	?>
</form>
