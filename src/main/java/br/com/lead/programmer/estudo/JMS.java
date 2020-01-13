package br.com.lead.programmer.estudo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMS 
{
     
    @SuppressWarnings("resource")
    public static void main( String[] args ) throws NamingException, JMSException
    {
        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();
        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");
        MessageConsumer consumer = session.createConsumer(fila);

        Message message = consumer.receive();
        System.out.println("Recebemos a mensagem: "+message);
        
        session.close();
        connection.close();
        context.close();
    }
}
