<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$notiflist = json_decode(file_get_contents('php://input'),true);
$response = array();
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($notiflist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		
		if(!($notiflist[$i]['type']==3)){
			if($stmt = $mysqli->prepare("INSERT INTO notifications (user_id, doer_id, is_read, type, date_performed, source_id) VALUES (?,?,?,?,?,?)")){
				
				$stmt->bind_param("ssssss", $notiflist[$i]['user_id'], $notiflist[$i]['doer_id'], $notiflist[$i]['is_read'], $notiflist[$i]['type'], $notiflist[$i]['date_performed'], $notiflist[$i]['source_id']);
				$stmt->execute();
				$stmt->close();
			}
		}
		
		if($notiflist[$i]['type']==3){
			if($stmt = $mysqli->prepare("SELECT * FROM followers WHERE topic_id = ?")){
				$stmt->bind_param("s", $notiflist[$i]['source_id']);
				$stmt->execute();
				$stmt->bind_result($_id, $topic_id, $user_id);
				$stmt->store_result();
				
				
			
				if($stmt->fetch()){
			
					do{
						array_push($response, array('_id'=>$_id,
						'topic_id'=>$topic_id,
						'user_id'=>$user_id));
					}while($stmt->fetch());
					$stmt->close();
					
					$x = 0;
					$y = count($response);
					
					while($x<$y){
						if($stmt = $mysqli->prepare("INSERT INTO notifications (user_id, doer_id, is_read, type, date_performed, source_id) VALUES (?,?,?,?,?,?)")){
				
							if($response[$x]['user_id']!=$notiflist[$i]['user_id']){
									$stmt->bind_param("ssssss", $response[$x]['user_id'], $notiflist[$i]['doer_id'], $notiflist[$i]['is_read'], $notiflist[$i]['type'], $notiflist[$i]['date_performed'], $notiflist[$i]['source_id']);
									$stmt->execute();
									$stmt->close();
							}
							else{
								$stmt->close();
							}
							
						}
						else{
							$stmt->close();
						}
						$x = $x + 1;
					}
					
				}
				else{
					$stmt->close();
				}
			}
			//"SELECT * FROM followers WHERE topic_id = '" + topicId + "'"
		}
		
		$i=$i+1;
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