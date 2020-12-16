import { Drash } from 'https://deno.land/x/drash@v1.3.1/mod.ts';
import { readCSVObjects } from "https://deno.land/x/csv/mod.ts";
import MapperStudent from "../mapper/studentMapper.ts";
import MapperUser from "../mapper/userMapper.ts";
import Student from "../models/student.ts";
import User from "../models/user.ts";
import { v4 } from "https://deno.land/std@0.81.0/uuid/mod.ts";

export default class StudentConntroller extends Drash.Http.Resource {
	static paths = ['/student'];
	public async POST() {
		const file = this.request.getBodyFile("file");
		if (!file || !file.content) {
			throw new Drash.Exceptions.HttpException(
				400,
				'This resource requires files to be uploaded via the request body.'
			);
        }
        this.response.headers.set("Content-Type", "application/json");
		const filename = "./tmp_"+ v4.generate() +".csv"
		Deno.writeFileSync(filename, file.content);
		const f = await Deno.open(filename);
		const users = []
		for await (const obj of readCSVObjects(f, {columnSeparator: ";"})) {
			const mu = new MapperUser();
			const ms = new MapperStudent();
			const user: User|null = mu.map(obj);
			const student: Student|null = ms.map(obj);
			const u = { ...user, ...student }
			users.push(u);
		}
		Deno.remove(filename);
		this.response.body = users;
		return this.response;
	}
}
