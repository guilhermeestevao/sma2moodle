<?php

	class Database{

		const USER = "postgres";
		const PASS = "root";
		const HOST = "localhost";
		const DB = "moodleuece";
		const PORT = "5432";

		private $connected = false;

		public function connect(){
			pg_connect("host=".self::HOST." port=".self::PORT." dbname=".self::DB." user=".self::USER." password=".self::PASS);
			$connected = true;
		}


		public function exec($sql){

			if(!$this->connected) self::connect();
			// $sql = addslashes($sql);
			$res = pg_query($sql);

			#mysql_close($link);

			return $res;
		}

		public function numRows($sql){

			$res = self::exec($sql);

			$num = pg_num_rows($res);

			return $num;
		}

		public static function fetch_array($res){
			return pg_fetch_array($res);
		}

	}


?>
