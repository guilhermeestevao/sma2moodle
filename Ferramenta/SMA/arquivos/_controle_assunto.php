<?php

	include_once "funcoes.php";
	include_once "../config.php";

	$_SESSION['atividade'] = $_REQUEST['atividade'];

	redirecionar("index.php?protocolo=controlar_assunto");
?>
