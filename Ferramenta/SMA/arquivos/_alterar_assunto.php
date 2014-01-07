<?php

	include_once "funcoes.php";
	include_once "../config.php";

	$assunto = $_REQUEST['assunto'];
	$id_assunto = $_REQUEST['id_assunto'];

	alterarAssunto($id_assunto, $assunto);

	redirecionar("index.php?protocolo=controlar_assunto");

?>
