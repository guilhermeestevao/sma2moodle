<?php

	include_once "../database.php";
	include_once "../strings.php";

	$db = new Database();

	$sql = "SELECT ag_actions.id as action_id, ag_mensagens.id as id, ag_mensagens.mensagem as mensagem, ag_mensagens.destinatario as destinatario, ag_agentes.nome as agente, ag_actions.nome as action FROM ag_mensagens, ag_agentes, ag_actions WHERE ag_mensagens.agente = ag_agentes.id AND ag_mensagens.action = ag_actions.id";

	$res = $db->exec($sql);

	$messages = array();

	while($line = Database::fetch_array($res)){

		$sql = "SELECT * FROM ag_formas_de_envio, ag_forma_envio_action WHERE ag_formas_de_envio.id = ag_forma_envio_action.forma AND ag_forma_envio_action.action = {$line['action_id']};";

		$sendW = $db->exec($sql);

		$ways = array();

		while($line2 = Database::fetch_array($sendW)){
			$ways[] = array('id' => $line2['forma'], 'name' => $line2['f_envio'], 'checked' => '1');
		}

		$sql = "SELECT * FROM ag_formas_de_envio WHERE id = id";

		foreach($ways as $lol){
			// echo var_dump($lol);
			$sql.=" AND id != {$lol['id']}";
		}

		// echo $sql."\n\n";

		$sendW = $db->exec($sql);

		while($line2 = Database::fetch_array($sendW)){
			$ways[] = array('id' => $line2['id'], 'name' => $line2['f_envio'], 'checked' => '0');
		}

		$msg = $line['mensagem'];
		 $msg = str_replace("\\n", "\n", $msg);
		// $msg = str_replace("<", "&lt;", $msg);
		// $mg = str_replace(">", "&gt;", $msg);

		$messages[] = array('action_id' => $line['action_id'],'id' => $line['id'], 'message' => $msg, 'agent' => $line['agente'], 'action' => $line['action'], 'sendWays' => $ways);
	}

	$arr = array('edit' => getString('edit'), 'delete' => getString('delete'), 'data' => $messages);

	$json = json_encode($arr);
	echo $json;

?>