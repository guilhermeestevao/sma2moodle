<?php

	include_once "../database.php";

	$db = new Database();

	$sql = "SELECT m.*, ((SELECT id_licao as id FROM ag_licao_material WHERE id_material = m.id) UNION (SELECT id_tarefa as id FROM ag_tarefa_material WHERE id_material = m.id) UNION (SELECT id_questionario FROM ag_questionario_material WHERE id_material = m.id)) as id_referente FROM ag_material m;";

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

		$files[] = array('course' => $course, 'referente' => (int) $line['id_referente'], 'name' => $line['nome'], 'path' => $line['caminho'], 'link' => $line['link']);
	}

	$json = json_encode($files);
	echo $json;

?>