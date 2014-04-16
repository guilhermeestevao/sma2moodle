<?php

	include_once "../database.php";

	$db = new Database();

	$message = $_REQUEST['message'];
	$agent = $_REQUEST['agent'];
	$action = $_REQUEST['action'];
	$sendWay = $_REQUEST['sendWay'];
	$id = $_REQUEST['id'];
	$destinatario = "alan";

	// echo var_dump($_REQUEST);

	$sql = "DELETE FROM ag_mensagens WHERE id = $id";
#	echo $sql."\n\n";

	$db->exec($sql);

	$sql = sprintf("INSERT INTO ag_mensagens (id, mensagem, destinatario, agente, action) VALUES((SELECT MAX(id)+1 FROM ag_mensagens), '%s', '%s', (SELECT id FROM ag_agentes WHERE nome = '$agent'), (SELECT id FROM ag_actions WHERE nome = '$action'));",
		mysql_real_escape_string($message), mysql_real_escape_string($destinatario)
		);
#	echo $sql."\n\n";

	$res = $db->exec($sql);

	$sql = "DELETE FROM ag_forma_envio_action WHERE action = (SELECT id FROM ag_actions WHERE nome = '$action');";
#	echo $sql."\n\n";

	$res = $db->exec($sql);

	foreach($sendWay as $item){
		$sql = "INSERT INTO ag_forma_envio_action (forma, action) VALUES ($item, (SELECT id FROM ag_actions WHERE nome = '$action'))";
	#	echo $sql."\n\n";
		$res = $res && $db->exec($sql);
	}

	if($res)
		echo 0;
	else echo mysql_error();

?>
