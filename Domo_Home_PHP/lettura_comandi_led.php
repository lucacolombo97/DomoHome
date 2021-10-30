<?php
include("Connessione.php");

$sql="SELECT stato FROM Comandi_Led WHERE stato=1";

$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
    echo $row['stato'];
}

mysqli_close($connessione);


?>