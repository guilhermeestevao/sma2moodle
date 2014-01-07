<?php
echo"<form method='post' action='cadastro.php'>";
	echo $strings['cad_f']."<br/><br/>";
	echo" <input type='text' name='campo'><br>";
	echo" <input type='submit' value ='".$strings['cad']."'>";
	echo"<input type='button' value ='".$strings['cancel']."' onclick='document.location='index.php''>";
echo"</form>";
?>
