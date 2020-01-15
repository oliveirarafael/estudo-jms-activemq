package br.com.lead.programmer.estudo.topico.objectmessage;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.lead.programmer.estudo.modelo.Pedido;
import br.com.lead.programmer.estudo.modelo.PedidoFactory;



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

    public void enviaMensagem() throws JMSException, NamingException {
        Destination fila = (Destination) this.context.lookup("loja");
        MessageProducer producer = this.session.createProducer(fila);

        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        Message message = this.session.createObjectMessage(pedido);
        producer.send(message);
        this.session.close();
        this.connection.close();
        this.context.close();
    }

    public static void main(String[] args) throws JMSException, NamingException {
          new ProducerJMS().enviaMensagem();
    }
    
}