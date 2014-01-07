<?php

	include_once "funcoes.php";
	include_once "../config.php";

	session_start();

	if(isset($_SESSION['step4'])){
		unset($_SESSION['step4']);
		$_SESSION['step'] = 4;
	}

	else if(isset($_SESSION['step3'])){
		unset($_SESSION['step3']);
		$_SESSION['step'] = 3;
	}

	else if(isset($_SESSION['step2'])){
		unset($_SESSION['step2']);
		$_SESSION['step'] = 2;
	}

	else if(isset($_SESSION['step1'])){
		unset($_SESSION['step1']);
		$_SESSION['step'] = 1;
	}

	redirecionar("index.php");


?>
