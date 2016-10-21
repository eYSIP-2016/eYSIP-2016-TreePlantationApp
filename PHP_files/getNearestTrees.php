<?php 

 if($_SERVER['REQUEST_METHOD']=='POST'){

	require_once('dbConnect.php'); 

	$Latitude = $_POST['Latitude'];
	$Longitude = $_POST['Longitude'];


//within 2 meters of range approx
	$hLat = $Latitude + 0.00001;
	$lLat = $Latitude - 0.00001;
	$hLon = $Longitude + 0.00001;
	$lLon = $Longitude - 0.00001;

	$sql = "SELECT MyPlantedTrees_Id
		FROM MyPlantedTrees WHERE MyPlantedTrees_Lat BETWEEN $lLat AND $hLat AND MyPlantedTrees_Lon BETWEEN $lLon AND $hLon;";

	$r = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($r)){
		array_push($result,array(
			"MyPlantedTrees_Id"=>$row['MyPlantedTrees_Id'],
			
		)
	);
	}
	echo json_encode(array("result"=>$result));
	mysqli_close($con);
 }

?>
