<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script>
			function f1(id){
				<?php
				echo "var valor = prompt(".$strings['nvalue'].");";
				echo "window.location='editar.php?id='+id+'&valor='+valor;";
				?>
			}
			function f2(id){
				<?php
					echo"if (confirm(".$strings['sure'].")) {"
					echo"window.location='excluir.php?id='+id;"
				echo"} ";
				?>
				//window.close();
			}
			
		</script>
	</head>

	<body>

		<?php
			$campo= $_SESSION['campo'];
			
			pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
			$sql="SELECT * FROM ag_f_envio WHERE forma LIKE '%".$campo."%';";
			$result= pg_query($sql) ;			
			echo"<div class='container_centro'><br/>";
			$vazio=0;
			
			while($row = pg_fetch_array($result)){
				echo"<div class='container_centro'>";
				echo "<div class='div_centro'>";
					echo "<input type='text' readonly='true' value='".$row['forma']."'>";
					echo "<button onclick='f1(".$row['id'].")' >".$strings['edit']."</button>";
					echo "<button onclick='f2(".$row['id'].")' >".$strings['delete']."</button>";
				echo"</div></div>";
				$vazio++;
			}
			if ($vazio==0){
				echo $strings['no_result'];
			}
			
			echo"</div>";
		
		?>

		
	</body>

</html>
