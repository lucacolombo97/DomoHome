<?php
include("Connessione.php");

$sql="SELECT R,G,B FROM Comandi_RGB WHERE stato=1";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
	echo $row['R']."/";
    echo $row['G']."/";
    echo $row['B']."/";
}

mysqli_close($connessione);


?>