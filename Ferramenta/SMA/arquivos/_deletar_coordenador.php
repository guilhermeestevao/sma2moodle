<?php

	$id = $_REQUEST['id'];
	include_once "funcoes.php";
	deletarCoordenador($id);
	mensagem($strings['sucesso']);
	redirecionar("index.php");

?>