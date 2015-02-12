<?php
/**
 * Authentification de l'utilisateur
 *
 * @param login 
 * @param pass
 *
 * 1) Récupération des informations envoyées par l'application (login, password)
 * 2) Connection DB
 * 3) Verification si user exsite déjà dans la DB
 * 4) Verification si mdp correspond
 * 5) Retour
 *
 * Liens : 
 * 
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$login = $_POST['login'];
$pass  = $_POST['pass'];
$pass  = md5($mcrypt->decrypt($pass));

if (!empty($login) && !empty($pass)){
	/* 2) Connection DB */
	 $connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
		
	/* 3) Verification si user existe déjà dans la DB */
	if (userExist($login, $connexion))
	{
		// Récupération mdp user
		$sql_select_mdp_user = "SELECT * FROM takeaphotoforme_users WHERE login = '$login'";
		$query_select_mdp_user = $connexion->prepare($sql_select_mdp_user);
		$query_select_mdp_user->execute();

		//Test de la requete
		if ($query_select_mdp_user){
			//Test du nombre de retour
			if ($query_select_mdp_user->rowCount()){
				$data_user = $query_select_mdp_user->fetch(PDO::FETCH_ASSOC);
				$mdp_user = $data_user['password'];
				$id_user = $data_user['id_user'];
				
				/* 4) Verification si mdp correspond */
				if ($mdp_user == $pass){
					$result['result'] = "TRUE";
					$result['id'] = $id_user;
				}
				else{
					$result['result'] = "FALSE";
					$result['message'] = "Les mots de passe ne correspondent pas" . $mdp_user . " - " . $pass;
				}
			}
			else{
				$result['result'] = "FALSE";
				$result['message'] = "Il n'y a pas de retour correspondant";
			}
		}
	}
	else
	{
		$result['result'] = "FALSE";
		$result['message'] = "L'utilisateur n'existe pas";
	}
}
else
{
	$result['result'] = "FALSE";
	$result['message'] = "Il n'y a aucun paramètre";
}
	
/* 5) Retour */
print(json_encode($result));
?>
