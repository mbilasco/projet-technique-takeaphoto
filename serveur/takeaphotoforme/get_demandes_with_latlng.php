<?php
/**
 * Renvoie le tableau des demandes associées à une position GPS
 *
 * @param lat
 * @param lng 
 */
include("include/include.php");

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* Récupération des demandes associées à la position GPS */
$lat = $_POST['lat'];
$lng = $_POST['lng'];
$sql_select_demande = "SELECT * FROM takeaphotoforme_demandes WHERE latitude = '$lat' AND longitude = '$lng'";
$query_select_demande = $connexion->prepare($sql_select_demande);
$query_select_demande->execute();

// Construction json
$json = array();

while($data=$query_select_demande->fetch(PDO::FETCH_OBJ)){
	$json[] = array(
		"id_demande" => $data->id_demande,
		"id_user" => $data->id_user,
		"latitude" => $data->latitude,
		"longitude" => $data->longitude,
		"description" => $data->description,
	    "etat" => $data->etat
	);
}

$result['result'] = "TRUE";
$result['requete'] = $query_select_demande;
$result['demande'] = $json;

/* Retour */
print(json_encode($result));
?>