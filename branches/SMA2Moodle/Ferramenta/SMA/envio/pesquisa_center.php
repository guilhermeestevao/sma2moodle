<html>

	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	</head>

	<body>
		<div class="container_centro">
			<div class="div_centro">
				<?php
				echo"<form method='post' action='pesquisa.php'>";
					echo $strings['pesq_f']."<br/><br/>";
					echo"<input type='text' name='campo'>";
					echo"<input type='submit' value =".$strings['pesq'].">";
					echo"<input type='button' value =".$strings['cancel']." onclick=\"document.location='index.php'\">";
				echo"</form>";
				?>
			</div>
		</div>
	</body>

</html>
