<?php

	$nome_curso = $_REQUEST['curso'];
	$email = $_REQUEST['nome'];
	
	include_once "../config.php";
	include_once "funcoes.php";
	
	if($nome_curso != "..."){
		if(addCoordenador($nome_curso, $email)){
			mensagem($strings['sucesso']);
			redirecionar('index.php');
		}
		else{
			mensagem($strings['falha']);
			redirecionar("index.php");
		}
	}else{
		mensagem("Curso inválido");
		redirecionar("index.php");
	}

?>
