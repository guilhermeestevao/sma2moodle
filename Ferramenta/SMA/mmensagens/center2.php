<html>
	<head>
		<link rel="stylesheet" type="text/css" href="estilo.css"/>		
		<script>
			function deleteAll(){
				var checks = document.getElementsByClassName("check-target");
				var param = "";
				for(i = 0; i < checks.length; i++){
					if(checks[i].checked){
						param += checks[i].value+",";
					}
				}
				param = param.substring(0, param.length-1);
				if(param != "")
					window.location = "deletar_marcadas.php?ids="+param;
			}
		
			function checkAll(value){
				var checks = document.getElementsByClassName("check-target");
				for(i in checks){
					checks[i].checked = value;
				}
			}
			
			function focus(id){
				document.getElementById(id).focus();
			}
		
			function concatene(id){
				focus(id);
				document.getElementById(id).value += "<"+document.getElementById("op"+id).value+">";
				document.getElementById("op"+id).selectedIndex = 0;
			}
			
			function alterar(id){
			    document.getElementById("esc").value = "alterar";
				document.getElementById("form-"+id).onsubmit = "return true";
			}
			
			function deletar(id){
				if(confirm("Deseja realmente deletar essa mensagem?")){
					document.getElementById("esc").value = "deletar";
					document.getElementById("form-"+id).onsubmit = "return true";
				}
			}
		</script>		
				
	</head>

		<input type="checkbox" onchange="checkAll(this.checked)">Marcar todos/Desmarcar<br>        
       <button  onclick="deleteAll()"> Deletar</button><br>


		<?php 		
			include_once "metodos.php";
			conectar();
			$sql = consultar();
						
		while($linha = pg_fetch_array($sql)){
				$mensagem = $linha['mensagem'];
				$forma_de_envio = $linha['f_envio'];
				$id  = $linha['id'];
				$consulta = modo_de_envio();
				$dest = busca_perfis();
				$perfis = busca_perfis();
				$destinatario_msg = $linha['destinatario'];
		?>



	<body>

		<div class="container_centro">
			<div class="div_centro">					
				
				</form>
				<form action="alt.php" method="POST" onsubmit="return false;" id="form-<?php echo $id;?>">
					<input type="checkbox"  value="<?php echo $id; ?>" class="check-target"/>
					ID : <input type="text" name="cod" value="<?php echo $id; ?>" size="8" readonly> <br>
					<textarea id="<?php echo $id;  ?>" name="mensagem" cols=80 rows=10> <?php echo $mensagem; ?> </textarea><br>
					
					Destinatário: <select name="destinatario">
										<option   selected value="<?php echo $destinatario_msg; ?>"><?php echo $destinatario_msg; ?></option>	
										<?php
										while($linha3 = pg_fetch_array($dest)){
											if( $linha3['perfil'] != $destinatario_msg ){
											?>
											<option  value="<?php echo $linha3['perfil']; ?>" > <?php echo $linha3['perfil']; ?></option>
										<?php 
										}
											}
										?>
								  </select>	
								  
					<select  name="envio">
						<option   selected value="<?php echo $forma_de_envio; ?>"><?php echo busca_porId_envio($forma_de_envio); ?></option>	
						<?php  while($linha2 = pg_fetch_array($consulta) )   {
								if( $linha2['id'] != $forma_de_envio ){
						?>
						<option  value="<?php echo $linha2['id']; ?>"><?php echo $linha2['f_envio']; ?></option>	
						
						<?php 
							}
							}
						?>
						
					</select>
						
					<input type="submit"  value="alterar" name= "esc" id="esc" onclick="alterar(<?php echo $id;  ?>)">					
					<input type="submit"  value="deletar" name="esc" id="esc" onclick="deletar(<?php echo $id; ?>)">
						
				</form>
				
				<select id="<?php echo 'op'.$id;  ?>" onchange="concatene(<?php echo $id;  ?>)" >
					<option  value="<nome_do_aluno>" selected>...</option>
					<?php
					while($perfil = pg_fetch_array($perfis)){
					?>
					<option  value="<?php echo $perfil['perfil']; ?>" > <?php echo "&lt".$perfil['perfil']."&gt"; ?></option>
					<?php }?>
				</select>
								
						
		
		
				
				
			 
			</div>
		</div>
		
	</body>

</html>
<?php 
	}
		?>
