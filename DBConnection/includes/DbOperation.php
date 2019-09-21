<?php
 
class DbOperation
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/DbConnect.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new DbConnect();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->con = $db->connect();
    }
 
 /*
 * The create operation
 * When this method is called a new record is created in the database
 */
 function createDoctor($specialization, $name, $surname, $email, $password, $security_question, $security_answer){
//$password = password_hash($password, PASSWORD_DEFAULT);		
 $stmt = $this->con->prepare("INSERT INTO doctors (specialization, name, surname, email, password, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?)");
 $stmt->bind_param("sssssss", $specialization, $name, $surname, $email, $password, $security_question, $security_answer);
 if($stmt->execute())
 return true; 
 return false; 
 }
 
 //----------------------------------------------------------------------------------------------------------------------------------
 
 /*
 * bookTerminForDoctor Tomas Mako
 */
 function book_termin_in_mako_tomas($patientName, $patientSurname, $terminDate, $terminTime, $stavTerminu, $emailForConfirm){
	 $stmt = $this->con->prepare("INSERT INTO mako_tomas (patientName, patientSurname, terminDate, terminTime, stavTerminu, emailForConfirm) VALUES (?,?,?,?,?,?)");
	 $stmt->bind_param("ssssss", $patientName, $patientSurname, $terminDate, $terminTime, $stavTerminu, $emailForConfirm);
	 if($stmt->execute())
 return true; 
 return false;
 }
 
 //-----------------------------------------------------------------------------------------------------------------------------------
 
 /*
 * get all records from Tomas Mako table
 */
 function getRecordsFromMakoTomasTable() {
	 $stmt = $this->con->prepare("SELECT patientName, patientSurname, emailForConfirm, terminDate, terminTime, stavTerminu FROM mako_tomas ");
 $stmt->execute();
 $stmt->bind_result($patientName, $patientSurname, $emailForConfirm, $terminDate, $terminTime, $stavTerminu);
 
 $tomasMakoRecords = array(); 
 
 while($stmt->fetch()){
 $tomasMako  = array(); 
 $tomasMako['patientName'] = $patientName; 
 $tomasMako['patientSurname'] = $patientSurname; 
 $tomasMako['emailForConfirm'] = $emailForConfirm;
 $tomasMako['terminDate'] = $terminDate; 
 $tomasMako['terminTime'] = $terminTime;
 $tomasMako['stavTerminu'] = $stavTerminu;
 
 array_push($tomasMakoRecords, $tomasMako); 
 }
 
 return $tomasMakoRecords; 
 }
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 // Check if doctor have free termin 
 
 function checkifDoctorHaveFreeChosenTermin_mako_tomas($terminDate,$terminTime){
	$stmt = $this->con->prepare("SELECT terminDate, terminTime FROM mako_tomas WHERE terminDate = ? AND terminTime = ?"); 
	$stmt->bind_param("ss", $terminDate, $terminTime);
	$stmt->execute();
	$stmt->bind_result($terminDate, $terminTime);
	
	$termin = array(); 
 
	while($stmt->fetch()){
	$selectedtermin  = array();
	
	$selectedtermin['terminDate'] = $terminDate; 
	$selectedtermin['terminTime'] = $terminTime;
	
	array_push($termin, $selectedtermin); 
 }
 
 return $termin; 
 }
 
 //---------------------------------------------------------------------------------------------------------------------------------------
 
 /*
 * bookTerminForDoctor Igor Buzinsky
 */
 function book_termin_in_buzinsky_igor($patientName, $patientSurname, $terminDate, $terminTime){
	 $stmt = $this->con->prepare("INSERT INTO buzinsky_igor (patientName, patientSurname, terminDate, terminTime) VALUES (?,?,?,?)");
	 $stmt->bind_param("ssss", $patientName, $patientSurname, $terminDate, $terminTime);
	 if($stmt->execute())
 return true; 
 return false;
 }
 
 //----------------------------------------------------------------------------------------------------------------------------------------
 
 /*
 * The read operation
 * When this method is called it is returning all the existing record of the database
 */
 function getDoctors(){
 $stmt = $this->con->prepare("SELECT id, specialization, name, surname, email, password, security_question, security_answer FROM doctors");
 $stmt->execute();
 $stmt->bind_result($id, $specialization, $name, $surname, $email, $password, $security_question, $security_answer);
 
 $doctors = array(); 
 
 while($stmt->fetch()){
 $doctor  = array();
 $doctor['id'] = $id; 
 $doctor['specialization'] = $specialization; 
 $doctor['name'] = $name; 
 $doctor['surname'] = $surname; 
 $doctor['email'] = $email;
 $doctor['password'] = $password; 
 $doctor['security_question'] = $security_question; 
 $doctor['security_answer'] = $security_answer;
 
 array_push($doctors, $doctor); 
 }
 
 return $doctors; 
 }
 
 //------------------------------------------------------------------------------------------------------------------------------------------------
 
 // get doctor termins
 function getDoctorTermins($username){
	 $stmt = $this->con->prepare("SELECT id, patientName, patientSurname, emailForConfirm, terminDate, terminTime, stavTerminu FROM $username ORDER BY id DESC");
	 //$stmt->bind_param("s", $username);
	 $stmt->execute();
	 $stmt->bind_result($id, $patientName, $patientSurname, $emailForConfirm, $terminDate, $terminTime, $stavTerminu);
	 
	 $doctorTermins = array();

	 while($stmt->fetch()){
 $doctorTermin  = array();
 $doctorTermin['id'] = $id;
 $doctorTermin['patientName'] = $patientName; 
 $doctorTermin['patientSurname'] = $patientSurname; 
 $doctorTermin['emailForConfirm'] = $emailForConfirm;
 $doctorTermin['terminDate'] = $terminDate; 
 $doctorTermin['terminTime'] = $terminTime; 
 $doctorTermin['stavTerminu'] = $stavTerminu; 
 
 array_push($doctorTermins, $doctorTermin); 
 }
 
 return $doctorTermins; 
 }
 
 //---------------------------------------------------------------------------------------------------------------------------------------------------
 
 function getDoctorTerminsBaseOfDateTomasMako($terminDate){
	
	$stmt = $this->con->prepare("SELECT patientName, patientSurname, terminDate, terminTime, stavTerminu FROM mako_tomas WHERE terminDate = ?");
	$stmt->bind_param("s", $terminDate); 
	$stmt->execute();
	$stmt->bind_result($patientName, $patientSurname, $terminDate, $terminTime, $stavTerminu);
	
	$doctorTermins = array();
	
		 while($stmt->fetch()){
 $doctorTermin  = array();
 
 $doctorTermin['patientName'] = $patientName; 
 $doctorTermin['patientSurname'] = $patientSurname;

 $doctorTermin['terminDate'] = $terminDate; 
 $doctorTermin['terminTime'] = $terminTime;
 $doctorTermin['stavTerminu'] = $stavTerminu; 
 
 array_push($doctorTermins, $doctorTermin); 
 }
 
 return $doctorTermins; 
 }
 
 //---------------------------------------------------------------------------------------------------------------------------------------------------
 
  function getDoctorTerminsBaseOfDateIgorBuzinsky($terminDate){
	
	$stmt = $this->con->prepare("SELECT patientName, patientSurname, terminDate, terminTime FROM buzinsky_igor WHERE terminDate = ?");
	$stmt->bind_param("s", $terminDate); 
	$stmt->execute();
	$stmt->bind_result($patientName, $patientSurname, $terminDate, $terminTime);
	
	$doctorTermins = array();
	
		 while($stmt->fetch()){
 $doctorTermin  = array();
 $doctorTermin['patientName'] = $patientName; 
 $doctorTermin['patientSurname'] = $patientSurname; 
 $doctorTermin['terminDate'] = $terminDate; 
 $doctorTermin['terminTime'] = $terminTime; 
 
 array_push($doctorTermins, $doctorTermin); 
 }
 
 return $doctorTermins; 
 }
 
 
 //---------------------------------------------------------------------------------------------------------------------------------------------------
 
 //function for check if termin is available
 function checkTerminInDoctorIfIsFree($doctordb, $terminDate, $terminTime){
	$stmt = $this->con->prepare("SELECT patientName, patientSurname, terminDate, terminTime FROM $doctordb WHERE $terminDate AND $terminTime");
	//$stmt->bind_param("ss", $terminDate, $terminTime);
	$stmt->execute();
	$stmt->bind_result($patientName, $patientSurname, $terminDate, $terminTime);
			
	$doctorTermins = array();

	while($stmt -> fetch()){
		$doctorTermin  = array();
		$doctorTermin['patientName'] = $patientName; 
		$doctorTermin['patientSurname'] = $patientSurname; 
		$doctorTermin['terminDate'] = $terminDate; 
		$doctorTermin['terminTime'] = $terminTime;
	array_push($doctorTermins, $doctorTermin);		
	}
	return $doctorTermins; 
 }
 
 //---------------------------------------------------------------------------------------------------------------------------------------------------
 
 //Get doctor by email and password
 function getDoctorByEmailAndPassword($email,$password){
 //function getDoctorByEmailAndPassword($email){ 	 
	 //$stmt = $this->con->prepare("SELECT * FROM doctors WHERE email = ? AND password = ?");
	 $stmt = $this->con->prepare("SELECT email, password FROM doctors WHERE email = ? AND password = ?");
	 //$stmt->bind_param("s", $email);
	 $stmt->bind_param("ss", $email, $password);
	 $stmt->execute();
	//$stmt->bind_result($id, $specialization, $name, $surname, $email, $password, $security_question, $security_answer);
	$stmt->bind_result($email, $password);
 
 $doctors = array(); 
 
 while($stmt->fetch()){
 $doctor  = array();
 //$doctor['id'] = $id; 
 //$doctor['specialization'] = $specialization; 
 //$doctor['name'] = $name; 
 //$doctor['surname'] = $surname; 
 $doctor['email'] = $email;
 $doctor['password'] = $password; 
 //$doctor['security_question'] = $security_question; 
 //$doctor['security_answer'] = $security_answer;
 
 array_push($doctors, $doctor); 
 }
 
 return $doctors; 
 }
 
 //---------------------------------------------------------------------------------------------------------------------------------------------------
 
 //Get doctor by email and surname
 function checkIfDoctorAlreadyExistByNameAndSurname($name,$surname){
 //function getDoctorByEmailAndPassword($email){ 	 
	 //$stmt = $this->con->prepare("SELECT * FROM doctors WHERE email = ? AND password = ?");
	 //$stmt = $this->con->prepare("SELECT email, password FROM doctors WHERE email = ? AND password = ?");
	 $stmt = $this->con->prepare("SELECT name, surname FROM doctors WHERE name = ? AND surname = ?");
	 //$stmt->bind_param("s", $email);
	 //$stmt->bind_param("ss", $email, $password);
	 $stmt->bind_param("ss", $name, $surname);
	 $stmt->execute();
	//$stmt->bind_result($id, $specialization, $name, $surname, $email, $password, $security_question, $security_answer);
	//$stmt->bind_result($email, $password);
	$stmt->bind_result($name, $surname);
 
 $doctors = array(); 
 
 while($stmt->fetch()){
 $doctor  = array();
 //$doctor['id'] = $id; 
 //$doctor['specialization'] = $specialization; 
 $doctor['name'] = $name; 
 $doctor['surname'] = $surname; 
 //$doctor['email'] = $email;
 //$doctor['password'] = $password; 
 //$doctor['security_question'] = $security_question; 
 //$doctor['security_answer'] = $security_answer;
 
 array_push($doctors, $doctor); 
 }
 
 return $doctors; 
 }
 
 //--------------------------------------------------------------------------------------------------------------------------------------------------
 
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
	
