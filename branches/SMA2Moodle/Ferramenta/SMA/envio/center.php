<?php
	session_start();
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
?>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script>
			function f1(id){
				var valor = prompt("Digite novo valor:");
				if (valor =="" || valor== null){
					
				}else {
					window.location="editar.php?id="+id+"&valor="+valor;
				}
				
			}
			
			function f2(id){
				if (confirm("Tem certeza que deseja excluir essa categoria?")) {
					window.location="excluir.php?id="+id;
				} 
				//window.close();
			}
			
		</script>
		
	</head>

	<body>

	Formas de Envio:<br/>
		<?php
			$sql="SELECT * FROM ag_f_envio;";
			$result= pg_query($sql);
			
			echo"<div class='container_centro'><br/>";
			while($row = pg_fetch_array($result)){
			echo"<div class='container_centro'>";
			echo "<div class='div_centro'>";
				echo "<input type='text' readonly='true' value='".$row['forma']."'>";
				echo "<button onclick='f1(".$row['id'].")' >Editar</button>";
				echo "<button onclick='f2(".$row['id'].")' >Excluir</button>";
			echo"</div></div>";
			}
			echo"</div></div>";
		?>
	</body>

</html>
