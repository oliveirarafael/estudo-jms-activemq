package br.com.lead.programmer.estudo;

import java.util.Scanner;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;

public class ConsumerJMS {

    private ConexaoJMS conexaoJMS;

    public ConsumerJMS(ConexaoJMS conexaoJMS) {
        this.conexaoJMS = conexaoJMS;
    }

    public void recebeMensagem() throws NamingException, JMSException {
        Destination fila = (Destination) this.conexaoJMS.getContext().lookup("financeiro");
        MessageConsumer consumer = this.conexaoJMS.getSession().createConsumer(fila);
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

        new Scanner(System.in).nextLine();
        
        this.conexaoJMS.getSession().close();
        this.conexaoJMS.getConnection().close();
        this.conexaoJMS.getContext().close();
    }

    public static void main(String[] args) throws NamingException, JMSException {
        new ConsumerJMS(new ConexaoJMS()).recebeMensagem();
    }
    
}