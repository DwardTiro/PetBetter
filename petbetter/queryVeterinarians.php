<?php

require 'init.php';

$query_id = $_POST['query_id'];
$queryjson = $_POST['queryjson'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	
	
	//$query = "%{$_POST['query']}%";
	//"%{$queryjson}%"
	
	
	$pieces = explode(" ", $queryjson);
	
	if(count($pieces)==1){
		$query = "%{$pieces[0]}%";
		$query2 = "%{$pieces[0]}%";
		if($stmt = $mysqli->prepare("SELECT v._id AS _id, v.user_id, u.first_name AS first_name, u.last_name AS last_name, u.mobile_num AS mobile_num, u.phone_num AS phone_num, 
			u.email AS email, u.password AS password, u.age AS age, u.user_type AS user_type, u.user_photo AS user_photo, v.specialty AS specialty, 
			v.rating AS rating, COUNT(mr.sender_id) AS ctr FROM veterinarians AS v INNER JOIN users u ON v.user_id = u.user_id LEFT JOIN messagereps mr ON v.user_id = mr.user_id 
			WHERE u.is_disabled = 0 AND mr.sender_id = ? AND (u.first_name LIKE ? OR u.last_name LIKE ? OR v.specialty LIKE ? OR v.education LIKE ?) 
			GROUP BY mr.user_id ORDER BY ctr DESC")){
			$stmt->bind_param("sssss", $query_id, $query, $query2, $query, $query);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $first_name, $last_name, $mobile_num, $phone_num, $email, $password, $age, $user_type, $user_photo, $specialty, $rating, $ctr);
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
					'specialty'=>$specialty,
					'rating'=>$rating));
				}while($stmt->fetch());
				
				
				$stmt->close();
				
				
				
				//echo json_encode($response);
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
				//echo 'SQL Query Error this??';
			}
			//echo json_encode($stmt);
			//echo json_encode(array('user'=>$response));
		}
		
		if($stmt = $mysqli->prepare("SELECT v._id AS _id, v.user_id, u.first_name AS first_name, u.last_name AS last_name, 
			u.mobile_num AS mobile_num, u.phone_num AS phone_num, u.email AS email, u.password AS password, u.age AS age, u.user_type AS user_type, u.user_photo AS user_photo, 
			v.specialty AS specialty, v.rating AS rating FROM veterinarians AS v INNER JOIN users u ON v.user_id = u.user_id WHERE u.is_disabled = 0 AND (u.first_name LIKE ? OR u.last_name LIKE ? 
			OR v.specialty LIKE ? OR v.education LIKE ?)")){
			$stmt->bind_param("ssss", $query, $query2, $query, $query);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $first_name, $last_name, $mobile_num, $phone_num, $email, $password, $age, $user_type, $user_photo, $specialty, $rating);
			$stmt->store_result();
		
			if($stmt->fetch()){
				
				do{
					if(!in_array(array('_id'=>$_id,'user_id'=>$user_id, 'first_name'=>$first_name, 'last_name'=>$last_name, 'mobile_num'=>$mobile_num, 'phone_num'=>$phone_num, 'email'=>$email,
						'password'=>$password, 'age'=>$age, 'user_type'=>$user_type, 'user_photo'=>$user_photo, 'specialty'=>$specialty, 'rating'=>$rating), $response)){
							
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
						'specialty'=>$specialty,
						'rating'=>$rating));
					}	
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
				//echo 'SQL Query Error this?';
			}
			//echo json_encode($stmt);
			//echo json_encode(array('user'=>$response));
		}
	}
		
	if(count($pieces)>=2){
		$query = "%{$pieces[0]}%";
		$query2 = "%{$pieces[1]}%";
		$query3 = "%{$queryjson}%";
		if($stmt = $mysqli->prepare("SELECT v._id AS _id, v.user_id, u.first_name AS first_name, u.last_name AS last_name, u.mobile_num AS mobile_num, u.phone_num AS phone_num, 
			u.email AS email, u.password AS password, u.age AS age, u.user_type AS user_type, u.user_photo AS user_photo, v.specialty AS specialty, 
			v.rating AS rating, COUNT(mr.sender_id) AS ctr FROM veterinarians AS v INNER JOIN users u ON v.user_id = u.user_id LEFT JOIN messagereps mr ON v.user_id = mr.user_id 
			WHERE u.is_disabled = 0 AND mr.sender_id = ? AND (u.first_name LIKE ? OR u.last_name LIKE ? OR v.specialty LIKE ? OR v.education LIKE ?) 
			GROUP BY mr.user_id ORDER BY ctr DESC")){
			$stmt->bind_param("sssss", $query_id, $query, $query2, $query3, $query3);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $first_name, $last_name, $mobile_num, $phone_num, $email, $password, $age, $user_type, $user_photo, $specialty, $rating, $ctr);
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
					'specialty'=>$specialty,
					'rating'=>$rating));
				}while($stmt->fetch());
				
				
				$stmt->close();
				
				
				
				//echo json_encode($response);
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
				//echo 'SQL Query Error this??';
			}
			//echo json_encode($stmt);
			//echo json_encode(array('user'=>$response));
		}
		
		if($stmt = $mysqli->prepare("SELECT v._id AS _id, v.user_id, u.first_name AS first_name, u.last_name AS last_name, 
			u.mobile_num AS mobile_num, u.phone_num AS phone_num, u.email AS email, u.password AS password, u.age AS age, u.user_type AS user_type, u.user_photo AS user_photo, 
			v.specialty AS specialty, v.rating AS rating FROM veterinarians AS v INNER JOIN users u ON v.user_id = u.user_id WHERE u.is_disabled = 0 AND (u.first_name LIKE ? OR u.last_name LIKE ? 
			OR v.specialty LIKE ? OR v.education LIKE ?)")){
			$stmt->bind_param("ssss", $query, $query2, $query3, $query3);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $first_name, $last_name, $mobile_num, $phone_num, $email, $password, $age, $user_type, $user_photo, $specialty, $rating);
			$stmt->store_result();
		
			if($stmt->fetch()){
				
				do{
					if(!in_array(array('_id'=>$_id,'user_id'=>$user_id, 'first_name'=>$first_name, 'last_name'=>$last_name, 'mobile_num'=>$mobile_num, 'phone_num'=>$phone_num, 'email'=>$email,
						'password'=>$password, 'age'=>$age, 'user_type'=>$user_type, 'user_photo'=>$user_photo, 'specialty'=>$specialty, 'rating'=>$rating), $response)){
							
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
						'specialty'=>$specialty,
						'rating'=>$rating));
					}	
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
				//echo 'SQL Query Error this?';
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