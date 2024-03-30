import com.miniprojet.messaging.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class MessagingClient2 {
    private final ManagedChannel channel; // canal gRPC géré
    private final MessagingServiceGrpc.MessagingServiceBlockingStub blockingStub; // stub gRPC pour les appels bloquants
    private final String clientId; 

    public MessagingClient2(String host, int port, String clientId) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(), clientId); // appel du deuxième constructeur
    }

    // deuxième constructeur de la classe MessagingClient1
    
    public MessagingClient2(ManagedChannelBuilder<?> channelBuilder, String clientId) {
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
        String clientId2 = "client_Y"; 
        MessagingClient2 client2 = new MessagingClient2("localhost", 7894, clientId2);
        try {
        	
            // démarrage d'un thread pour envoyer des messages
            Thread sendMessageThread = new Thread(() -> {
                client2.sendMessage("client_X", "Bonjour je suis : "+ clientId2);
            });
            sendMessageThread.start(); 

            // démarrage d'un thread pour récupérer les messages
            Thread retrieveMessagesThread = new Thread(() -> {
                client2.retrieveMessages();
            });
            retrieveMessagesThread.start(); 
            // attente de la fin des threads
            sendMessageThread.join();
            retrieveMessagesThread.join();
        } finally {
            client2.shutdown(); // arrêt du client
        }
    }
}
