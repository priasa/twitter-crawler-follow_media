package id.smarta.krakatau.streamer.util;

import java.io.Serializable;
import java.util.Date;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

/**
 * 
 * @author ARDI PRIASA
 *
 */
@Component
public class DefaultMessageProducer {

	private JmsTemplate jmsTemplate;
	private Destination replyTo;
	private String jmsType;

	public Message sendReceive(String session, String destination, Object message) throws JMSException {

		final String jmsId = session;
		getJmsTemplate().convertAndSend(destination, message, new MessagePostProcessor() {

			public Message postProcessMessage(Message message) throws JMSException {
				message.setJMSCorrelationID(jmsId);
				message.setJMSReplyTo(replyTo);
				message.setJMSType(getJmsType());
				message.setJMSTimestamp(new Date().getTime());
				return message;
			}
		});
		return getJmsTemplate().receiveSelected(replyTo, "JMSCorrelationID = " + "'" + session + "'");
	}

	public Message sendReceive(String session, Object message) throws JMSException {

		final String jmsId = session;
		getJmsTemplate().convertAndSend(message, new MessagePostProcessor() {

			public Message postProcessMessage(Message message) throws JMSException {
				message.setJMSCorrelationID(jmsId);
				message.setJMSReplyTo(replyTo);
				message.setJMSType(getJmsType());
				message.setJMSTimestamp(new Date().getTime());
				return message;
			}
		});
		return getJmsTemplate().receiveSelected(replyTo, "JMSCorrelationID = " + "'" + session + "'");
	}

	public Message sendReceive(String session, Object message, String jmsTypeRequest) throws JMSException {

		final String jmsId = session;
		final String jmsType = jmsTypeRequest;
		getJmsTemplate().convertAndSend(message, new MessagePostProcessor() {

			public Message postProcessMessage(Message message) throws JMSException {
				message.setJMSCorrelationID(jmsId);
				message.setJMSReplyTo(replyTo);
				message.setJMSType(jmsType);
				message.setJMSTimestamp(new Date().getTime());
				return message;
			}
		});
		return getJmsTemplate().receiveSelected(replyTo, "JMSCorrelationID = " + "'" + session + "'");
	}

	public void sendNoReply(Destination destination, final String jmsCorrelationId, final Object objectMessage) {
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject((Serializable) objectMessage);
				message.setJMSCorrelationID(jmsCorrelationId);
				return message;
			}
		});

	}

	public void sendNoReply(Destination destination, final String jmsCorrelationId, final Object objectMessage,
			final String jmsType) {
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject((Serializable) objectMessage);
				message.setJMSCorrelationID(jmsCorrelationId);
				message.setJMSType(jmsType);
				return message;
			}
		});

	}

	public void sendNoReply(final String jmsCorrelationId, final Object message, final String jmsType) {
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject((Serializable) message);
				message.setJMSCorrelationID(jmsCorrelationId);
				message.setJMSType(jmsType);
				return message;
			}
		});

	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Destination replyTo) {
		this.replyTo = replyTo;
	}

	public String getJmsType() {
		return jmsType;
	}

	public void setJmsType(String jmsType) {
		this.jmsType = jmsType;
	}

}
