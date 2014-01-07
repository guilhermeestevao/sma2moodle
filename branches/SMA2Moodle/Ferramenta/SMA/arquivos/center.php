<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
	</head>

	<body>
		<div class="container_centro">
			<div class="div_centro">
				<?php 
					if($_SESSION['protocolo']=='upload') include_once "envio_arquivo.php";
					else if($_SESSION['protocolo']=='controle') include_once "controle_arquivos.php";				
					else if($_SESSION['protocolo']=='alteracao') include_once "alteracao_arquivo.php";				
					else if($_SESSION['protocolo']=='add_assunto') include_once "cadastro_assunto.php";

					else if($_SESSION['protocolo']=='add_assunto2') include_once "cadastro_assunto_.php";
					else if($_SESSION['protocolo']=='teste') include_once "teste.php";
					
					else if($_SESSION['protocolo']=='controlar_assunto') include_once "controle_assunto.php";		

					else if($_SESSION['protocolo']=='editar_assunto') include_once "alterar_assunto.php";		
					else if($_SESSION['protocolo']=='controle_coordenadores') include_once "controle_coordenador.php";
					else if($_SESSION['protocolo']=='adicionar_coordenadores') include_once "cad_coordenador.php";
		
				?>
			</div>
		</div>
	</body>

</html>

