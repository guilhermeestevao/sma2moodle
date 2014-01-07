<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
	</head>

	<body>
		<div class="container_menu">
		<div class="titulo"><dt>&nbsp;Filtros</dt></div>
			<ul class="esquerda">
				<?php session_start();?>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle"><?php echo $strings['todos']?></a></li>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle&filtro=0"><?php echo $strings['documentos']?></a></li>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle&filtro=1"><?php echo $strings['fotos']?></a></li>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle&filtro=2"><?php echo $strings['musicas']?></a></li>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle&filtro=3"><?php echo $strings['videos']?></a></li>
				<li><a href="index.php?id_curso=<?php echo $_SESSION['id_curso'];?>&protocolo=controle&filtro=4"><?php echo $strings['outros']?></a></li>
				

			</ul>
		</div>
	</body>

</html>
