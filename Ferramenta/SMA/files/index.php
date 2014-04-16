<html>

	<head>
		<link rel="stylesheet" type="text/css" href="../style.css"/>
		<script type="text/javascript" src="../jquery.min.js"></script>
		<script type="text/javascript" src="../jquery.ajaxfileupload.js"></script>
		<script type="text/javascript" src="functions.js"></script>

		<?php include_once "top.php";?>
	</head>

	<body>

		<img src="../loading.gif" id="loading"/>

		<?php

			include_once "add_file.html";
			include_once "show_files.php";

		?>

	</body>

</html>

<?php

	include_once "../check_login.php";

?>