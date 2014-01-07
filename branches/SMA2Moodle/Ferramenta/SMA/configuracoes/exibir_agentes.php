<?php

	pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");
	include_once "../config.php";
	session_start();
	
	echo '<link rel="stylesheet" type="text/css" href="estilo.css"/>';
	$result;
	#echo var_dump($_SESSION['filtro']);
	#if(isset($_SESSION['filtro'])) $result = pg_query("SELECT * FROM mdl_course WHERE fullname LIKE '%".$_SESSION['filtro']."%';");
	$result = pg_query("SELECT * FROM mdl_course;");
			
	if (!$result) {
		echo 'Não foi possível executar a consulta: ';
		exit;
	}
	$j = 0;
	
	while($row = pg_fetch_array($result)){
		
		echo "<div class='container_centro'>";
		echo "<div class='div_centro'>";
		
		echo "<form action='_inserir.php' method='POST'> ";
		
		echo "<input type='hidden' name='id_curso' value='".$row['id']."'/>";
		$sql = "SELECT * FROM ag_agente_curso WHERE id_curso = ".$row['id']."";
		$result2 = pg_query($sql);
		
		$agentesDoCurso = array();
		
		while($linha = pg_fetch_array($result2)){
			array_push($agentesDoCurso, $linha['id_agente']);
		}
		
		echo "<br/>".$row['fullname']."<br/>";
		echo "<br/>";
		
		$sql = "SELECT * FROM ag_agentes";
		
		$result3 = pg_query($sql) ;
		$i = 0;
		while($linha = pg_fetch_array($result3)){
			echo "<div class='sup'>";
			if(in_array($linha['id'], $agentesDoCurso)) echo "<div class='agente'><input id=".$j." class='agente2' name='".$i."' type='checkbox' checked>".$linha['nome']."</input></div>"; 
			else echo "<div class='agente'><input id=".$j." name='".$i."' type='checkbox' class='agente2' unchecked>".$linha['nome']."</input></div>"; 
			echo '<input type="hidden" name="id_'.$i.'" value="'.$linha['id'].'"/>';
			$_SESSION['id_'.$i] = $linha['id'];

			echo "</div>";
			$i++;
		}	
		
		
		echo "<br/><br/></br><br/>";
		echo "<div class='supdiv'>";
		echo "<br/>";
		echo "<input type='checkbox' name='chkAll' onclick='checkAll(this, ".$j.")'>".$strings['hab'];
		$j++;
		echo "</div>";		
		
		echo "<div class='supdiv'>";
		
		echo "<input type='hidden' name='count' value='".$i."'/>";
		echo "<input value='".$strings['cancel']."' type='button' onclick='document.location=".'"hab_dhab_curso.php"'."'/>";
		
		echo "<input type='submit'value='".$strings['save']."'/>";
		echo "</div>";
		echo "</form></div></div><br/>";
		
		
	}
	
	

?>
