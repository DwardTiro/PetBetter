<?php

require 'init.php';

$faci_id = $_POST['faci_id']; 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	if(!(isset($_POST['faci_id']))){
		echo 'No faci_id';
		exit(0);
	}

	if($stmt = $mysqli->prepare("SELECT * FROM workhours WHERE is_deleted = 0 AND faci_id = ?")){
		$stmt->bind_param("s", $faci_id);
		$stmt->execute();
		$stmt->bind_result($_id, $faci_id, $days, $hours_open, $hours_close, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'faci_id'=>$faci_id,
				'days'=>$days,
				'hours_open'=>$hours_open,
				'hours_close'=>$hours_close,
				'is_deleted'=>$is_deleted));
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