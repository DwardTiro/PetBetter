<?php

require 'init.php';

$query_id = $_POST['query_id'];
$queryjson = $_POST['queryjson'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	//echo $queryjson;
	
	//$query = "%{$_POST['query']}%";
	$query = "%{$queryjson}%";

	if($stmt = $mysqli->prepare("SELECT f.faci_id AS faci_id, f.faci_name AS faci_name, f.location AS location, f.contact_info AS contact_info, f.rating AS rating, 
	f.faci_photo AS faci_photo, f.is_disabled AS is_disabled FROM facilities AS f INNER JOIN bookmarks AS b ON f.faci_id = b.item_id WHERE f.is_disabled = 0 AND b.bookmark_type = 1 
	AND b.user_id = ? AND (f.faci_name LIKE ? OR f.location LIKE ?)")){
		$stmt->bind_param("sss", $query_id, $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $faci_name, $location, $contact_info, $rating, $faci_photo, $is_disabled);
		$stmt->store_result();
	
		if($stmt->fetch()){
			do{
				array_push($response, array('faci_id'=>$_id,
				'faci_name'=>$faci_name,
				'location'=>$location,
				'contact_info'=>$contact_info,
				'rating'=>$rating,
				'faci_photo'=>$faci_photo,
				'is_disabled'=>$is_disabled));
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
		}
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}
	
	if($stmt = $mysqli->prepare("SELECT * FROM facilities AS f WHERE f.is_disabled = 0 AND (f.faci_name LIKE ? OR f.location LIKE ?)")){
		$stmt->bind_param("ss", $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $faci_name, $location, $contact_info, $rating, $faci_photo, $is_disabled);
		$stmt->store_result();
	
		if($stmt->fetch()){
			do{
				if(!in_array(array('faci_id'=>$_id, 'faci_name'=>$faci_name, 'location'=>$location, 'contact_info'=>$contact_info, 'rating'=>$rating, 'faci_photo'=>$faci_photo,
					'is_disabled'=>$is_disabled), $response)){
					
					array_push($response, array('faci_id'=>$_id,
					'faci_name'=>$faci_name,
					'location'=>$location,
					'contact_info'=>$contact_info,
					'rating'=>$rating,
					'faci_photo'=>$faci_photo,
					'is_disabled'=>$is_disabled));
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
			//echo 'SQL Query Error';
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