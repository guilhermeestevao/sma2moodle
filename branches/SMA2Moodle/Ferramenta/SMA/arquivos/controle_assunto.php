<?php

	include_once "funcoes.php";
	include_once "../config.php";

	$result = getAssuntos();
	while($linha = pg_fetch_array($result)){
		echo "<dt>".$linha['nome']."</dt>";
		echo "<dd><a href='alterar_assunto.php?nome_assunto=".$linha['nome']."&id=".$linha['id']."'>".$strings['alterar']."</a></dd>";
		echo "<dd><a href='_deletar_assunto.php?id=".$linha['id']."' onclick='return confirm(".$strings['confirm_excluir'].");' >".$strings['deletar']."</a></dd><br/>";
		}
		#echo "<input type='button' value='".$strings['voltar']."' onclick='document.location=".'"_voltar_assunto.php"'."'/>";
		echo "<input type='button' value='".$strings['add_assunto']."' onclick='document.location=".'"index.php?protocolo=add_assunto2"'."'/>";
	
?>
