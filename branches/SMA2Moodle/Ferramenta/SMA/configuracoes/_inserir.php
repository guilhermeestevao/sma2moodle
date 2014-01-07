<?php
	pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");

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
		echo var_dump($_SESSION);
		$id_agente = $_REQUEST['id_'.$i];
		$id_curso = $_REQUEST['id_curso'];

		if($checks[$i] == "sim"){
			$sql = "DELETE FROM ag_agente_curso WHERE id_agente = ".$id_agente." AND id_curso = ".$id_curso."";
			#echo $sql."<br/>";
			pg_query($sql);
			$sql = "INSERT INTO ag_agente_curso VALUES(id, ".$id_curso.", ".$id_agente.")";
			#echo $sql."<br/>";
			
			pg_query($sql);
		}
		else{
			$sql = "DELETE FROM ag_agente_curso WHERE id_agente = ".$id_agente." AND id_curso = ".$id_curso."";
			#echo $sql."<br/>";			
			pg_query($sql);
			
		}
	}
	header("Location: hab_dhab_curso.php");

?>
