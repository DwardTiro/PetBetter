<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$servicelist = json_decode(file_get_contents('php://input'),true);
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";
$pending = array(); 

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($servicelist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		if($stmt = $mysqli->prepare("INSERT INTO services (faci_id, service_name, service_price, is_deleted) VALUES (?,?,?,?)")){
			$stmt->bind_param("ssss", $servicelist[$i]['faci_id'], $servicelist[$i]['service_name'], $servicelist[$i]['service_price'], $servicelist[$i]['is_deleted']);
			$stmt->execute();
			$stmt->close();
			$i = $i + 1;
		}
		else{
			//echo 'and here?';
			break;
		}
		
	}
	
	if($stmt = $mysqli->prepare("SELECT _id FROM services WHERE is_deleted = 0 AND faci_id = ?")){
		$stmt->bind_param("s", $servicelist[0]['faci_id']);
		$stmt->execute();
		$stmt->bind_result($_id);
		$stmt->store_result();
	
		if($stmt->fetch()){
			
			do{
				array_push($pending, array('_id'=>$_id));
			}while($stmt->fetch());
			
			$stmt->close();
			
			$n = count($pending);
			//echo $n;
			$i = 0;
			$ptype = 3;
			$papproved = 0;
			//echo $vetlist[$i]['_id'];
			while($i<$n){
				if($stmt = $mysqli->prepare("SELECT 1 FROM pending WHERE foreign_id = ?")){
					$stmt->bind_param("s", $pending[$i]['_id']);
					$stmt->execute();
					$stmt->bind_result($ctr);
					$stmt->store_result();
					//$stmt->bind_result($ctr);
					
					if($stmt->fetch()){
						//throw new Exception('Email already taken');
						$i = $i + 1;
					}
					else{
						$stmt->close();
						if($stmt = $mysqli->prepare("INSERT INTO pending (foreign_id, type, is_approved) VALUES (?,?,?)")){
							$stmt->bind_param("sss", $pending[$i]['_id'], $ptype, $papproved);
							$stmt->execute();
							$stmt->close();
							$i = $i + 1;
						}
						else{
							echo 'Failed to add to db';
							break;
						}
					}
					
					
				}
			}
			
			/*
			while($i<$n){
				
				if($stmt = $mysqli->prepare("INSERT INTO pending (foreign_id, type, is_approved) VALUES (?,?,?)")){
					$stmt->bind_param("sss", $pending[$i]['_id'], 3, 0);
					$stmt->execute();
					$stmt->close();
					$i = $i + 1;
				}
				else{
					echo 'Failed to add to db';
					break;
				}
				
			}
			*/
		}
		else{
			
			$stmt->close();
			echo 'SQL Query Error';
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