//---------------------------------------------------------------------------------------------------------------------------------------------------	
 
 /*
 * The update operation
 * When this method is called the record with the given id is updated with the new given values
 */
 function updateTerminInDoctorForTomasMako($id, $patientName, $patientSurname, $terminTime, $terminDate, $stavTerminu){
 $stmt = $this->con->prepare("UPDATE mako_tomas SET patientName = ?, patientSurname = ?, terminTime = ?, terminDate =?, stavTerminu = ? WHERE id = ?");
 $stmt->bind_param("sssssi", $patientName, $patientSurname, $terminTime, $terminDate, $stavTerminu, $id );
 if($stmt->execute())
 return true; 
 return false; 
 }
 
//--------------------------------------------------------------------------------------------------------------------------------------------------- 
 
 /*
 * The update operation
 * When this method is called the record with the given id is updated with the new given values
 */
 function updateDoctor($id, $name, $realname, $rating, $teamaffiliation){
 $stmt = $this->con->prepare("UPDATE heroes SET name = ?, realname = ?, rating = ?, teamaffiliation = ? WHERE id = ?");
 $stmt->bind_param("ssisi", $name, $realname, $rating, $teamaffiliation, $id);
 if($stmt->execute())
 return true; 
 return false; 
 }
 
 //--------------------------------------------------------------------------------------------------------------------------------------------------
 
 /*
 * The delete operation
 * When this method is called record is deleted for the given id 
 */
 function deleteDoctor($id){
 $stmt = $this->con->prepare("DELETE FROM heroes WHERE id = ? ");
 $stmt->bind_param("i", $id);
 if($stmt->execute())
 return true; 
 
 return false; 
 }
}
?>