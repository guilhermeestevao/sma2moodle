<style>

.div_noticias{
	border:1px solid #dddddd;
	height:140px;
	width:100%;
	margin-bottom:5px;
}	

.lista_noticias{
	height:120px;
	overflow:auto;
}

.lista_noticias li{
	padding-bottom:10px;
}

.lista_noticias li.white{
	background-color:"#ffffff";
}

.lista_noticias li.cinza{
	background-color:"#dddddd";
}

</style>

<?php
	
		pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");

		$sql = "SELECT * FROM ag_actions_agentes WHERE id_curso = ".$_REQUEST['id']." AND id_aluno = ".$_SESSION['USER']->id." ORDER BY id DESC";

		#echo $sql;
		#echo $sql;

		$result = pg_exec($sql);

		if(pg_num_rows($result) > 0){
			?>
			<h4>Mensagens:</h4>

			<div class='div_noticias'>
				<ul class='lista_noticias'>
				<?php
					$class = "white";
					while($linha = pg_fetch_array($result)){
						echo "<li style='background-color:".$class.";'>".$linha['mensagem']."</li>";
						if($class == "white")
							$class = "#dddddd";
						else $class="white";
					}
	
				?>
				</ul>

			</div>
			<?php
		}
		
?>



