<?php

	session_start();
	$msg = $_SESSION['nome']." > ".$_REQUEST['msg'];
	include_once "funcoes.php";

?>
