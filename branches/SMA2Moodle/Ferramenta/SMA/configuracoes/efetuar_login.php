<?php
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
?>
<html>
	<head>
		<title>Atenticando usu�rio</title>
		<script type='text/javascript'>
			function loginsucessfully(){
				setTimeout("window.location='index.php'", 0);
			}
			function loginerror(){
				setTimeout("window.location='login.php'", 0);
			}
		</script>
	</head>
	<body>
	
		<?php
			include_once "topo_login.php";
			echo "<center><div class='container_total'>";
			include_once "login_form.php";
			echo "</div></center>";
			$login = $_POST['login'];
			$senha = $_POST['senha'];
			if ($login == null){
				echo "<script>window.alert ('ATEN��O: Campo Login obrigatorio.');</script> ";
				echo "<script>loginerror()</script>";
				
				
			}
			else if($senha == null){
				echo "<script>loginerror()</script>";
				echo "<script>window.alert ('ATEN��O: Campo Senha obrigatorio.');</script> ";
				
			}
			else{
				$result = pg_query("SELECT * FROM login WHERE login = '$login';");
				if (!$result) {
					echo 'N�o foi poss�vel executar a consulta: ';
					exit;
				}
				$row = pg_fetch_row($result);
				
				if($row[2] == $login){
					if($row[3] == $senha){
						session_start();
						$_SESSION['login']=$_POST['login'];
						$_SESSION['senha']=$_POST['senha'];
						echo "<script>loginsucessfully()</script>";
					}
					else{
						echo "<script>window.alert ('ERRO: Senha n�o cadastrada.');</script> ";
						echo "<script>loginerror()</script>";
					}
				}
				else{
					echo "<script>window.alert ('ERRO: Login n�o cadastrado.');</script> ";
					echo "<script>loginerror()</script>";
				}
			}
			
		?>
	</body>
</html>
