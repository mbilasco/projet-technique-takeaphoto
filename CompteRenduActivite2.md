**Temps utilisé pour la configuration : ~ 30h homme**

**Temps utilisé sur la mise en place de OAuth2.0 avec Flickr sur un projet temporaire : ~ 8h homme**

**Temps total sur la configuration : ~ 40h homme**

# Tentatives de configuration de l'ancien projet Android : #

  * Sur la dernière version du SVN fourni :
    * Ajout par "import > Existing Android Code into Workspace" : Echec
    * Création d'un nouveau projet et remplacement des differents dossier : Echec
    * Création d'un nouveau projet et ajout des fichiers un à un : Echec
    * Tests sur les 2 IDE principaux : Eclipse et Android Studio

**Note :**
Détection de nombreuses erreurs réelles dans le code :
  * ex : appel à des méthodes qui n'existent pas : "getDemandeWithID" dans la classe "DemandesBDD.java..."

  * Sur une ancienne version du "DRIVE" :
    * Ajout par "import > Existing Android Code into Workspace" : Echec
    * Création d'un nouveau projet et remplacement des differents dossier : Echec
    * Tests sur les 2 IDE principaux : Eclipse et Android Studio
    * Création d'un nouveau projet et ajout des fichiers un à un : Succès suite aux étapes suivantes :
      * Modification du fichier "manifest.xml"
        * Changement de la version minimum
      * Réécriture des lignes contenant un appel à R :
        * Suppression de "R.id.menu\_create"
        * Écriture de "R.id.menu\_create"
      * Correction des problèmes dans les fichiers "styles.xml"
      * Ajout des librairies "android-support-v4.jar" et "google-play-services.jar" dans le dossier libs de l'application
      * Utilisation de la fonction "clean" à plusieurs reprises

**PROBLEME : L'application crash à la connexion !**
**CAUSE POSSIBLES :**
  * Version trop ancienne et non fonctionnelle ?
  * Code non fonctionnel ?
  * Problème à cause du serveur qui est down ?

# Questions : #
  * Comment débugger la dernière version de l'application ?
  * Faut-il utiliser la dernière ou l'ancienne "a peu près" fonctionnelle ?
  * Faut-il recommencer de 0 ?

# Mise en place de OAuth2.0 avec Flickr : #
  * Étude de l'API Flickr et de OAuth2.0.
  * Ajout des librairies flickj-android-2.0.0.jar et slf4j-android-1.5.8.jar dans le projet.
  * Implémentation d'une activity test pour se connecter à Flickr via notre apiKey : Erreur.
  * Récupération d'un exemple d'utilisation de l'API Flickr : à tester.