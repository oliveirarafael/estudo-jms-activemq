package br.com.lead.programmer.estudo.topico;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumerEstoqueJMS {

    private InitialContext context;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;

    public ConsumerEstoqueJMS() {
        try {
            context = new InitialContext();
            factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connection = factory.createConnection();
            connection.setClientID("Estoque");
            connection.start();
            //Session cuida da transação das mensagens
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void recebeMensagem() throws NamingException, JMSException {
        Topic topico = (Topic) this.context.lookup("loja");
        MessageConsumer consumer = this.session.createDurableSubscriber(topico, "assinatura");
        consumer.setMessageListener(new MessageListener(){
        
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Recebemos a mensagem: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });   

        System.out.println("Conectado...");
        
        //this.session.close();
        //this.connection.close();
        //this.context.close();
    }

    public static void main(String[] args) throws NamingException, JMSException {
        new ConsumerEstoqueJMS().recebeMensagem();
    }
    
}