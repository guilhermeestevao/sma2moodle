<?php
	
	include_once "../database.php";
	include_once "../strings.php";

	$db = new Database();

	$sql = "SELECT * FROM mdl_course";

	$res = $db->exec($sql);

	$cursos = array();

	while($line = Database::fetch_array($res)){

		$idCurso = $line['id'];

		$sql = "SELECT id, nome, (SELECT count(*) FROM ag_agente_curso WHERE id_curso = $idCurso AND id_agente = ag_agentes.id) as qtd FROM ag_agentes;";
		#echo "$sql<br/>";

		$resAgentes = $db->exec($sql);

		$agentes = array();

		while($agente = Database::fetch_array($resAgentes)){
			$agentes[] = array('id' => $agente['id'], 'name' => $agente['nome'], 'ativo' => $agente['qtd']);
		}

		$cursos[] = array('name' => $line['fullname'], 'id' => $idCurso, 'agentes' => $agentes);
	}

	$arr = array('submit' => getString('submit'), 'data' => $cursos);
	$json = json_encode($arr);

	echo $json;

?>