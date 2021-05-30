# CNAM Normandie - Licence STS PROJET PRATIQUE MODULE SMB116
La partie pratique de l'évaluation du module SMB116 consiste à développer une application de gestion de villes pour visualiser la météo et la carte locale. Ci-après le cahier des exigences.

## Exigences fonctionnelles

#### EXG_IHM_001 :
    L'application doit permettre de gérer une liste de villes dans lesquelles on désire connaître la température et le temps qu'il fait. La gestion consiste à pouvoir ajouter, modifier et supprimer des villes dans la liste.

#### EXG_IHM_002 :
    Chaque élément de la liste représentant une ville devra faire figurer
    -    Le nom de la ville
    -   La température en degré
    -   Une icône représentant la météo actuelle
    -   Une icône permettant de visualiser la carte de la ville avec « google map »

#### EXG_IHM_003 :
    Un favori pourra être sélectionné dans la liste pour apparaître au-dessus de la liste de manière évidente et plus voyante.

#### EXG_IHM_004 :
    Le favori pourra être retiré pour laisser la place au favori par défaut. Le favori par défaut sera l'endroit où se situe le téléphone si le GPS est activé.

#### EXG_IHM_005 :
    Un « clique » sur l'icône de visualisation de la carte d'un élément ville permettra de lancer une activité avec une carte « google map » visualisant la ville.

#### EXG_IHM_006 
    : La météo et la température dans chaque élément de la liste et dans le favori doivent pouvoir être mis à jour automatiquement toutes les 5 minutes si la connexion réseau est disponible.

## Exigences techniques 
### EXG_TECH_001 : 
    Le système de persistance utilisé pour les villes sera SQLLite

### EXG_TECH_002: 
    Le système de persistance utilisé pour la ville par défaut sera les préférences partagées.

### EXG_TECH_003: 
    La bibliothèque Volley sera utilisée pour la communication avec l'API Météo EXG_TECH_004 : L'API météo sera l'api OpenWeather

## Travail à faire :
Concevoir et développer l'application pour le lundi 31 mai 2021\. On pourra utiliser les liens suivants :
    - https://icon-icons.com/fr/recherche/icones/meteo\
    - https://openweathermap.org/current


# maquette 

  <img width="776" alt="Capture d’écran 2021-05-10 à 21 51 17" src="https://user-images.githubusercontent.com/34074097/117716998-88244f80-b1da-11eb-8a7c-611d655b4b24.png">

