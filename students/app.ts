import { Drash } from "https://deno.land/x/drash@v1.3.1/mod.ts";
import StudentController from "./controllers/StudentController.ts";

const server = new Drash.Http.Server({
    response_output: "text/html",
    resources: [StudentController]
});

const host = "localhost";
const port = 8080;

server.run({
  hostname: host,
  port: port
});

console.log(`Serveur up at ${host}:${port}`)