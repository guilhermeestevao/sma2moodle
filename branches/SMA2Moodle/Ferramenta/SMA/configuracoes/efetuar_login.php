<?php
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
?>
<html>
	<head>
		<title>Atenticando usuário</title>
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
				echo "<script>window.alert ('ATENÇÃO: Campo Login obrigatorio.');</script> ";
				echo "<script>loginerror()</script>";
				
				
			}
			else if($senha == null){
				echo "<script>loginerror()</script>";
				echo "<script>window.alert ('ATENÇÃO: Campo Senha obrigatorio.');</script> ";
				
			}
			else{
				$result = pg_query("SELECT * FROM login WHERE login = '$login';");
				if (!$result) {
					echo 'Não foi possível executar a consulta: ';
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
						echo "<script>window.alert ('ERRO: Senha não cadastrada.');</script> ";
						echo "<script>loginerror()</script>";
					}
				}
				else{
					echo "<script>window.alert ('ERRO: Login não cadastrado.');</script> ";
					echo "<script>loginerror()</script>";
				}
			}
			
		?>
	</body>
</html>
