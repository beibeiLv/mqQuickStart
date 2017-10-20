package mqquickystart;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
  
//Queue（点对点）方式  消费这Consumer  
public class QueueConsumer {  
    private static String user = ActiveMQConnection.DEFAULT_USER;  
    private static String password =ActiveMQConnection.DEFAULT_PASSWORD;  
    private static String url = "tcp://10.64.222.205:61616";  
    public static void main(String[] args) throws Exception{  
        // ConnectionFactory ：连接工厂，JMS 用它创建连接  
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user,password,url);  
        // Connection ：JMS 客户端到JMS Provider 的连接  
        Connection connection = connectionFactory.createConnection();  
        connection.start();  
        // Session： 一个发送或接收消息的线程  
        final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);  
        // Destination ：消息的目的地;消息发送给谁.  
        Queue destination=session.createQueue("example.A");  
        // 消费者，消息接收者  
        MessageConsumer consumer = session.createConsumer(destination);  
        consumer.setMessageListener(new MessageListener(){//有事务限制  
            public void onMessage(Message message) {  
                try {  
                    TextMessage textMessage=(TextMessage)message;  
                    System.out.println(textMessage.getText());  
                } catch (JMSException e1) {  
                    e1.printStackTrace();  
                }  
                try {  
                    session.commit();  
                } catch (JMSException e) {  
                    e.printStackTrace();  
                }  
            }  
        });  
          
/*  另外一种接受方式 
 *    while (true) { 
              //设置接收者接收消息的时间，为了便于测试，这里谁定为100s 
              TextMessage message = (TextMessage) consumer.receive(100000); 
              if (null != message) { 
                  System.out.println("收到消息" + message.getText()); 
              } else { 
                  break; 
              } 
          }*/  
    }  
}  
