<?php

require 'init.php';

$location = json_decode(file_get_contents('php://input'),true);

$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
		if($stmt = $mysqli->prepare("INSERT INTO markers (bldg_name, longitude, latitude, location, user_id, type, faci_id) VALUES (?,?,?,?,?,?,?)")){
			$stmt->bind_param(
				"sssssss",
				$markers['bldg_name'],
				$markers['longitude'],
				$markers['latitude'],
				$markers['location'],
				$markers['user_id'],
				$markers['type'], 
				$markers['vet_id']);
			$stmt->execute();
			$stmt->close();
			echo 'Location added';
		}
		else{
			echo 'Failed to add to db';
		}
		
	}
		

?>