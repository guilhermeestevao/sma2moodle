<?php

	session_start();

	$mensagem = $_SESSION['mensagem'];
	$envio = $_SESSION['envio'];
	$destinatario = $_SESSION['destinatario'];
	$agente = $_SESSION['agentes'];
	$action = $_SESSION['acoes'];

	include_once "metodos.php";
	conectar();

	cadastrar($mensagem, $envio, $destinatario, $agente, $action);	

	echo "<script>document.location='index.php';</script>";

?>
