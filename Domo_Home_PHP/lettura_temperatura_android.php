<?php
include("Connessione.php");

$sql="SELECT valore FROM Aggiorna_Temperatura WHERE id=1";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
	echo $row['valore'];
}

mysqli_close($connessione);

?>