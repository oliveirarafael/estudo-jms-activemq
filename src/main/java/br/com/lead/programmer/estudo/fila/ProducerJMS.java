package br.com.lead.programmer.estudo.fila;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class ProducerJMS {

    private InitialContext context;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;

    public ProducerJMS() {
        try {
            context = new InitialContext();
            factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connection = factory.createConnection();
            connection.start();
            //Session cuida da transação das mensagens
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void enviaMensagem(String mensagem) throws JMSException, NamingException {
        Destination fila = (Destination) this.context.lookup("financeiro");
        MessageProducer producer = this.session.createProducer(fila);

        Message message = this.session.createTextMessage(mensagem);
        producer.send(message);
    }

    public static void main(String[] args) throws JMSException, NamingException {
        for(int i = 0; i <= 100; i++ ){
          new ProducerJMS().enviaMensagem("Mensagem: #"+i);
        }
    }
    
    
}