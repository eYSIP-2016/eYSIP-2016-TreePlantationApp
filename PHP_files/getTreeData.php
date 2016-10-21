<?php 
	if($_SERVER['REQUEST_METHOD']=='GET'){
	
		$id  = $_GET['id'];	
		require_once('dbConnect.php');
		$sql = "SELECT * FROM Trees WHERE Trees_id='".$id."'";
		$r = mysqli_query($con,$sql);
		$res = mysqli_fetch_array($r);
		$result = array();
		array_push($result,array(
			"id"=>$res['Trees_Id'],
			"name"=>$res['Trees_Name'],
			"source"=>$res['Trees_Source'],
			"genus"=>$res['Trees_Genus'],
			"SeedSapling"=>$res['Trees_SeedSapling'],
			"imageurl"=>$res['Trees_Imageurl']
			)
		);
	
		echo json_encode(array("result"=>$result));
		mysqli_close($con);
	}

?>
