# Projet Systèmes Répartis: implémentation de serveurs et clients avec Java RMI, gRPC et Sockets.
Ce projet consiste à développer des serveurs et des clients en utilisant trois technologies de communication distribuée en Java : Java RMI, gRPC et les sockets. L'objectif est de comprendre le fonctionnement et les différences entre ces technologies en mettant en œuvre des fonctionnalités spécifiques dans chaque système.

### Déploiement du projet:

1- ouvrir git bash et naviguer vers le repertoire ou vous voulez cloner la repo

2- executer `git clone https://github.com/jazz-codes/Projet_Systemes_repartis.git`

## Service de Gestion d'une liste de taches en Java RMI

Le workflow de cette application consiste à démarrer un serveur RMI (TaskServer) qui crée une instance de l'implémentation du service des tâches (TaskServiceImplFactory), enregistrée dans un registre RMI, puis un client (TaskClient) se connecte au registre RMI pour récupérer l'instance du service des tâches et interagit avec celui-ci via un menu interactif proposant des opérations telles que récupérer la liste des tâches, ajouter une nouvelle tâche, supprimer une tâche existante ou quitter.

### Pour tester cette application 

#### - Côté serveur

ouvrir un nouveau terminal et naviguer vers **Gestion_taches_RMI\Server**

1- compiler tout les fichiers java : `javac *.java`

2- générer les stubs

    rmic -v'1.1' TaskServiceImplFactory
    rmic -v'1.1' TaskServiceImpl
    
3- executer le serveur : `java TaskServer`

#### - Côté client

ouvrir un nouveau terminal et naviguer vers **Gestion_taches_RMI\Client**

1- copier les interfaces nécéssaires au repertoire du client :

    cp ../Server/TaskServiceFactoryInterface.class .
    cp ../Server/TaskServiceInterface.class .

2- copier les stubs générés au repertoire du client :

	cp ../Server/TaskServiceImplFactory_Stub.class .
	cp ../Server/TaskServiceImpl_Stub.class .

3- compiler le client : `javac TaskClient.java`

4- executer le client : `java TaskClient`


## Service de messagerie en GRPC

Le workflow de cette application consiste en un serveur de messagerie gRPC qui écoute sur un port spécifique, permettant à deux clients (client_X et client_Y) de se connecter au serveur, d'envoyer des messages à un destinataire spécifié et de récupérer les messages qui leur sont destinés de manière asynchrone en utilisant des threads distincts pour l'envoi et la récupération de messages.

### Pour tester cette application

_NB: apache-maven doit etre installé sur votre machine_



ouvrir un nouveau terminal et naviguer vers le repetoire **MessagerieGRPC**

1- executer `mvn clean install` pour faire un build from scratch et ecraser le repertoire target existant

2- executer `mvn clean compile`

#### - Côté serveur

3- executer  `mvn exec:java -Dexec.mainClass="MessagingServer"`

#### - Côté client

4- ouvrir deux autres terminaux et naviguer vers le repetoire **MessagerieGRPC**

4.1-  dans le premier terminal executer `mvn exec:java -Dexec.mainClass="MessagingClient1"` 

4.2-  dans le deuxième terminal executer `mvn exec:java -Dexec.mainClass="MessagingClient2"`

4.3- vous pouvez réxécuter le client 1 pour voir les messages qui'il a reçu apres son arret

## Service de Chat implémentant les Sockets

Le workflow de cette application de chat consiste en la création d'un serveur qui écoute sur un port spécifique et qui gère les connexions des clients, puis chaque client se connecte au serveur et peut envoyer des messages qui seront diffusés à tous les autres clients connectés.

### Pour tester cette application

ouvrir un nouveau terminal et naviguer vers le repetoire **Service_de_Chat_Sockets**

1- compiler tout les fichiers java : `javac *.java`
#### - Côté serveur

2- executer le serveur : `java ChatServer`

#### - Côté clients

3- ouvrir un ou plusieurs autres terminaux et naviguer vers le repetoire **Service_de_Chat_Sockets**

4- executer le(s) client(s) : `java ChatClient`




 







  
