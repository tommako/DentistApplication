//addEmp.php
<?php 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//Getting values
		$specialization = $_POST['specialization'];
		$name = $_POST['name'];
		$surname = $_POST['surname'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		$security_question = $_POST['security_question'];
		$security_answer = $_POST['security_answer'];



		
		//Creating an sql query
		//$sql = "INSERT INTO employee (name, designation, salary) VALUES ('$name','$desg','$sal')";
		$sql = "INSERT INTO doctors (`specialization`, `name`, `surname`, 'email', 'password', 'security_question', 'security_answer') VALUES ('$specialization','$name','$surname','$email','$password','$security_question','$security_answer')";
		//$sql = "INSERT INTO doctors (`specialization`, `name`, `surname`, 'email', 'password', 'security_question', 'security_answer') VALUES ('specialization','name','surname','email','password','security_question','security_answer')";
		
		//Importing our db connection script

		require_once('dbAppConnection.php');
		
		//Executing query to database
		if(mysqli_query($con,$sql)){
			echo 'Employee Added Successfully';
		}else{
			echo 'Could Not Add Employee';
		}
		
		//Closing the database 
		mysqli_close($con);
	}else{
		echo 'Not Post Request';
	}
?>