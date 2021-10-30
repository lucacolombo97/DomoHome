<?php
include("Connessione.php");

$sql="UPDATE Comandi_Allarme SET stato=NOT stato, suonato=0 WHERE id=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

echo "ok";

mysqli_close($connessione);

?>