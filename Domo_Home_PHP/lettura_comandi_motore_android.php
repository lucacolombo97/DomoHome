<?php
include("Connessione.php");

$id=$_GET['id'];

$sql="SELECT direzione FROM Comandi_Motore WHERE id=$id";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
    echo $row['direzione'];
}

?>