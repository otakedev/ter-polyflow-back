import { Drash } from "https://deno.land/x/drash/mod.ts";
import {HomeResource} from "./routes/index.ts";

const server = new Drash.Http.Server({
  response_output: "application/json",
  resources: [HomeResource]
});

const port = 4000;

server.run({
  hostname: "127.0.0.1",
  port: port
});

console.log("Run at the port " + port)