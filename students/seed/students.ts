import { faker } from "https://raw.githubusercontent.com/jackfiszr/deno-faker/master/mod.ts";
import Application from "../models/Application.ts";
import Student from "../models/Student.ts";

const students: Student[] = [];

for(let i = 0; i<100; i++) {
    const applications: Application[] = []
    applications.push(new Application(faker.name.jobTitle()))
    students.push(new Student(faker.random.uuid(), faker.name.firstName(), faker.name.lastName(), applications))
}

export default students;