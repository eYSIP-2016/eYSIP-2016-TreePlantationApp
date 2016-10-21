<?php 
//plantbyorg
//called in mytreephotos activity to get the url of the images of the plantedTree_id

 if($_SERVER['REQUEST_METHOD']=='GET'){
 
	 $ptId = $_GET['ptid'];


	 require_once('dbConnect.php');
	 
	 $sql = "SELECT * FROM PlantedImages WHERE PlantedImages_ptID='".$ptId."';";
	 $r = mysqli_query($con,$sql);
	 
	$res = mysqli_fetch_array($r);
	$result = array();
	array_push($result,array(
		"id"=>$res['PlantedImages_Id'],
		"ptID"=>$res['PlantedImages_ptID'],
		"imageUrl"=>$res['PlantedImages_imageUrl']
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
