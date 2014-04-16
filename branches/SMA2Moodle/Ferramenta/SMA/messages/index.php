<?php
	include_once "../strings.php";
?>

<html>

	<head>

		<meta charset="UTF-8"/>

		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<script type="text/javascript" src="../jquery.min.js"></script>
		<script type="text/javascript" src="functions.js"></script>

	</head>

	<body>

		<?php include_once "top.php";?>

		<center>

			<ul id="tags">
				<li id='tag_1'>nome do aluno</li>
				<li id='tag_2'>nome da disciplina</li>
				<li id='tag_3'>nome da atividade</li>
				<li id='tag_4'>nota</li>
				<li id='tag_5'>média</li>
				<li id='tag_6'>dia de encerramento da atividade</li>
				<li id='tag_7'>hora de encerramento da atividade</li>
				<li id='tag_8'>data do chat</li>
				<li id='tag_9'>data</li>
				<li id='tag_10'>nova data</li>
				<li id='tag_11'>nome do tutor</li>
				<li id='tag_12'>nome do fórum</li>
				<li id='tag_13'>data do chat</li>
				<li id='tag_14'>nome do curso</li>
			</ul>	

			<div id="new_message">
				
				<textarea id="new_message_text_area">
					

				</textarea>

				<br/>

				<select id="new_message_select_agent">


				</select><br/>


				<select id="new_message_select_action">

				</select><br/>

				<span id="new_message_ways">
									

				</span>

				</br>

				<input type="button" value="<?php echo getString('cancel');?>" onclick="closeNewMessage()"/>
				<input type="button" value="<?php echo getString('submit');?>" onclick="sendNewMessage()"/>

			</div>

			<div id="pano"></div>

			<div id="messages">

				<select id="agent_filter" onchange="loadMessages()">

				</select>
		
				<ul id="message_list">

					

				</ul>
		
			</div>

			<div id="manage_sending_ways">

				<input type='button' value="Fechar" onclick="closeWays()"/>				
				<input type='button' value="Adicionar Forma de Envio" onclick="showAddItem()"/>
				
				<table id="ways_table">

										

				</table>

			</div>

			<div id="add_way">
					
				<input type="text" placeholder="Insira o nome" id="way_name"/>
				<input type="button" value="Cancel" onclick="cancelAddingWay()"/>
				<input type="button" value="Submit" onclick="addWay()"/>

			</div>

			<div id="blank">

			</div>

			<img src="../loading.gif" id="loading"/>

		</center>

	</body>

</html>

<?php

	include_once "../check_login.php";

?>