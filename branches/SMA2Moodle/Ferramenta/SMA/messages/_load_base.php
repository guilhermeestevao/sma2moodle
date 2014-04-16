<?php

	include_once "../database.php";

	$db = new Database();

	$sql = "SELECT * FROM ag_agentes";

	$res = $db->exec($sql);

	$agents = array();

	while($line = Database::fetch_array($res)){
		$agents[] = array('id' => $line['id'], 'name' => $line['nome']);
	}

	$sql = "SELECT * FROM ag_formas_de_envio";

	$res = $db->exec($sql);

	$sendWays = array();

	while($line = Database::fetch_array($res)){
		$sendWays[] = array('id' => $line['id'], 'name' => $line['f_envio']);
	}

	$arr = array('agents' => $agents, 'send_ways' => $sendWays);
	$json = json_encode($arr);

	echo $json;

?>