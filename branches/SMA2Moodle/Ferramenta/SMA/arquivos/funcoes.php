

<?php

	$uo = "USER_OBJECT";
	include_once "../../config.php";
	$lang = null;
	#echo var_dump(($_SESSION['SESSION']));

	if(!isset($_SESSION['SESSION']->lang)) $lang = $_SESSION['USER']->lang;
	else $lang = ($_SESSION['SESSION']->lang);

	#echo $lang;

	if($lang == "en") include_once "_en.php";
	else if($lang == "pt_br") include_once "_pt_br.php";

	function getNomeTipo($pos){
		if($pos == 0) return "Documento";
		if($pos == 1) return "Imagem";
		if($pos == 2) return "Musica";
		if($pos == 3) return "Video";
		if($pos == 4) return "Outro";
	}


	function mdl_query($sql){
		$result = pg_query($sql);
		return $result;
	}

	function getTipo($nome){
#		echo "alan";
		$documentos = array("css", "js", "xml", "java", "py", "txt", "php", "cpp", "c", "doc", "docx", "pdf", "reg", "ini", "asp");
		$fotos = array("jpg", "png", "jpeg", "bmp");
		$musicas = array("wma", "mp3", "wav", "aac", "ogg", "ac3");
		$videos = array("avi", "rmvb", "mpeg", "mov", "mkv");

		if(in_array($nome, $documentos)) return 0;
		else if(in_array($nome, $fotos)) return 1;
		else if(in_array($nome, $musicas)) return 2;
		else if(in_array($nome, $videos)) return 3;
		else return 4;

	}
	function redirecionar($caminho){
		echo "<script>document.location='".$caminho."';</script>";
	}

	function mensagem($mensagem){
		echo "<script>alert('".$mensagem."');</script>";
	}

	function conectar(){
		pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");
	}

	function login($user, $pass){
		conectar();
		$sql = "SELECT id FROM usuario WHERE usuario='".$user."' AND senha='".$pass."';";
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)){
			session_start();
			$_SESSION['id_user'] = $linha['id'];
			return true;
		}
		return false;
	}

	function getIdUser(){
		session_start();
		$id = $_SESSION['id_user'];
		return $id;
	}

	function addFile($id, $nome, $path, $curso, $atividade, $assunto, $type){
#		echo $id.".<br/>";
		#echo $curso." ".$atividade." ".$assunto;
		$time = time();
		$tipo;

		$tipo = getTipo($type);

		conectar();

		$curso = getCursoFromNome($curso);

		$tatividade = getTipoAtividade($atividade, $curso);

		$atividade = getAtFromNome($atividade, $curso);

		$assunto = getAssuntoFromNome($assunto, $atividade);

		$sql = "INSERT INTO ag_material VALUES('".$path."', '', '".$nome."', '".$type."', $assunto);";
		#echo $sql;
		$sql2 = "";
		if($tatividade == 0)
			$sql2 = "INSERT INTO ag_tarefa_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";
		if($tatividade == 1)
			$sql2 = "INSERT INTO ag_licao_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";
		if($tatividade == 2)
			$sql2 = "INSERT INTO ag_questionario_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";

		#echo "<br/><br/>";
		#echo $sql2;


		if(mdl_query($sql) && mdl_query($sql2)) return true;
	}

	function alterarFile($id, $nome, $path, $curso, $atividade, $assunto, $id_arquivo, $type){
		
		$time = time();
		$tipo;
		$type = strtolower(end(explode(".", $nome)));

		$tipo = getTipo($type);

		conectar();

		$curso = getCursoFromNome($curso);

		$tatividade = getTipoAtividade($atividade, $curso);

		$atividade = getAtFromNome($atividade, $curso);

		$assunto = getAssuntoPos($assunto);

		$sql = "UPDATE ag_material SET caminho = '".$path."', link = '', nome = '".$nome."', tipo = '".$type."', id_assunto = $assunto WHERE id = $id_arquivo;";



		#echo $sql."<br/>";
		if(mdl_query($sql)) return true;
		mensagem(pg_error());

}

	function getFiles($id, $filtro){
		conectar();
		$sql = "SELECT * FROM ag_material;";
		return mdl_query($sql);
	}

	function deletarArquivo($id){
		$sql = "SELECT * FROM mdl_arquivo WHERE id = ".$id."";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)){
			unlink($linha['caminho_arquivo']);
		}
		$sql = "DELETE FROM ag_material WHERE id = ".$id.";";
		mdl_query($sql);
		mensagem("Arquivo excluido com sucesso!");
		redirecionar("index.php");
	}

	function getCursos($id_professor){
		$sql = null;
		if(!$_SESSION['SESSION']->load_navigation_admin)
		$sql = "SELECT c.id,c.fullname FROM mdl_role_assignments rs INNER JOIN mdl_context e ON rs.contextid=e.id  INNER JOIN  mdl_course c ON c.id = e.instanceid WHERE e.contextlevel=50 AND (rs.roleid=3 || rs.roleid=4) AND rs.userid=$id_professor;";
		else $sql = "SELECT * FROM mdl_course";

		conectar();
		$result = mdl_query($sql) or die("alan");
		return $result;
	}

	function getAtividades($id_curso){
		$sql = "select id, name from mdl_assign WHERE course = $id_curso UNION SELECT id, name FROM mdl_lesson WHERE course = $id_curso UNION SELECT id, name FROM mdl_quiz WHERE course = $id_curso";
		conectar();
		$result = mdl_query($sql) or die(pg_error());
		return $result;
	}

	function getAssuntos(){
		$sql = "select * from ag_assunto";
		#echo $sql."<br/>";
		conectar();
		$result = mdl_query($sql);
		return $result;
	}

	function getAssuntoPos($pos){
		$result = getAssuntos();
		$c = 0;
		while($linha = pg_fetch_array($result)){
			if($c == $pos-1) return $linha['id'];
			$c+=1;
		}
	}

	function getCursoFromNome($nome){
		$sql = "SELECT id FROM mdl_course WHERE fullname = '".$nome."'";
		#echo "#separador_moodle_g#".$sql;
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) {
			return $linha['id'];
		}
		return null;
	}

	function getTipoAtividade($atividade, $id_curso){
		$sql = "SELECT * FROM mdl_assign WHERE name='".$atividade."' AND course = $id_curso";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) return 0;
		$sql = "SELECT * FROM mdl_lesson WHERE name='".$atividade."' AND course = $id_curso";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) return 1;
		$sql = "SELECT * FROM mdl_quiz WHERE name='".$atividade."' AND course = $id_curso";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) return 2;
	}

	function getAtFromNome($atividade, $id_curso){
		$sql = "(SELECT id FROM mdl_assign WHERE 
			name='".$atividade."' AND course = $id_curso)

			UNION (SELECT id FROM mdl_lesson WHERE 
			name='".$atividade."' AND course = $id_curso)

			UNION (SELECT id FROM mdl_quiz WHERE 
			name='".$atividade."' AND course = $id_curso)";

		#echo $sql."<br/><br/>";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) return $linha['id'];
		return null;
	}

	function cadastrarAssunto($id_atividade, $assunto){
		$sql = "INSERT INTO ag_assunto VALUES('".$assunto."');";
		conectar();
		mdl_query($sql);
	}

	function getAssuntoFromNome($assunto, $atividade){
		$sql = "SELECT * from ag_assunto WHERE nome = '".$assunto."';";
		#echo "<br/>".$sql."<br/>";
		conectar();
		$result = mdl_query($sql);
		while($linha = pg_fetch_array($result)) return $linha['id'];
		return null;
	}

	function alterarAssunto($id_assunto, $assunto){
		$sql = "UPDATE ag_assunto SET nome = '".$assunto."' WHERE id=$id_assunto;";
		conectar();
		mdl_query($sql);
	}

	function deletarAssunto($id_assunto){
		$sql = "DELETE FROM ag_assunto WHERE id = $id_assunto";
		conectar();
		mdl_query($sql);
	}

	function addLink($id, $nome, $path, $curso, $atividade, $assunto, $titulo, $link){
		$time = time();
		$tipo;
		$type = strtolower(end(explode(".", $nome)));

		$tipo = getTipo($type);

		conectar();

		$curso = getCursoFromNome($curso);

		$tatividade = getTipoAtividade($atividade, $curso);

		$atividade = getAtFromNome($atividade, $curso);
		#echo "Atividade : ".$atividade;
		$assunto = getAssuntoFromNome($assunto, $atividade);

		#echo "<br/><br/>".$curso." ".$atividade." ".$assunto."<br/><br/>";

		$sql = "INSERT INTO ag_material VALUES('', '".$path."', '".$nome."', '".'text/plain'."', $assunto);";

		if($tatividade == 0)
			$sql2 = "INSERT INTO ag_tarefa_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";
		if($tatividade == 1)
			$sql2 = "INSERT INTO ag_licao_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";
		if($tatividade == 2)
			$sql2 = "INSERT INTO ag_questionario_material VALUES($atividade, (SELECT MAX(id) FROM ag_material));";


#		echo $sql."<br/><br/>".$sql2."<br/>";
#		echo $sql;
		if(mdl_query($sql) && mdl_query($sql2)) return true;
		mensagem(pg_error());
	}

