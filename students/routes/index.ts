import { Drash } from 'https://deno.land/x/drash/mod.ts';
import students from "../seed/students.ts";

export class HomeResource extends Drash.Http.Resource {
	static paths = ['/student'];
	public GET() {
		this.response.body = students;
		return this.response;
	}
}