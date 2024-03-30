import com.miniprojet.messaging.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagingServer {
    private final int port; 
    private final Server server; // instance du serveur gRPC
    private final List<Message> userMessages;

    public MessagingServer(int port) throws IOException {
        this(ServerBuilder.forPort(port), port); // appel du deuxième constructeur
    }

    // deuxième constructeur de la classe MessagingServer prenant un ServerBuilder
    public MessagingServer(ServerBuilder<?> serverBuilder, int port) {
        this.port = port;
        this.server = serverBuilder.addService(new MessagingServiceImpl()).build();
        this.userMessages = new ArrayList<>();
    }

    
    public void start() throws IOException {
        server.start(); 
        System.out.println("serveur en ecoute sur le port " + port); 
        // ajout d'un hook pour arrêter le serveur lors de l'arrêt de la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** arret du serveur gRPC suite a l'arret de la JVM ");
            MessagingServer.this.stop();
            System.err.println("*** le serveur s'est arrete");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown(); 
        }
    }

    // classe interne implémentant les services de messagerie
    
    
    private class MessagingServiceImpl extends MessagingServiceGrpc.MessagingServiceImplBase {
        // AtomicInteger pour garantir l'incrémentation sécurisée d'un compteur
        private AtomicInteger messageIdCounter = new AtomicInteger(1); 

        
        // implémentation de l'envoi de message
        @Override
        public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
            
        	// création d'un message avec un identifiant unique
            Message message = Message.newBuilder()
                    .setMessageId(String.valueOf(messageIdCounter.getAndIncrement()))
                    .setSenderId(request.getSenderId())
                    .setReceiverId(request.getReceiverId())
                    .setMessageText(request.getMessageText())
                    .build();
            
            // ajout du message à la liste des messages d'utilisateur
            synchronized (userMessages) {
                userMessages.add(message);
            }
            // création d'une réponse avec l'identifiant du message
            MessageResponse response = MessageResponse.newBuilder().setMessageId(message.getMessageId()).build();
            
            responseObserver.onNext(response); // envoi de la réponse
            responseObserver.onCompleted(); // indication de la fin de l'envoi
        }
        
        

        // implémentation de la récupération des messages pour un utilisateur
        @Override
        public void retrieveMessagesForUser(UserRequest request, StreamObserver<MessagesResponse> responseObserver) {
        	
            String userId = request.getUserId(); 
            List<Message> userMessagesForId = new ArrayList<>(); 
            
            // parcours des messages d'utilisateur et ajout à la liste s'ils correspondent à l'identifiant donné
            synchronized (userMessages) {
                for (Message message : userMessages) {
                    if (message.getReceiverId().equals(userId)) {
                        userMessagesForId.add(message);
                    }
                }
            }
            
            
            // construction de la réponse avec la liste des messages
            
            MessagesResponse.Builder responseBuilder = MessagesResponse.newBuilder();
            responseBuilder.addAllMessages(userMessagesForId);
            
            responseObserver.onNext(responseBuilder.build()); // envoi de la réponse
            responseObserver.onCompleted(); // indication de la fin de l'envoi
        }
    }

    public static void main(String[] args) throws Exception {
        // création et démarrage du serveur sur le port 7894
        MessagingServer server = new MessagingServer(7894);
        server.start(); 
        server.server.awaitTermination(); // attente de l'arrêt du serveur
    }
}
