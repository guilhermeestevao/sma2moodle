<?php

	include_once "funcoes.php";
	
	$nome = $_REQUEST['msg'];
	
	$result = getAtividades(getCursoFromNome($nome));

	$res = "";
	
	while($linha = pg_fetch_array($result)){
		$res.="#separator_moodle_g#".$linha['name'];
	}

	if($res == "") echo "0";
	else echo $res;

?>
