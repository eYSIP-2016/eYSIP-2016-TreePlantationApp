<?php 
//This php file is called by LoginActivity, takes email and password as parameters and returns Id and email
// if exists in the users table

 if($_SERVER['REQUEST_METHOD']=='POST'){
 
	 $username = $_POST['Email'];
	 $password = $_POST['Password'];

	 require_once('dbConnect.php');
	 
	 $sql = "SELECT * FROM users WHERE users_Email='".$username."' AND users_Password='".$password."';";
	 $r = mysqli_query($con,$sql);
	 
	$res = mysqli_fetch_array($r);
	$result = array();
	array_push($result,array(
		"Id"=>$res['users_Id'],
		"Email"=>$res['users_Email']
		)
	);



	 if(!empty($res)){
		echo json_encode(array("result"=>$result));
	 }else{
	 	echo "failure";
	 }

	 mysqli_close($con);
 }

?>
