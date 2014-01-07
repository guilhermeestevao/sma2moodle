<?php

	include_once "funcoes.php";
	
	
	$result = getAssuntos();
	
	while($linha = pg_fetch_array($result)){
		if($res=="")
			$res.="#separator_moodle_g#".$linha['nome'];
		else 
			$res.="#separator_moodle_g#".$linha['nome'];
	}

	if($res == "") echo "0";
	else echo $res;

?>
