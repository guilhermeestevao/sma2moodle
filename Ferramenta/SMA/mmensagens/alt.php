<?php 
	include_once "metodos.php";
	$mensagem = $_POST['mensagem'];
	$envio = $_POST['envio'];
	$id = $_POST['cod'];
	$escolha = $_POST['esc'];
	$destinatario = $_POST['destinatario'];
				
	conectar();
	if($escolha=="alterar"){		
		alterar($mensagem, $envio, $id, $destinatario);
	
	}else{	
		deletar($id);
	}
	echo "<script>document.location='consulta.php';</script>";
		
	
	
?>
