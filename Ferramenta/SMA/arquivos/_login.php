<?php

	include_once "funcoes.php";

	if(isset($_POST['usuario']) && isset($_POST['senha'])){
		$user = $_POST['usuario'];
		$senha = $_POST['senha'];
		if(login($user, $senha) == true){
			session_start();
			$_SESSION['username'] = $user;
			redirecionar("index.php");
		}
		else{
			mensagem($string['u_s_incorretos']);
			redirecionar("login.html");
		}
	}
	else redirecionar("login.html");

?>
