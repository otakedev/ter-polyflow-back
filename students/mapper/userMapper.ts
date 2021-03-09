import User from '../models/user.ts';
import Mapper from './mapper.ts';

class MapperUser extends Mapper<User> {
	map(object: any): User | null {
		if (
			Mapper.multipleFieldEmpty(object, [
				'Mail',
				'Prénom',
				'Nom',
			])
		)
			return null;
		return new User({
			email: object['Mail'],
			firstname: object['Prénom'],
            lastname: object['Nom'],
		});
	}
}

export default MapperUser;
