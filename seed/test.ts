import { faker } from "https://raw.githubusercontent.com/jackfiszr/deno-faker/master/mod.ts";

Object.keys(faker.lorem).forEach(func => {
    console.log(func)
    console.log(faker.lorem[func]())
})