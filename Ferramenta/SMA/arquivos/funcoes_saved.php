	function getAssuntoFromNome($assunto, $atividade){
		$sql = "SELECT * from mdl_assunto WHERE nome = '".$assunto."' AND id_atividade = $atividade";
		#echo "<br/>".$sql."<br/>";
		conectar();
		$result = mdl_query($sql);
		while($linha = mysql_fetch_array($result)) return $linha['id'];
		return null;
	}

	function alterarAssunto($id_assunto, $assunto){
		$sql = "UPDATE mdl_assunto SET nome = '".$assunto."' WHERE id=$id_assunto;";
		conectar();
		mdl_query($sql);
	}

	function deletarAssunto($id_assunto){
		$sql = "DELETE FROM mdl_assunto WHERE id = $id_assunto";
		conectar();
		mdl_query($sql);
	}

	function addLink($id, $fname, $path, $curso, $atividade, $assunto, $titulo, $link){
		$time = time();
		$tipo;
		$type = strtolower(end(explode(".", $nome)));

		$tipo = getTipo($type);
				
		conectar();

		$curso = getCursoFromNome($curso);
		#$atividade = getAtFromNome($atividade, $curso);	
		#echo "Atividade : ".$atividade;
		#$assunto = getAssuntoFromNome($assunto, $atividade);

		#echo "<br/><br/>".$curso." ".$atividade." ".$assunto."<br/><br/>";
		
		$sql = "INSERT INTO mdl_arquivo VALUES(id, ".$id.", '".$titulo."', '".$link."', FROM_UNIXTIME(".$time."), ".$tipo.", ".$curso.", ".$atividade.", ".$assunto.");";	

		#echo $sql."<br/><br/>";
#		echo $sql;
		if(mdl_query($sql)) return true;
		mensagem(mysql_error());						
	}

function alterarLink($id, $fname, $path, $curso, $atividade, $assunto, $titulo, $link, $id_arquivo){
		$time = time();
		$tipo;
		$type = strtolower(end(explode(".", $nome)));

		$tipo = getTipo($type);
				
		conectar();

		$curso = getCursoFromNome($curso);
		#$atividade = getAtFromNome($atividade, $curso);	
		#echo "Atividade : ".$atividade;
		#$assunto = getAssuntoFromNome($assunto, $atividade);

		#echo "<br/><br/>".$curso." ".$atividade." ".$assunto."<br/><br/>";
		$sql = "DELETE FROM mdl_arquivo WHERE id = $id_arquivo";
#		echo $sql."<br/>";
		mdl_query($sql);
		$sql = "INSERT INTO mdl_arquivo VALUES($id_arquivo, ".$id.", '".$titulo."', '".$link."', FROM_UNIXTIME(".$time."), ".$tipo.", ".$curso.", ".$atividade.", ".$assunto.");";	


		#echo $sql."<br/><br/>";
#		echo $sql;
		if(mdl_query($sql)) return true;
		mensagem(mysql_error());						
	}

