<?php

	include_once "../database.php";

	$db = new Database();

	$id = $_REQUEST['id'];

	$sql = sprintf("DELETE FROM ag_formas_de_envio WHERE id = $id;");

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo mysql_error();

?>