<?php
/**
 * Renvoie le tableau des demandes d'un utilisateur
 *
 * @param login 
 * @param pass
 * @param id_demande
 * @param etat || @param description
 *
 * 1) Récupération des informations envoyées par l'application (login & mdp)
 * 2) Connection DB
 * 3) Récupération de l'id de la demande & de la variable
 + 4) Update sur la demande
 * 5) Retour
 *
 * Liens : 
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$id_demande = $_POST['id_demande'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];

$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

if (!empty($id_demande)){
		$sql_update_demande = "UPDATE takeaphotoforme_demandes SET latitude = $latitude, longitude = $longitude WHERE id_demande = $id_demande";
		
		$query_update_demande = $connexion->prepare($sql_update_demande);
		$query_update_demande->execute();
		
		$result['result'] = "TRUE";
		$result['demande'] = $sql_update_demande;
		$result['latitude'] = $latitude;
		$result['longitude'] = $longitude;
}
else{
	$result['result'] = "FALSE";
	$result['message'] = "L'id de la demande est inexistant.";
}

/* 5) Retour */
print(json_encode($result));
?>