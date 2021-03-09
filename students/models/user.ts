export default class User {
  email?: string;
  firstname?: string;
  id?: number;
  lastname?: string;
  profile_pic_url?: string;

  constructor(object: any) {
		Object.assign(this, object)
	}
}
