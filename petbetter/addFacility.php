<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$facility = json_decode(file_get_contents('php://input'),true);

$respone = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$title = substr(md5(rand()), 0, 7);
	$upload_path = "uploads/facilities/$title.jpg";
		
	if(!($facility['faci_photo']==null)){
		file_put_contents($upload_path, base64_decode($facility['faci_photo']));
	}
	else{
		$upload_path = null;
	}
	if($stmt = $mysqli->prepare("INSERT INTO facilities (faci_name, location, contact_info, vet_id, rating, faci_photo) VALUES (?,?,?,?,?,?)")){
		$stmt->bind_param("ssssss", $facility['faci_name'], $facility['location'], $facility['contact_info'], $facility['vet_id'], $facility['rating'], $upload_path);
		$stmt->execute();
		$stmt->close();
		echo 'Facility added';
	}
	else{
		echo 'Failed to add to db';
	}
		
}
		

?>