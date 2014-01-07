
<?php
	include_once "metodos.php";

	$mensagem = $_POST['mensagem'];
	$envio = $_POST['envio'];
	$destinatario = $_POST['destinatario'];
	$agente = $_POST['agentes'];
	$action = $_POST['acoes'];

	session_start();
	$_SESSION['mensagem'] = $mensagem;
	$_SESSION['envio'] = $envio;
	$_SESSION['destinatario'] = $destinatario;
	$_SESSION['agentes'] = $agente;
	$_SESSION['acoes'] = $action;

	conectar();

	$n = getNumberOfMessages($agente, $action);

	if($n > 0){
		?>

		<script>

			answer = confirm("Ja existe um modelo de mensagem cadastrado, clique em ok para sobreescrever o modelo existente com esse novo modelo.");

			if (answer == 0) 
			{ 
				document.location = "index.php";
			}

			else{
				document.location = "_cad.php";
			}

		</script>

		<?php



	}


?>	




