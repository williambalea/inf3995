# Équipe 1 
# Conception d'un système de prédiction de données sur l'usage du BIXI

# Utilisation

## Git
Pour pouvoir utiliser notre système, ouvrez un terminal en écrivant:
```
git clone https://gitlab.com/dapak/inf3995.git
```

## Docker-Compose
1. Installation de Docker
Faire seulement l'étape: "SET UP THE REPOSITORY" à
```
https://docs.docker.com/engine/install/ubuntu/
```
2. Installation de Docker-Compose
Faire seulement l'étape: "Install Compose on Linux systems" à
```
https://docs.docker.com/compose/install/
```
3. Aller sur le répertoire:
```
inf3995/backend
```
4. Faire la commande suivant pour faire rouler les conteneurs Docker:
```
docker-compose up -d --build
```
Cette commande permet de faire exécuter les conteneurs Docker du serveur web central et des trois engins de donnees. Ainsi, l'application Android et PC peuvent fonctionner.

## Application Android

L'application Android permet de visualiser les données, les stations sur une carte, les prédictions de données sur l'usage du BIXI. Le fichier APK est une application crée pour Android, le système d'exploitation mobile de Google. Il est possible de l'utiliser en telechargeant l'APK sur un appareil mobile Android. Ainsi, celui-ci se retrouve :

```
inf3995/Application_Android/app/release/app-release.apk
```

## Application PC

L'application PC est conçu seulement pour l'administrateur. Donc, l'authentification de celui-ci est requise pour pouvoir l'utiliser. L'application se retrouve dans:

```
inf3995/Application_PC/release/bixiAT.exe
```

NOTE: L'administrateur doit avoir enregistrer son adresse IP au près des développeurs pour pouvoir accéder aux logs. Veuillez contacter un membre de l'equipe pour plus d'information.

