<html>
	
	<head>

		<style>
			body{
				color:#666666;
			}

			ul.menu{
				margin-left: -8px;
				margin-right: -8px;
				padding:5px;
				background-color: #cccccc;
			}

			ul.menu li{

			}

			#presentation{
				width:500px;
				height:400px;
				box-shadow: 1px 1px 5px #bbbbbb;
				border-radius: 5px;
				border:1px solid #dddddd;
				
			}#details{
				width:90%;
				height:270px;
				box-shadow: 1px 1px 5px #bbbbbb;
				border-radius: 5px;
				border:1px solid #dddddd;
				padding: 5px;
				background-color: #eeeeee;
			}
			dt{
				font-size: 40px;
				padding:5px;
				font-family: Helvetica;
				border:1px solid #cccccc;
				border-radius: 5px;
				width:90%;
				box-shadow: 1px 1px 5px #bbbbbb;
				background-color: #eeeeee;
			}
			#list, #list li{
				list-style: none;
				list-style-position:inside;
			}

			#list li{
				font-size:30px;
				padding:20px;
				font-family: Helvetica;
				margin-left: -40px;
			}

			#list li:hover{
				background-color: #dddddd;
				border-radius: 5px;
				cursor: pointer;
			}

			#list li a{
				color:#666666;
				text-decoration: none;
			}
		</style>

		<?php
			include_once "strings.php";
		?>

<ul class='menu'>
		<a href="../" id="menu_show_files"><?php echo getString('go_back');?></a>
	</li>
</ul>

	</head>

	<body>
		<center>
			<br/><br/>
			<div id="presentation">
				<br/>
				<dt>Sistema Multi-Agentes</dt>

				<br/>

				<div id="details">
					<ul id="list">
						<li><a href="files/">
							Controle de Arquivos
						</a></li>
						<li><a href="control">
							Controle de Agentes
						</a></li>
						<li><a href="messages">
							Controle de Mensagens
						</a></li>
					</ul>
				</div>
			</div>

		</center>
	</body>

</html>

<?php
	include_once "check_login.php";

?>