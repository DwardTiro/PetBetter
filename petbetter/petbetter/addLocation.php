<?php

require 'init.php';

$location = json_decode(file_get_contents('php://input'),true);

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
		if($stmt = $mysqli->prepare("INSERT INTO markers (bldg_name, longitude, latitude, location, user_id, type, faci_id) VALUES (?,?,?,?,?,?,?)")){
			$stmt->bind_param(
				"sssssss",
				$location['bldg_name'],
				$location['longitude'],
				$location['latitude'],
				$location['location'],
				$location['user_id'],
				$location['type'], 
				$location['vet_id']);
			$stmt->execute();
			$stmt->close();
			echo 'Location added';
		}
		else{
			echo 'Failed to add to db';
		}
		
	}
		

?>