package epice.collectif;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class Chatbot {

    private SlackSession session;
    private final String CHANNEL = "bot";
    private Properties prop;
    private URL buildUrl;
    private boolean isStarted;


    public Chatbot(String buildUrlStr){

        try {
            buildUrl = new URL(buildUrlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void connect(String token){

        session = SlackSessionFactory.createWebSocketSlackSession(token);
        try {
            session.connect();
        } catch (IOException e) {
            e.printStackTrace();
            isStarted = false;
        }
        isStarted = true;
    }

    public void listen() throws IOException {



        session.addMessagePostedListener((event, session) -> {

            SlackChannel channel = session.findChannelByName(CHANNEL);

            if (!channel.getId().equals(event.getChannel().getId())) {
                return;
            }

            String messageContent = event.getMessageContent();
            if (messageContent.startsWith("build") && messageContent.contains("chatbot")) {

                HttpURLConnection connection = null;
                int responseCode = 0;
                try {
                    connection = (HttpURLConnection) buildUrl.openConnection();
                    responseCode = connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("\nSending 'GET' request to URL : " + buildUrl);
                System.out.println("Response Code : " + responseCode);

                session.sendMessage(event.getChannel(),"Jenkins job '(BUILD) chatbot' is started");
            }
            else if (messageContent.startsWith("build") && messageContent.contains("core")) {
                session.sendMessage(event.getChannel(),"/build-core");
                session.sendMessage(event.getChannel(),"Jenkins job '(BUILD) core-component' is started");
            }
            else
                return;
        });
    }

    public SlackSession getSession() {
        return session;
    }
}
