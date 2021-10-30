<?php
include("Connessione.php");

$sql="SELECT suonato FROM Comandi_Allarme WHERE id=1 AND suonato=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
	echo $row['suonato'];
}

mysqli_close($connessione);

?>