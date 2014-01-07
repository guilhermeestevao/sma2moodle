<?php
	/*include_once "../config.php";
		$lang = null;
		if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
		else $lang = ($_SESSION['SESSION']->lang);

		if($lang == "en") include_once "en.php";
		else if($lang == "pt_br") include_once "pt_br.php"; */

function conectar(){
		pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");				
}

function modo_de_envio(){
		$sql = pg_query("select * from ag_formas_de_envio");
		return $sql;

}
function cadastrar($mensagem, $envio, $destinatario, $agente, $action){


		$agente = getIdFromAgente($agente);
		$action = getIdFromAction($action);

		$sql = "insert into ag_mensagens(mensagem , destinatario, f_envio, agente, action) values('$mensagem' , '$destinatario', '$envio', $agente, $action) ";

		#echo $sql;
		pg_query("DELETE FROM ag_mensagens WHERE agente = $agente AND action = $action");
		$sql = pg_query($sql);

		if($sql){
			aviso("Mensagem cadastrada com sucesso.");
		}
		else{
			aviso("Erro! A mensagem não foi Cadastrada.");	
		}
		
}

function getIdFromAgente($nome){
	$res = pg_query("SELECT * FROM ag_agentes WHERE nome = '".$nome."'");
	while($linha = pg_fetch_array($res)){
		return $linha['id'];
	}
}

function getIdFromAction($nome){
	$res = pg_query("SELECT * FROM ag_actions WHERE nome = '".$nome."'");
	while($linha = pg_fetch_array($res)){
		return $linha['id'];
	}
}

function busca_porId_envio($id){
		$sql = pg_query("select f_envio from ag_formas_de_envio where id = $id");
		while($linha = pg_fetch_array($sql)){
			return  $linha['f_envio'];
			
		}
		
}

function busca_porId_destinatario($id){
	$sql = pg_query("select destinatario from ag_mensagens where id = $id");
		while($linha = pg_fetch_array($sql)){
			return  $linha['destinatario'];
			
		}
}

function busca_perfis(){
		$sql = pg_query("select * from mdl_role");
		return $sql;
		
}




function alterar($mensagem, $envio, $id, $destinatario){
		$sql = pg_query("UPDATE ag_mensagens set mensagem='$mensagem',destinatario='$destinatario', f_envio='$envio' where id=$id");
		if($sql){
			aviso("Mensagem alterada com sucesso!");
		}
		else{
			aviso("Erro! A mensagem não foi alterada.");
			
		}
		
}

function deletar($id){		
		$sql = pg_query("DELETE FROM ag_mensagens WHERE id = $id" );
		if($sql){
			aviso("Mensagem deletada com sucesso!");
		}
		else{
			aviso("Erro! A mensagem não foi deletada.");
						
		}
		
}

function aviso($aviso){
		echo "<script>alert('$aviso')</script>";
}
function consultar(){
		return pg_query ("select * from ag_mensagens order by id desc");
				
}

function getAgentes(){
	$res = pg_query("select * from ag_agentes");
	return $res;

}

function getActionsFromAgente($agente){
	conectar();
	$sql = "SELECT * FROM ag_actions WHERE agente = ".getIdFromAgente($agente)."";
	$res = pg_query($sql);
	return $res;
}

function getNumberOfMessages($agente, $action){
	$agente = getIdFromAgente($agente);
	$action = getIdFromAction($action);
	conectar();
	$sql = "SELECT * FROM ag_mensagens WHERE action = $action AND agente = $agente";
	#echo $sql;
	$sql = pg_query($sql);

	return pg_num_rows($sql);
}

function getDesc($action){
	conectar();
	$sql = "SELECT descricao FROM ag_actions WHERE id = ".getIdFromAction($action)."";	
	$res = pg_query($sql);
	while($l = pg_fetch_array($res)){
		return $l['descricao'];
	}
}

?>
