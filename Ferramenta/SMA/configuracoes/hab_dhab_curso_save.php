<html>
	<head>
		
	</head>
	
	<body>
		<?php
			pg_connect("host=localhost port=5432 dbname=moodleuece user=postgres password=root");

			$op= $_POST['op'];
			
			if($op == "Cancelar"){
				header("Location: hab_dhab_curso.php");
			}
			else{
				session_start();
				
				$x=$_SESSION['x'];
				$y=0;
				$z=0;
				while($y<=$x){
				if($y%7==0){
					echo"<br/>".$_SESSION['row'.$z]."<br/>";
					echo"<br/>ag1";
					
				}
				if(($y-1)%7==0){
					echo"<br/>ag2";
					
				}
				if(($y-2)%7==0){
					echo"<br/>ag3";
					
				}
				if(($y-3)%7==0){
					echo"<br/>ag4";
					
				}
				if(($y-4)%7==0){
					echo"<br/>ag5";
					
				}
				if(($y-5)%7==0){
					echo"<br/>ag6";
					
				}
				if(($y-6)%7==0){
					echo"<br/>ag7";
					
				}
				
				if(isset($_POST["check".$y])){
					echo " selecionado<br/>";
				}
				else{
					echo " não <br/>";
				}
				$y++;
				
				}
			}
		?>
	</body>
</html>
