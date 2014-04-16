<?php

	include_once "../database.php";

	$course = $_POST['course'];
	$activity = $_POST['activity'];
	$subject = $_POST['subject'];

	$title = $_POST['title'];
	$link = $_POST['link'];

	#echo "$course | $activity | $subject | $title | $link";

	$db = new Database();

	$type = NULL;

	$sql = "SELECT * FROM mdl_assign WHERE name = (SELECT name FROM mdl_assign WHERE id = $activity) AND course = $course";
	$n = $db->numRows($sql);
	if($n > 0) $type = 0;

	$sql = "SELECT * FROM mdl_lesson WHERE name = (SELECT name FROM mdl_lesson WHERE id = $activity) AND course = $course";
	$n = $db->numRows($sql);
	if($n > 0) $type = 1;

	$sql = "SELECT * FROM mdl_quiz WHERE name = (SELECT name FROM mdl_quiz WHERE id = $activity) AND course = $course";
	$n = $db->numRows($sql);
	if($n > 0) $type = 2;

	$sql = sprintf("INSERT INTO ag_material (caminho, link, nome, tipo, id_assunto) VALUES ('', '%s', '%s', 'text/plain', $subject);", mysql_real_escape_string($link), mysql_real_escape_string($title));
	$res = $db->exec($sql);

	if($res){
		
		$sql = "SELECT MAX(id) as id FROM ag_material";
		$res = $db->exec($sql);

		$id = NULL;

		while($line = Database::fetch_array($res)){
			$id = $line['id'];
		}

		if($type == 0){
			$sql = "INSERT INTO ag_tarefa_material VALUES ($activity, $id)";
		}
		else if($type == 1){
			$sql = "INSERT INTO ag_licao_material VALUES ($activity, $id)";
		}
		else if($type == 2){
			$sql = "INSERT INTO ag_questionario_material VALUES ($activity, $id)";
		}

		$res = $db->exec($sql);

		if($res) echo 0;
		else echo mysql_error();

	}else{
		echo mysql_error();
	}


?>
