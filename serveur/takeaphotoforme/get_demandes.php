<?php
/**
 * Renvoie le tableau des demandes
 */
include("include/include.php");

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* Récupération des demandes de l'utilisateur */
$sql_select_demande = "SELECT * FROM takeaphotoforme_demandes";
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
$result['demandes'] = $json;

/* Retour */
print(json_encode($result));
?>