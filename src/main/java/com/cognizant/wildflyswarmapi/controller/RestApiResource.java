package com.cognizant.wildflyswarmapi.controller;

import com.cognizant.wildflyswarmapi.model.User;
import com.cognizant.wildflyswarmapi.service.UserService;
import com.cognizant.wildflyswarmapi.service.UserServiceImpl;
import com.cognizant.wildflyswarmapi.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Koneru on 7/6/17.
 */
@Path("/api")
public class RestApiResource{
    public static final Logger logger = LoggerFactory.getLogger(RestApiResource.class);

    @Autowired
    UserService userService = new UserServiceImpl(); //Service which will do all data retrieval/manipulation work

    // -------------------Retrieve All Users---------------------------------------------
    @GET
    @Path("/user/")
    @Produces("application/json")
    //@RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    // -------------------Retrieve Single User------------------------------------------
    @GET
    @Path("/user/{id}")
    @Produces("application/json")
    @Consumes("*/*")
    //@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Response getUser(@PathParam("id") long id) {
        logger.info("Fetching User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return Response.status(200).entity(new CustomErrorType("User with id " + id
                    + " not found")).build();
        }
        return Response.ok(user).build();
    }

    // -------------------Create a User-------------------------------------------
    @POST
    @Path("/user/")
    @Produces("application/json")
    @Consumes("application/json")
    //@RequestMapping(value = "/user/", method = RequestMethod.POST)
    public Response createUser(User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);

        if (userService.isUserExist(user)) {
            logger.error("Unable to create. A User with name {} already exist", user.getName());
            return Response.status(200).entity(new CustomErrorType("Unable to create. A User with name " +
                    user.getName() + " already exist.")).build();
        }
        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
        return Response.ok(headers).build();
        //return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------------- Update a User ------------------------------------------------
    @PUT
    @Path("/user/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateUser(@PathParam("id") long id, User user) {
        logger.info("Updating User with id {}", id);

        User currentUser = userService.findById(id);

        if (currentUser == null) {
            logger.error("Unable to update. User with id {} not found.", id);
            return Response.status(200).entity(new CustomErrorType("Unable to upate. User with id " + id + " not found.")).build();
        }

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());

        userService.updateUser(currentUser);
        return Response.ok(currentUser).build();
    }

    // ------------------- Delete a User-----------------------------------------
    @DELETE
    @Path("/user/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    //@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public Response deleteUser(@PathParam("id") long id) {
        logger.info("Fetching & Deleting User with id {}", id);

        User user = userService.findById(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return Response.status(200).entity(new CustomErrorType("Unable to delete. User with id " + id + " not found.")).build();
        }
        userService.deleteUserById(id);
        return Response.noContent().build();
    }

    // ------------------- Delete All Users-----------------------------
    @DELETE
    @Path("/user/")
    @Produces("application/json")
    @Consumes("application/json")
   // @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public Response deleteAllUsers() {
        logger.info("Deleting All Users");

        userService.deleteAllUsers();
        return Response.noContent().build();
    }
}
