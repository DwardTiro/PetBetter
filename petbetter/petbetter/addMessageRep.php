<?php

require 'init.php';

$_id = $_POST['_id'];
$user_id = $_POST['user_id'];
$message_id = $_POST['message_id'];
$rep_content = $_POST['rep_content'];
$is_sent = $_POST['is_sent'];
$date_performed = $_POST['date_performed'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if(!(isset($_POST['_id']) and isset($_POST['user_id']) and isset($_POST['message_id']) and isset($_POST['rep_content']) and isset($_POST['is_sent']) and isset($_POST['date_performed']))){
		echo 'No username or password';
		exit(0);
	}

	if($stmt = $mysqli->prepare("INSERT INTO messagereps (_id, user_id, message_id, rep_content, is_sent, date_performed) VALUES (?,?,?,?,?,?)")){
		$stmt->bind_param("ssssss", $_id, $user_id, $message_id, $rep_content, $is_sent, $date_performed);
		$stmt->execute();
		$stmt->close();
		echo 'Message reply added';
		
		//$stmt->bind_result($_id, $first_name, $last_name, $mobile_num, $phone_num, $email,  $password, $age, $user_type);
		//$stmt->store_result();
	
	/*
		if($stmt->fetch()){
			
			$stmt->close();
			//echo json_encode($response);
			echo json_encode(array('_id'=>$_id,
			'first_name'=>$first_name,
			'last_name'=>$last_name,
			'mobile_num'=>$mobile_num,
			'phone_num'=>$phone_num,
			'email'=>$email,
			'password'=>$password,
			'age'=>$age,
			'user_type'=>$user_type));
		}
		else{
			
			$stmt->close();
			echo 'SQL Query Error';
		}
		*/
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}
	else{
		echo 'SQL Query Error';
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