<?php
include("Connessione.php");

$sql="UPDATE Comandi_Allarme SET suonato=1 WHERE id=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

echo "ok";

mysqli_close($connessione);

?>
