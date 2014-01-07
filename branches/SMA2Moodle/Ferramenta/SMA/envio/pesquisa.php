<?php
	session_start();
	$campo=$_REQUEST['campo'];
	$_SESSION['campo']=$campo;
	include_once "topo.php";
	echo "<center><div class='container_total'>";
	include_once "left.php";
	include_once "right.php";
	//include_once "pesquisa_center.php";
	echo "<br/>";
	include_once "_pesquisa_center.php";
	echo "</div></center>";	
	
?>