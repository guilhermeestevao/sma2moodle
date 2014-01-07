<?php
	
	function write(){

	
		
		include_once "funcoes.php";
		
		include_once "../config.php";

		session_start();
		echo var_dump($strings);

		if(($_SESSION['step'] == 1)){
			echo "Escolha o curso: <select name='curso'>";
				$result = getCursos($_SESSION['USER']->id);
				while($linha = pg_fetch_array($result)){
					echo "<option>".$linha['fullname']."</option>";
				}
			echo "</select>";
			echo "</br>"; 				
			echo "<input type='submit' value='Proximo'/>";
			echo "     ";
			#echo "<input type='button' value='Voltar' onclick='document.location=".'"_back.php"'."'/>";
		}
		
		else if(($_SESSION['step'] == 2)){
			$i = 0;
			echo "Escolha o tipo da atividade: <select name='atividade'>";
				$result = getAtividades(getCursoFromNome($_SESSION['step1']));
				while($linha = pg_fetch_array($result)){
					$i++;
					echo "<option>".$linha['nome']."</option>";
				}
			echo "</select>";
			echo "</br>"; 				
			if($i == 0) echo "Infelizmente, voce nao pode continuar, pois nao possui nenhuma atividade cadastrada no curso.";
			else echo "<input type='submit' value='Proximo'/>";
			echo "     ";
			echo "<input type='button' value='Voltar' onclick='document.location=".'"_back.php"'."'/>";
		}	

		else if(($_SESSION['step'] == 3)){
			$id_curso = getCursoFromNome($_SESSION['step1']);
			$id_atividade = getAtFromNome($_SESSION['step2'], $id_curso);
			$result = getAssuntos($id_atividade);
			if($result == null){
				echo "Esta atividade ainda nao possui nenhum assunto cadastrado.";
				echo "<br/><a href='index.php?protocolo=add_assunto'>Cadastre um agora</a>";			
				$_SESSION['id_atividade_'] = $id_atividade;
			}
			else{
				$i = 0;
				echo "Escolha o assunto da atividade: <select name='assunto'>";
					while($linha = pg_fetch_array($result)){
						$i++;
						echo "<option>".$linha['nome']."</option>";
					}
				echo "</select>";
				if($i > 0){
					echo "ou <a href='index.php?protocolo=add_assunto'>Cadastre outro assunto</a>";			
					$_SESSION['id_atividade_'] = $id_atividade;
	
					echo "</br>"; 				
					echo "<input type='submit' value='Proximo'/>";
				}
				else {
				echo "<br/>Esta atividade ainda nao possui nenhum assunto cadastrado.";
				echo "<br/><a href='index.php?protocolo=add_assunto'>Cadastre um agora</a>";			
				$_SESSION['id_atividade_'] = $id_atividade;
				}
				echo "     ";
				echo "<input type='button' value='Voltar' onclick='document.location=".'"_back.php"'."'/>";
				
				
			}
		}

		else if(($_SESSION['step'] == 4)){
			echo "Qual tipo de informação deseja adicionar: <select name='tipo_upload'>";
				echo "<option>Arquivo</option>";
				echo "<option>Link</option>";
			echo "</select>";			

			echo "</br>"; 				
			echo "<input type='submit' value='Proximo'/>";
			echo "     ";
			echo "<input type='button' value='Voltar' onclick='document.location=".'"_back.php"'."'/>";
			
		}

		else if($_SESSION['step'] == 5){
			#echo var_dump($_SESSION['step4']);

			if($_SESSION['step4'] == "Arquivo"){
				echo "Escolha o arquivo: <input type='file' name='arquivo'/>";
			} else if($_SESSION['step4'] == "Link"){
				echo "Titulo: <br/><input type='text' name='titulo'/><br/>";
				echo "Informe o link: <br/><input type='text' name='link'/><br/>";
			}
			echo "</br>"; 				
			echo "<input type='submit' value='Proximo'/>";
			echo "     ";
			echo "<input type='button' value='Voltar' onclick='document.location=".'"_back.php"'."'/>";
			
		}
		
		
	}


?>
