package app.config

import org.springframework.context.annotation.Conditional
import java.lang.annotation.Documented

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Documented
@Conditional(DeploymentAffinityCondition::class)
annotation class DeploymentAffinity(val value: Affinity)
