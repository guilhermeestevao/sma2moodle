<?php

	include_once "../../config.php";
	include_once "funcoes.php";
	
	conectar();

	echo "<title>".$strings['title']."</title>";

	if(isset($_SESSION['USER'])){

		$_SESSION['id_curso'] = end(explode("id=", $_SESSION['SESSION']->fromdiscussion));

		if(!isset($_SESSION['USER']->firstname)) redirecionar("../../index.php");
		if(isset($_REQUEST['protocolo'])) $_SESSION['protocolo'] = $_REQUEST['protocolo'];
		else $_SESSION['protocolo'] = 'upload';

		if(isset($_REQUEST['filtro'])) $_SESSION['filtro'] = $_REQUEST['filtro'];
		else unset($_SESSION['filtro']);

		include_once "topo.php";
		echo "<center><div class='container_total'>";
		include_once "left.php";
		include_once "right.php";
		include_once "center.php";
		echo "</div></center>";
		
	}	
	else{
		redirecionar("../../index.php");
	}


?>

