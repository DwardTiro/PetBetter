<?php

require 'init.php';

//$vetlist = $_POST['vetlist'];;

$userlist = json_decode(file_get_contents('php://input'),true);
//$sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";

//$result = mysqli_query($con, $sql);

if($_SERVER['REQUEST_METHOD']=='POST'){
	
	$n = count($userlist);
	//echo $n;
	$i = 0;
	//echo $notiflist[$i]['_id'];
	
	while($i<$n){
		if($stmt = $mysqli->prepare("INSERT INTO users (_id, first_name, last_name, email, password, user_type) VALUES (?,?,?,?,?,?)")){
			$stmt->bind_param("ssssss", $userlist[$i]['_id'], $userlist[$i]['first_name'], $userlist[$i]['last_name'], $userlist[$i]['email'], $userlist[$i]['password'], $userlist[$i]['user_type']);
			$stmt->execute();
			$stmt->close();
			$i = $i + 1;
		}
		else{
			//echo 'and here?';
			break;
		}
		
	}
	
	
}

?>