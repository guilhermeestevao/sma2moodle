<?php

	include_once "funcoes.php";
	
	$nome = $_REQUEST['msg'];
	
	$result = getCoordenadores(getCursoFromNome($nome));
	
	while($linha = pg_fetch_array($result)){			
		if($res=="")
			$res.="#separator_moodle_g#".$linha['email']."&nbsp;&nbsp;<a href='_deletar_coordenador.php?curso=".$linha['curso']."&id=".$linha['id']."'>".$strings['deletar']."</a>";
		else 
			$res.="#separator_moodle_g#".$linha['email']."&nbsp;&nbsp;<a href='_deletar_coordenador.php?curso=".$linha['curso']."&id=".$linha['id']."'>".$strings['deletar']."</a>";
	}

	if($res == "") echo "";
	else echo $res;

?>
