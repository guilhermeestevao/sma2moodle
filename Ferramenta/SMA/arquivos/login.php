<?php

	include_once "funcoes.php";
	include_once "../config.php";
	
	session_start();
	if(isset($_SESSION['USER'])) redirecionar("index.php");
	else {
		session_destroy();
		include_once "login.html";
	}
	

?>
