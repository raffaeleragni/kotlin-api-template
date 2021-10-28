package app.samples.messaging

import app.config.Affinity
import app.config.DeploymentAffinity
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@DeploymentAffinity(Affinity.QUEUE)
class MessageRouter(
  val sns: NotificationMessagingTemplate,
  @Value("\${notifications.sample.name}")
  val destination: String
) {

  @SqsListener(value = ["\${queues.sample.name}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  fun process(message: String) {
    if (message.isNotBlank())
      sns.sendNotification(message, destination)
  }
}
