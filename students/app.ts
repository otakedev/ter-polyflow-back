import { Drash } from 'https://deno.land/x/drash@v1.3.1/mod.ts';
import StudentController from './controllers/StudentController.ts';

export default class HomeController extends Drash.Http.Resource {
	static paths = ['/'];
	public GET() {
		this.response.headers.set("Content-Type", "application/json");
		this.response.body = [
			{
				age: 10,
				currentYear: 'string',
				email: 'string',
				firstname: 'string',
				gender: 'string',
				id: 10,
				lastname: 'string',
				profilePicUrl: null,
				wish: null,
			},
		];
		return this.response;
	}
}

const server = new Drash.Http.Server({
	response_output: 'text/html',
	resources: [StudentController, HomeController],
});

const host = '127.0.0.1';
const port = 4000;

server.run({
	hostname: host,
	port: port,
});

console.log(`Serveur up at ${host}:${port}`);
