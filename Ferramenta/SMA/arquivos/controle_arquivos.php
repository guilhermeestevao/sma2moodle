<?php

	if(isset($_SESSION['filtro'])) echo "Filtro : ".getNomeTipo($_SESSION['filtro'])."s<br/>";
	else echo "Sem Filtro<br/>";
	
	include_once "funcoes.php";
	include_once "../config.php";

	if(!isset($_REQUEST['id_curso'])) redirecionar("../index.php");

	$result = getFiles($_REQUEST['id_curso'], isset($_SESSION['filtro']));

	echo "<table class='arquivos'>";
	$cont = 0;
	while($linha = pg_fetch_array($result)){
		$cont++;
#		echo var_dump($linha);
		echo "<tr>";
			echo "<td><a href='_download_file.php?arquivo=".($linha['caminho'].$linha['link']."'>".$linha['nome']).addslashes()."</a></td>";
			#echo "<td>".$linha['fullname']."</td>";
			echo "<td>";
			#if($linha['id_usuario'] == $_SESSION['USER']->id){
				$_SESSION['altera_curso'] = $linha['fullname'];
				$_SESSION['altera_atividade'] = $linha['nome'];
				$_SESSION['id_arquivo'] = $linha['id'];
				echo "<a href='index.php?protocolo=alteracao'>".$strings['alterar']."</a>";
			#	}
			#else echo "&nbsp;";
			echo "</td>";
			echo "<td>";
			echo "<a href='_deletar.php?id=".$linha['id']."' onclick='return confirm(".'"Deseja mesmo excluir o arquivo?"'.");' class='deletar'>".$strings['deletar']."</a>";
			#else echo "&nbsp;";
			echo "</td>";

		echo "</tr>";
	}

	if($cont==0) echo "Nenhum arquivo cadastrado, <a href='index.php?protocolo=upload'>".$strings['cadastre_assunto']."</a>";

	echo "</table>";
	
?>
