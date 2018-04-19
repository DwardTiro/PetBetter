<?php

require 'init.php';

$order = $_POST['order'];
$user_id = $_POST['user_id'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	if($order==1){
		if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.user_id AS user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, 
			p.topic_id AS topic_id, p.date_created AS date_created, p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type, p.is_deleted AS is_deleted 
			FROM posts AS p INNER JOIN topics AS t ON p.topic_id = t._id INNER JOIN followers AS f ON t._id = f.topic_id WHERE p.is_deleted = 0 AND f.user_id = ? AND f.is_allowed = 1 ORDER BY date_created DESC")){
			$stmt->bind_param("s", $user_id);
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
	
	if($order==2){
		if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.user_id AS user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, p.topic_id AS topic_id, 
			p.date_created AS date_created, p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type, p.is_deleted AS is_deleted, SUM(u.value) AS sumvalue FROM posts AS p 
			LEFT JOIN upvotes AS u ON p._id = u.feed_id INNER JOIN topics AS t ON p.topic_id = t._id INNER JOIN followers AS f ON t._id = f.topic_id WHERE p.is_deleted = 0 AND f.user_id = ? AND
			f.is_allowed = 1 GROUP BY p._id ORDER BY SUM(u.value) DESC")){
			$stmt->bind_param("s", $user_id);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $topic_name, $topic_content, $topic_id, $date_created, $post_photo, $id_link, $id_type, $is_deleted, $sumvalue);
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
	
	if($order==3){
		if($stmt = $mysqli->prepare("SELECT * FROM posts WHERE is_deleted = 0 ORDER BY date_created DESC")){
			
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
	if($order==4){
		if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.user_id AS user_id, p.topic_name AS topic_name, p.topic_content AS topic_content, p.topic_id AS topic_id, p.date_created AS date_created, 
			p.post_photo AS post_photo, p.id_link AS id_link, p.id_type AS id_type, p.is_deleted AS is_deleted, SUM(u.value) AS sumvalue FROM posts AS p LEFT JOIN upvotes AS u ON 
			p._id = u.feed_id WHERE p.is_deleted = 0 GROUP BY p._id ORDER BY SUM(u.value) DESC")){
			//$stmt->bind_param("s", $user_id);
			$stmt->execute();
			$stmt->bind_result($_id, $user_id, $topic_name, $topic_content, $topic_id, $date_created, $post_photo, $id_link, $id_type, $is_deleted, $sumvalue);
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