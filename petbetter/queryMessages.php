<?php

require 'init.php';

$queryjson = $_POST['queryjson'];
$_id = $_POST['_id'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	//echo $queryjson;
	
	//$query = "%{$_POST['query']}%";
	//"%{$queryjson}%"
	
	/*
	SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_one = u._id WHERE u._id = '" + userId + "'" +
                "UNION " +
                "SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, u.first_name AS first_name, " +
                "u.last_name AS last_name FROM messages AS m INNER JOIN users AS u ON m.user_two = u._id WHERE u._id = '" + userId + "'
	*/
	
	
	$pieces = explode(" ", $queryjson);
	
	
	if(count($pieces)==1){
		$query = "%{$pieces[0]}%";
		$query2 = "%{$pieces[0]}%";
		if($stmt = $mysqli->prepare("SELECT DISTINCT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, u.last_name AS last_name 
				FROM messages AS m INNER JOIN users AS u ON m.user_one = u.user_id WHERE (m.user_one = ? OR m.user_two = ?) AND (u.first_name LIKE ? OR u.last_name LIKE ?) 
				UNION 
				SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, m.is_allowed AS is_allowed, u.first_name AS first_name, u.last_name AS last_name FROM messages 
				AS m INNER JOIN users AS u ON m.user_two = u.user_id WHERE (m.user_one = ? OR m.user_two = ?) AND (u.first_name LIKE ? OR u.last_name LIKE ?)")){
			$stmt->bind_param("ssssssss", $_id, $_id, $query, $query2, $_id, $_id, $query, $query2);
			$stmt->execute();
			$stmt->bind_result($_id, $user_one, $user_two, $is_allowed, $first_name, $last_name);
			$stmt->store_result();
		
			if($stmt->fetch()){
				
				do{
					array_push($response, array('_id'=>$_id,
					'user_one'=>$user_one,
					'user_two'=>$user_two,
					'is_allowed'=>$is_allowed,
					'first_name'=>$first_name,
					'last_name'=>$last_name));
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
	if(count($pieces)==2){
		$query = "%{$pieces[0]}%";
		$query2 = "%{$pieces[1]}%";
		if($stmt = $mysqli->prepare("SELECT DISTINCT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, u.first_name AS first_name, u.last_name AS last_name FROM messages 
				AS m INNER JOIN users AS u ON m.user_one = u.user_id WHERE (m.user_one = ? OR m.user_two = ?) AND (u.first_name LIKE ? OR u.last_name LIKE ?) 
				UNION 
				SELECT m._id AS _id, m.user_one AS user_one, m.user_two AS user_two, u.first_name AS first_name, u.last_name AS last_name FROM messages 
				AS m INNER JOIN users AS u ON m.user_two = u.user_id WHERE (m.user_one = ? OR m.user_two = ?) AND (u.first_name LIKE ? OR u.last_name LIKE ?)")){
			$stmt->bind_param("ssssssss", $_id, $_id, $query, $query2, $_id, $_id, $query, $query2);
			$stmt->execute();
			$stmt->bind_result($_id, $user_one, $user_two, $first_name, $last_name);
			$stmt->store_result();
		
			if($stmt->fetch()){
				
				do{
					array_push($response, array('_id'=>$_id,
					'user_one'=>$user_one,
					'user_two'=>$user_two,
					'first_name'=>$first_name,
					'last_name'=>$last_name));
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