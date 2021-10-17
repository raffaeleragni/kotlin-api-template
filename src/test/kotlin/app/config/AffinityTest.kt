package app.config

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.core.type.AnnotatedTypeMetadata

class AffinityTest {
  @Test
  fun `no affinity`() {
    val affinity = DeploymentAffinityCondition()
    val context = mock<ConditionContext>()
    val metadata = mock<AnnotatedTypeMetadata> {
      on { getAnnotationAttributes(DeploymentAffinity::class.java.name) } doReturn null
    }

    val result = affinity.matches(context, metadata);

    assertThat("No affinity annotation returns true", result, `is`(true));
  }

  @ParameterizedTest
  @CsvSource(
    "QUEUE",
    "API",
    "NONE"
  )
  fun `queue affinity`(case: Affinity) {
    val map = mapOf("value" to case)

    val affinity = DeploymentAffinityCondition()
    val environment = mock<Environment> {
      on { acceptsProfiles(any<Profiles>()) } doReturn true
    }
    val context = mock<ConditionContext> {
      on { getEnvironment() } doReturn environment
    }
    val metadata = mock<AnnotatedTypeMetadata> {
      on { getAnnotationAttributes(DeploymentAffinity::class.java.name) } doReturn map
    }

    var result = affinity.matches(context, metadata);

    assertThat("QUEUE returns true", result, `is`(true));
  }
}
