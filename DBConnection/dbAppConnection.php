//db_config.php
<?php
 
/*
 * @Author Shapon Pal <shaponpal4@gmail.com>
 * @Version 1.0
 * @Package Android MySQL CRUD Tutorial <freeprojectcode.com>
 */
 
//Defining Constants
 define('HOST','localhost');
 define('USER','root');
 define('PASS','');
 define('DB','doctor_order_termin_app');
 
 //Connecting to Database
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
?>