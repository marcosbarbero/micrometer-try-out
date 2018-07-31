Sample Tryout Micrometer App
---

It's just a toy spring boot tryout micrometer application where I'm trying to find the request
throughput and latency.

### Tech Stack

  * Java
  * Maven
  * Spring Boot 2
  * Spring Boot Actuator
  * Micrometer
  * Prometheus
  * Grafana
  * Gatling
  * Docker  

### Design Overview

The application is configured to export `histogram` metrics by `micrometer` where Prometheus keep listening to
`/actuator/prometheus` endpoint and Grafana uses Prometheus as datasource, with that information we can create
dashboards to check the application's throughput and latency.

>Sorry there's image for now

Spring Boot &rarr; Micrometer &rarr; Prometheus &rarr; Grafana


### Build & Run

#### Spring Boot App

To run the sample app just run the following command:

```bash
$ ./mvnw spring-boot:run
```

The app will be available at `localhost:8080` and it contains only a single endpoint `/api/sample` in which
returns only a random `uuid`.

Sample request:
```bash
$ curl -i localhost:8080/api/sample

HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 36
Date: Tue, 31 Jul 2018 09:48:11 GMT

66a20e40-2795-4666-b470-634b8768da3b
```

#### Prometheus

For this example I'm running Prometheus using docker, if you are going to do the same just edit 
the file `prometheus.yml` adding your own `IP` address, or the `IP` where you are running the
app.

**IMPORTANT**: *It's mandatory to change the IP in the following configuration otherwise Prometheus can not
pull the data from the application.*

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'spring'
    scrape_interval: 5s
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['192.168.178.143:8080'] #Provide your IP here
```

If you are running everything locally you can use `localhost` as `IP`.

#### Grafana

As for Prometheus I'm running Grafana using docker and here you can find two file `grafana-datasource.yml` 
and `grafana-dashboard-yml`.

The `grafana-datasource.yml` configures Prometheus as datasource, you can also do it manually in case you have a 
standalone Grafana instance running.

The `grafana-dashboard.yml` contains the configuration pointing to the location where the dashboards are stored.

#### Run Prometheus & Grafana (Docker)

Everything related to Prometheus and Grafana is properly configured using Docker Compose, so just run:

```bash
$ docker-compose up -d
```

Once it's finished you'll be able to access the Grafana panel at http://localhost:3000, for the first login you might
need to configure and username and password, the default value for both is `admin`.

### Load Test - Gatling 

Now that everything is settle you can just run the Gatling script for a load test using the following command:

```bash
$ mvn gatling:test
```

### Reports

#### Throughput

#### Latency