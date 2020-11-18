import { faker } from "https://raw.githubusercontent.com/jackfiszr/deno-faker/master/mod.ts";

import { Client } from "https://deno.land/x/mysql/mod.ts";
const client = await new Client().connect({
  hostname: "127.0.0.1",
  username: "root",
  db: "polyflowdev",
  password: "root",
});

await client.execute(`SELECT CONCAT('TRUNCATE TABLE ',TABLE_NAME,';') AS truncateCommand FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'polyflowdev';`);

for(let i=0; i<30; i++) {
    await client.execute(`INSERT INTO user(id, email, firstname, lastname, profile_pic_url) values(?, ?, ?, ?, ?)`, [
        i,
        faker.internet.email(),
        faker.name.firstName(),
        faker.name.lastName(),
        faker.image.imageUrl()
    ]);
    await client.execute(`INSERT INTO student(id, age, current_year, gender) values(?, ?, ?, ?)`, [
        i,
        faker.random.number()%10 + 20,
        "SI3",
        ["F", 'M'][faker.random.number()%2]
    ]);
}

await client.execute(`INSERT INTO user(id, email, firstname, lastname, profile_pic_url) values(?, ?, ?, ?, ?)`, [
    30,
    faker.internet.email(),
    faker.name.firstName(),
    faker.name.lastName(),
    faker.image.imageUrl()
]);
await client.execute(`INSERT INTO administrator(id, occupation) values(?, ?)`, [
    30,
    faker.lorem.word()
]);


for(let i=0; i<100; i++) {
    await client.execute(`INSERT INTO workflow_details(id, description) values(?, ?)`, [
        i, 
        faker.lorem.sentences(),
    ]);
    await client.execute(`INSERT INTO workflow(id, creation_date, deadline_date, subject, title, author_id, workflow_details_id, student_id) values(?, ?, ?, ?, ?, ?, ?, ?)`, [
        i, 
        faker.date.past(),
        faker.date.future(),
        faker.lorem.sentences(),
        faker.lorem.sentences(),
        30,
        i,
        faker.random.number()%30
    ]);
    for(let j=0; j<10; j++) {
        const id = 10*i+j;
        await client.execute(`INSERT INTO workflow_step(id, description, external_link, status, title, workflow_details_id) values(?, ?, ?, ?, ?)`, [
            id,
            faker.lorem.sentences(),
            faker.internet.url(),
            faker.random.number()%3,
            faker.lorem.sentences(),
            i
        ]);
    }
}