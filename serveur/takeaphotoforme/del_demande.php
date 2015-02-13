<?php
/**
 * Supprime la demande passée en paramètre
 *
 * @param id_demande
 */
include("include/include.php");

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* Récupération de l'id de la demande a supprimer */
$id_demande = $_POST['id_demande'];

/* Supression de la demande */
$sql_del_demandes = "DELETE FROM takeaphotoforme_demandes WHERE id_demande = $id_demande";
$query_del_demandes = $connexion->prepare($sql_del_demandes);
$query_del_demandes->execute();

$result['result'] = "TRUE";

/* Retour */
print(json_encode($result));
?>