<?php

include_once "funcoes.php";
include_once "../config.php";

if(isset($_FILES["arquivo"])){
	$fname = $_FILES["arquivo"]["name"];
	$ftmpname = $_FILES["arquivo"]["tmp_name"];
	
	$path = "/var/www/moodle/files_data/".$fname;
	
	$content = file_get_contents($ftmpname);

	$curso = $_POST['curso'];
	$atividade = $_POST['atividade'];
	$assunto = $_POST['assunto'];

	$pc = file_put_contents($path.time(), $content);
	
	$af = addFile($_SESSION['USER']->id, $fname, $path.time(), $curso, $atividade, $assunto);

	if($pc && $af){
		mensagem("Sucesso ao enviar arquivo!");
		redirecionar("index.php?protocolo=controle&id_curso=".$_SESSION['id_curso']);
	} else {
		mensagem("Erro ao enviar arquivo");
		redirecionar("index.php");
	}
	
} else {
	redirecionar("index.php");
}
?>
