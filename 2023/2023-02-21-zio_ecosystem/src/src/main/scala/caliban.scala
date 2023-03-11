import caliban.*
import caliban.schema.Annotations.GQLDescription
import zhttp.http.*
import zhttp.service.Server
import zio.*

enum Position:
  case Organizer
  case Speaker
  case Viewer

case class Participant(name: String, position: Position)

case class ParticipantsArgs(position: Position)
case class ParticipantArgs(name: String)

case class Queries(
    @GQLDescription("Get all participants in a specified position")
    participants: ParticipantsArgs => List[Participant],
    @GQLDescription("Find a participant by name")
    participant: ParticipantArgs => Option[Participant]
)

object CalibanTest extends ZIOAppDefault:

  val participants = List(
    Participant("Jiri", Position.Speaker),
    Participant("Walter", Position.Organizer)
  )

  def run =
    for
      interpreter <- GraphQL
        .graphQL(
          RootResolver(
            Queries(
              args => participants.filter(_.position == args.position),
              args => participants.find(_.name == args.name)
            )
          )
        )
        .interpreter
      _ <- Server
        .start(
          8088,
          Http.collectHttp[Request] { case _ -> !! / "graphql" =>
            ZHttpAdapter.makeHttpService(interpreter)
          }
        )
    yield ()

// curl -q --data-binary '{ "query": "query { participants(position: Speaker) { name, position } }" }' localhost:8088/graphql | jq

// curl -q --data-binary '{ "query": "query { participant(name: \"Walter\") { name, position } }" }' localhost:8088/graphql | jq
