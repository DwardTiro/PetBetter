<?php

require 'init.php';

$facilist = json_decode(file_get_contents('php://input'),true);

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	$i = 0;
	
	
	$title = substr(md5(rand()), 0, 7);
	$upload_path = "uploads/facilities/$title.jpg";
		
	if(!($facilist['faci_photo']==null)){
		file_put_contents($upload_path, base64_decode($facilist['faci_photo']));
	}
	else{
		$upload_path = null;
	}
	echo $upload_path;
	if($stmt = $mysqli->prepare("UPDATE facilities SET faci_name = ?, location = ?, hours_open = ?,  hours_close = ?, contact_info = ?, rating = ?, faci_photo = ? WHERE faci_id = ?")){
		$stmt->bind_param("ssssssss", $facilist['faci_name'], $facilist['location'], $facilist['hours_open'], $facilist['hours_close'], $facilist['contact_info'], $facilist['rating'], $upload_path, $facilist['faci_id']);
		$stmt->execute();
		$stmt->close();
	}
	else{
		//echo 'and here?';
		
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
	$response['email'] = $user['email'];
	$response['password'] = $user['password'];
	$response['age'] = $user['age'];
	$response['user_type'] = $user['user_type'];
}
else{
	$response['error'] = true; 
	$response['message'] = "Invalid email or password";
}
*/
/*
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['email']) and isset($_POST['password'])){
		$db = new DbOperations(); 

		if($db->userLogin($_POST['email'], $_POST['password'])){
			$user = $db->getUserByUsername($_POST['email']);
			$response['error'] = false;
			$response['_id'] = $user['_id'];
			$response['first_name'] = $user['first_name'];
			$response['last_name'] = $user['last_name'];
			$response['mobile_num'] = $user['mobile_num'];
			$response['phone_num'] = $user['phone_num'];
			$response['email'] = $user['email'];
			$response['password'] = $user['password'];
			$response['age'] = $user['age'];
			$response['user_type'] = $user['user_type'];
		}else{
			$response['error'] = true; 
			$response['message'] = "Invalid email or password";			
		}

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}
echo json_encode($response)
*/

?>