<?php

	$course = $_POST['course'];

	include_once "../database.php";

	$sql = "DELETE FROM ag_agente_curso WHERE id_curso = $course;";

	$db = new Database();

	$db->exec($sql);

	foreach($_POST as $key=>$value){
		if($key != "course"){
			$sql = "INSERT INTO ag_agente_curso(id_curso, id_agente) VALUES($course, $value);";
			#echo $sql."<br/>";

			$db->exec($sql);
		}
	}

	header('Location: index.php');

?>