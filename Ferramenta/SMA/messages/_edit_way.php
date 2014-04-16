<?php

	include_once "../database.php";

	$db = new Database();

	$name = $_REQUEST['name'];
	$id = $_REQUEST['id'];

	$sql = sprintf("UPDATE ag_formas_de_envio SET f_envio = '%s' WHERE id = $id;", mysql_real_escape_string($name));

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo mysql_error();

?>