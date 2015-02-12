<?php
/**
 * Creation de l'utilisateur 
 *
 * @param login 
 * @param pass
 *
 * SQL
 * CREATE TABLE IF NOT EXISTS `takeaphotoforme_users` (
 * `id_user` int(11) NOT NULL AUTO_INCREMENT,
 * `login` varchar(20) NOT NULL,
 * `password` varchar(12) NOT NULL,
 * `date_creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * PRIMARY KEY (`id_user`),
 * UNIQUE KEY `login` (`login`)
 * ) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=28 ;
 *
 * 1) Récupération des informations envoyées par l'application
 * 2) Connection DB
 * 3) Verification si exsite déjà dans la DB
 * 4) Insertion DB 
 * 5) Récupération ID_USER
 * 6) Creation dossier user
 * 7) Retour
 *
 * Liens : 
 * - http://www.w3schools.com/php/func_filesystem_mkdir.asp (MKDIR)
 */
include("include/include.php");

/* 1) Récupération des informations envoyées par l'application */
$login = $_POST['login'];
$pass = $_POST['pass'];
$pass  = md5($mcrypt->decrypt($pass));

if (!empty($login) && !empty($pass)){

	/* 2) Connection DB */
	$connexion = new PDO('mysql:host='.$config['host'].';dbname='.$config['db'], $config['user'], $config['pass']);
	
	/* 3) Verification si exsite déjà dans la DB */
	if (userExist($login, $connexion))
	{
		$result['result'] = "FALSE";
		$result['message'] = "L'utilisateur existe déjà dans la base de donnée.";
	}
	else
	{
		/* 4) Insertion DB */
		$sql_insert = "INSERT INTO takeaphotoforme_users(login,password) VALUES('$login','$pass')";
		$query_insert = $connexion->prepare($sql_insert);
		$query_insert->execute();
	
		if ($query_insert){
			/* 5) Récupération ID_USER */
			$sql_select_id_user = "SELECT * FROM takeaphotoforme_users WHERE login = '$login'";
			$query_select_id_user = $connexion->prepare($sql_select_id_user);
			$query_select_id_user->execute();
			
			//Test de la requete
			if ($query_select_id_user){
				//Test du nombre de retour
				if ($query_select_id_user->rowCount()){
					$data_user = $query_select_id_user->fetch(PDO::FETCH_ASSOC);
					$id_user = $data_user['id_user'];
					
					/* 6) Creation dossier user */	
					mkdir("users/" . $id_user);
					
					$result['result'] = "TRUE";
					$result['id'] = $id_user;
				}
				else{
					$result['result'] = "FALSE";
					$result['message'] = "Aucun retour correspondant.";
				}
			}
		}
		else{
			$result['result'] = "FALSE";
			$result['message'] = "Erreur sur la requête d'insertion";
		}
	}
}

/* 7) Retour */
print(json_encode($result));
?>
