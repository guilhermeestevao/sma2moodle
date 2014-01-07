<?php

	include_once "funcoes.php";
	echo '<meta http-equiv="content-type" content="text/html; charset=utf-8">';

	if(isset($_GET['arquivo'])){

		$file = $_GET['arquivo'];
		if(file_exists($file)){
		    header('Content-Description: File Transfer');
		    header('Content-Type: application/octet-stream');
		    header('Content-Disposition: attachment; filename='.basename($file));
		    header('Content-Transfer-Encoding: binary');
		    header('Expires: 0');
		    header('Cache-Control: must-revalidate');
		    header('Pragma: public');
		    header('Content-Length: ' . filesize($file));
		    ob_clean();
		    flush();
		    readfile($file);
		    exit;
		}else redirecionar($_GET['arquivo']);

	} else redirecionar("index.php?protocolo=controle");


?>
