<?php

	$subject = $_POST['subject'];

	include_once "../database.php";

	$db = new Database();

	$sql = sprintf("INSERT INTO ag_assunto (nome) VALUES ('%s');", mysql_real_escape_string($subject));

	#echo $sql;

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo mysql_error();

?>