# Fin de la mise en place de Flickr sur le projet de test : #
  * Upload d'une photo stockée sur le téléphone vers un compte Flickr de test => OK, fonctionnel
  * Fin de la mise en place de l'authentification OAuth avec les identifiants de connexion Yahoo => OK, fonctionnel

# Intégration du projet de test Flickr dans le projet : #
  * Nous sommes repartis sur l'idée d'un compte par personne, car on a pas réussi à automatiser un passage dans le workflow de l'authentification (celui où l'utilisateur autorise l'accès de notre application au compte flickr de l'utilisateur). Cela nous permettra d'authentifier les utilisateurs du coup par leurs identifiants Yahoo, quitte à complétement enlever l'espace de connexion actuellement en place. Pour regrouper les photos issues des comptes flickr des utilisateurs, nous allons les joindre à un groupe dans lesquelles seront stockées toutes les photos.
  * Intégration de la 2e solution sur le projet => En cours