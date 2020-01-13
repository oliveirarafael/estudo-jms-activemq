package br.com.lead.programmer.estudo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

public class ProducerJMS {

    private ConexaoJMS conexaoJMS;

    public ProducerJMS(ConexaoJMS conexaoJMS) {
        this.conexaoJMS = conexaoJMS;
    }

    public void enviaMensagem(String mensagem) throws JMSException, NamingException {
        Destination fila = (Destination) this.conexaoJMS.getContext().lookup("financeiro");
        MessageProducer producer = this.conexaoJMS.getSession().createProducer(fila);

        Message message = this.conexaoJMS.getSession().createTextMessage(mensagem);
        producer.send(message);
    }

    public static void main(String[] args) throws JMSException, NamingException {
        for(int i = 0; i <= 100; i++ ){
          new ProducerJMS(new ConexaoJMS()).enviaMensagem("Mensagem: #"+i);
        }
    }
    
    
}