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
	
	if($stmt = $mysqli->prepare("SELECT t._id AS _id, t.creator_id AS creator_id, t.topic_name AS topic_name, t.topic_desc AS topic_desc, t.date_created AS date_created, 
		t.is_deleted AS is_deleted FROM topics AS t INNER JOIN followers AS f ON t._id = f.topic_id WHERE is_deleted = 0 AND f.user_id = ? 
		AND (topic_name LIKE ? OR topic_desc LIKE ?)")){
		//query might cause error
		$stmt->bind_param("sss", $query_id, $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $creator_id, $topic_name, $topic_desc, $date_created, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'creator_id'=>$creator_id,
				'topic_name'=>$topic_name,
				'topic_desc'=>$topic_desc,
				'date_created'=>$date_created,
				'is_deleted'=>$is_deleted));
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
			//echo 'SQL Query Error';
		}
		//echo json_encode($stmt);
		//echo json_encode(array('user'=>$response));
	}

	if($stmt = $mysqli->prepare("SELECT * FROM topics WHERE is_deleted = 0 AND (topic_name LIKE ? OR topic_desc LIKE ?)")){
		//query might cause error
		$stmt->bind_param("ss", $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $creator_id, $topic_name, $topic_desc, $date_created, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				if(!in_array(array('_id'=>$_id, 'creator_id'=>$creator_id, 'topic_name'=>$topic_name, 'topic_desc'=>$topic_desc, 'date_created'=>$date_created, 'is_deleted'=>$is_deleted), $response)){
					
					array_push($response, array('_id'=>$_id,
					'creator_id'=>$creator_id,
					'topic_name'=>$topic_name,
					'topic_desc'=>$topic_desc,
					'date_created'=>$date_created,
					'is_deleted'=>$is_deleted));
					
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