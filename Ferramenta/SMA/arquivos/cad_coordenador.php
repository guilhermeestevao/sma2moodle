<form action = '_add_coordenador.php' method='post'>

	<?php
		include_once "../config.php";
		include_once "funcoes.php";

		echo $strings['escolha_o_curso']."<select name='curso' id='curso' onchange='cursoAction()'>";
			$result = getCursos($_SESSION['USER']->id);
			echo "<option>...</option>";
			while($linha = pg_fetch_array($result)){
				echo "<option>".$linha['fullname']."</option>";
			}
		echo "</select>";
	?>	
	<br/>
	<?php echo $strings['email'].": "?><input type='text' name='nome'/> <br/> <input type='submit' value="<?php echo $strings['submit']?>"/>

</form>