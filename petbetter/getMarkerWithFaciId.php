<?php

require 'init.php';

$faci_id = $_POST['faci_id'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE faci_id = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE faci_id = '$faci_id' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("SELECT * FROM markers WHERE faci_id = ?")){
		$stmt->bind_param("s", $faci_id);
		$stmt->execute();
		$stmt->bind_result($_id, $bldg_name, $longitude, $latitude, $location, $user_id, $type, $faci_id);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			$stmt->close();
			//echo json_encode($response);
			echo json_encode(array('_id'=>$_id,
			'bldg_name'=>$bldg_name,
			'longitude'=>$longitude,
			'latitude'=>$latitude,
			'location'=>$location,
			'user_id'=>$user_id,
			'type'=>$type,
			'faci_id'=>$faci_id));
		}
		else{
			
			$stmt->close();
			echo 'SQL Query Error';
		}
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}
}

/*

if(mysqli_fetch_array($result)){
	$user = mysqli_fetch_array($result);
	$response['error'] = false;
	$response['_id'] = $user['_id'];
	$response['first_name'] = $user['first_name'];
	$response['last_name'] = $user['last_name'];
	$response['mobile_num'] = $user['mobile_num'];
	$response['phone_num'] = $user['phone_num'];
	$response['faci_id'] = $user['faci_id'];
	$response['password'] = $user['password'];
	$response['age'] = $user['age'];
	$response['user_type'] = $user['user_type'];
}
else{
	$response['error'] = true; 
	$response['message'] = "Invalid faci_id or password";
}
*/
/*
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['faci_id']) and isset($_POST['password'])){
		$db = new DbOperations(); 

		if($db->userLogin($_POST['faci_id'], $_POST['password'])){
			$user = $db->getUserByUsername($_POST['faci_id']);
			$response['error'] = false;
			$response['_id'] = $user['_id'];
			$response['first_name'] = $user['first_name'];
			$response['last_name'] = $user['last_name'];
			$response['mobile_num'] = $user['mobile_num'];
			$response['phone_num'] = $user['phone_num'];
			$response['faci_id'] = $user['faci_id'];
			$response['password'] = $user['password'];
			$response['age'] = $user['age'];
			$response['user_type'] = $user['user_type'];
		}else{
			$response['error'] = true; 
			$response['message'] = "Invalid faci_id or password";			
		}

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}
echo json_encode($response)
*/

?>