package br.com.lead.programmer.estudo.topico.objectmessage;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import br.com.lead.programmer.estudo.modelo.Pedido;

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
            connection.setClientID("Estoque_Selector");
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
        
        //Terceiro parametro representa o uso da mesma conexao para consumir ou produzir as mensagens
        MessageConsumer consumer = this.session.createDurableSubscriber(topico, "assinatura-seletor", "ebook is null OR ebook=false", false);
        consumer.setMessageListener(new MessageListener(){
        
            public void onMessage(Message message) {
                try {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    Pedido pedido = (Pedido) objectMessage.getObject();
                    System.out.println("Recebemos a mensagem: " + pedido.getCodigo());
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