<?php

	$course = $_POST['course'];

	include_once "../database.php";

	$db = new Database();

	$sql = "select id, name from mdl_assign WHERE course = $course 
	UNION 
	SELECT id, name FROM mdl_lesson WHERE course = $course 
	UNION
	SELECT id, name FROM mdl_quiz WHERE course = $course";

	$res = $db->exec($sql);

	$activities = array();

	while($line = Database::fetch_array($res)){
		$activities[] = array('id' => $line['id'], 'name' => $line['name']);
	}

	$json = json_encode($activities);
	echo $json;

?>