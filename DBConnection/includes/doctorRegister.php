<?php
 
require_once '../includes/db_functions.php';
 
// json response array
$response = array("error" => FALSE);

if (isset($_POST['specialization']) && isset($_POST['name']) && isset($_POST['surname']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['security_question']) && isset($_POST['security_answer'])) {

	// receiving the post params
	$specialization = $_POST['specialization'];
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$email = $_POST['email'];
	$password = $_POST['password'];
	$security_question = $_POST['security_question'];
	$security_answer = $_POST['security_answer'];
 
	 // check if user already exists with the same email
    if(emailExists($email)){
		// email already exists
        $response["error"] = TRUE;
        $response["error_msg"] = "Email already exists with " . $email;
        echo json_encode($response);
	}else {
        // create a new doctor
		$user = storeDoctor($specialization, $name, $surname, $email, $password, $security_question, $security_answer);
		if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["id"] = $user["id"];
            $response["user"]["specialization"] = $user["specialization"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["surname"] = $user["surname"];
			$response["user"]["email"] = $user["email"];
			$response["user"]["password"] = $user["password"];
			$response["user"]["security_question"] = $user["security_question"];
			$response["user"]["security_answer"] = $user["security_answer"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred!";
            echo json_encode($response);
        }
    }

} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters missing!";
    echo json_encode($response);
}
?>