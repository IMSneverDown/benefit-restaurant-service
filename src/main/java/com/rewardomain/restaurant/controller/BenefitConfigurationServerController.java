package com.rewardomain.restaurant.controller;
import com.rewardomain.restaurant.bean.Restaurant;
import com.rewardomain.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/benefit-restaurant/merchants")
public class BenefitConfigurationServerController {

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/{merchant_number}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable long merchant_number) {

        Restaurant restaurant = repository.findByNumber(merchant_number);
        String port = environment.getProperty("local.server.port");
        HttpStatusCode httpStatusCode;

        if (restaurant == null) {
            restaurant = new Restaurant();
            httpStatusCode = HttpStatus.valueOf(404);
        }
        else{
            restaurant.setExecutionChain("restaurant-service instance: "+port);
            httpStatusCode = HttpStatus.valueOf(200);
        }
        return new ResponseEntity<>(restaurant, httpStatusCode);
    }


    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> addRestaurant(@RequestBody Restaurant restaurant) {
        repository.save(restaurant);
        Map<String, Object> response = new HashMap<>();
        response.put("status_code", 201);
        response.put("message", "Restaurant created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = repository.findAll();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PutMapping("/{merchant_number}/{availability}")
    public ResponseEntity<String> updateRestaurantAvailability(@PathVariable long merchant_number, @PathVariable String availability) {

        Restaurant restaurant = repository.findByNumber(merchant_number);
        if (restaurant == null) {
            return new ResponseEntity<>("Restaurant not found.", HttpStatus.NOT_FOUND);
        }
        restaurant.setAvailability(availability);
        repository.save(restaurant);

        return new ResponseEntity<>("Benefit availability updated.", HttpStatus.OK);
    }

}
