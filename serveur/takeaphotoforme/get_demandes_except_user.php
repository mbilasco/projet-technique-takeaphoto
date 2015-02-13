<?php
/**
 * Renvoie le tableau des demandes exepté un utilisateur
 *
 * @param idUser
 */
include("include/include.php");

/* Récupération des informations envoyées par l'application */
$id_user = $_POST['idUser'];

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
	
/* Récupération des demandes sauf celle de l'utilisateur */
$sql_select_demandes = "SELECT * FROM takeaphotoforme_demandes WHERE id_user != \"".$id_user."\" AND etat!=2";
$query_select_demandes=$connexion->prepare($sql_select_demandes);
$query_select_demandes->execute();

// Construction json
$json = array();

while($data=$query_select_demandes->fetch(PDO::FETCH_OBJ)){
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

/* Retour */
print(json_encode($result));
?>