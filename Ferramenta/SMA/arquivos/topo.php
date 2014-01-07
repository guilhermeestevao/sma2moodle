<html>

	<?php include_once "funcoes.php";?>

	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" >
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
	</head>

	<body>
		<div><dt class="titulo_topo"><?php echo $strings['title']?></dt></div>
		<div class="user_info"><dt>
			<?php
				echo "<dt class='pequeno'>".$strings['loged']." ".$_SESSION['USER']->firstname." ".$_SESSION['USER']->lastname." (<a href='_logout.php'>".$strings['voltar']."</a>) </dt>";		
			?>
		</dt></div>

		<ul class="topo">
			<li><a href="index.php?protocolo=upload"><?php echo $strings['envio_de_arquivos'];?></a></li>
			<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle"><?php echo $strings['controle_de_arquivos'];?></a></li>
			<li><a href="index.php?protocolo=controlar_assunto"><?php echo $strings['controle_de_assuntos'];?></a></li>
			<li><a href="index.php?protocolo=controle_coordenadores"><?php echo $strings['controle_de_coordenadores'];?></a></li>
		</ul>
	</body>

</html>
