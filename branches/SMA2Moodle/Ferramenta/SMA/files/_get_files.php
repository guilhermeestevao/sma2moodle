<?php

	include_once "../database.php";
	include_once "../strings.php";
	include_once "../../config.php";

	$db = new Database();

	$sql = "SELECT m.*, ((SELECT id_licao as id FROM ag_licao_material WHERE id_material = m.id) UNION (SELECT id_tarefa as id FROM ag_tarefa_material WHERE id_material = m.id) UNION (SELECT id_questionario FROM ag_questionario_material WHERE id_material = m.id)) as id_referente FROM ag_material m WHERE id_professor = {$_SESSION['USER']->id};";
	if($_SESSION['SESSION']->load_navigation_admin) $sql = "SELECT m.*, ((SELECT id_licao as id FROM ag_licao_material WHERE id_material = m.id) UNION (SELECT id_tarefa as id FROM ag_tarefa_material WHERE id_material = m.id) UNION (SELECT id_questionario FROM ag_questionario_material WHERE id_material = m.id)) as id_referente FROM ag_material m;";

	$res = $db->exec($sql);

	$files = array();

	while($line = Database::fetch_array($res)){

		$referente = (int) $line['id_referente'];
		
		$sql = "(SELECT course FROM mdl_assign WHERE id = $referente) UNION (SELECT course FROM mdl_lesson WHERE id = $referente) UNION (SELECT course FROM mdl_quiz WHERE id = $referente)";
		$res2 = $db->exec($sql);

		$course = NULL;
		while($line2 = Database::fetch_array($res2)){
			$sql = "SELECT fullname FROM mdl_course WHERE id = ".$line2['course'];
			$res3 = $db->exec($sql);

			while($line3 = Database::fetch_array($res3)){
				$course = $line3['fullname'];
			}
		}

		$files[] = array('course' => $course, 'name' => $line['nome'], 'path' => $line['caminho'], 'link' => $line['link'], 'id' => $line['id']);
	}

	$arr = array('delete' => getString('delete'), 'course_name' => getString('course'), 'edit' => getString('edit'),'access_link' => getString('files_open_link'), 'access_file' => getString('files_download_file'),'name' => getString('files_name'), 'access_options' => getString('files_access_option'), 'manager_options' => getString('files_manager_options'), 'data' => $files);

	$json = json_encode($arr);
	echo $json;

?>