<?php
/**
 * Met à jour l'etat d'une demande
 *
 * @param id_demande 
 * @param etat
 */
include("include/include.php");

/* Récupération des informations envoyées par l'application */
$id_demande = $_POST['id_demande'];
$etat = $_POST['etat'];

$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

if (!empty($id_demande)){
	$sql_update_demande = "UPDATE takeaphotoforme_demandes SET etat = " . $etat . " WHERE id_demande = " . $id_demande;

	$query_update_demande = $connexion->prepare($sql_update_demande);
	$query_update_demande->execute();

	$result['result'] = "TRUE";
}
else{
	$result['result'] = "FALSE";
	$result['message'] = "L'id de la demande est inexistant.";
}

/* Retour */
print(json_encode($result));
?>