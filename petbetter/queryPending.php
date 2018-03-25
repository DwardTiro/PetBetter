<?php

require 'init.php';

$queryjson = $_POST['queryjson'];
$type = $_POST['type'];

$response = array(); 
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	//echo $queryjson;
	
	//$query = "%{$_POST['query']}%";
	//"%{$queryjson}%"
	
	
	if($type!=3){
		$pieces = explode(" ", $queryjson);
	
		if(count($pieces)==1){
			$query = "%{$pieces[0]}%";
			$query2 = "%{$pieces[0]}%";
			if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.foreign_id AS foreign_id, p.type AS type, p.is_approved AS is_approved FROM veterinarians AS v INNER JOIN users u ON 
				v.user_id = u.user_id INNER JOIN pending p ON v.user_id = p.foreign_id WHERE (u.first_name LIKE ? OR u.last_name LIKE ?) AND p.type = ? AND p.is_approved = 0")){
				$stmt->bind_param("sss", $query, $query2, $type);
				$stmt->execute();
				$stmt->bind_result($_id, $foreign_id, $type, $is_approved);
				$stmt->store_result();
			
				if($stmt->fetch()){
					
					do{
						array_push($response, array('_id'=>$_id,
							'foreign_id'=>$foreign_id,
							'type'=>$type,
							'is_approved'=>$is_approved));
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
		if(count($pieces)==2){
			$query = "%{$pieces[0]}%";
			$query2 = "%{$pieces[1]}%";
			if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.foreign_id AS foreign_id, p.type AS type, p.is_approved AS is_approved FROM veterinarians AS v INNER JOIN users u ON 
				v.user_id = u.user_id INNER JOIN pending p ON v.user_id = p.foreign_id WHERE (u.first_name LIKE ? OR u.last_name LIKE ?) AND p.type = ? AND p.is_approved = 0")){
				$stmt->bind_param("sss", $query, $query2, $type);
				$stmt->execute();
				$stmt->bind_result($_id, $foreign_id, $type, $is_approved);
				$stmt->store_result();
			
				if($stmt->fetch()){
					
					do{
						array_push($response, array('_id'=>$_id,
							'foreign_id'=>$foreign_id,
							'type'=>$type,
							'is_approved'=>$is_approved));
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
	}
	
	if($type==3){
		//SELECT * FROM facilities WHERE faci_name LIKE ?
		$query = "%{$queryjson}%";
		//echo $queryjson;
		if($stmt = $mysqli->prepare("SELECT p._id AS _id, p.foreign_id AS foreign_id, p.type AS type, p.is_approved AS is_approved FROM facilities AS f INNER JOIN services s 
			ON f.faci_id = s.faci_id INNER JOIN pending p ON s._id = p.foreign_id WHERE f.faci_name LIKE ? AND p.type = ? AND p.is_approved = 0")){
			$stmt->bind_param("ss", $query, $type);
			$stmt->execute();
			$stmt->bind_result($_id, $foreign_id, $type, $is_approved);
			$stmt->store_result();
			
			if($stmt->fetch()){
				
				do{
					array_push($response, array('_id'=>$_id,
						'foreign_id'=>$foreign_id,
						'type'=>$type,
						'is_approved'=>$is_approved));
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