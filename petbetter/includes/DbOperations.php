<?php 

	class DbOperations{

		private $con; 

		function __construct(){

			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();

		}

		/*CRUD -> C -> CREATE */

		public function createUser($user_id, $age, $first_name, $last_name, $gender, $birth_date, $reg_date, $password, $email){
			if($this->isUserExist($user_id, $email)){
				return 0; 
			}else{
				$password = md5($pass);
				$stmt = $this->con->prepare("INSERT INTO `user` (`user_id`, `age`, `first_name`,`last_name`, `gender`,`birth_date`,`reg_date`, `password`, `email`) VALUES (NULL, ?, ?, ?,?,?,?,?,?,?);");
				$stmt->bind_param("sssssssss",$user_id, $age, $first_name, $last_name, $gender, $birth_date, $reg_date, $password, $email);

				if($stmt->execute()){
					return 1; 
				}else{
					return 2; 
				}
			}
		}

		public function checkLogin($email, $password){
			//$password = md5($password);
			$stmt = $this->con->prepare("SELECT * FROM users WHERE email = ? AND password = ?");
			$stmt->bind_param("ss",$email,$password);
			$stmt->execute();
			$stmt->store_result(); 
			return $stmt->num_rows > 0; 
		}

		public function getUserByUsername($email){
			$stmt = $this->con->prepare("SELECT * FROM user WHERE email = ?");
			$stmt->bind_param("s",$email);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}
		
		public function getCourses(){
			$courses = array();
			$stmt = $this->con->prepare("SELECT course_id, prerequisite_id, course_name, course_description, capacity FROM course");
			$stmt->execute();
			$stmt->store_result();
			
			while($row = $stmt->get_result()->fetch_assoc()){
				$courses[] = $row;
			}
			
			return $courses;
		}
		

		private function isUserExist($user_id, $email){
			$stmt = $this->con->prepare("SELECT id FROM user WHERE username = ? OR email = ?");
			$stmt->bind_param("ss", $user_id, $email);
			$stmt->execute(); 
			$stmt->store_result(); 
			return $stmt->num_rows > 0; 
		}

	}
?>