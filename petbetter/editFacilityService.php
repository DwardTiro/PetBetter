<?php

require 'init.php';


//$service_name = $_POST['service_name'];
//$service_price = $_POST['service_price'];
//$service_id = $_POST['service_id'];

$servicelist = json_decode(file_get_contents('php://input'),true);

if($_SERVER['REQUEST_METHOD']=='POST'){

	$n = count($servicelist);

	$i = 0;

	while($i < $n){

		if($stmt = $mysqli->prepare("UPDATE services SET service_name = ?, service_price = ? WHERE _id = ?")){
		$stmt->bind_param("sss", $servicelist[$i]['service_name'], $servicelist[$i]['service_price'],  $servicelist[$i]['_id']);
		$stmt->execute();
		$stmt->close();
		$i = $i + 1;
		echo 'Success';

		}
		else{
		//echo 'and here?';
		echo 'SQL Query Error';
		
		}
	}
		
	
}

?>