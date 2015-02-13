<?php
/**
 * Met à jour une demande
 *
 * @param id_demande
 * @param etat 
 * @param description
*/
include("include/include.php");

/* Connection DB */
$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);

/* Récupération de l'id de la demande a mettre à jour */
$id_demande = $_POST['id_demande'];
if (!empty($id_demande)){
	$etat = $_POST['etat'];
	$description = $_POST['description'];
		
	if ($etat > -1 || !empty($description)){
		if ($etat > -1){
			$sql_update_demande = "UPDATE takeaphotoforme_demandes SET etat = $etat WHERE id_demande = $id_demande";
		}
		else if (!empty($description)){
			$sql_update_demande = "UPDATE takeaphotoforme_demandes SET description = '$description' WHERE id_demande = $id_demande";
		}
		
		/* Update sur la demande */
		$query_update_demande = $connexion->prepare($sql_update_demande);
		$query_update_demande->execute();
		
		$result['result'] = "TRUE";
	}
	else{
		$result['result'] = "FALSE";
		$result['message'] = "Il faut au moins un paramètre (etat ou description).";
	}
}
else{
	$result['result'] = "FALSE";
	$result['message'] = "L'id de la demande est inexistant.";
}

/* Retour */
print(json_encode($result));
?>