<?php
	session_start();
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
	$id=$_GET['id'];
	$valor=$_GET['valor'];
	$sql="UPDATE ag_f_envio SET forma = '".$valor."' WHERE id = ".$id.";";
	$result= pg_query($sql) ;
	
	
	header("Location: index.php");
?>
