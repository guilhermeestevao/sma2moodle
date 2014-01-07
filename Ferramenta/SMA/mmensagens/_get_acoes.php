<?php

	include_once "metodos.php";

	$agente = $_REQUEST['agente'];

	$acoes = getActionsFromAgente($agente);

	$count = pg_num_rows($acoes);	

	while($acao = pg_fetch_array($acoes)){
		echo $acao['nome'];
		if($count > 1)
			echo "#sep_agente_action#";
		$count-=1;
	}	

?>
