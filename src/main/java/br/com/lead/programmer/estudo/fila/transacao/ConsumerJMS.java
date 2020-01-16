package br.com.lead.programmer.estudo.fila.transacao;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumerJMS {

    private InitialContext context;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;

    public ConsumerJMS() {
        try {
            this.context = new InitialContext();
            this.factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            this.connection = factory.createConnection();
            this.connection.start();
            //Session cuida da transação das mensagens
            this.session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void recebeMensagem() throws NamingException, JMSException {
        Destination fila = (Destination) this.context.lookup("financeiro");
        MessageConsumer consumer = this.session.createConsumer(fila);
        
        consumer.setMessageListener(new MessageListener(){
        
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Recebemos a mensagem: " + textMessage.getText());
                    session.commit();
                } catch (JMSException e) {
                    session.rollback();
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
        new ConsumerJMS().recebeMensagem();
    }
    
}