<?php 
// Called in PlantList activity under plant by organisation to get all the trees available with the 
//organisation to plant from the table Trees.
		require_once('dbConnect.php');
		$sql = "SELECT * FROM Trees";
		$r = mysqli_query($con,$sql);
	
		$result = array();

		while($row = mysqli_fetch_array($r)){
			array_push($result,array(
				"id"=>$row['Trees_Id'],
				"name"=>$row['Trees_Name'],
				"source"=>$row['Trees_Source'],
				"genus"=>$row['Trees_Genus'],
				"SeedSapling"=>$row['Trees_SeedSapling'],
				"imageurl"=>$row['Trees_Imageurl']
				)
		);
		}
		echo json_encode(array("result"=>$result));
		mysqli_close($con);

?>
