<?php
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
	
	$checks = array();
	session_start();
	$i = 0;
	$count = $_REQUEST['count'];
	while($i<$count){
		if(isset($_REQUEST[$i]))
			array_push($checks, "sim");
		else array_push($checks, "nao");
		$i++;
	}
	
	for($i = 0; $i<$count; $i++){
		if($checks[$i] == "sim"){
			$sql = "DELETE FROM ag_agente_curso WHERE id_agente = ".$_SESSION['id_'.$i]." AND id_curso = ".$_SESSION['id_curso'.$i]."";
			pg_query( $sql);
			$sql = "INSERT INTO ag_agente_curso VALUES(id, ".$_SESSION['id_'.$i].", ".$_SESSION['id_curso'.$i].")";
			pg_query( $sql);
		}
		else{
			$sql = "DELETE FROM ag_agente_curso WHERE id_agente = ".$_SESSION['id_'.$i]." AND id_curso = ".$_SESSION['id_curso'.$i]."";
			pg_query( $sql);
		}
	}
	
?>
