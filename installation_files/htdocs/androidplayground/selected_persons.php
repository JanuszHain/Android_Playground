<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$mysqli = $db->connect();

$personsIds = array();

if(isset($_GET['personsIds'])){
	$personsIds = $_GET['personsIds'];
}




$response = array();

foreach($personsIds as $id){
	
	if (mysqli_multi_query($mysqli,"CALL LoadPerson($id)"))
{
  do
    {
    // Store first result set
    if ($result=mysqli_store_result($mysqli)) {
      // Fetch one and one row
      while ($row = $result->fetch_assoc())
        {

					$data = array();
					$data["id"]=$row["id"];
					$data["firstName"]=$row["firstName"];
					$data["secondName"]=$row["secondName"];
					$data["birthDate"]=$row["birthDate"];
					$data["city"]=$row["city"];
					$data["photoUrl"]=$row["photoUrl"];
					array_push($response, $data);

 

				
        }
      // Free result set
      mysqli_free_result($result);
      }
    }
  while (mysqli_next_result($mysqli));
}

}//foreach

echo json_encode($response, JSON_UNESCAPED_SLASHES);

$mysqli->close();
?>