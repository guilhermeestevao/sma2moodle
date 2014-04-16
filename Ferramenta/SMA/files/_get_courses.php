<?php

	include_once "../database.php";
	include_once "../../config.php";

	$sql = NULL;

	$teacher = $_SESSION['USER']->id;

	if(!$_SESSION['SESSION']->load_navigation_admin)
		$sql = "SELECT c.id,c.fullname FROM mdl_role_assignments rs INNER JOIN mdl_context e ON rs.contextid=e.id  INNER JOIN  mdl_course c ON c.id = e.instanceid WHERE e.contextlevel=50 AND (rs.roleid=3 OR rs.roleid=4) AND rs.userid=$teacher;";
	else 
		$sql = "SELECT * FROM mdl_course";

	$db = new Database();

	$res = $db->exec($sql);

	$courses = array();

	while($line = Database::fetch_array($res)){
		$courses[] = array('id' => $line['id'], 'fullname' => $line['fullname']);
	}

	$json = json_encode($courses);
	echo $json;

?>