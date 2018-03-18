<?php

require 'init.php';

$user_id = $_POST['user_id'];

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("SELECT f.faci_id, f.faci_name, f.location, f.hours_open, f.hours_close, f.contact_info, f.vet_id, f.rating, f.faci_photo FROM `bookmarks` AS b JOIN facilities AS f ON b.item_id = f.faci_id WHERE b.bookmark_type = 1 AND b.user_id = ?")){
		$stmt->bind_param("s", $user_id);
		$stmt->execute();
		$stmt->bind_result($faci_id, $faci_name, $location, $hours_open, $hours_close, $contact_info, $vet_id, $rating, $faci_photo);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('faci_id'=>$faci_id,
				'faci_name'=>$faci_name,
				'location'=>$location,
				'hours_open'=>$hours_open,
				'hours_close'=>$hours_close,
				'contact_info'=>$contact_info,
				'vet_id'=>$vet_id,
				'rating'=>$rating,
				'faci_photo'=>$faci_photo));
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