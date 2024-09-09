package io.github.xpakx.images_integration.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service
public class CustomMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        if (object instanceof String) {
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        }
        return new Message(((String) object).getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return new String(message.getBody());
    }
}