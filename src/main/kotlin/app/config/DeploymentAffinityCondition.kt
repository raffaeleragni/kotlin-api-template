package app.config

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.env.Profiles
import org.springframework.core.type.AnnotatedTypeMetadata

class DeploymentAffinityCondition: Condition {
  override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
    var attrs = metadata.getAnnotationAttributes(DeploymentAffinity::class.java.name) ?: return true
    val affinity = attrs["value"]
      .toString()
      .lowercase()

    return context.environment.acceptsProfiles(Profiles.of(affinity))
  }
}