<?php
include("Connessione.php");

$id=$_GET['id'];

$sql="SELECT direzione FROM Comandi_Motore WHERE id=$id AND stato=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

while($row=mysqli_fetch_array($query))
{
    echo $row['direzione'];
}

$sql="UPDATE Comandi_Motore SET direzione=NOT direzione WHERE id=$id AND stato=1";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

$sql="UPDATE Comandi_Motore SET stato=0 WHERE id=$id";
$query=mysqli_query($connessione,$sql) or die ("Errore query");

mysqli_close($connessione);
?>