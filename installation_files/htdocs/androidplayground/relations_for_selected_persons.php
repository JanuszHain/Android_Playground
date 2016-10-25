<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$mysqli = $db->connect();

$idMainPerson = -1;

$personsIds = array();

if(isset($_GET['idMainPerson'])){
	$idMainPerson = $_GET['idMainPerson'];
}

if(isset($_GET['personsIds'])){
	$personsIds = $_GET['personsIds'];
}




$response = array();

foreach($personsIds as $id){
	
	if (mysqli_multi_query($mysqli,"CALL LoadRelationBetweenSetPersons($idMainPerson, $id)"))
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
					$data["idPersonA"]=$row["idPerson1"];
					$data["idPersonB"]=$row["idPerson2"];
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