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
$id_user = $_POST['idUser'];

/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* 3)Récupération de l'id de la demande a supprimer */
$id_demande = $_POST['id_demande'];

if(!empty($id_demande)){
	/* 5) Récupération des urls */
	$json = array();
	$url = "users/$id_user/$id_demande/";
	$pointeur=opendir($url);
	while ($fichier = readdir($pointeur)) {
	    if ($fichier != "." && $fichier != "..") {
		    $jsonURL[] = array(
			"url" => $url . $fichier
		    );
	    }
	}
	closedir($pointeur);
}	

$result['result'] = "TRUE";	
$result['url'] = $jsonURL;

/* 5) Retour */
print(json_encode($result));
?>