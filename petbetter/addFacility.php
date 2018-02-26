<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$facility = json_decode(file_get_contents('php://input'),true);

$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
		if($stmt = $mysqli->prepare("INSERT INTO facilities (faci_name, location, hours_open, hours_close, contact_info, vet_id, rating) VALUES (?,?,?,?,?,?,?)")){
			$stmt->bind_param(
				"sssssss",
				$facility['faci_name'],
				$facility['location'],
				$facility['hours_open'],
				$facility['hours_close'],
				$facility['contact_info'],
				$facility['vet_id'], 
				$facility['rating']);
			$stmt->execute();
			$stmt->close();
			echo 'Facility added';
		}
		else{
			echo 'Failed to add to db';
		}
		
	}
		

?>