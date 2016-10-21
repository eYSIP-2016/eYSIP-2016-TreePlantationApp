<?php

//this is for Plant yourself part
//this uploads pics to the server for the trees that you planted yourself

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		require_once('dbConnect.php');

		$userId = $_POST['UserId'];
		$numberPic = $_POST['NumberPic'];
		$timeStamp = $_POST['TimeStamp'];
		$latitude = $_POST['Latitude'] + 0;
		$longitude = $_POST['Longitude'] + 0;
		$actionCode = $_POST['Action'] +0;
				
		if($actionCode == 1){
				$species = $_POST['Species'];
				$comment = $_POST['Comment'];
				$onlineDirectory = $_POST['OnlineDirectory'];

				$sql ="SELECT MyPlantedTrees_Id FROM MyPlantedTrees ORDER BY MyPlantedTrees_Id ASC";
				$res = mysqli_query($con,$sql);
				$MyPlantedTreesId = 0;
				while($row = mysqli_fetch_array($res)){
					$MyPlantedTreesId = $row['MyPlantedTrees_Id']+1;
				}
				// ------------------------------------------------------------------------------------------------------------------------------------------------------
				$rootdir = getcwd()."/myPlantedTreeImages/$MyPlantedTreesId";
				if( !is_dir($rootdir) )
				{
					mkdir($rootdir);
				}
				//$rooturl = "http://10.0.2.2/androidPHP/myPlantedTreeImages/$MyPlantedTreesId";
				$rooturl = "$onlineDirectory"."$MyPlantedTreesId";
				$sql = "INSERT INTO MyPlantedTrees (MyPlantedTrees_Species,MyPlantedTrees_Comments,MyPlantedTrees_PhotoDirectory,MyPlantedTrees_Lat,MyPlantedTrees_Lon,MyPlantedTrees_dirId) VALUES ('$species','$comment','$rooturl',$latitude,$longitude,$MyPlantedTreesId)";
				if(mysqli_query($con,$sql)){
				}else{
					echo "failure in insert";		
				}

				$subdir = "$rootdir/$MyPlantedTreesId-$userId-$timeStamp";
				if( !is_dir($subdir) )
				{
					mkdir($subdir);
					echo "made directory";
				}
				$suburl = "$rooturl/$MyPlantedTreesId-$userId-$timeStamp";

				$nImages = $numberPic -1 ;
				$sql = "INSERT INTO MyPlantedTreeImages (MyPlantedTreeImages_Timestamp,MyPlantedTreeImages_TreeId,MyPlantedTreeImages_UserId,MyPlantedTreeImages_ImageDir,MyPlantedTreeImages_nImages) VALUES ('$timeStamp',$MyPlantedTreesId,$userId,'$suburl', $nImages)";
				if(mysqli_query($con,$sql)){
				}else{
					echo "failure in insert";		
				}


				
				for($i=0; $i<$numberPic-1 ; $i=$i+1 ){
					$path = "myPlantedTreeImages/$MyPlantedTreesId/$MyPlantedTreesId-$userId-$timeStamp/$i.jpg";

					$imagestring = $_POST['Image'.$i];
					$decoded = base64_decode($imagestring);
					
					if(file_put_contents($path,$decoded)){
						echo "Success";
					}
					else{
						echo "file_put_contents error";			
					}
				}

		}
		if($actionCode==2){
			$Latitude = $_POST['Latitude'];
			$Longitude = $_POST['Longitude'];
			$NearestTreeId = $_POST['NearestTreeId'];
			$onlineDirectory = $_POST['OnlineDirectory'];


			$sql = "SELECT MyPlantedTrees_Id,MyPlantedTrees_dirId
			FROM MyPlantedTrees WHERE MyPlantedTrees_Id = $NearestTreeId;";
			$r = mysqli_query($con,$sql);

			$result = array();
			$row = mysqli_fetch_array($r);
			$nearestTreeId = $row['MyPlantedTrees_Id']+0;
			$nearestTreeDirId = $row['MyPlantedTrees_dirId']+0;

			$rootdir = getcwd()."/myPlantedTreeImages/$nearestTreeDirId";
			
			if( !is_dir($rootdir) )
			{
				echo "some error occured, tree id exists but folder doesnt";
			}
			//$rooturl = "http://10.0.2.2/androidPHP/myPlantedTreeImages/$nearestTreeDirId";
			$rooturl = "$onlineDirectory"."$nearestTreeDirId";

			$subdir = "$rootdir/$nearestTreeDirId-$userId-$timeStamp";
			if( !is_dir($subdir) )
			{
				mkdir($subdir);
				echo "made sub directory";
			}
			$suburl = "$rooturl/$nearestTreeDirId-$userId-$timeStamp";

			$nImages = $numberPic -1 ;
			$sql = "INSERT INTO MyPlantedTreeImages (MyPlantedTreeImages_Timestamp,MyPlantedTreeImages_TreeId,MyPlantedTreeImages_UserId,MyPlantedTreeImages_ImageDir,MyPlantedTreeImages_nImages) VALUES ('$timeStamp',$nearestTreeId,$userId,'$suburl',$nImages)";
			if(mysqli_query($con,$sql)){
			}else{
				echo "failure in insert ". $suburl;	
			} 


			
			for($i=0; $i<$numberPic-1 ; $i=$i+1 ){
				$path = "myPlantedTreeImages/$nearestTreeDirId/$nearestTreeDirId-$userId-$timeStamp/$i.jpg";

				$imagestring = $_POST['Image'.$i];
				$decoded = base64_decode($imagestring);
				
				if(file_put_contents($path,$decoded)){
					echo "Success";
				}
				else{
					echo "file_put_contents error";			
				}
			}
		}
		mysqli_close($con);

	}else{
		echo "Error";
	}

?>
