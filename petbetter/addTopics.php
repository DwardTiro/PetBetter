<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$topiclist = json_decode(file_get_contents('php://input'),true);
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);
$topicids = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($topiclist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		if($stmt = $mysqli->prepare("INSERT INTO topics (creator_id, topic_name, topic_desc, date_created, is_deleted) VALUES (?,?,?,?,?)")){
			$stmt->bind_param("sssss", $topiclist[$i]['creator_id'], $topiclist[$i]['topic_name'], $topiclist[$i]['topic_desc'], $topiclist[$i]['date_created'], $topiclist[$i]['is_deleted']);
			$stmt->execute();
			$stmt->close();
			
			if($stmt = $mysqli->prepare("SELECT _id FROM topics WHERE date_created = ?")){
				$stmt->bind_param("s", $topiclist[$i]['date_created']);
				$stmt->execute();
				$stmt->bind_result($_id);
				$stmt->store_result();
				
			
				if($stmt->fetch()){
					
					
					do{
						array_push($topicids, array('_id'=>$_id));
					}while($stmt->fetch());
					
					$stmt->close();
					
					
					if($stmt = $mysqli->prepare("INSERT INTO followers (topic_id, user_id, is_allowed) VALUES (?,?,1)")){
						$stmt->bind_param("ss", $topicids[0]['_id'], $topiclist[$i]['creator_id']);
						$stmt->execute();
						$stmt->close();
						echo 'Follower added';
						
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
				
			}
			else{
				//echo 'and here?';
				break;
			}
		$i = $i + 1;
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
}

?>