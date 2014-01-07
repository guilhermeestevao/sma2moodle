<?php


	include_once "funcoes.php";
	include_once "../config.php";

	$id_atividade = getAtFromNome($_SESSION['atividade'], $_SESSION['id_curso']);

	echo $id_atividade;

	$assunto = $_REQUEST['nome'];
	cadastrarAssunto($id_atividade, $assunto);

	redirecionar("index.php?protocolo=controlar_assunto");


?>
