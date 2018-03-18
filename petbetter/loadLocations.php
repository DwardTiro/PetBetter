<?php

require 'init.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("SELECT * FROM markers")){
		
		$stmt->execute();
		$stmt->bind_result($_id, $bldg_name, $latitude, $longitude,  $location, $user_id, $type, $faci_id);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array(
					'_id'=>$_id,
					'bldg_name'=>$bldg_name,
					'latitude'=>$latitude,
					'longitude'=>$longitude,
					'location'=>$location,
					'user_id'=>$user_id,
					'type'=>$type,
					'faci_id'=>$faci_id));
			}while($stmt->fetch());
			
			
			$stmt->close();
			
			echo json_encode($response);

		}
		else{
			
			$stmt->close();
			echo 'SQL Query Error';
		}

	}
}

?>