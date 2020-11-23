import { faker } from "https://raw.githubusercontent.com/jackfiszr/deno-faker/master/mod.ts";
import * as bcrypt from "https://deno.land/x/bcrypt/mod.ts";

import { Client } from "https://deno.land/x/mysql/mod.ts";
const client = await new Client().connect({
  hostname: "127.0.0.1",
  username: "root",
  db: "polyflow",
  password: "root",
});

await client.execute(`SELECT CONCAT('TRUNCATE TABLE ',TABLE_NAME,';') AS truncateCommand FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'polyflowdev';`);

for(let i=0; i<30; i++) {
    await client.execute(`INSERT INTO user(id, email, firstname, lastname, profile_pic_url, password) values(?, ?, ?, ?, ?, ?)`, [
        i,
        faker.internet.email(),
        faker.name.firstName(),
        faker.name.lastName(),
        faker.image.imageUrl(),
        await bcrypt.hash("123")
    ]);
    await client.execute(`INSERT INTO student(id, age, current_year, gender) values(?, ?, ?, ?)`, [
        i,
        faker.random.number()%10 + 20,
        "SI3",
        ["F", 'M'][faker.random.number()%2]
    ]);
}

await client.execute(`INSERT INTO user(id, email, firstname, lastname, profile_pic_url, password) values(?, ?, ?, ?, ?, ?)`, [
    30,
    "admin@admin.fr",
    faker.name.firstName(),
    faker.name.lastName(),
    faker.image.imageUrl(),
    await bcrypt.hash("123")
]);
await client.execute(`INSERT INTO administrator(id, occupation) values(?, ?)`, [
    30,
    faker.lorem.word()
]);


for(let i=0; i<100; i++) {
    await client.execute(`INSERT INTO workflow_details(id, description) values(?, ?)`, [
        i, 
        faker.lorem.sentence(),
    ]);
    await client.execute(`INSERT INTO workflow(id, creation_date, deadline_date, subject, title, author_id, workflow_details_id, student_id) values(?, ?, ?, ?, ?, ?, ?, ?)`, [
        i, 
        faker.date.past(),
        faker.date.future(),
        faker.lorem.sentence(),
        faker.lorem.sentence(),
        30,
        i,
        faker.random.number()%30
    ]);
    for(let j=0; j<4; j++) {
        const id = 10*i+j;
        await client.execute(`INSERT INTO workflow_step(id, description, external_link, title, workflow_details_id, step_index) values(?, ?, ?, ?, ?, ?)`, [
            id,
            faker.lorem.sentences(),
            faker.internet.url(),
            faker.lorem.sentence(),
            i,
            j
        ]);
    }
    await client.execute(`UPDATE workflow SET workflow_current_step_id = ? WHERE id=?`, [
        10*i,
        i
    ]);
}

Deno.exit(0);