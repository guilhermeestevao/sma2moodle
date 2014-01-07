<?php
               $ids = $_REQUEST['ids'];        

			   include_once "metodos.php";
			   conectar();
               pg_query ("Delete From  ag_mensagens where id in ($ids)");
               echo "<script>document.location='consulta.php'</script>"
               
               ?>