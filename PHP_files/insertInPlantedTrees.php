<?php
// PlantbyOrg --
// Called from TreePlantedMessage, parameter are treeId,userId,treeName, latitude and longitude and
// a new entry is created in the plantedTrees table
if($_SERVER['REQUEST_METHOD']=='POST'){

	$treeIdString = $_POST['treeId'];
	$userIdString = $_POST['userId'];
	$treeName = $_POST['treeName'];
	$latitudeString = $_POST['latitude'];
	$longitudeString = $_POST['longitude'];

	$treeId = 0 + $treeIdString;
	$userId = 0 + $userIdString;
	$latitude = 0 + $latitudeString;
	$longitude = 0 + $longitudeString;

	require_once('dbConnect.php');


	$sql = "INSERT INTO plantedTrees (plantedTrees_TreeId, plantedTrees_UserId, plantedTrees_Name, plantedTrees_Latitude, plantedTrees_Longitude)
	VALUES ($treeId,$userId,'$treeName',$latitude,$longitude);";



	if (mysqli_query($con, $sql)) {
		echo "Success" ;
	} 
	else {
		echo mysqli_error($con)  ;
	}
}

mysqli_close($con);
?> 
