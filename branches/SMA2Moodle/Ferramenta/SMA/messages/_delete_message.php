<?php

	$id = $_REQUEST['id'];

	include_once "../database.php";

	$db = new Database();

	$sql = "DELETE FROM ag_mensagens WHERE id = $id";

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo 1;

?>