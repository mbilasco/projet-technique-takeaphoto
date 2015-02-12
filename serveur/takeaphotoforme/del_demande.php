<?php
/**
 * Supprime la demande passé en paramètre
 *
 * @param login 
 * @param pass
 * @param id_demande
 *
 * 1) Récupération des informations envoyées par l'application (login & mdp)
 * 2) Connection DB
 * 3) Récupération de l'id de la demande a supprimer
 * 4) Supression de la demande
 * 5) Retour
 *
 * Liens : 
 */
include("include/include.php");

/* 3) Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* 3)Récupération de l'id de la demande a supprimer */
$id_demande = $_POST['id_demande'];

/* 4) Supression de la demande */
$sql_del_demandes = "DELETE FROM takeaphotoforme_demandes WHERE id_demande = $id_demande";
$query_del_demandes = $connexion->prepare($sql_del_demandes);
$query_del_demandes->execute();

$result['result'] = "TRUE";

/* 4) Retour */
print(json_encode($result));
?>