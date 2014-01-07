<?php

	include_once "funcoes.php";
	include_once "../config.php";

	
	
	$nome = $_REQUEST['msg'];
	
	$result = getAtividades(getCursoFromNome($nome));

	$res = "";
	echo "#separator_moodle_g#";
	while($linha = pg_fetch_array($result)){
		if($res=="")
			$res.=$linha['nome'];
		else 
			$res.="#separator_moodle_g#".$linha['nome'];
	}

	if($res == "") echo "0";
	else echo $res;

?>
