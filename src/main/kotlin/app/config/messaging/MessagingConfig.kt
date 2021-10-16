package app.config.messaging

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sqs.AmazonSQSAsync
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import io.awspring.cloud.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingConfig {
  @Bean
  fun notificationMessagingTemplate(aws: AmazonSNS): NotificationMessagingTemplate {
    return NotificationMessagingTemplate(aws);
  }

  @Bean
  fun queueMessagingTemplate(aws: AmazonSQSAsync): QueueMessagingTemplate {
    return QueueMessagingTemplate(aws)
  }

  @Bean
  fun simpleMessageListenerContainerFactory(aws: AmazonSQSAsync): SimpleMessageListenerContainerFactory {
    val factory = SimpleMessageListenerContainerFactory();
    factory.setAmazonSqs(aws)
    factory.setWaitTimeOut(20)
    factory.setMaxNumberOfMessages(10)
    return factory
  }
}
