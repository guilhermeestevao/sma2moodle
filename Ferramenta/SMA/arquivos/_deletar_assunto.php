<?php

	include_once "funcoes.php";
	include_once "../config.php";

	$id_assunto = $_REQUEST['id'];

	deletarAssunto($id_assunto);

	redirecionar("index.php?protocolo=controlar_assunto");

?>
