import com.miniprojet.messaging.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class MessagingClient1 {
    private final ManagedChannel channel; // canal gRPC géré
    private final MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub; // stub gRPC pour les appels bloquants
    private final String clientId; 

    public MessagingClient1(String host, int port, String clientId) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(), clientId); // appel du deuxième constructeur
    }

    // deuxième constructeur de la classe MessagingClient1
    
    public MessagingClient1(ManagedChannelBuilder<?> channelBuilder, String clientId) {
        channel = channelBuilder.build();
        blockingStub = MessagingServiceGrpc.newBlockingStub(channel);
        this.clientId = clientId;
    }

    public void sendMessage(String receiverId, String messageText) {
    	
        // création de la demande de message
        MessageRequest request = MessageRequest.newBuilder()
                .setSenderId(clientId)
                .setReceiverId(receiverId)
                .setMessageText(messageText)
                .build();
        blockingStub.sendMessage(request); // envoi de la demande
        System.out.println("Message envoye par : " + clientId);
    }

    public void retrieveMessages() {
    	
        // création de la demande de récupération de messages pour l'utilisateur actuel
        UserRequest request = UserRequest.newBuilder().setUserId(clientId).build();
        
        // appel à la méthode du serveur pour récupérer les messages
        MessagesResponse response = blockingStub.retrieveMessagesForUser(request);
        
        // affichage des messages reçus
        for (Message message : response.getMessagesList()) {
            System.out.println("Message recu par : " + clientId + ": " + message.getMessageText());
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS); // arrêt du canal gRPC
    }

    public static void main(String[] args) throws Exception {
        String clientId1 = "client_X"; 
        
        MessagingClient1 client1 = new MessagingClient1("localhost", 7894, clientId1);
        try {
        	
            // démarrage d'un thread pour envoyer des messages
            Thread sendMessageThread = new Thread(() -> {
                client1.sendMessage("client_Y", "Bonjour je suis : "+ clientId1);
            });
            sendMessageThread.start(); 

            // démarrage d'un thread pour récupérer les messages
            Thread retrieveMessagesThread = new Thread(() -> {
                client1.retrieveMessages();
            });
            retrieveMessagesThread.start(); 

            // attente de la fin des threads
            sendMessageThread.join();
            retrieveMessagesThread.join();
        } finally {
            client1.shutdown(); 
        }
    }
}
