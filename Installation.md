# Note d'installation :

# 1ère étape : Récupération du dépot Git #
  * git clone https://code.google.com/p/projet-technique-takeaphoto/

# 2ème étape : Ajout du projet dans Eclipse #
  * Note : Il faut préalablement avoir installé une version d'Eclipse avec l'ADT Android ainsi que le SDK Android. Vous pouvez trouver les deux logiciels aux adresses suivantes :
    * http://developer.android.com/tools/help/adt.html
    * http://developer.android.com/sdk/index.html
  * Lancer Eclipse
  * Cliquer sur File > Import...
  * Cliquer sur Android > Existing Android Code Into Workspace
  * Cliquer sur Browse...
  * Sélectionner le dossier android du dossier projet-technique-takeaphoto que vous avez cloné dans l'étape 1
  * Cliquer sur Finish

# 3ème étape : Ajout des librairies dans Eclipse #
  * Pour que le projet fonctionne il est nécessaire d'ajouter certaines librairies, pour ajouter des librairies et configurer le Build Path, il est nécessaire de faire un clique droit sur le projet et de cliquer sur Build Path > Configure Build Path... :
    * Dans l'onglet Java Build Path, ajouter ou modifier le lien vers le JAR du Google Play Services qui se trouve dans votre répertoire sdk à l'adresse suivante : sdk/extras/google/google\_play\_services/libproject/google-play-services\_lib/libs/google-play-services.jar
      * Note : si ce fichier n'existe pas vous devez ajouter le google-play-services depuis le SDK.
    * Il faut également faire les liens vers les différentes librairies présentes dans le dossier libs (notamment les jar de Flickr)

# 4ème étape : Installation du serveur #
  * Afin d'installer le serveur, il est nécessaire de copier le dossier "takeaphotoforme" présent dans répertoire serveur du repository GIT sur un serveur externe
  * Mettre ensuite les informations de votre Base de données dans le fichier include/configuration/config.php
  * Ajouter dans la classe ServeurAsync du projet, l'adresse de votre serveur à la ligne 56

# 5ème étape : Installation de la base de données #
  * Afin d'installer la base de données, exécuter le script "script\_creation.sql" présent dans le dossier bdd du repository GIT sur votre BDD externe

# 6ème étape : Configuration API Flickr #
  * Demandez votre clé API en vous rendant à cette url : https://www.flickr.com/services/apps/create puis copier l'API KEY et l'API SECRET dans la classe FlickrHelper