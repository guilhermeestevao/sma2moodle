<?php
	include_once "metodos.php";
	conectar();
	$modo_de_envio = modo_de_envio();
	$perfis = busca_perfis();
	$dest = busca_perfis();
	$agentes = getAgentes();
	
?>
<html>
	
	<head>

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


function selectedAgente(){

	document.getElementById("adesc").innerHTML = "";

	linkReq = criaRequest();

	if(linkReq != undefined){

		var msgBox = document.getElementById('agentes');


		if(msgBox.selectedIndex == 0) {


		var at = document.getElementById('acoes');		

		for(index in at) at.remove(index);
		at.options[at.options.length] = new Option("...", "...");

return 0;}

		linkReq.open("POST","_get_acoes.php",true);
		try{linkReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');}
		catch(failed){alert(failed);}

		linkReq.onreadystatechange = receiveResult;
		
		var params = "agente="+msgBox.value;

		linkReq.send(params);

	}

}

function receiveResult(){

	if (linkReq.readyState == '4'){

		var obj = linkReq;

		resposta = (obj.response);

		var at = document.getElementById('acoes');		

		var values=resposta.split("#sep_agente_action#"); 

		for(index in at) at.remove(index);
		at.options[at.options.length] = new Option("...", "...");

		for(var item in values){
			at.options[at.options.length] = new Option(values[item], values[item]);
		}

	}

}
function selectedAction(){

	linkReq = criaRequest();

	if(linkReq != undefined){

		var msgBox = document.getElementById('acoes');

		linkReq.open("POST","_get_desc.php",true);
		try{linkReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');}
		catch(failed){alert(failed);}

		linkReq.onreadystatechange = receiveResultFromAction;
		
		var params = "action="+msgBox.value;

		linkReq.send(params);

	}

}

function receiveResultFromAction(){

	if (linkReq.readyState == '4'){

		var obj = linkReq;

		resposta = (obj.response);
	
		document.getElementById("adesc").innerHTML = resposta;

	}

}



</script>	

		<link rel="stylesheet" type="text/css" href="estilo.css"/>
			
		<script>
		
			function focus(id){
				document.getElementById(id).focus();
			}
		
			function concatene(){
				focus("texto");
				document.getElementById("texto").value += "<"+document.getElementById("opcao").value+">";
				document.getElementById("opcao").selectedIndex = 0;
			}
		</script>		
				
	</head>

	<body>
		<div class="container_centro">
			<div class="div_centro">
				<form action="cad.php" method="POST">
					<textarea id="texto" name="mensagem" cols=100 rows=10> </textarea><br>
					
					Destinatário: <select name="destinatario">
										<?php
										while($destinatario = pg_fetch_array($dest)){
										?>
										<option  value="<?php echo $destinatario['name']; ?>" > <?php echo $destinatario['name']; ?></option>
										<?php }?>
								  </select>
					
					Modo de envio: <select  name="envio">
						<?php while($linha = pg_fetch_array($modo_de_envio)){?>
						<option  value="<?php echo $linha['id'] ?>"><?php echo $linha['f_envio']?></option>
						<?php }?>
					</select>

<br/>

	Agente: <select name="agentes" id="agentes" onchange="selectedAgente()">
		<option value="...">...</option>
										<?php
										while($agente = pg_fetch_array($agentes)){
										?>
										<option  value="<?php echo $agente['nome']; ?>" > <?php echo $agente['nome']; ?></option>
										<?php }?>
								  </select>

	Acao: <select name="acoes" id="acoes" onchange="selectedAction()">

		<option value='...'>...</option>

								  </select>

	<div id="adesc"></div>


<br/>
					<input type="submit" name ="botao" value="Cadastrar">
				</form>
				
				
				<select id="opcao" onchange="concatene()" name="opcao" >
					<option  value="<nome_do_aluno>" selected></option>
					<?php
					while($perfil = pg_fetch_array($perfis)){
					?>
					<option  value="<?php echo $perfil['shortname']; ?>" > <?php echo "&lt".$perfil['shortname']."&gt"; ?></option>
					<?php }?>
				</select>

				
								
				
			 
			</div>
		</div>
	</body>

</html>
