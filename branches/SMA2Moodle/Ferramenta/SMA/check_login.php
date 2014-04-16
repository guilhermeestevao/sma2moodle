<?php

	include_once "/var/www/moodle/config.php";

	if(!isloggedin()){
		header('Location: ../index.php');
	}

?>