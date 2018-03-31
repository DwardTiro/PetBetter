<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$postlist = json_decode(file_get_contents('php://input'),true);
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($postlist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		$title = substr(md5(rand()), 0, 7);
		$upload_path = "uploads/posts/$title.jpg";
		
		if(!($postlist[$i]['post_photo']==null)){
			file_put_contents($upload_path, base64_decode($postlist[$i]['post_photo']));
		}
		else{
			$upload_path = null;
		}
		
		if($stmt = $mysqli->prepare("INSERT INTO posts (user_id, topic_name, topic_content, topic_id, date_created, post_photo, id_link, id_type, is_deleted) VALUES (?,?,?,?,?,?,?,?,?)")){
			$stmt->bind_param("sssssssss", $postlist[$i]['user_id'], $postlist[$i]['topic_name'], $postlist[$i]['topic_content'], $postlist[$i]['topic_id'], $postlist[$i]['date_created'], 
				$upload_path, $postlist[$i]['id_link'], $postlist[$i]['id_type'], $postlist[$i]['is_deleted']);
			$stmt->execute();
			$stmt->close();
			$i = $i + 1;
			echo 'Insertion successful';
		}
		else{
			//echo 'and here?';
			break;
		}
		
	}
	
		
		//echo json_encode(array('_id'=>$vetlist['_id']));
		
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