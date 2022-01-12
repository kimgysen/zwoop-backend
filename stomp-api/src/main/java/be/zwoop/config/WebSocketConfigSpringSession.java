package be.zwoop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfigSpringSession extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

	@Value("${spring.rabbitmq.host}")
	private String relayHost;

	@Value("${spring.rabbitmq.port}")
	private Integer relayPort;

	@Value("${spring.rabbitmq.username}")
	private String relayUsername;

	@Value("${spring.rabbitmq.password}")
	private String relayPassword;


	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint("/ws")
				.setAllowedOrigins("*");
	}

	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/exchange/", "/topic/")
				.setAutoStartup(true)
				.setUserDestinationBroadcast("/topic/unresolved.user.dest")
				.setUserRegistryBroadcast("/topic/registry.broadcast")
				.setRelayHost(relayHost)
				.setRelayPort(relayPort)
				.setSystemLogin(relayUsername)
				.setSystemPasscode(relayPassword)
				.setClientLogin(relayUsername)
				.setClientPasscode(relayPassword);

		registry.setApplicationDestinationPrefixes("/chatroom");
	}
}
