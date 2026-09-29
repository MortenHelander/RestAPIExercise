
import app.Dog;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7070);

        Map<Integer, Dog> dogs = new HashMap<>();
        Dog dog1 = new Dog(1, "Moe", "Cockerspaniel", 2);
        Dog dog2 = new Dog(2, "Hannibal", "Chiwauwa", 2);
        Dog dog3 = new Dog(3, "Joe", "Labrador", 2);

        dogs.put(dog1.id(), dog1);
        dogs.put(dog2.id(), dog2);
        dogs.put(dog3.id(), dog3);

        //getall
        app.get("/api/v1/dogs", ctx -> {
            ctx.json(dogs.values());
        });

        //get by id
        app.get("/api/v1/dogs/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            Dog dog = dogs.get(id);

            if (dog == null){
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            ctx.json(dog);
        });

        app.post("/api/v1/dogs", ctx-> {
            int id = dogs.size()+1;
            Dog dog = ctx.bodyAsClass(Dog.class);
            if (dog == null){
                ctx.status(HttpStatus.NOT_ACCEPTABLE);
                return;
            }
            Dog goodboy = new Dog(id, dog.name(), dog.breed(), dog.age());
            dogs.put(id, goodboy);
            ctx.json(dog);


        });

        //update
        app.put("/api/v1/dogs/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (!dogs.containsKey(id)){
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            Dog dog = ctx.bodyAsClass(Dog.class);
            if (dog == null){
                ctx.status(HttpStatus.NOT_ACCEPTABLE);
                return;
            }
            dogs.put(id, dog);
            ctx.json(dog);
        });

        //delete
        app.delete("/api/v1/dogs/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (!dogs.containsKey(id)){
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            dogs.remove(id);
            ctx.status(HttpStatus.NO_CONTENT);
        });

    }
}
