<?php
// PlantByOrg
// Called in MyTreeList activity to get all the trees planted by the user from JOIN of plantedTrees and
// Trees on treeId

	if($_SERVER['REQUEST_METHOD']=='GET'){		
		require_once('dbConnect.php');


		$user=$_GET['id'];
		$sql = "SELECT 
				plantedTrees.plantedTrees_Id,
				plantedTrees.plantedTrees_TreeId,
				plantedTrees.plantedTrees_UserId,
				plantedTrees.plantedTrees_Name,
				plantedTrees.plantedTrees_Latitude,
				plantedTrees.plantedTrees_Longitude,				
				Trees.Trees_Genus,
				Trees.Trees_SeedSapling,
				Trees.Trees_Source,
				Trees.Trees_Name,
				Trees.Trees_Imageurl
			FROM plantedTrees INNER JOIN Trees ON 
				plantedTrees.plantedTrees_TreeId=Trees.Trees_Id 
			WHERE plantedTrees_UserId=".$user;
		$r = mysqli_query($con,$sql);
	
		$result = array();

		while($row = mysqli_fetch_array($r)){
			array_push($result,array(
				"pt_id"=>$row['plantedTrees_Id'],
				"pt_lat"=>$row['plantedTrees_Latitude'],
				"pt_lon"=>$row['plantedTrees_Longitude'],
				"pt_name"=>$row['plantedTrees_Name'],
				"pt_tree_id"=>$row['plantedTrees_TreeId'],
				"pt_user_id"=>$row['plantedTrees_UserId'],
				"t_genus"=>$row['Trees_Genus'],
				"t_image_url"=>$row['Trees_Imageurl'],
				"t_seed_sapling"=>$row['Trees_SeedSapling'],
				"t_source"=>$row['Trees_Source'],
				"t_name"=>$row['Trees_Name']
				
			)
		);
		}
		echo json_encode(array("result"=>$result));
		mysqli_close($con);
	}
?>
