<?php

	include_once "../database.php";

	$db = new Database();

	$sql = "SELECT * FROM ag_formas_de_envio";

	$res = $db->exec($sql);

	$ways = array();

	while($line = Database::fetch_array($res)){
		$ways[] = array('id' => $line['id'], 'name' => $line['f_envio']);
	}

	$json = json_encode($ways);
	echo $json;

?>