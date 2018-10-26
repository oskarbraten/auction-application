package jms;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.google.gson.JsonObject;

import ejb.AuctionManager;


@MessageDriven(mappedName = "jms/dat250/Topic", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "topicUser = 'dweet'") })
public class DweetListener implements MessageListener {

    @EJB
    AuctionManager auctionManager;

    @Override
    public void onMessage(Message message) {

        try {

            JsonObject json = new JsonObject();
            json.addProperty("user", message.getStringProperty("username"));
            json.addProperty("message", "Purchased a product.");
            json.addProperty("productName", message.getStringProperty("productName"));

            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("user: " + message.getStringProperty("username"));
            logger.info("message: Purchased a product.");
            logger.info("productName: " + message.getStringProperty("productName"));
            logger.info("DTWEET: Sending tweet to dweet...");
            logger.info("DTWEET JSON: " + json);

            try {
                DweetConnection dc = new DweetConnection();
                dc.publish(json);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (JMSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}