<?php

	include_once "funcoes.php";
	include_once "../config.php";

	$id_atividade = $_SESSION['id_atividade_'];

	$assunto = $_REQUEST['nome'];
	cadastrarAssunto($id_atividade, $assunto);

	redirecionar("index.php");

?>
