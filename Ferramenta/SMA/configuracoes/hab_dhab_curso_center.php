


<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<script>
			function checkAll(o){
				var boxes = document.getElementsByTagName("input");
				for (var x=0;x<boxes.length;x++){			
					var obj = boxes[x];
					if (obj.type == "checkbox"){
						if (obj.name!="chkAll") obj.checked = o.checked;
					}
				}
			}
		</script>

	</head>

	<body>
		
		<?php
			pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");

			$state="checked";
			
			$result = pg_query("SELECT * FROM mdl_course;");
			
			if (!$result) {
				echo 'Não foi possível executar a consulta: ';
				exit;
			}
			echo "<div class='container_centro'>";
			echo "<div class='div_centro'>";
			echo "<form action='exibir_agentes.php' method='post'>";
			echo "<table>";
			echo "<tr>";
			echo "<td><input type='checkbox' name='chkAll' onclick='checkAll(this)'> ".$strings['hab']."</td>";	
			$x=0;
			$y=0;
			
			while($row = pg_fetch_array($result)){
				include 
				
			}
			
			echo "</table>";				
			echo "</form>";
			echo "</div>";
			echo "</div>";
		?>
			
	</body>

</html>
