<?php

require 'init.php';

$checkdisabled = $_POST['checkdisabled'];
$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if($checkdisabled==0){
		if($stmt = $mysqli->prepare("SELECT * FROM facilities")){
		
			$stmt->execute();
			$stmt->bind_result($_id, $faci_name, $location, $contact_info, $rating, $faci_photo, $is_disabled);
			$stmt->store_result();
		
			if($stmt->fetch()){
				do{
					$location = utf8_encode($location);
					array_push($response, array('faci_id'=>$_id,
					'faci_name'=>$faci_name,
					'location'=>$location,
					'contact_info'=>$contact_info,
					'rating'=>$rating,
					'faci_photo'=>$faci_photo,
					'is_disabled'=>$is_disabled));
				}while($stmt->fetch());
				
				
				$stmt->close();
				
				echo json_encode($response);
				/*
				echo json_encode(array('_id'=>$_id,
				'user_id'=>$user_id,
				'topic_name'=>$topic_name,
				'topic_content'=>$topic_content,
				'date_created'=>$date_created,
				'first_name'=>$first_name,
				'is_deleted'=>$is_deleted));
				*/
			}
			else{
				
				$stmt->close();
				echo 'SQL Query Error';
			}
			//echo json_encode($stmt);
			//echo json_encode(array('user'=>$response));
		}
	}
	if($checkdisabled==1){
		if($stmt = $mysqli->prepare("SELECT * FROM facilities WHERE is_disabled = 0")){
			
			$stmt->execute();
			$stmt->bind_result($_id, $faci_name, $location, $contact_info, $rating, $faci_photo, $is_disabled);
			$stmt->store_result();
		
			if($stmt->fetch()){
				
				do{
					$location = utf8_encode($location);
					array_push($response, array('faci_id'=>$_id,
					'faci_name'=>$faci_name,
					'location'=>$location,
					'contact_info'=>$contact_info,
					'rating'=>$rating,
					'faci_photo'=>$faci_photo,
					'is_disabled'=>$is_disabled));
				}while($stmt->fetch());
				
				
				$stmt->close();
				
				echo json_encode($response);
				/*
				echo json_encode(array('_id'=>$_id,
				'user_id'=>$user_id,
				'topic_name'=>$topic_name,
				'topic_content'=>$topic_content,
				'date_created'=>$date_created,
				'first_name'=>$first_name,
				'is_deleted'=>$is_deleted));
				*/
			}
			else{
				
				$stmt->close();
				echo 'SQL Query Error';
			}
			//echo json_encode($stmt);
			//echo json_encode(array('user'=>$response));
		}
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