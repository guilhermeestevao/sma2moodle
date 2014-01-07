<?php
	include_once "../../config.php";
	session_start();

	if(!isset($_SESSION['USER'])){
		header("Location: login.php");
		exit;
	}
?>

<html>
	<head>
	</head>
	<body>
		
		<?php
			include_once "topo.php";
			echo "<center><div class='container_total'>";
			include_once "left.php";
			include_once "right.php";
			include "exibir_agentes.php";
			echo "</div></center>";
		?>
	</body>	
</html>
