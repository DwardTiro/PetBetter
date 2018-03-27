<?php

require 'init.php';

$foreign_id = $_POST['foreign_id'];
$specialty = $_POST['specialty'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if($stmt = $mysqli->prepare("UPDATE veterinarians SET specialty = ? WHERE user_id = ?")){
		$stmt->bind_param("ss", $specialty, $foreign_id);
		$stmt->execute();
		$stmt->close();
		
		if($stmt = $mysqli->prepare("SELECT _id FROM pending WHERE type = 4 AND foreign_id = ?")){
			$stmt->bind_param("s", $foreign_id);
			$stmt->execute();
			$stmt->store_result();
			$ctr = $stmt->num_rows();
			$stmt->close();
			
			//echo $ctr;
			if($ctr==1){
				if($stmt = $mysqli->prepare("UPDATE pending SET is_approved = 0 WHERE foreign_id = ? AND type = 4")){
					$stmt->bind_param("s", $foreign_id);
					$stmt->execute();
					$stmt->close();
				}
				else{
					//echo 'hello?';
			
				}
			}
			if($ctr==0){
				//INSERT INTO bookmarks (item_id, bookmark_type, foreign_id) VALUES (?,?,?) 4 0
				if($stmt = $mysqli->prepare("INSERT INTO pending (foreign_id, type, is_approved) VALUES(?,4,0)")){
					$stmt->bind_param("s", $foreign_id);
					$stmt->execute();
					$stmt->close();
				}
				else{
					//echo 'and here?';
					//echo 'hi?';
				}
			}
		}
	}
	
	//echo $upload_path;
		
	
	
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