<?php

require 'init.php';

$_id = $_POST['_id'];
$first_name = $_POST['first_name'];
$last_name = $_POST['last_name'];
$mobile_num = $_POST['mobile_num'];
$phone_num = $_POST['phone_num'];
$email = $_POST['email'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if(!(isset($_POST['_id']) and isset($_POST['first_name']) and isset($_POST['last_name']) and isset($_POST['mobile_num']) and isset($_POST['phone_num']) and isset($_POST['email']))){
		echo 'Details are lacking';
		exit(0);
	}

	if($stmt = $mysqli->prepare("UPDATE users SET first_name = ?, last_name = ?, mobile_num = ?,  phone_num = ?, email = ? WHERE _id = ?")){
		$stmt->bind_param("ssssss", $first_name, $last_name, $mobile_num, $phone_num, $email, $_id);
		$stmt->execute();
		$stmt->close();
		echo 'Update successful';
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}
	else{
		echo 'Update unsuccessful';
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