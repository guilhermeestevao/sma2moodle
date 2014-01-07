<?php

	include_once "topo.php";
	echo "<center><div class='container_total'>";
	include_once "left.php";
	include_once "right.php";
	include_once "cadastro_center.php";
	echo "</div></center>";
		
	$connection = pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");
	
	
	$valor = $_POST['campo'];
	if($valor == null){
		echo "<script>window.alert ('Erro: Campo Forma de envio deve ser preenchido!!!'); ";
			echo "setTimeout("."window.location='cadastrar.php'".", 0)</script>";
	}
	else{
	
		$sql = "INSERT INTO ag_f_envio (forma) values ('".$valor."');";
		$resposta = pg_query($connection, $sql);
		
			
		if  (!$resposta) {
			echo "query did not execute";
		}
		else{	
			echo "<script>window.alert ('Cadastro efetuado com sucesso!!!'); ";
			echo "setTimeout("."window.location='index.php'".", 0)</script>";
		}
	}
?>
