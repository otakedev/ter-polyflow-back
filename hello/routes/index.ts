import { Drash } from 'https://deno.land/x/drash/mod.ts';

export class HomeResource extends Drash.Http.Resource {
	static paths = ['/hello'];
	public GET() {
		this.response.body = "hello world";
		return this.response;
	}
}