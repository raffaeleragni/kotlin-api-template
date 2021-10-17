package app.samples.storage

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DBValue(
  @Id
  val id: String,
  val name: String
)

@Repository
interface DBStorage: CrudRepository<DBValue, String>
