<?php

	include_once "../database.php";

	$course = $_POST['course'];
	$activity = $_POST['activity'];
	$subject = $_POST['subject'];

	$file = $_FILES['file'];

	$title = $file['name'];
	$link = '';

	$mimetype = $file['type'];

	$uploadfile = "/var/www/moodle/SMA/files_uploaded/";

	mkdir($uploadfile);

	$it = chmod($uploadfile, 0777);

	$uploadfile.=time()."/";

	mkdir($uploadfile);

	$it = ($it && chmod($uploadfile, 0777));

	$uploadfile.=$title;

	$uploadfile = str_replace(" ", "_", $uploadfile);

	if($it && move_uploaded_file($file['tmp_name'], $uploadfile)){
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

		$sql = sprintf("INSERT INTO ag_material (caminho, link, nome, tipo, id_assunto) VALUES ('%s', '', '%s', '%s', $subject);", 
			mysql_real_escape_string($uploadfile), mysql_real_escape_string($title), mysql_real_escape_string($mimetype)
			);

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

	}

	else echo "failed"." : $uploadfile \n";

?>
