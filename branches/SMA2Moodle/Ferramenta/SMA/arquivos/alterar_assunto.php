<?php

	include_once "funcoes.php";
	include_once "../config.php";

	if(!isset($_SESSION['id_assunto'])){

		$_SESSION['id_assunto'] = $_REQUEST['id'];
		$_SESSION['nome_assunto'] = $_REQUEST['nome_assunto'];
		redirecionar("index.php?protocolo=editar_assunto");

	}

	else{
		echo "<form method='post' action='_alterar_assunto.php'>";
		
			echo $strings['altere_assunto'].":<br/><input type='text' name='assunto' value='".$_SESSION['nome_assunto']."'/><br/>";
			echo "<input type='hidden' name='id_assunto' value='".$_SESSION['id_assunto']."'/>";
			echo "<input type='submit' value='".$strings['submit']."'/>";

			echo "<input type='button' value='".$strings['voltar']."' onclick='document.location=".'"index.php?protocolo=controlar_assunto"'."'/>";

		echo "</form>";
		unset($_SESSION['id_assunto']);
	}

?>
