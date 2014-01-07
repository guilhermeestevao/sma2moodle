<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		<?php
			include_once "../../config.php";
			$lang=null;
			if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
			else $lang = ($_SESSION['SESSION']->lang);

			if($lang == "en") include_once "en.php";
			else if($lang == "pt_br") include_once "pt_br.php";

		?>

		<div class="user_info"><dt>
			<?php

				echo "<dt class='pequeno'>".$strings['loged']." ".$_SESSION['USER']->firstname." ".$_SESSION['USER']->lastname." (<a href='_logout.php'>".$strings['voltar']."</a>) </dt>";		
			?>
		</dt></div>

	</head>


		<?php
		echo"<div><dt class=\"titulo_topo\">".$strings['manter']."</dt></div>";

		echo"<ul class='topo'>";
			echo"<li><a href='index.php'>".$strings['manter']."</a></li>";
			echo"<li><a href='cadastrar.php'>".$strings['cad']."</a></li>";
			echo"<li><a href='pesquisar.php'>".$strings['pesq']."</a></li>";
			echo '<li><a href="../mmensagens/index.php">Mensagens</a></li>';
			
			echo"<li><a href='#'></a></li>";
		echo"</ul>";
		
		?>

</html>
