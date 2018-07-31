package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val baseUrl = "http://localhost:8080"
  val concurrentUsers = 300

  val httpConf = http.baseURL(baseUrl)

  val sampleRequest = repeat(100) {
    exec(http("sample-request")
      .get("/api/sample"))
      .pause(1 second, 2 seconds)
  }

  val scn = scenario("Sample")
    .exec(sampleRequest)

  setUp(scn.inject(rampUsers(concurrentUsers).over(30 seconds)).protocols(httpConf))
}
