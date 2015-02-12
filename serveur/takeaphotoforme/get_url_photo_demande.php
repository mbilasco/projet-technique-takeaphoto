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
 * 3) Récupération de l'id de la demande
 * 4) Récupération de l'id de l'utilisateur
 + 5) Récupération des urls
 * 6) Retour
 *
 * Liens : 
 * - http://www.infres.enst.fr/~danzart/php3/phpplus.php#repertoires
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$id_demande = $_POST['id_demande'];

/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
$sql_select_demande = "SELECT * FROM takeaphotoforme_reponses WHERE id_demande = $id_demande";
$query_select_demande = $connexion->prepare($sql_select_demande);
$query_select_demande->execute();

//Construction json
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

/* 4) Retour */
print(json_encode($result));
?>