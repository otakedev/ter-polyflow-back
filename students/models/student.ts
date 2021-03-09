export default class Student {
	age?: number;
	current_year?: string;
	gender?: string;
	id?: number;

	constructor(object: any) {
		Object.assign(this, object)
	}
}
