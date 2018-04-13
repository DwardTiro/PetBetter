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

	if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.user_id AS user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, p.topic_id AS topic_id, p.date_created AS date_created, 
		p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type, p.is_deleted AS is_deleted FROM posts AS p INNER JOIN bookmarks AS b ON p._id = b.item_id 
		WHERE is_deleted = 0 AND b.bookmark_type = 2 AND b.user_id = ? AND (topic_name LIKE ? OR topic_content LIKE ?)")){
		//query might cause error
		$stmt->bind_param("sss", $query_id, $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $user_id, $topic_name, $topic_content, $topic_id, $date_created, $post_photo, $id_link, $id_type, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($response, array('_id'=>$_id,
				'user_id'=>$user_id,
				'topic_name'=>$topic_name,
				'topic_content'=>$topic_content,
				'topic_id'=>$topic_id,
				'date_created'=>$date_created,
				'post_photo'=>$post_photo,
				'id_link'=>$id_link,
				'id_type'=>$id_type,
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

	if($stmt = $mysqli->prepare("SELECT * FROM posts WHERE is_deleted = 0 AND (topic_name LIKE ? OR topic_content LIKE ?)")){
		//query might cause error
		$stmt->bind_param("ss", $query, $query);
		$stmt->execute();
		$stmt->bind_result($_id, $user_id, $topic_name, $topic_content, $topic_id, $date_created, $post_photo, $id_link, $id_type, $is_deleted);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				if(!in_array(array('_id'=>$_id, 'user_id'=>$user_id, 'topic_name'=>$topic_name, 'topic_content'=>$topic_content, 'topic_id'=>$topic_id, 'date_created'=>$date_created, 
					'post_photo'=>$post_photo, 'id_link'=>$id_link, 'id_type'=>$id_type, 'is_deleted'=>$is_deleted), $response)){
					
					array_push($response, array('_id'=>$_id,
					'user_id'=>$user_id,
					'topic_name'=>$topic_name,
					'topic_content'=>$topic_content,
					'topic_id'=>$topic_id,
					'date_created'=>$date_created,
					'post_photo'=>$post_photo,
					'id_link'=>$id_link,
					'id_type'=>$id_type,
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