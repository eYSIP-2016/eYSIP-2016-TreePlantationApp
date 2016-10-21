<?php

//this is for Plant by organisation part
//this uploads pics to the server for the trees that are planted by the organisation

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		$imagestring = $_POST['imagestring'];
		$ptid = $_POST['pt_id'];
		$onlineDirectory = $_POST['OnlineDirectory'];

		require_once('dbConnect.php');
		
		$sql ="SELECT PlantedImages_Id FROM PlantedImages ORDER BY PlantedImages_Id ASC";
		
		$res = mysqli_query($con,$sql);
		
		$id = 0;
		
		while($row = mysqli_fetch_array($res)){
				$id = $row['PlantedImages_Id'];
		}
		
		$path = "plantedTreeImages/$id.jpg";
		
		//$actualpath = "http://10.0.2.2/androidPHP/$path";
		$actualpath = "$onlineDirectory"."$path";
		
		$sql = "INSERT INTO PlantedImages (PlantedImages_ptID,PlantedImages_imageUrl) VALUES ('$ptid','$actualpath')";
		
		if(mysqli_query($con,$sql)){
			$decoded = base64_decode($imagestring);
			
			if(file_put_contents($path,$decoded)){
				echo "Success";
			}
			else{
				echo "file_put_contents error";			
			}
		}
		
		mysqli_close($con);
	}else{
		echo "Error";
	}