function alterarLink($id, $fname, $path, $curso, $atividade, $assunto, $titulo, $link, $id_arquivo){
		$time = time();
		$tipo;
		$type = strtolower(end(explode(".", $nome)));

		$tipo = getTipo($type);

		conectar();

		$assunto = getAssuntoPos($assunto);

		$curso = getCursoFromNome($curso);
		#$atividade = getAtFromNome($atividade, $curso);
		#echo "Atividade : ".$atividade;
		#$assunto = getAssuntoFromNome($assunto, $atividade);

		#echo "<br/><br/>".$curso." ".$atividade." ".$assunto."<
		$sql = "UPDATE ag_material SET nome = '".$fname."', caminho = '', link = '".$path."', tipo = '".'text/plain'."', id_assunto = $assunto WHERE id = $id;";

		if(mdl_query($sql)) return true;
		mensagem(pg_error());
	}

	function addCoordenador($nome_curso, $email){
		$curso = getCursoFromNome($nome_curso);
		$sql = "INSERT INTO mdl_coordenador values($curso, '".$email."')";
		
		conectar();
		
		$result = mdl_query($sql);
		
		return $result;
	}
	
	function deletarCoordenador($id){
		$curso = getCursoFromNome($nome_curso);
		$sql = "DELETE FROM mdl_coordenador WHERE id=$id;";
		conectar();
		mdl_query($sql);
	}
	
	function getCoordenadores($curso){
		$sql = "SELECT * FROM mdl_coordenador WHERE curso = $curso;";
		conectar();
		return mdl_query($sql);
	}

?>
