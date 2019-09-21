<?php require_once("../includes/db_connection.php");?>
<?php

	function storeDoctor($specialization, $name, $surname, $email, $password, $security_question, $security_answer){
		global $connection;
		
		$query = "INSERT INTO doctors(";
		$query .= "specialization, name, surname, email, password, security_question, security_answer) ";
		$query .= "VALUES('{$specialization}','{$name}','{$surname}'),'{$email}','{$password}','{$security_question}','{$security_answer}')";

		$result = mysqli_query($connection, $query);

		if($result){
			$doctor = "SELECT * FROM doctors WHERE email = '{$email}'";
			$res = mysqli_query($connection, $doctor);

			while ($doctor = mysqli_fetch_assoc($res)){
				return $doctor;
			}
		}else{
				return false;
			}

	}


	function getUserByEmailAndPassword($email, $password){
		global $connection;
		$query = "SELECT * from doctors where email = '{$email}' and password = '{$password}'";
	
		$user = mysqli_query($connection, $query);
		
		if($user){
			while ($res = mysqli_fetch_assoc($user)){
				return $res;
			}
		}
		else{
			return false;
		}
	}
	
	function getPatientTermins($doctorName){
		global $connection;
		
		$query = "SELECT * from doctorName = '{$doctorName}'";
		
		$doctorName = mysql_query($connection, $query);
		
		if($doctorName){
			while ($res = mysql_fetch_assoc($doctorName)){
				return $res;
			}
		}
		else{
			return false;
		}
		
	}


	function emailExists($email){
		global $connection;
		$query = "SELECT email from doctors where email = '{$email}'";

		$result = mysqli_query($connection, $query);

		if(mysqli_num_rows($result) > 0){
			return true;
		}else{
			return false;
		}
	}

?>