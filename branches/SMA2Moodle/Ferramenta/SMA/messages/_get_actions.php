<?php

	include_once "../database.php";

	$db = new Database();

	$agent = $_REQUEST['agent'];

	$sql = "SELECT * FROM ag_actions WHERE agente = $agent";

	$res = $db->exec($sql);

	$arr = array();

	while($action = Database::fetch_array($res)){
		$arr[] = array('id' => $action['id_action'], 'name' => $action['nome']);
	}

	$json = json_encode($arr);
	echo $json;

?>