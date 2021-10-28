package app.samples.messaging

import io.awspring.cloud.messaging.core.NotificationMessagingTemplate
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class MessageRouterTest {
  @Test
  fun `not routed when empty`() {
    val sns = mock<NotificationMessagingTemplate> {}
    val router = MessageRouter(sns, "destination")

    router.process("")

    verifyNoInteractions(sns)
  }

  @Test
  fun `routed when valid`() {
    val sns = mock<NotificationMessagingTemplate> {}
    val router = MessageRouter(sns, "destination")

    router.process("message")

    verify(sns).sendNotification("message", "destination")

  }
}
