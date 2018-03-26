<?php

require 'init.php';


$user_id = $_POST['user_id'];
$specialty = $_POST['specialty'];
$education = $_POST['education'];
$profile_desc = $_POST['profile_desc']; 

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

	$i = 0;
	
	if($stmt = $mysqli->prepare("UPDATE veterinarians SET specialty = ?, education = ?, profile_desc = ? WHERE user_id = ?")){
		$stmt->bind_param("ssss", $specialty, $education, $profile_desc, $user_id);
		$stmt->execute();
		$stmt->close();
	}
	else{
		//echo 'and here?';
		
	}
		
	
	
}

?>