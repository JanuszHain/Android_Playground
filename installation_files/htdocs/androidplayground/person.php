<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$mysqli = $db->connect();

$id = -1;

if(isset($_GET['id'])){
	$id = $_GET['id'];
}

$result = $mysqli->query("CALL LoadPerson('$id')");


 $response = array();
$row = $result->fetch_assoc();
  $data = array();
  $data["id"]=$row["id"];
	$data["firstName"]=$row["firstName"];
	$data["secondName"]=$row["secondName"];
	$data["birthDate"]=$row["birthDate"];
	$data["city"]=$row["city"];
	$data["photoUrl"]=$row["photoUrl"];
  array_push($response, $data);
	
	echo json_encode($data, JSON_UNESCAPED_SLASHES);
	
	$mysqli->close();
	 
?>





