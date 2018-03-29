<?php

require 'init.php';

$service_id = $_POST['service_id'];

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

	
	if($stmt = $mysqli->prepare("UPDATE services SET is_deleted = 1 WHERE _id = ?")){
		$stmt->bind_param("s", $service_id);
		$stmt->execute();
		$stmt->close();

		echo 'Success';
	}
	else{
		//echo 'and here?';
		echo 'SQL Query Error';
		
	}
		
	
	
}

?>