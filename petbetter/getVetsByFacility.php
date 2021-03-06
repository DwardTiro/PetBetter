<?php

require 'init.php';

$faci_id = $_POST['faci_id'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){

	if($stmt = $mysqli->prepare("SELECT v._id AS _id, v.user_id, u.first_name AS first_name, u.last_name AS last_name, u.mobile_num AS mobile_num, u.phone_num AS phone_num, 
		u.email AS email, u.password AS password, u.age AS age, u.user_type AS user_type, u.user_photo AS user_photo, u.is_disabled AS is_disabled, v.specialty AS specialty, v.rating AS rating, 
		v.education AS education, v.is_licensed AS is_licensed, v.profile_desc AS profile_desc FROM veterinarians AS v 
		INNER JOIN users u ON v.user_id = u.user_id INNER JOIN facility_membership AS fm ON v._id = fm.vet_id INNER JOIN facilities AS f ON fm.faci_id = f.faci_id WHERE f.faci_id = ?")){
		$stmt->bind_param("s", $faci_id);
		$stmt->execute();
		$stmt->bind_result($_id, $user_id, $first_name, $last_name, $mobile_num, $phone_num, $email, $password, $age, $user_type, $user_photo, $is_disabled, $specialty, $rating, 
			$education, $is_licensed, $profile_desc);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'user_id'=>$user_id,
				'first_name'=>$first_name,
				'last_name'=>$last_name,
				'mobile_num'=>$mobile_num,
				'phone_num'=>$phone_num,
				'email'=>$email,
				'password'=>$password,
				'age'=>$age,
				'user_type'=>$user_type,
				'user_photo'=>$user_photo,
				'is_disabled'=>$is_disabled,
				'specialty'=>$specialty,
				'rating'=>$rating,
				'education'=>$education,
				'is_licensed'=>$is_licensed,
				'profile_desc'=>$profile_desc));
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