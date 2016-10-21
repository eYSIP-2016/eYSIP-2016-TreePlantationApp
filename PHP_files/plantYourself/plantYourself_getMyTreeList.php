<?php
if($_SERVER['REQUEST_METHOD']=='POST'){		
	require_once('dbConnect.php');


	$user=$_POST['Id'];


	$sql="SELECT MyPlantedTreeImages_Id,MyPlantedTreeImages_TreeId,MyPlantedTreeImages_Timestamp,MyPlantedTreeImages_ImageDir FROM MyPlantedTreeImages WHERE MyPlantedTreeImages_UserId=$user";
	$r = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){
		array_push($result,array(
				"MyPlantedTreeImages_Id"=>$row['MyPlantedTreeImages_Id'],
				"MyPlantedTreeImages_TreeId"=>$row['MyPlantedTreeImages_TreeId'],
				"MyPlantedTreeImages_Timestamp"=>$row['MyPlantedTreeImages_Timestamp'],
				"MyPlantedTreeImages_ImageDir"=>$row['MyPlantedTreeImages_ImageDir']
				)
			);
	}
	echo json_encode(array("result"=>$result));
	mysqli_close($con);
}
?>
