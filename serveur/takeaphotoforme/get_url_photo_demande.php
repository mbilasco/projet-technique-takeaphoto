<?php
/**
 * Renvoie l'url de la demande passée en paramètre
 *
 * @param id_demande
 */
include("include/include.php");

/* Récupération des informations envoyées par l'application */
$id_demande = $_POST['id_demande'];

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
$sql_select_demande = "SELECT * FROM takeaphotoforme_reponses WHERE id_demande = $id_demande";
$query_select_demande = $connexion->prepare($sql_select_demande);
$query_select_demande->execute();

// Construction json
$json = array();

while($data=$query_select_demande->fetch(PDO::FETCH_OBJ)){
	$json[] = array(
		"id_reponse" => $data->id_reponse,
		"url" => $data->url,
		"id_demande" => $data->id_demande
	);
}

$result['result'] = "TRUE";
$result['reponses'] = $json;
$result['requete'] = $query_select_demande;

/* Retour */
print(json_encode($result));
?>