<?php

	include_once "../database.php";

	$db = new Database();

	$name = $_REQUEST['name'];

	$sql = sprintf("INSERT INTO ag_formas_de_envio (f_envio) VALUES ('%s');", mysql_real_escape_string($name));

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo mysql_error();

?>