<?php

	include_once "../database.php";

	$db = new Database();

	$sql = "SELECT * FROM ag_assunto;";

	$res = $db->exec($sql);

	$subjects = array();

	while($line = Database::fetch_array($res)){
		$subjects[] = array('id' => $line['id'], 'name' => $line['nome']);
	}

	$json = json_encode($subjects);
	echo $json;

?>