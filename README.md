# Equipe 1 
# Conception d'un systeme de prediction de donnees sur l'usage du BIXI

# Utilisation

## Docker-Compose
1.Installation de Docker
Faire seulement l'etape SET UP THE REPOSITORY a
```
https://docs.docker.com/engine/install/ubuntu/
```
2.Installation de Docker-Compose
Faire seulement l'etape Install Compose on Linux systems a 
```
https://docs.docker.com/compose/install/
```
```
inf3995/backend
```
```
docker-compose up -d --build
```
Cette commande permet de faire executer les conteneurs Docker du serveur web central et des trois engins de donnees. Ainsi, l'application Android et PC peuvent fonctionner.

## Application Android

L'application Android permet de visualiser les donnees, les stations sur une carte, les predictions de donnees sur l'usage du BIXI. Le fichier APK est une application cree pour Android, le systeme d'exploitation mobile de Google. Il est possible de l'utiliser en telechargeant l'APK sur un appareil mobile Android. Ainsi, celui-ci se retrouve :

```
inf3995/Application_Android/release/bixi.apk
```

## Application PC

L'application PC est concu seulement pour l'administrateur. Donc, l'authentification de celui-ci est requis pour pouvoir l'utiliser. L'application se retrouve dans:

```
inf3995/Application_PC/release/bixiAT.exe
```

NOTE: L'administrateur doit avoir enregistrer son adresse IP au pres des developpeurs pour pouvoir acceder aux logs. Veuillez contacter un membre de l'equipe pour plus d'information.

