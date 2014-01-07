<?php
	pg_connect("host=localhost port=5432 dbname=moodle user=postgres password=root");
?>
<?php
	include_once "../../config.php";
	session_start();
	if(!isset($_SESSION['USER'])){
		if(isset($_REQUEST['filtro'])) $_SESSION['filtro'] = $_REQUEST['filtro'];
		else $_SESSION['filtro'] = "";
		header("Location: login.php");

		exit;
	}
	else{
		header("Location: hab_dhab_curso.php");
		exit;
	}

?>
