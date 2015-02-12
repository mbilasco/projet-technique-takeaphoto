<?php
/**
 * Renvoie le tableau des demandes d'un utilisateur
 *
 * @param login
 * @param pass 
 * @param id_demande
 *
 * 1) Récupération des informations envoyées par l'application (login & mdp)
 * 2) Connection DB
 * 3) Récupération de la demande
 * 4) Retour
 *
 */
include("include/include.php");

/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* 3) Récupération des demandes de l'utilisateur */
$id_demande = $_POST['id_demande'];
$sql_select_demande = "SELECT * FROM takeaphotoforme_demandes WHERE id_demande = $id_demande";
$query_select_demande = $connexion->prepare($sql_select_demande);
$query_select_demande->execute();

//Construction json
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
$result['demande'] = $json;

/* 4) Retour */
print(json_encode($result));
?>