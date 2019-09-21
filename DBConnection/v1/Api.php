<?php 
 
 //getting the dboperation class
 require_once '../includes/DbOperation.php';
 
 //function validating all the paramters are available
 //we will pass the required parameters to this function 
 function isTheseParametersAvailable($params){
 //assuming all parameters are available 
 $available = true; 
 $missingparams = ""; 
 
 foreach($params as $param){
 if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
 $available = false; 
 $missingparams = $missingparams . ", " . $param; 
 }
 }
 
 //if parameters are missing 
 if(!$available){
 $response = array(); 
 $response['error'] = true; 
 $response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
 
 //displaying error
 echo json_encode($response);
 
 //stopping further execution
 die();
 }
 }
 
 //an array to display response
 $response = array();
 
 //if it is an api call 
 //that means a get parameter named api call is set in the URL 
 //and with this parameter we are concluding that it is an api call
 if(isset($_GET['apicall'])){
 
 switch($_GET['apicall']){
 
 //--------------------------------------------------------------------------------------------------------------------------------------------
 //the CREATE operation
 //if the api call value is 'createDoctor'
 //we will create a record in the database
 case 'createDoctor':
 //first check the parameters required for this request are available or not 
 isTheseParametersAvailable(array('specialization', 'name', 'surname', 'email', 'password', 'security_question', 'security_answer'));
 
 //creating a new dboperation object
 $db = new DbOperation();
 
 //creating a new record in the database
 $result = $db->createDoctor(
 $_POST['specialization'],
 $_POST['name'],
 $_POST['surname'],
 $_POST['email'],
 $_POST['password'],
 $_POST['security_question'],
 $_POST['security_answer']
 );
 
 
 //if the record is created adding success to response
 if($result){
 //record is created means there is no error
 $response['error'] = false; 
 
 //in message we have a success message
 $response['message'] = 'Doctor addedd successfully';
 
 //and we are getting all the heroes from the database in the response
 $response['doctors'] = $db->getDoctors();
 }else{
 
 //if record is not added that means there is an error 
 $response['error'] = true; 
 
 //and we have the error message
 $response['message'] = 'Some error occurred please try again';
 }
 
 break; 
 
 //-------------------------------------------------------------------------------------------------------------------------------
 
 //book termin for doctor Tomas Mako
 case 'book_termin_in_mako_tomas':
 
//first check the parameters required for this request are available or not 
 isTheseParametersAvailable(array('patientName', 'patientSurname', 'terminDate', 'terminTime', 'stavTerminu', 'emailForConfirm'));
 
 //creating a new dboperation object
 $db = new DbOperation();
 
 //creating a new record in the database
 $result = $db->book_termin_in_mako_tomas(
 $_POST['patientName'],
 $_POST['patientSurname'],
 $_POST['terminDate'],
 $_POST['terminTime'],
 $_POST['stavTerminu'],
 $_POST['emailForConfirm']
 );
 
 //if the record is created adding success to response
 if($result){
 //record is created means there is no error
 $response['error'] = false; 
 
 //in message we have a success message
 $response['message'] = 'Doctor addedd successfully';
 
 //and we are getting all the heroes from the database in the response
 $response['tomasMakoRecords'] = $db->getRecordsFromMakoTomasTable();
 }else{
 
 //if record is not added that means there is an error 
 $response['error'] = true; 
 
 //and we have the error message
 $response['message'] = 'Some error occurred please try again';
 }
 
 break;
 
 //--------------------------------------------------------------------------------------------------------------------------------
 
 case 'checkifDoctorHaveFreeChosenTermin_mako_tomas':
 $db = new DbOperation();
  $result = $db->checkifDoctorHaveFreeChosenTermin_mako_tomas($_GET['terminDate'],$_GET['terminTime']);
  if($result){
	 $response['error'] = true;
	$response['message'] = 'Termin is not free!';
 }else{
 $response['error'] = false; 
 $response['message'] = 'Termin is free in DB, can be add!';
 $response['termin'] = $db->checkifDoctorHaveFreeChosenTermin_mako_tomas($_GET['terminDate'],$_GET['terminTime']);
 }
 
 break;
 
 //----------------------------------------------------------------------------------------------------------------------------------
 
 //book termin for doctor Igor Buzinsky
 case 'book_termin_in_buzinsky_igor':
 
//first check the parameters required for this request are available or not 
 isTheseParametersAvailable(array('patientName', 'patientSurname', 'terminDate', 'terminTime'));
 
 //creating a new dboperation object
 $db = new DbOperation();
 
 //creating a new record in the database
 $result = $db->book_termin_in_buzinsky_igor(
 $_POST['patientName'],
 $_POST['patientSurname'],
 $_POST['terminDate'],
 $_POST['terminTime']
 );
 
 break;
 
 //------------------------------------------------------------------------------------------------------------------------------------
 
 //the READ operation
 //if the call is getdoctors
 case 'getDoctors':
 $db = new DbOperation();
 $response['error'] = false; 
 $response['message'] = 'Request successfully completed';
 $response['doctors'] = $db->getDoctors();
 break; 
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 // get doctor termins
 case 'getDoctorTermins':
 $db = new DbOperation();
 $result = $db->getDoctorTermins($_GET['username']);
 $response['error'] = false; 
 $response['message'] = 'Request successfully completed';
 $response['doctorTermin'] = $db->getDoctorTermins($_GET['username']);
 break;
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 case 'getDoctorTerminsBaseOfDateTomasMako':
 $db = new DbOperation();
 $result = $db->getDoctorTerminsBaseOfDateTomasMako($_GET['terminDate']);
 $response['error'] = false;
 $response['message'] = 'Request successfully completed';
 $response['doctorTermin'] = $db->getDoctorTerminsBaseOfDateTomasMako($_GET['terminDate']);
 break;
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 case 'getDoctorTerminsBaseOfDateIgorBuzinsky':
 $db = new DbOperation();
 $result = $db->getDoctorTerminsBaseOfDateIgorBuzinsky($_GET['terminDate']);
 $response['error'] = false;
 $response['message'] = 'Request successfully completed';
 $response['doctorTermin'] = $db->getDoctorTerminsBaseOfDateIgorBuzinsky($_GET['terminDate']);
 break;
 
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 // check if termin is available
 case 'checkTerminInDoctorIfIsFree':
 $db = new DbOperation();
 $result = $db->checkTerminInDoctorIfIsFree($_GET['doctordb'],$_GET['terminDate'],$_GET['terminTime']);
 //$doctordb, $patientName, $patientSurname, $terminDate, $terminTime
 $response['error'] = false; 
 $response['message'] = 'Request successfully completed';
 $response['doctorTermin'] = $db->checkTerminInDoctorIfIsFree($_GET['doctordb'],$_GET['terminDate'],$_GET['terminTime']);
 break;
 
 //--------------------------------------------------------------------------------------------------------------------------------------
 
 // the GetDoctorByEmailAndPassword
 case 'getDoctorByEmailAndPassword':
 //isTheseParametersAvailable(array('email','password'));
 $db = new DbOperation();
 //$result = $db->getDoctorByEmailAndPassword($_GET['email']);
 $result = $db->getDoctorByEmailAndPassword($_GET['email'],$_GET['password']);
 $response['error'] = false; 
 $response['message'] = 'Request successfully completed';
 //$response['doctors'] = $db->getDoctorByEmailAndPassword($_GET['email']);
 $response['doctors'] = $db->getDoctorByEmailAndPassword($_GET['email'],$_GET['password']);
 break;
 
 //--------------------------------------------------------------------------------------------------------------------------------------
 
 // the LoginDoctorByNameAndVerifiPassword
 case 'loginDoctorByNameAndVerifiPassword':
 $db = new DbOperation();
 $response['error'] = false; 
 $response['message'] = 'Request successfully completed';
 $response['doctor'] = $db->loginDoctorByNameAndVerifiPassword();
 break;
 
 //-------------------------------------------------------------------------------------------------------------------------------------
 
 case 'checkIfDoctorAlreadyExistByNameAndSurname':
 //isTheseParametersAvailable(array('email','password'));
 $db = new DbOperation();
 //$result = $db->getDoctorByEmailAndPassword($_GET['email']);
 //$result = $db->checkIfDoctorAlreadyExistByNameAndSurname($_GET['email'],$_GET['password']);
  $result = $db->checkIfDoctorAlreadyExistByNameAndSurname($_GET['name'],$_GET['surname']);
 if($result){
	 $response['error'] = true;
	$response['message'] = 'Doctor already exist!';
 }else{
 $response['error'] = false; 
 $response['message'] = 'Doctor is not in DB, can be add!';
 //$response['doctors'] = $db->getDoctorByEmailAndPassword($_GET['email']);
 //$response['doctors'] = $db->checkIfDoctorAlreadyExistByNameAndSurname($_GET['email'],$_GET['password']);
 $response['doctors'] = $db->checkIfDoctorAlreadyExistByNameAndSurname($_GET['name'],$_GET['surname']);
 }
 break;
 
 //--------------------------------------------------------------------------------------------------------------------------------------
 
 //the UPDATE operation
 case 'updateTerminInDoctorForTomasMako':
 isTheseParametersAvailable(array('id','patientName','patientSurname','terminTime','terminDate', 'stavTerminu'));
 $db = new DbOperation();
 $result = $db->updateTerminInDoctorForTomasMako(
 $_POST['id'],
 $_POST['patientName'],
 $_POST['patientSurname'],
 $_POST['terminTime'],
 $_POST['terminDate'],
 $_POST['stavTerminu']
 );
 
 if($result){
 $response['error'] = false; 
 $response['message'] = 'Termin updated successfully';
 //$response['termins'] = $db->getHeroes();
 }else{
 $response['error'] = true; 
 $response['message'] = 'Some error occurred please try again';
 }
 break; 
 
 //--------------------------------------------------------------------------------------------------------------------------------------
 
 //the UPDATE operation
 case 'updatedoctor':
 isTheseParametersAvailable(array('id','name','realname','rating','teamaffiliation'));
 $db = new DbOperation();
 $result = $db->updateHero(
 $_POST['id'],
 $_POST['name'],
 $_POST['realname'],
 $_POST['rating'],
 $_POST['teamaffiliation']
 );
 
 if($result){
 $response['error'] = false; 
 $response['message'] = 'Doctor updated successfully';
 $response['heroes'] = $db->getHeroes();
 }else{
 $response['error'] = true; 
 $response['message'] = 'Some error occurred please try again';
 }
 break; 
 
 //---------------------------------------------------------------------------------------------------------------------------------------
 
 //the delete operation
 case 'deletedoctor':
 
 //for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
 if(isset($_GET['id'])){
 $db = new DbOperation();
 if($db->deleteHero($_GET['id'])){
 $response['error'] = false; 
 $response['message'] = 'Doctor deleted successfully';
 $response['heroes'] = $db->getHeroes();
 }else{
 $response['error'] = true; 
 $response['message'] = 'Some error occurred please try again';
 }
 }else{
 $response['error'] = true; 
 $response['message'] = 'Nothing to delete, provide an id please';
 }
 break; 
 
 //------------------------------------------------------------------------------------------------------------------------------------------
 
 }
 
 }else{
 //if it is not api call 
 //pushing appropriate values to response array 
 $response['error'] = true; 
 $response['message'] = 'Invalid API Call';
 }
 
 //displaying the response in json structure 
 echo json_encode($response);
 ?>