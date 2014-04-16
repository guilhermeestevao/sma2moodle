<?php

	include_once "../database.php";
	include_once "../../config.php";

	$db = new Database();

	$sql = "SELECT * FROM ag_material ";

	$res = $db->exec($sql);

	$files = array();

	while($line = Database::fetch_array($res)){

		$id =$line['id'];

		$sql = "(SELECT id_licao as original FROM ag_licao_material WHERE id_material = $id) UNION (SELECT id_questionario as original FROM ag_questionario_material WHERE id_material = $id) UNION (SELECT id_tarefa as original FROM ag_tarefa_material WHERE id_material = $id)";
		echo "$sql<br/>";

		$res2 = $db->exec($sql);

		while($line2 = Database::fetch_array($res2)){

			$original = $line2['original'];

			$sql = "(SELECT course FROM mdl_assign WHERE id = $original) UNION (SELECT course FROM mdl_lesson WHERE id = $original) UNION (SELECT course FROM mdl_quiz WHERE id = $original)";
			#echo "$sql<br/>";

			$res3 = $db->exec($sql);

			while($line3 = Database::fetch_array($res3)){
				$cid = $line3['course'];
				$sql = "SELECT rs.userid as user_id FROM mdl_role_assignments rs INNER JOIN mdl_context e ON rs.contextid=e.id  INNER JOIN  mdl_course c ON c.id = e.instanceid WHERE e.contextlevel=50 AND (rs.roleid=3 OR rs.roleid=4) AND contextid = $cid";

				$res4 = $db->exec($sql);

				while($line4 = Database::fetch_array($res4)){
					if($line4['user_id'] == $_SESSION['USER']->id || $_SESSION['SESSION']->load_navigation_admin){

						$files[] = array('name' => $line['nome'], 'path' => $line['caminho'], 'link' => $line['link'], 'id' => $line['id']);
						break;
					}
				}
			}

		}

		#echo "<br/>";

		
	}



	$json = json_encode($files);
	echo $json;

?>