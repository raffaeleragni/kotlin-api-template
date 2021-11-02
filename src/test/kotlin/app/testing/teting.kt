package app.testing

val SAMPLE_TOKEN_WITH_ROLES = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJyb2xlcyI6WyJST0xFMSJdfQ.F0A_1bY5zItY27JTsdJz4t3PzRsI5ABrQlF_18G-rUo"
inline fun <reified T, reified A> singleAnnotationOfClass() = T::class.annotations.first { it is A } as A
inline fun <reified T, reified A> singleAnnotationOfMethod(name: String) = T::class.members.first { it.name == name }.annotations.first { it is A } as A
