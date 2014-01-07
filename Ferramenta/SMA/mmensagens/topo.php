<html>

	<?php

		include_once "../config.php";
		$lang = null;
		if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
		else $lang = ($_SESSION['SESSION']->lang);

		if($lang == "en") include_once "en.php";
		else if($lang == "pt_br") include_once "pt_br.php";

	
	
	?>
	
	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>

		<div class="user_info">
			<a href="../index.php">voltar<a/>
		</div>
	</head>

	<body>
		<div><dt class="titulo_topo">Titulo da Pagina</dt></div>

		<ul class="topo">
			
			<li><a href="index.php"><?php echo $strings['cadastrar']?></a></li>
			<li><a href="consulta.php">Visualizar Mensagens</a></li>
			<li><a href="../envio/index.php">Formas de envio</a></li>
		</ul>
	</body>

</html>
