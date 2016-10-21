<?php
// this php file is called from RegisterUser activity to register a user in the users table.
if($_SERVER['REQUEST_METHOD']=='POST'){

	$username = $_POST['Email'];
	$password = $_POST['Password'];


	require_once('dbConnect.php');


	$sql = "INSERT INTO users (users_Email, users_Password)
	VALUES ('$username','$password');";



	if (mysqli_query($con, $sql)) {
		echo "Success" ;
	} 
	else {
		echo mysqli_error($con)  ;
	}
}

mysqli_close($con);
?> 
