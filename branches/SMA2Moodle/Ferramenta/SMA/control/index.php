<?php
	include_once "../strings.php";
?>

<html>

	<head>

		<ul id="menu">
			<li onclick="document.location='../'" id="voltar"><?php echo getString('go_back');?></li>
		</ul>

		<link rel="stylesheet" type="text/css" href="estilo.css"/>
		<script type="text/javascript" src="../jquery.min.js"></script>
		<script type="text/javascript" src="functions.js">

		</script>

	</head>

	<body><center>

	<div>
		
		<input type="text" id="field" placeholder="<?php echo getString('control_course_name');?>"/><br/>

		<img src="../loading.gif" id="loading"/>

		<ul id="listAgentes">

		</ul>
		
	</div>

	</center></body>

</html>

<?php
	include_once "../check_login.php";

	if(!$_SESSION["SESSION"]->load_navigation_admin){
		header('Location: ../index.php');
	}
?>