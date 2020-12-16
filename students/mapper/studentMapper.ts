import Student from '../models/student.ts';
import Mapper from './mapper.ts';

class MapperStudent extends Mapper<Student> {
	map(object: any): Student | null {
		if (
			Mapper.multipleFieldEmpty(object, [
				'Date de naissance',
				'Civilité',
			])
		)
			return null;
		return new Student({
			age: 2020 - parseInt(object['Date de naissance'].split('/')[2]),
			current_year: '',
			gender: object['Civilité'],
		});
	}
}

export default MapperStudent;
