import Application from "./Application.ts";

export default class Student {

    private studentNumber: string;
    private firstname: string;
    private lastname: string;

    private applications: Application[];

    constructor(studentNumber: string, firstname: string, lastname: string, applications: Application[]) {
        this.studentNumber = studentNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        this.applications = applications;
    }

}