<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<?php	
			include_once "../config.php";
			$lang = null;
			#echo var_dump(($_SESSION['SESSION']));

			if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
			else $lang = ($_SESSION['SESSION']->lang);

			#echo $lang;

			if($lang == "en") include_once "_en.php";
			else if($lang == "pt_br") include_once "_pt_br.php";
		?>
		<script>
			function checkAll(o, id){
				var boxes = document.getElementsByTagName("input");
				for (var x=0;x<boxes.length;x++){			
					var obj = boxes[x];
					
					if (obj.type == "checkbox" && obj.id == id){
						if (obj.name!="chkAll") obj.checked = o.checked;
					}
				}
			}
			
			function check(id){
				
			}


		</script>		

			<?php
				include_once "../config.php";
				
			?>

		<div class="user_info"><dt>
			<a href="../index.php">voltar</a>
		</dt></div>

		
	</head>

	<body>
		<div><dt class="titulo_topo">Sistema de Customização do SMA MOODLE</dt></div>

		<ul class="topo">
			<li><a href="#">Mensagens</a></li>
			<li><a href="hab_dhab_curso.php">Configuração</a></li>
			<li><a href="#">Materiais</a>
				<ul><li><a href="#">Assunto</a></li>
					<li><a href="#">Materiais Complementares</a></li>
					<li><a href="#">Relacionar Materiais</a></li>
				</ul>
			</li>
			<li><a href="logout.php">Sair</a></li>
		</ul><br/>
	</body>

</html>
