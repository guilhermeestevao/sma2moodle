<?php

	include_once "funcoes.php";
	include_once "../config.php";

	unset($_SESSION['atividade']);
	
	redirecionar("index.php?protocolo=controlar_assunto");

?>
