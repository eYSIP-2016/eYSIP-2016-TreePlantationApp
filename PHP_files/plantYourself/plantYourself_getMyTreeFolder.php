<?php
// plant Yourself
// called from MyGallery
// parameters - MyPlantedTreeImages_Id
// returns - directory and number of images corresponding to the id
if($_SERVER['REQUEST_METHOD']=='POST'){		
	require_once('dbConnect.php');


	$MyPlantedTreeImages_Id=$_POST['MyPlantedTreeImages_Id'];


	$sql="SELECT MyPlantedTreeImages_ImageDir,MyPlantedTreeImages_nImages FROM MyPlantedTreeImages WHERE MyPlantedTreeImages_Id=$MyPlantedTreeImages_Id";
	$r = mysqli_query($con,$sql);

	$result = array();

	$row = mysqli_fetch_array($r);
	array_push($result,array(
				"MyPlantedTreeImages_ImageDir"=>$row['MyPlantedTreeImages_ImageDir'],
				"MyPlantedTreeImages_nImages"=>$row['MyPlantedTreeImages_nImages']
				)
			);
	
	echo json_encode(array("result"=>$result));
	mysqli_close($con);
}
?>
