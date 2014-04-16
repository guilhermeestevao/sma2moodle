<?php

	include_once "../database.php";

	$file = $_POST['file'];

	$db = new Database();

	$sql = "SELECT * FROM ag_material WHERE id = $file";

	$res = $db->exec($sql);

	while($line = Database::fetch_array($res)){
		unlink($line['caminho']);
	}

	$sql = "DELETE FROM ag_material WHERE id = $file";

	$res = $db->exec($sql);

	if($res) echo 0;
	else echo mysql_error();

?>