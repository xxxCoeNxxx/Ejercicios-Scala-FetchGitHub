import sttp.client3._
import sttp.client3.okhttp.OkHttpSyncBackend
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._  // Importación clave para derivación automática

case class GitHubUser(
  login: String,
  id: Long,
  name: Option[String],
  company: Option[String],
  public_repos: Int,
  followers: Int,
  following: Int
)

object GitHubAPIExample {
  def main(args: Array[String]): Unit = {
    val backend = OkHttpSyncBackend()
    val username = "daviniadelarosa"

    val request = basicRequest
      .get(uri"https://api.github.com/users/$username")
      .header("User-Agent", "Scala-GitHub-API-Example")
      .header("Accept", "application/vnd.github.v3+json")

    val response = request.send(backend)
    
    response.body match {
      case Right(json) =>
        parse(json) match {
          case Right(parsedJson) =>
            parsedJson.as[GitHubUser] match {
              case Right(user) =>
                // Corregido: user es accesible dentro de este bloque
                println(s"Datos de ${user.login}:")
                println(s"ID: ${user.id}")
                println(s"Nombre: ${user.name.getOrElse("No disponible")}")
                println(s"Compañía: ${user.company.getOrElse("No disponible")}")
                println(s"Repos públicos: ${user.public_repos}")
                println(s"Seguidores: ${user.followers}")
                println(s"Siguiendo: ${user.following}")
              
              case Left(decodingError) =>  // Renombrado para evitar conflicto
                println(s"Error parseando JSON: $decodingError")
            }
          case Left(parsingError) =>
            println(s"Error en el formato JSON: $parsingError")
        }
      case Left(requestError) =>
        println(s"Error en la solicitud: $requestError")
    }
    
    backend.close()
  }
}

