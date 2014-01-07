<?php
	session_start();
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
	
	$id=$_GET['id'];
	
	$sql="DELETE from ag_f_envio WHERE id = ".$id.";";
	$result= pg_query($sql) ;
	
	header("Location: index.php");
?>
