<?php
include("Connessione.php");

$sql="SELECT atmosfera FROM Comandi_RGB WHERE id=1";
$query=mysqli_query($connessione,$sql) or die("Errore query");

while($row=mysqli_fetch_array($query))
{
	echo $row['atmosfera'];
}

mysqli_close($connessione);
?>