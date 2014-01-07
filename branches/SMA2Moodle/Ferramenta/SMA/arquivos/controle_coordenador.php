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
		
		document.getElementById('coordenadores').innerHTML = "";

		if(resposta != "0"){

			var values=resposta.split("#separator_moodle_g#"); 

			for(var item in values){
					//alert(values[item]);
					if(values[item].indexOf("<meta") != 0 && values[item] != "0"){
						//alert(values[item]);
						document.getElementById('coordenadores').innerHTML = document.getElementById('coordenadores').innerHTML+values[item]+"<br/>";
					}
			}
		}
	}
}

function cursoAction(){

	linkReq = criaRequest();

	if(linkReq != undefined){

		var msgBox = document.getElementById('curso');
		if(msgBox.selectedIndex == 0) {return 0;}

		linkReq.open("POST","_get_coordenadores.php",true);
		try{linkReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');}
		catch(failed){alert(failed);}



		linkReq.onreadystatechange = recebeCursoAction;

		
		
		var params = "msg="+msgBox.value;
		

		linkReq.send(params);

	}

}
</script>

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
			
?>

		<div id="coordenadores">
			
		</div>
		
		<br/>
		<a href="index.php?protocolo=adicionar_coordenadores"><?php echo $strings['add_coordenador']?></a>
